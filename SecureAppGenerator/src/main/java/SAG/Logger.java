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

import org.martus.common.MartusLogger;

public class Logger extends MartusLogger
{
	private static final String VERBOSE_DEBUGGING_ON = "true";
	private static final String DEBUG_VERBOSE_ENV = "SAG_DEBUG_VERBOSE";

	public synchronized static void logVerbose(String text)
	{
  		String verbose = System.getenv(DEBUG_VERBOSE_ENV);
  		if(verbose != null && verbose.equals(VERBOSE_DEBUGGING_ON))
  			MartusLogger.log(text);
	}
}
