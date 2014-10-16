package org.springfield.flanders.homer;

public class MountProperties {
	
	private String hostname;
	private String path;
	private String account;
	private String password;
	private String protocol;
	
	//flanders specific
	private String ffmpegPath;

	public void setHostname(String h) {
		hostname = h;
	}
	
	public void setPath(String p) {
		path = p;
	}
	
	public void setAccount(String a) {
		account = a;
	}
	
	public void setPassword(String p) {
		password = p;
	}
	
	public void setProtocol(String p) {
		protocol = p;
	}

	public void setFfmpegPath(String f) {
		ffmpegPath = f;
	}
	
	public String getHostname() {
		return hostname;
	}
	
	public String getPath() {
		return path;
	}
	
	public String getAccount() {
		return account;
	}
	
	public String getPassword() {
		return password;
	}
	
	public String getProtocol() {
		return protocol;
	}
	
	public String getFfmpegPath() {
		return ffmpegPath;
	}
}
