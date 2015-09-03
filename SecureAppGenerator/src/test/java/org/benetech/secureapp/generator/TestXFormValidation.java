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
import java.io.IOException;

import org.martus.util.TestCaseEnhanced;
import org.martus.util.UnicodeWriter;
import org.xml.sax.SAXException;
import org.benetech.secureapp.generator.ObtainXFormController;

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
	
	public void testBasics() throws Exception
	{
		assertTrue("nop", true);
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
	/*
	public void testValidXForm() throws Exception
	{
		File xformValid = createTempFileFromName("xFormvalid", "xml");
		writeXmlToFile(xformValid, xmlMartusCustomization);
		StringBuilder errors = xformController.isXFormValid(xformValid);
		assertEquals("Should not have any errors for a valid xForm File", "", errors.toString());

		xformValid = createTempFileFromName("xFormSimpleValid", "xml");
		writeXmlToFile(xformValid, xmlSimplexForm);
		errors = xformController.isXFormValid(xformValid);
		assertEquals("Simple xForm should not have any errors.", "", errors.toString());
	}

	protected void writeXmlToFile(File xformValid,
			String xmlMartusCustomization2) throws IOException
	{
		UnicodeWriter writer = new UnicodeWriter(xformValid);
		writer.write(xmlMartusCustomization2);
		writer.close();
	}

	public void testXFormInvalidXML() throws Exception
	{
		File xformXmlInvalid = createTempFileFromName("xFormXmlInvalid", "xml");
		writeXmlToFile(xformXmlInvalid, xmlInvalidxForm);
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
		writeXmlToFile(xformXmlInvalid, xmlxFormInvalidFields);
		StringBuilder errorResults = xformController.isXFormValid(xformXmlInvalid);
		String expectedErrorResults = getExpectedErrorResults();
		assertEquals("should have multiple errors for the unsupported field types", expectedErrorResults , errorResults.toString());
	}
*/
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
	
	private String xmlMartusCustomization = 
			"<?xml version=\"1.0\"?>\n"
			+ "<h:html xmlns=\"http://www.w3.org/2002/xforms\"\n"
			+ "xmlns:h=\"http://www.w3.org/1999/xhtml\"\n"
			+ "xmlns:ev=\"http://www.w3.org/2001/xml-events\"\n"
			+ "xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\"\n"
			+ "xmlns:jr=\"http://openrosa.org/javarosa\"\n"
			+ ">\n"
			+ "<h:head>\n"
        			+ "<h:title>secureApp Prototype</h:title>"
        			+ "<model>"
		   			+ "<instance>"
            			+ "<martus_customization_example id=\"martus_customization_example\">"
				  			+ "<language/>"
				  			+ "<Office/>"
				  			+ "<RecordSource/>"
				  			+ "<SpecifyOther/>"
				  			+ "<IntervieweeName/>"
				  			+ "<IntervieweeLanguage/>"
				  
				  			+ "<InterviewDatesRepeat jr:template=\"\">"
								+ "<InterviewDates/>"
				  			+ "</InterviewDatesRepeat>"
				  
				  			+ "<Anonymous/>"
				  			+ "<AdditionalInfo/>"
				  			+ "<Testify/>"
				  			+ "<EventDateStart/>"
				  			+ "<EventDateEnd/>"
				  			+ "<EventLocation/>"
				  			+ "<Event_Location__City/>"
				  
				  			+ "<VictimRepeat jr:template=\"\">"
								+ "<FirstName/>"
								+ "<LastName/>"
								+ "<Identified/>"
								+ "<BirthDate/>"
								+ "<Gender/>"
								+ "<BirthRegion/>"
								+ "<Ethnicity/>"
				  			+ "</VictimRepeat>"
				  			+ "<Profession_History_Table_Note_/>"
				  			+ "<ProfessionalRepeat jr:template=\"\">"
								+ "<FirstName/>"
								+ "<LastName/>"
								+ "<Profession/>"
								+ "<DatesProfessionStart/>"
								+ "<DatesProfessionEnd/>"
				  			+ "</ProfessionalRepeat>"
				  			+ "<narrative>What happened in detail is as follows:</narrative>"
				  			+ "<today/>"
				  			+ "<meta>"
								+ "<instanceID/>"
				  			+ "</meta>"
						+ "</martus_customization_example>"
           			+ "</instance>"
		   
			  			+ "<bind nodeset=\"/martus_customization_example/language\" type=\"select1\"/>"
			  			+ "<bind nodeset=\"/martus_customization_example/Office\" type=\"string\"/>"
			  			+ "<bind nodeset=\"/martus_customization_example/RecordSource\" required=\"true()\" type=\"select1\"/>"
			  			+ "<bind nodeset=\"/martus_customization_example/SpecifyOther\" type=\"string\"/>"
			  			+ "<bind nodeset=\"/martus_customization_example/IntervieweeName\" type=\"string\"/>"
			  			+ "<bind nodeset=\"/martus_customization_example/IntervieweeLanguage\" type=\"select1\"/>"
			  			+ "<bind nodeset=\"/martus_customization_example/InterviewDatesRepeat/InterviewDates\" type=\"date\" constraint=\". >= date('1900-01-01')\" jr:constraintMsg=\"The date you entered is not within the accepted date range.\"/>"
			  			+ "<bind nodeset=\"/martus_customization_example/Anonymous\" type=\"string\"/>"
			  			+ "<bind nodeset=\"/martus_customization_example/AdditionalInfo\" type=\"string\"/>"
			  			+ "<bind nodeset=\"/martus_customization_example/Testify\" type=\"string\"/>"
			  			+ "<bind nodeset=\"/martus_customization_example/EventDateStart\" type=\"date\" constraint=\". >= date('1900-01-01')\" jr:constraintMsg=\"The date you entered is not within the accepted date range.\"/>"
			  			+ "<bind nodeset=\"/martus_customization_example/EventDateEnd\" type=\"date\" constraint=\". >= date('1900-01-01')\" jr:constraintMsg=\"The date you entered is not within the accepted date range.\"/>"
			  			+ "<bind nodeset=\"/martus_customization_example/EventLocation\" type=\"select1\"/>"
			  			+ "<bind nodeset=\"/martus_customization_example/Event_Location__City\" type=\"select1\"/>"
			  			+ "<bind nodeset=\"/martus_customization_example/VictimRepeat\" required=\"false()\"/>"
			  			+ "<bind nodeset=\"/martus_customization_example/VictimRepeat/FirstName\" type=\"string\"/>"
			  			+ "<bind nodeset=\"/martus_customization_example/VictimRepeat/LastName\" type=\"string\"/>"
			  			+ "<bind nodeset=\"/martus_customization_example/VictimRepeat/Identified\" type=\"string\"/>"
			  			+ "<bind nodeset=\"/martus_customization_example/VictimRepeat/BirthDate\" type=\"date\" constraint=\". >= date('1900-01-01')\" jr:constraintMsg=\"The date you entered is not within the accepted date range.\"/>"
			  			+ "<bind nodeset=\"/martus_customization_example/VictimRepeat/Gender\" required=\"true()\" type=\"select1\"/>"
			  			+ "<bind nodeset=\"/martus_customization_example/VictimRepeat/BirthRegion\" type=\"select1\"/>"
			  			+ "<bind nodeset=\"/martus_customization_example/VictimRepeat/Ethnicity\" type=\"string\"/>"
			  			+ "<bind nodeset=\"/martus_customization_example/Profession_History_Table_Note_\" type=\"string\"/>"
			  			+ "<bind nodeset=\"/martus_customization_example/ProfessionalRepeat\"/>"
			  			+ "<bind nodeset=\"/martus_customization_example/ProfessionalRepeat/FirstName\" type=\"string\"/>"
			  			+ "<bind nodeset=\"/martus_customization_example/ProfessionalRepeat/LastName\" type=\"string\"/>"
			  			+ "<bind nodeset=\"/martus_customization_example/ProfessionalRepeat/Profession\" type=\"string\"/>"
			  			+ "<bind nodeset=\"/martus_customization_example/ProfessionalRepeat/DatesProfessionStart\" type=\"date\" constraint=\". >= date('1900-01-01')\" jr:constraintMsg=\"The date you entered is not within the accepted date range.\"/>"
			  			+ "<bind nodeset=\"/martus_customization_example/ProfessionalRepeat/DatesProfessionEnd\" type=\"date\" constraint=\". >= date('1900-01-01')\" jr:constraintMsg=\"The date you entered is not within the accepted date range.\"/>"
			  			+ "<bind nodeset=\"/martus_customization_example/narrative\" type=\"string\"/>"
			  			+ "<bind nodeset=\"/martus_customization_example/today\" type=\"date\" jr:preload=\"date\" jr:preloadParams=\"today\" />"
        			+ "</model>"
    			+ "</h:head>"

    			+ "<h:body>"
				+ "<group>"
      			+ "<label>General Information</label>"
					+ "<select1 appearance=\"minimal\" ref=\"/martus_customization_example/language\">"
			  			+ "<label>Language</label>"
			  			+ "<item>"
							+ "<label>- Other -</label>"
							+ "<value>language_1</value>"
			  			+ "</item>"
			  			+ "<item>"
							+ "<label>English</label>"
							+ "<value>language_2</value>"
			  			+ "</item>"
			  			+ "<item>"
							+ "<label>Arabic</label>"
							+ "<value>language_3</value>"
			  			+ "</item>"
			  			+ "<item>"
							+ "<label>Vietnamese</label>"
							+ "<value>language_52</value>"
			  			+ "</item>"
						+ "</select1>"
				+ "</group>"
				+ "<group>"
	  			+ "<label>Summary Section</label>"
					+ "<input ref=\"/martus_customization_example/Office\">"
		  			+ "<label>Regional Office Collecting Data</label>"
					+ "</input>"
					+ "<select1 ref=\"/martus_customization_example/RecordSource\">"
		  			+ "<label>Source of Record Information</label>"
		  			+ "<item>"
						+ "<label>Media/Press</label>"
						+ "<value>source_1</value>"
		  			+ "</item>"
		  			+ "<item>"
						+ "<label>Legal Report</label>"
						+ "<value>source_2</value>"
		  			+ "</item>"
		  			+ "<item>"
						+ "<label>Personal Interview</label>"
						+ "<value>source_3</value>"
		  			+ "</item>"
		  			+ "<item>"
						+ "<label>Other</label>"
						+ "<value>source_4</value>"
		  			+ "</item>"
					+ "</select1>"
					+ "<input ref=\"/martus_customization_example/SpecifyOther\">"
		  			+ "<label>If Source = &quot;Other&quot;, please specify</label>"
					+ "</input>"
					+ "<input ref=\"/martus_customization_example/IntervieweeName\">"
		  			+ "<label>Interviewee Name</label>"
					+ "</input>"
					+ "<select1 appearance=\"minimal\" ref=\"/martus_customization_example/IntervieweeLanguage\">"
		  			+ "<label>Interviewee Speaks</label>"
		  			+ "<item>"
						+ "<label>- Other -</label>"
						+ "<value>language_1</value>"
		  			+ "</item>"
		 			+ " <item>"
						+ "<label>English</label>"
						+ "<value>language_2</value>"
		  			+ "</item>"
		  			+ "<item>"
						+ "<label>Arabic</label>"
						+ "<value>language_3</value>"
		  			+ "</item>"
		  			+ "<item>"
						+ "<label>Vietnamese</label>"
						+ "<value>language_52</value>"
		  			+ "</item>"
					+ "</select1>"
				+ "</group>"
				+ "<group ref=\"/martus_customization_example/InterviewDatesRepeat\">"
	  			+ "<label>Date(s) of Interview(s)</label>"
	  			+ "<repeat appearance=\"field-list\" nodeset=\"/martus_customization_example/InterviewDatesRepeat\">"
					+ "<input ref=\"/martus_customization_example/InterviewDatesRepeat/InterviewDates\">"
		  			+ "<label></label>"
					+ "</input>"
	  			+ "</repeat>"
				+ "</group>"
				+ "<group>"
	  			+ "<label></label>"
					+ "<trigger ref=\"/martus_customization_example/Anonymous\">"
		 			+ " <label>Does interviewee wish to remain anonymous?</label>"
					+ "</trigger>"
					+ "<trigger ref=\"/martus_customization_example/AdditionalInfo\">"
		  			+ "<label>Is interviewee willing to give additional information if needed?</label>"
					+ "</trigger>"
					+ "<trigger ref=\"/martus_customization_example/Testify\">"
		  			+ "<label>Is interviewee willing to testify?</label>"
					+ "</trigger>"
					+ "<input ref=\"/martus_customization_example/EventDateStart\">"
		  			+ "<label>Date of Event (Start)</label>"
					+ "</input>"
					+ "<input ref=\"/martus_customization_example/EventDateEnd\">"
		  			+ "<label>Date of Event (End)</label>"
					+ "</input>"
					+ "<select1 appearance=\"minimal\" ref=\"/martus_customization_example/EventLocation\">"
		  			+ "<label>Event Location (Region)</label>"
		  			+ "<item>"
						+ "<label>Region 1</label>"
						+ "<value>R1</value>"
		  			+ "</item>"
		  			+ "<item>"
						+ "<label>Region 2</label>"
						+ "<value>R2</value>"
		  			+ "</item>"
		 			+ " <item>"
						+ "<label>Region 3</label>"
						+ "<value>R3</value>"
		  			+ "</item>"
					+ "</select1>"
					+ "<select1 appearance=\"minimal\" ref=\"/martus_customization_example/Event_Location__City\">"
			  			+ "<label>Event Location (City)</label>"
			  			+ "<item>"
							+ "<label>City 1</label>"
							+ "<value>C1</value>"
			  			+ "</item>"
			  			+ "<item>"
							+ "<label>City 2</label>"
							+ "<value>C2</value>"
			  			+ "</item>"
			  			+ "<item>"
							+ "<label>City 3</label>"
							+ "<value>C3</value>"
			  			+ "</item>"
			  			+ "<item>"
							+ "<label>City 4</label>"
							+ "<value>C4</value>"
			  			+ "</item>"
			  			+ "<item>"
							+ "<label>City 5</label>"
							+ "<value>C5</value>"
			  			+ "</item>"
			  			+ "<item>"
							+ "<label>City 6</label>"
							+ "<value>C6</value>"
			 			+ " </item>"
						+ "</select1>"
				+ "</group>"
		
				+ "<group>"
					+ "<label>People Section</label>"
				+ "</group>"
				+ "<group ref=\"/martus_customization_example/VictimRepeat\">"
		  			+ "<label>Victim Information</label>"
		  			+ "<repeat nodeset=\"/martus_customization_example/VictimRepeat\">"
						+ "<input ref=\"/martus_customization_example/VictimRepeat/FirstName\">"
			  			+ "<label>First Name</label>"
						+ "</input>"
						+ "<input ref=\"/martus_customization_example/VictimRepeat/LastName\">"
			 			+ " <label>Last Name</label>"
						+ "</input>"
						+ "<trigger ref=\"/martus_customization_example/VictimRepeat/Identified\">"
			  			+ "<label>Is Identified?</label>"
						+ "</trigger>"
						+ "<input ref=\"/martus_customization_example/VictimRepeat/BirthDate\">"
			  			+ "<label>Date of Birth</label>"
						+ "</input>"
						+ "<select1 appearance=\"minimal\" ref=\"/martus_customization_example/VictimRepeat/Gender\">"
			  			+ "<label>Sex</label>"
			  			+ "<item>"
							+ "<label>Male</label>"
							+ "<value>gender_1</value>"
			  			+ "</item>"
			  			+ "<item>"
							+ "<label>Female</label>"
							+ "<value>gender_2</value>"
			  			+ "</item>"
			  			+ "<item>"
							+ "<label>Unknown</label>"
							+ "<value>gender_3</value>"
			  			+ "</item>"
						+ "</select1>"
						+ "<select1 appearance=\"minimal\" ref=\"/martus_customization_example/VictimRepeat/BirthRegion\">"
			  			+ "<label>Region of Birth</label>"
			  			+ "<item>"
							+ "<label>Region 1</label>"
							+ "<value>R1</value>"
			  			+ "</item>"
			 			+ " <item>"
							+ "<label>Region 2</label>"
							+ "<value>R2</value>"
			  			+ "</item>"
			  			+ "<item>"
							+ "<label>Region 3</label>"
							+ "<value>R3</value>"
			  			+ "</item>"
						+ "</select1>"
						+ "<input ref=\"/martus_customization_example/VictimRepeat/Ethnicity\">"
			  			+ "<label>Ethnicity</label>"
						+ "</input>"
		  			+ "</repeat>"
				+ "</group>"
				+ "<group>"
	  			+ "<label></label>"
					+ "<input ref=\"/martus_customization_example/Profession_History_Table_Note_\">"
						+ "<label>Profession History Table Note: If you have information about a person who has had different professions over time, enter multiple rows with the same First and Last Names and show the date ranges for each profession on a separate row.</label>"
					+ "</input>"
				+ "</group>"	
				+ "<group ref=\"/martus_customization_example/ProfessionalRepeat\">"
	  			+ "<label>Profession History</label>"
					+ "<repeat nodeset=\"/martus_customization_example/ProfessionalRepeat\">"
					+ "  <input ref=\"/martus_customization_example/ProfessionalRepeat/FirstName\">"
			  			+ "<label>First Name</label>"
						+ "</input>"
		  			+ "<input ref=\"/martus_customization_example/ProfessionalRepeat/LastName\">"
			  			+ "<label>Last Name</label>"
						+ "</input>"
		  			+ "<input ref=\"/martus_customization_example/ProfessionalRepeat/Profession\">"
			  			+ "<label>Profession</label>"
						+ "</input>"
		  			+ "<input ref=\"/martus_customization_example/ProfessionalRepeat/DatesProfessionStart\">"
			  			+ "<label>Dates of Profession (Start)</label>"
		  			+ "</input>"
		  			+ "<input ref=\"/martus_customization_example/ProfessionalRepeat/DatesProfessionEnd\">"
			  			+ "<label>Dates of Profession (End)</label>"
						+ "</input>"
					+ "</repeat>"
				+ "</group>"
				+ "<group>"
	  			+ "<label></label>"
					+ "<input appearance=\"multiline\" ref=\"/martus_customization_example/narrative\">"
		  			+ "<label>Narrative description of events</label>"
					+ "</input>	"
				+ "</group>	"

    			+ "</h:body>"
			+ "</h:html>";

	
private String xmlSimplexForm = 
			"<?xml version=\"1.0\"?>\n"
			+ "<h:html xmlns=\"http://www.w3.org/2002/xforms\"\n"
			+ "xmlns:h=\"http://www.w3.org/1999/xhtml\"\n"
			+ "xmlns:ev=\"http://www.w3.org/2001/xml-events\"\n"
			+ "xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\"\n"
			+ "xmlns:jr=\"http://openrosa.org/javarosa\"\n"
			+ ">\n"
			+ "<h:head>\n"
			+ "<h:title>SAG Test Fields</h:title>"
			+ "<model>"
	            + "<instance>"
					+ "<sag_test_fields id=\"sag_test_fields\">"
					+ "<location/>"
					+ "</sag_test_fields>"
	            + "</instance>"
				  + "<bind nodeset=\"/sag_test_fields/location\" required=\"false()\" type=\"string\"/>"
				  + "</model>"
	    + "</h:head>"

	    + "<h:body>"
			+ "<group appearance=\"field-list\">"
			+ "<label>Section 1</label>"
				+ "<input ref=\"/sag_test_fields/location\">"
				+ "<label>Location</label>"
				+ "</input>"
			+ "</group>"
	    + "</h:body>"
	+ "</h:html>";

private String xmlInvalidxForm = 
	"<?xml version=\"1.0\"?>\n"
	+ "<h:html xmlns=\"http://www.w3.org/2002/xforms\"\n"
	+ "xmlns:h=\"http://www.w3.org/1999/xhtml\"\n"
	+ "xmlns:ev=\"http://www.w3.org/2001/xml-events\"\n"
	+ "xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\"\n"
	+ "xmlns:jr=\"http://openrosa.org/javarosa\"\n"
	+ ">\n"
	+ "<h:head>\n"
	+ "<h:title>SAG Test Fields</h:title>"
	+ "<model_invalid>"
	    + "<instance>"
			+ "<sag_test_fields id=\"sag_test_fields\">"
			+ "<location/>"
			+ "</sag_test_fields>"
	    + "</instance>"
		  + "<bind nodeset=\"/sag_test_fields/location\" required=\"false()\" type=\"string\"/>"
		  + "</model>"
	+ "</h:head>"
	
	+ "<h:body>"
		+ "<group appearance=\"field-list\">"
		+ "<label>Section 1</label>"
			+ "<input ref=\"/sag_test_fields/location\">"
			+ "<label>Location</label>"
			+ "</input>"
		+ "</group>"
	+ "</h:body>"
	+ "</h:html>";
	

private String xmlxFormInvalidFields = 
	"<?xml version=\"1.0\"?>\n"
	+ "<h:html xmlns=\"http://www.w3.org/2002/xforms\"\n"
	+ "xmlns:h=\"http://www.w3.org/1999/xhtml\"\n"
	+ "xmlns:ev=\"http://www.w3.org/2001/xml-events\"\n"
	+ "xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\"\n"
	+ "xmlns:jr=\"http://openrosa.org/javarosa\"\n"
	+ ">\n"
	+ "<h:head>\n"
	+ "<h:title>SAG Test Fields</h:title>"
	+ "<model>"
            + "<instance>"
				+ "<sag_test_fields id=\"sag_test_fields\">"
					+ "<location/>"
					+ "<geo/>"
					+ "<mybarcode/>"
					+ "<myPass/>"
					+ "<callout/>"
					+ "<multiAnswer/>"
					+ "<time/>"
					+ "<audio/>"
					+ "<video/>"
					+ "<image/>"
				+ "</sag_test_fields>"
            + "</instance>"
            
			+ "<bind nodeset=\"/sag_test_fields/location\" required=\"false()\" type=\"string\"/>"
			+ "<bind nodeset=\"/sag_test_fields/geo\" type=\"geopoint\" />"
			+ "<bind nodeset=\"/sag_test_fields/mybarcode\" type=\"barcode\" />"
			+ "<bind nodeset=\"/sag_test_fields/myPass\" type=\"xsd:string\" />"
			+ "<bind nodeset=\"/sag_test_fields/callout\" type=\"intent\" />"
			+ "<bind nodeset=\"/sag_test_fields/multiAnswer\" />"
			+ "<bind nodeset=\"/sag_test_fields/time\" type=\"xsd:time\" />"
			+ "<bind nodeset=\"/sag_test_fields/audio\" type=\"binary\" />"
			+ "<bind nodeset=\"/sag_test_fields/video\" type=\"binary\" />"
			+ "<bind nodeset=\"/sag_test_fields/image\" type=\"binary\" />"
		+ "</model>"
    + "</h:head>"

    + "<h:body>"
		+ "<group appearance=\"field-list\">"
			+ "<label>Section 1</label>"
			+ "<input ref=\"/sag_test_fields/location\">"
			  + "<label>Location</label>"
			+ "</input>"
			+ "<input ref=\"/sag_test_fields/geo\">"
				+ "<label>GeoLabel</label>"
			+ "</input>"
			+ "<input ref=\"/sag_test_fields/mybarcode\">"
				+ "<label>BarcodeLabel</label>"
			+ "</input>"
			+ "<secret ref=\"/sag_test_fields/myPass\">"
				+ "<label>PassCode</label>"
			+ "</secret>"
			+ "<input ref=\"/sag_test_fields/callout\" appearance=\"intent:callout\">"
				+ "<label>CallOut</label>"
			+ "</input>"
			+ "<select ref=\"/sag_test_fields/multiAnswer\">"
				+ "<label>multiAnswer-label</label>"
				+ "<item>"
					+ "<label>labelItem1</label>"
					+ "<value>item1</value>"
				+ "</item>"
				+ "<item>"
					+ "<label>labelItemq</label>"
					+ "<value>item2</value>"
				+ "</item>"
				+ "</select>"
				+ "<input ref=\"/sag_test_fields/time\">"
					+ "<label>LabelTime</label>"
				+ "</input>"
				+ "<upload ref=\"/sag_test_fields/audio\" mediatype=\"audio/*\">"
					+ "<label>Audio</label>"
				+ "</upload>"
				+ "<upload ref=\"/sag_test_fields/video\" mediatype=\"video/*\">"
					+ "<label>Video</label>"
				+ "</upload>"
				+ "<upload ref=\"/sag_test_fields/image\" mediatype=\"image/*\">"
					+ "<label>Image</label>"
				+ "</upload>"
			+ "</group>"
	    + "</h:body>"
	+ "</h:html>";
}
