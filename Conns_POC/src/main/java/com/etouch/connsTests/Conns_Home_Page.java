package com.etouch.connsTests;

import java.awt.AWTException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.http.client.ClientProtocolException;
import org.apache.log4j.Logger;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.etouch.common.BaseTest;
import com.etouch.common.CommonMethods;
import com.etouch.common.TafExecutor;
import com.etouch.connsPages.ConnsHomePage;
import com.etouch.connsPages.ConnsMainPage;
import com.etouch.taf.core.TestBed;
import com.etouch.taf.core.TestBedManager;
import com.etouch.taf.core.config.TestBedManagerConfiguration;
import com.etouch.taf.core.datamanager.excel.annotations.IExcelDataFiles;
import com.etouch.taf.core.exception.PageException;
import com.etouch.taf.tools.rally.VideoRecorder;
import com.etouch.taf.util.CommonUtil;
import com.etouch.taf.util.ExcelUtil;
import com.etouch.taf.util.LogUtil;
import com.etouch.taf.util.SoftAssertor;
import com.etouch.taf.util.TafPassword;
import com.etouch.taf.webui.selenium.MobileView;
import com.etouch.taf.webui.selenium.WebPage;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.TouchAction;

@IExcelDataFiles(excelDataFiles = { "ConnsHomePageData=testData" })
public class Conns_Home_Page extends BaseTest {

	/** The url3. */
	String url3 = TestBedManager.INSTANCE.getDefectConfig().getUrl3();

	/** The issue url. */
	String issueUrl = TestBedManager.INSTANCE.getDefectConfig().getIssueUrl();

	/** The username. */
	String username = TestBedManager.INSTANCE.getDefectConfig().getUsername();

	/** The password. */
	TafPassword password = TestBedManager.INSTANCE.getDefectConfig().getPassword();

	/** The keys. */
	String keys = TestBedManager.INSTANCE.getDefectConfig().getKeys();

	// required for rally
	/** The Constant DEFECT_PROP. */

	// required for rally
	/** The Constant DEFECT_PROP. */

	/** The project id. */
	String PROJECT_ID = TestBedManager.INSTANCE.getDefectConfig().getProjectId();

	/** The defect owner. */
	String DEFECT_OWNER = TestBedManager.INSTANCE.getDefectConfig().getDefectOwner();

	/** The workspace id. */
	String WORKSPACE_ID = TestBedManager.INSTANCE.getDefectConfig().getWorkspaceId();
	VideoRecorder videoRecorder = null;
	AppiumDriver driver = null;
	MobileView mobileView = null;
	private ConnsMainPage mainPage;
	TouchAction act1;
	DesiredCapabilities capabilities;

	Properties props;
	String deviceName;
	String plateformVersion;
	String platformName;
	String udid;
	String appPackage;
	String appActivity;
	String appiumHost;
	String appiumPortNumber;
	String apkPath;
	private String testBedName;
	TestBed testBed;
	Path path;
	String DataFilePath;
	String testType;
	String testEnv;
	String browserName;
	CommonMethods commonMethods;

	int j = 0;

	static Log log = LogUtil.getLog(Conns_Home_Page.class);
	Logger logger = Logger.getLogger(Conns_Home_Page.class.getName());
	private final int MAX_WAIT = 20;

	private String url;
	private String PageUrl = "";
	private WebPage webPage;
	private ConnsHomePage ConnsHomePage;
	static String platform;
	static String AbsolutePath = TafExecutor.class.getProtectionDomain().getCodeSource().getLocation().getPath();
	static String videoLocation = AbsolutePath.substring(0, AbsolutePath.indexOf("/target/classes/")).substring(1)
			.concat("/src/test/resources/testdata/videos");

	/**
	 * Prepare before class.
	 *
	 * @throws Exception
	 *             the exception
	 */
	@BeforeClass(alwaysRun = true)
	public void setUp(ITestContext context) throws InterruptedException, FileNotFoundException, IOException {
		try {
			testBedName = context.getCurrentXmlTest().getAllParameters().get("testBedName");
			CommonUtil.sop("Test bed Name is " + testBedName);
			testBed = TestBedManager.INSTANCE.getCurrentTestBeds().get(testBedName);
			testType = TestBedManager.INSTANCE.getCurrentTestBeds().get(testBedName).getTestType();
			browserName = TestBedManager.INSTANCE.getCurrentTestBeds().get(testBedName).getBrowser().getName();
			commonMethods = new CommonMethods();
			System.out.println("Test Type is : " + testType);
			try {
				testEnv = System.getenv().get("Environment");
				System.out.println("testEnv is : " + System.getenv().get("Environment"));
				path = Paths.get(TestBedManager.INSTANCE.getProfile().getXlsDataConfig().get("testData"));
				DataFilePath = path.toAbsolutePath().toString().replace("Env", testEnv);
				System.out.println("DataFilePath After is : " + DataFilePath);

				platform = testBed.getPlatform().getName().toUpperCase();
				if (testType.equalsIgnoreCase("Web")) {
					System.out.println("videoLocation" + videoLocation.toString().replace("Env", testEnv));
				}
				url = TestBedManagerConfiguration.INSTANCE.getWebConfig().getURL();
				synchronized (this) {

					webPage = new WebPage(context);
					ConnsHomePage = new ConnsHomePage(url, webPage);
					System.out.println("Conns onject" + ConnsHomePage);
					mainPage = new ConnsMainPage(url, webPage);
					System.out.println(mainPage);
				}

			} catch (Exception e) {
				log.info("errr is " + e);
				SoftAssertor.addVerificationFailure(e.getMessage());
			}
		}

		catch (Exception e) {

			CommonUtil.sop("errr is for" + testBedName + " -----------" + e);
			SoftAssertor.addVerificationFailure(e.getMessage());
		}
	}

	@AfterTest
	public void releaseResources() throws IOException, AWTException {
		webPage.getDriver().quit();
	}

	/**
	 * Test Case - 001 - Verify title and URL of page Conns Home Page
	 * 
	 */

	@Test(priority = 1, enabled = true, description = "Verify HomePage title")
	public void Verify_HomePage_Title() {
		SoftAssert softAssert = new SoftAssert();
		try {
			// webPage.getDriver().get("http://connsecommdev-1365538477.us-east-1.elb.amazonaws.com/conns_rwd/");

			String[][] test = ExcelUtil.readExcelData(DataFilePath, "Conns_Home_Page", "Verifytitle");
			String ExpectedTitle = test[0][1];
			softAssert.assertEquals(webPage.getPageTitle(), ExpectedTitle,
					"Page Title verification failed. Expected title - " + ExpectedTitle + " Actual title - "
							+ webPage.getPageTitle());
			softAssert.assertAll();
		} catch (Throwable e) {
			mainPage.getScreenShotForFailure(webPage, "Verify_HomePage_Title");
			softAssert.assertAll();
			Assert.fail(e.getLocalizedMessage());
		}
	}

	/**
	 * Test Case - 002 - Verify Font Size and Style of specified on element on
	 * Conns Home Page
	 * 
	 */

