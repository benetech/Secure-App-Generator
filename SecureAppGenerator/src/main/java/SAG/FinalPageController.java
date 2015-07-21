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

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.core.io.FileSystemResource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Controller
public class FinalPageController extends WebMvcConfigurerAdapter
{
	@RequestMapping(value = "/downloads/{file_name}.{ext}", method = RequestMethod.GET)
	@ResponseBody
	public FileSystemResource getFile(HttpServletResponse response, @PathVariable( "file_name") String fileName, @PathVariable("ext") String extension) 
	{
		String fileWithExtension = fileName + "." + extension;
		response.setHeader("Content-Disposition", "attachment;filename=" + fileWithExtension );		
        response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
        
        FileSystemResource fileSystemResource = new FileSystemResource(getFileFor(fileWithExtension));
	    return fileSystemResource; 
	}

	@RequestMapping(value=WebPage.FINAL, method=RequestMethod.GET)
    public String directError(HttpSession session, Model model) 
    {
		SecureAppGeneratorApplication.setInvalidResults(session);
        return WebPage.ERROR;
    }

	private File getFileFor(String fileName)
	{
		Logger.log("Request to download :" + fileName);
		return new File(SecureAppGeneratorApplication.getDownloadsDirectory(), fileName);
	}
	
}
