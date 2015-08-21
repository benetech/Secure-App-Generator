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

import org.martus.util.TestCaseEnhanced;
import org.xml.sax.SAXException;

public class TestXFormValidation extends TestCaseEnhanced
{
	private ObtainXFormController xformController;
	public TestXFormValidation(String name)
	{
		super(name);
	}

	@Override
	protected void setUp() throws Exception
	{
		xformController = new ObtainXFormController();
		super.setUp();
	}

	public void testNullFile() throws Exception
	{
		try
		{
			File xformNull = null;
			xformController.isXFormValid(xformNull);
			fail("Didn't throw on null file?");
		}
		catch (Exception expected)
		{
		}
	}
	
	public void testValidXForm() throws Exception
	{
		File xformValid = createTempFileFromName("xFormvalid", "xml");
		copyResourceFileToLocalFile(xformValid, "MartusCustomization.xml");
		StringBuilder errors = xformController.isXFormValid(xformValid);
		assertEquals("Should not have any errors for a valid xForm File", "", errors.toString());

		xformValid = createTempFileFromName("xFormSimpleValid", "xml");
		copyResourceFileToLocalFile(xformValid, "simpleXForm.xml");
		errors = xformController.isXFormValid(xformValid);
		assertEquals("Simple xForm should not have any errors.", "", errors.toString());
	}

	public void testXFormInvalidXML() throws Exception
	{
		File xformXmlInvalid = createTempFileFromName("xFormXmlInvalid", "xml");
		copyResourceFileToLocalFile(xformXmlInvalid, "InvalidMartusCustomization.xml");
		try
		{
			xformController.isXFormValid(xformXmlInvalid);
			fail("should have thrown a SAXException.");
		}
		catch (SAXException expectedException)
		{
		}
	}
		
	public void testXFormUnsupportedFieldTypes() throws Exception
	{
		File xformXmlInvalid = createTempFileFromName("xFormXmlInvalid", "xml");
		copyResourceFileToLocalFile(xformXmlInvalid, "xFormGeoTag.xml");
		StringBuilder errorResults = xformController.isXFormValid(xformXmlInvalid);
		String expectedErrorResults = getExpectedErrorResults();
		assertEquals("should have multiple errors for the unsupported field types", expectedErrorResults , errorResults.toString());
	}

	private String getExpectedErrorResults()
	{
		StringBuilder errorResults = new StringBuilder();
		errorResults.append("[SECRET : id=5 : label=PassCode]");
		errorResults.append(", [SELECT_MULTI : id=7 : label=multiAnswer-label]");
		errorResults.append(", [AUDIO_CAPTURE : id=9 : label=Audio]");
		errorResults.append(", [VIDEO_CAPTURE : id=10 : label=Video]");
		errorResults.append(", [IMAGE_CHOOSE : id=11 : label=Image]");
		errorResults.append(", [GEOPOINT : \"GeoLabel - null\"]");
		errorResults.append(", [BARCODE : \"BarcodeLabel - null\"]");
		errorResults.append(", [UNSUPPORTED : \"CallOut - null\"]");
		errorResults.append(", [CHOICE_LIST : \"multiAnswer-label - null\"]");
		errorResults.append(", [BINARY : \"Audio - null\"]");
		errorResults.append(", [BINARY : \"Video - null\"]");
		errorResults.append(", [BINARY : \"Image - null\"]");
		return errorResults.toString();
	}
	
}
