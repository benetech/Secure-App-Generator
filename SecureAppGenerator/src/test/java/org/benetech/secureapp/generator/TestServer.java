/*

Martus(TM) is a trademark of Beneficent Technology, Inc. 
This software is (c) Copyright 2016, Beneficent Technology, Inc.

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

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.martus.util.TestCaseEnhanced;

public class TestServer extends TestCaseEnhanced
{

	public TestServer(String name)
	{
		super(name);
	}
	
	public void testQaStagingServers() throws Exception
	{
		updateSystemEnvironment("staging-benetech-sag");
		assertTrue("Should be Production server", ServerConstants.usingRealMartusServer());
		assertEquals("Production server IP incorrect?", ServerConstants.getCurrentServerIp(), "54.72.26.74");
		assertFalse("Should not be QA", ServerConstants.isQaAwsServer());
		assertTrue("Should be Staging", ServerConstants.isStagingAwsServer());
			
		updateSystemEnvironment("benetech-sag");
		assertTrue("Should be Production server", ServerConstants.usingRealMartusServer());
		assertEquals("Production server IP incorrect?", ServerConstants.getCurrentServerIp(), "54.72.26.74");
		assertFalse("Should not be QA", ServerConstants.isQaAwsServer());
		assertFalse("Should not be Staging", ServerConstants.isStagingAwsServer());

		updateSystemEnvironment("QA-benetech-sag");
		assertFalse("Should be QA server", ServerConstants.usingRealMartusServer());
		assertEquals("QA server IP incorrect?", ServerConstants.getCurrentServerIp(), "54.213.152.140");
		assertTrue("Should be QA", ServerConstants.isQaAwsServer());
		assertFalse("Should not be Staging", ServerConstants.isStagingAwsServer());

		updateSystemEnvironment("qa-benetech-sag");
		assertFalse("Should be QA server", ServerConstants.usingRealMartusServer());
		assertEquals("QA server IP incorrect?", ServerConstants.getCurrentServerIp(), "54.213.152.140");
		assertTrue("Should be QA", ServerConstants.isQaAwsServer());
		assertFalse("Should not be Staging", ServerConstants.isStagingAwsServer());
	}

	private void updateSystemEnvironment(String bucketToUse)
	{
		Map<String, String> env = new HashMap<String, String>();
		env.put(AmazonS3Utils.AMAZON_S3_DOWNLOAD_BUCKET_ENV, bucketToUse);
		setEnv(env);
	}
	
	protected static void setEnv(Map<String, String> newenv)
	{
	  try
	    {
	        Class<?> processEnvironmentClass = Class.forName("java.lang.ProcessEnvironment");
	        Field theEnvironmentField = processEnvironmentClass.getDeclaredField("theEnvironment");
	        theEnvironmentField.setAccessible(true);
	        Map<String, String> env = (Map<String, String>) theEnvironmentField.get(null);
	        env.putAll(newenv);
	        Field theCaseInsensitiveEnvironmentField = processEnvironmentClass.getDeclaredField("theCaseInsensitiveEnvironment");
	        theCaseInsensitiveEnvironmentField.setAccessible(true);
	        Map<String, String> cienv = (Map<String, String>)     theCaseInsensitiveEnvironmentField.get(null);
	        cienv.putAll(newenv);
	    }
	    catch (NoSuchFieldException e)
	    {
	      try {
	        Class<?>[] classes = Collections.class.getDeclaredClasses();
	        Map<String, String> env = System.getenv();
	        for(Class<?> cl : classes) {
	            if("java.util.Collections$UnmodifiableMap".equals(cl.getName())) {
	                Field field = cl.getDeclaredField("m");
	                field.setAccessible(true);
	                Object obj = field.get(env);
	                Map<String, String> map = (Map<String, String>) obj;
	                map.clear();
	                map.putAll(newenv);
	            }
	        }
	      } catch (Exception e2) {
	        e2.printStackTrace();
	      }
	    } catch (Exception e1) {
	        e1.printStackTrace();
	    } 
	}	

}
