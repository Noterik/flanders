/* 
* FlandersProperties.java
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
package org.springfield.flanders.homer;

public class FlandersProperties {
	private String ipnumber;
	private String name;
	private String status;
	private String decisionengine;
	private String numberofworkers;
	private String defaultloglevel;
	private String preferedsmithers;
	private String ffmpegpath;
	private String temporarydirectory;
	private String batchFilesPath;
	private String batchFilesExtension;
	private String mjpegIndexPath;
	
	public void setIpNumber(String i) {
		ipnumber = i;
	}
	
	public void setName(String n) {
		name = n;
	}
	
	public void setDecisionEngine(String d) {
		decisionengine = d;
	}
	
	public void setNumberOfWorkers(String n) {
		numberofworkers = n;
	}
	
	public void setDefaultLogLevel(String l) {
		defaultloglevel = l;
	}
	
	public void setFFMPEGPath(String p) {
		ffmpegpath = p;
	}
	
	public void setTemporaryDirectory(String d) {
		temporarydirectory = d;
	}
	
	public void setBatchFilesPath(String p) {
		batchFilesPath = p;
	}
	
	public void setBatchFilesExtension(String b) {
		batchFilesExtension = b;
	}
	
	public void setPreferedSmithers(String p) {
		preferedsmithers = p;
	}
	
	public void setStatus(String s) {
		status = s;
	}
	
	public void setMjpegIndexPath(String m) {
		mjpegIndexPath = m;
	}
	
	public String getName() {
		return name;
	}
	
	public String getIpNumber() {
		return ipnumber;
	}
	
	
	public String getStatus() {
		return status;
	}
	
	public String getDecisionEngine() {
		return decisionengine;
	}
	
	public String getNumberOfWorkers() {
		return numberofworkers;
	}
	
	public String getDefaultLogLevel() {
		return defaultloglevel;
	}
	
	public String getFFMPEGPath() {
		return ffmpegpath;
	}
	
	public String getPreferedSmithers() {
		return preferedsmithers;
	}
	
	public String getTemporaryDirectory() {
		return temporarydirectory;
	}
	
	public String getBatchFilesPath() {
		return batchFilesPath;
	}
	
	public String getBatchFilesExtension() {
		return batchFilesExtension;
	}
	
	public String getMjpegIndexPath() {
		return mjpegIndexPath;
	}
}