	@Test(priority = 2, enabled = true, description = "Verify_Font_And_Size")
	public void Verify_Font_And_Size() {
		SoftAssert softAssert = new SoftAssert();
		try {
			String[][] ExpectedFontValues = ExcelUtil.readExcelData(DataFilePath, "Conns_Home_Page",
					"VerifyFontandSize");
			for (int i = 0; i < ExpectedFontValues.length; i++) {
				List<String> actualCssValues = commonMethods.getFontProperties(webPage, ExpectedFontValues[i][1],
						softAssert);
				if (testType.equalsIgnoreCase("Mobile")) {
					softAssert.assertTrue(actualCssValues.get(0).contains(ExpectedFontValues[i][5]),
							"CSS value verification failed for link " + ExpectedFontValues[i][0]
									+ "Expected font Size : " + ExpectedFontValues[i][5] + " Actual Font Size : "
									+ actualCssValues.get(0));
					softAssert.assertTrue(actualCssValues.get(1).contains(ExpectedFontValues[i][6]),
							"CSS value verification failed for link " + ExpectedFontValues[i][0]
									+ "Expected font color : " + ExpectedFontValues[i][6] + " Actual font family : "
									+ actualCssValues.get(1));
					softAssert.assertTrue(actualCssValues.get(2).contains(ExpectedFontValues[i][4]),
							"CSS value verification failed for link " + ExpectedFontValues[i][0]
									+ "Expected font family : " + ExpectedFontValues[i][4] + " Actual font family : "
									+ actualCssValues.get(2));

				} else {
					softAssert.assertTrue(actualCssValues.get(0).contains(ExpectedFontValues[i][2]),
							"CSS value verification failed for link " + ExpectedFontValues[i][0]
									+ "Expected font Size : " + ExpectedFontValues[i][2] + " Actual Font Size : "
									+ actualCssValues.get(0));
					softAssert.assertTrue(actualCssValues.get(1).contains(ExpectedFontValues[i][3]),
							"CSS value verification failed for link " + ExpectedFontValues[i][0]
									+ "Expected font color : " + ExpectedFontValues[i][3] + " Actual font family : "
									+ actualCssValues.get(1));
					softAssert.assertTrue(actualCssValues.get(2).toLowerCase().contains((ExpectedFontValues[i][4]).toLowerCase()),
							"CSS value verification failed for link " + ExpectedFontValues[i][0]
									+ "Expected font family : " + ExpectedFontValues[i][4] + " Actual font family : "
									+ actualCssValues.get(2));
					
				}
			}
			softAssert.assertAll();
		} catch (Throwable e) {
			mainPage.getScreenShotForFailure(webPage, "Verify_Font_And_Size");
			softAssert.assertAll();
			Assert.fail(e.getLocalizedMessage());
		}
	}

	/**
	 * Test Case - 003 - Verify broken images on page Conns Home Page
	 * 
	 */

	@Test(priority = 3, enabled = true, description = "Verify_Broken_Images")
	public void Verify_Broken_Images() throws ClientProtocolException, IOException {
		ConnsHomePage.verifyBrokenImage();
	}

	/**
	 * Test Case - 004 - verify Links Above Header Conns Home Page
	 * 
	 */

	@Test(priority = 4, enabled = true, description = "Verify_LinksRedirection_Of_Above_Header_Section")
	public void Verify_LinksRedirection_Of_Above_Header_Section() {
		SoftAssert softAssert = new SoftAssert();
		String ActualURL = null;
		// webPage.getDriver().get(url);
		try {
			String[][] testData = ExcelUtil.readExcelData(DataFilePath, "Conns_Home_Page", "verifyLinksAboveHeader");

			for (int i = 0; i < testData.length; i++)

			{

				if (testType.equalsIgnoreCase("Mobile") && (!(testData[i][3].equalsIgnoreCase("NA")))) {
					System.out.println("Inside the first if. Value of I : " + i);
					commonMethods.clickElementbyXpath(webPage, testData[i][3], softAssert);
					Thread.sleep(1000);
				}

				if (testType.equalsIgnoreCase("Mobile") && (!(testData[i][2].equalsIgnoreCase("NA")))) {
					System.out.println("Inside the 2nd if. Value of I : " + i);
					ActualURL=commonMethods.clickElementbyXpathAndGetURL(webPage, testData[i][2], softAssert);
/*					ActualURL = ConnsHomePage.clickAndGetPageURLUsingJS(webPage, testData[i][2], testData[i][0],
							testData[i][5], softAssert);*/
					softAssert.assertTrue(ActualURL.contains(testData[i][4]),
							"Link Name  :" + testData[i][0] + " : failed " + "Actual URL is  :" + ActualURL + " "
									+ "Expected URL is  :" + testData[i][4]);
					
				}
				if (testType.equalsIgnoreCase("Web")) {
					//replacing "clickAndGetPageURLUsingJS" method with "clickAndGetPageURLUsingJS"
					ActualURL = ConnsHomePage.clickAndGetPageURLUsingJS(webPage, testData[i][1], testData[i][0],
							testData[i][5], softAssert);

					softAssert.assertTrue(ActualURL.contains(testData[i][4]),
							"Link Name  :" + testData[i][0] + " : failed " + "Actual URL is  :" + ActualURL + " "
									+ "Expected URL is  :" + testData[i][4]);
				}
				webPage.getDriver().get(url);

			}

			softAssert.assertAll();
		} catch (Throwable e) {
			softAssert.assertAll();
			Assert.fail(e.getLocalizedMessage());

		}
	}

	/**
	 * Test Case - 005 - verify Your Cart functionality by adding product in
	 * cart Conns Home Page
	 * 
	 */

	@Test(priority = 5, enabled = true, description = "Verify_Your_Cart_Functionality")
	public void Verify_Your_Cart_Functionality() {
		SoftAssert softAssert = new SoftAssert();
		// webPage.getDriver().get(url);
		try {
			if (testType.equalsIgnoreCase("Web")) {
				String[][] testData = ExcelUtil.readExcelData(DataFilePath, "Conns_Home_Page", "verifyYourCart");
				List<String> actualValues = ConnsHomePage.verifyYourCart(testData, testType);
				softAssert.assertEquals(actualValues.get(0), actualValues.get(1));
				log.info("1st Asset pass");
				softAssert.assertTrue(actualValues.get(2).contains(testData[0][11]), " failed " + "Actual URL is  :"
						+ actualValues.get(2) + " " + "Expected URL is  :" + testData[0][11]);
				log.info("2nd Asset pass");
			}

			if (testType.equalsIgnoreCase("Mobile")) {
				String[][] testData = ExcelUtil.readExcelData(DataFilePath, "Conns_Home_Page",
						"verifyYourCartOnMobile");
				List<String> actualValues = ConnsHomePage.verifyYourCartOnMobile(testData, testType);
				log.info("actualValues.get(0) : " + actualValues.get(0));
				log.info("actualValues.get(1) : " + actualValues.get(1));
				softAssert.assertEquals(actualValues.get(0), actualValues.get(1));
			}
			softAssert.assertAll();

		} catch (Throwable e) {
			mainPage.getScreenShotForFailure(webPage, "Verify_Your_Cart_Functionality");
			softAssert.assertAll();
			Assert.fail(e.getLocalizedMessage());
		}

	}

	/**
	 * Test Case - 006 - verify links In Header Conns Home Page
	 * 
	 */

