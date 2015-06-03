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

public class AppConfiguration
{
	private String appName;
	private String appNameError;
	private String appIconLocation;
	private String appIconError;
	private String appXFormLocation;
	private String appXFormError;
	
	public void setAppName(String name)
	{
		appName = name;
	}
	
	public String getAppName()
	{
		return appName;
	}

	public String getAppNameError()
	{
		return appNameError;
	}

	public void setAppNameError(String appNameError)
	{
		this.appNameError = appNameError;
	}

	public String getAppIconLocation()
	{
		return appIconLocation;
	}

	public void setAppIconLocation(String appIconLocation)
	{
		this.appIconLocation = appIconLocation;
	}
	
	@Override
	public String toString()
	{
		return "Name: " + appName + ", Icon Loc: " + appIconLocation + ", XForm Loc: " + appXFormLocation;
	}

	public String getAppIconError()
	{
		return appIconError;
	}

	public void setAppIconError(String appIconError)
	{
		this.appIconError = appIconError;
	}

	public String getAppXFormLocation()
	{
		return appXFormLocation;
	}

	public void setAppXFormLocation(String appXFormLocation)
	{
		this.appXFormLocation = appXFormLocation;
	}

	public String getAppXFormError()
	{
		return appXFormError;
	}

	public void setAppXFormError(String appXFormError)
	{
		this.appXFormError = appXFormError;
	}	
}
