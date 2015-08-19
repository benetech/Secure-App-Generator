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

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpSession;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.io.FileUtils;
import org.javarosa.core.model.FormDef;
import org.javarosa.core.model.FormIndex;
import org.javarosa.core.model.GroupDef;
import org.javarosa.core.model.IFormElement;
import org.javarosa.core.model.QuestionDef;
import org.javarosa.core.util.OrderedMap;
import org.javarosa.form.api.FormEntryController;
import org.javarosa.form.api.FormEntryModel;
import org.javarosa.form.api.FormEntryPrompt;
import org.javarosa.xform.util.XFormUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * 
 *  DataTypes and ControlTypes
 *  
 * 	DATATYPE_UNSUPPORTED = -1;
 *  DATATYPE_NULL = 0;  			For nodes that have no data, or data type otherwise unknown
 *  DATATYPE_TEXT = 1;    		Text question type. 
 *  DATATYPE_INTEGER = 2;  		Numeric question type. These are numbers without decimal points
 *  DATATYPE_DECIMAL = 3;   		Decimal question type. These are numbers with decimals
 *  DATATYPE_DATE = 4;    		Date question type. This has only date component without time.
 *  DATATYPE_TIME = 5;    		Time question type. This has only time element without date
 *  DATATYPE_DATE_TIME = 6;		Date and Time question type. This has both the date and time components
 *  DATATYPE_CHOICE = 7;			This is a question with alist of options where not more than one option can be selected at a time.
 *  DATATYPE_CHOICE_LIST = 8;	This is a question with alist of options where more than one option can be selected at a time.
 *  DATATYPE_BOOLEAN = 9;    	Question with true and false answers.
 *  DATATYPE_GEOPOINT = 10; 		Question with location answer.
 *  DATATYPE_BARCODE = 11; 		Question with barcode string answer.
 *  DATATYPE_BINARY = 12; 		Question with external binary answer.
 *  DATATYPE_LONG = 13; 			Question with external binary answer. 
 *  DATATYPE_GEOSHAPE = 14; 		Question with GeoShape answer.
 *  DATATYPE_GEOTRACE = 15; 		Question with GeoTrace answer.
 *
 *  CONTROL_UNTYPED = -1;
 *  CONTROL_INPUT = 1;
 *  CONTROL_SELECT_ONE = 2;
 *  CONTROL_SELECT_MULTI = 3;
 *  CONTROL_TEXTAREA = 4;
 *  CONTROL_SECRET = 5;
 *  CONTROL_RANGE = 6;
 *  CONTROL_UPLOAD = 7;
 *  CONTROL_SUBMIT = 8;
 *  CONTROL_TRIGGER = 9;
 *  CONTROL_IMAGE_CHOOSE = 10;
 *  CONTROL_LABEL = 11;
 *  CONTROL_AUDIO_CAPTURE = 12;
 *  CONTROL_VIDEO_CAPTURE = 13;
 */

@Controller
public class ObtainXFormController extends WebMvcConfigurerAdapter
{
	private static final String XML_TYPE = "xml";
	private static final String XML_FILE_LOCATION = SecureAppGeneratorApplication.getStaticWebDirectory() + "/xFormToUse.xml";  //TODO this will be based on build directory for this session
	private static final String XFORM_FILE_EXTENSION = ".xml";
	private static final String XFORMS_DEFAULT_DIRECTORY = SecureAppGeneratorApplication.getStaticWebDirectory() + "/xforms";
	private static final int NO_PATH_SEPARATOR_FOUND = -1;
	private static final char PATH_SEPARATOR = '/';

	@RequestMapping(value=WebPage.OBTAIN_XFORM, method=RequestMethod.GET)
    public String directError(HttpSession session, Model model) 
    {
		SecureAppGeneratorApplication.setInvalidResults(session);
        return WebPage.ERROR;
    }

	@RequestMapping(value=WebPage.OBTAIN_XFORM_PREVIOUS, method=RequestMethod.POST)
    public String goBack(HttpSession session, Model model) throws Exception 
    {
		AppConfiguration config = (AppConfiguration) session.getAttribute(SessionAttributes.APP_CONFIG);
		SecureAppGeneratorApplication.setDefaultIconForSession(session, config);
		model.addAttribute(SessionAttributes.APP_CONFIG, config);
		return WebPage.OBTAIN_LOGO;
    }

