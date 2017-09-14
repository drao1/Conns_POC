package com.etouch.taf.util;

import io.appium.java_client.AppiumDriver;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;

import com.etouch.taf.core.TestBedManager;
import com.etouch.taf.core.config.TestBedManagerConfiguration;
import com.etouch.taf.core.datamanager.excel.XlsxReader;
import com.etouch.taf.core.driver.DriverManager;
import com.etouch.taf.core.exception.DriverException;

/**
 * The Class TestUtil.
 */
public class TestUtil {

	/** The log. */
	static Log log = LogUtil.getLog(TestUtil.class);

	/**
	 * The main method.
	 * 
	 * @param args
	 *            the arguments
	 */
	public static void main(String[] args) {

	}

	/**
	 * Pick config file.
	 * 
	 * @param currentEnvironment
	 *            the current environment
	 * @return the string
	 */
	public static String pickConfigFile(String currentEnvironment) {

		String defaultConfigFile = "devConfig.yml";
		if (currentEnvironment != null && currentEnvironment.length() > 0) {
			return currentEnvironment + "Config.yml";
		} else {
			return defaultConfigFile;
		}
	}

	// find if the Test Suite is runnable based on the run mode
	/**
	 * Checks if is suite runnable.
	 * 
	 * @param xls
	 *            the xls
	 * @param suiteName
	 *            the suite name
	 * @return true, if is suite runnable
	 */
	public static boolean isSuiteRunnable(XlsxReader xls, String suiteName) {
		log.debug("Xls name:" + xls + ", Suite name:" + suiteName);
		boolean isExecutable = false;
		for (int i = 2; i <= xls.getRowCount("Test Suite"); i++) {
			String tsid = xls.getCellData("Test Suite", "TSID", i);
			String runmode = xls.getCellData("Test Suite", "Runmode", i);
			log.debug(tsid + " Runmode is " + runmode);

			if (tsid.equalsIgnoreCase(suiteName)) {
				if (runmode.equalsIgnoreCase("Y")) {
					isExecutable = true;
				} else {
					isExecutable = false;
				}
			}
		}
		xls = null;
		return isExecutable;
	}

	// Find if the specified test is runnable based on runmode
	/**
	 * Checks if is test runnable.
	 * 
	 * @param xls
	 *            the xls
	 * @param testCaseName
	 *            the test case name
	 * @return true, if is test runnable
	 */
	public static boolean isTestRunnable(XlsxReader xls, String testCaseName) {
		boolean isExecutable = false;

		log.debug("Sheet name:" + xls + ", Test case name:" + testCaseName);
		for (int i = 2; i <= xls.getRowCount("Test Cases"); i++) {
			String tcid = xls.getCellData("Test Cases", "TCID", i);
			String runmode = xls.getCellData("Test Cases", "Runmode", i);

			if (tcid.equalsIgnoreCase(testCaseName)) {
				if (runmode.equalsIgnoreCase("Y")) {
					isExecutable = true;
				} else {
					isExecutable = false;
				}
			}
		}

		xls = null;// release memory
		return isExecutable;
	}

	/**
	 * Gets the data.
	 * 
	 * @param xls
	 *            the xls
	 * @param testCaseName
	 *            the test case name
	 * @return the data
	 */
	public static Object[][] getData(XlsxReader xls, String testCaseName) {

		log.debug("Reader sheet name:" + xls + ", testcasename:" + testCaseName);
		// if the test data sheet is not present for a test case
		if (!xls.isSheetExist(testCaseName)) {
			xls = null;
			return new Object[1][0];
		}

		int rows = xls.getRowCount(testCaseName);
		int cols = xls.getColumnCount(testCaseName);

		// Retrieving data from excel
		Object[][] data = new Object[rows - 1][cols - 3];
		for (int rowNum = 2; rowNum <= rows; rowNum++) {
			for (int colNum = 0; colNum < cols - 3; colNum++) {
				data[rowNum - 2][colNum] = xls.getCellData(testCaseName, colNum, rowNum);
			}
		}
		return data;
	}

	// checks Runmode for dataSet
	/**
	 * Gets the data set runmodes.
	 * 
	 * @param xlsFile
	 *            the xls file
	 * @param sheetName
	 *            the sheet name
	 * @return the data set runmodes
	 */
	public static String[] getDataSetRunmodes(XlsxReader xlsFile, String sheetName) {
		String[] runmodes = null;
		if (!xlsFile.isSheetExist(sheetName)) {
			xlsFile = null;
			sheetName = null;
			runmodes = new String[1];
			runmodes[0] = "Y";
			xlsFile = null;
			sheetName = null;
			return runmodes;
		}
		runmodes = new String[xlsFile.getRowCount(sheetName) - 1];
		for (int i = 2; i <= runmodes.length + 1; i++) {
			runmodes[i - 2] = xlsFile.getCellData(sheetName, "Runmode", i);
			log.debug("Runmodes of sheet:" + xlsFile.getCellData(sheetName, "Runmode", i));
		}
		xlsFile = null;
		sheetName = null;
		return runmodes;

	}