	@Test(priority = 6, enabled = true, description = "Verify_Links_In_Header_Section")
	public void Verify_Links_In_Header_Section() {
		SoftAssert softAssert = new SoftAssert();
		// webPage.getDriver().get(url);
		String ActualURL ="";
			try {
			String[][] testData = ExcelUtil.readExcelData(DataFilePath, "Conns_Home_Page", "verifyLinksInHeader");
			for (int i = 0; i < testData.length; i++) {

				if (testType.equalsIgnoreCase("Mobile")) {
					System.out.println("1111");
					ActualURL=commonMethods.clickElementbyXpathAndGetURL(webPage, testData[i][3], softAssert);

				}
				else{
				 ActualURL = ConnsHomePage.clickAndGetPageURLUsingJS(webPage, testData[i][1], testData[i][0],
						testData[i][5], softAssert);
				 System.out.println("*4444");
				}
				System.out.println("5555");
				softAssert.assertTrue(ActualURL.contains(testData[i][4]), "Link Name  :" + testData[i][0] + " : failed "
						+ "Actual URL is  :" + ActualURL + " " + "Expected URL is  :" + testData[i][4]);
				System.out.println("6666");
				webPage.getDriver().get(url);
				System.out.println("7777");
			}

			softAssert.assertAll();
		} catch (Throwable e) {
			mainPage.getScreenShotForFailure(webPage, "Verify_Links_In_Header_Section");
			//mainPage.getScreenShotForFailure(webPage, "Verify_LinksRedirection_Of_Above_Header_Section");
			softAssert.assertAll();
			Assert.fail(e.getLocalizedMessage());
			System.out.println("*8888");
		}

	}

	/**
	 * Test Case - 007 - verify links under Furniture & Mattresses main menu
	 * Conns Home Page
	 * 
	 */

	@Test(priority = 7, enabled = true, description = "Verify_LinksRedirection_Under_Furniture_And_Mattresses_Menu")
	public void Verify_LinksRedirection_Under_Furniture_And_Mattresses_Menu()
			throws PageException, InterruptedException {
		SoftAssert softAssert = new SoftAssert();
		String ActualURL = null;
		// webPage.getDriver().get(url);
		try {
			String[][] testData = ExcelUtil.readExcelData(DataFilePath, "Conns_Home_Page",
					"verifyLinksForFurnitureAndMattresses");
			for (int i = 0; i < testData.length; i++) {

				log.info("Verify_LinksRedirection_Under_Furniture_And_Mattresses_Menu: Inside for loop" + i);
				if (testType.equalsIgnoreCase("Web")) {
					commonMethods.hoverOnelementbyXpath(webPage, testData[i][0], softAssert);
					ActualURL = ConnsHomePage.clickAndGetPageURLUsingJS(webPage, testData[i][1], testData[i][3],
							testData[i][8], softAssert);
					log.info("ActualURL: "+ActualURL);
				     log.info("Expected URL Should Contain:  "+testData[i][2]);
					softAssert.assertTrue(ActualURL.contains(testData[i][2]),
							"Link Name  :" + testData[i][3] + " : failed " + "Actual URL is  :" + ActualURL + " "
									+ "Expected URL is  :" + testData[i][2]);
					webPage.getDriver().get(url);
					log.info(
							"Verify_LinksRedirection_Under_Furniture_And_Mattresses_Menu: Inside for loop Web condition"
									+ i);
				}

				log.info(
						"Verify_LinksRedirection_Under_Furniture_And_Mattresses_Menu: Inside for loop before Mobile condition"
								+ testData[i][4]);

				if (testType.equalsIgnoreCase("Mobile") && !((testData[i][4].equalsIgnoreCase("NA")))) {
					log.info("Valur of i : " + i);
					log.info(
							"Verify_LinksRedirection_Under_Furniture_And_Mattresses_Menu: Inside for loop Mobile condition"
									+ i);
					commonMethods.clickElementbyXpath(webPage, testData[i][4], softAssert);
					commonMethods.clickElementbyXpath(webPage, testData[i][5], softAssert);
					commonMethods.clickElementbyXpath(webPage, testData[i][6], softAssert);

					ActualURL = ConnsHomePage.clickAndGetPageURLUsingJS(webPage, testData[i][7], testData[i][3],
							testData[i][8], softAssert);
					softAssert.assertTrue(ActualURL.contains(testData[i][2]),
							"Link Name  :" + testData[i][3] + " : failed " + "Actual URL is  :" + ActualURL + " "
									+ "Expected URL is  :" + testData[i][2]);
					webPage.getDriver().get(url);
				}
			}
			softAssert.assertAll();
		} catch (Throwable e) {
			mainPage.getScreenShotForFailure(webPage, "Verify_LinksRedirection_Under_Furniture_And_Mattresses_Menu");
			softAssert.assertAll();
			Assert.fail(e.getLocalizedMessage());
		}
	}

	/**
	 * Test Case - 008 - verify links under Appliances main menu Conns Home Page
	 * 
	 */

	@Test(priority = 8, enabled = true, description = "Verify_LinksRedirection_Under_Appliances_Menu")
	public void Verify_LinksRedirection_Under_Appliances_Menu() throws PageException, InterruptedException {
		SoftAssert softAssert = new SoftAssert();
		String ActualURL = null;
		// webPage.getDriver().get(url);
		try {
			String[][] testData = ExcelUtil.readExcelData(DataFilePath, "Conns_Home_Page", "verifyLinksForAppliance");
			for (int i = 0; i < testData.length; i++) {
				if (testType.equalsIgnoreCase("Web")) {
					commonMethods.hoverOnelementbyXpath(webPage, testData[i][0], softAssert);
					ActualURL = ConnsHomePage.clickAndGetPageURLUsingJS(webPage, testData[i][1], testData[i][3],
							testData[i][8], softAssert);
					softAssert.assertTrue(ActualURL.contains(testData[i][2]),
							"Link Name  :" + testData[i][3] + " : failed " + "Actual URL is  :" + ActualURL + " "
									+ "Expected URL is  :" + testData[i][2]);
					webPage.getDriver().get(url);
				}
				if (testType.equalsIgnoreCase("Mobile") && !((testData[i][4].equalsIgnoreCase("NA")))) {
					System.out.println("Valur of i : " + i);
					commonMethods.clickElementbyXpath(webPage, testData[i][4], softAssert);
					commonMethods.clickElementbyXpath(webPage, testData[i][5], softAssert);
					commonMethods.clickElementbyXpath(webPage, testData[i][6], softAssert);

					ActualURL = ConnsHomePage.clickAndGetPageURLUsingJS(webPage, testData[i][7], testData[i][3],
							testData[i][8], softAssert);
					softAssert.assertTrue(ActualURL.contains(testData[i][2]),
							"Link Name  :" + testData[i][3] + " : failed " + "Actual URL is  :" + ActualURL + " "
									+ "Expected URL is  :" + testData[i][2]);
					webPage.getDriver().get(url);
				}
			}
			softAssert.assertAll();
		} catch (Throwable e) {
			mainPage.getScreenShotForFailure(webPage, "Verify_LinksRedirection_Under_Appliances_Menu");
			softAssert.assertAll();
			Assert.fail(e.getLocalizedMessage());
		}

	}