	@RequestMapping(value=WebPage.OBTAIN_XFORM_NEXT, method=RequestMethod.POST)
    public String retrieveXForm(HttpSession session, @RequestParam("file") MultipartFile file, @RequestParam("selectedForm") String formName, Model model, AppConfiguration appConfig)
    {
		Path xFormBuildPath = Paths.get(XML_FILE_LOCATION);
		String xFormLocation = null;
		String xFormName = null;
		if (file.isEmpty()) 
        {
			if(!copyXFormsFileSelectedToBuildDirectory(session, formName))
				return WebPage.ERROR; 

            xFormLocation = xFormBuildPath.getFileName().toString();
			xFormName = getFormNameOnly(formName);
        }
        else
        {
            try 
            {
            		if(!file.getContentType().contains(XML_TYPE))
            		{
            			Logger.log(session, "Non-XML xForm: " + file.getContentType());
             		return returnErrorMessage(model, appConfig, "Error: Xform must be of type xml."); 
            		}
            		SecureAppGeneratorApplication.saveMultiPartFileToLocation(file, xFormBuildPath.toFile());
                Logger.logVerbose(session, "Uploaded XFORM Location" + xFormBuildPath.toString());
  
                xFormLocation = xFormBuildPath.getFileName().toString();
    				xFormName = getFormNameOnly(getFormNameOnly(file.getOriginalFilename()));

              } 
            catch (Exception e) 
            {
            		Logger.logException(session, e);
            		try
				{
					Files.delete(xFormBuildPath);
				}
				catch (IOException e1)
				{
					Logger.logException(session, e1);
				}
            		return returnErrorMessage(model, appConfig, "Error: uploading file."); 
            }
        }
		
		try
		{
			isValidXForm();
		}
		catch (Exception e)
		{
    			Logger.logException(session, e);
       		return returnErrorMessage(model, appConfig, "Error: xForm invalid: " + e.getLocalizedMessage()); 
		}
		
		AppConfiguration config = (AppConfiguration)session.getAttribute(SessionAttributes.APP_CONFIG);
		config.setAppXFormLocation(xFormLocation);
		config.setAppXFormName(xFormName);
		session.setAttribute(SessionAttributes.APP_CONFIG, config);
		model.addAttribute(SessionAttributes.APP_CONFIG, config);
		return WebPage.OBTAIN_CLIENT_TOKEN;
    }

	private void isValidXForm() throws Exception
	{
		File xformFile = new File(XML_FILE_LOCATION);
		
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		dBuilder.parse(xformFile);

		byte[] xformRawData = Files.readAllBytes(xformFile.toPath());
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(xformRawData);
        FormDef formDef = XFormUtils.getFormFromInputStream(byteArrayInputStream);
        FormEntryModel formEntryModel = new FormEntryModel(formDef);
        StringBuilder fieldErrors = new StringBuilder();
        inspectFields(formEntryModel, formDef, fieldErrors);
        if(fieldErrors.length() != 0)
    			throw new Exception("Unsupported XForm Field(s): " +fieldErrors.toString());
	}
	
	private void inspectFields(FormEntryModel model, FormDef formDef, StringBuilder fieldErrors)
    {
         List<IFormElement> children = formDef.getChildren();
         recursivelyInspectFields(children, fieldErrors);
         checkModel(model, fieldErrors);
    }

