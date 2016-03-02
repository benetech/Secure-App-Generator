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
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Map;
import java.util.Vector;

import javax.servlet.http.HttpSession;

import org.benetech.secureapp.generator.AmazonS3Utils.S3Exception;
import org.martus.clientside.ClientSideNetworkGateway;
import org.martus.common.DammCheckDigitAlgorithm.CheckDigitInvalidException;
import org.martus.common.Exceptions.ServerCallFailedException;
import org.martus.common.Exceptions.ServerNotAvailableException;
import org.martus.common.Exceptions.ServerNotCompatibleException;
import org.martus.common.MartusAccountAccessToken;
import org.martus.common.MartusAccountAccessToken.TokenInvalidException;
import org.martus.common.MartusAccountAccessToken.TokenNotFoundException;
import org.martus.common.crypto.MartusCrypto;
import org.martus.common.crypto.MartusCrypto.AuthorizationFailedException;
import org.martus.common.crypto.MartusCrypto.CreateDigestException;
import org.martus.common.crypto.MartusSecurity;
import org.martus.common.network.ClientSideNetworkInterface;
import org.martus.common.network.NetworkInterfaceConstants;
import org.martus.common.network.NetworkResponse;
import org.martus.common.network.PassThroughTransportWrapper;
import org.martus.common.network.TransportWrapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Controller
public class ObtainTokenController extends WebMvcConfigurerAdapter
{
	private static final String SAG_KEYPAIR_DIRECTORY = SecureAppGeneratorApplication.getStaticWebDirectory() + "/keys";
	private static final String SAG_KEYPAIR_FILE = "sagKeyPair.dat";
	private static final String SAG_KEYPAIR_PASSWORD = "12SaGPassword";

	@RequestMapping(value=WebPage.OBTAIN_CLIENT_TOKEN, method=RequestMethod.GET)
    public String directError(HttpSession session, Model model) 
    {
		SagLogger.logWarning(session, "OBTAIN_CLIENT_TOKEN Get Request");
		SecureAppGeneratorApplication.setInvalidResults(session);
        return WebPage.ERROR;
    }
	
	@RequestMapping(value=WebPage.OBTAIN_XFORM_CREATE, method=RequestMethod.GET)
    public String createXForm(HttpSession session, Model model, AppConfiguration appConfig)
    {
		model.addAttribute(SessionAttributes.APP_CONFIG, appConfig);
       return WebPage.OBTAIN_XFORM_CREATE;
    }

	@RequestMapping(value=WebPage.OBTAIN_CLIENT_TOKEN_PREVIOUS, method=RequestMethod.POST)
    public String goBack(HttpSession session, Model model, AppConfiguration appConfig)
    {
		model.addAttribute(SessionAttributes.APP_CONFIG, appConfig);
       return WebPage.OBTAIN_XFORM;
    }
	
	@RequestMapping(value=WebPage.OBTAIN_CLIENT_TOKEN_NEXT, method=RequestMethod.POST)
	public String nextPage(HttpSession session, Model model, AppConfiguration appConfig) 
    {
		if(isValidToken(session, appConfig)) 
		{
			try
			{
				SagLogger.logInfo(session, "Obtaining Token from "+ServerConstants.getCurrentServerIp());
				getClientPublicKeyFromToken(session, appConfig);
				updateServerConfiguration(session);
				updateApkVersionInfoAndName(session);
				model.addAttribute(SessionAttributes.APP_CONFIG, appConfig);
				SagLogger.logInfo(session, "Token Found");
				return WebPage.SUMMARY;
			}
			catch (TokenNotFoundException e)
			{
				String tokenString = appConfig.getClientToken().trim();
				SagLogger.logWarning(session, "Token Not Found on Server.:"+tokenString);
				appConfig.setClientTokenError("token_not_found");
			}
			catch (S3Exception e)
			{
				SagLogger.logException(session, e);
				appConfig.setClientTokenError("server_s3");
			}
			catch (AuthorizationFailedException e)
			{
				SagLogger.logException(session, e);
				appConfig.setClientTokenError("crypto_authorization_failed");
			}
			catch (Exception e)
			{
				SagLogger.logException(session, e);
				appConfig.setClientTokenError("server_token");
			}
		}
		model.addAttribute(SessionAttributes.APP_CONFIG, appConfig);
		return WebPage.OBTAIN_CLIENT_TOKEN;
    }

	private void updateApkVersionInfoAndName(HttpSession session) throws IOException, S3Exception
	{
        AppConfiguration config = (AppConfiguration)session.getAttribute(SessionAttributes.APP_CONFIG);
        AppConfiguration.setBuildVersionFromGeneratedSettingsFile(config);
        String uniqueBuildNumber = AmazonS3Utils.getUniqueBuildNumber(session, config.getApkName());
        config.setApkSagVersionBuild(uniqueBuildNumber);
 		session.setAttribute(SessionAttributes.APP_CONFIG, config);
	}

