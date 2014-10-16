/* 
* HttpHelper.java
* 
* Copyright (c) 2014 Noterik B.V.
* 
* This file is part of flanders, related to the Noterik Springfield project.
*
* flanders is free software: you can redistribute it and/or modify
* it under the terms of the GNU General Public License as published by
* the Free Software Foundation, either version 3 of the License, or
* (at your option) any later version.
*
* flanders is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
* GNU General Public License for more details.
*
* You should have received a copy of the GNU General Public License
* along with flanders.  If not, see <http://www.gnu.org/licenses/>.
*/
package org.springfield.flanders.tools;

public class HttpHelper {

	/**
	 * This function wraps the supplied parameters in a webtv2 compliant XML
	 * error message and returns it
	 * @param errorId
	 * @param message
	 * @param details
	 * @param uri
	 * @return
	 */

	public static StringBuffer getErrorMessage(String errorId, String message, String details, String uri) {
		StringBuffer xml = new StringBuffer("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		xml.append("<error id=\"" + errorId + "\">");
		xml.append("<properties>");
		xml.append("<message>" + message + "</message>");
		xml.append("<details>" + details + "</details>");
		xml.append("<uri>" + uri + "</uri>");
		xml.append("</properties>");
		xml.append("</error>");
		return xml;
	}

	public static String getErrorMessageAsString(String errorId, String message, String details, String uri) {
		StringBuffer xml = getErrorMessage(errorId, message, details, uri);
		return xml == null ? null : xml.toString();
	}
}