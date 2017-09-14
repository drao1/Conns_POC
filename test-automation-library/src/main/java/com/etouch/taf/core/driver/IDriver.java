/*
 * Builds the IDriver
 */
package com.etouch.taf.core.driver;

import com.etouch.taf.core.TestBed;
import com.etouch.taf.core.exception.DriverException;

/**
 * The Interface IDriver.
 */
public interface IDriver {
	
 /**
 * Builds the driver.
 *
 * @param profile the profile
 * @throws DriverException the driver exception
 */
public void buildDriver(TestBed profile) throws DriverException;
	
	/**
	 * Gets the driver.
	 *
	 * @return the driver
	 */
	public Object getDriver();
}