	/**
	 * Test Case - 009 - verify links under TV, Audio & Electronics main menu
	 * Conns Home Page
	 * 
	 */
	@Test(priority = 9, enabled = true, description = "Verify_LinksRedirection_Under_TV_Audio_And_Electronics_Menu")
	public void Verify_LinksRedirection_Under_TV_Audio_And_Electronics_Menu()
			throws PageException, InterruptedException {
		SoftAssert softAssert = new SoftAssert();
		String ActualURL = null;
		webPage.getDriver().get(url);
		try {
			String[][] testData = ExcelUtil.readExcelData(DataFilePath, "Conns_Home_Page",
					"verifyLinksForTvAudioElectronics");

			for (int i = 0; i < testData.length; i++) {
				log.info("Current value of i is : " + i);
				if (testType.equalsIgnoreCase("Web")) {
					commonMethods.hoverOnelementbyXpath(webPage, testData[i][0], softAssert);
					ActualURL = ConnsHomePage.clickAndGetPageURLUsingJS(webPage, testData[i][1], testData[i][3],
							testData[i][8], softAssert);
					softAssert.assertTrue(ActualURL.contains(testData[i][2]),
							"Link Name  :" + testData[i][3] + " : failed " + "Actual URL is  :" + ActualURL + " "
									+ "Expected URL is  :" + testData[i][2]);
					webPage.getDriver().get(url);
				}
				if (testType.equalsIgnoreCase("Mobile") && !((testData[i][4].equalsIgnoreCase("NA")))) {
					System.out.println("Valur of i : " + i);
					commonMethods.clickElementbyXpath(webPage, testData[i][4], softAssert);
					commonMethods.clickElementbyXpath(webPage, testData[i][5], softAssert);
					commonMethods.clickElementbyXpath(webPage, testData[i][6], softAssert);

					ActualURL = ConnsHomePage.clickAndGetPageURLUsingJS(webPage, testData[i][7], testData[i][3],
							testData[i][8], softAssert);
					softAssert.assertTrue(ActualURL.contains(testData[i][2]),
							"Link Name  :" + testData[i][3] + " : failed " + "Actual URL is  :" + ActualURL + " "
									+ "Expected URL is  :" + testData[i][2]);
					webPage.getDriver().get(url);
				}
			}
			softAssert.assertAll();
		} catch (Throwable e) {
			mainPage.getScreenShotForFailure(webPage, "Verify_LinksRedirection_Under_TV_Audio_And_Electronics_Menu");
			softAssert.assertAll();
			Assert.fail(e.getLocalizedMessage());
		}

	}

	/**
	 * Test Case - 010 - verify links under Computer Accessories main menu Conns
	 * Home Page
	 * 
	 */
	@Test(priority = 10, enabled = true, description = "Verify_LinksRedirection_Under_ComputerAccessories_Menu")
	public void Verify_LinksRedirection_Under_ComputerAccessories_Menu() throws PageException, InterruptedException {
		SoftAssert softAssert = new SoftAssert();
		String ActualURL = null;
		webPage.getDriver().get(url);
		try {
			String[][] testData = ExcelUtil.readExcelData(DataFilePath, "Conns_Home_Page",
					"verifyLinksForComputerAccessories");
			for (int i = 0; i < testData.length; i++) {
				if (testType.equalsIgnoreCase("Web")) {
					commonMethods.hoverOnelementbyXpath(webPage, testData[i][0], softAssert);
					ActualURL = ConnsHomePage.clickAndGetPageURLUsingJS(webPage, testData[i][1], testData[i][3],
							testData[i][8], softAssert);
					softAssert.assertTrue(ActualURL.contains(testData[i][2]),
							"Link Name  :" + testData[i][3] + " : failed " + "Actual URL is  :" + ActualURL + " "
									+ "Expected URL is  :" + testData[i][2]);
					webPage.getDriver().get(url);
				}
				if (testType.equalsIgnoreCase("Mobile") && !((testData[i][4].equalsIgnoreCase("NA")))) {
					System.out.println("Valur of i : " + i);
					commonMethods.clickElementbyXpath(webPage, testData[i][4], softAssert);
					commonMethods.clickElementbyXpath(webPage, testData[i][5], softAssert);
					commonMethods.clickElementbyXpath(webPage, testData[i][6], softAssert);

					ActualURL = ConnsHomePage.clickAndGetPageURLUsingJS(webPage, testData[i][7], testData[i][3],
							testData[i][8], softAssert);
					softAssert.assertTrue(ActualURL.contains(testData[i][2]),
							"Link Name  :" + testData[i][3] + " : failed " + "Actual URL is  :" + ActualURL + " "
									+ "Expected URL is  :" + testData[i][2]);
					webPage.getDriver().get(url);
				}
			}
			softAssert.assertAll();
		} catch (Throwable e) {
			mainPage.getScreenShotForFailure(webPage, "Verify_LinksRedirection_Under_ComputerAccessories_Menu");
			softAssert.assertAll();
			Assert.fail(e.getLocalizedMessage());
		}

	}

	/**
	 * Test Case - 011 - verify links under Financing and Promotions main menu
	 * Conns Home Page
	 * 
	 */

	@Test(priority = 11, enabled = true, description = "Verify_LinksRedirection_Under_FinancingPromotions_Menu")
	public void Verify_LinksRedirection_Under_FinancingPromotions_Menu() throws PageException, InterruptedException {
		SoftAssert softAssert = new SoftAssert();
		String ActualURL = null;
		webPage.getDriver().get(url);
		try {
			String[][] testData = ExcelUtil.readExcelData(DataFilePath, "Conns_Home_Page",
					"verifyLinksForFinancingPromotions");
			for (int i = 0; i < testData.length; i++) {
				if (testType.equalsIgnoreCase("Web")) {
					commonMethods.hoverOnelementbyXpath(webPage, testData[i][0], softAssert);
					ActualURL = ConnsHomePage.clickAndGetPageURLUsingJS(webPage, testData[i][1], testData[i][3],
							testData[i][8], softAssert);
					softAssert.assertTrue(ActualURL.contains(testData[i][2]),
							"Link Name  :" + testData[i][3] + " : failed " + "Actual URL is  :" + ActualURL + " "
									+ "Expected URL is  :" + testData[i][2]);
					webPage.getDriver().get(url);
				}
				if (testType.equalsIgnoreCase("Mobile") && !((testData[i][4].equalsIgnoreCase("NA")))) {
					System.out.println("Valur of i : " + i);
					commonMethods.clickElementbyXpath(webPage, testData[i][4], softAssert);
					commonMethods.clickElementbyXpath(webPage, testData[i][5], softAssert);
					commonMethods.clickElementbyXpath(webPage, testData[i][6], softAssert);

					ActualURL = ConnsHomePage.clickAndGetPageURLUsingJS(webPage, testData[i][7], testData[i][3],
							testData[i][8], softAssert);
					softAssert.assertTrue(ActualURL.contains(testData[i][2]),
							"Link Name  :" + testData[i][3] + " : failed " + "Actual URL is  :" + ActualURL + " "
									+ "Expected URL is  :" + testData[i][2]);
					webPage.getDriver().get(url);
				}
			}
			softAssert.assertAll();
		} catch (Throwable e) {
			mainPage.getScreenShotForFailure(webPage, "Verify_LinksRedirection_Under_FinancingPromotions_Menu");
			softAssert.assertAll();
			Assert.fail(e.getLocalizedMessage());
		}

	}

	/**
	 * Test Case - 012 - verify Yes Banner Conns Home Page
	 * 
	 */

