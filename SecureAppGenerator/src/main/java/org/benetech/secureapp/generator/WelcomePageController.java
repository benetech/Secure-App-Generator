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

package org.benetech.secureapp.generator;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Controller
public class WelcomePageController extends WebMvcConfigurerAdapter
{
	@RequestMapping({"/"})
	String index(HttpSession session) throws Exception 
	{
		SecureAppGeneratorApplication.setInvalidResults(session);
		setupDefaultSessionAttributes(session);
	    return WebPage.WELCOME;
	}

	public void setupDefaultSessionAttributes(HttpSession session) throws Exception
	{
		AppConfiguration defaultConfig = new AppConfiguration();
		SecureAppGeneratorApplication.setDefaultIconForSession(session, defaultConfig);
		SecureAppGeneratorApplication.setSessionFromConfig(session, defaultConfig);
	}

	@RequestMapping(value=WebPage.WELCOME, method=RequestMethod.GET)
    public String directError(HttpSession session, Model model) 
    {
		SecureAppGeneratorApplication.setInvalidResults(session);
        return WebPage.ERROR;
    }
	
	@RequestMapping(value=WebPage.WELCOME, method=RequestMethod.POST)
    public String loadAppNamePage(HttpSession session, Model model) 
    {
        return WebPage.NAME_APP;
    }
	
	@ModelAttribute(SessionAttributes.APP_CONFIG)
	public AppConfiguration appConfig() 
	{
	    return new AppConfiguration();
	}	
}
