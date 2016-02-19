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

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;


@Controller
public class NameAppController extends WebMvcConfigurerAdapter
{
	@RequestMapping(value=WebPage.NAME_APP, method=RequestMethod.GET)
    public String directError(HttpSession session, Model model) 
    {
		SagLogger.logWarning(session, "NAME_APP Get Request");
		SecureAppGeneratorApplication.setInvalidResults(session);
        return WebPage.ERROR;
    }

	@RequestMapping(value=WebPage.NAME_APP_PREV, method=RequestMethod.POST)
    public String goBack(HttpSession session, Model model, AppConfiguration appConfig) 
    {
		model.addAttribute(SessionAttributes.APP_CONFIG, appConfig);
       return WebPage.WELCOME;
    }

	
	@RequestMapping(value=WebPage.NAME_APP_NEXT, method=RequestMethod.POST)
	
	public String nextPage(HttpSession session, Model model, AppConfiguration appConfig) throws Exception 
    {
		SecureAppGeneratorApplication.setDefaultIconForSession(session, appConfig);
		if (!validateAppName(session, appConfig)) 
		{
			model.addAttribute(SessionAttributes.APP_CONFIG, appConfig);
			SecureAppGeneratorApplication.setSessionFromConfig(session, appConfig);
			return WebPage.NAME_APP;
		}
		//TODO TEST:
		SagLogger.logError(session, "TEST Logging Error: IGNORE");
		SagLogger.logWarning(session, "TEST Logging Warning: IGNORE");
		SagLogger.logDebug(session, "TEST Logging DEBUG: IGNORE");
		SagLogger.logInfo(session, "TEST Logging Info: IGNORE");
		
		
		model.addAttribute(SessionAttributes.APP_CONFIG, appConfig);
		session.setAttribute(SessionAttributes.APP_CONFIG, appConfig);
        return WebPage.OBTAIN_LOGO;
    }

	public boolean validateAppName(HttpSession session, AppConfiguration appConfig)
	{
		String name = appConfig.getAppName().trim();
		appConfig.setAppName(name);
		int length = name.length();
		if(length<3 || length>30)
		{
			SagLogger.logWarning(session, "App Name:Length");
			appConfig.setAppNameError("app_name_length");
			return false;
		}
		
		if (!name.matches("^[^!\"#$%&'\\[\\]*.+,/:;<=>?@\\^`{|}~]+$")) 
		{
			SagLogger.logWarning(session, "App Name:invalid char");
			appConfig.setAppNameError("app_name_characters");
			return false;
		}
		if(name.indexOf('\\')>=0)//FixMe: Regex didn't work for some reason
		{
			SagLogger.logWarning(session, "App Name:invalid char \\");
			appConfig.setAppNameError("app_name_characters");
			return false;
		}
		appConfig.setAppNameError(null);
		return true;
	}
}
