package com.etouch.taf.core.config;

import java.net.URL;

import com.etouch.taf.util.TafPassword;

/**
 * @author etouch
 * 
 */
public class RallyConfig {

	private String projectId;

	private String workspaceId;

	private String defectOwner;

	private String build;

	private String username;

	private TafPassword password;

	private URL url;

	private String rallyApp;

	private boolean screenshotAttachment;

	private boolean videoAttachment;

	/**
	 * @return the projectId
	 */
	public String getProjectId() {
		return projectId;
	}

	/**
	 * @param projectId
	 *            the projectId to set
	 */
	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}

	/**
	 * @return the workspaceId
	 */
	public String getWorkspaceId() {
		return workspaceId;
	}

	/**
	 * @param workspaceId
	 *            the workspaceId to set
	 */
	public void setWorkspaceId(String workspaceId) {
		this.workspaceId = workspaceId;
	}

	/**
	 * @return the defectOwner
	 */
	public String getDefectOwner() {
		return defectOwner;
	}

	/**
	 * @param defectOwner
	 *            the defectOwner to set
	 */
	public void setDefectOwner(String defectOwner) {
		this.defectOwner = defectOwner;
	}

	/**
	 * @return the build
	 */
	public String getBuild() {
		return build;
	}

	/**
	 * @param build
	 *            the build to set
	 */
	public void setBuild(String build) {
		this.build = build;
	}

	/**
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * @param username
	 *            the username to set
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
	public URL getUrl() {
		return url;
	}

	/**
	 * @param url
	 *            the url to set
	 */
	public void setUrl(URL url) {
		this.url = url;
	}

	/**
	 * @return the rallyApp
	 */
	public String getRallyApp() {
		return rallyApp;
	}

	/**
	 * @param rallyApp
	 *            the rallyApp to set
	 */
	public void setRallyApp(String rallyApp) {
		this.rallyApp = rallyApp;
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
