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
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Date;
import java.util.concurrent.TimeUnit;

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
	public static final String ORIGINAL_BUILD_DIRECTORY = "/Users/charlesl/EclipseMartus/martus-android/secure-app-vital-voices"; 
    public static final String GRADLE_GENERATED_SETTINGS_LOCAL = "/generated.build.gradle";
    public static final String VERSION_BUILD_XML = "versionBuild";
    public static final String VERSION_MINOR_XML = "versionMinor";
    public static final String VERSION_MAJOR_XML = "versionMajor";

	private static final String APP_NAME_XML = "appName";
	private static final String VERSION_SAG_BUILD_XML = "versionSagBuild";
    private static final String LOGO_NAME_PNG = "ic_launcher.png";
	private static final String XML_DESKTOP_PUBLIC_KEY = "public_key_desktop";
	private static final String XML_MARTUS_SERVER_PUBLIC_KEY = "martus_server_public_key";
	private static final String XML_MARTUS_SERVER_IP = "martus_server_ip";
	private static final String XML_APP_NAME = "app_name";
	private static final String GRADLE_LOCATION = "/Users/charlesl/Dev/gradle-2.3/bin/gradle";
    private static final String GRADLE_PARAMETERS = " -p ";
	private static final String GRADLE_BUILD_COMMAND = " build";
	private static final String APK_LOCAL_FILE_DIRECTORY = "/build/outputs/apk/";
	private static final String MAIN_DIRECTORY = "/Users/charlesl/SAG";
	private static final String MAIN_BUILD_DIRECTORY = MAIN_DIRECTORY + "/Build";
    private static final String GRADLE_SETTINGS = MAIN_BUILD_DIRECTORY + "/settings.gradle";
    private static final String APK_RESOURCE_FILE_LOCAL = "/res/values/non-traslatable-auto-generated-resources.xml";
    private static final String APK_HDPI_FILE_LOCAL = "/res/drawable-hdpi/";
    private static final String APK_MDPI_FILE_LOCAL = "/res/drawable-mdpi/";
    private static final String APK_NODPI_FILE_LOCAL = "/res/drawable-nodpi/";
    private static final String APK_XHDPI_FILE_LOCAL = "/res/drawable-xhdpi/";
    private static final String APK_XXHDPI_FILE_LOCAL = "/res/drawable-xxhdpi/";
    private static final String APK_XFORM_FILE_LOCAL = "/assets/xforms/sample.xml";
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
			updateApkSettings(baseBuildDir, config);
			updateGradleSettings(baseBuildDir, config);
			copyIconToApkBuild(baseBuildDir, config.getAppIconLocation());
			copyFormToApkBuild(baseBuildDir, config.getAppXFormLocation());
			File apkCreated = buildApk(baseBuildDir, config);
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

	private void copyFormToApkBuild(File baseBuildDir, String appXFormLocation) throws IOException
	{
		File source = new File(SecureAppGeneratorApplication.WEB_STATIC_DIRECTORY, appXFormLocation);
		File destination = new File(baseBuildDir, APK_XFORM_FILE_LOCAL);
		SagFileUtils.copy(source, destination);
	}

	private void copyIconToApkBuild(File baseBuildDir, String appIconLocation) throws IOException
	{
		//TODO adjust resolution
		File source = new File(SecureAppGeneratorApplication.WEB_STATIC_DIRECTORY, appIconLocation);
		File destination = new File(baseBuildDir, APK_NODPI_FILE_LOCAL + LOGO_NAME_PNG);
		SagFileUtils.copy(source, destination);
		destination = new File(baseBuildDir, APK_MDPI_FILE_LOCAL + LOGO_NAME_PNG);
		SagFileUtils.copy(source, destination);
		destination = new File(baseBuildDir, APK_HDPI_FILE_LOCAL + LOGO_NAME_PNG);
		SagFileUtils.copy(source, destination);
		destination = new File(baseBuildDir, APK_XHDPI_FILE_LOCAL + LOGO_NAME_PNG);
		SagFileUtils.copy(source, destination);
		destination = new File(baseBuildDir, APK_XXHDPI_FILE_LOCAL + LOGO_NAME_PNG);
		SagFileUtils.copy(source, destination);
	}

	private void updateGradleSettings(File baseBuildDir, AppConfiguration config) throws IOException
	{
		StringBuilder data = new StringBuilder("");
		appendGradleValue(data, VERSION_MAJOR_XML, config.getApkVersionMajor());
		appendGradleValue(data, VERSION_MINOR_XML, config.getApkVersionMinor());
		appendGradleValue(data, VERSION_BUILD_XML, config.getApkVersionBuild());
		appendGradleValue(data, VERSION_SAG_BUILD_XML, config.getApkSagVersionBuild());
		appendGradleValue(data, APP_NAME_XML, config.getAppName());

		File apkResourseFile = new File(baseBuildDir, GRADLE_GENERATED_SETTINGS_LOCAL);
  		FileOutputStream fileOutputStream = new FileOutputStream(apkResourseFile);
		BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(fileOutputStream,"UTF-8"));       
   		writer.write(data.toString());
   		writer.flush();
   		writer.close();
 	}
		
	private void updateApkSettings(File baseBuildDir, AppConfiguration config) throws IOException
	{
		StringBuilder data = new StringBuilder("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n");
		data.append("<resources>\n");
		appendKeyValue(data, XML_APP_NAME, config.getAppName());
		appendKeyValue(data, XML_MARTUS_SERVER_IP, config.getServerIP());
		appendKeyValue(data, XML_MARTUS_SERVER_PUBLIC_KEY, config.getServerPublicKey());
		appendKeyValue(data, XML_DESKTOP_PUBLIC_KEY, config.getClientPublicKey());
		data.append("</resources>\n");

		File apkResourseFile = new File(baseBuildDir, APK_RESOURCE_FILE_LOCAL);
  		FileOutputStream fileOutputStream = new FileOutputStream(apkResourseFile);
		BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(fileOutputStream,"UTF-8"));       
   		writer.write(data.toString());
   		writer.flush();
   		writer.close();
 	}

	private void appendKeyValue(StringBuilder data, String key, String value)
	{
		data.append("<string name=\"");
		data.append(key);
		data.append("\">");
		data.append(value);
		data.append("</string>\n");
	}

	private void appendGradleValue(StringBuilder data, String key, String value)
	{
		data.append("project.ext.set(\"");
		data.append(key);
		data.append("\", \"");
		data.append(value);
		data.append("\")\n");
	}

	private File buildApk(File baseBuildDir, AppConfiguration config) throws IOException, InterruptedException
	{
		System.out.println("Building " + config.getApkName());
		Runtime rt = Runtime.getRuntime();
		addBaseBuildDirToGradleSettings(baseBuildDir);
   		String gradleCommand = GRADLE_LOCATION + GRADLE_PARAMETERS + baseBuildDir + GRADLE_BUILD_COMMAND;
		System.out.println(gradleCommand);
		long startTime = System.currentTimeMillis();
   		Process pr = rt.exec(gradleCommand);
    		pr.waitFor();
  		long endTime = System.currentTimeMillis();
   		long buildTime = endTime-startTime;
   		String timeToBuild = String.format("%02d:%02d", 
   			    TimeUnit.MILLISECONDS.toMinutes(buildTime) - 
   			    TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(buildTime)),
   			    TimeUnit.MILLISECONDS.toSeconds(buildTime) - 
   			    TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(buildTime)));

		System.out.println("Build took:" + timeToBuild);
 
		int returnCode = pr.exitValue();
   		if(returnCode != EXIT_VALUE_GRADLE_SUCCESS)
	   		throw new IOException("Error creating APK");

   		String tempApkBuildFileDirectory = baseBuildDir.getAbsolutePath() + APK_LOCAL_FILE_DIRECTORY;
		File appFileCreated = new File(tempApkBuildFileDirectory, config.getGradleApkRawBuildFileName());
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
		String finalApkBuildFile = SecureAppGeneratorApplication.APK_LOCAL_DOWNLOADS_DIRECTORY + apkFinalName;
		File targetFile = new File(finalApkBuildFile);
		Path target = targetFile.toPath();
		if(targetFile.exists())
			throw new IOException("This build already exists.");
		Files.move(source, target, StandardCopyOption.ATOMIC_MOVE);

		AppConfiguration config = (AppConfiguration)session.getAttribute(SessionAttributes.APP_CONFIG);
		String finalApkFileRelativeLocation = SecureAppGeneratorApplication.APK_RELATIVE_DOWNLOADS_DIRECTORY + apkFinalName;
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
		File downloadsDirectory = new File(SecureAppGeneratorApplication.APK_LOCAL_DOWNLOADS_DIRECTORY);
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
