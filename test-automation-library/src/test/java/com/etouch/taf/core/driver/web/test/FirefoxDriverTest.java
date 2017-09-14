package com.etouch.taf.core.driver.web.test;

import java.util.Properties;

import junit.framework.TestCase;

import org.apache.commons.logging.Log;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.WebDriver;

import com.etouch.taf.core.TestBed;
import com.etouch.taf.core.config.BrowserConfig;
import com.etouch.taf.core.driver.web.FirefoxDriver;
import com.etouch.taf.core.exception.DriverException;
import com.etouch.taf.core.test.util.TafTestUtil;
import com.etouch.taf.util.CommonUtil;
import com.etouch.taf.util.LogUtil;
import com.etouch.taf.util.SoftAssertor;

public class FirefoxDriverTest {

	static Log log = LogUtil.getLog(FirefoxDriverTest.class);

	TestBed testbed = null;

	FirefoxDriver driverObj = null;

	@Before
	public void setUp() {
		TafTestUtil.initialize();

		testbed = new TestBed();
		BrowserConfig bcfg = new BrowserConfig();
		bcfg.setName("Firefox");
		bcfg.setVersion("25");
		testbed.setBrowser(bcfg);
		testbed.setTestBedName("Firefox");
	}

	@Test
	public void test() throws DriverException {
		driverObj = new FirefoxDriver(testbed);
		driverObj.buildDriver();
		SoftAssertor.assertNotNull(driverObj.getDriver());
	}

	@After
	public void tearDown() {
		WebDriver driver = null;
		CommonUtil.sop("At tear down ");
		try {
			driver = (WebDriver) driverObj.getDriver();
			driver.wait(150000);
		} catch (Exception e) {
			// SoftAssertor.addVerificationFailure(e.getMessage());
		}
		driver.close();
		driver.quit();
		// mainPage.stopRecording();
	}
}
