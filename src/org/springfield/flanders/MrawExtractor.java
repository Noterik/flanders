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

public class MrawExtractor {
	private static String tempFolder;
	
	public static String extractMetaData(String source) {
		FlandersProperties fp = LazyHomer.getMyFlandersProperties();
		tempFolder = fp.getTemporaryDirectory();
		
		if (source == null) {
			return HttpHelper.getErrorMessageAsString("500", "No source defined in request",
					"Please define a source",
					"http://teamelements.noterik.com/team");
		}
		String metadataFile = buildFileName();
		
		String cmd = null; 
	    	String arg1 = null;
	    	String arg2 = null;
	    	String arg3 = null;
				
		cmd =  GlobalConfig.instance().getIdtRawScriptDir().replace("\n", "");
		arg1 = GlobalConfig.instance().getIdtRawPath().replace("\n", "");
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
		    return HttpHelper.getErrorMessageAsString("500", "Could not construct the command for idt_raw",
			    "ERROR: could not construct the command for idt_raw",
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
			path = "c:" + tempFolder.replace('/', File.separatorChar);
			fileName = path + File.separatorChar + stamp + ".dat";
			boolean success = (new File(path)).mkdirs();
		}
		//running in linux
		else{    		
		    fileName = tempFolder + "/" + stamp + ".dat";
		    boolean success = (new File(tempFolder)).mkdirs();
		}   	
		
		return fileName;
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
    
    /**
     * @param metaEl
     * @param value
     * @param type
     * @return
     */
    public static Element addValue(Element metaEl, String value, String type){
	Element elem = (Element)metaEl.selectSingleNode(type);
			
	if(elem == null){
		metaEl.addElement(type).setText(value);
	}/*else if(!value.equals("0") && !value.equals("") && Double.valueOf(value)!= 0.0){
		elem.setText(value);
	}*/
			
	return metaEl;
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
	    xml = HttpHelper.getErrorMessageAsString("500", "idt_raw could not save the meta-data to a file",
		"Error: " + e.getMessage(),
		"http://teamelements.noterik.com/team");
	}
		
	BufferedReader reader = new BufferedReader(tempMetadataFile);
	long br = 0;
	double dur = 0;
	int fps = 0;
		
	try {
	    String line = reader.readLine();
			
	    while (line != null) {
		 int term = line.indexOf('=');
		 if (term != -1) {
		     String key = line.substring(0, term);
		     String value = line.substring(term + 1, line.length());
		     
		     if (key.toLowerCase().equals("framerate")) {
			 metaEl = addValue(metaEl, value, "framerate");
			 fps = Integer.parseInt(value);
		     } else if (key.toLowerCase().equals("width")) {
			 metaEl = addValue(metaEl, value, "width");
		     } else if (key.toLowerCase().equals("height")) {
			 metaEl = addValue(metaEl, value, "height");
		     } else if (key.toLowerCase().equals("number_of_frames")) {
			 dur = ((double) Integer.parseInt(value)) / ((double) fps);
			 metaEl = addValue(metaEl, dur + "", "duration");
		     }
		 }
		 line = reader.readLine();
	    }
	} catch (IOException e) {
	    xml = HttpHelper.getErrorMessageAsString("500", "idt_raw could not save the meta-data to a file",
		"idt_raw could not save the meta-data to a file",
		"http://teamelements.noterik.com/team");
	} finally {
	    try {
		reader.close();
		tempMetadataFile.close();
	    } catch (IOException e) {
		e.printStackTrace();
	    }
	}
	
	if(dur > 0) {
	    System.out.println("Calculating video bitrate");
	    File f = new File(source);
	    long fs = f.length() * 8;
	    System.out.println("size: " + fs + " duration: " + (int)dur);
	    if((int)dur != 0) {
		br = fs / (int)dur;
	    }
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
		
	return xml;
    }
	
}
