package com.vi.aws.logging.log4jappenders;

import com.amazonaws.services.logs.AWSLogsClient;
import com.amazonaws.services.logs.model.*;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.appender.AbstractAppender;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.layout.PatternLayout;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.*;

import static com.vi.aws.logging.log4jappenders.Config.*;

/**
 * Created by mihailo.despotovic on 4/8/15.
 */
@Plugin(name = "CloudWatchAppender", category = "Core", elementType = "appender", printObject = true)
public class CloudWatchAppender extends AbstractAppender {

    private final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy.MM.dd HH.mm.ss"); // aws doesn't allow ":" in stream name

    private static final String AWS_INSTANCE_ID; // per-instance, so static
    static { AWS_INSTANCE_ID = retrieveInstanceId(); }

    private final BlockingQueue<InputLogEvent> queue = new LinkedBlockingQueue<>(AWS_LOG_STREAM_MAX_QUEUE_DEPTH);
    private volatile boolean shutdown = false;
    private final int flushPeriodMillis;
    private Thread deliveryThread;
    private final Object monitor = new Object();

    private String sequenceTokenCache = null; // aws doc: "Every PutLogEvents request must include the sequenceToken obtained from the response of the previous request.
    private long lastReportedTimestamp = -1;

    private String logGroupName;
    private String logStreamName;

    private AWSLogsClient awsLogsClient = null;
    private volatile boolean queueFull = false;

    @PluginFactory
    public static CloudWatchAppender createAppender(@PluginAttribute("name") String name,
                                                       @PluginAttribute("awsLogGroupName") String awsLogGroupName,
                                                       @PluginAttribute("awsLogStreamName") String awsLogStreamName,
                                                       @PluginAttribute("awsLogStreamFlushPeriodInSeconds") String awsLogStreamFlushPeriodInSeconds,
                                                       @PluginElement("Layout") Layout<Serializable> layout) {
        return new CloudWatchAppender(
                name == null ? DEFAULT_LOG_APPENDER_NAME : name,
                awsLogGroupName == null ? DEFAULT_AWS_LOG_GROUP_NAME : awsLogGroupName,
                awsLogStreamName,
                awsLogStreamFlushPeriodInSeconds, layout);
    }

