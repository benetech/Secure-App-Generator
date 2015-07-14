package SAG;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.nio.file.Files;

import javax.servlet.http.HttpSession;

import org.martus.common.Base64XmlOutputStream;
import org.martus.common.XmlWriterFilter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.web.multipart.MultipartFile;


@SpringBootApplication
public class SecureAppGeneratorApplication extends SpringBootServletInitializer 
{
	private static final String APP_DEFAULT_NAME = "My App";
	private static final String DEFAULT_APP_ICON_LOCATION = "/images/Martus-swoosh-30x30.png";//TODO fix the location for server
	public static final String APK_RELATIVE_DOWNLOADS_DIRECTORY = "/Downloads/";
	public static final String APK_LOCAL_DOWNLOADS_DIRECTORY = getStaticWebDirectory() + APK_RELATIVE_DOWNLOADS_DIRECTORY;
	public static final String ICON_LOCAL_File = getStaticWebDirectory() + DEFAULT_APP_ICON_LOCATION;
	public static final String SAG_DATA_DIR_ENV = "SAG_DATA_DIR";

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
		setInvalidResults(session, "Invalid Request"); //TODO move this to a localizable String Table
	}
	
	static void setInvalidResults(HttpSession session, String message) 
	{
		ErrorResults invalidRequest = new ErrorResults();
		invalidRequest.setResults(message);  
		session.setAttribute(SessionAttributes.INVALID_REQUEST, invalidRequest);
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
			System.out.println("Static dir:"+staticDir.getAbsolutePath());
			return staticDir;
		}
		catch (IOException e)
		{
			e.printStackTrace();
			return new File("/static");
		}
	}
	
	static String getDataDirectory()
	{
   		String dataRootDirectory = System.getenv(SecureAppGeneratorApplication.SAG_DATA_DIR_ENV);
   		return dataRootDirectory;
	}

	static void setSessionFromConfig(HttpSession session, AppConfiguration config)
	{
		AppConfiguration sessionConfig = new AppConfiguration();
		sessionConfig.setAppName(APP_DEFAULT_NAME);
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
}
