/*

Martus(TM) is a trademark of Beneficent Technology, Inc. 
This software is (c) Copyright 2015-2016, Beneficent Technology, Inc.

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

package org.benetech.secureapp.generator;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URLConnection;
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
	private static final String PNG_EXT = ".png";
	private static final int CELLPHONE_LOGO_SIZE = 33; //used in CSS .cellphone_logo_size
	private static final long MAX_IMAGE_SIZE = 2097152;
	private static final String IMAGE_PNG = "image/png";

	@RequestMapping(value=WebPage.OBTAIN_LOGO, method=RequestMethod.GET)
    public String directError(HttpSession session, Model model) 
    {
		SagLogger.logWarning(session, "OBTAIN_LOGO Get Request");
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
    public String retrieveLogo(HttpSession session, @RequestParam("pngFile") MultipartFile iconFile, Model model, AppConfiguration appConfig)
    {
       if (!iconFile.isEmpty()) 
         {
            try 
            {
            		if(iconFile.getSize() > MAX_IMAGE_SIZE)
            		{
            			SagLogger.logWarning(session, "Warning Logo exceeded max size: " + iconFile.getSize());
            	      	appConfig.setAppIconError("logo_file_size");
            			model.addAttribute(SessionAttributes.APP_CONFIG, appConfig);
         			return WebPage.OBTAIN_LOGO; 
            		}

            		File tempIconLocation = File.createTempFile(LOGO_FILE_NAME, PNG_EXT);
            		String logoAbsolutePath = tempIconLocation.getAbsolutePath();
				SagLogger.logDebug(session, "Uploaded Icon Location" + logoAbsolutePath);
               	tempIconLocation.deleteOnExit();
              	
            		SecureAppGeneratorApplication.saveMultiPartFileToLocation(iconFile, tempIconLocation);
            		AppConfiguration config = (AppConfiguration)session.getAttribute(SessionAttributes.APP_CONFIG);
            		config.setAppIconLocalFileLocation(logoAbsolutePath);
            		File basedirIcon = tempIconLocation.getParentFile();
            		File resizedIconForWebPages = null;
            		try
				{
					resizedIconForWebPages = BuildingApkController.resizeAndSavePngImage(basedirIcon, logoAbsolutePath, CELLPHONE_LOGO_SIZE, tempIconLocation.getName());
				}
				catch (Exception e)
				{
        				SagLogger.logException(session, e);
				}
            		if(unableToResizeToPngImage(session, resizedIconForWebPages))
            		{
            			SagLogger.logError(session, "Error Non-PNG Logo Image: " + iconFile.getContentType());
            	      	appConfig.setAppIconError("logo_file_type_invalid");
            			model.addAttribute(SessionAttributes.APP_CONFIG, appConfig);
         			return WebPage.OBTAIN_LOGO; 
            		}
            		SagLogger.logInfo(session, "Custom logo uploaded");
             	config.setAppIconBase64Data(SecureAppGeneratorApplication.getBase64DataFromFile(resizedIconForWebPages));
             	resizedIconForWebPages.delete();
            		session.setAttribute(SessionAttributes.APP_CONFIG, config);
            } 
            catch (Exception e) 
            {
            		SagLogger.logException(session, e);
            		SecureAppGeneratorApplication.setInvalidResults(session, "upload_logo_failed", e);
                return WebPage.ERROR;
            }
        }
 		model.addAttribute(SessionAttributes.APP_CONFIG, appConfig);
       return WebPage.OBTAIN_XFORM;
    }

	private boolean unableToResizeToPngImage(HttpSession session, File resizedIconForWebPages)
	{
		try
		{
			if(resizedIconForWebPages != null && resizedIconForWebPages.exists())
			{
				InputStream is = new BufferedInputStream(new FileInputStream(resizedIconForWebPages));
				String mimeType = URLConnection.guessContentTypeFromStream(is);
				is.close();
				if(mimeType.contains(IMAGE_PNG))
					return false;
			}
		}
		catch (Exception e)
		{
    			SagLogger.logException(session, e);
		}
		return true;
	}
	
	public static void deleteLogo(AppConfiguration appConfig)
	{
		String appIconLocation = appConfig.getAppIconLocalFileLocation();
		if(appIconLocation == null)
			return;
		if(appIconLocation.contains(SecureAppGeneratorApplication.DEFAULT_APP_ICON_LOCATION))
			return;
		File originalLogo = new File(appIconLocation);
		originalLogo.delete();
	}
	
	//NOTE: Needed due to how page is called from another page 
	@ModelAttribute("formsImpMap")
	public Map<String,String> populateFormsMap() throws MalformedURLException, IOException 
	{
		return ObtainXFormController.populateFormsMap();
	}
}
