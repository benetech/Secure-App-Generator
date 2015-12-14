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

	private String getExpectedErrorResults()
	{
		StringBuilder errorResults = new StringBuilder();
		errorResults.append("[SECRET : ID=5 : LABEL=PassCode]");
		errorResults.append(", [SELECT_MULTI : ID=7 : LABEL=multiAnswer-label]");
		errorResults.append(", [AUDIO_CAPTURE : ID=9 : LABEL=Audio]");
		errorResults.append(", [VIDEO_CAPTURE : ID=10 : LABEL=Video]");
		errorResults.append(", [IMAGE_CHOOSE : ID=11 : LABEL=Image]");
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
	"<?xml version=\"1.0\" encoding=\"UTF-8\" ?>"
			+ "<h:html xmlns:h=\"http://www.w3.org/1999/xhtml\" xmlns:orx=\"http://openrosa.org/jr/xforms\" xmlns=\"http://www.w3.org/2002/xforms\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:jr=\"http://openrosa.org/javarosa\" xmlns:vellum=\"http://commcarehq.org/xforms/vellum\">"
			+ "<h:head>	"
			+ "<h:title>Untitled Form</h:title>"
			+ "<model>"
			+ "<instance>"
				+ "<data xmlns:jrm=\"http://dev.commcarehq.org/jr/xforms\" xmlns=\"http://openrosa.org/formdesigner/D0FB2323-8E1D-40D6-AFAC-881ADB218D56\" uiVersion=\"1\" version=\"1\" name=\"Untitled Form\">"
							+ "<Record_Information>"
					+ "<Language />"
							+ "</Record_Information>"
						+ "<Summary_Section>"
						+ "<Regional_office_collecting_the_data />"
							+ "<Source_of_record_information />"
							+ "<If_Source_other />"
							+ "</Summary_Section>"
						+ "<Interviewee_Information>"
						+ "<Interviewee_Name />"
							+ "<Interviewee_Speaks />"
							+ "<Interview_Start_Date />"
							+ "<Interview_End_Date />"
							+ "<IntervieweeAnonymous />"
							+ "<IntervieweeAddtInfo />"
							+ "<IntervieweeTestify />"
							+ "</Interviewee_Information>"
						+ "<Event_Information>"
						+ "<Event_Start_Date />"
							+ "<Event_End_Date />"
							+ "	<Event_Location />"
							+ "	</Event_Information>"
						+ "<Victim_Information>"
						+ "<FirstName />"
							+ "<LastName />"
							+ "<Is_Identified />"
							+ "<Date_of_Birth />"
							+ "<Sex />"
							+ "<Region_of_Birth />"
							+ "<Ethnicity />"
							+ "</Victim_Information>"
						+ "<Professional_History>"
						+ "<First_Name />"
							+ "<Last_Name />"
							+ "<Profession />"
							+ "<Profession_Start_Date />"
							+ "<Profession_End_Date />"
							+ "</Professional_History>"
						+ "<Narrative>"
						+ "<Narrative_Description />"
						+ "</Narrative>"
						+ "</data>"
						+ "</instance>"
				+ "<bind nodeset=\"/data/Record_Information\" />"
						+ "<bind nodeset=\"/data/Record_Information/Language\" />"
						+ "<bind nodeset=\"/data/Summary_Section\" />"
						+ "<bind nodeset=\"/data/Summary_Section/Regional_office_collecting_the_data\" type=\"xsd:string\" />"
						+ "<bind nodeset=\"/data/Summary_Section/Source_of_record_information\" />"
						+ "<bind nodeset=\"/data/Summary_Section/If_Source_other\" type=\"xsd:string\" />"
						+ "<bind nodeset=\"/data/Interviewee_Information\" />"
						+ "<bind nodeset=\"/data/Interviewee_Information/Interviewee_Name\" type=\"xsd:string\" />"
						+ "<bind nodeset=\"/data/Interviewee_Information/Interviewee_Speaks\" />"
						+ "<bind nodeset=\"/data/Interviewee_Information/Interview_Start_Date\" type=\"xsd:date\" />"
						+ "<bind nodeset=\"/data/Interviewee_Information/Interview_End_Date\" type=\"xsd:date\" />"
						+ "<bind nodeset=\"/data/Interviewee_Information/IntervieweeAnonymous\" />"
						+ "<bind nodeset=\"/data/Interviewee_Information/IntervieweeAddtInfo\" />"
						+ "<bind nodeset=\"/data/Interviewee_Information/IntervieweeTestify\" />"
						+ "<bind nodeset=\"/data/Event_Information\" />"
						+ "<bind nodeset=\"/data/Event_Information/Event_Start_Date\" type=\"xsd:date\" />"
						+ "<bind nodeset=\"/data/Event_Information/Event_End_Date\" type=\"xsd:date\" />"
						+ "<bind nodeset=\"/data/Event_Information/Event_Location\" type=\"xsd:string\" />"
						+ "<bind nodeset=\"/data/Victim_Information\" />"
						+ "<bind nodeset=\"/data/Victim_Information/FirstName\" type=\"xsd:string\" />"
						+ "<bind nodeset=\"/data/Victim_Information/LastName\" type=\"xsd:string\" />"
						+ "<bind nodeset=\"/data/Victim_Information/Is_Identified\" />"
						+ "<bind nodeset=\"/data/Victim_Information/Date_of_Birth\" type=\"xsd:date\" />"
						+ "<bind nodeset=\"/data/Victim_Information/Sex\" />"
						+ "<bind nodeset=\"/data/Victim_Information/Region_of_Birth\" type=\"xsd:string\" />"
						+ "	<bind nodeset=\"/data/Victim_Information/Ethnicity\" type=\"xsd:string\" />"
						+ "	<bind nodeset=\"/data/Professional_History\" />"
						+ "<bind nodeset=\"/data/Professional_History/First_Name\" type=\"xsd:string\" />"
						+ "<bind nodeset=\"/data/Professional_History/Last_Name\" type=\"xsd:string\" />"
						+ "<bind nodeset=\"/data/Professional_History/Profession\" type=\"xsd:string\" />"
						+ "<bind nodeset=\"/data/Professional_History/Profession_Start_Date\" type=\"xsd:date\" />"
						+ "<bind nodeset=\"/data/Professional_History/Profession_End_Date\" type=\"xsd:date\" />"
						+ "<bind nodeset=\"/data/Narrative\" />"
						+ "<bind nodeset=\"/data/Narrative/Narrative_Description\" type=\"xsd:string\" />"
						+ "</model>"
				+ "</h:head>"
		+ "<h:body>"
		+ "	<group ref=\"/data/Record_Information\" appearance=\"field-list\">"
					+ "	<label>Record Information</label>"
				+ "<select1 ref=\"/data/Record_Information/Language\"  appearance=\"minimal\">"
						+ "<label>Language</label>"
					+ "<item>"
					+ "	<label>- Other -</label>"
						+ "	<value>language_1</value>"
						+ "	</item>"
					+ "	<item>"
					+ "<label>English</label>"
						+ "	<value>language_2</value>"
						+ "	</item>"
					+ "<item>"
					+ "	<label>Arabic</label>"
						+ "	<value>language_3</value>"
						+ "	</item>"
					+ "<item>"
					+ "	<label>Armenian</label>"
						+ "	<value>language_4</value>"
						+ "</item>"
					+ "<item>"
					+ "	<label>Telugu</label>"
						+ "	<value>language_45</value>"
						+ "</item>"
					+ "	<item>"
					+ "<label>Thai</label>"
						+ "<value>language_46</value>"
						+ "</item>"
					+ "<item>"
					+ "<label>Turkish</label>"
						+ "<value>language_47</value>"
						+ "</item>"
					+ "<item>"
					+ "<label>Turkmen</label>"
						+ "	<value>language_48</value>"
						+ "</item>"
					+ "<item>"
					+ "	<label>Ukranian</label>"
						+ "	<value>language_49</value>"
						+ "</item>"
					+ "<item>"
					+ "	<label>Urdu</label>"
						+ "	<value>language_50</value>"
						+ "	</item>"
					+ "	<item>"
					+ "	<label>Uzbek</label>"
						+ "	<value>language_51</value>"
						+ "</item>"
					+ "	<item>"
					+ "		<label>Vietnamese</label>"
						+ "	<value>language_52</value>"
						+ "	</item>"
					+ "</select1>"
				+ "</group>"
			+ "	<group ref=\"/data/Summary_Section\" appearance=\"field-list\">"
					+ "	<label>Summary Section</label>"
				+ "	<input ref=\"/data/Summary_Section/Regional_office_collecting_the_data\">"
						+ "		<label>Regional office collecting the data</label>"
					+ "	</input>"
				+ "	<select1 ref=\"/data/Summary_Section/Source_of_record_information\">"
						+ "	<label>Source of record information</label>"
					+ "	<item>"
					+ "		<label>Media Press</label>"
						+ "		<value>MediaPress</value>"
						+ "</item>"
					+ "<item>"
					+ "	<label>Legal Report</label>"
						+ "	<value>Legal_Report</value>"
						+ "</item>"
					+ "	<item>"
					+ "	<label>Personal Interview</label>"
						+ "	<value>Personal_Interview</value>"
						+ "	</item>"
					+ "<item>"
					+ "	<label>Other</label>"
						+ "	<value>Other</value>"
						+ "	</item>"
					+ "</select1>"
				+ "<input ref=\"/data/Summary_Section/If_Source_other\">"
						+ "<label>If Source = &quot;other&quot;, please  specify</label>"
					+ "</input>"
				+ "</group>"
			+ "<group ref=\"/data/Interviewee_Information\" appearance=\"field-list\">"
					+ "<label>Interviewee Information</label>"
				+ "<input ref=\"/data/Interviewee_Information/Interviewee_Name\">"
						+ "	<label>Interviewee  Name</label>"
					+ "	</input>"
				+ "<select1 ref=\"/data/Interviewee_Information/Interviewee_Speaks\" appearance=\"minimal\">"
						+ "<label>Interviewee Speaks</label>"
					+ "<item>"
					+ "	<label>- Other -</label>"
						+ "	<value>language_1</value>"
						+ "	</item>"
					+ "	<item>"
					+ "<label>English</label>"
						+ "	<value>language_2</value>"
						+ "	</item>"
					+ "<item>"
					+ "	<label>Arabic</label>"
						+ "	<value>language_3</value>"
						+ "	</item>"
					+ "<item>"
					+ "	<label>Armenian</label>"
						+ "	<value>language_4</value>"
						+ "</item>"
					+ "<item>"
					+ "	<label>Telugu</label>"
						+ "	<value>language_45</value>"
						+ "</item>"
					+ "	<item>"
					+ "<label>Thai</label>"
						+ "<value>language_46</value>"
						+ "</item>"
					+ "<item>"
					+ "<label>Turkish</label>"
						+ "<value>language_47</value>"
						+ "</item>"
					+ "<item>"
					+ "<label>Turkmen</label>"
						+ "	<value>language_48</value>"
						+ "</item>"
					+ "<item>"
					+ "	<label>Ukranian</label>"
						+ "	<value>language_49</value>"
						+ "</item>"
					+ "<item>"
					+ "	<label>Urdu</label>"
						+ "	<value>language_50</value>"
						+ "	</item>"
					+ "	<item>"
					+ "	<label>Uzbek</label>"
						+ "	<value>language_51</value>"
						+ "</item>"
					+ "	<item>"
					+ "		<label>Vietnamese</label>"
						+ "	<value>language_52</value>"
						+ "	</item>"
					+ "</select1>"
				+ "<input ref=\"/data/Interviewee_Information/Interview_Start_Date\" appearance=\"no-calendar\">"
						+ "	<label>Interview Start Date</label>"
					+ "</input>"
				+ "<input ref=\"/data/Interviewee_Information/Interview_End_Date\" appearance=\"no-calendar\">"
						+ "	<label>Interview End Date</label>"
					+ "</input>"
				+ "<select1 ref=\"/data/Interviewee_Information/IntervieweeAnonymous\">"
						+ "<label>Does interviewee wish to remain anonymous?</label>"
					+ "<item>"
					+ "<label>Yes</label>"
						+ "<value>Yes</value>"
						+ "</item>"
					+ "<item>"
					+ "<label>No</label>"
						+ "	<value>No</value>"
						+ "</item>"
					+ "</select1>"
				+ "<select1 ref=\"/data/Interviewee_Information/IntervieweeAddtInfo\">"
						+ "<label>Is interviewee willing to give additional information if needed?</label>"
					+ "	<item>"
					+ "	<label>Yes</label>"
						+ "	<value>Yes</value>"
						+ "	</item>"
					+ "	<item>"
					+ "	<label>No</label>"
						+ "	<value>No</value>"
						+ "	</item>"
					+ "</select1>"
				+ "<select1 ref=\"/data/Interviewee_Information/IntervieweeTestify\">"
						+ "<label>Is interviewee willing to testify?</label>"
					+ "<item>"
					+ "	<label>Yes</label>"
						+ "	<value>Yes</value>"
						+ "	</item>"
					+ "<item>"
					+ "	<label>No</label>"
						+ "	<value>No</value>"
						+ "	</item>"
					+ "</select1>"
				+ "</group>"
			+ "<group ref=\"/data/Event_Information\" appearance=\"field-list\">"
					+ "<label>Event Information</label>"
				+ "<input ref=\"/data/Event_Information/Event_Start_Date\" appearance=\"no-calendar\">"
						+ "	<label>Event Start Date</label>"
					+ "	</input>"
				+ "	<input ref=\"/data/Event_Information/Event_End_Date\" appearance=\"no-calendar\">"
						+ "	<label>Event End Date</label>"
					+ "</input>"
				+ "<input ref=\"/data/Event_Information/Event_Location\">"
						+ "	<label>Event Location</label>"
					+ "	</input>"
				+ "</group>"
			+ "<group>"
			+ "<label>Victim Information</label>"
				+ "<repeat nodeset=\"/data/Victim_Information\">"
						+ "<input ref=\"/data/Victim_Information/FirstName\">"
							+ "<label>First Name</label>"
						+ "</input>"
					+ "<input ref=\"/data/Victim_Information/LastName\">"
							+ "<label>Last Name</label>"
						+ "</input>"
					+ "<select1 ref=\"/data/Victim_Information/Is_Identified\">"
							+ "<label>Is Identified?</label>"
						+ "<item>"
						+ "<label>Yes</label>"
							+ "	<value>Yes</value>"
							+ "</item>"
						+ "<item>"
						+ "<label>No</label>"
							+ "<value>No</value>"
							+ "</item>"
						+ "</select1>"
					+ "<input ref=\"/data/Victim_Information/Date_of_Birth\" appearance=\"no-calendar\">"
							+ "<label>Date of Birth</label>"
						+ "</input>"
					+ "<select1 ref=\"/data/Victim_Information/Sex\">"
							+ "<label>Sex</label>"
						+ "<item>"
						+ "<label>Male</label>"
							+ "<value>Male</value>"
							+ "</item>"
						+ "<item>"
						+ "<label>Female</label>"
							+ "<value>Female</value>"
							+ "</item>"
						+ "<item>"
						+ "<label>Unknown</label>"
							+ "<value>Unknown</value>"
							+ "</item>"
						+ "</select1>"
					+ "<input ref=\"/data/Victim_Information/Region_of_Birth\">"
							+ "<label>Region of Birth</label>"
						+ "</input>"
					+ "<input ref=\"/data/Victim_Information/Ethnicity\">"
							+ "<label>Ethnicity</label>"
						+ "</input>"
					+ "</repeat>"
				+ "</group>"
			+ "<group>"
			+ "<label>Professional History</label>"
				+ "<repeat nodeset=\"/data/Professional_History\">"
						+ "<input ref=\"/data/Professional_History/First_Name\">"
							+ "<label>First Name</label>"
						+ "</input>"
					+ "<input ref=\"/data/Professional_History/Last_Name\">"
							+ "<label>Last Name</label>"
						+ "</input>"
					+ "<input ref=\"/data/Professional_History/Profession\">"
							+ "<label>Profession</label>"
						+ "</input>"
					+ "<input ref=\"/data/Professional_History/Profession_Start_Date\" appearance=\"no-calendar\">"
							+ "<label>Profession Start  Date</label>"
						+ "</input>"
					+ "<input ref=\"/data/Professional_History/Profession_End_Date\" appearance=\"no-calendar\">"
							+ "<label>Profession End Date</label>"
						+ "</input>"
					+ "</repeat>"
				+ "</group>"
			+ "<group ref=\"/data/Narrative\" appearance=\"field-list\">"
					+ "<label>Narrative</label>"
				+ "<input ref=\"/data/Narrative/Narrative_Description\">"
					+ "<label>Narrative description of events</label>"
				+ "</input>"
			+ "</group>"
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
					+ "<sag_test_fields ID=\"sag_test_fields\">"
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
			+ "<sag_test_fields ID=\"sag_test_fields\">"
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
				+ "<sag_test_fields ID=\"sag_test_fields\">"
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
