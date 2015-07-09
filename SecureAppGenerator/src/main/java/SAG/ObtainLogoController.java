/*

Martus(TM) is a trademark of Beneficent Technology, Inc. 
This software is (c) Copyright 2015, Beneficent Technology, Inc.

Martus is free software; you can redistribute it and/or
modify it under the terms of the GNU General Public License
as published by the Free Software Foundation; either
version 2 of the License, or (at your option) any later
version with the additions and exceptions described in the
accompanying Martus license file entitled "license.txt".

It is distributed WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, including warranties of fitness of purpose or
merchantability.  See the accompanying Martus License and
GPL license for more details on the required license terms
for this software.

You should have received a copy of the GNU General Public
License along with this program; if not, write to the Free
Software Foundation, Inc., 59 Temple Place - Suite 330,
Boston, MA 02111-1307, USA.

*/

package SAG;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Controller
public class ObtainLogoController extends WebMvcConfigurerAdapter
{
	private static final String LOGO_FILE_NAME = "companyLogo";  
	private static final String IMAGE_PNG = "image/png";
	private static final String PNG_EXT = ".png";

	@RequestMapping(value=WebPage.OBTAIN_LOGO, method=RequestMethod.GET)
    public String directError(HttpSession session, Model model) 
    {
		SecureAppGeneratorApplication.setInvalidResults(session);
        return WebPage.ERROR;
    }

	@RequestMapping(value=WebPage.OBTAIN_LOGO_PREVIOUS, method=RequestMethod.POST)
    public String goBack(HttpSession session, Model model) 
    {
		AppConfiguration config = (AppConfiguration) session.getAttribute(SessionAttributes.APP_CONFIG);
		model.addAttribute("appConfig", config);
		return WebPage.NAME_APP;
    }

	@RequestMapping(value=WebPage.OBTAIN_LOGO_NEXT, method=RequestMethod.POST)
    public String retrieveLogo(HttpSession session, @RequestParam("file") MultipartFile file, Model model, AppConfiguration appConfig)
    {
        if (!file.isEmpty()) 
         {
            try 
            {
            	
            		//String dataRootDirectory = System.getenv(SecureAppGeneratorApplication.SAG_DATA_DIR_ENV);
            		File tempIconLocation = File.createTempFile(LOGO_FILE_NAME, PNG_EXT);
            		tempIconLocation.deleteOnExit();
            		if(!file.getContentType().contains(IMAGE_PNG))
            		{
            			appConfig.setAppIconError("Error: Image type must be png.");
            			model.addAttribute(SessionAttributes.APP_CONFIG, appConfig);
         			return WebPage.OBTAIN_LOGO; 
            		}
             	
            		SecureAppGeneratorApplication.saveMultiPartFileToLocation(file, tempIconLocation);
            		AppConfiguration config = (AppConfiguration)session.getAttribute(SessionAttributes.APP_CONFIG);
            		config.setAppIconLocalFileLocation(tempIconLocation.getAbsolutePath());
            		System.out.println(tempIconLocation.getAbsolutePath());
            		config.setAppIconBase64Data(SecureAppGeneratorApplication.getBase64DataFromFile(tempIconLocation.getAbsoluteFile()));
            		session.setAttribute(SessionAttributes.APP_CONFIG, config);
            } 
            catch (Exception e) 
            {
            		SecureAppGeneratorApplication.setInvalidResults(session, "You failed to upload a file => " + e.getMessage());
                return WebPage.ERROR;
            }
        } 
		model.addAttribute(SessionAttributes.APP_CONFIG, appConfig);
       return WebPage.OBTAIN_XFORM;
    }
	
	//NOTE: Needed due to how page is called from another page 
	@ModelAttribute("formsImpMap")
	public Map<String,String> populateFormsMap() throws MalformedURLException, IOException 
	{
		return ObtainXFormController.populateFormsMap();
	}
}
