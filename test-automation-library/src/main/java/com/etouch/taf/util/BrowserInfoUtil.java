/*
 * 
 */
package com.etouch.taf.util;

import com.etouch.taf.core.TestBed;
import com.etouch.taf.core.TestBedManager;
import com.etouch.taf.core.resources.TestBedType;


/**
 * This enum class checks on browser type.
 * 
 * @author eTouch Systems Corporation
 * @version 1.0
 *
 */
public class BrowserInfoUtil {
	
	public BrowserInfoUtil(String testBedName){
		this.testBedName=testBedName;
	}
	
	/** The test bed name. */
	private String testBedName;
	
	/**
	 * Gets the browser name.
	 *
	 * @return the browser name
	 */
	private String getBrowserName() {
		TestBed testBed=TestBedManager.INSTANCE.getCurrentTestBeds().get(this.testBedName);
		return testBed.getBrowser().getName();
	}

	/**
	 * Sets the browser name.
	 *
	 * @param browserName the new browser name
	 */
	public void setBrowserName(String browserName) {
		this.testBedName = browserName;
	}

	/**
	 * check the current test is running on IE or non-IE browsers.
	 *
	 * @return return true if it's a IE browser test, else it will return false
	 */
	public boolean isIE() {
		return getBrowserName().toUpperCase().contains("IE");
	}

	/**
	 * check the current test is running on IE or non-IE browsers.
	 *
	 * @return return true if it's a IE browser test, else it will return false
	 */
	public boolean isIE8() {
		return TestBedType.INTERNETEXPLORER.getTestBedName().equalsIgnoreCase(getBrowserName());
	}

	

	/**
	 * check the current test is running on IE or non-IE browsers.
	 *
	 * @return return true if it's a IE browser test, else it will return false
	 */
	public boolean isFF() {
		return TestBedType.FIREFOX.getTestBedName().equalsIgnoreCase(getBrowserName());
	}

	/**
	 * check the current test is running on IE or non-IE browsers.
	 *
	 * @return return true if it's a IE browser test, else it will return false
	 */
	public boolean isChrome() {
		return TestBedType.CHROME.getTestBedName().equalsIgnoreCase(getBrowserName());
	}

	/**
	 * check the current test is running on Safari or non-Safari browsers.
	 *
	 * @return return true if it's a Safari browser test, else it will return false
	 */
	public boolean isSafari() {
		return TestBedType.SAFARI.getTestBedName().equalsIgnoreCase(getBrowserName());
	}
	
	/**
	 * Checks if is android.
	 *
	 * @return true, if is android
	 */
	public boolean isAndroid() {
		return TestBedType.ANDROIDWEBEM.getTestBedName().equalsIgnoreCase(getBrowserName());
	}
	
	/**
	 * Checks if is ios.
	 *
	 * @return true, if is ios
	 */
	public boolean isIOS() {
		return TestBedType.IOS.getTestBedName().equalsIgnoreCase(getBrowserName());
	}
	
}

