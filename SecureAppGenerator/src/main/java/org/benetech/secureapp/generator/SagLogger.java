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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

public class SagLogger
{
	  static Logger log = Logger.getLogger("SecureAppGenerator");
	
	public static synchronized void logDebug(HttpSession session, String text)
	{
		log.debug(getMsgIncludingSessionIdIfPresent(session, text));
	}

	public static synchronized void logInfo(HttpSession session, String text)
	{
		log.info(getMsgIncludingSessionIdIfPresent(session, text));
	}

	public static void logException(HttpSession session, Exception e)
	{
		log.error(getMsgIncludingSessionIdIfPresent(session, "Exception"), e);
	}

	public static synchronized void logError(HttpSession session, String errorMsg)
	{
		log.error(getMsgIncludingSessionIdIfPresent(session, errorMsg));
	}

	public static synchronized void logWarning(HttpSession session, String warningMsg)
	{
		log.warn(getMsgIncludingSessionIdIfPresent(session, warningMsg));
	}
	
	public static synchronized void logProcess(HttpSession session, Process p) throws IOException
	{
		String line;
		logDebug(session, "Exec Output:");
		BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));
		BufferedReader error = new BufferedReader(new InputStreamReader(p.getErrorStream()));
		while ((line = input.readLine()) != null) 
		{
			logDebug(session, "  |" + line);
		}
		while ((line = error.readLine()) != null) 
		{
			logDebug(session, "  ||" + line);
		}		
		logDebug(session, "Done.");
		input.close();
	}
	
	public static String getElapsedTime(long startTime, long endTime)
	{
		long elapsedTime = endTime-startTime;
   		String timeToBuild = String.format("%02d:%02d", 
   			    TimeUnit.MILLISECONDS.toMinutes(elapsedTime) - 
   			    TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(elapsedTime)),
   			    TimeUnit.MILLISECONDS.toSeconds(elapsedTime) - 
   			    TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(elapsedTime)));
		return timeToBuild;
	}
	
	private static String getMsgIncludingSessionIdIfPresent(HttpSession session, String text)
	{
		if(session != null)
			return session.getId() + " | " + text;
		return text;
	}
}
