package org.benetech.secureapp.generator;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.nio.file.Files;

import javax.servlet.http.HttpSession;

import org.martus.common.Base64XmlOutputStream;
import org.martus.common.XmlWriterFilter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.web.multipart.MultipartFile;


@SpringBootApplication
public class SecureAppGeneratorApplication extends SpringBootServletInitializer 
{
	private static final String MASTER_SA_BUILD_DIRECTORY = "/SecureAppBuildMaster"; 
	static final String DEFAULT_APP_ICON_LOCATION = "/images/Martus-swoosh-30x30.png";
	private static final String ICON_LOCAL_File = getStaticWebDirectory() + DEFAULT_APP_ICON_LOCATION;
	private static final String GRADLE_HOME_ENV = "GRADLE_HOME";

	@Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(SecureAppGeneratorApplication.class);
    }

	public static void main(String[] args) 
    {
        SpringApplication.run(SecureAppGeneratorApplication.class, args);
    }

	
	static void setInvalidResults(HttpSession session) 
	{
		setInvalidResults(session, getLocalizedErrorMessage("invalid_request")); 
	}
	
	static void setInvalidResults(HttpSession session, String message) 
	{
		ErrorResults invalidRequest = new ErrorResults();
		invalidRequest.setResults(message);  
		session.setAttribute(SessionAttributes.INVALID_REQUEST, invalidRequest);
	}
	
	public static void setInvalidResults(HttpSession session, String msgId, Exception e)
	{
		setInvalidResults(session, getLocalizedErrorMessage(msgId, e));
	}

	static void setDefaultIconForSession(HttpSession session, AppConfiguration config) throws Exception
	{
		config.setAppIconLocation(DEFAULT_APP_ICON_LOCATION);
		config.setAppIconLocalFileLocation(ICON_LOCAL_File);
		
		final File staticDir = getStaticWebDirectory();	
		config.setAppIconBase64Data(getBase64DataFromFile(new File(staticDir, DEFAULT_APP_ICON_LOCATION)));
		session.setAttribute(SessionAttributes.APP_CONFIG, config);
	}

	public static File getStaticWebDirectory()
	{
		try
		{
			final PathMatchingResourcePatternResolver pmrpr = new PathMatchingResourcePatternResolver();
			final File staticDir = pmrpr.getResource("classpath:static").getFile();
			//Logger.logVerbose(null, "Static dir:"+staticDir.getAbsolutePath());
			return staticDir;
		}
		catch (IOException e)
		{
			SagLogger.logException(null, e);
			return new File("/static");
		}
	}
	
	static String getOriginalBuildDirectory()
	{
		return (new File(getStaticWebDirectory(), MASTER_SA_BUILD_DIRECTORY)).getAbsolutePath();
	}
	
	static String getGadleDirectory()
	{
   		String dataRootDirectory = System.getenv(SecureAppGeneratorApplication.GRADLE_HOME_ENV);
   		SagLogger.logDebug(null, "Gradle Dir:"+dataRootDirectory);
   		return dataRootDirectory;
	}
	
	static void setSessionFromConfig(HttpSession session, AppConfiguration config)
	{
		AppConfiguration sessionConfig = new AppConfiguration();
		sessionConfig.setAppName(getMessage("default.app_name"));
		sessionConfig.setAppIconLocation(config.getAppIconLocation());
		sessionConfig.setClientToken(config.getClientToken());
		session.setAttribute(SessionAttributes.APP_CONFIG, sessionConfig);
	}

	static void saveMultiPartFileToLocation(MultipartFile file, File formFileUploaded) throws IOException, FileNotFoundException
	{
		byte[] bytes = file.getBytes();
		BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(formFileUploaded));
		stream.write(bytes);
		stream.close();
	}	
	
	private static String convertToBase64BySingleBytes(byte[] data) throws IOException
	{
		Writer writer = new StringWriter();
		XmlWriterFilter wf = new XmlWriterFilter(writer);
		Base64XmlOutputStream out = new Base64XmlOutputStream(wf);
		for(int i = 0; i < data.length; ++i)
			out.write(data[i]);
		out.close();
		String result = "data:image/png;base64," + writer.toString();
		return result;
	}
	
	static String getBase64DataFromFile(File file) throws IOException, FileNotFoundException
	{
		byte[] data = Files.readAllBytes(file.toPath());
		return convertToBase64BySingleBytes(data);
	}

	public static File getRandomDirectoryFile(String type) throws IOException
	{
	    final File tempDir;
	    tempDir = File.createTempFile(type, Long.toString(System.nanoTime()));
	    tempDir.delete();
	    tempDir.mkdirs();
	    return tempDir;
	}	
	
	static public void writeDataToFile(File file, StringBuilder data) throws FileNotFoundException, UnsupportedEncodingException, IOException
	{
		FileOutputStream fileOutputStream = new FileOutputStream(file);
		BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(fileOutputStream,"UTF-8"));       
   		writer.write(data.toString());
   		writer.flush();
    		writer.close();
	}

	static public int executeCommand(HttpSession session, String command, File initialDirectory) throws IOException, InterruptedException
	{
		SagLogger.logDebug(session, "Exec Command:" + command);
		Runtime rt = Runtime.getRuntime();
		Process p = rt.exec(command, null, initialDirectory);
		SagLogger.logProcess(session, p);		
		p.waitFor();
		return p.exitValue();
	}
	
	static public String getLocalizedErrorMessage(String msgId)
	{
		if(msgId == null)
			return null;
		StringBuilder errorMsg = new StringBuilder(getMessage("error_prefix"));
		errorMsg.append(" ");
		errorMsg.append(getLocalizedErrorMessageNoPrefix(msgId));
		return errorMsg.toString();
	}
	
	static public String getLocalizedErrorMessageNoPrefix(String msgId)
	{
		if(msgId == null)
			return null;
		StringBuilder errorMsg = new StringBuilder(getMessage("error."+msgId));
		return errorMsg.toString();
	}

	public static String getLocalizedErrorMessage(String msgId, Exception e)
	{
		StringBuilder errorMessage = new StringBuilder(getLocalizedErrorMessage(msgId));
		errorMessage.append(" ");
		errorMessage.append(e.getLocalizedMessage());
		return errorMessage.toString();
	}

/*
 *  FIXME this doesn't work from a WAR file but works in Eclispe
 *  Workaround is having to copy *.properties files to 
 *  root of classes folder in gradle script
 	@Bean
	public MessageSource messageSource() 
	{
        return getMessageSource();
	}
 */

	//TODO: we may want to have only a single instance of ReloadableResourceBundleMessageSource
	private static MessageSource getMessageSource()
	{
		ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
		messageSource.setBasenames("classpath:/messages");
		messageSource.setUseCodeAsDefaultMessage(true);
		messageSource.setDefaultEncoding("UTF-8");
		messageSource.setCacheSeconds(0);
		return messageSource;
	}

	static public String getMessage(String msgId)
	{
		return getMessageSource().getMessage(msgId, null, LocaleContextHolder.getLocale());
	}
}
