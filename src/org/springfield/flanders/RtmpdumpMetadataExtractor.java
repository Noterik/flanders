/* 
* RtmpdumpMetadataExtractor.java
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
import org.springfield.flanders.tools.HttpHelper;

public class RtmpdumpMetadataExtractor {
	
	private static final String TMP_FOLDER = "/tmp/flanders";	
	
	public static String extractMetaData(String stream, String filename) {
		if (stream == null) {
			return HttpHelper.getErrorMessageAsString("500", "No source defined in request",
					"Please define a source",
					"http://teamelements.noterik.com/team");
		}
		String metadataFile = buildFileName();				
		String cmd =  GlobalConfig.instance().getRtmpdumpScriptDir().replace("\n", "");
		String arg1 = GlobalConfig.instance().getRtmpdumpPath().replace("\n", "");
		String arg2 = stream.replace("\n", "");
		String arg3 = filename.replace("\n", "");
		String arg4 = metadataFile.replace("\n", "");		
		System.out.println("Command is: ");		
		System.out.println(cmd + " " + arg1 + " " + arg2 + " " + arg3);				
		if (stream != null && !stream.equals("") && metadataFile != null && !metadataFile.equals("")) {
			System.out.println("-- ABOUT TO RUN THE COMMAND --");
			commandRunner(cmd, arg1, arg2, arg3, arg4);			
			System.out.println("-- FINISHED WITH THE COMMAND --");
			return getResponseStringFromRtmpdumpTempFile(metadataFile, stream);			
		} else {
			return HttpHelper.getErrorMessageAsString("500", "Could not construct the command for rtmpdump",
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
    		path = "c:" + TMP_FOLDER.replace('/', File.separatorChar);
    		fileName = path + File.separatorChar + stamp + ".dat";
    		boolean success = (new File(path)).mkdirs();
    	}
    	//running in linux
    	else{    		
    		fileName = TMP_FOLDER + "/" + stamp + ".dat";
    		boolean success = (new File(TMP_FOLDER)).mkdirs();
    	}   	
    	
		
		return fileName;
	}

	
	
	/**
	 * given the location of the metadata file 
	 * @param path
	 * @return
	 */
	private static String getResponseStringFromRtmpdumpTempFile(String path, String source) {
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
		double filesize = 0;
		double width=0, height=0;
		try {
			while (reader.ready()) {
				String line = reader.readLine().trim();
				// windows rtmpdump 2.3 thing
				if(line.indexOf("INFO:") != -1) {
					line = line.substring( line.indexOf("INFO:")+5 ).trim();
				}
				System.out.println(line);
				String[] parts = line.split("\\s+");
				if (parts.length >= 2) {
					String key = parts[0];
					String value = parts[1];
					if (key.equals("timescale")) {
						//TODO
					} else if (key.equals("length")) {
						//TODO
					}  else if (key.equals("language")) {
						//TODO
					} else if (key.equals("sampletype")) {
						//TODO
					} else if (key.equals("timescale")) {
						//TODO
					} else if (key.equals("language")) {
						//TODO
					} else if (key.equals("sampletype")) {
						//TODO
					} else if (key.equals("audiochannels")) {
						try {
							double audiochannels = Double.parseDouble(value);
							metaEl = addValue(metaEl, Long.toString((long)audiochannels), "audiochannels");
						} catch(Exception e) {
							System.out.println("Could not parse audiochannels");
						}
					} else if (key.equals("audiosamplerate")) {
						try {
							double samplerate = Double.parseDouble(value);
							metaEl = addValue(metaEl, Long.toString((long)samplerate), "samplerate");
						} catch(Exception e) {
							System.out.println("Could not parse samplerate");
						}
					} else if (key.equals("videoframerate")) {
						metaEl = addValue(metaEl, value, "framerate");
					} else if (key.equals("aacaot")) {
						//TODO
					} else if (key.equals("avclevel")) {
						//TODO
					} else if (key.equals("avcprofile")) {
						//TODO
					} else if (key.equals("audiocodecid")) {
						metaEl = addValue(metaEl, value, "audiocodec");
					} else if (key.equals("videocodecid")) {
						metaEl = addValue(metaEl, value, "videocodec");	
					} else if (key.equals("width")) {
						try {
							width = Double.parseDouble(value);
							metaEl = addValue(metaEl, Long.toString((long)width), "width");
						} catch(Exception e) {
							System.out.println("Could not parse width");
						}
					} else if (key.equals("height")) {
						try {
							height = Double.parseDouble(value);
							metaEl = addValue(metaEl, Long.toString((long)height), "height");
						} catch(Exception e) {
							System.out.println("Could not parse height");
						}
					} else if (key.equals("moovposition")) {
						//TODO
					} else if (key.equals("duration")) {
						metaEl = addValue(metaEl, value, "duration");
						try {
							dur = Double.parseDouble(value);
						} catch(Exception e) {
							System.out.println("Could not parse duration");
						}
					}
				}
				
			}
			if(height != 0) {
				double pixelaspect = width/height;
				metaEl = addValue(metaEl, Double.toString(pixelaspect), "pixelaspect");
			}
			if(dur > 0){
				System.out.println("Calculating video bitrate");
				System.out.println("size: " + filesize + " duration: " + (int)dur + " abr: " + abr);
				if((int)dur != 0)
					br = (long)filesize / (int)dur - abr;
				metaEl = addValue(metaEl, br + "", "videobitrate");
			}
			
			metaEl.addElement("metadata_file").setText(path);
			
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
		}else if(!value.trim().equals("0") && !value.trim().equals("0.0") && !value.trim().equals("")){
			elem.setText(value);
		}
		return metaEl;
	}		

	
	/**
	 * Runs the command passed as parameter
	 *
	 * @param comand
	 */
	private static void commandRunner(String cmd, String arg1, String arg2, String arg3, String arg4) {
		
		ProcessBuilder pb = new ProcessBuilder(cmd, arg1, arg2, arg3, arg4);
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