	/**
	 * Report data set result. updating results for a particular data set
	 * 
	 * @param xls
	 *            the xls
	 * @param testCaseName
	 *            the test case name
	 * @param rowNum
	 *            the row num
	 * @param result
	 *            the result
	 */
	public static void reportDataSetResult(XlsxReader xls, String testCaseName, int rowNum, String result) {
		xls.setCellData(testCaseName, "Results", rowNum, result);
	}

	/**
	 * Report data set result class link. updating results for a particular data
	 * set
	 * 
	 * @param xls
	 *            the xls
	 * @param testCaseName
	 *            the test case name
	 * @param rowNum
	 *            the row num
	 * @param result
	 *            the result
	 */
	public static void reportDataSetResultClassLink(XlsxReader xls, String testCaseName, int rowNum, String result) {
		xls.setCellData(testCaseName, "ClassregLnk", rowNum, result);
	}

	/**
	 * Report data set result class id. updating results for a particular data
	 * set
	 * 
	 * @param xls
	 *            the xls
	 * @param testCaseName
	 *            the test case name
	 * @param rowNum
	 *            the row num
	 * @param result
	 *            the result
	 */
	public static void reportDataSetResultClassId(XlsxReader xls, String testCaseName, int rowNum, String result) {
		xls.setCellData(testCaseName, "ClassId", rowNum, result);
	}

	/**
	 * Gets the row num.
	 * 
	 * @param xls
	 *            the xls
	 * @param id
	 *            the id
	 * @return the row num
	 */
	public static int getRowNum(XlsxReader xls, String id) {
		for (int i = 2; i <= xls.getRowCount("Test Cases"); i++) {
			String tcid = xls.getCellData("Test Cases", "TCID", i);
			if (tcid.equals(id)) {
				xls = null;
				return i;
			}
		}
		return -1;
	}

	/**
	 * Creates the driver.
	 * 
	 * @param create
	 *            the create
	 * @return the web driver
	 * @throws DriverException
	 *             the driver exception
	 */
	public static WebDriver createDriver(ITestContext context, boolean isCreate) throws DriverException {
		String testBedName = context.getCurrentXmlTest().getAllParameters().get("testBedName");

		WebDriver driverObj = (WebDriver) TestBedManager.INSTANCE.getCurrentTestBeds().get(testBedName).getDriver();
		if (driverObj == null && isCreate) {
			driverObj = (WebDriver) DriverManager.buildDriver(testBedName).getDriver();
			TestBedManager.INSTANCE.getCurrentTestBeds().get(testBedName).setDriver(driverObj);
		}

		return driverObj;

	}

	public static Properties loadProps(String propFilePath) {
		Properties prop = new Properties();
		InputStream inStream = null;

		try {

			inStream = new FileInputStream(propFilePath);

			// load properties from the file to the props object.
			prop.load(inStream);

		} catch (IOException io) {
			log.error(io.getMessage());
		} finally {
			if (inStream != null) {
				try {
					inStream.close();
				} catch (IOException e) {
					log.error(e.getMessage());
				}
			}

		}

		return prop;
	}

	/**
	 * Close driver.
	 * 
	 * @param driverObj
	 *            the driver obj
	 */
	public static void closeDriver(WebDriver driverObj) {
		log.debug("After Method SOP");
		if (driverObj != null) {
			try {
				driverObj.close();
				Thread.sleep(5000);
				driverObj.quit();

			} catch (Exception ex) {
				log.error(ex.getMessage());
			}

		}

		if (ConfigUtil.isSafariEnabled())
			try {
				shutdownSeleniumServer();
			} catch (IOException e) {
				log.error(e.getMessage());
			}
	}

	public static void closeMobileDriver(AppiumDriver driverObj) {
		if (driverObj != null) {
			driverObj.quit();
		}
	}

	public static String formatJsonString(String str) {
		str = str.replace("\"", "\\\"");
		str = str.replace("\n", "\\n");
		return str;
	}

	public static void copyFile(File source, File destination) throws IOException {

		Files.copy(source.toPath(), destination.toPath());

	}

	public static boolean isCopyToServer() {
		if (TestBedManagerConfiguration.INSTANCE.getVideoConfig().getCopyToServer().equalsIgnoreCase("yes"))
			return true;
		else
			return false;
	}

	public static void shutdownSeleniumServer() throws IOException {
		URL seleniumHub = new URL("http://" + TestBedManagerConfiguration.INSTANCE.getWebConfig().getHub() + ":4444/selenium-server/driver/?cmd=shutDownSeleniumServer");
		URLConnection connection = seleniumHub.openConnection();
		BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
		String inputLine;
		while ((inputLine = in.readLine()) != null) {
			log.info("Shutting down selenium server: response - " + inputLine);
		}
		in.close();
	}

}