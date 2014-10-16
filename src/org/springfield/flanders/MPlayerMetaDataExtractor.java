/* 
* MPlayerMetaDataExtractor.java
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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.GregorianCalendar;

import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springfield.flanders.homer.FlandersProperties;
import org.springfield.flanders.homer.LazyHomer;
import org.springfield.flanders.tools.HttpHelper;

/**
* Application that will analyze a video file and return an xml file
* containing information regarding the video. 
* It uses mplayer to gather the information.
*
* @author Levi Pires <l.pires@noterik.nl>
* @copyright Copyright: Noterik B.V. 2008
* @package org.springfield.flanders
* @project flanders
* @access private
* @version 
*
*/

public class MPlayerMetaDataExtractor {	
	private static String temp_folder;	
	
	public static String extractMetaData(String source) {
		FlandersProperties fp = LazyHomer.getMyFlandersProperties();
		temp_folder = fp.getTemporaryDirectory();
		
		if (source == null) {
			return HttpHelper.getErrorMessageAsString("500", "No source defined in request",
					"Please define a source",
					"http://teamelements.noterik.com/team");
		}
    	String cmd = null; 
    	String arg1 = null;
    	String arg2 = null;
    	String arg3 = null;
		String metadataFile = buildFileName();				
		cmd =  GlobalConfig.instance().getMplayerScriptDir().replace("\n", "");
		arg1 = GlobalConfig.instance().getMplayerPath().replace("\n", "");
		arg2 = source.replace("\n", "");
		arg3 = metadataFile.replace("\n", "");		
		System.out.println("Command is: ");		
		System.out.println(cmd + " " + arg1 + " " + arg2 + " " + arg3);				
		if (source != null && !source.equals("") && metadataFile != null && !metadataFile.equals("")) {
			System.out.println("-- ABOUT TO RUN THE COMMAND --");
			commandRunner(cmd, arg1, arg2, arg3);			
			System.out.println("-- FINISHED WITH THE COMMAND --");
			return getResponseStringFromMPlayerTempFile(metadataFile, source);			
		} else {
			return HttpHelper.getErrorMessageAsString("500", "Could not construct the command for mplayer",
					"ERROR: could not construct the command for mplayer",
					"http://teamelements.noterik.com/team");
		}
    	
	}
	
	/**
	 * depending on the operating system, this function will create the file name/location
	 * for the temporary metadata file
	 * 
	 * @return String metadata filename
	 */
	public static String buildFileName(){
		
		Calendar cal = new GregorianCalendar();
    	
    	long stamp = cal.getTimeInMillis();
    	
    	System.out.println(stamp);
    	String path = "";
    	String fileName = "";
    	
    	String opSys = System.getProperty("os.name").toLowerCase();
    	
    	//running in windows
    	if(opSys.contains("windows")){
    		path = "c:" + temp_folder.replace('/', File.separatorChar);
    		fileName = path + File.separatorChar + stamp + ".dat";
    		boolean success = (new File(path)).mkdirs();
    	}
    	//running in linux
    	else{    		
    		fileName = temp_folder + "/" + stamp + ".dat";
    		boolean success = (new File(temp_folder)).mkdirs();
    	}   	
    	
		
		return fileName;
	}

	
	
