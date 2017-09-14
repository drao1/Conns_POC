package com.etouch.taf.core.config;

/**
 * @author etouch
 *
 */
public class KDConfig {
	
	private boolean execute;
      
	private String filePath;
	
	private String parallelMode;
	
	
	/**
	 * @return the execute
	 */
	public boolean isExecute() {
		return execute;
	}

	/**
	 * @param execute the execute to set
	 */
	public void setExecute(boolean execute) {
		this.execute = execute;
	}

	/**
	 * @return the filePath
	 */
	public String getFilePath() {
		return filePath;
	}

	/**
	 * @param filePath the filePath to set
	 */
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	/**
	 * @return the parallelMode
	 */
	public String getParallelMode() {
		return parallelMode;
	}

	/**
	 * @param parallelMode the parallelMode to set
	 */
	public void setParallelMode(String parallelMode) {
		this.parallelMode = parallelMode;
	}

}  