    private void checkModel(FormEntryModel model, StringBuilder fieldErrors)
	{
		FormIndex index = model.getFormIndex();
		while(!index.isEndOfFormIndex())
		{
			int event = model.getEvent(index);
			if(event != FormEntryController.EVENT_QUESTION)
			{
				index = model.incrementIndex(index);
				continue;
			}
			FormEntryPrompt prompt = model.getQuestionPrompt(index);
			final int dataType = prompt.getDataType();
			switch(dataType)
			{
				case org.javarosa.core.model.Constants.DATATYPE_TEXT:
 				case org.javarosa.core.model.Constants.DATATYPE_DATE:
 				case org.javarosa.core.model.Constants.DATATYPE_CHOICE:
 				case org.javarosa.core.model.Constants.DATATYPE_INTEGER:
 				case org.javarosa.core.model.Constants.DATATYPE_DECIMAL:
				case org.javarosa.core.model.Constants.DATATYPE_LONG:
 				case org.javarosa.core.model.Constants.DATATYPE_TIME:
 				case org.javarosa.core.model.Constants.DATATYPE_BOOLEAN:
 					break;

 				//Unsupported DataType's below
 				case org.javarosa.core.model.Constants.DATATYPE_BARCODE:
 					addFieldTypeNotSupported("BARCODE", fieldErrors, prompt);
 					break;
 				case org.javarosa.core.model.Constants.DATATYPE_BINARY:
 					addFieldTypeNotSupported("BINARY", fieldErrors, prompt);
 					break;
 				case org.javarosa.core.model.Constants.DATATYPE_CHOICE_LIST:
 					addFieldTypeNotSupported("CHOICE_LIST", fieldErrors, prompt);
 					break;
 				case org.javarosa.core.model.Constants.DATATYPE_DATE_TIME:
 					addFieldTypeNotSupported("DATE_TIME", fieldErrors, prompt);
 					break;
 				case org.javarosa.core.model.Constants.DATATYPE_GEOPOINT:
 					addFieldTypeNotSupported("GEOPOINT", fieldErrors, prompt);
 					break;
 				case org.javarosa.core.model.Constants.DATATYPE_GEOSHAPE:
 					addFieldTypeNotSupported("GEOSHAPE", fieldErrors, prompt);
 					break;
 				case org.javarosa.core.model.Constants.DATATYPE_GEOTRACE:
 					addFieldTypeNotSupported("GEOTRACE", fieldErrors, prompt);
 					break;
 				case org.javarosa.core.model.Constants.DATATYPE_NULL:
 					addFieldTypeNotSupported("NULL", fieldErrors, prompt);
 					break;
 				case org.javarosa.core.model.Constants.DATATYPE_UNSUPPORTED:
 					addFieldTypeNotSupported("UNSUPPORTED", fieldErrors, prompt);
 					break;
 				default:
 					addFieldTypeNotSupported("UNKNOWN", fieldErrors, prompt);
 					break;
            	
			}
			index = model.incrementIndex(index);
		}
	}

	private void recursivelyInspectFields(List<IFormElement> children, StringBuilder fieldErrors)
    {
        for (IFormElement child : children)
        {
            if (child instanceof GroupDef)
            {
                GroupDef groupDef = (GroupDef) child;
                List<IFormElement> groupChildren = groupDef.getChildren();
                recursivelyInspectFields(groupChildren, fieldErrors);
            }
 
            if (child instanceof QuestionDef)
            {
                QuestionDef questionDef = (QuestionDef) child;
                final int controlType = questionDef.getControlType();
 	            switch(controlType)
	            {
	            case org.javarosa.core.model.Constants.CONTROL_INPUT:
	            case org.javarosa.core.model.Constants.CONTROL_SELECT_ONE:
	            case org.javarosa.core.model.Constants.CONTROL_LABEL:
	            case org.javarosa.core.model.Constants.CONTROL_TEXTAREA:
 	            case org.javarosa.core.model.Constants.CONTROL_TRIGGER:
 	            		break;

 	            	//Unsupported Control types below
	            case org.javarosa.core.model.Constants.CONTROL_SELECT_MULTI:
        				addFieldControlNotSupported("SELECT_MULTI", fieldErrors, questionDef);
        				break;
	            case org.javarosa.core.model.Constants.CONTROL_AUDIO_CAPTURE:
	            		addFieldControlNotSupported("AUDIO_CAPTURE", fieldErrors, questionDef);
	            		break;
	            case org.javarosa.core.model.Constants.CONTROL_IMAGE_CHOOSE:
        				addFieldControlNotSupported("IMAGE_CHOOSE", fieldErrors, questionDef);
	            		break;
	            case org.javarosa.core.model.Constants.CONTROL_RANGE:
        				addFieldControlNotSupported("RANGE", fieldErrors, questionDef);
            			break;
	            case org.javarosa.core.model.Constants.CONTROL_SECRET:
        				addFieldControlNotSupported("SECRET", fieldErrors, questionDef);
            			break;
	            case org.javarosa.core.model.Constants.CONTROL_SUBMIT:
        				addFieldControlNotSupported("SUBMIT", fieldErrors, questionDef);
            			break;
	            case org.javarosa.core.model.Constants.CONTROL_UNTYPED:
        				addFieldControlNotSupported("UNTYPED", fieldErrors, questionDef);
            			break;
	            case org.javarosa.core.model.Constants.CONTROL_UPLOAD:
        				addFieldControlNotSupported("UPLOAD", fieldErrors, questionDef);
            			break;
	            case org.javarosa.core.model.Constants.CONTROL_VIDEO_CAPTURE:
        				addFieldControlNotSupported("VIDEO_CAPTURE", fieldErrors, questionDef);
            			break;
	            	default:
            			addFieldControlNotSupported("UNKNOWN", fieldErrors, questionDef);
	            		break;
	            }
           }
        }
     }

