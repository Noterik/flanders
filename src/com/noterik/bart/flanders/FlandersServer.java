package com.noterik.bart.flanders;

import org.apache.log4j.Logger;

public class FlandersServer {
	/** The FlandersServer's log4j Logger */
	private static Logger logger = Logger.getLogger(FlandersServer.class);
	
	/** instance */
	private static FlandersServer instance = new FlandersServer();
	
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
