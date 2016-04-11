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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.amazonaws.util.json.JSONException;
import com.amazonaws.util.json.JSONObject;

@Controller
public class SmokeTestPageController extends WebMvcConfigurerAdapter
{
	private static final String KEY_BUILD_FILES = "Files:";
	private static final String KEY_FREE_DISK_SPACE_MB = "Free DiskSpace (MB)";
	private static final String KEY_S3_BUCKET = "S3 Bucket";
	private static final String VALUE_OK = "OK";
	private static final String KEY_TOKEN_SERVER = "Token Server";
	private static final String KEY_MARTUS_SERVER_IP = "Martus Server IP";
	private static final String KEY_MARTUS_SERVER_NAME = "Martus Server Name";
	private static final String KEY_SAG_VERSION = "Version";
	private static final String MASTER_BUILD_FILES = "BuildFiles.txt";
	private static final String SAG_FILES = "SagFiles.txt";
	private static final String TEST_APK_TO_CHECK = "SomeAPKToCheck";
	private static final String SMOKE_TEST_CLIENT_TOKEN_PRODUCTION = "2158797";
	private static final String SMOKE_TEST_CLIENT_TOKEN_DEV = "2876540";

	@ResponseBody 
	@RequestMapping(value=WebPage.SMOKETEST, method=RequestMethod.GET)
	public String smokeTest(HttpSession session, Model model) throws Exception 
    {
		SagLogger.logDebug(session, "SMOKE TEST Request");
		SecureAppGeneratorApplication.setInvalidResults(session);
		SecureAppGeneratorApplication.setupDefaultSessionAttributes(session);
		JSONObject smokeResults = new JSONObject();
		AppConfiguration config = new AppConfiguration();
		smokeResults.append(KEY_SAG_VERSION, config.getApkVersionNumberFull());
		setMartusServerUsed(config, smokeResults);
		testMartusTokenServer(session, config, smokeResults);
		testAmazonS3Server(session, smokeResults);
		testFreeDiskSpace(session, smokeResults);
		File originalBuildDirectory = new File(SecureAppGeneratorApplication.getOriginalBuildDirectory());
		testFilesExist(session, MASTER_BUILD_FILES, originalBuildDirectory,  smokeResults);
		testFilesExist(session, SAG_FILES, SecureAppGeneratorApplication.getStaticWebDirectory(), smokeResults);

		return smokeResults.toString();
    }

	private void setMartusServerUsed(AppConfiguration config, JSONObject result) throws JSONException
	{
		ServerConstants.setServerConfig(config);
		if(ServerConstants.usingRealMartusServer())
			config.setClientToken(SMOKE_TEST_CLIENT_TOKEN_PRODUCTION);
		else
			config.setClientToken(SMOKE_TEST_CLIENT_TOKEN_DEV);

		result.append(KEY_MARTUS_SERVER_NAME, config.getServerName());
		result.append(KEY_MARTUS_SERVER_IP, ServerConstants.getCurrentServerIp());
	}

	private void testMartusTokenServer(HttpSession session, AppConfiguration config, JSONObject results) throws Exception
	{
		try
		{
			ObtainTokenController.getClientPublicKeyFromToken(session, config);
			results.append(KEY_TOKEN_SERVER, VALUE_OK);
		}
		catch (Exception e)
		{
			configureErrorResult(session, e, "Token Server Error: ");
		}
	}

	private void testAmazonS3Server(HttpSession session, JSONObject results)
			throws Exception
	{
		try
		{
			AmazonS3Utils.getUniqueBuildNumber(session, TEST_APK_TO_CHECK);
			String s3Bucket = AmazonS3Utils.getDownloadS3Bucket();
			results.append(KEY_S3_BUCKET, s3Bucket);
		}
		catch (Exception e)
		{
			configureErrorResult(session, e, "Amazon S3 Error: ");
		}
	}

	private void testFreeDiskSpace(HttpSession session, JSONObject results) throws Exception
	{
		File tempFile = File.createTempFile("SpaceTest", "tmp");
		long totalSpace = tempFile.getTotalSpace();
		long freeSpace = tempFile.getFreeSpace();
		tempFile.delete();
		long tenPercentFreeSpaceLeft = (long) ((double)totalSpace * 0.1);
		results.append(KEY_FREE_DISK_SPACE_MB, BuildingApkController.getMegaBytes(freeSpace));
		if(freeSpace < tenPercentFreeSpaceLeft)
			configureErrorResult(session, new Exception("Under 10% free disk space: Total:"+totalSpace +", Free:" + freeSpace ), "Disk Space Error: ");
	}
	
	private void testFilesExist(HttpSession session, String masterFileList, File sourceDirectory, JSONObject smokeResults) throws Exception
	{
		File fileListDirectory = SecureAppGeneratorApplication.getStaticWebDirectory();
		ensureBuildFileExists(session, fileListDirectory, masterFileList);
		BufferedReader br = null;
		try
		{
			File buildFiles = new File(fileListDirectory, masterFileList);
			br = new BufferedReader(new FileReader(buildFiles));
			String line = null;
			while ((line = br.readLine()) != null) 
			{
				ensureBuildFileExists(session, sourceDirectory, line);
			}
		}
		finally
		{
			if(br != null)
				br.close();	
		}
		smokeResults.append(KEY_BUILD_FILES + masterFileList, VALUE_OK);
	}

	private void ensureBuildFileExists(HttpSession session,
			File sourceDirectory, String buildFileToTest) throws Exception
	{
		File sourceFile = new File(sourceDirectory, buildFileToTest);
		if(!sourceFile.exists())
			configureErrorResult(session, new Exception(sourceFile.getAbsolutePath()), "Build File Missing: ");
	}

	private void configureErrorResult(HttpSession session, Exception e,
			String errorMessage) throws Exception
	{
		SecureAppGeneratorApplication.setInvalidResults(session, errorMessage + e.toString());
		SagLogger.logException(session, e);
		throw e;
	}
}