	@Test(priority = 12, enabled = true, description = "Verify_ApplyNow_LinkRedirection_In_YesMoney_Banner")
	public void Verify_ApplyNow_LinkRedirection_In_YesMoney_Banner() {
		SoftAssert softAssert = new SoftAssert();
		String ActualURL = null;
		webPage.getDriver().get(url);
		try {
			String[][] testData = ExcelUtil.readExcelData(DataFilePath, "Conns_Home_Page", "verifyYesMeBanner");
			for (int i = 0; i < testData.length; i++) {

				if (testType.equalsIgnoreCase("Mobile") && (!(testData[i][3].equalsIgnoreCase("NA")))) {
					System.out.println("Inside the first if. Value of I : " + i);
					commonMethods.clickElementbyXpath(webPage, testData[i][3], softAssert);
					Thread.sleep(1000);
				}

				if (testType.equalsIgnoreCase("Mobile") && (!(testData[i][2].equalsIgnoreCase("NA")))) {
					System.out.println("Inside the 2nd if. Value of I : " + i);
					ActualURL=commonMethods.clickElementbyXpathAndGetURL(webPage, testData[i][2], softAssert);
					softAssert.assertTrue(ActualURL.contains(testData[i][4]), "Link Name  :" + testData[i][0] + " : failed "
							+ "Actual URL is  :" + ActualURL + " " + "Expected URL is  :" + testData[i][4]);					
				}				
				
				if (testType.equalsIgnoreCase("Web"))
				{
					ActualURL = ConnsHomePage.clickAndGetPageURLUsingJS(webPage, testData[i][1], testData[i][0],
					testData[i][5], softAssert);
					softAssert.assertTrue(ActualURL.contains(testData[i][4]), "Link Name  :" + testData[i][0] + " : failed "
							+ "Actual URL is  :" + ActualURL + " " + "Expected URL is  :" + testData[i][4]);					
				}

				webPage.getDriver().get(url);
			}
			softAssert.assertAll();
			
		} catch (Throwable e) {
			mainPage.getScreenShotForFailure(webPage, "Verify_ApplyNow_LinkRedirection_In_YesMoney_Banner");
			softAssert.assertAll();
			Assert.fail(e.getLocalizedMessage());
		}

	}

	/**
	 * Test Case - 013 - verify NextDay Banner Conns Home Page
	 * 
	 */

	@Test(priority = 13, enabled = true, description = "Verify_MoreInfo_LinkRedirection_In_NextDayDeleivery_Banner")
	public void Verify_MoreInfo_LinkRedirection_In_NextDayDeleivery_Banner() {
		SoftAssert softAssert = new SoftAssert();
		String ActualURL = null;
		// webPage.getDriver().get(url);
		try {
			String[][] testData = ExcelUtil.readExcelData(DataFilePath, "Conns_Home_Page",
					"verifyNextDayDeliveryBanner");
			for (int i = 0; i < testData.length; i++) {

				if (testType.equalsIgnoreCase("Mobile") && (!(testData[i][3].equalsIgnoreCase("NA")))) {
					System.out.println("Inside the first if. Value of I : " + i);
					commonMethods.clickElementbyXpath(webPage, testData[i][3], softAssert);
					Thread.sleep(1000);
				}

				if (testType.equalsIgnoreCase("Mobile") && (!(testData[i][2].equalsIgnoreCase("NA")))) {
					System.out.println("Inside the 2nd if. Value of I : " + i);
					ActualURL=commonMethods.clickElementbyXpathAndGetURL(webPage, testData[i][2], softAssert);
					softAssert.assertTrue(ActualURL.contains(testData[i][4]), "Link Name  :" + testData[i][0] + " : failed "
							+ "Actual URL is  :" + ActualURL + " " + "Expected URL is  :" + testData[i][4]);					
				}				
				
				if (testType.equalsIgnoreCase("Web"))
				{
				ActualURL = ConnsHomePage.clickAndGetPageURLUsingJS(webPage, testData[i][1], testData[i][0],
						testData[i][5], softAssert);
				softAssert.assertTrue(ActualURL.contains(testData[i][4]), "Link Name  :" + testData[i][0] + " : failed "
						+ "Actual URL is  :" + ActualURL + " " + "Expected URL is  :" + testData[i][4]);				
				}

				webPage.getDriver().get(url);
			}
			softAssert.assertAll();
		} catch (Throwable e) {
			mainPage.getScreenShotForFailure(webPage, "Verify_MoreInfo_LinkRedirection_In_NextDayDeleivery_Banner");
			softAssert.assertAll();
			Assert.fail(e.getLocalizedMessage());
		}
	}

	/**
	 * Test Case - 014 - verify Save Big With Conns section Conns Home Page
	 * 
	 */

	@Test(priority = 14, enabled = true, description = "Verify_Details_Under_Save_Big_With_Conns_Latest_Deals")
	public void Verify_Details_Under_Save_Big_With_Conns_Latest_Deals() throws PageException, InterruptedException {
		webPage.getDriver().get(url);
		String[][] testData = ExcelUtil.readExcelData(DataFilePath, "Conns_Home_Page", "verifySaveBigWithConns");
		ConnsHomePage.verifySaveBigWithConnsSection(testData);
	}

	/**
	 * Test Case - 015 - verify Top Category links Conns Home Page
	 * 
	 */

	@Test(priority = 15, enabled = true, description = "Verify_Top_Categories_LinksRedirection")
	public void Verify_Top_Categories_LinksRedirection() throws PageException, InterruptedException {
		SoftAssert softAssert = new SoftAssert();
		String ActualURL = null;
		// webPage.getDriver().get(url);
		try {
			String[][] testData = ExcelUtil.readExcelData(DataFilePath, "Conns_Home_Page", "verifyTopCategorySection");
			for (int i = 0; i < testData.length; i++) {

				if (testType.equalsIgnoreCase("Mobile") && (!(testData[i][3].equalsIgnoreCase("NA")))) {
					System.out.println("Inside the first if. Value of I : " + i);
					commonMethods.clickElementbyXpath(webPage, testData[i][3], softAssert);
					Thread.sleep(1000);
				}

				if (testType.equalsIgnoreCase("Mobile") && (!(testData[i][2].equalsIgnoreCase("NA")))) {
					System.out.println("Inside the 2nd if. Value of I : " + i);
					ActualURL=commonMethods.clickElementbyXpathAndGetURL(webPage, testData[i][2], softAssert);
					softAssert.assertTrue(ActualURL.contains(testData[i][4]), "Link Name  :" + testData[i][0] + " : failed "
							+ "Actual URL is  :" + ActualURL + " " + "Expected URL is  :" + testData[i][4]);					
				}				
				
				if (testType.equalsIgnoreCase("Web"))
				{					
					ActualURL = ConnsHomePage.clickAndGetPageURLUsingJS(webPage, testData[i][1], testData[i][0],
					testData[i][5], softAssert);
					softAssert.assertTrue(ActualURL.contains(testData[i][4]), "Link Name  :" + testData[i][0] + " : failed "
							+ "Actual URL is  :" + ActualURL + " " + "Expected URL is  :" + testData[i][4]);					
				}

				webPage.getDriver().get(url);
			}

			softAssert.assertAll();
		} catch (Throwable e) {
			mainPage.getScreenShotForFailure(webPage, "Verify_Top_Categories_LinksRedirection");
			softAssert.assertAll();
			Assert.fail(e.getLocalizedMessage());
		}
	}

	/**
	 * Test Case - 016 - verify Build Your Own Financial Future Banner Conns
	 * Home Page
	 * 
	 */

