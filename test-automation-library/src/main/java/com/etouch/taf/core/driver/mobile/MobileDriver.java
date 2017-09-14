/*
 * Creates the mobile driver
 */
package com.etouch.taf.core.driver.mobile;

import com.etouch.taf.core.TestBed;
import com.etouch.taf.core.driver.DriverBuilder;
import com.etouch.taf.core.exception.DriverException;

/**
 * The Class MobileDriver.
 */
public  class MobileDriver extends DriverBuilder {
	
	/**
	 * Instantiates a new mobile driver.
	 * @param testBed the test bed
	 * @throws DriverException the driver exception
	 */
	public MobileDriver(TestBed testBed) throws DriverException {
		super(testBed);
	}

	/* (non-Javadoc)
	 * @see com.etouch.taf.core.driver.DriverBuilder#buildDriver()
	 */
	@Override
	public void buildDriver() throws DriverException {
		// No changes are expected in the default behavior
	}

	
	/* (non-Javadoc)
	 * @see com.etouch.taf.core.driver.DriverBuilder#getDriver()
	 */
	@Override
	public Object getDriver() throws DriverException {
		return driver;
	}
}