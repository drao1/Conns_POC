package com.etouch.taf.core;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.logging.Log;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.testng.TestNG;

import com.etouch.taf.core.config.CurrentDrivers;
import com.etouch.taf.core.config.CurrentTestBeds;
import com.etouch.taf.core.config.DefectConfig;
import com.etouch.taf.core.config.KDConfig;
import com.etouch.taf.core.config.TFSConfig;
import com.etouch.taf.core.config.TestBedManagerConfiguration;
import com.etouch.taf.core.config.TestngConfig;
import com.etouch.taf.core.config.WebConfig;
import com.etouch.taf.core.exception.DefectException;
import com.etouch.taf.kd.exception.DuplicateDataKeyException;
import com.etouch.taf.kd.exception.InvalidActionException;
import com.etouch.taf.kd.exception.InvalidValueException;
import com.etouch.taf.tools.defect.DefectManagerFactory;
import com.etouch.taf.tools.defect.IDefectManager;
import com.etouch.taf.util.ConfigUtil;
import com.etouch.taf.util.LogUtil;

/**
 * Test Bed Manager loads the profile for the test environment. This class
 * initializes {@link TestBed}
 * 
 * @author eTouch Systems Corporation
 * @version 1.0
 * 
 */
public enum TestBedManager {

	/** The instance to create a Singleton object of TestbedManager. */
	INSTANCE;

	/** The log. */
	static Log log = LogUtil.getLog(TestBedManager.class);
	
	/**  This object helps to hold information about the testbed which is executing right now. */
	
	CurrentTestBeds currentTestBeds=new CurrentTestBeds();
	
	/** Maintain list of drivers(browsers) opened and not closed. 
	 * Every testBed has its own list of drivers
	 */
	CurrentDrivers currentDrivers=new CurrentDrivers();

	
	/** The defect. */
	private IDefectManager defect = null;

	/**
	 * Reads configuration file and create profile and sets in test bed.
	 * 
	 * @param ipStream
	 *            the new config
	 * @param currentTestBeds
	 *            the current test beds
	 * @throws Exception
	 *             the exception
	 */

	public void setConfig(InputStream ipStream, String[] currentTestBeds) throws Exception {

		TestBedManagerConfiguration.setIpStream(ipStream);
		TestBedManagerConfiguration.getInstance();

		if (validateCurrentTestBed(currentTestBeds)) {
			if (ConfigUtil.isWebTestTypeEnabled()) {
				if (currentTestBeds != null && currentTestBeds.length > 0) {
					TestBedManagerConfiguration.getInstance().getWebConfig().setCurrentTestBeds(currentTestBeds);
				} else {
					throw new Exception("Please mention paramter in Maven command for TestBed Name");
				}
			}

			if (ConfigUtil.isMobileTestTypeEnabled()) {
				if (currentTestBeds != null && currentTestBeds.length > 0) {
					TestBedManagerConfiguration.getInstance().getMobileConfig().setCurrentTestBeds(currentTestBeds);
				} else {
					throw new Exception("Please mention paramter in Maven command for TestBed Name");
				}
			}
		}

	}

	private boolean validateCurrentTestBed(String[] currentTestBeds) {
		boolean result = false;
		if (currentTestBeds.length > 0) {
			if (currentTestBeds[0].length() > 0) {
				if (!currentTestBeds[0].toString().isEmpty()) {
					result = true;
				}
			}
		}
		return result;
	}

	/**
	 * Creates the defect manager.
	 * 
	 * @throws DefectException
	 *             the defect exception
	 */
	public void createDefectManager() throws DefectException {

		if (ConfigUtil.isDefectToolSupported()) {

			this.defect = DefectManagerFactory.manageDefect(TestBedManagerConfiguration.getInstance().getDefectConfig().getDefectTool());

		}
	}

	/**
	 * Execute test ng.
	 */
	public void executeTestNG() {

		TestNG testNG = TestSuiteManager.INSTANCE.buildTestSuites();

		log.info("Test suites begin to run");
		testNG.run();

	}


	/**
	 * Returns profile.
	 * 
	 * @return Profile configuration instance.
	 */
	public TestBedManagerConfiguration getProfile() {
		return TestBedManagerConfiguration.getInstance();
	}

	/**
	 * Returns base URL.
	 * 
	 * @return profile base URL
	 */
	public String loadBaseURL() {
		WebConfig webConfig = TestBedManagerConfiguration.getInstance().getWebConfig();
		return webConfig.getURL();
	}

	/**
	 * Returns defect instance.
	 * 
	 * @return DefectManager
	 */
	public IDefectManager getDefect() {
		return this.defect;
	}

	/**
	 * Gets the defect config.
	 * 
	 * @return the defect config
	 */
	public DefectConfig getDefectConfig() {
		return TestBedManagerConfiguration.INSTANCE.getDefectConfig();
	}

	/**
	 * Gets the testng config.
	 * 
	 * @return the testng config
	 */
	public TestngConfig getTestngConfig() {
		return TestBedManagerConfiguration.INSTANCE.getTestngConfig();
	}

	/**
	 * Gets the TFS config.
	 * 
	 * @return the TFS config
	 */
	public TFSConfig getTFSConfig() {
		return TestBedManagerConfiguration.INSTANCE.getTfsConfig();
	}

	public CurrentTestBeds getCurrentTestBeds() {
		return currentTestBeds;
	}

	public void setCurrentTestBeds(CurrentTestBeds currentTestBeds) {
		this.currentTestBeds = currentTestBeds;
	}

	/**
	 * 
	 * Provide the method to execute TestNg object build from the KD Excel
	 * sheets test cases.
	 * 
	 * @throws ClassNotFoundException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws InvalidFormatException
	 * @throws IOException
	 * @throws InvalidActionException
	 * @throws InvalidValueException
	 * @throws DuplicateDataKeyException
	 */
	public void executeKDTestNG() throws ClassNotFoundException, InstantiationException, IllegalAccessException, InvalidFormatException, IOException, InvalidActionException,
			InvalidValueException, DuplicateDataKeyException {

		KDManager kdManager = KDManager.getInstance();

		if (kdManager.isTestSuitesAvailable()) {
			TestNG testNg = kdManager.buildKDTestSuites();

			log.debug("Running TestNG - Test Suite for Keyword Driven ...");
			testNg.run();
		}
	}

	public KDConfig getKdConfig() {
		return TestBedManagerConfiguration.INSTANCE.getKdConfig();
	}


	public CurrentDrivers getCurrentDrivers() {
		return currentDrivers;
	}


	public void setCurrentDrivers(CurrentDrivers currentDrivers) {
		this.currentDrivers = currentDrivers;
	}

	
	
	
	
}
