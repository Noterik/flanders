/* 
* MjpegIndexer.java
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
package org.springfield.flanders;

import java.io.IOException;
import java.io.InputStream;

import org.apache.log4j.Logger;
import org.springfield.flanders.homer.FlandersProperties;
import org.springfield.flanders.homer.LazyHomer;
import org.springfield.flanders.tools.HttpHelper;

/**
 * MjpegIndexer.java
 *
 * @author Pieter van Leeuwen
 * @copyright Copyright: Noterik B.V. 2014
 * @package org.springfield.flanders
 * 
 */
public class MjpegIndexer {
	private static Logger LOG = Logger.getLogger(MjpegIndexer.class);
	
	public static String extractMetaData(String source, String mountPath) {
		FlandersProperties fp = LazyHomer.getMyFlandersProperties();
		
		if (source == null) {
			return HttpHelper.getErrorMessageAsString("500", "No source defined in request",
					"Please define a source",
					"http://teamelements.noterik.com/team");
		}
    	String cmd = null; 
    	String arg1 = null;
    	String arg2 = null;
    	String arg3 = null;
		String metadataFile = buildFileName(source);	
		cmd = fp.getMjpegIndexPath();
		arg1 = mountPath + source.replace("\n", "");
		arg2 = mountPath + metadataFile.replace("\n", "");		
		LOG.debug("Command is: ");		
		LOG.debug(cmd + " " + arg1 + " " + arg2);			
		if (source != null && !source.equals("") && metadataFile != null && !metadataFile.equals("")) {
			LOG.debug("-- ABOUT TO RUN THE COMMAND --");
			commandRunner(cmd, arg1, arg2);			
			LOG.debug("-- FINISHED WITH THE COMMAND --");
			return "<index><json>"+metadataFile+"</json></index>";			
		} else {
			return HttpHelper.getErrorMessageAsString("500", "Could not construct the command for mplayer",
					"ERROR: could not construct the command for mplayer",
					"http://teamelements.noterik.com/team");
		}
	}
	
	public static String buildFileName(String source){
		String prefix = source.substring(0, source.lastIndexOf("."));
		return prefix+".json";
	}
	
	/**
	 * Runs the command passed as parameter
	 *
	 * @param comand
	 */
	private static void commandRunner(String cmd, String arg1, String arg2) {
		
		ProcessBuilder pb = new ProcessBuilder(cmd, arg1, arg2);
		pb.redirectErrorStream(true);
		
		try {
			Process p = pb.start();
			InputStream in = p.getInputStream();
			
			int c;
			while ((c = in.read()) != -1) {
				System.out.print((char) c);
			}
			in.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
