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

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Controller
public class ObtainXFormController extends WebMvcConfigurerAdapter
{
	private static final String XML_TYPE = "xml";
	private static final String XML_FILE_LOCATION = "./bin/static/xFormToUse.xml";  //TODO this will be based on build directory for this session

	@RequestMapping(value=WebPage.OBTAIN_XFORM, method=RequestMethod.GET)
    public String directError(HttpSession session, Model model) 
    {
		SecureAppGeneratorApplication.setInvalidResults(session);
        return WebPage.ERROR;
    }

	@RequestMapping(value=WebPage.OBTAIN_XFORM_PREVIOUS, method=RequestMethod.POST)
    public String goBack(HttpSession session, Model model) 
    {
		AppConfiguration config = (AppConfiguration) session.getAttribute("appConfig");
		model.addAttribute("appConfig", config);
		return WebPage.OBTAIN_LOGO;
    }

	@RequestMapping(value=WebPage.OBTAIN_XFORM_NEXT, method=RequestMethod.POST)
    public String retrieveLogo(HttpSession session, @RequestParam("file") MultipartFile file, Model model, AppConfiguration appConfig)
    {
        if (!file.isEmpty()) 
        {
            try 
            {
            		if(!file.getContentType().contains(XML_TYPE))
            		{
            			appConfig.setAppXFormError("Error: Xforms type must be of type xml.");
            			model.addAttribute(SessionAttributes.APP_CONFIG, appConfig);
         			return WebPage.OBTAIN_XFORM; 
            		}
                byte[] bytes = file.getBytes();
                File formFileUploaded = new File(XML_FILE_LOCATION);//TODO fix file location
				BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(formFileUploaded));
                stream.write(bytes);
                stream.close();
                AppConfiguration config = (AppConfiguration)session.getAttribute(SessionAttributes.APP_CONFIG);
        			config.setAppXFormLocation("xFormToUse.xml"); //TODO fix file location
        			session.setAttribute(SessionAttributes.APP_CONFIG, config);
            } 
            catch (Exception e) 
            {
            		SecureAppGeneratorApplication.setInvalidResults(session, "You failed to upload a file => " + e.getMessage());
                return WebPage.ERROR;
            }
        } 
		model.addAttribute(SessionAttributes.APP_CONFIG, appConfig);
       return WebPage.FINAL;
    }
}
