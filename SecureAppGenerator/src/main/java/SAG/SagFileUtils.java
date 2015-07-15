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
/* Source code from http://stackoverflow.com/questions/5368724/how-to-copy-a-folder-and-all-its-subfolders-and-files-into-another-folder
 * 
 */
package SAG;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class SagFileUtils
{
	static public void copy(File sourceLocation, File targetLocation) throws IOException 
	{
	    if (sourceLocation.isDirectory()) 
	    {
	    		
	        copyDirectory(sourceLocation, targetLocation);
	    } 
	    else 
	    {
	        copyFile(sourceLocation, targetLocation);
	    }
	}

	static private void copyDirectory(File source, File target) throws IOException 
	{
	    if (!target.exists()) 
	    {
	        target.mkdir();
	        if(!target.exists())
	        		System.out.println("ERROR: unable to create directory");
	    }

	    for (String f : source.list()) 
	    {
	        copy(new File(source, f), new File(target, f));
	    }
	}

	static private void copyFile(File source, File target) throws IOException 
	{        
	    if(source.isHidden())
	    		return;
		try (
	            InputStream in = new FileInputStream(source);
	            OutputStream out = new FileOutputStream(target)
	    ) 
	    {
	        byte[] buf = new byte[1024];
	        int length;
	        while ((length = in.read(buf)) > 0) 
	        {
	            out.write(buf, 0, length);
	        }
	    }
	}
}