	@Test(priority = 16, enabled = true, description = "Verify_Top_Categories_LinksRedirection")
	public void Verify_LearnMore_LinkRedirection_Under_Build_Your_Own_Financial_Future_Banner()
			throws PageException, InterruptedException {
		SoftAssert softAssert = new SoftAssert();
		String ActualURL = null;
		// webPage.getDriver().get(url);
		try {
			String[][] testData = ExcelUtil.readExcelData(DataFilePath, "Conns_Home_Page",
					"verifyBuildYourOwnFinancialFutureBanner");
			for (int i = 0; i < testData.length; i++) {

				if (testType.equalsIgnoreCase("Mobile") && (!(testData[i][3].equalsIgnoreCase("NA")))) {
					System.out.println("Inside the first if. Value of I : " + i);
					commonMethods.clickElementbyXpath(webPage, testData[i][3], softAssert);
					Thread.sleep(1000);
				}

				if (testType.equalsIgnoreCase("Mobile") && (!(testData[i][2].equalsIgnoreCase("NA")))) {
					System.out.println("Inside the 2nd if. Value of I : " + i);
					ActualURL=commonMethods.clickElementbyXpathAndGetURL(webPage, testData[i][2], softAssert);
					softAssert.assertTrue(ActualURL.contains(testData[i][4]),
							"Link Name  :" + testData[i][0] + " : failed " + "Actual URL is  :" + ActualURL + " "
									+ "Expected URL is  :" + testData[i][4]);					
				}				
				
				if (testType.equalsIgnoreCase("Web"))
				{				
/*				if (testType.equalsIgnoreCase("Mobile")) {
					String ActualURL = ConnsHomePage.clickAndGetPageURLUsingJS(webPage, testData[i][3],
							testData[i][0], testData[i][5], softAssert);
					softAssert.assertTrue(ActualURL.contains(testData[i][4]),
							"Link Name  :" + testData[i][0] + " : failed " + "Actual URL is  :" + ActualURL + " "
									+ "Expected URL is  :" + testData[i][4]);
				}*/
					ActualURL = ConnsHomePage.clickAndGetPageURLUsingJS(webPage, testData[i][1],
							testData[i][0], testData[i][5], softAssert);
					softAssert.assertTrue(ActualURL.contains(testData[i][4]),
							"Link Name  :" + testData[i][0] + " : failed " + "Actual URL is  :" + ActualURL + " "
									+ "Expected URL is  :" + testData[i][4]);					
				}

				webPage.getDriver().get(url);
			}
			softAssert.assertAll();
		} catch (Throwable e) {
			mainPage.getScreenShotForFailure(webPage,
					"Verify_LearnMore_LinkRedirection_Under_Build_Your_Own_Financial_Future_Banner");
			softAssert.assertAll();
			Assert.fail(e.getLocalizedMessage());
		}

	}

	/**
	 * Test Case - 017 - verify Help Children Banner Conns Home Page
	 * 
	 */
	@Test(priority = 17, enabled = true, description = "Verify_LearnMore_LinkRedirection_Under_Helping_Children_Thrive_Banner")
	public void Verify_LearnMore_LinkRedirection_Under_Helping_Children_Thrive_Banner()
			throws PageException, InterruptedException {
		SoftAssert softAssert = new SoftAssert();
		String ActualURL = null;
		// webPage.getDriver().get(url);
		try {
			String[][] testData = ExcelUtil.readExcelData(DataFilePath, "Conns_Home_Page", "verifyHelpChildrenBanner");
			for (int i = 0; i < testData.length; i++) {
				if (testType.equalsIgnoreCase("Mobile")) {

					if (testType.equalsIgnoreCase("Mobile") && (!(testData[i][3].equalsIgnoreCase("NA")))) {
						System.out.println("Inside the first if. Value of I : " + i);
						commonMethods.clickElementbyXpath(webPage, testData[i][3], softAssert);
						Thread.sleep(1000);
					}

					if (testType.equalsIgnoreCase("Mobile") && (!(testData[i][2].equalsIgnoreCase("NA")))) {
						System.out.println("Inside the 2nd if. Value of I : " + i);
						ActualURL=commonMethods.clickElementbyXpathAndGetURL(webPage, testData[i][2], softAssert);
						softAssert.assertTrue(ActualURL.contains(testData[i][4]),
								"Link Name  :" + testData[i][0] + " : failed " + "Actual URL is  :" + ActualURL + " "
										+ "Expected URL is  :" + testData[i][4]);						
					}				
					
					if (testType.equalsIgnoreCase("Web"))
					{							
/*					String ActualURL = ConnsHomePage.clickAndGetPageURLUsingJS(webPage, testData[i][3],
							testData[i][0], testData[i][5], softAssert);
					softAssert.assertTrue(ActualURL.contains(testData[i][4]),
							"Link Name  :" + testData[i][0] + " : failed " + "Actual URL is  :" + ActualURL + " "
									+ "Expected URL is  :" + testData[i][4]);*/

					ActualURL = ConnsHomePage.clickAndGetPageURLUsingJS(webPage, testData[i][1],
							testData[i][0], testData[i][5], softAssert);
					softAssert.assertTrue(ActualURL.contains(testData[i][4]),
							"Link Name  :" + testData[i][0] + " : failed " + "Actual URL is  :" + ActualURL + " "
									+ "Expected URL is  :" + testData[i][4]);					
					}

				}
				webPage.getDriver().get(url);
			}
			softAssert.assertAll();
		} catch (Throwable e) {
			mainPage.getScreenShotForFailure(webPage,
					"Verify_LearnMore_LinkRedirection_Under_Helping_Children_Thrive_Banner");
			softAssert.assertAll();
			Assert.fail(e.getLocalizedMessage());
		}
	}

	/**
	 * Test Case - 018 - verify Six Reasons Banner Conns Home Page
	 * 
	 */

	@Test(priority = 18, enabled = true, description = "Verify_LearnMore_LinkRedirection_Under_SixReasons_ToShop_Banner")
	public void Verify_LearnMore_LinkRedirection_Under_SixReasons_ToShop_Banner()
			throws PageException, InterruptedException {
		SoftAssert softAssert = new SoftAssert();
		String ActualURL = null;
		// webPage.getDriver().get(url);
		try {
			String[][] testData = ExcelUtil.readExcelData(DataFilePath, "Conns_Home_Page", "verifySixReasonsBanner");
			for (int i = 0; i < testData.length; i++) {
				
				if (testType.equalsIgnoreCase("Mobile") && (!(testData[i][3].equalsIgnoreCase("NA")))) {
					System.out.println("Inside the first if. Value of I : " + i);
					commonMethods.clickElementbyXpath(webPage, testData[i][3], softAssert);
					Thread.sleep(1000);
				}

				if (testType.equalsIgnoreCase("Mobile") && (!(testData[i][2].equalsIgnoreCase("NA")))) {
					System.out.println("Inside the 2nd if. Value of I : " + i);
					ActualURL=commonMethods.clickElementbyXpathAndGetURL(webPage, testData[i][2], softAssert);
					softAssert.assertTrue(ActualURL.contains(testData[i][4]),
							"Link Name  :" + testData[i][0] + " : failed " + "Actual URL is  :" + ActualURL + " "
									+ "Expected URL is  :" + testData[i][4]);					
				}				
				
				if (testType.equalsIgnoreCase("Web"))
				{				
/*				if (testType.equalsIgnoreCase("Mobile")) {
					String ActualURL = ConnsHomePage.clickAndGetPageURLUsingJS(webPage, testData[i][3],
							testData[i][0], testData[i][5], softAssert);
					softAssert.assertTrue(ActualURL.contains(testData[i][4]),
							"Link Name  :" + testData[i][0] + " : failed " + "Actual URL is  :" + ActualURL + " "
									+ "Expected URL is  :" + testData[i][4]);
				} else {*/
					ActualURL = ConnsHomePage.clickAndGetPageURLUsingJS(webPage, testData[i][1],
							testData[i][0], testData[i][5], softAssert);
					softAssert.assertTrue(ActualURL.contains(testData[i][4]),
							"Link Name  :" + testData[i][0] + " : failed " + "Actual URL is  :" + ActualURL + " "
									+ "Expected URL is  :" + testData[i][4]);					
				}

				webPage.getDriver().get(url);
			}
			softAssert.assertAll();
		}

		catch (Throwable e) {
			mainPage.getScreenShotForFailure(webPage,
					"Verify_LearnMore_LinkRedirection_Under_SixReasons_ToShop_Banner");
			softAssert.assertAll();
			Assert.fail(e.getLocalizedMessage());
		}

	}

