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

package SAG;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.martus.common.DammCheckDigitAlgorithm.CheckDigitInvalidException;
import org.martus.common.MartusAccountAccessToken;
import org.martus.common.MartusAccountAccessToken.TokenInvalidException;
import org.martus.common.crypto.MartusCrypto;
import org.martus.common.crypto.MartusCrypto.CreateDigestException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Controller
public class ObtainTokenController extends WebMvcConfigurerAdapter
{
	private static final char DASH_CHAR = '-';
	private static final String APK_EXTENSION = ".apk";
	private static final char UNDERSCORE_CHAR = '_';
	private static final char SPACE_CHAR = ' ';
	public static final String DESKTOP_PUBLIC_KEY = "MIIBojANBgkqhkiG9w0BAQEFAAOCAY8AMIIBigKCAYEAhToihLM3R540behcXLDKSHXjhiRsL3oQdyWeUfphpYXIQydfPny40zoFoznC3YwM8Jykf9ToQstO1k74SYwRuIEEdtews6ETV7U8sDz5IZnjOOg4xtBIEAAtFkO23oG429i1scOFI9L9p8xkhVePeZZ4CNHBXztYDcKVcqn+cEn8aTqBZ0sdcOUkZlMlfR628GhhCekS1lm0t6CSdRWKyqvGRJ6RbROep16ATZdriaJiPyVKMy1y/mAIoz/rRIOCUphnDTQjlKox6sQ6EaWykLCn0oY8KAR87Rar0OY09fuEn2KkP3gdhzVmmITFrGTFi37kzGDfCjJ8tn86G2D8KvAyiDdi8OlgSpDqI/G1MzPB04vrQa/HRcAwT1W7qmd0pKwu+GmQ+o9j1oiOpvFrK48TedZbI9fUWqXfuNSvS1pukfJ+svAPLlo+rBCT6F0hoGYgSFWUzRiiZuKZR4au51tqxhU4Qk7PDcFe/kx3TrtJKYLkLM1jQgT0ERdtlPZ9AgMBAAE=";

	@RequestMapping(value=WebPage.OBTAIN_CLIENT_TOKEN, method=RequestMethod.GET)
    public String directError(HttpSession session, Model model) 
    {
		SecureAppGeneratorApplication.setInvalidResults(session);
        return WebPage.ERROR;
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
		if(isValidToken(appConfig)) 
		{
			if(getClientPublicKeyFromToken(session, appConfig))
			{
				updateServerConfiguration(session);
				updateApkVersionInfoAndName(session);
 				model.addAttribute(SessionAttributes.APP_CONFIG, appConfig);
				return WebPage.SUMMARY;
			}
		}
		model.addAttribute(SessionAttributes.APP_CONFIG, appConfig);
		return WebPage.OBTAIN_CLIENT_TOKEN;
    }

	private void updateApkVersionInfoAndName(HttpSession session)
	{
        AppConfiguration config = (AppConfiguration)session.getAttribute(SessionAttributes.APP_CONFIG);
        String appName = config.getAppName();
        String appNameWithoutSpaces = appName.replace(SPACE_CHAR, UNDERSCORE_CHAR);
        StringBuilder apkName = new StringBuilder(appNameWithoutSpaces);
        apkName.append(DASH_CHAR);
 
        int majorVersionNumber = getMajorVersionNumber();
        config.setApkVersionMajor(majorVersionNumber);
        apkName.append(majorVersionNumber);
        apkName.append(UNDERSCORE_CHAR);
       
        int minorVersionNumber = getMinorVersionNumber();
        config.setApkVersionMinor(minorVersionNumber);
        apkName.append(minorVersionNumber);
        apkName.append(UNDERSCORE_CHAR);
        
        int uniqueBuildNumber = getUniqueBuildNumber(apkName.toString());
        config.setApkVersionBuild(uniqueBuildNumber);
        
        apkName.append(uniqueBuildNumber);
        apkName.append(APK_EXTENSION);
        
        config.setApkName(apkName.toString());
		session.setAttribute(SessionAttributes.APP_CONFIG, config);
	}

	private int getMajorVersionNumber()
	{
		//TODO get real Major Version # from Build configuration
		return 2;
	}

	private int getMinorVersionNumber()
	{
		//TODO get real Minor Version # from Build configuration
		return 3;
	}

	private int getUniqueBuildNumber(String partialApkName)
	{
		int greatestBuildNumberFound = 0;
		//TODO check the build directory for any APK's with this name & Major/Minor version # then
		//get the last build # and increment by 1.
		
		return greatestBuildNumberFound+1;
	}

	private void updateServerConfiguration(HttpSession session)
	{
        AppConfiguration config = (AppConfiguration)session.getAttribute(SessionAttributes.APP_CONFIG);
		if(ServerConstants.usingRealServer())
			config.setServerName("SL1 IE");
		else
			config.setServerName("SL1 Development");
		config.setServerIP(ServerConstants.getCurrentServerIp());
		config.setServerPublicCode(ServerConstants.getCurrentSeverKey());
		session.setAttribute(SessionAttributes.APP_CONFIG, config);
	}

	// TODO add unit tests
	private boolean getClientPublicKeyFromToken(HttpSession session, AppConfiguration appConfig)
	{
		//TODO get real key from Token server.
        AppConfiguration config = (AppConfiguration)session.getAttribute(SessionAttributes.APP_CONFIG);
 		config.setClientPublicKey(DESKTOP_PUBLIC_KEY);
 		try
		{
			config.setClientPublicCode(MartusCrypto.computeFormattedPublicCode40(DESKTOP_PUBLIC_KEY));
		}
		catch (CreateDigestException | CheckDigitInvalidException e)
		{
			appConfig.setClientTokenError("Error: Token not found.");
			return false;
		}
 		session.setAttribute(SessionAttributes.APP_CONFIG, config);
		return true;
	}

	//TODO add unit tests!
	private boolean isValidToken(AppConfiguration appConfig)
	{
		String tokenString = appConfig.getClientToken().trim();
		appConfig.setClientToken(tokenString);
		try
		{
			new MartusAccountAccessToken(tokenString);
		}
		catch (TokenInvalidException e)
		{
			appConfig.setClientTokenError("Error: Token is invalid");
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
