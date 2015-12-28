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

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.FileUtils;
import org.benetech.secureapp.generator.AmazonS3Utils.S3Exception;
import org.benetech.secureapp.generator.BuildException;
import org.imgscalr.Scalr;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Controller
public class BuildingApkController extends WebMvcConfigurerAdapter
{
	private static final String PNG_TYPE = "png";
	public static final String VERSION_BUILD_XML = "versionBuild";
    public static final String VERSION_MINOR_XML = "versionMinor";
    public static final String VERSION_MAJOR_XML = "versionMajor";

    static final int APK_NODPI_SIZE = 36;
    static final int APK_MDPI_SIZE = 48;
    static final int APK_HDPI_SIZE = 72;
    static final int APK_XHDPI_SIZE = 96;
    static final int APK_XXHDPI_SIZE = 144;

    private static final String APP_NAME_XML = "appName";
	private static final String APP_CUSTOM_APPLICATION_ID_XML = "customApplicationId";
	private static final String APP_BASE_APPLICATION_ID = "org.benetech.secureapp.";
	private static final String VERSION_SAG_BUILD_XML = "versionSagBuild";
    private static final String LOGO_NAME_PNG = "ic_launcher_secure_app.png";
	private static final String XML_DESKTOP_PUBLIC_KEY = "public_key_desktop";
	private static final String XML_MARTUS_SERVER_PUBLIC_KEY = "martus_server_public_key";
	private static final String XML_MARTUS_SERVER_IP = "martus_server_ip";
	private static final String XML_APP_NAME = "app_name";
	private static final String GRADLE_EXE = "/bin/gradle";
    private static final String GRADLE_PARAMETERS = " -p ";
	private static final String GRADLE_BUILD_COMMAND_LOGGING = " --stacktrace --debug";
	private static final String GRADLE_BUILD_COMMAND_RELEASE = " assemblerelease";
	private static final String APK_LOCAL_FILE_DIRECTORY = "/build/outputs/apk/";
    private static final String APK_RESOURCE_FILE_LOCAL = "/res/values/non-traslatable-auto-generated-resources.xml";
    private static final String APK_NODPI_FILE_LOCAL = "/res/drawable-nodpi/";
    private static final String APK_MDPI_FILE_LOCAL = "/res/drawable-mdpi/";
    private static final String APK_HDPI_FILE_LOCAL = "/res/drawable-hdpi/";
    private static final String APK_XHDPI_FILE_LOCAL = "/res/drawable-xhdpi/";
    private static final String APK_XXHDPI_FILE_LOCAL = "/res/drawable-xxhdpi/";
    private static final String APK_XFORM_FILE_LOCAL = "/assets/xforms/sample.xml";
    private static final String SECURE_APP_PROJECT_DIRECTORY = "secure-app";
    private static final String GRADLE_GENERATED_SETTINGS_FILE = "generated.build.gradle";
    public static final String GRADLE_GENERATED_SETTINGS_LOCAL = SECURE_APP_PROJECT_DIRECTORY + "/" + GRADLE_GENERATED_SETTINGS_FILE;
    private static final int EXIT_VALUE_GRADLE_SUCCESS = 0;
    private static final String APK_BUILT_SUCCESS = "true";
    
    @RequestMapping(value = "/buildingApk/isAPKBuilt", method = RequestMethod.POST)
	@ResponseBody
	public String isAPKBuilt(HttpSession session)
	{
		AppConfiguration config = (AppConfiguration) session.getAttribute(SessionAttributes.APP_CONFIG);
		return config.getApkBuilt();
	}

	@RequestMapping(value=WebPage.BUILDING_APK, method=RequestMethod.GET)
    public String directError(HttpSession session, Model model) 
    {
		SecureAppGeneratorApplication.setInvalidResults(session);
        return WebPage.ERROR;
    }

	@RequestMapping(value=WebPage.BUILDING_APK_NEXT, method=RequestMethod.POST)	
	public String initiateBuild(HttpSession session, Model model) throws Exception 
    {
		AppConfiguration config = (AppConfiguration)session.getAttribute(SessionAttributes.APP_CONFIG);
		model.addAttribute(SessionAttributes.APP_CONFIG, config);
		if(config.getApkBuildError() == null)
			return WebPage.FINAL;

		SecureAppGeneratorApplication.setInvalidResults(session, config.getApkBuildError());
		return WebPage.ERROR;
    }
	
	static public void startTheBuild(HttpSession session, Model model)
    {
		BuildApkThread thread = new BuildApkThread(session, model);
		thread.start();
    }
    
