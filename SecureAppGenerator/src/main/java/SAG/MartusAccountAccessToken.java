/*

The Martus(tm) free, social justice documentation and
monitoring software. Copyright (C) 2014, Beneficent
Technology, Inc. (Benetech).

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
//This was taken from martus-common and really should be refactored not to include the pieces not needed here\
//TODO: Make this the base class which the MartusAccountAccessToken would derive from
package SAG;


public class MartusAccountAccessToken
{
	public static class TokenInvalidException extends Exception 
	{
		private static final long serialVersionUID = 454108626214413854L;
	}
	public static class TokenNotFoundException extends Exception 
	{
		private static final long serialVersionUID = 7196257508466309424L;
	}
	
	public MartusAccountAccessToken(String newToken) throws TokenInvalidException
	{
		setToken(newToken);
	}
	
	public String getToken()
	{
		return token;
	}
	
	public boolean equals(Object otherObject)
	{
		if(otherObject instanceof MartusAccountAccessToken)
			return getToken().equals(((MartusAccountAccessToken)otherObject).getToken());
		return false;
	}

	public String toString()
	{
		return getToken();
	}
	
	public int hashCode()
	{
		return getToken().hashCode();
	}
	
	private void setToken(String newToken) throws TokenInvalidException
	{
		if(!isValid(newToken))
			throw new TokenInvalidException();
		token = newToken;
	}
	
	public boolean isValid(String tokenToValidate)
	{
		return MartusAccountAccessToken.isTokenValid(tokenToValidate);
	}

	public static boolean isTokenValid(String tokenToValidate)
	{
		if(tokenToValidate.length() < MINIMUM_TOKEN_LENGTH)
			return false;
		
		DammCheckDigitAlgorithm validationCheck = new DammCheckDigitAlgorithm();
		return validationCheck.isTokenValid(tokenToValidate);
	}

	public static int MINIMUM_TOKEN_LENGTH = 7;
	
	public static final String REQUEST_MARTUS_ACCOUNT_ACCESS_TOKEN_JSON_TAG = "RequestMartusAccessToken";
	public static final String REQUEST_MARTUS_ACCOUNT_ACCESS_ACCOUTID_JSON_TAG = "RequestMartusAccessPublicKey";
	
	public static final String MARTUS_ACCOUNT_ACCESS_TOKEN_JSON_TAG = "MartusAccountAccessToken";
	public static final String MARTUS_ACCESS_TOKEN_JSON_TAG = "Token";
	public static final String MARTUS_ACCESS_TOKEN_CREATION_DATE_JSON_TAG = "TimeCreated";
	public static final String MARTUS_ACCESS_ACCOUNT_ID_JSON_TAG = "PublicKey";
	
	private String token;
}
