/* 
* FlandersServer.java
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

import org.apache.log4j.Logger;

public class FlandersServer {
	/** The FlandersServer's log4j Logger */
	private static Logger logger = Logger.getLogger(FlandersServer.class);
	
	/** instance */
	private static FlandersServer instance = new FlandersServer();
	
	/** Flanders package root */
	public static final String PACKAGE_ROOT = "org.springfield.flanders";
	
	/** Root path: the path the webservice is running in */
	private String rootPath;
	
	private Boolean running =  false;
	
	/**
	 * Sole constructor
	 */
	public FlandersServer() {
		instance = this;
	}
	
	/**
	 * Get Nelson Server instance
	 * @return
	 */
	public static FlandersServer instance() {
		return instance;
	}
	
	/**
	 * Set root path
	 */
	public void setRootPath(String rootPath) {
		this.rootPath = rootPath;
		init();
	}
	
	/**
	 * Initializes the configuration
	 */
	public void init() {
		running = true;
	}
	
	public Boolean isRunning() {
		if (running) {
			return true;
		}
		return false;
	}
	
	 /**
     * Shutdown
     */
	public void destroy() {
		instance = null;
		running = false;
	}
}
