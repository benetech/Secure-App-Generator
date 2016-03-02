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

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Iterator;
import java.util.List;

public class AppConfiguration
{
	private static final String VERSION = "SAG Beta 98";
	private static final String COPY_RIGHT = "This website is &#169; Copyright 2015-2016, Beneficent Technology, Inc.";
	private static final char DASH_CHAR = '-';
	private static final String APK_EXTENSION = ".apk";
	private static final String DEBUG_APK_EXTENSION = "-release" + APK_EXTENSION;
	private static final char DOT_CHAR = '.';
	private static final char UNDERSCORE_CHAR = '_';
	private static final char SPACE_CHAR = ' ';
    private static final String APK_NOT_BUILT = "false";
    private static final String APK_BUILT_ERROR = "error";
	private static final int SHORT_NAME_MAX_CHARACTER_LIMIT = 15;
	
	private String appName;
	private String appNameShort;
	private String appNameError;
	private String appIconLocation;
	private String appIconLocalFileLocation;
	private String appIconBase64Data;
	private String appIconError;
	private String appXFormName;
	private File appXFormFile;
	private String appXFormError;
	private String clientToken;
	private String clientTokenError;
	private String clientPublicKey;
	private String clientPublicCode;
	private String serverName;
	private String serverIP;
	private String serverPublicKey;
	private String apkVersionMajor = "0";
	private String apkVersionMinor = "0";
	private String apkVersionBuild = "0";
	private String apkSagVersionBuild = "0";
	private String apkBuildError;
	private String apkURL;
	private String apkBuildResult;
	private String copyright;
	
	public AppConfiguration()
	{
		InitializeVariables();
	}
	
	private void InitializeVariables()
	{
		resetVersion();
		apkBuildResult = APK_NOT_BUILT;
		copyright = COPY_RIGHT;
		appName = "";
	}

	public void resetVersion()
	{
		setApkSagVersionBuild(VERSION);
		try
		{
			setBuildVersionFromGeneratedSettingsFile(this);
		}
		catch (IOException e)
		{
			SagLogger.logException(null, e);
		}
	}
	
	public void setAppName(String appName)
	{
		this.appName = appName;
		setAppNameShort(appName);
	}
	
	public String getAppName()
	{
		return appName;
	}

	public String getAppNameShort()
	{
		return appNameShort;
	}

	private void setAppNameShort(String appFullName)
	{
		if(appFullName.length() > SHORT_NAME_MAX_CHARACTER_LIMIT)
		{
			StringBuilder shortName = new StringBuilder(appFullName.substring(0, SHORT_NAME_MAX_CHARACTER_LIMIT-3));
			shortName.append("...");
			appNameShort = shortName.toString();
		}
		else
		{
			appNameShort = appFullName;
		}
	}

	public String getAppNameError()
	{
		return appNameError;
	}

	public void setAppNameError(String appNameErrorId)
	{
		this.appNameError = SecureAppGeneratorApplication.getLocalizedErrorMessage(appNameErrorId);
	}

	public String getAppIconLocation()
	{
		return appIconLocation;
	}

	public void setAppIconLocation(String appIconLocation)
	{
		this.appIconLocation = appIconLocation;
	}
	
	@Override
	public String toString()
	{
		return "Name: " + appName + ", Icon Loc: " + appIconLocation + ", XForm Loc: " + appXFormFile.getAbsolutePath();
	}

	public String getAppIconError()
	{
		return appIconError;
	}

	public void setAppIconError(String appIconErrorId)
	{
		this.appIconError = SecureAppGeneratorApplication.getLocalizedErrorMessage(appIconErrorId);
	}

	public String getAppXFormName()
	{
		return appXFormName;
	}

	public void setAppXFormName(String appXFormName)
	{
		this.appXFormName = appXFormName;
	}

	public File getAppXFormFile()
	{
		return appXFormFile;
	}

	public void setAppXFormFile(File appXFormFile)
	{
		this.appXFormFile = appXFormFile;
	}

	public String getAppXFormError()
	{
		return appXFormError;
	}

	public void setAppXFormError(String appXFormErrorId)
	{
		setAppXFormErrorRaw(SecureAppGeneratorApplication.getLocalizedErrorMessage(appXFormErrorId));
	}

	public void setAppXFormErrorRaw(String appXFormErrorMessage)
	{
		this.appXFormError = appXFormErrorMessage;
	}

	public String getClientToken()
	{
		return clientToken;
	}

	public void setClientToken(String clientToken)
	{
		this.clientToken = clientToken;
	}

	public String getClientTokenError()
	{
		return clientTokenError;
	}

	public void setClientTokenError(String clientTokenErrorId)
	{
		this.clientTokenError = SecureAppGeneratorApplication.getLocalizedErrorMessage(clientTokenErrorId);
	}

	public String getClientPublicKey()
	{
		return clientPublicKey;
	}

	public void setClientPublicKey(String clientPublicKey)
	{
		this.clientPublicKey = clientPublicKey;
	}

	public String getClientPublicCode()
	{
		return clientPublicCode;
	}

	public void setClientPublicCode(String clientPublicCode)
	{
		this.clientPublicCode = clientPublicCode;
	}	
	
