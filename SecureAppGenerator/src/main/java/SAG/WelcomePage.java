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

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class WelcomePage
{

	@RequestMapping({"/"})
	String index(HttpSession session) 
	{
		setInvalidResults(session);
	    return WebPage.WELCOME;
	}

	private void setInvalidResults(HttpSession session) 
	{
		ErrorResults invalidRequest = new ErrorResults();
		invalidRequest.setResults("Invalid Request");  //TODO move this to a localizable String Table
		session.setAttribute("invalidRequest", invalidRequest);
	}

	@RequestMapping(value="/"+ WebPage.WELCOME, method=RequestMethod.GET)
    public String provideUploadInfo(HttpSession session, Model model) 
    {
		setInvalidResults(session);
        return WebPage.ERROR;
    }
}
