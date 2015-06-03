package SAG;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.servlet.http.HttpSession;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.multipart.MultipartFile;

@SpringBootApplication
public class SecureAppGeneratorApplication 
{
	private static final String APP_DEFAULT_NAME = "My App";
	private static final String DEFAULT_APP_ICON_LOCATION = "../images/Martus-swoosh-30x30.png";//TODO fix the location for server

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
	
	static void setDefaultIconForSession(HttpSession session, AppConfiguration config)
	{
		config.setAppIconLocation(DEFAULT_APP_ICON_LOCATION);
		session.setAttribute(SessionAttributes.APP_CONFIG, config);
	}

	static void setDefaultNameForSession(HttpSession session, AppConfiguration config)
	{
		AppConfiguration sessionConfig = new AppConfiguration();
		sessionConfig.setAppName(APP_DEFAULT_NAME);
		sessionConfig.setAppIconLocation(config.getAppIconLocation());
		session.setAttribute(SessionAttributes.APP_CONFIG, sessionConfig);
	}

	static void saveMultiPartFileToLocation(MultipartFile file, String fileLocation) throws IOException, FileNotFoundException
	{
		byte[] bytes = file.getBytes();
		File formFileUploaded = new File(fileLocation);//TODO fix file location
		BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(formFileUploaded));
		stream.write(bytes);
		stream.close();
	}
}
