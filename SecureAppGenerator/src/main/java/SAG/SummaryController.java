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

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

import javax.servlet.http.HttpSession;

import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Controller
public class SummaryController extends WebMvcConfigurerAdapter
{
    private static final String DEBUG_APK_EXTENSION = "-debug.apk";
	private static final String GRADLE_LOCATION = "/Users/charlesl/Dev/gradle-2.3/bin/gradle";
	private static final String WEB_STATIC_DIRECTORY = "/Users/charlesl/EclipseMartus/Martus-Secure-App-Generator/SecureAppGenerator/bin/static/";
    private static final String GRADLE_PARAMETERS = " -p ";
	private static final String GRADLE_BUILD_COMMAND = " build";
	private static final String APK_LOCAL_FILE_DIRECTORY = "/build/outputs/apk/";
	private static final String ORIGINAL_BUILD_DIRECTORY = "/Users/charlesl/EclipseMartus/martus-android/secure-app-vital-voices"; 
	private static final String MAIN_DIRECTORY = "/Users/charlesl/SAG";
	private static final String MAIN_BUILD_DIRECTORY = MAIN_DIRECTORY + "/Build";
	private static final String APK_RELATIVE_DOWNLOADS_DIRECTORY = "Downloads/";
	private static final String APK_LOCAL_DOWNLOADS_DIRECTORY = WEB_STATIC_DIRECTORY + APK_RELATIVE_DOWNLOADS_DIRECTORY;
    private static final String GRADLE_SETTINGS = MAIN_BUILD_DIRECTORY + "/settings.gradle";
	private static final int EXIT_VALUE_GRADLE_SUCCESS = 0;

	@RequestMapping(value=WebPage.SUMMARY, method=RequestMethod.GET)
    public String directError(HttpSession session, Model model) 
    {
		SecureAppGeneratorApplication.setInvalidResults(session);
        return WebPage.ERROR;
    }

	@RequestMapping(value=WebPage.SUMMARY_PREVIOUS, method=RequestMethod.POST)
    public String goBack(HttpSession session, Model model, AppConfiguration appConfig) 
    {
		model.addAttribute(SessionAttributes.APP_CONFIG, appConfig);
       return WebPage.OBTAIN_CLIENT_TOKEN;
    }
	
	@RequestMapping(value=WebPage.SUMMARY_NEXT, method=RequestMethod.POST)	
	public String buildApk(HttpSession session, Model model, AppConfiguration appConfig) 
    {
		File baseBuildDir = null;
		try
		{
			baseBuildDir = getSessionBuildDirectory();
			copyDefaultBuildFilesToStagingArea(baseBuildDir);
			AppConfiguration config = (AppConfiguration)session.getAttribute(SessionAttributes.APP_CONFIG);
			File apkCreated = buildApk(baseBuildDir, config.getApkName());
			copyApkToDownloads(session, apkCreated, config.getApkName());
			model.addAttribute(SessionAttributes.APP_CONFIG, appConfig);
			return WebPage.FINAL;
		}
		catch (Exception e)
		{
			appConfig.setApkBuildError("Error: Unable to generate APK.");
			model.addAttribute(SessionAttributes.APP_CONFIG, appConfig);
			e.printStackTrace();
			return WebPage.SUMMARY;
		}
		finally
		{
			try
			{
				if(baseBuildDir != null)
					FileUtils.deleteDirectory(baseBuildDir);
			}
			catch (IOException e)
			{
			}			
		}
    }

	private File buildApk(File baseBuildDir, String apkFileName) throws IOException, InterruptedException
	{
		System.out.println("Building " + apkFileName);
		Runtime rt = Runtime.getRuntime();
		addBaseBuildDirToGradleSettings(baseBuildDir);
   		String gradleCommand = GRADLE_LOCATION + GRADLE_PARAMETERS + baseBuildDir + GRADLE_BUILD_COMMAND;
		System.out.println(gradleCommand);
   		Process pr = rt.exec(gradleCommand);
   		pr.waitFor();
		System.out.println("Finished Building " + apkFileName);
 
		int returnCode = pr.exitValue();
   		if(returnCode != EXIT_VALUE_GRADLE_SUCCESS)
	   		throw new IOException("Error creating APK");

   		String tempApkBuildFileDirectory = baseBuildDir.getAbsolutePath() + APK_LOCAL_FILE_DIRECTORY;
		String apkTempReleaseFileName = baseBuildDir.getName() + DEBUG_APK_EXTENSION;
		File appFileCreated = new File(tempApkBuildFileDirectory, apkTempReleaseFileName);
		return appFileCreated;
	}

	private void addBaseBuildDirToGradleSettings(File baseBuildDir) throws IOException
	{
		boolean appendToFile = true;
		StringBuilder data = new StringBuilder("include ':");
		data.append(baseBuildDir.getName());
		data.append("'\n");
  		FileWriter fileWritter = new FileWriter(GRADLE_SETTINGS, appendToFile);
        BufferedWriter bufferWritter = new BufferedWriter(fileWritter);
        bufferWritter.write(data.toString());
        bufferWritter.flush();
        bufferWritter.close();

 	}

	public void copyApkToDownloads(HttpSession session, File apkFileToMove, String apkFinalName) throws IOException
	{
		Path source = apkFileToMove.toPath();
		String finalApkBuildFile = APK_LOCAL_DOWNLOADS_DIRECTORY + apkFinalName;
		Path target = new File(finalApkBuildFile).toPath();
		Files.move(source, target, StandardCopyOption.REPLACE_EXISTING);

		AppConfiguration config = (AppConfiguration)session.getAttribute(SessionAttributes.APP_CONFIG);
		String finalApkFileRelativeLocation = APK_RELATIVE_DOWNLOADS_DIRECTORY + apkFinalName;
		config.setApkLink(finalApkFileRelativeLocation);
		session.setAttribute(SessionAttributes.APP_CONFIG, config);
	}
	
	private void copyDefaultBuildFilesToStagingArea(File baseBuildDir) throws IOException
	{
		File source = new File(ORIGINAL_BUILD_DIRECTORY);
		//TODO we may want to invoke OS level call to do this instead.
		SagFileUtils.copy(source, baseBuildDir);
	}

	public File getSessionBuildDirectory() throws IOException
	{
		String tempBuildDirName = getRandomDirectoryFileName();
		File baseBuildDir = new File(MAIN_BUILD_DIRECTORY, tempBuildDirName);
		if(baseBuildDir.exists())
			throw new IOException("Random build directory exists?");
		if(!baseBuildDir.mkdirs())
			throw new IOException("Unable to create directories:" + baseBuildDir.getAbsolutePath());
		File downloadsDirectory = new File(APK_LOCAL_DOWNLOADS_DIRECTORY);
		if(!downloadsDirectory.exists())
			if(!downloadsDirectory.mkdir())
				throw new IOException("Unable to create downloads directory:" + downloadsDirectory.getAbsolutePath());
		return baseBuildDir;
	}
	
	public static String getRandomDirectoryFileName() throws IOException
		{
		    final File temp;
		    temp = File.createTempFile("build", Long.toString(System.nanoTime()));
		    temp.delete();
		    return temp.getName();
		}
	
}