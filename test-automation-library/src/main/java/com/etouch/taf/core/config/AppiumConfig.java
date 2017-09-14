package com.etouch.taf.core.config;

public class AppiumConfig {
	
	private String nodePath;
	
	private String appiumJSPath;
	
	private String macNodePath;
	
	private String macAppiumJSPath;
	
	public String getNodePath() {
		return nodePath;
	}

	public void setNodePath(String nodePath) {
		this.nodePath = nodePath;
	}

	public String getAppiumJSPath() {
		return appiumJSPath;
	}

	public void setAppiumJSPath(String appiumJSPath) {
		this.appiumJSPath = appiumJSPath;
	}

	public String getMacNodePath() {
		return macNodePath;
	}

	public void setMacNodePath(String macNodePath) {
		this.macNodePath = macNodePath;
	}

	public String getMacAppiumJSPath() {
		return macAppiumJSPath;
	}

	public void setMacAppiumJSPath(String macAppiumJSPath) {
		this.macAppiumJSPath = macAppiumJSPath;
	}

}
