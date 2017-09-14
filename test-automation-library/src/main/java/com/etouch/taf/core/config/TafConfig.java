package com.etouch.taf.core.config;

import java.util.List;


/**
 * The Class TestAutomationFramework Config.
 */
public abstract class TafConfig {
	

	/** The hub. */
	private String hub;
	
	/** The port. */
	private String port;
	
	/** The tool. */
	private String tool;
	
	private String seleniumServerPath;	

	/** The current test beds. */
	private String[] currentTestBeds;
	
	/** The test env. */
	private String testEnv;
	
	private List<TestngClass> testngClass;
	
	/** The test beds. */
	private List<TestBedConfig> testBeds;
	
	/**
	 * Gets the hub.
	 *
	 * @return the hub
	 */
	public String getHub() {
		return hub;
	}
	
	/**
	 * Sets the hub.
	 *
	 * @param hub the new hub
	 */
	public void setHub(String hub) {
		this.hub = hub;
	}
	
	/**
	 * Gets the port.
	 *
	 * @return the port
	 */
	public String getPort() {
		return port;
	}
	
	/**
	 * Sets the port.
	 *
	 * @param port the new port
	 */
	public void setPort(String port) {
		this.port = port;
	}
	
	/**
	 * Gets the tool.
	 *
	 * @return the tool
	 */
	public String getTool() {
		return tool;
	}
	
	/**
	 * Sets the tool.
	 *
	 * @param tool the new tool
	 */
	public void setTool(String tool) {
		this.tool = tool;
	}
	
	/**
	 * Gets the current test beds.
	 *
	 * @return the current test beds
	 */
	public String[] getCurrentTestBeds() {
		return currentTestBeds;
	}
	
	/**
	 * Sets the current test beds.
	 *
	 * @param currentTestBeds the new current test beds
	 */
	public void setCurrentTestBeds(String[] currentTestBeds) {
		this.currentTestBeds = currentTestBeds;
	}
	
	/**
	 * Gets the test beds.
	 *
	 * @return the test beds
	 */
	public List<TestBedConfig> getTestBeds() {
		return testBeds;
	}
	
	/**
	 * Sets the test beds.
	 *
	 * @param testBeds the new test beds
	 */
	public void setTestBeds(List<TestBedConfig> testBeds) {
		this.testBeds = testBeds;
	}
	
	/**
	 * Gets the test env.
	 *
	 * @return the test env
	 */
	public String getTestEnv() {
		return testEnv;
	}
	
	/**
	 * Sets the test env.
	 *
	 * @param testEnv the new test env
	 */
	public void setTestEnv(String testEnv) {
		this.testEnv = testEnv;
	}

	public List<TestngClass> getTestngClass() {
		return testngClass;
	}

	public void setTestngClass(List<TestngClass> testngClass) {
		this.testngClass = testngClass;
	}
	
	
	public String getSeleniumServerPath() {
		return seleniumServerPath;
	}

	public void setSeleniumServerPath(String seleniumServerPath) {
		this.seleniumServerPath = seleniumServerPath;
	}

		
}
