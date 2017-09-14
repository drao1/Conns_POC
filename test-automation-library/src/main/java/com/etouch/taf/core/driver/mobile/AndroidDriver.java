/* Creates the Android driver */
package com.etouch.taf.core.driver.mobile;

import org.apache.commons.logging.Log;
import org.openqa.selenium.remote.DesiredCapabilities;
import com.etouch.taf.core.TestBed;
import com.etouch.taf.core.driver.DriverBuilder;
import com.etouch.taf.core.exception.DriverException;
import com.etouch.taf.mobile.appium.AppiumDriver;
import com.etouch.taf.mobile.experitest.cloud.ExperitestCloudDriver;
import com.etouch.taf.util.AppiumLauncher;
import com.etouch.taf.util.ConfigUtil;
import com.etouch.taf.util.LogUtil;

/**
 * The Class AndroidDriver.
 */
public class AndroidDriver extends DriverBuilder {

	/** The log. */
	static Log log = LogUtil.getLog(AndroidDriver.class);

	/**
	 * Instantiates a new android driver.
	 * @param testBed the test bed
	 * @throws DriverException the driver exception
	 */
	public AndroidDriver(TestBed testBed) throws DriverException {
		super(testBed);
	}

	/**
	 * Creates driver for Android.
	 * @throws DriverException the driver exception
	 */
	@Override
	public void buildDriver() throws DriverException {
		try {
			if (ConfigUtil.isLocalEnv(testBed.getTestBedName())) {

				if (ConfigUtil.isAppium()) {
					AppiumLauncher.launchAppiumSession(testBed);
					Thread.sleep(20000);
					driver = AppiumDriver.buildAndroidDriver(testBed);
				} else if (ConfigUtil.isExperiTest()) {
					driver = ExperitestCloudDriver.buildDriver(testBed);
				}
			} else if (ConfigUtil.isRemoteEnv(testBed.getTestBedName())) {
				driver = AppiumDriver.buildAndroidDriver(testBed);
			} else if (ConfigUtil.isBrowserStackEnv(testBed.getTestBedName())) {
				capabilities = DesiredCapabilities.android();
				capabilities.setCapability("device", testBed.getDevice().getName());
				buildBrowserstackCapabilities();
			}
		} catch (InterruptedException e) {
			log.error("failed to create driver for android : " + e.getMessage());
			throw new DriverException("Could not create driver :: " + e.getMessage());
		}
	}

	/*
	 * (non-Javadoc)
	 * @see com.etouch.taf.core.driver.DriverBuilder#getDriver()
	 */
	@Override
	public Object getDriver() throws DriverException {
		return driver;
	}
}