package com.etouch.taf.webui.selenium;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.commons.logging.Log;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.safari.SafariDriver;

import com.etouch.taf.core.TestBed;
import com.etouch.taf.core.config.TestBedManagerConfiguration;
import com.etouch.taf.core.exception.DriverException;
import com.etouch.taf.util.LogUtil;
import com.etouch.taf.util.TestBedUtil;

/**
 * The Class SeleniumDriver.
 */
public class SeleniumDriver {

	/** The log. */
	static Log log = LogUtil.getLog(SeleniumDriver.class);

	/**
	 * Builds the fire fox driver.
	 * 
	 * @return the firefox driver
	 * @throws DriverException
	 *             the driver exception
	 */
	public static WebDriver buildFireFoxDriver() throws DriverException {
		System.out.println("in here nnn33");
		 //System.setProperty("webdriver.gecko.driver","D:\\SeleniumDrivers\\geckodriver.exe");
		 WebDriver driver = new FirefoxDriver();
		 //driver.get("http://www.seleniumeasy.com/selenium-tutorials/launching-firefox-browser-with-geckodriver-selenium-3");
		/*DesiredCapabilities capabilities=DesiredCapabilities.firefox();
		capabilities.setCapability("marionette", true);*/
		System.out.println("New 212121 marionette");
		System.out.println("with old method");
		return driver;
	}

	/**
	 * Builds the fire fox driver.
	 * 
	 * @param ffProfile
	 *            the ff profile
	 * @return the firefox driver
	 */
	public static FirefoxDriver buildFireFoxDriver(FirefoxProfile ffProfile) {
		return new FirefoxDriver(ffProfile);
	}
	
	public static WebDriver buildFireFoxDriver(File file) throws DriverException {

		if (file != null) {
			System.out.println("setting properties");
			//System.setProperty("webdriver.gecko.driver", file.getPath());
			System.setProperty("webdriver.gecko.driver","G:\\Selenium\\Firefox driver\\geckodriver.exe");
			System.out.println("new path set");

		}
		WebDriver driver = new FirefoxDriver();
		return driver;
		//return new FirefoxDriver();
	}
	/**
	 * Builds the safari driver.
	 * 
	 * @return the safari driver
	 * @throws DriverException
	 *             the driver exception
	 */
	/*public static RemoteWebDriver buildSafariDriver() throws DriverException {

		DesiredCapabilities dc = DesiredCapabilities.safari();
		RemoteWebDriver driver = null;
		try {
			driver = new RemoteWebDriver(new URL("http://localhost:4444/wd/hub"), dc);
		} catch (MalformedURLException e) {

			log.debug("MalformedURLException", e);
		}
		return driver;
	}*/
	
	//Updated below method to create local safari driver
	public static WebDriver buildSafariDriver() throws DriverException {
		WebDriver driver = null;
		try {
			driver = new SafariDriver();
		} catch (Exception e) {

			log.debug("Exception", e);
		}
		return driver;
	}

	/**
	 * Builds the chrome driver.
	 * 
	 * @param file
	 *            the file
	 * @return the chrome driver
	 * @throws DriverException
	 *             the driver exception
	 */
	public static ChromeDriver buildChromeDriver(File file) throws DriverException {

		if (file != null) {

			System.setProperty("webdriver.chrome.driver", file.getPath());

		}

		return new ChromeDriver();
	}

	/**
	 * Builds the ie driver.
	 * 
	 * @param file
	 *            the file
	 * @return the internet explorer driver
	 * @throws DriverException
	 *             the driver exception
	 */
	public static InternetExplorerDriver buildIEDriver(File file) throws DriverException {
		if (file != null) {
			System.out.println("IE Driver File path");
			System.setProperty("webdriver.ie.driver", file.getAbsolutePath());
		}

		DesiredCapabilities cap = new DesiredCapabilities();
		cap.setCapability(CapabilityType.UNEXPECTED_ALERT_BEHAVIOUR,
				org.openqa.selenium.UnexpectedAlertBehaviour.ACCEPT);
		cap.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS, true);

		return new InternetExplorerDriver(cap);

	}

	/**
	 * Builds the url.
	 * 
	 * @param capabilities
	 *            the capabilities
	 * @return the string
	 */
	protected static String buildURL(DesiredCapabilities capabilities) {
		String hubLocation = null;
		TestBedManagerConfiguration tbMgrConfig = TestBedManagerConfiguration.getInstance();
		String hub = tbMgrConfig.getWebConfig().getHub();
		String port = tbMgrConfig.getWebConfig().getPort();

		TestBed testBed = TestBedUtil.currentTestBedInfo();

		// defalut hub to localhost
		if (hub == null || hub.isEmpty()) {
			hub = "localhost";
		}

		// default port to 4444
		if (port == null || port.isEmpty()) {
			port = "4444";
		}
		if (hub.contains("browserstack") || hub.contains("saucelabs")) {
			hubLocation = hub;
		} else {
			hubLocation = "http://" + hub + ":" + port + "/wd/hub";
		}

		// add proxy setting to the webdriver
		String proxyString = tbMgrConfig.getWebConfig().getProxy();
		String noProxyString = tbMgrConfig.getWebConfig().getNoProxy();
		if (proxyString != null && !proxyString.isEmpty()) {
			Proxy proxysetting = new Proxy();
			proxysetting.setHttpProxy(proxyString).setFtpProxy(proxyString).setSslProxy(proxyString);
			if (noProxyString != null && !proxyString.isEmpty()) {
				proxysetting.setNoProxy(noProxyString);
			}

			capabilities.setCapability(CapabilityType.PROXY, proxysetting);
		}

		return hubLocation;
	}

	/**
	 * Builds the remote driver.
	 * 
	 * @param capabilities
	 *            the capabilities
	 * @return the remote web driver
	 * @throws DriverException
	 *             the driver exception
	 */
	public static RemoteWebDriver buildRemoteDriver(DesiredCapabilities capabilities) throws DriverException {
		RemoteWebDriver driver = null;
		try {
			driver = new RemoteWebDriver(new URL(buildURL(capabilities)), capabilities);
		} catch (MalformedURLException e) {
			log.error("failed to create driver : " + e.getMessage());
			throw new DriverException("failed to create driver : " + e.getMessage());
		}
		return driver;
	}
}