	static private void initiateSyncronousApkBuild(HttpSession session, Model model) throws IOException, InterruptedException, S3Exception, BuildException, Exception
	{
		File secureAppBuildDir = null;
		secureAppBuildDir = configureSecureAppBuildDirectory(session);
		AppConfiguration config = (AppConfiguration)session.getAttribute(SessionAttributes.APP_CONFIG);
		updateApkSettings(secureAppBuildDir, config);
		updateGradleSettings(secureAppBuildDir, config);
		copyIconToApkBuild(secureAppBuildDir, config.getAppIconLocalFileLocation());
		ObtainLogoController.deleteLogo(config);
		copyFormToApkBuild(secureAppBuildDir, config.getAppXFormLocation());
		File apkCreated = buildApk(session, secureAppBuildDir, config);
		File renamedApk = renameApk(apkCreated, config);
		copyApkToDownloads(session, renamedApk);
		if(Fdroid.includeFDroid())
			Fdroid.copyApkToFDroid(session, renamedApk);
		model.addAttribute(SessionAttributes.APP_CONFIG, config);
		session.setAttribute(SessionAttributes.APP_CONFIG, config);
		try
		{
			if(secureAppBuildDir != null)
				FileUtils.deleteDirectory(secureAppBuildDir.getParentFile());
			//TODO delete XForm
		}
		catch (IOException e)
		{
			Logger.logException(session, e);			
		}			
	}
	

	static private File renameApk(File apkCreated, AppConfiguration config) throws IOException
	{
		File finalFile = new File(apkCreated.getParent(), config.getApkName());
		FileUtils.moveFile(apkCreated, finalFile);
		return finalFile;
	}

	static private File configureSecureAppBuildDirectory(HttpSession session) throws IOException
	{
		File baseBuildDir = getSessionBuildDirectory();
		copyDefaultBuildFilesToStagingArea(session, baseBuildDir);
		return new File(baseBuildDir, SECURE_APP_PROJECT_DIRECTORY);
	}

	static private void copyFormToApkBuild(File baseBuildDir, String appXFormLocation) throws IOException
	{
		File source = new File(appXFormLocation);
		File destination = new File(baseBuildDir, APK_XFORM_FILE_LOCAL);
		FileUtils.copyFile(source, destination);
	}

	static private void copyIconToApkBuild(File baseBuildDir, String appIconLocation) throws IOException
	{
		resizeAndSavePngImage(baseBuildDir, appIconLocation, APK_NODPI_SIZE, APK_NODPI_FILE_LOCAL);
		resizeAndSavePngImage(baseBuildDir, appIconLocation, APK_MDPI_SIZE, APK_MDPI_FILE_LOCAL);
		resizeAndSavePngImage(baseBuildDir, appIconLocation, APK_HDPI_SIZE, APK_HDPI_FILE_LOCAL);
		resizeAndSavePngImage(baseBuildDir, appIconLocation, APK_XHDPI_SIZE, APK_XHDPI_FILE_LOCAL);
		resizeAndSavePngImage(baseBuildDir, appIconLocation, APK_XXHDPI_SIZE, APK_XXHDPI_FILE_LOCAL);
	}

	public static File resizeAndSavePngImage(File baseBuildDir, String appIconLocation, int resizeValue, String apkFileName)
			throws IOException
	{
		File source = new File(appIconLocation);
		BufferedImage originalImage = ImageIO.read(source);
		BufferedImage scaledImg = Scalr.resize(originalImage, Scalr.Mode.AUTOMATIC, resizeValue, resizeValue);
		File destination = new File(baseBuildDir, apkFileName + LOGO_NAME_PNG);
		ImageIO.write(scaledImg, PNG_TYPE, destination);
		return destination;
	}
	
	static private void updateGradleSettings(File baseBuildDir, AppConfiguration config) throws IOException
	{
		StringBuilder data = new StringBuilder("");
		appendGradleValue(data, VERSION_MAJOR_XML, config.getApkVersionMajor());
		appendGradleValue(data, VERSION_MINOR_XML, config.getApkVersionMinor());
		appendGradleValue(data, VERSION_BUILD_XML, config.getApkVersionBuild());
		appendGradleValue(data, VERSION_SAG_BUILD_XML, config.getApkSagVersionBuild());
		appendGradleValue(data, APP_NAME_XML, config.getAppName());
		appendGradleValue(data, APP_CUSTOM_APPLICATION_ID_XML, APP_BASE_APPLICATION_ID + config.getAppNameWithoutSpaces());

		File apkResourseFile = new File(baseBuildDir, GRADLE_GENERATED_SETTINGS_FILE);
  		SecureAppGeneratorApplication.writeDataToFile(apkResourseFile, data);
 	}
		
