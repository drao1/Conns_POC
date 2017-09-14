package com.etouch.taf.core.listener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import com.etouch.taf.core.TestBed;
import com.etouch.taf.core.TestBedManager;
import com.etouch.taf.core.config.TestBedManagerConfiguration;
import com.etouch.taf.util.AppiumLauncher;
import com.etouch.taf.util.CommonUtil;
import com.etouch.taf.util.ConfigUtil;
import com.etouch.taf.util.LogUtil;
import com.etouch.taf.util.SoftAssertor;
import com.etouch.taf.util.TestBedUtil;
import com.etouch.taf.webui.selenium.WebPage;

public class TestListener implements ITestListener {

	public static HashMap<String, Thread> myThreads = new HashMap<String, Thread>();

	/** The log. */
	static Log log = LogUtil.getLog(TestListener.class);

	/** The page ur ls. */
	static public Properties pageURLs = null;

	/** The rally property file. */
	static public Properties rallyPropertyFile = null;

	/** The is initialize. */
	static boolean isInitialize = false;

	@Override
	public void onTestStart(ITestResult result) {
		log.debug(" resutls is here" + result);
	}

	@Override
	public void onTestSuccess(ITestResult result) {
		log.debug(" resutls is here" + result);

	}

	@Override
	public void onTestFailure(ITestResult result) {

	}

	@Override
	public void onTestSkipped(ITestResult result) {

	}

	@Override
	public void onTestFailedButWithinSuccessPercentage(ITestResult result) {

	}

	@Override
	public synchronized void onStart(ITestContext context) {
		String testBedName = context.getCurrentXmlTest().getAllParameters().get("testBedName");
		String testSuiteName = context.getCurrentXmlTest().getAllParameters().get("testSuiteName");
		// synchronized (this) {

		try {
			createWebPageObject(context, testBedName, testSuiteName);

			TestBedManager.INSTANCE.createDefectManager();
			TestBedUtil.loadTestBedDetails(testBedName);
			// Thread.sleep(5000);
		} catch (Exception e) {
			log.debug(e);
		}

		CommonUtil.sop(" Thread started running and came out of sync block");
		// Moved Driver created to CurrentTestBed Class

	}

	/**
	 * This method will create a webPage object But it will not associate with
	 * any driver or nor load a URL
	 * 
	 * @param context
	 * @param testBedName
	 * @param testSuiteName
	 */
	private void createWebPageObject(ITestContext context, String testBedName, String testSuiteName) {
		try {

			String url = TestBedManagerConfiguration.INSTANCE.getWebConfig().getURL();
			WebPage webPage = new WebPage();

			log.debug("[" + testBedName + "] [" + testSuiteName + "] Loading web page...");

			context.setAttribute("WebPage", webPage);

			// / This Webpage has webpage object. But not a driver

		} catch (Exception exp) {
			log.error(exp + " :: " + exp.getMessage());
			log.debug(exp);
			SoftAssertor.addVerificationFailure(exp.getMessage());
		}
	}

	@Override
	public void onFinish(ITestContext context) {
		String testBedName = context.getCurrentXmlTest().getAllParameters().get("testBedName");

		WebPage webPage = (WebPage) context.getAttribute("WebPage");

		WebDriver driver = webPage.getDriver();
		log.debug("Driver ==>" + driver.toString());

		 

		TestBed testBed = TestBedManager.INSTANCE.getCurrentTestBeds().get(testBedName);
		if (ConfigUtil.isMobileTestTypeEnabled())
			AppiumLauncher.closeAppiumSession(testBed);
		
		closeDriver(testBedName, driver);
		removeDrivers(testBedName,driver);

		log.debug("<<<<<<<<<<<<<<<<<<<[Drivers Closed :: " + testBedName + "] >>>>>>>>>>>>>>>>>");

	}

	/**
	 * Close and quit drivers associated with currentTestBed
	 * @param testBedName
	 * @param currentDriver
	 */
	private void closeDriver(String testBedName, WebDriver currentDriver) {
		ArrayList<Object> currentDrivers=TestBedManager.INSTANCE.getCurrentDrivers().get(testBedName);
		if(currentDrivers!=null){
		for(Object driver:currentDrivers){
			WebDriver driverToClose=(WebDriver)driver;
			if(!driverToClose.equals(currentDriver)){
				driverToClose.close();
				driverToClose.quit();
			}
		}
		}
		
	}
	
	/**
	 * Removes drivers associated with currentTestBed from TestBedManager list.
	 * @param testBedName
	 * @param currentDriver
	 */
	private void removeDrivers(String testBedName, WebDriver currentDriver){

		TestBedManager.INSTANCE.getCurrentDrivers().remove(testBedName);
	}
}



