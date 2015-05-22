package SAG;

import javax.servlet.http.HttpSession;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SecureAppGeneratorApplication 
{
	private static final String DEFAULT_APP_ICON_LOCATION = "../images/Martus-swoosh-30x30.png";//TODO fix the location for server

	public static void main(String[] args) 
    {
        SpringApplication.run(SecureAppGeneratorApplication.class, args);
    }

	public static void setInvalidResults(HttpSession session) 
	{
		setInvalidResults(session, "Invalid Request"); //TODO move this to a localizable String Table
	}
	
	public static void setInvalidResults(HttpSession session, String message) 
	{
		ErrorResults invalidRequest = new ErrorResults();
		invalidRequest.setResults(message);  
		session.setAttribute(SessionAttributes.INVALID_REQUEST, invalidRequest);
	}
	
	public static void setDefaultIconForSession(HttpSession session, AppConfiguration config)
	{
		config.setAppIconLocation(DEFAULT_APP_ICON_LOCATION);
		session.setAttribute(SessionAttributes.APP_CONFIG, config);
	}
	
}
