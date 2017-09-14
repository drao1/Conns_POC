package com.etouch.connsTests;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.etouch.common.BaseTest;
import com.etouch.common.TafExecutor;
import com.etouch.connsPages.ConnsAccountAndSignInPage;
import com.etouch.connsPages.ConnsMainPage;
import com.etouch.taf.core.TestBed;
import com.etouch.taf.core.TestBedManager;
import com.etouch.taf.core.config.TestBedManagerConfiguration;
import com.etouch.taf.core.datamanager.excel.TafExcelDataProvider;
import com.etouch.taf.core.datamanager.excel.TestParameters;
import com.etouch.taf.core.datamanager.excel.annotations.IExcelDataFiles;
import com.etouch.taf.core.datamanager.excel.annotations.ITafExcelDataProviderInputs;
import com.etouch.taf.core.exception.PageException;
import com.etouch.taf.tools.rally.VideoRecorder;
import com.etouch.taf.util.CommonUtil;
import com.etouch.taf.util.ExcelUtil;
import com.etouch.taf.util.LogUtil;
import com.etouch.taf.util.SoftAssertor;
import com.etouch.taf.util.TafPassword;
import com.etouch.taf.webui.ITafElement;
import com.etouch.taf.webui.selenium.MobileView;
import com.etouch.taf.webui.selenium.WebPage;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.TouchAction;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;

@IExcelDataFiles(excelDataFiles = { "ConnsAccountSignINData=testData" })
public class TestConnsAccountAndSignInPage extends BaseTest {
	
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
	// private static final String DEFECT_PROP = null;

	// required for rally
	/** The Constant DEFECT_PROP. */

	/** The Constant STORY_ID. */
	// private static final String STORY_ID = null;

	/** The project id. */
	String PROJECT_ID = TestBedManager.INSTANCE.getDefectConfig().getProjectId();

	/** The defect owner. */
	String DEFECT_OWNER = TestBedManager.INSTANCE.getDefectConfig().getDefectOwner();

	/** The workspace id. */
	String WORKSPACE_ID = TestBedManager.INSTANCE.getDefectConfig().getWorkspaceId();
	VideoRecorder videoRecorder = null;
	AppiumDriver driver = null;
	MobileView mobileView = null;
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
	int j = 0;

	static Log log = LogUtil.getLog(TestConnsAccountAndSignInPage.class);
	Logger logger = Logger.getLogger(ConnsAccountAndSignInPage.class.getName());
	private final int MAX_WAIT = 20;

	private String url;
	private String PageUrl = "";
	private WebPage webPage;
	private ConnsAccountAndSignInPage ConnsSignInPage;
	static String platform;
	static String AbsolutePath = TafExecutor.class.getProtectionDomain().getCodeSource().getLocation().getPath();
	static String videoLocation = AbsolutePath.substring(0, AbsolutePath.indexOf("/target/classes/")).substring(1)
			.concat("/src/test/resources/testdata/videos");

	/*
	 * public static String var_ObjType; public static String var_Xpath; public
	 * static String var_Overlay_Close_Css;
	 */