    private CloudWatchAppender(final String name,
                               final String awsLogGroupName,
                               final String awsLogStreamName,
                               final String awsLogStreamFlushPeriodInSeconds,
                               final Layout<Serializable> layout) {
        super(name, null, layout == null ? PatternLayout.createDefaultLayout() : layout, false);

        // figure out the flush period
        int flushPeriod = AWS_LOG_STREAM_FLUSH_PERIOD_IN_SECONDS;
        if (awsLogStreamFlushPeriodInSeconds != null) {
            try {
                flushPeriod = Integer.parseInt(awsLogStreamFlushPeriodInSeconds);
            } catch (NumberFormatException nfe) {
                debug("Bad awsLogStreamFlushPeriodInSeconds (" + awsLogStreamFlushPeriodInSeconds + "), defaulting to: " + AWS_LOG_STREAM_FLUSH_PERIOD_IN_SECONDS + "s");
            }
        } else {
            debug("No awsLogStreamFlushPeriodInSeconds specified, defaulted to " + AWS_LOG_STREAM_FLUSH_PERIOD_IN_SECONDS + "s");
        }
        flushPeriodMillis = flushPeriod * 1000;

        try {

            awsLogsClient = new AWSLogsClient(); // this should pull the credentials automatically from the environment

            // set the group name
            this.logGroupName = awsLogGroupName;

            // determine the stream name (prefix) and suffix it with the timestamp to ensure uniqueness
            String logStreamNamePrefix = awsLogStreamName;
            if (logStreamNamePrefix == null) {
                logStreamNamePrefix = ENV_LOG_STREAM_NAME;
            }
            if (logStreamNamePrefix == null) {
                logStreamNamePrefix = AWS_INSTANCE_ID;
            }
            String finalLogStreamName;
            do {
                finalLogStreamName = logStreamNamePrefix + " " + getTimeNow();
                this.sequenceTokenCache = createLogGroupAndLogStreamIfNeeded(logGroupName, finalLogStreamName);
            } while (this.sequenceTokenCache != null);
            logStreamName = finalLogStreamName;

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Runnable messageProcessor = new Runnable() {
        @Override
        public void run() {
            debug("Draining queue for " + logStreamName + " stream every " + (flushPeriodMillis / 1000) + "s...");
            while (!shutdown) {
                try {
                    flush();
                } catch (Throwable t) {
                    t.printStackTrace();
                }
                if (!shutdown && queue.size() < AWS_DRAIN_LIMIT) {
                    try {
                        synchronized (monitor) {
                            monitor.wait(flushPeriodMillis);
                        }
                    } catch (InterruptedException ix) {
                        ix.printStackTrace();
                    }
                }
            }

            while (!queue.isEmpty()) {
                flush();
            }
        }
    };

    private void flush() {
        int drained;
        final List<InputLogEvent> logEvents = new ArrayList<>(AWS_DRAIN_LIMIT);
        do {
            drained = queue.drainTo(logEvents, AWS_DRAIN_LIMIT);
            if (logEvents.isEmpty()) {
                break;
            }
            Collections.sort(logEvents, new Comparator<InputLogEvent>() {
                @Override
                public int compare(InputLogEvent o1, InputLogEvent o2) {
                    return o1.getTimestamp().compareTo(o2.getTimestamp());
                }
            });
            if (lastReportedTimestamp > 0) {
                //in the off chance that the new events start with older TS than the last sent event
                //reset their timestamps to the last timestamp until we reach an event with
                //higher timestamp
                for (InputLogEvent event : logEvents) {
                    if (event.getTimestamp() < lastReportedTimestamp)
                        event.setTimestamp(lastReportedTimestamp);
                    else
                        break;
                }
            }
            lastReportedTimestamp = logEvents.get(logEvents.size() - 1).getTimestamp();
            final PutLogEventsRequest putLogEventsRequest = new PutLogEventsRequest(logGroupName, logStreamName, logEvents);
            putLogEventsRequest.setSequenceToken(sequenceTokenCache);
            try {
                final PutLogEventsResult putLogEventsResult = awsLogsClient.putLogEvents(putLogEventsRequest); // 1 MB or 10000 messages AWS cap!
                sequenceTokenCache = putLogEventsResult.getNextSequenceToken();
            } catch (final DataAlreadyAcceptedException daae) {
                debug("DataAlreadyAcceptedException, will reset the token to the expected one");
                sequenceTokenCache = daae.getExpectedSequenceToken();
            } catch (final InvalidSequenceTokenException iste) {
                debug("InvalidSequenceTokenException, will reset the token to the expected one");
                sequenceTokenCache = iste.getExpectedSequenceToken();
            } catch (Exception e) {
                debug("Error writing logs");
                e.printStackTrace();
            }
            logEvents.clear();
        } while (drained >= AWS_DRAIN_LIMIT);
    }

    /**
     * Create AWS log event based on the log4j log event and add it to the queue.
     */
    @Override
    public void append(final LogEvent event) {
        final InputLogEvent awsLogEvent = new InputLogEvent();
        final long timestamp = event.getTimeMillis();
        final String message = new String(getLayout().toByteArray(event));
        awsLogEvent.setTimestamp(timestamp);
        awsLogEvent.setMessage(message);
        if (!queue.offer(awsLogEvent) && !queueFull) {
            debug("Log queue is full!");
            queueFull = true;
        } else if (queueFull)
            queueFull = false;
    }

    /**
     * Create log group ans log stream if needed.
     *
     * @param logGroupName  the name of the log group
     * @param logStreamName the name of the stream
     * @return sequence token for the created stream
     */
    private String createLogGroupAndLogStreamIfNeeded(String logGroupName, String logStreamName) {
        final DescribeLogGroupsResult describeLogGroupsResult = awsLogsClient.describeLogGroups(new DescribeLogGroupsRequest().withLogGroupNamePrefix(logGroupName));
        boolean createLogGroup = true;
        if (describeLogGroupsResult != null && describeLogGroupsResult.getLogGroups() != null && !describeLogGroupsResult.getLogGroups().isEmpty()) {
            for (final LogGroup lg : describeLogGroupsResult.getLogGroups()) {
                if (logGroupName.equals(lg.getLogGroupName())) {
                    createLogGroup = false;
                    break;
                }
            }
        }
        if (createLogGroup) {
            debug("Creating logGroup: " + logGroupName);
            final CreateLogGroupRequest createLogGroupRequest = new CreateLogGroupRequest(logGroupName);
            awsLogsClient.createLogGroup(createLogGroupRequest);
        }
        String logSequenceToken = null;
        boolean createLogStream = true;
        final DescribeLogStreamsRequest describeLogStreamsRequest = new DescribeLogStreamsRequest(logGroupName).withLogStreamNamePrefix(logStreamName);
        final DescribeLogStreamsResult describeLogStreamsResult = awsLogsClient.describeLogStreams(describeLogStreamsRequest);
        if (describeLogStreamsResult != null && describeLogStreamsResult.getLogStreams() != null && !describeLogStreamsResult.getLogStreams().isEmpty()) {
            for (final LogStream ls : describeLogStreamsResult.getLogStreams()) {
                if (logStreamName.equals(ls.getLogStreamName())) {
                    createLogStream = false;
                    logSequenceToken = ls.getUploadSequenceToken();
                }
            }
        }

        if (createLogStream) {
            debug("Creating logStream: " + logStreamName);
            final CreateLogStreamRequest createLogStreamRequest = new CreateLogStreamRequest(logGroupName, logStreamName);
            awsLogsClient.createLogStream(createLogStreamRequest);
        }
        return logSequenceToken;
    }

    // tiny helper self-describing methods

    @Override
    public void start() {
        super.start();
        debug("Starting cloudWatchAppender for: " + logGroupName + ":" + logStreamName);
        deliveryThread = new Thread(messageProcessor, "CloudWatchAppenderDeliveryThread");
        deliveryThread.start();
    }

    @Override
    public void stop() {
        super.stop();
        shutdown = true;
        if (deliveryThread != null) {
            synchronized (monitor) {
                monitor.notify();
            }
            try {
                deliveryThread.join(SHUTDOWN_TIMEOUT_MILLIS);
            } catch (InterruptedException ix) {
                ix.printStackTrace();
            }
        }
        if (queue.size() > 0) {
            flush();
        }
    }

    private String getTimeNow() { return simpleDateFormat.format(new Date()); }
    private void debug(final String s) { System.out.println(getTimeNow() + " CloudWatchAppender: " + s); }
}
