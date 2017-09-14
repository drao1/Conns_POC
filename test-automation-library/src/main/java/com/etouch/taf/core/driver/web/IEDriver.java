/*
 * 
 */
package com.etouch.taf.core.driver.web;

import java.io.File;

import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

import com.etouch.taf.core.TestBed;
import com.etouch.taf.core.driver.DriverBuilder;
import com.etouch.taf.core.driver.DriverConstantUtil;
import com.etouch.taf.core.exception.DriverException;
import com.etouch.taf.util.ConfigUtil;
import com.etouch.taf.webui.selenium.SeleniumDriver;


/**
 * The Class IEDriver.
 */
public class IEDriver extends DriverBuilder {

	/**
	 * Instantiates a new IE driver.
	 * @param testBed the test bed
	 * @throws DriverException the driver exception
	 */
	public IEDriver(TestBed testBed) throws DriverException {
		super(testBed);
	}

	/**
	 * Creates Driver for IE8,IE9,IE10.
	 * @throws DriverException the driver exception
	 */
	@Override
	public void buildDriver() throws DriverException {

		if (ConfigUtil.isLocalEnv(testBed.getTestBedName())) {
			// If the tool is given as Selenium in DevConfig, then create a selenium IEDriver
			if (ConfigUtil.isSelenium()) {
				File ieDriverFile = getIEDriverFile();
				driver = SeleniumDriver.buildIEDriver(ieDriverFile);
			}
		} else if (ConfigUtil.isRemoteEnv(testBed.getTestBedName())) {
			if (ConfigUtil.isSelenium()) {
			capabilities = DesiredCapabilities.internetExplorer();
			
			capabilities.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS,true);
				// capabilities.setVersion(testBed.getBrowser().getVersion());
				// commented above setVersion code and Added below code to open specified browser version on browserstack
				capabilities.setCapability("version", testBed.getBrowser().getVersion());
				capabilities.setCapability("os", testBed.getPlatform().getName());
				capabilities.setCapability("os_version", testBed.getPlatform().getVersion());
				// updated code ended
			driver= SeleniumDriver.buildRemoteDriver(capabilities);
			}
		} else if (ConfigUtil.isBrowserStackEnv(testBed.getTestBedName())) {
			capabilities = DesiredCapabilities.internetExplorer();
			buildBrowserstackCapabilities();
		}
	}

	/**
	 * This method returns Driver file for InternetExplorer Driver If is in
	 * mentioned in config.yml then set that as system property otherwise, use
	 * the defaults InternetExplorer driver from library based on the given platform
	 * @return the IE driver file
	 * @throws DriverException the driver exception
	 */
	public File getIEDriverFile() throws DriverException {

		File file;
		if (testBed.getBrowser().getDriverLocation() != null) {
			file = new File(testBed.getBrowser().getDriverLocation());
		} else {
			if (ConfigUtil.isWindows(testBed)) {
				file = new File(DriverConstantUtil.IE_DRIVER_FILE);
			} else {
				throw new DriverException(
						" Not found a InternetExplorer- not in Windows OS");
			}
		}
		return file;
	}
	
	/* (non-Javadoc)
	 * @see com.etouch.taf.core.driver.DriverBuilder#getDriver()
	 */
	@Override
	public Object getDriver() throws DriverException {
		return driver;
	}
}