	/* public static String LandingPage; */

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
			log.info("Test Type is : " + testType);
			try {

				platform = testBed.getPlatform().getName().toUpperCase();
				if (testType.equalsIgnoreCase("Web")) {
					log.info("videoLocation" + videoLocation);

					// SpecializedScreenRecorder.startVideoRecordingForDesktopBrowser(videoLocation);
				} else {
				}
				path = Paths.get(TestBedManager.INSTANCE.getProfile().getXlsDataConfig().get("testData"));
				DataFilePath = path.toAbsolutePath().toString();
				url = TestBedManagerConfiguration.INSTANCE.getWebConfig().getURL();
				String[][] test = ExcelUtil.readExcelData(DataFilePath, "AccountSignINPage", "PageURL");
				url = TestBedManagerConfiguration.INSTANCE.getWebConfig().getURL() + test[0][0];

				synchronized (this) {

					webPage = new WebPage(context);
					ConnsSignInPage = new ConnsAccountAndSignInPage(url, webPage);
					// connsHomepage=new ConnsHomePageNew;

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

	/**
	 * Test Case - 001 - Verify title and URL of Login page
	 * 
	 */

	@Test(priority = 1, enabled = true)
	public void verifyLoginPageTitle() {
		log.info("************ Stated title verification of Login Page*******************");
		String[][] testdata = ExcelUtil.readExcelData(DataFilePath, "AccountSignINPage", "verifyLoginPageTitle");
		ConnsSignInPage.verifyPageTitle(testdata);

	}

	/**
	 * Test Case - 002 - Verify font on login page
	 * 
	 */

	@Test(dataProvider = "tafDataProvider", dataProviderClass = TafExcelDataProvider.class, priority = 2, enabled = false, description = "Verify FontandSize")
	@ITafExcelDataProviderInputs(excelFile = "ConnsAccountSignINData", excelsheet = "AccountSignINPage", dataKey = "verifyFontandSizeOnLoginPage")
	public void verifyFontandSizeOnLoginPage(ITestContext context, TestParameters inputs)
			throws PageException, InterruptedException {
		Thread.sleep(2000);
		log.info("**************Started Font and Size of content verification on Login Page *******************************");
		int index = 0;
		try {
			List<String> ActualValues = ConnsSignInPage.getInfo(inputs.getParamMap().get("Locator"),
					inputs.getParamMap().get("IsAltPresent"), inputs.getParamMap().get("ObjType"),
					inputs.getParamMap().get("isTitlePresent"));
			List<String> exp_pageText = ConnsSignInPage.getExpectedInfo(inputs.getParamMap().get("TextAttributes"),
					testBedName);
			ConnsSignInPage.verifyGivenValues(ActualValues, exp_pageText, "verifying font of" + index + "element");
		} catch (Exception e) {
			log.error(">--------------Test case verify font on login page failed -------------<" + e.getMessage());
			SoftAssertor.addVerificationFailure(e.getMessage());
			e.printStackTrace();
		}

		finally {
			index++;
		}
		log.info("Verification of font and size completed on Login Page");
	}

	/**
	 * Test Case - 003 - Verify content on Login Page
	 * 
	 */

	@Test(priority = 3, enabled = true)
	public void verifyContentOnLoginPage() {
		log.info("**************Started Content verification on Login Page *******************");
		String[][] testdata = ExcelUtil.readExcelData(DataFilePath, "AccountSignINPage", "verifyContentOnLoginPage");
		ConnsSignInPage.verifyContent(testdata);

	}

	/**
	 * Test Case - 004 - Verify WhatsThisOverlay on login page
	 * 
	 */

	@Test(priority = 4, enabled = true)
	public void verifyWhatsThisOverlay() {
		log.info("**************Started verification of What's this overlay on Login Page *******************");
		String[][] testdata = ExcelUtil.readExcelData(DataFilePath, "AccountSignINPage", "verifyWhatsThisOverlay");
		ConnsSignInPage.WhatsThisOverlay(testdata);

	}
	
	/**
	 * Test Case - 005 - Verify links on Login Page
	 * 
	 */

	@Test(priority = 5, enabled = true)
	public void verifylinksOnLoginPage() {
		log.info("**************Started verification of Links on Login Page *******************");
		String[][] testdata = ExcelUtil.readExcelData(DataFilePath, "AccountSignINPage", "verifylinksOnLoginPage");
		ConnsSignInPage.verifyLinks(testdata);

	}

	/**
	 * Test Case - 006 - Verify login functionality with Invalid Inputs
	 * 
	 */

	@Test(priority = 6, enabled = false)
	public void verifyLoginfuncInvalidInput() {
		log.info("**************Started verification of Negative testing on Login Page *******************");
		String[][] testdata = ExcelUtil.readExcelData(DataFilePath, "AccountSignINPage", "verifyLoginfuncInvalidInput");
		ConnsSignInPage.LoginfuncInvalidInput(testdata);

	}
	
	/**
	 * Test Case - 007 - Verify forgot password page
	 * 
	 * 
	 */

	@Test(priority = 7, enabled = false)
	public void verifyforgotPwdPage() {
		log.info("******Started verification of title on Forgot Password Page ********");
		String[][] testdata = ExcelUtil.readExcelData(DataFilePath, "AccountSignINPage", "ForgotPasswordPageTitle");
		ConnsSignInPage.ForgotPasswordPageTitle(testdata);
		log.info("******Started verification of content on Forgot Password Page *********");
		String[][] inputdata = ExcelUtil.readExcelData(DataFilePath, "AccountSignINPage", "verifycontentonforgotPwdPage");
		ConnsSignInPage.verifyContent(inputdata);	
		log.info("******Started verification of Links on Forgot Password Page ********");
		String[][] data = ExcelUtil.readExcelData(DataFilePath, "AccountSignINPage", "verifyLinksOnforgotPwdPage");
		ConnsSignInPage.verifyLinks(data);
		log.info("******Started verification of Forgot Password functionality with Invalid and valid data ********");
		String[][] testingdata = ExcelUtil.readExcelData(DataFilePath, "AccountSignINPage", "forgotPasswordFucwithInvaliddata");
		ConnsSignInPage.verfifyforgotPasswordFucwithInvaliddata(testingdata);
	}

	/**
	 * Test Case - 008 - Verify Login functionality with Valid Input
	 * 
	 */

	@Test(priority = 8, enabled = false)
	public void verifyLoginFunctionality() {
		log.info("******Started verification of Login functionality with valid data ********");
		String[][] testdata = ExcelUtil.readExcelData(DataFilePath, "AccountSignINPage", "verifyLoginFunctionality");
		ConnsSignInPage.verifyLoginFunctionality(testdata);

	}

	/**
	 * Test Case - 009 - Verify content in Account Information section
	 * 
	 */

	@Test(priority = 9, enabled = false)
	public void verifyAccountDashBoardContent() {
		log.info("******Started verification of content on Account Dashborad tab after login ********");
		String[][] testdata = ExcelUtil.readExcelData(DataFilePath, "AccountSignINPage",
				"verifyAccountInformationSection");
		ConnsSignInPage.verifyContent(testdata);

	}

	/**
	 * Test Case - 010 - Verify links in Account Information section
	 * 
	 */

	@Test(priority = 10, enabled = false)
	public void verifyLinksOnAccountDashBoardTab() {
		log.info("******Started verification of Links in Account Dashborad tab after login ********");
		String[][] testdata = ExcelUtil.readExcelData(DataFilePath, "AccountSignINPage",
				"verifyLinksOnAccountInformationSec");
		ConnsSignInPage.verifyLinks(testdata);

	}

	/**
	 * Test Case - 011 - Verify content in Credit Application section on Account Dashboard tab
	 * 
	 */

	@Test(priority = 11, enabled = false)
	public void VerifyCreditApplicationSec() {
		log.info("******Started verification of credit Application sec on Account Dashborad tab after login ********");
		String[][] testdata = ExcelUtil.readExcelData(DataFilePath, "AccountSignINPage", "VerifyCreditApplicationSec");
		ConnsSignInPage.verifyContent(testdata);

	}
	/**
	 * Test Case - 012 - Verify AccountInformationTab and Change Password functionality
	 * @throws PageException 
	 * 
	 */

	@Test(priority = 12, enabled = false)
	public void VerifyAccountInformationTab() throws PageException{
		String[][] testdata = ExcelUtil.readExcelData(DataFilePath, "AccountSignINPage",
				"VerifyAccountInformationForm");
		log.info("*************Clicking on Account Information tab after login************ ");
		ConnsSignInPage.ClickonButton(testdata);
		log.info("************* Started Account Information form verification with valid and invalid data after login************ ");
		ConnsSignInPage.VerifyValidationMsg(testdata);
		String[][] inputdata = ExcelUtil.readExcelData(DataFilePath, "AccountSignINPage", "VerifyChangePasswordfun");
		log.info("************* Started Change Password forn verification with valid and invalid data after login************ ");
		ConnsSignInPage.VerifyChangePasswordfun(inputdata);
		String[][] data = ExcelUtil.readExcelData(DataFilePath, "AccountSignINPage", "LinkonAccountInformation");
		log.info("************* Started links verification in Account Infromation tab after login************ ");
		ConnsSignInPage.verifyLinks(data);
	}
	
	/**
	 * Test Case - 013 - Verify Address Book Tab functionality
	 * 
	 */

	@Test(priority = 13, enabled = false)
	public void verifyAddressBookTab() throws PageException {
		String[][] testdata = ExcelUtil.readExcelData(DataFilePath, "AccountSignINPage", "verifyAddingNewAddress");

		log.info("************* Started verification of validation message on New Address Page after login************ ");
		ConnsSignInPage.verifyValidationMessageforNewAddressform(testdata);

		log.info("************* Started verification of New Address Page form with valid data after login************ ");
		String[][] data = ExcelUtil.readExcelData(DataFilePath, "AccountSignINPage", "verifyValidAddressform");
		ConnsSignInPage.verifyValidformSubmission(data);

		String[][] inputdata = ExcelUtil.readExcelData(DataFilePath, "AccountSignINPage", "verifylinksonAddresspage");
		log.info("************* Clikcing on Save Address button on Add New Address form ************ ");
		ConnsSignInPage.verifyLinks(inputdata);
		log.info("************* Verifying Success Message once New addess has been added ************ ");
		String[][] testingdata = ExcelUtil.readExcelData(DataFilePath,"AccountSignINPage", "verifySuccessMessage");
		//ConnsSignInPage.verifyContent(testingdata);
		log.info("************* Verifying links on Address Book ************ ");
		String[][] QAdata = ExcelUtil.readExcelData(DataFilePath, "AccountSignINPage", "verifylinksonAddressBookPage");
		//ConnsSignInPage.verifyLinks(QAdata);

	}

	/**
	 * Test Case - 014 - Verify News Letter Tab functionality
	 * 
	 */
	@Test(priority = 14, enabled = false)
	public void verifyNewsletterTab() throws PageException {
		log.info("******Started verification of NewsletterTab after login ********");
		String[][] testdata = ExcelUtil.readExcelData(DataFilePath, "AccountSignINPage", "verifyNewsletterTab");
		ConnsSignInPage.verifyLinks(testdata);
		

	}

}