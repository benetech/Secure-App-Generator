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

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.FileUtils;
import org.benetech.secureapp.generator.AmazonS3Utils.S3Exception;
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
	public static final String APP_BASE_APPLICATION_ID = "org.benetech.secureapp.";
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
		if(session == null)
		{
			SagLogger.logError(session, "isAPKBuilt called with HttpSession:null");
			return AppConfiguration.APK_BUILT_ERROR;
		}
		AppConfiguration config = (AppConfiguration) session.getAttribute(SessionAttributes.APP_CONFIG);
		if(config == null)
		{
			SagLogger.logError(session, "isAPKBuilt called with AppConfiguration:null");
			SecureAppGeneratorApplication.setInvalidResults(session, "Internal Error");
			return AppConfiguration.APK_BUILT_ERROR;
		}
		return config.getApkBuilt();
	}

	@RequestMapping(value=WebPage.BUILDING_APK, method=RequestMethod.GET)
    public String directError(HttpSession session, Model model) 
    {
		SagLogger.logWarning(session, "BUILDING_APK Get Request");
		SecureAppGeneratorApplication.setInvalidResults(session);
        return WebPage.ERROR;
    }

	@RequestMapping(value=WebPage.BUILDING_APK_NEXT, method=RequestMethod.POST)	
	public String finishedBuild(HttpSession session, Model model) throws Exception 
    {
		AppConfiguration config = (AppConfiguration)session.getAttribute(SessionAttributes.APP_CONFIG);
		model.addAttribute(SessionAttributes.APP_CONFIG, config);
		if(config.getApkBuildError() == null)
		{
			SagLogger.logInfo(session, "Finished SAG");
			return WebPage.FINAL;
		}
		SagLogger.logError(session, "SAG Build Failed.");
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
		File appXFormFileToUse = config.getAppXFormFile();
		appXFormFileToUse.deleteOnExit();
		copyFormToApkBuild(secureAppBuildDir, appXFormFileToUse);
		File apkCreated = buildApk(session, secureAppBuildDir, config);
		File renamedApk = renameApk(apkCreated, config);
		copyApkToDownloads(session, renamedApk);
		if(Fdroid.includeFDroid())
			Fdroid.copyApkToFDroid(session, renamedApk);
		model.addAttribute(SessionAttributes.APP_CONFIG, config);
		session.setAttribute(SessionAttributes.APP_CONFIG, config);

		cleanupBuildDirectory(session, secureAppBuildDir, appXFormFileToUse);
	}

	private static void cleanupBuildDirectory(HttpSession session,
			File secureAppBuildDir, File appXFormFileToUse)
	{
		File rootDirectory = secureAppBuildDir.getParentFile().getParentFile();
		LogMemoryCheck(session, "Before Delete BuildDir", rootDirectory);
		try
		{
			if(secureAppBuildDir != null)
				FileUtils.deleteDirectory(secureAppBuildDir.getParentFile());
			appXFormFileToUse.delete();
		}
		catch (IOException e)
		{
			SagLogger.logException(session, e);			
		}	
		LogMemoryCheck(session, "After Delete Build", rootDirectory);
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
		LogMemoryCheck(session, "Before File copy", baseBuildDir);
		copyDefaultBuildFilesToStagingArea(session, baseBuildDir);
		LogMemoryCheck(session, "After File copy", baseBuildDir);
		return new File(baseBuildDir, SECURE_APP_PROJECT_DIRECTORY);
	}

	private static void LogMemoryCheck(HttpSession session, String description, File baseBuildDir)
	{
		long totalSpace = getMegaBytes(baseBuildDir.getTotalSpace());
		long freeSpace = getMegaBytes(baseBuildDir.getFreeSpace());
		long usableSpace = getMegaBytes(baseBuildDir.getUsableSpace());
		
		int processors = Runtime.getRuntime().availableProcessors();
		long freeMemory = getMegaBytes(Runtime.getRuntime().freeMemory());
		long maxMemory = Runtime.getRuntime().maxMemory();
		String jvmMemory = (maxMemory == Long.MAX_VALUE ? "no limit" : String.valueOf(getMegaBytes(maxMemory)));
		long totalMemory = getMegaBytes(Runtime.getRuntime().totalMemory());
		StringBuilder memoryUsed = new StringBuilder();
		memoryUsed.append("MEMORY CHECK: ");
		memoryUsed.append(description);
		memoryUsed.append(": Processors = ");
		memoryUsed.append(processors);
		memoryUsed.append(", Total Memory = ");
		memoryUsed.append(totalMemory);
		memoryUsed.append(" MB, JVM Memory = ");
		memoryUsed.append(jvmMemory);
		memoryUsed.append(" MB, Free Memory = ");
		memoryUsed.append(freeMemory);
		memoryUsed.append(" MB -- Disk Total Space = ");
		memoryUsed.append(totalSpace);
		memoryUsed.append(" MB, Disk Usable Space = ");
		memoryUsed.append(usableSpace);
		memoryUsed.append(" MB, Disk Free Space = ");
		memoryUsed.append(freeSpace);
		memoryUsed.append(" MB.");
		SagLogger.logInfo(session, memoryUsed.toString());
	}

	public static long getMegaBytes(long size)
	{
		return size / 1048576;
	}

	static private void copyFormToApkBuild(File baseBuildDir, File appXFormFile) throws IOException
	{
		File destination = new File(baseBuildDir, APK_XFORM_FILE_LOCAL);
		FileUtils.copyFile(appXFormFile, destination);
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
		String uniqueAppId = getUniqueAppId(config.getAppNameWithoutSpaces());
		appendGradleValue(data, APP_CUSTOM_APPLICATION_ID_XML, uniqueAppId);

		File apkResourseFile = new File(baseBuildDir, GRADLE_GENERATED_SETTINGS_FILE);
  		SecureAppGeneratorApplication.writeDataToFile(apkResourseFile, data);
 	}
		
	public static String getUniqueAppId(String appNameWithoutSpaces)
	{
		String hashName = APP_BASE_APPLICATION_ID;
		hashName += convertToAlpha(appNameWithoutSpaces.hashCode());
		hashName += ".";
		hashName += convertToAlpha(System.currentTimeMillis());
		return hashName;
	}

	private static String convertToAlpha(long value)
	{
		String currentTime = String.valueOf(Math.abs(value));
		String characterOnly = "";
		for(int i = 0; i < currentTime.length(); ++i)
		{
			char tmp = currentTime.charAt(i);
			tmp += 17;
			characterOnly += tmp;
		}
		return characterOnly;
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
		SagLogger.logInfo(session, "Building " + config.getApkName());
		String includeLogging = "";
		includeLogging = GRADLE_BUILD_COMMAND_LOGGING;
		String gradleCommand = SecureAppGeneratorApplication.getGadleDirectory() + GRADLE_EXE + GRADLE_PARAMETERS + baseBuildDir + includeLogging + GRADLE_BUILD_COMMAND_RELEASE;
		long startTime = System.currentTimeMillis();
		int returnCode = SecureAppGeneratorApplication.executeCommand(session, gradleCommand, null);
  		long endTime = System.currentTimeMillis();
  		String timeToBuild = SagLogger.getElapsedTime(startTime, endTime);

    		if(returnCode != EXIT_VALUE_GRADLE_SUCCESS)
   		{
   			SagLogger.logError(session, "Build return code:" + returnCode);
	   		throw new BuildException("Error creating APK");
   		}
    		
  		SagLogger.logInfo(session, "Build succeeded:" + timeToBuild);
  		String tempaApkBuildFileDirectory = baseBuildDir.getAbsolutePath() + APK_LOCAL_FILE_DIRECTORY;
		File appFileCreated = new File(tempaApkBuildFileDirectory, config.getGradleApkRawBuildFileName());
		return appFileCreated;
	}

	static public void copyApkToDownloads(final HttpSession session, final File apkFile) throws S3Exception
	{
		SagLogger.logDebug(session, "Uploading APK To S3");
		AmazonS3Utils.uploadToAmazonS3(session, apkFile);
		SagLogger.logDebug(session, "Upload Complete.");
	}
	
	static private void copyDefaultBuildFilesToStagingArea(HttpSession session, File baseBuildDir) throws IOException
	{
		File source = new File(SecureAppGeneratorApplication.getOriginalBuildDirectory());
		SagLogger.logInfo(session, "Copying Build directory, from:" + source.getAbsolutePath() + " to: "+ baseBuildDir.getAbsolutePath());
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
				SagLogger.logException(session, e);
				config.setApkBuildError("generating_apk");
			}
			model.addAttribute(SessionAttributes.APP_CONFIG, config);
			session.setAttribute(SessionAttributes.APP_CONFIG, config);
		}
	}
}

