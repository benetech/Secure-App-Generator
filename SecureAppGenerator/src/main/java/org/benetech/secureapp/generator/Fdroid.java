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

import java.io.File;
import java.io.IOException;

import javax.servlet.http.HttpSession;

import org.apache.commons.io.FileUtils;

public class Fdroid
{
    private static final String FDROID_REPO_DIR = "repo";
	private static final String INCLUDE_FDROID_ENV = "INCLUDE_FDROID";
	private static final String FDROID_TRUE = "true";

    public static File getOriginalFDroidDirectory()
	{
		return new File(SecureAppGeneratorApplication.getStaticWebDirectory(), "fdroidmaster");
	}

	static public void copyApkToFDroid(HttpSession session, File apkCreated) throws Exception
	{
		try
		{
			File baseDir = createTempFDroidRepo(session);
			File repoDir = new File(baseDir, FDROID_REPO_DIR);
			File destination = new File(repoDir, apkCreated.getName());
			Logger.log(session, "Copy to FDroid Repo: "+ destination.getAbsolutePath());
			FileUtils.copyFile(apkCreated, destination);
			destination.setExecutable(true);
			destination.setWritable(true);

			String fDroidCommand = "fdroid update -v";
			SecureAppGeneratorApplication.executeCommand(session, fDroidCommand, baseDir);
			fDroidCommand = "fdroid server update -v";
			SecureAppGeneratorApplication.executeCommand(session, fDroidCommand, baseDir);
		}
		finally
		{
	//		try
			{
	//			TODO: add this back once tested on server.			
	//			if(baseDir != null)
	//				FileUtils.deleteDirectory(baseDir);
			}
	//		catch (IOException e)
			{
	//			Logger.logException(e);			
			}			
		}
	}

	static private File createTempFDroidRepo(HttpSession session) throws IOException
	{
		File baseDir = SecureAppGeneratorApplication.getRandomDirectoryFile("fdroid");
		FileUtils.copyDirectory(getOriginalFDroidDirectory(), baseDir);
		return baseDir;
	}

	static public boolean includeFDroid()
	{
  		String includeFDroid = System.getenv(INCLUDE_FDROID_ENV);
  		return(includeFDroid != null && includeFDroid.toLowerCase().equals(FDROID_TRUE));
	}
}
