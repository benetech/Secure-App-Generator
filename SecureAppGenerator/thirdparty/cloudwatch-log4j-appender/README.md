CloudWatchAppender
==================


Emits log4j events into AWS CloudWatch streams.

## Build

    $ git clone git@github.com:Virtual-Instruments/cloudwatch-log4j-appender.git
    $ cd CloudWatchAppender
    $ mvn install

## Usage
```
    <CloudWatchAppender name="CloudWatchAppender"
                           awsLogGroupName="MyGroup"
                           awsLogStreamName="CloudWatchAppender"
                           awsLogStreamFlushPeriodInSeconds="5">
      <PatternLayout>
        <Pattern>%5p | %d{ISO8601}{UTC} | %t | %C | %M:%L | %m %ex %n</Pattern>
      </PatternLayout>
    </CloudWatchAppender>
```
## Configuration variables

Optional log4j appender plugin attributes:

+ **awsLogGroupName**: the name of the AWS log group (default: "unknown").
+ **awsLogStreamName**: the name of the AWS log stream inside the AWS log group from above (default: "Test Stream").
  Might also be be overridden by the LOG_STREAM_NAME environment variable (see below).
  Note that the stream name will always be suffixed by the current timestamp.
  This means that every time the logger restarts, it will create a new log stream.
+ **awsLogStreamFlushPeriodInSeconds**: the period of the flusher (default: 5).

## Environment variables

Your AWS credentials should be specified in the AWS environment in the standard way.
For testing purposes, you can override the credentials in the environment:

+ **AWS_ACCESS_KEY**: sets the AWS Access Key.
+ **AWS_SECRET_KEY**: sets the AWS Secret Key.

You can also supply the AWS log stream name via environment:

+ **LOG_STREAM_NAME**: sets the AWS log stream name.

## License

Licensed under the Apache License, Version 2.0: http://www.apache.org/licenses/LICENSE-2.0
