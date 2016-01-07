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
import org.martus.util.TestCaseEnhanced;

public class TestAppNameValidation extends TestCaseEnhanced
{
	private NameAppController nameAppController;
	public TestAppNameValidation(String name)
	{
		super(name);
	}

	@Override
	protected void setUp() throws Exception
	{
		nameAppController = new NameAppController();
		super.setUp();
	}
	
	
	public void testValidateAppName() throws Exception
	{	
		AppConfiguration appConfig = new AppConfiguration();
		assertNull("no initial error msg",  appConfig.getAppNameError());
		assertFalse("Not configured valid app name?", nameAppController.validateAppName(null, appConfig));
		assertEquals("Incorrect error msg for not initalized appConfig with no Name", getLocalizedMessage("app_name_length"), appConfig.getAppNameError());
		appConfig.setAppName(VALID_NAME);
		assertEquals("App Name different?", VALID_NAME, appConfig.getAppName());
		assertEquals("Short App name different?", VALID_NAME, appConfig.getAppNameShort());
		assertTrue("Correct app name not valid?", nameAppController.validateAppName(null, appConfig));
		appConfig.setAppName(NAME_TOO_SHORT);
		assertFalse("App Name should be too short.", nameAppController.validateAppName(null, appConfig));
		assertEquals("error message for short name incorrect?", getLocalizedMessage("app_name_length"), appConfig.getAppNameError());
		appConfig.setAppName(NAME_TOO_LONG);
		assertFalse("App Name should be too long.", nameAppController.validateAppName(null, appConfig));
		assertEquals("error message for long name incorrect?", getLocalizedMessage("app_name_length"), appConfig.getAppNameError());
		appConfig.setAppName(NAME_STARTING_WITH_A_NUMBER);
		assertFalse("App Name starting with a Number", nameAppController.validateAppName(null, appConfig));
		assertEquals("error message for name beginning with a number incorrect?", getLocalizedMessage("app_name_numeric"), appConfig.getAppNameError());
		appConfig.setAppName(VALID_NAME_WITH_EXTRA_SPACES);
		assertTrue("App name with beginning/ending spaces not valid?", nameAppController.validateAppName(null, appConfig));
		assertEquals("Did not strip extra spaces from app name??", VALID_NAME, appConfig.getAppName());
		String illegalCharacters = "^!\"#$%&'()-\\.[]*+,/:;<=>?@`{|}~$";
		char[] charArray = illegalCharacters.toCharArray();
		for(int i = 0; i < illegalCharacters.length(); ++i)
		{
			testIllegalCharacter(appConfig, charArray[i]);
		}
		
		appConfig.setAppName(VALID_NAME_OVER_15_CHARACTERS);
		assertEquals("App Name different?", VALID_NAME_OVER_15_CHARACTERS, appConfig.getAppName());
		assertEquals("Short App name not correct?", VALID_NAME_OVER_15_CHARACTERS_SHORT, appConfig.getAppNameShort());
		
		appConfig.setAppName(VALID_NAME_EXACTLY_15_CHARACTERS);
		assertEquals("15 char. App Name different?", VALID_NAME_EXACTLY_15_CHARACTERS, appConfig.getAppName());
		assertEquals("15 char. Short App name different?", VALID_NAME_EXACTLY_15_CHARACTERS, appConfig.getAppNameShort());

		appConfig.setAppName(VALID_NAME_EXACTLY_16_CHARACTERS);
		assertEquals("16 char. App Name different?", VALID_NAME_EXACTLY_16_CHARACTERS, appConfig.getAppName());
		assertEquals("16 char. Short App name different?", VALID_NAME_EXACTLY_16_CHARACTERS_SHORT, appConfig.getAppNameShort());

		appConfig.setAppName(LONG_ARABIC_NAME);
		assertEquals("Arabic App Name different?", LONG_ARABIC_NAME, appConfig.getAppName());
		assertEquals("Arabic Short App name different?", SHORT_ARABIC_NAME, appConfig.getAppNameShort());
	}
	//TODO add test for following illegal characters [^!\"#$%&'()\\[\\]*+,/:;<=>?@\\^`{|}~]+$

	private void testIllegalCharacter(AppConfiguration appConfig, char illegalCharacter)
	{
		appConfig.setAppName("My" + illegalCharacter + "App" );
		assertFalse("App Name with illegal character:"+illegalCharacter , nameAppController.validateAppName(null, appConfig));
		assertEquals("error message incorrect for illegal character:" + illegalCharacter, getLocalizedMessage("app_name_characters"), appConfig.getAppNameError());
	}

	public String getLocalizedMessage(String msg)
	{
		return SecureAppGeneratorApplication.getLocalizedErrorMessage(msg);
	}

	private final String VALID_NAME_WITH_EXTRA_SPACES = "  My Good Name   ";
	private final String VALID_NAME = "My Good Name";
	private final String NAME_TOO_SHORT = "My";
	private final String NAME_TOO_LONG = "My Really Long App Name More Than Thirty Characters";
	private final String NAME_STARTING_WITH_A_NUMBER = "1My incorrect File Name";
	private final String VALID_NAME_OVER_15_CHARACTERS = "My Good App Long Name";
	private final String VALID_NAME_OVER_15_CHARACTERS_SHORT = "My Good App ...";
	private final String VALID_NAME_EXACTLY_15_CHARACTERS = "123456789012345";
	private final String VALID_NAME_EXACTLY_16_CHARACTERS = "1234567890123456";
	private final String VALID_NAME_EXACTLY_16_CHARACTERS_SHORT = "123456789012...";
	private final String LONG_ARABIC_NAME = "هو 施 هو 施 هو 施 هو 施";
	private final String SHORT_ARABIC_NAME = "هو 施 هو 施 هو...";
}