	/**
	 * Test Case - 019 - verify Promotions Banner Conns Home Page
	 * 
	 */

	@Test(priority = 19, enabled = true, description = "Verify_LearnMore_LinkRedirection_Under_Promotions_Banner")
	public void Verify_LearnMore_LinkRedirection_Under_Promotions_Banner() throws PageException, InterruptedException {
		SoftAssert softAssert = new SoftAssert();
		String ActualURL = null;
		// webPage.getDriver().get(url);
		try {
			String[][] testData = ExcelUtil.readExcelData(DataFilePath, "Conns_Home_Page", "verifyPromotionsBanner");
			for (int i = 0; i < testData.length; i++) {
				
				if (testType.equalsIgnoreCase("Mobile") && (!(testData[i][3].equalsIgnoreCase("NA")))) {
					System.out.println("Inside the first if. Value of I : " + i);
					commonMethods.clickElementbyXpath(webPage, testData[i][3], softAssert);
					Thread.sleep(1000);
				}

				if (testType.equalsIgnoreCase("Mobile") && (!(testData[i][2].equalsIgnoreCase("NA")))) {
					System.out.println("Inside the 2nd if. Value of I : " + i);
					ActualURL=commonMethods.clickElementbyXpathAndGetURL(webPage, testData[i][2], softAssert);
					softAssert.assertTrue(ActualURL.contains(testData[i][4]),
							"Link Name  :" + testData[i][0] + " : failed " + "Actual URL is  :" + ActualURL + " "
									+ "Expected URL is  :" + testData[i][4]);					
				}				
				
				if (testType.equalsIgnoreCase("Web"))
				{					
/*				if (testType.equalsIgnoreCase("Mobile")) {
					String ActualURL = ConnsHomePage.clickAndGetPageURLUsingJS(webPage, testData[i][3],
							testData[i][0], testData[i][5], softAssert);
					softAssert.assertTrue(ActualURL.contains(testData[i][4]),
							"Link Name  :" + testData[i][0] + " : failed " + "Actual URL is  :" + ActualURL + " "
									+ "Expected URL is  :" + testData[i][4]);
				} else {*/
					ActualURL = ConnsHomePage.clickAndGetPageURLUsingJS(webPage, testData[i][1],
							testData[i][0], testData[i][5], softAssert);
					softAssert.assertTrue(ActualURL.contains(testData[i][4]),
							"Link Name  :" + testData[i][0] + " : failed " + "Actual URL is  :" + ActualURL + " "
									+ "Expected URL is  :" + testData[i][4]);					
				}

				webPage.getDriver().get(url);
			}

			softAssert.assertAll();
		}

		catch (Throwable e) {
			mainPage.getScreenShotForFailure(webPage, "Verify_LearnMore_LinkRedirection_Under_Promotions_Banner");
			softAssert.assertAll();
			Assert.fail(e.getLocalizedMessage());
		}
	}

	/**
	 * Test Case - 020 - verify Follow Us Section Conns Home Page
	 * 
	 */

	@Test(priority = 20, enabled = true, description = "Verify_LinkRedirection_Under_Follow_Us_Section")
	public void Verify_LinkRedirection_Under_Follow_Us_Section() throws PageException, InterruptedException {
		SoftAssert softAssert = new SoftAssert();
		String ActualURL = null;
		// webPage.getDriver().get(url);
		try {
			String[][] testData = ExcelUtil.readExcelData(DataFilePath, "Conns_Home_Page", "verifyFollowUsSection");
			for (int i = 0; i < testData.length; i++) {

				if (testType.equalsIgnoreCase("Mobile") && (!(testData[i][3].equalsIgnoreCase("NA")))) {
					System.out.println("Inside the first if. Value of I : " + i);
					commonMethods.clickElementbyXpath(webPage, testData[i][3], softAssert);
					Thread.sleep(1000);
				}

				if (testType.equalsIgnoreCase("Mobile") && (!(testData[i][2].equalsIgnoreCase("NA")))) {
					System.out.println("Inside the 2nd if. Value of I : " + i);
					ActualURL=commonMethods.clickElementbyXpathAndGetURL(webPage, testData[i][2], softAssert);
					softAssert.assertTrue(ActualURL.contains(testData[i][4]),
							"Link Name  :" + testData[i][0] + " : failed " + "Actual URL is  :" + ActualURL + " "
									+ "Expected URL is  :" + testData[i][4]);					
				}				
				
				if (testType.equalsIgnoreCase("Web"))
				{					
					ActualURL = ConnsHomePage.clickAndGetPageURLUsingJS(webPage, testData[i][1], testData[i][0],
							testData[i][5], softAssert);
					softAssert.assertTrue(ActualURL.contains(testData[i][4]), "Link Name  :" + testData[i][0] + " : failed "
							+ "Actual URL is  :" + ActualURL + " " + "Expected URL is  :" + testData[i][4]);
				}
				webPage.getDriver().get(url);
			}
			softAssert.assertAll();
		} catch (Throwable e) {
			mainPage.getScreenShotForFailure(webPage, "Verify_LinkRedirection_Under_Follow_Us_Section");
			softAssert.assertAll();
			Assert.fail(e.getLocalizedMessage());
		}
	}

	/**
	 * Test Case - 021 - verify BBBRating Banner Conns Home Page
	 * 
	 */

	@Test(priority = 21, enabled = true, description = "Verify_LearnMore_LinkRedirection_For_BBB_Rating_Banner")
	public void Verify_LearnMore_LinkRedirection_For_BBB_Rating_Banner() throws PageException, InterruptedException {
		SoftAssert softAssert = new SoftAssert();
		String ActualURL = null;
		// webPage.getDriver().get(url);
		try {
			String[][] testData = ExcelUtil.readExcelData(DataFilePath, "Conns_Home_Page", "verifyBBBRatingBanner");
			for (int i = 0; i < testData.length; i++) {

				if (testType.equalsIgnoreCase("Mobile") && (!(testData[i][3].equalsIgnoreCase("NA")))) {
					System.out.println("Inside the first if. Value of I : " + i);
					commonMethods.clickElementbyXpath(webPage, testData[i][3], softAssert);
					Thread.sleep(1000);
				}

				if (testType.equalsIgnoreCase("Mobile") && (!(testData[i][2].equalsIgnoreCase("NA")))) {
					System.out.println("Inside the 2nd if. Value of I : " + i);
					ActualURL=commonMethods.clickElementbyXpathAndGetURL(webPage, testData[i][2], softAssert);
					softAssert.assertTrue(ActualURL.contains(testData[i][4]),
							"Link Name  :" + testData[i][0] + " : failed " + "Actual URL is  :" + ActualURL + " "
									+ "Expected URL is  :" + testData[i][4]);					
				}				
				
				if (testType.equalsIgnoreCase("Web"))
				{				
					ActualURL = ConnsHomePage.clickAndGetPageURLUsingJS(webPage, testData[i][1], testData[i][0],
					testData[i][5], softAssert);
					softAssert.assertTrue(ActualURL.contains(testData[i][4]), "Link Name  :" + testData[i][0] + " : failed "
						+ "Actual URL is  :" + ActualURL + " " + "Expected URL is  :" + testData[i][4]);
				}
				webPage.getDriver().get(url);
			}
			softAssert.assertAll();
		}

		catch (Throwable e) {
			mainPage.getScreenShotForFailure(webPage, "Verify_LearnMore_LinkRedirection_For_BBB_Rating_Banner");
			softAssert.assertAll();
			Assert.fail(e.getLocalizedMessage());
		}
	}