	/**
	 * given the location of the metadata file 
	 * @param path
	 * @return
	 */
	private static String getResponseStringFromMPlayerTempFile(String path, String source) {
		FileReader tempMetadataFile = null;
		//String xml = "<meta-data>";
		String xml = "";
		System.out.println("file is: " + path);
		
		Element metaEl = DocumentHelper.createElement("meta-data");
		try {
			tempMetadataFile = new FileReader(path);
		} catch (FileNotFoundException e) {
			xml = HttpHelper.getErrorMessageAsString("500", "MPlayer could not save the meta-data to a file",
					"Error: " + e.getMessage(),
					"http://teamelements.noterik.com/team");
		}
		
		BufferedReader reader = new BufferedReader(tempMetadataFile);
		long br = 0;
		long abr = 0;
		double dur = 0;
		int width = 0;
		int height = 0;
		try {
			while (reader.ready()) {
				String line = reader.readLine();
				int term = line.indexOf('=');
				if (term != -1) {
					String key = line.substring(0, term);
					String value = line.substring(term + 1, line.length());
					if (key.equals("ID_VIDEO_ID")) {
						//TODO
					} else if (key.equals("ID_AUDIO_ID")) {
						//TODO
					} else if (key.equals("ID_FILENAME")) {
						//TODO
					} else if (key.equals("ID_DEMUXER")) {
						//TODO
					} else if (key.equals("ID_VIDEO_FORMAT")) {
						metaEl = addValue(metaEl, value, "videocodec");					
					} else if (key.equals("ID_VIDEO_BITRATE")) {
						// this is not always retreived correctly so always calculate it.												
					} else if (key.equals("ID_VIDEO_WIDTH")) {
						metaEl = addValue(metaEl, value, "width");
						width = Integer.parseInt(value);
					} else if (key.equals("ID_VIDEO_HEIGHT")) {
						metaEl = addValue(metaEl, value, "height");
						height = Integer.parseInt(value);
					} else if (key.equals("ID_VIDEO_FPS")) {
						metaEl = addValue(metaEl, value, "framerate");
					} else if (key.equals("ID_VIDEO_ASPECT")) {
						metaEl = addValue(metaEl, value, "pixelaspect");
						double pixelaspect = Double.parseDouble(value);
						if (pixelaspect != 0.0) {
							//check if width & height have a correct aspect ratio
							if (width != 0 && height != 0 && (int)(width / pixelaspect) != height) {
								width = (int)(height*pixelaspect);
								metaEl = addValue(metaEl, String.valueOf(width), "width");
							}
						}						
					} else if (key.equals("ID_AUDIO_CODEC")) {						
						//TODO
					} else if (key.equals("ID_AUDIO_FORMAT")) {						
						metaEl = addValue(metaEl, value, "audiocodec");
					} else if (key.equals("ID_AUDIO_BITRATE")) {
						System.out.println("ABR: " + value);
						abr = new Long(value).longValue();
						metaEl = addValue(metaEl, value, "audiobitrate");
					} else if (key.equals("ID_AUDIO_RATE")) {						
						metaEl = addValue(metaEl, value, "samplerate");				
					} else if (key.equals("ID_AUDIO_NCH")) {
						metaEl = addValue(metaEl, value, "audiochannels");
					} else if (key.equals("ID_LENGTH")) {
						System.out.println("DURATION: " + value);
						dur = Double.valueOf(value);						
						metaEl = addValue(metaEl, dur + "", "duration");
					} else if (key.equals("ID_VIDEO_CODEC")){
						//TODO
					}
				}
			}
			if(dur > 0){
				System.out.println("Calculating video bitrate");
				File f = new File(source);
				long fs = f.length() * 8;
				System.out.println("size: " + fs + " duration: " + (int)dur + " abr: " + abr);
				if((int)dur != 0)
					br = fs / (int)dur - abr;
				metaEl = addValue(metaEl, br + "", "videobitrate");
			}
			
			metaEl.addElement("metadata_file").setText(path);
			
			// add filesize
			File sFile = new File(source);
			if(sFile.exists()) {
				long filesize = sFile.length();
				metaEl = addValue(metaEl, filesize+"", "filesize");
			}
			
			xml = metaEl.asXML();
			System.out.println(xml);			
		} catch (IOException e) {
			xml = HttpHelper.getErrorMessageAsString("500", "MPlayer could not save the meta-data to a file",
					"MPlayer could not save the meta-data to a file",
					"http://teamelements.noterik.com/team");
		} finally {
			try {
				reader.close();
				tempMetadataFile.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return xml;
	}
	
	
	/**
	 * converts the String with the seconds passed as argument to an int
	 *  
	 * @param int duration in milliseconds
	 * @return
	 */
	public static int calculateMillis(String value){		
		double length = Double.valueOf(value);
		int durInt = (int)(length);		
		return durInt;
	}
	
	
	/**
	 * 
	 * 
	 * @param metaEl
	 * @param value
	 * @param type
	 * @return
	 */
	public static Element addValue(Element metaEl, String value, String type){
		Element elem = (Element)metaEl.selectSingleNode(type);
			
		if(elem == null){
			metaEl.addElement(type).setText(value);
		}else if(!value.equals("0") && !value.equals("") && Double.valueOf(value)!= 0.0){
			elem.setText(value);
		}
				
		return metaEl;
	}		

	
	/**
	 * Runs the command passed as parameter
	 *
	 * @param comand
	 */
	private static void commandRunner(String cmd, String arg1, String arg2, String arg3) {
		
		ProcessBuilder pb = new ProcessBuilder(cmd, arg1, arg2, arg3);
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