	private void updateServerConfiguration(HttpSession session)
	{
        AppConfiguration config = (AppConfiguration)session.getAttribute(SessionAttributes.APP_CONFIG);
		ServerConstants.setServerConfig(config);
		config.setServerPublicKey(ServerConstants.getCurrentSeverKey());
		session.setAttribute(SessionAttributes.APP_CONFIG, config);
	}

	private static ClientSideNetworkInterface createXmlRpcNetworkInterfaceHandler()
	{
		TransportWrapper transport = new PassThroughTransportWrapper();
		String ourServer = ServerConstants.getCurrentServerIp();
		String ourServerPublicKey = ServerConstants.getCurrentSeverKey();
		return ClientSideNetworkGateway.buildNetworkInterface(ourServer, ourServerPublicKey, transport);
	}

	// TODO add unit tests
	static public void getClientPublicKeyFromToken(HttpSession session, AppConfiguration appConfig) throws Exception
	{
        AppConfiguration config = (AppConfiguration)session.getAttribute(SessionAttributes.APP_CONFIG);
 		try
		{
 			//TODO do this once not ever time this page gets displayed
 			MartusSecurity security = new MartusSecurity();
 			ClientSideNetworkGateway gateway = new ClientSideNetworkGateway(createXmlRpcNetworkInterfaceHandler());
 			
 			File keyPair = new File(SAG_KEYPAIR_DIRECTORY, SAG_KEYPAIR_FILE);
			boolean createNewKeypair = true;
 			if(keyPair.exists())
			{
				try
				{
					SagLogger.logDebug(session, "reading keypair: " + SAG_KEYPAIR_DIRECTORY);
					security.readKeyPair(keyPair, SAG_KEYPAIR_PASSWORD.toCharArray());
					SagLogger.logDebug(session, "read keypair");
					createNewKeypair = false;
				}
				catch (Exception e)
				{
					SagLogger.logException(session, e);
					createNewKeypair = true;
				}
			}
 			
			if(createNewKeypair)
			{
				SagLogger.logInfo(session, "Creating new SAG Keypair");
				File keyPairDir = new File(SAG_KEYPAIR_DIRECTORY);
				if(!keyPairDir.exists())
					keyPairDir.mkdirs();
				security.createKeyPair();
				FileOutputStream outputStream = new FileOutputStream(keyPair);
				security.writeKeyPair(outputStream, SAG_KEYPAIR_PASSWORD.toCharArray());
				outputStream.flush();
				outputStream.close();
				SagLogger.logInfo(session, "Created Keypair");
			}
			String tokenString = appConfig.getClientToken();
			MartusAccountAccessToken accessToken = new MartusAccountAccessToken(tokenString);
 			NetworkResponse response = gateway.getMartusAccountIdFromAccessToken(security, accessToken);
 			if(response.getResultCode().equals(NetworkInterfaceConstants.NO_TOKEN_AVAILABLE))
 				throw new TokenNotFoundException();
 			if(response.getResultCode().equals(NetworkInterfaceConstants.SERVER_NOT_COMPATIBLE))
 				throw new ServerNotCompatibleException();
 			if(response.getResultCode().equals(NetworkInterfaceConstants.SERVER_ERROR))
 				throw new ServerCallFailedException();
 			if(!response.getResultCode().equals(NetworkInterfaceConstants.OK))
 			{
 				SagLogger.logError(session, "Token Network returncode:" +response.getResultCode());
 				throw new ServerNotAvailableException();
 			}
 			
 			Vector<String> singleAccountId = response.getResultVector();
 			if(singleAccountId.size() != 1)
 				throw new TokenNotFoundException();
 			String AccountId = singleAccountId.get(0);
	 		config.setClientPublicKey(AccountId);
			config.setClientPublicCode(MartusCrypto.computeFormattedPublicCode40(AccountId));
			SagLogger.logDebug(session, "Account Found:" + config.getClientPublicCode());
		}
		catch (CreateDigestException | CheckDigitInvalidException e)
		{
			SagLogger.logException(session, e);
			throw new TokenNotFoundException();
		}
 		session.setAttribute(SessionAttributes.APP_CONFIG, config);
	}

	//TODO add unit tests!
	private boolean isValidToken(HttpSession session, AppConfiguration appConfig)
	{
		String tokenString = appConfig.getClientToken().trim();
		appConfig.setClientToken(tokenString);
		try
		{
			new MartusAccountAccessToken(tokenString);
		}
		catch (TokenInvalidException e)
		{
			SagLogger.logError(session, "Token invalid:" + tokenString);
			appConfig.setClientTokenError("token_invalid");
			return false;
		}
		return true;
	}
	
	//NOTE: Needed due to how page is called from another page 
	@ModelAttribute("formsImpMap")
	public Map<String,String> populateFormsMap() throws MalformedURLException, IOException 
	{
		return ObtainXFormController.populateFormsMap();
	}
}
