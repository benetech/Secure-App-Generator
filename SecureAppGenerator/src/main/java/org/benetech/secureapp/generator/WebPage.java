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

public interface WebPage
{
	public static final String UPLOAD = "Upload";
	public static final String NEXT_PAGE = "Next";
	public static final String PREVIOUS_PAGE = "Previous";
	public static final String WELCOME = "/welcome";
	public static final String FINAL = "/final";
	public static final String ERROR = "/error";
	
	public static final String NAME_APP = "/nameApp";
	public static final String NAME_APP_PREV = NAME_APP +PREVIOUS_PAGE;
	public static final String NAME_APP_NEXT = NAME_APP +NEXT_PAGE;
	
	public static final String OBTAIN_LOGO = "/obtainLogo";
	public static final String OBTAIN_LOGO_PREVIOUS = OBTAIN_LOGO + PREVIOUS_PAGE;
	public static final String OBTAIN_LOGO_NEXT = OBTAIN_LOGO + NEXT_PAGE;
	public static final String OBTAIN_LOGO_UPLOAD = OBTAIN_LOGO + UPLOAD;
	
	public static final String OBTAIN_XFORM = "/obtainXForm";
	public static final String OBTAIN_XFORM_PREVIOUS = OBTAIN_XFORM+ PREVIOUS_PAGE;
	public static final String OBTAIN_XFORM_NEXT = OBTAIN_XFORM + NEXT_PAGE;
	public static final String OBTAIN_XFORM_UPLOAD = OBTAIN_XFORM + UPLOAD;

	public static final String OBTAIN_CLIENT_TOKEN = "/obtainToken";
	public static final String OBTAIN_CLIENT_TOKEN_PREVIOUS = OBTAIN_CLIENT_TOKEN+ PREVIOUS_PAGE;
	public static final String OBTAIN_CLIENT_TOKEN_NEXT = OBTAIN_CLIENT_TOKEN + NEXT_PAGE;

	public static final String SUMMARY = "/summary";
	public static final String SUMMARY_PREVIOUS = SUMMARY+ PREVIOUS_PAGE;
	public static final String SUMMARY_NEXT = SUMMARY + NEXT_PAGE;

}
