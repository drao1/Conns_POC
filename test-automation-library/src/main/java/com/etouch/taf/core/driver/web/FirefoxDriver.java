/**
 * 
 **/
package com.etouch.taf.core.driver.web;

import java.io.File;

import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.remote.DesiredCapabilities;
import com.etouch.taf.core.TestBed;
import com.etouch.taf.core.driver.DriverConstantUtil;
import com.etouch.taf.core.exception.DriverException;
import com.etouch.taf.util.ConfigUtil;
import com.etouch.taf.webui.selenium.SeleniumDriver;

/**
 * Helps to build Firefox Driver firefox profile is not included yet.
 * @author eTouch
 */
public class FirefoxDriver extends WebDriver {

	/**
	 * Instantiates a new firefox driver.
	 * @param testBed the test bed
	 * @throws DriverException the driver exception
	 */
	public FirefoxDriver(TestBed testBed) throws DriverException {
		super(testBed);
	}

	/**
	 * Builds Firefox Driver according to the given configuration values in config.yml
	 * @throws DriverException the driver exception
	 */
	@Override
	public void buildDriver() throws DriverException {

		if (ConfigUtil.isLocalEnv(testBed.getTestBedName())) {
			// if tool is given devConfig.yml as Selenium, then create a
			// selenium FireFox driver
			if (ConfigUtil.isSelenium()) {
				File firefoxDriverFile=getFireFoxDriverFile();
				System.out.println("Without location");
				//	driver =SeleniumDriver.buildFireFoxDriver(firefoxDriverFile);
				
			/*	//disabling geo location
				FirefoxProfile geoDisabled = new FirefoxProfile();
				geoDisabled.setPreference("geo.enabled", false);
				geoDisabled.setPreference("geo.provider.use_corelocation", false);
				geoDisabled.setPreference("geo.prompt.testing", false);
				geoDisabled.setPreference("geo.prompt.testing.allow", false);
				DesiredCapabilities capabilities = DesiredCapabilities.firefox();
				capabilities.setCapability(org.openqa.selenium.firefox.FirefoxDriver.PROFILE, geoDisabled);*/
				driver = SeleniumDriver.buildFireFoxDriver();
			}
		} else if (ConfigUtil.isRemoteEnv(testBed.getTestBedName())) {

			if (ConfigUtil.isSelenium()) {

				FirefoxProfile ffProfile = new FirefoxProfile();
				ffProfile.setEnableNativeEvents(false);
				/*ffProfile.setPreference("geo.enabled", false);
				ffProfile.setPreference("geo.provider.use_corelocation", false);
				ffProfile.setPreference("geo.prompt.testing", false);
				ffProfile.setPreference("geo.prompt.testing.allow", false);*/
				capabilities = DesiredCapabilities.firefox();
				capabilities.setCapability(
						org.openqa.selenium.firefox.FirefoxDriver.PROFILE,
						testBed);
				
				// Added below code to open specified browser version on browserstack	
				capabilities.setCapability("version", testBed.getBrowser().getVersion());
				capabilities.setCapability("os", testBed.getPlatform().getName());
				capabilities.setCapability("os_version", testBed.getPlatform().getVersion());
//				updated code ended
				
				driver=SeleniumDriver.buildRemoteDriver(capabilities);
			}
		} else if (ConfigUtil.isBrowserStackEnv(testBed.getTestBedName())) {
			capabilities = DesiredCapabilities.firefox();
			buildBrowserstackCapabilities();
		}
	}

	/**
	 * This method will give an instance of FirefoxProfile Uncomment the code
	 * according to which firefox profile you want to create.
	 * @return the firefox profile
	 */
	private FirefoxProfile getFirefoxProfile() {				
		return new FirefoxProfile();
	}

	/**
	 * (non-Javadoc)
	 * @see com.etouch.taf.core.driver.DriverBuilder#getDriver()
	 **/
	@Override
	public Object getDriver() throws DriverException {
		return driver;
	}
	
	public File getFireFoxDriverFile(){
		System.out.println("Getting File Location");
		System.out.println("Driver location :"+testBed.getBrowser().getDriverLocation());
		File file;
		if(testBed.getBrowser().getDriverLocation()!=null){			
			file =new File(testBed.getBrowser().getDriverLocation());
		}else{
			if(ConfigUtil.isWindows(testBed))
			{
				System.out.println("In here");
				file = new File(DriverConstantUtil.FIREFOX_WINDOWS_DRIVER_FILE);
				System.out.println("Got Driver File");
			}
			else
			{
				file = new File(DriverConstantUtil.FIREFOX_MAC_DRIVER_FILE);
			}
		}
		return file;
	}
}