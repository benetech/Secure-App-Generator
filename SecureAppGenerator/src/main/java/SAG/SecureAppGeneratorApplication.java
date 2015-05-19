package SAG;

import javax.servlet.http.HttpSession;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SecureAppGeneratorApplication 
{
    public static void main(String[] args) 
    {
        SpringApplication.run(SecureAppGeneratorApplication.class, args);
    }

	public static void setInvalidResults(HttpSession session) 
	{
		ErrorResults invalidRequest = new ErrorResults();
		invalidRequest.setResults("Invalid Request");  //TODO move this to a localizable String Table
		session.setAttribute("invalidRequest", invalidRequest);
	}
}
