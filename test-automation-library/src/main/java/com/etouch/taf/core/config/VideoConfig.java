package com.etouch.taf.core.config;



/**
 * The Class TestngConfig.
 */
public class VideoConfig extends TafConfig {
	
	/** The videoPath. */
	private String videoPath;

	/** The copy to server. */
	private String copyToServer;
		
	/** The reporter. */
	private String serverName;
	
	/**  Parallel Mode. */
	private String deploymentPath;
	
	/**  Parallel Mode. */
	private String baseVideoLink;
	
	/** The screenshot path. */
	private String baseScreenshotPath;
	
	/**  The videoFileName  */
	private String videoFileName;
	
	/**  The screenshotFileName  */
	private String screenshotFileName;
	
	/**
	 * Gets the screenshot path.
	 *
	 * @return the screenshot path
	 */
	public String getBaseScreenshotPath() {
		return baseScreenshotPath;
	}

	/**
	 * Sets the screenshot path.
	 *
	 * @param screenshotPath the new screenshot path
	 */
	public void setBaseScreenshotPath(String screenshotPath) {
		this.baseScreenshotPath = screenshotPath;
	}

	/**
	 * Gets the video path.
	 *
	 * @return the video path
	 */
	public String getVideoPath() {
		return videoPath;
	}

	/**
	 * Sets the video path.
	 *
	 * @param videoPath the new video path
	 */
	public void setVideoPath(String videoPath) {
		this.videoPath = videoPath;
	}

	/**
	 * Gets the copy to server.
	 *
	 * @return the copy to server
	 */
	public String getCopyToServer() {
		return copyToServer;
	}

	/**
	 * Sets the copy to server.
	 *
	 * @param copyToServer the new copy to server
	 */
	public void setCopyToServer(String copyToServer) {
		this.copyToServer = copyToServer;
	}

	/**
	 * Gets the server name.
	 *
	 * @return the server name
	 */
	public String getServerName() {
		return serverName;
	}

	/**
	 * Sets the server name.
	 *
	 * @param serverName the new server name
	 */
	public void setServerName(String serverName) {
		this.serverName = serverName;
	}

	/**
	 * Gets the deployment path.
	 *
	 * @return the deployment path
	 */
	public String getDeploymentPath() {
		return deploymentPath;
	}

	/**
	 * Sets the deployment path.
	 *
	 * @param deploymentPath the new deployment path
	 */
	public void setDeploymentPath(String deploymentPath) {
		this.deploymentPath = deploymentPath;
	}

	/**
	 * Gets the base video link.
	 *
	 * @return the base video link
	 */
	public String getBaseVideoLink() {
		return baseVideoLink;
	}

	/**
	 * Sets the base video link.
	 *
	 * @param baseVideoLink the new base video link
	 */
	public void setBaseVideoLink(String baseVideoLink) {
		this.baseVideoLink = baseVideoLink;
	}

	/**
	 * Gets the base video link.
	 *
	 * @return the video File Name
	 */
	public String getVideoFileName() {
		
		return videoFileName;
	}
	
	/**
	 * Sets the video file name.
	 *
	 * @param videoFileName the new videoFileName
	 */
	public void setVideoFileName(String videoFileName) {
		this.videoFileName = videoFileName;
	}
	
	/**
	 * Gets the base video link.
	 *
	 * @return the video File Name
	 */
	public String getScreenshotFileName() {
		
		return screenshotFileName;
	}
	
	/**
	 * Sets the video file name.
	 *
	 * @param videoFileName the new videoFileName
	 */
	public void setScreenshotFileName(String screenshotFileName) {
		this.screenshotFileName = screenshotFileName;
	}

	

	
}
