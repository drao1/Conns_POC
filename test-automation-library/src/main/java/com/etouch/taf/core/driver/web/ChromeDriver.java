/*
 * 
 */
package com.etouch.taf.core.driver.web;

import java.io.File;
import org.apache.commons.logging.Log;
import org.openqa.selenium.remote.DesiredCapabilities;
import com.etouch.taf.core.TestBed;
import com.etouch.taf.core.driver.DriverBuilder;
import com.etouch.taf.core.driver.DriverConstantUtil;
import com.etouch.taf.core.driver.mobile.IOSDriver;
import com.etouch.taf.core.exception.DriverException;
import com.etouch.taf.util.ConfigUtil;
import com.etouch.taf.util.LogUtil;
import com.etouch.taf.webui.selenium.SeleniumDriver;

/**
 * The Class ChromeDriver.
 */
public class ChromeDriver extends DriverBuilder{

	/** The log. */
	private static Log log = LogUtil.getLog(IOSDriver.class);

	/**
	 * Instantiates a new chrome driver.
	 * @param testBed the test bed
	 * @throws DriverException the driver exception
	 */
	public ChromeDriver(TestBed testBed) throws DriverException {
		super(testBed);
	}
	
	/**
	 * Creates driver for Chrome.
	 * @throws DriverException the driver exception
	 */
	@Override
	public void buildDriver() throws DriverException
	{
		if(ConfigUtil.isLocalEnv(testBed.getTestBedName()))
		{
			// if it is a Selenium tool, then create selenium ChromeDriver
			if(ConfigUtil.isSelenium()){
				File chromeDriverFile=getChromeDriverFile();
				log.debug("Found Driver file");
				driver =SeleniumDriver.buildChromeDriver(chromeDriverFile);
			}
		} else if(ConfigUtil.isRemoteEnv(testBed.getTestBedName()))
		{
			if(ConfigUtil.isSelenium()){
				capabilities = DesiredCapabilities.chrome();
				
			// Added below code to open specified browser version on browserstack	
				capabilities.setCapability("browser_version", testBed.getBrowser().getVersion());
				capabilities.setCapability("Platform_Name", testBed.getPlatform().getName());
				capabilities.setCapability("Platform_version", testBed.getPlatform().getVersion());
			//	updated code ended
				driver = SeleniumDriver.buildRemoteDriver(capabilities);
			}
		}
		else if(ConfigUtil.isBrowserStackEnv(testBed.getTestBedName()))
		{
			capabilities = DesiredCapabilities.chrome();
			buildBrowserstackCapabilities();
		}
	}
	
	/**
	 * This method returns Driver file for Chrome Driver
	 * If is in mentioned in config.yml then set that as system property
	 * otherwise, use the defaults Chromedriver from library based on the given platform
	 * @return the chrome driver file
	 */
	public File getChromeDriverFile(){
		
		File file;
		if(testBed.getBrowser().getDriverLocation()!=null){			
			file =new File(testBed.getBrowser().getDriverLocation());
		}else{
			if(ConfigUtil.isWindows(testBed))
			{
				file = new File(DriverConstantUtil.CHROME_WINDOWS_DRIVER_FILE);
			}
			else
			{
				file = new File(DriverConstantUtil.CHROME_MAC_DRIVER_FILE);
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