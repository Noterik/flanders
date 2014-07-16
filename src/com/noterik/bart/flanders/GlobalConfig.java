/*
 * Created on Feb 11, 2008
 */
package com.noterik.bart.flanders;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import com.noterik.bart.flanders.tools.Chmod;

public class GlobalConfig {

	private static GlobalConfig instance = new GlobalConfig();
	private static String CONFIG_FILE = "config.xml";

	private String baseDir;
	private String mplayerScriptDir;
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
			mplayerScriptDir = baseDir + "scripts" + File.separator + "mp_extract.bat";
			rtmpdumpScriptDir = baseDir + "scripts" + File.separator + "rtmpd_extract.bat";
		}
		//running linux
		else{
			mplayerScriptDir = baseDir + "scripts" + File.separator + "mp_extract.sh";		
			rtmpdumpScriptDir = baseDir + "scripts" + File.separator + "rtmpd_extract.sh";		
			Chmod.chmodDefault(mplayerScriptDir);
			Chmod.chmodDefault(rtmpdumpScriptDir);
		}
		
		initConfig();
	}
	
	public String getBaseDir(){
		return baseDir;
	}	
	
	public String getMplayerScriptDir(){
		return mplayerScriptDir;
	}
	
	public String getMplayerPath() {
		return config.getProperty("mplayer-path");
	}
	
	public String getRtmpdumpScriptDir(){
		return rtmpdumpScriptDir;
	}
	
	public String getRtmpdumpPath() {
		return config.getProperty("rtmpdump-path");
	}

}