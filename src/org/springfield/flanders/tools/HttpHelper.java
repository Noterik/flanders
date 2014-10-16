/*
 * Created on Jun 30, 2008
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