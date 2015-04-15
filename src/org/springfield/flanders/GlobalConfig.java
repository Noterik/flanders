/* 
* GlobalConfig.java
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

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import org.springfield.flanders.tools.Chmod;

public class GlobalConfig {

	private static GlobalConfig instance = new GlobalConfig();
	private static String CONFIG_FILE = "config.xml";

	private String baseDir;
	private String ffprobeScriptDir;
	private String rtmpdumpScriptDir;
	private Properties config; 

	/**
	 * Sole constructor
	 */
	private GlobalConfig(){}

	public static GlobalConfig instance(){
		return instance;
	}

	private void initConfig() {
		File file = new File(baseDir + "/conf/" + CONFIG_FILE);
		config = new Properties();
		try {
			if (file.exists()) {
				config.loadFromXML(new BufferedInputStream(new FileInputStream(file)));

			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void initialize(String baseDir) {
		System.out.println("initializing config: " + baseDir);
		this.baseDir = baseDir;
		
		String os = System.getProperty("os.name").toLowerCase();
		//running windows
		if(os.contains("windows")){
			ffprobeScriptDir = baseDir + "scripts" + File.separator + "ffprobe_extract.bat";
			rtmpdumpScriptDir = baseDir + "scripts" + File.separator + "rtmpd_extract.bat";
		}
		//running linux
		else{
			ffprobeScriptDir = baseDir + "scripts" + File.separator + "ffprobe_extract.sh";		
			rtmpdumpScriptDir = baseDir + "scripts" + File.separator + "rtmpd_extract.sh";		
			Chmod.chmodDefault(ffprobeScriptDir);
			Chmod.chmodDefault(rtmpdumpScriptDir);
		}
		
		initConfig();
	}
	
	public String getBaseDir(){
		return baseDir;
	}	
	
	public String getFfprobeScriptDir(){
		return ffprobeScriptDir;
	}
	
	public String getFfprobePath() {
		return config.getProperty("ffprobe-path");
	}
	
	public String getRtmpdumpScriptDir(){
		return rtmpdumpScriptDir;
	}
	
	public String getRtmpdumpPath() {
		return config.getProperty("rtmpdump-path");
	}

}