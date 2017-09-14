/*
 * 
 */
package com.etouch.taf.core.resources;

import org.apache.commons.logging.Log;

import com.etouch.taf.util.LogUtil;


/**
 * The Enum TestToolType.
 */
public enum TestToolType {
	
	/** The Appium. */
	APPIUM("Appium"), 
	
	/** The Selenium. */
	SELENIUM("Selenium"), 
	
	/** The Experi test. */
	EXPERITEST("ExperiTest");
	
	/** The log. */
	static Log log = LogUtil.getLog(TestToolType.class);
	
	/** The test tool type. */
	private String testToolType;

	/**
	 * Instantiates a new test tool type.
	 *
	 * @param testToolType the test tool type
	 */
	private TestToolType(String testToolType) {
		this.testToolType = testToolType;
	}

	/**
	 * Gets the test tool type.
	 *
	 * @return the test tool type
	 */
	public String getTestToolType() {
		return testToolType;
	}

	

}
