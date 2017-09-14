package com.etouch.taf.core.config;

import com.etouch.taf.util.TafPassword;

/**
 * @author etouch
 *
 */
public class JiraConfig {

	private String username;
	
	private TafPassword password;
	
	private String url;
	
	private String key;
	
	private String projectName;

	private String environment;
	
	private boolean screenshotAttachment;
	
	private boolean videoAttachment;
	
	/**
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * @param username the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	

	public TafPassword getPassword() {
		return password;
	}

	public void setPassword(TafPassword password) {
		this.password = password;
	}

	/**
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * @param url the url to set
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * @return the key
	 */
	public String getKey() {
		return key;
	}

	/**
	 * @param key the key to set
	 */
	public void setKey(String key) {
		this.key = key;
	}

	/**
	 * @return the projectName
	 */
	public String getProjectName() {
		return projectName;
	}

	/**
	 * @param projectName the projectName to set
	 */
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	/**
	 * @return the environment
	 */
	public String getEnvironment() {
		return environment;
	}

	/**
	 * @param environment the environment to set
	 */
	public void setEnvironment(String environment) {
		this.environment = environment;
	}

	/**
	 * @return the screenshotAttachment
	 */
	public boolean isScreenshotAttachment() {
		return screenshotAttachment;
	}

	/**
	 * @param screenshotAttachment the screenshotAttachment to set
	 */
	public void setScreenshotAttachment(boolean screenshotAttachment) {
		this.screenshotAttachment = screenshotAttachment;
	}

	/**
	 * @return the videoAttachment
	 */
	public boolean isVideoAttachment() {
		return videoAttachment;
	}

	/**
	 * @param videoAttachment the videoAttachment to set
	 */
	public void setVideoAttachment(boolean videoAttachment) {
		this.videoAttachment = videoAttachment;
	}
	

}