	static private void updateApkSettings(File baseBuildDir, AppConfiguration config) throws IOException
	{
		StringBuilder data = new StringBuilder("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n");
		data.append("<resources>\n");
		appendKeyValue(data, XML_APP_NAME, config.getAppName());
		appendKeyValue(data, XML_MARTUS_SERVER_IP, config.getServerIP());
		appendKeyValue(data, XML_MARTUS_SERVER_PUBLIC_KEY, config.getServerPublicKey());
		appendKeyValue(data, XML_DESKTOP_PUBLIC_KEY, config.getClientPublicKey());
		data.append("</resources>\n");

		File apkResourseFile = new File(baseBuildDir, APK_RESOURCE_FILE_LOCAL);
 		SecureAppGeneratorApplication.writeDataToFile(apkResourseFile, data);
 	}

	static private void appendKeyValue(StringBuilder data, String key, String value)
	{
		data.append("<string name=\"");
		data.append(key);
		data.append("\">");
		data.append(value);
		data.append("</string>\n");
	}

	static private void appendGradleValue(StringBuilder data, String key, String value)
	{
		data.append("project.ext.set(\"");
		data.append(key);
		data.append("\", \"");
		data.append(value);
		data.append("\")\n");
	}

	static private File buildApk(final HttpSession session, final File baseBuildDir, final AppConfiguration config) throws IOException, InterruptedException, BuildException
	{
		Logger.log(session, "Building " + config.getApkName());
		Logger.logMemoryStatistics();
		String includeLogging = "";
		includeLogging = GRADLE_BUILD_COMMAND_LOGGING;
		String gradleCommand = SecureAppGeneratorApplication.getGadleDirectory() + GRADLE_EXE + GRADLE_PARAMETERS + baseBuildDir + includeLogging + GRADLE_BUILD_COMMAND_RELEASE;
		long startTime = System.currentTimeMillis();
		int returnCode = SecureAppGeneratorApplication.executeCommand(session, gradleCommand, null);
  		long endTime = System.currentTimeMillis();
		Logger.logMemoryStatistics();
  		String timeToBuild = Logger.getElapsedTime(startTime, endTime);

    		if(returnCode != EXIT_VALUE_GRADLE_SUCCESS)
   		{
   			Logger.logError(session, "Build return code:" + returnCode);
	   		throw new BuildException("Error creating APK");
   		}
    		
  		Logger.log(session, "Build succeeded:" + timeToBuild);
  		String tempaApkBuildFileDirectory = baseBuildDir.getAbsolutePath() + APK_LOCAL_FILE_DIRECTORY;
		File appFileCreated = new File(tempaApkBuildFileDirectory, config.getGradleApkRawBuildFileName());
		return appFileCreated;
	}

	static public void copyApkToDownloads(final HttpSession session, final File apkFile) throws S3Exception
	{
		Logger.logVerbose(session, "Uploading APK To S3");
		AmazonS3Utils.uploadToAmazonS3(session, apkFile);
		Logger.logVerbose(session, "Upload Complete.");
	}
	
	static private void copyDefaultBuildFilesToStagingArea(HttpSession session, File baseBuildDir) throws IOException
	{
		File source = new File(SecureAppGeneratorApplication.getOriginalBuildDirectory());
		Logger.log(session, "Copying Build directory, from:" + source.getAbsolutePath() + " to: "+ baseBuildDir.getAbsolutePath());
		FileUtils.copyDirectory(source, baseBuildDir);
	}

	static private File getSessionBuildDirectory() throws IOException
	{
		File baseBuildDir = SecureAppGeneratorApplication.getRandomDirectoryFile("build");
		return baseBuildDir;
	}
	
	static class BuildApkThread extends Thread
	{
		private HttpSession session;
		private Model model;
		
		public BuildApkThread(HttpSession session, Model model)
		{
			this.session = session;
			this.model = model;
		}
		
		@Override
		public void run()
		{
			AppConfiguration config = (AppConfiguration)session.getAttribute(SessionAttributes.APP_CONFIG);
			try
			{
				initiateSyncronousApkBuild(session, model);
				config.setApkBuilt(APK_BUILT_SUCCESS);
			}
			catch (Exception e)
			{
				Logger.logException(session, e);
				config.setApkBuildError("generating_apk");
			}
			model.addAttribute(SessionAttributes.APP_CONFIG, config);
			session.setAttribute(SessionAttributes.APP_CONFIG, config);
		}
	}
}