	/**
	 * Test Case - 022 - verify links under About Conns footer Conns Home Page
	 * 
	 */

	@Test(priority = 22, enabled = true, description = "Verify_LinkRedirection_Under_Footer_About_Conns_Section")
	public void Verify_LinkRedirection_Under_Footer_About_Conns_Section() throws PageException, InterruptedException {
		SoftAssert softAssert = new SoftAssert();
		String ActualURL = null;
		// webPage.getDriver().get(url);
		try {
			String[][] testData = ExcelUtil.readExcelData(DataFilePath, "Conns_Home_Page",
					"verifyFooterAboutConnsLinks");
			for (int i = 0; i < testData.length; i++) {
				
				if (testType.equalsIgnoreCase("Mobile") && (!(testData[i][3].equalsIgnoreCase("NA")))) {
					System.out.println("Inside the first if. Value of I : " + i);
					commonMethods.clickElementbyXpath(webPage, testData[i][3], softAssert);
					Thread.sleep(1000);
				}

				if (testType.equalsIgnoreCase("Mobile") && (!(testData[i][2].equalsIgnoreCase("NA")))) {
					System.out.println("Inside the 2nd if. Value of I : " + i);
					ActualURL=commonMethods.clickElementbyXpathAndGetURL(webPage, testData[i][2], softAssert);
					softAssert.assertTrue(ActualURL.contains(testData[i][4]),
							"Link Name  :" + testData[i][0] + " : failed " + "Actual URL is  :" + ActualURL + " "
									+ "Expected URL is  :" + testData[i][4]);					
				}				
				
				if (testType.equalsIgnoreCase("Web"))
				{			
					ActualURL = ConnsHomePage.clickAndGetPageURLUsingJS(webPage, testData[i][1], testData[i][0],
							testData[i][5], softAssert);
					softAssert.assertTrue(ActualURL.contains(testData[i][4]), "Link Name  :" + testData[i][0] + " : failed "
							+ "Actual URL is  :" + ActualURL + " " + "Expected URL is  :" + testData[i][4]);
				}
				webPage.getDriver().get(url);				
			}
			softAssert.assertAll();
			
		} catch (Throwable e) {
			mainPage.getScreenShotForFailure(webPage, "Verify_LinkRedirection_Under_Footer_About_Conns_Section");
			softAssert.assertAll();
			Assert.fail(e.getLocalizedMessage());
		}
	}

	/**
	 * Test Case - 023 - verify links under Customer Service footer Conns Home
	 * Page
	 * 
	 */

	@Test(priority = 23, enabled = true, description = "Verify_LinkRedirection_Under_Footer_Customer_Service_Section")
	public void Verify_LinkRedirection_Under_Footer_Customer_Service_Section()
			throws PageException, InterruptedException {
		SoftAssert softAssert = new SoftAssert();
		String ActualURL = null;
		// webPage.getDriver().get(url);
		try {

			String[][] testData = ExcelUtil.readExcelData(DataFilePath, "Conns_Home_Page",
					"verifyFooterCustomerServiceSectionLinks");
			for (int i = 0; i < testData.length; i++) {
				
				if (testType.equalsIgnoreCase("Mobile") && (!(testData[i][3].equalsIgnoreCase("NA")))) {
					System.out.println("Inside the first if. Value of I : " + i);
					commonMethods.clickElementbyXpath(webPage, testData[i][3], softAssert);
					Thread.sleep(1000);
				}

				if (testType.equalsIgnoreCase("Mobile") && (!(testData[i][2].equalsIgnoreCase("NA")))) {
					System.out.println("Inside the 2nd if. Value of I : " + i);
					ActualURL=commonMethods.clickElementbyXpathAndGetURL(webPage, testData[i][2], softAssert);
					softAssert.assertTrue(ActualURL.contains(testData[i][4]),
							"Link Name  :" + testData[i][0] + " : failed " + "Actual URL is  :" + ActualURL + " "
									+ "Expected URL is  :" + testData[i][4]);					
				}				
				
				if (testType.equalsIgnoreCase("Web"))
				{				
				ActualURL = ConnsHomePage.clickAndGetPageURLUsingJS(webPage, testData[i][1], testData[i][0],
						testData[i][5], softAssert);
				softAssert.assertTrue(ActualURL.contains(testData[i][4]), "Link Name  :" + testData[i][0] + " : failed "
						+ "Actual URL is  :" + ActualURL + " " + "Expected URL is  :" + testData[i][4]);
				}
				webPage.getDriver().get(url);
			}
			softAssert.assertAll();
		}

		catch (Throwable e) {
			mainPage.getScreenShotForFailure(webPage, "Verify_LinkRedirection_Under_Footer_Customer_Service_Section");
			softAssert.assertAll();
			Assert.fail(e.getLocalizedMessage());
		}

	}

	/**
	 * Test Case - 024 - verify Footer We Accpet Conns Home Page
	 * 
	 */

	@Test(priority = 24, enabled = true, description = "Verify_Element_Visibility_Under_We_Accept_Section")
	public void Verify_Element_Visibility_Under_We_Accept_Section() throws PageException, InterruptedException {
		SoftAssert softAssert = new SoftAssert();
		// webPage.getDriver().get(url);
		try {
			String[][] testData = ExcelUtil.readExcelData(DataFilePath, "Conns_Home_Page", "verifyFooterWeAccpet");

			boolean statusOfWeAccept = commonMethods.verifyElementisPresent(webPage, testData[0][0], softAssert);
			softAssert.assertTrue(statusOfWeAccept, "We accept images are not present on the page");
			softAssert.assertAll();
		} catch (Throwable e) {
			mainPage.getScreenShotForFailure(webPage, "Verify_Element_Visibility_Under_We_Accept_Section");
			softAssert.assertAll();
			Assert.fail(e.getLocalizedMessage());
		}
	}

	/**
	 * Test Case - 025 - verify Footer Copyright Conns Home Page
	 * 
	 */
	@Test(priority = 25, enabled = true, description = "Verify_Content_Under_Footer_Copyright_Section")
	public void Verify_Content_Under_Footer_Copyright_Section() throws PageException, InterruptedException {
		SoftAssert softAssert = new SoftAssert();
		// webPage.getDriver().get(url);
		try {
			String[][] testData = ExcelUtil.readExcelData(DataFilePath, "Conns_Home_Page", "verifyFooterCopyright");

			String homeplusText1 = commonMethods.getTextbyXpath(webPage, testData[0][0], softAssert);
			softAssert.assertEquals(homeplusText1, testData[0][1]);
		} catch (Throwable e) {
			mainPage.getScreenShotForFailure(webPage, "Verify_Content_Under_Footer_Copyright_Section");
			softAssert.assertAll();
			Assert.fail(e.getLocalizedMessage());
		}

	}

}