	public String getServerName()
	{
		return serverName;
	}

	public void setServerName(String serverName)
	{
		this.serverName = serverName;
	}

	public String getServerIP()
	{
		return serverIP;
	}

	public void setServerIP(String serverIP)
	{
		this.serverIP = serverIP;
	}

	public String getServerPublicKey()
	{
		return serverPublicKey;
	}

	public void setServerPublicKey(String serverPublicKey)
	{
		this.serverPublicKey = serverPublicKey;
	}

	public String getApkVersionMajor()
	{
		return apkVersionMajor;
	}

	public void setApkVersionMajor(String apkVersionMajor)
	{
		this.apkVersionMajor = apkVersionMajor;
	}

	public String getApkVersionMinor()
	{
		return apkVersionMinor;
	}

	public void setApkVersionMinor(String apkVersionMinor)
	{
		this.apkVersionMinor = apkVersionMinor;
	}

	public String getApkVersionBuild()
	{
		return apkVersionBuild;
	}

	public void setApkVersionBuild(String apkVersionBuild)
	{
		this.apkVersionBuild = apkVersionBuild;
	}

	public String getApkSagVersionBuild()
	{
		return apkSagVersionBuild;
	}

	public void setApkSagVersionBuild(String apkSagVersionBuild)
	{
		this.apkSagVersionBuild = apkSagVersionBuild;
	}

	public String getApkBuildError()
	{
		return apkBuildError;
	}

	public void setApkBuildError(String apkBuildErrorId)
	{
		this.apkBuildError = SecureAppGeneratorApplication.getLocalizedErrorMessage(apkBuildErrorId);
		setApkBuilt(APK_BUILT_ERROR);	
	}
	
	public String getApkVersionNumberFull()
	{
		StringBuilder versionNumber = new StringBuilder(apkVersionMajor);
		versionNumber.append(DOT_CHAR);
		versionNumber.append(apkVersionMinor);
		versionNumber.append(DOT_CHAR);
		versionNumber.append(apkVersionBuild);
		versionNumber.append(DOT_CHAR);
		versionNumber.append(apkSagVersionBuild);
		return versionNumber.toString();
	}

	public String getGradleApkRawBuildFileName()
	{
		StringBuilder totalFileName = new StringBuilder(appName);
		totalFileName.append(DASH_CHAR);
		totalFileName.append(getApkVersionNumberFull());
		totalFileName.append(DEBUG_APK_EXTENSION);
		return totalFileName.toString();
	}

	public String getApkName()
	{
        String appNameWithoutSpaces = getAppNameWithoutSpaces();
        StringBuilder apkName = new StringBuilder(appNameWithoutSpaces);
        apkName.append(DASH_CHAR);
		apkName.append(getApkVersionNumberFull());
        apkName.append(APK_EXTENSION);
		return apkName.toString();
	}

	protected String getAppNameWithoutSpaces()
	{
		return appName.replace(SPACE_CHAR, UNDERSCORE_CHAR);
	}

	public String getAppIconBase64Data()
	{
		return appIconBase64Data;
	}

	public void setAppIconBase64Data(String appIconBase64Data)
	{
		this.appIconBase64Data = appIconBase64Data;
	}

	public String getAppIconLocalFileLocation()
	{
		return appIconLocalFileLocation;
	}

	public void setAppIconLocalFileLocation(String appIconLocalFileLocation)
	{
		this.appIconLocalFileLocation = appIconLocalFileLocation;
	}

	public String getApkURL()
	{
		return apkURL;
	}

	public void setApkURL(String apkURL)
	{
		this.apkURL = apkURL;
	}

	public String getApkBuilt()
	{
		return apkBuildResult;
	}

	public void setApkBuilt(String apkBuildResult)
	{
		this.apkBuildResult = apkBuildResult;
	}

	public String getCopyright()
	{
		return copyright;
	}
	
	public static void setBuildVersionFromGeneratedSettingsFile(AppConfiguration config) throws IOException
	{
		File apkResourseFile = new File(SecureAppGeneratorApplication.getOriginalBuildDirectory(), BuildingApkController.GRADLE_GENERATED_SETTINGS_LOCAL);
		List<String> lines = Files.readAllLines(apkResourseFile.toPath());
		for (Iterator<String> iterator = lines.iterator(); iterator.hasNext();)
		{
			String currentLine = iterator.next();
			if(currentLine.contains(BuildingApkController.VERSION_MAJOR_XML))
		        config.setApkVersionMajor(extractVersionInformationFromLine(currentLine));
			if(currentLine.contains(BuildingApkController.VERSION_MINOR_XML))
		        config.setApkVersionMinor(extractVersionInformationFromLine(currentLine));
			if(currentLine.contains(BuildingApkController.VERSION_BUILD_XML))
		        config.setApkVersionBuild(extractVersionInformationFromLine(currentLine));
		}
	}

	private static String extractVersionInformationFromLine(String currentLine)
	{
		//Line prototype: project.ext.set("versionMajor", "0") 
		String[] data = currentLine.split("\"");
		if(data.length < 4)
			return "0";
		return data[3];
	}

	
	
}
