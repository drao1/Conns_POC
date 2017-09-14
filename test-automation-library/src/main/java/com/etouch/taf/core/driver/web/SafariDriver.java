package com.etouch.taf.core.driver.web;

import java.io.IOException;

import org.apache.commons.logging.Log;
import org.openqa.selenium.Platform;
import org.openqa.selenium.remote.DesiredCapabilities;

import com.etouch.taf.core.TestBed;
import com.etouch.taf.core.config.TestBedManagerConfiguration;
import com.etouch.taf.core.driver.DriverBuilder;
import com.etouch.taf.core.exception.DriverException;
import com.etouch.taf.util.ConfigUtil;
import com.etouch.taf.util.LogUtil;
import com.etouch.taf.webui.selenium.SeleniumDriver;

/**
 * The Class SafariDriver.
 */
public class SafariDriver extends DriverBuilder {

	private static Log log = LogUtil.getLog(SafariDriver.class);

	/**
	 * Instantiates a new safari driver.
	 * 
	 * @param testBed
	 *            the test bed
	 * @throws DriverException
	 *             the driver exception
	 */
	public SafariDriver(TestBed testBed) throws DriverException {
		super(testBed);
	}

	/**
	 * Creates Driver for Safari.
	 * 
	 * @throws DriverException
	 *             the driver exception
	 */
	@Override
	public void buildDriver() throws DriverException {
		if (ConfigUtil.isLocalEnv(testBed.getTestBedName())) {

			// if the tool is mentioned as selenium in devconfig.yml then create
			// a Selenium safari Driver
		
			if (ConfigUtil.isSelenium()) {
			
				//Commented below code, as we will be creating a local selenium driver
			/*	try {
					Runtime.getRuntime().exec("java -jar " + TestBedManagerConfiguration.INSTANCE.getWebConfig().getSeleniumServerPath());
				} catch (IOException e) {

					log.debug("IOException", e);
				}
				*/
				driver = SeleniumDriver.buildSafariDriver();
			}
		} else if (ConfigUtil.isRemoteEnv(testBed.getTestBedName())) {

			if (ConfigUtil.isSelenium()) {
				capabilities = DesiredCapabilities.safari();
				capabilities.setBrowserName("safari");
				if (ConfigUtil.isWindows(testBed)) {
					capabilities.setPlatform(Platform.WINDOWS);
					
					// Added below code to open specified browser version on browserstack	
					capabilities.setCapability("version", testBed.getBrowser().getVersion());
					capabilities.setCapability("os", testBed.getPlatform().getName());
					capabilities.setCapability("os_version", testBed.getPlatform().getVersion());
//					updated code ended
					
				} else if (ConfigUtil.isMac(testBed)||testBed.getPlatform().getName().equalsIgnoreCase("OS X")) {
					capabilities.setPlatform(Platform.MAC);
					// Added below code to open specified browser version on browserstack	
					capabilities.setCapability("version", testBed.getBrowser().getVersion());
					capabilities.setCapability("os", testBed.getPlatform().getName());
					capabilities.setCapability("os_version", testBed.getPlatform().getVersion());
//					updated code ended
				}
				driver = SeleniumDriver.buildRemoteDriver(capabilities);
			}

		} else if (ConfigUtil.isBrowserStackEnv(testBed.getTestBedName())) {
			capabilities = DesiredCapabilities.safari();
			buildBrowserstackCapabilities();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.etouch.taf.core.driver.DriverBuilder#getDriver()
	 */
	@Override
	public Object getDriver() throws DriverException {
		return driver;
	}

}
