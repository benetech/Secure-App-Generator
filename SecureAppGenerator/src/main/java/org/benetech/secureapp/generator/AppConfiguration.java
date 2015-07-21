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

public class AppConfiguration
{
	private static final char DASH_CHAR = '-';
	private static final String APK_EXTENSION = ".apk";
	private static final String DEBUG_APK_EXTENSION = "-debug" + APK_EXTENSION;
	private static final char DOT_CHAR = '.';
	private static final char UNDERSCORE_CHAR = '_';
	private static final char SPACE_CHAR = ' ';
	
	private String appName;
	private String appNameError;
	private String appIconLocation;
	private String appIconLocalFileLocation;
	private String appIconBase64Data;
	private String appIconError;
	private String appXFormName;
	private String appXFormLocation;
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
	
	public void setAppName(String appName)
	{
		this.appName = appName;
	}
	
	public String getAppName()
	{
		return appName;
	}

	public String getAppNameError()
	{
		return appNameError;
	}

	public void setAppNameError(String appNameError)
	{
		this.appNameError = appNameError;
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
		return "Name: " + appName + ", Icon Loc: " + appIconLocation + ", XForm Loc: " + appXFormLocation;
	}

	public String getAppIconError()
	{
		return appIconError;
	}

	public void setAppIconError(String appIconError)
	{
		this.appIconError = appIconError;
	}

	public String getAppXFormName()
	{
		return appXFormName;
	}

	public void setAppXFormName(String appXFormName)
	{
		this.appXFormName = appXFormName;
	}

	public String getAppXFormLocation()
	{
		return appXFormLocation;
	}

	public void setAppXFormLocation(String appXFormLocation)
	{
		this.appXFormLocation = appXFormLocation;
	}

	public String getAppXFormError()
	{
		return appXFormError;
	}

	public void setAppXFormError(String appXFormError)
	{
		this.appXFormError = appXFormError;
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

	public void setClientTokenError(String clientTokenError)
	{
		this.clientTokenError = clientTokenError;
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

	public void setApkBuildError(String apkBuildError)
	{
		this.apkBuildError = apkBuildError;
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
        String appNameWithoutSpaces = appName.replace(SPACE_CHAR, UNDERSCORE_CHAR);
        StringBuilder apkName = new StringBuilder(appNameWithoutSpaces);
        apkName.append(DASH_CHAR);
		apkName.append(getApkVersionNumberFull());
        apkName.append(APK_EXTENSION);
		return apkName.toString();
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

}