	private void addFieldTypeNotSupported(String fieldType, StringBuilder fieldErrors, FormEntryPrompt prompt)
	{
		StringBuilder errorMsg = new StringBuilder(fieldType);
		errorMsg.append(" : \"");
		errorMsg.append(prompt.getQuestion().getTextID());
		errorMsg.append("\"");
		addErrorToListOfErrors(fieldErrors, errorMsg);
	}

	private void addFieldControlNotSupported(String fieldName, StringBuilder fieldErrors, QuestionDef questionDef)
	{
		StringBuilder errorMsg = new StringBuilder(fieldName);
		errorMsg.append(" : id=");
		errorMsg.append(questionDef.getID());
		errorMsg.append(" : label=");
		errorMsg.append(questionDef.getLabelInnerText());
		addErrorToListOfErrors(fieldErrors, errorMsg);
	}

	private void addErrorToListOfErrors(StringBuilder fieldErrors, StringBuilder errorMsg)
	{
		if(fieldErrors.length() != 0)
			fieldErrors.append(", ");
		fieldErrors.append("[");
		fieldErrors.append(errorMsg);
		fieldErrors.append("]");
	}	

	public String returnErrorMessage(Model model, AppConfiguration appConfig, String errorMsg)
	{
		appConfig.setAppXFormError(errorMsg);
		model.addAttribute(SessionAttributes.APP_CONFIG, appConfig);
		return WebPage.OBTAIN_XFORM;
	}

	public boolean copyXFormsFileSelectedToBuildDirectory(HttpSession session, String formName)
	{
		File source = new File(formName);
		File destination = new File(XML_FILE_LOCATION);
		try
		{
			FileUtils.copyFile(source, destination);
			return true;
		}
		catch (IOException e)
		{
			Logger.logException(session, e);
    			SecureAppGeneratorApplication.setInvalidResults(session, "failed_copy_file", e);
		    return false;
		}
	}
	
	@ModelAttribute("formsImpMap")
	public static OrderedMap<String,String> populateFormsMap() throws MalformedURLException, IOException 
	{
	    OrderedMap<String,String> formsImpMap = new OrderedMap<String,String>();
	    String chooseAnXForm = new String(SecureAppGeneratorApplication.getMessage("combo.select_default_form"));
	    formsImpMap.put(chooseAnXForm, null);
		File xFormsDirectory = new File(XFORMS_DEFAULT_DIRECTORY);
		if(!xFormsDirectory.exists())
			return formsImpMap;
		ArrayList<File> files = new ArrayList<File>(Arrays.asList(xFormsDirectory.listFiles()));
		files.forEach((file) -> addForms(formsImpMap, file));
	    return formsImpMap;
	}
	
	private static void addForms(OrderedMap<String,String> formsImpMap, File file)
	{
		String formName = file.getName();
		if(formName.toLowerCase().endsWith(XFORM_FILE_EXTENSION))
			formsImpMap.put(getFormNameOnly(formName), file.getAbsolutePath());
	}

	private static String getFormNameOnly(String formName)
	{
		int startPosition;
		int startOfFileName = formName.lastIndexOf(PATH_SEPARATOR);
		if(startOfFileName == NO_PATH_SEPARATOR_FOUND)
			startPosition = 0;
		else
			startPosition = startOfFileName + 1;
		
		int fileNameLengthWithoutXmlExtension = formName.length()-XFORM_FILE_EXTENSION.length();
		return formName.substring(startPosition, fileNameLengthWithoutXmlExtension);
	}
}
