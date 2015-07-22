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
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpSession;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.AccessControlList;
import com.amazonaws.services.s3.model.GroupGrantee;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.Permission;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3ObjectSummary;

public class AmazonS3Utils
{
	private static final String AMAZON_S3_DOWNLOAD_BUCKET_ENV = "S3_DOWNLOAD_BUCKET";
	private static final String AMAZON_S3_KEY_ENV = "AWS_KEY";
	private static final String AMAZON_S3_SECRET_ENV = "AWS_SECRET";
	private static final String AMAZON_S3_BASE_DIR = "https://s3.amazonaws.com/";
	private static final String AMAZON_DOWNLOADS_DIRECTORY = "downloads/";

	static public String getBaseUrl()
	{
		return AMAZON_S3_BASE_DIR;
	}
	
	static public String uploadToAmazonS3(HttpSession session, File fileToUpload)
	{
        AmazonS3 s3client = getS3();
		String bucketName = getDownloadS3Bucket();
		Logger.logVerbose(session, "S3BucketName = " + bucketName);
		if(!s3client.doesBucketExist(bucketName))
			Logger.logError(session, "Does not exist?  S3 Bucket :" + bucketName);

		AccessControlList acl = new AccessControlList();
		acl.grantPermission(GroupGrantee.AllUsers, Permission.Read);
		s3client.putObject(new PutObjectRequest(bucketName, getAPKDownloadFilePathWithFile(fileToUpload),  fileToUpload).withAccessControlList(acl));

		String apkURL = getBaseUrl() + getDownloadS3Bucket() + "/" + getAPKDownloadFilePathWithFile(fileToUpload);
		Logger.log(session, "APK URL = " + apkURL);
		return apkURL;
	}

	private static AmazonS3 getS3()
	{
		AmazonS3 s3client = new AmazonS3Client(new BasicAWSCredentials(getAwsKey(), getAwsSecret()));
		return s3client;
	}

	public static String getUniqueBuildNumber(String apkNameWithNoSagBuild)
	{
		String partialApkName = apkNameWithNoSagBuild.substring(0, apkNameWithNoSagBuild.length()-5).toLowerCase();
		int greatestBuildNumberFound = 0;

		AmazonS3 s3 = getS3();
		ObjectListing listing = s3.listObjects(getDownloadS3Bucket(), AMAZON_DOWNLOADS_DIRECTORY);
		List<S3ObjectSummary> summaries = listing.getObjectSummaries();

		while (listing.isTruncated()) 
		{
		   listing = s3.listNextBatchOfObjects (listing);
		   summaries.addAll (listing.getObjectSummaries());
		}
		
		if(!summaries.isEmpty())
		{
			for (Iterator<S3ObjectSummary> iterator = summaries.iterator(); iterator.hasNext();)
			{
				S3ObjectSummary currentApk = (S3ObjectSummary) iterator.next();
				String currentApkName = currentApk.getKey().toLowerCase();
				String amazonObjectPartialName = AMAZON_DOWNLOADS_DIRECTORY + partialApkName;
				if(currentApkName.startsWith(amazonObjectPartialName))
				{
					int buildStartPos = amazonObjectPartialName.length();
					int buildEndPos = currentApkName.length()-4;
					String currentBuildNumberString = currentApkName.substring(buildStartPos, buildEndPos);
					int currentBuildNumber = Integer.parseInt(currentBuildNumberString);
					if(currentBuildNumber > greatestBuildNumberFound)
						greatestBuildNumberFound = currentBuildNumber;
				}
			}
		}
		
		int nextSagBuildNumber = greatestBuildNumberFound+1;
		return Integer.toString(nextSagBuildNumber);
	}

	static private String getDownloadS3Bucket()
	{
  		return System.getenv(AMAZON_S3_DOWNLOAD_BUCKET_ENV);
	}
	
	static private String getAPKDownloadFilePathWithFile(File fileToUpload)
	{
		return  AMAZON_DOWNLOADS_DIRECTORY + fileToUpload.getName();
	}

	static private String getAwsSecret()
	{
 		return System.getenv(AMAZON_S3_SECRET_ENV);
	}

	static private String getAwsKey()
	{
 		return System.getenv(AMAZON_S3_KEY_ENV);
	}
}
