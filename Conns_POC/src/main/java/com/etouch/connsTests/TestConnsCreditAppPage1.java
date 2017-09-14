package com.etouch.connsTests;

import java.awt.AWTException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.SkipException;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import com.etouch.common.BaseTest;
import com.etouch.common.TafExecutor;
import com.etouch.connsPages.ConnsMainPage;
import com.etouch.taf.core.TestBed;
import com.etouch.taf.core.TestBedManager;
import com.etouch.taf.core.config.TestBedManagerConfiguration;
import com.etouch.taf.core.datamanager.excel.TafExcelDataProvider;
import com.etouch.taf.core.datamanager.excel.TestParameters;
import com.etouch.taf.core.datamanager.excel.annotations.IExcelDataFiles;
import com.etouch.taf.core.datamanager.excel.annotations.ITafExcelDataProviderInputs;
import com.etouch.taf.core.exception.PageException;
//import com.etouch.taf.tools.rally.SpecializedScreenRecorder;
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

@Test(groups = "YesMoneyCreditApplication")
@IExcelDataFiles(excelDataFiles = { "CreditAppData=testData" })
public class TestConnsCreditAppPage1 extends BaseTest {
	static String platform;
	static Log log = LogUtil.getLog(TestConnsCreditAppPage1.class);
	static String AbsolutePath= TafExecutor.class.getProtectionDomain().getCodeSource().getLocation().getPath();
	static String  videoLocation = AbsolutePath.substring(0,AbsolutePath.indexOf("/target/classes/")).substring(1).concat("/src/test/resources/testdata/videos");
	Logger logger = Logger.getLogger(TestConnsCreditAppPage1.class.getName());
	//private String url = "http://connsecommdev-1365connsecommdev-13655538477.us-east-1.elb.amazonaws.com/conns_rwd/yes-money-credit/application";
	private String url = "https://www.conns.com/yes-money-credit/application/";
	private WebPage webPage;
	private ConnsMainPage mainPage;
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
	String testBedName;
	TestBed testBed;

	@BeforeClass(alwaysRun = true)
	public void setUp(ITestContext context) throws InterruptedException, FileNotFoundException, IOException {
		try {

			testBedName = context.getCurrentXmlTest().getAllParameters().get("testBedName");
			CommonUtil.sop("Test bed Name is " + testBedName);
			testBed = TestBedManager.INSTANCE.getCurrentTestBeds().get(testBedName);
			
			try {
				
				platform = testBed.getPlatform().getName().toUpperCase();
				if ((platform.equals("WINDOWS")) || (platform.equals("MAC") || (platform.equals("OS X")))) {
					System.out.println("videoLocation"+videoLocation);
				//	SpecializedScreenRecorder.startVideoRecordingForDesktopBrowser(videoLocation);
				}
				else{
				}
				url = TestBedManagerConfiguration.INSTANCE.getWebConfig().getURL();
				synchronized (this) {

					mobileView = new MobileView(context);
					mainPage = new ConnsMainPage(url, webPage);

				}
			} catch (Exception e) {
				log.info("errr is " + e);
				SoftAssertor.addVerificationFailure(e.getMessage());
			}
		}

		catch (Exception e) {

			CommonUtil.sop("Error is for" + testBedName + " -----------" + e);
			SoftAssertor.addVerificationFailure(e.getMessage());
		}
	}

	@AfterTest
	public void releaseResources() throws IOException, AWTException {
		//SpecializedScreenRecorder.stopVideoRecording();
	}
	
	@Test(priority = 1)
	public void space() throws PageException
	{
		
		String elementHeight1 = webPage.findObjectByxPath("//*[@class='header_slider']").getCssValue("margin");
		String elementHeight2 =webPage.findObjectByxPath("//*[@class='home-banners']").getCssValue("margin");
		System.out.println(" ele 1 : "+elementHeight1+" ele 2 : "+elementHeight2);
	}
	/**
	 * Test Case 001 - Verify Navigation to Yes Money Credit Application Page and Verify Page title
	 * 
	 */
	@Test(dataProvider = "tafDataProvider", dataProviderClass = TafExcelDataProvider.class, priority = 1, enabled = false, description = "Verify Page Title")
	@ITafExcelDataProviderInputs(excelFile = "CreditAppData", excelsheet = "CreditApp", dataKey = "verifyPageTitle")
	public void verifyPageTitle(ITestContext context, TestParameters inputs)
			throws PageException, InterruptedException {

		String expectedPageTitle = inputs.getParamMap().get("Title");

		log.info("testing flow verifyPageTitle");

		try {

			String actualPageTitle = webPage.getPageTitle();
			Assert.assertEquals(expectedPageTitle, actualPageTitle);

		} catch (Exception e) {
			SoftAssertor.addVerificationFailure(e.getMessage());
			log.error("verifyPageTitle failed");
			log.error(e.getMessage());
		}

		log.info("Ending verifyPageTitle");

	}
	/**
	 * Test Case - 002 - Verify Font Size and Style of specified on element on Yes Money Credit Application page
	 * 
	 */

	@Test(dataProvider = "tafDataProvider", dataProviderClass = TafExcelDataProvider.class, priority = 2, enabled = false, description = "Verify Page Title")
	@ITafExcelDataProviderInputs(excelFile = "CreditAppData", excelsheet = "CreditApp", dataKey = "verifyFont")
	public void verifyFontSizeAndStyle(ITestContext context, TestParameters inputs) {
		int count = 0;
		try {

			ITafElement pageHeading = webPage.findObjectByClass(inputs.getParamMap().get("pageHeadingClass"));
			String value = pageHeading.getCssValue(inputs.getParamMap().get("fontAttribute")).replaceAll("\"", "")
					.replaceAll(" ", "").toLowerCase().trim();
			Assert.assertTrue(
					value.contains(inputs.getParamMap().get("expectedValue"))
							|| inputs.getParamMap().get("expectedValue").contains(value),
					"Verify Font Size and Style failed.!!!" + "Font Attribute name "
							+ inputs.getParamMap().get("fontAttribute") + "Actual : " + value + " and Expected :"
							+ inputs.getParamMap().get("expectedValue").trim());

		} catch (Throwable e) {
			count++;
			SoftAssertor.addVerificationFailure(e.getMessage());
			log.error("verifyFontSizeAndStyle failed");
			log.error(e.getMessage());
		}
		
		log.info("Ending verifyFontSizeAndStyle");

	}
	
	/**
	 * Test Case - 003 Verify Page Content are rendered
	 * 
	 */

	@Test(dataProvider = "tafDataProvider", dataProviderClass = TafExcelDataProvider.class, priority = 3, enabled = false, description = "Verify Page Content")
	@ITafExcelDataProviderInputs(excelFile = "CreditAppData", excelsheet = "CreditApp", dataKey = "verifyPageContent")
	public void verifyPageContent(ITestContext context, TestParameters inputs) {
		log.info("testing verifyPageContent started------>");

		boolean returnValue = false;
		String FieldName = inputs.getParamMap().get("FieldName");
		String Fieldxpath = inputs.getParamMap().get("Fieldxpath");

		try {

			returnValue = webPage.findObjectByxPath(Fieldxpath).isDisplayed();
			Assert.assertTrue(returnValue, "Verify Page content failed!!! " + FieldName + "Not rendered on page");

			log.info("testing verifyPageContent Completed------>");
		} catch (Exception e) {
			SoftAssertor.addVerificationFailure(e.getMessage());
			log.error("verifyPageContent failed");
			log.error(e.getMessage());
		}

		log.info("Ending verifyPageContent");
	}
	
	/**
	 * Test Case - 004 Verify all Link on Yes Money credit application page are functional 
	 * 
	 */
	@Test(dataProvider = "tafDataProvider", dataProviderClass = TafExcelDataProvider.class, priority = 4, enabled = false, description = "verify Link Navigation")
	@ITafExcelDataProviderInputs(excelFile = "CreditAppData", excelsheet = "CreditApp", dataKey = "verifyLinksforNewUser")
	public void verifyLinkNavigation(ITestContext context, TestParameters inputs) {
		log.info("testing verifyLinkNavigation started------>");

		String LinkText = inputs.getParamMap().get("LinkText");
		String ExpectedURL = inputs.getParamMap().get("ExpectedURL");
		String actualUrl = "";
		try {

			webPage.findObjectByLink(LinkText).click();
			webPage.sleep(10000);
			actualUrl = webPage.getCurrentUrl();
			Assert.assertTrue(actualUrl.endsWith(ExpectedURL));
			webPage.sleep(5000);
			webPage.getBackToUrl();
			webPage.sleep(4000);

			log.info("testing verifyLinkNavigation completed------>");

		} catch (Exception e) {
			SoftAssertor.addVerificationFailure(e.getMessage());
			log.error("verifyLinkNavigation failed");
			log.error(e.getMessage());
		}

	}
	/**
	 * Test Case - 005 Verify reference code field is enabled
	 * 
	 */
	@Test(dataProvider = "tafDataProvider", dataProviderClass = TafExcelDataProvider.class, priority = 5, enabled = false, description = "verify Reference Code")
	@ITafExcelDataProviderInputs(excelFile = "CreditAppData", excelsheet = "CreditApp", dataKey = "verifyReferenceCode")
	public void verifyReferenceCodeFields(ITestContext context, TestParameters inputs) {
		log.info("testing verifyReferenceCodeFields started------>");
		String ReferenceCodeID = inputs.getParamMap().get("ReferenceCodeID");
		String ReferenceCodeValue = inputs.getParamMap().get("ReferenceCodeValue");

		try {

			webPage.findObjectById(ReferenceCodeID).sendKeys(ReferenceCodeValue);
			webPage.findObjectById(ReferenceCodeID).sendKeys(Keys.TAB);
			webPage.sleep(1000);
			String actualReferenceValue = webPage.findObjectById("applicant:referencecode").getAttribute("value");
			Assert.assertEquals(actualReferenceValue, ReferenceCodeValue, "Reference code");
			log.info("testing verifyReferenceCodeFields completed------>");
		} catch (Exception e) {
			SoftAssertor.addVerificationFailure(e.getMessage());
			log.error("verifyReferenceCodeFields failed");
			log.error(e.getMessage());
		}

	}
	/**
	 * Test Case - 005 Verify error message displayed for submitting application without data
	 * 
	 */
	@Test(dataProvider = "tafDataProvider", dataProviderClass = TafExcelDataProvider.class, priority = 6, enabled = false, description = "verify Error Msg With Blank Data")
	@ITafExcelDataProviderInputs(excelFile = "CreditAppData", excelsheet = "CreditApp", dataKey = "verifyErrorMsgWithBlankData")
	public void verifySubmitWithoutData(ITestContext context, TestParameters inputs) {
		log.info("testing verifySubmitWithoutData started------>");
		String SubmitID = inputs.getParamMap().get("SubmitID");
		String FirstNameErrorMessageID = inputs.getParamMap().get("FirstNameErrorMessageID");
		String expectedErrorText = inputs.getParamMap().get("expectedErrorText");

		try {

			webPage.findObjectById(SubmitID).click();
			String actualErrorMessage = webPage.findObjectById(FirstNameErrorMessageID).getText();
			Assert.assertEquals(actualErrorMessage, expectedErrorText, "errorText");
			log.info("testing verifySubmitWithoutData Completed------>");
		} catch (Exception e) {
			SoftAssertor.addVerificationFailure(e.getMessage());
			log.error("verifySubmitWithoutData failed");
			log.error(e.getMessage());
		}

	}

	/**
	 * Test Case 007 - Verify field validation Error Message for fields with invalid data
	 * : Email,city,ZipCode,Cell Phone,Home phone,Alternate Phone,Monthly
	 * Mortage Rent,Monthly Income,Other income
	 */
	@Test(priority=7, enabled = false, description = "verify Error Msg With Blank Data")
	public void verifyFieldValidationErrorMessageWithInValidData() {
		log.info("testing verifyFieldValidationErrorMessageWithInValidData started------>");
		System.out.println("Here1");
		String xlsPath = TestBedManager.INSTANCE.getProfile().getXlsDataConfig().get("testData");
		System.out.println("Here2");
		Path path = Paths.get(xlsPath);
		System.out.println(path.toAbsolutePath().toString());
		String[][] test= ExcelUtil.readExcelData(path.toAbsolutePath().toString(), "CreditApp", "verifyFieldValidation");
		String elementID=null;
		String inputData=null;
		String errorMsgPath=null;
		String expcErrorMsg=null;
		for(int r=0; r<test.length; r++) {
		       for(int c=0; c<test[0].length; c++)
		           System.out.print(test[r][c] + " ");
		       System.out.println();
		    }
		
		for(int r=0; r<test.length; r++) {
			
			elementID = test[r][0];
			inputData = test[r][1];
			errorMsgPath = test[r][2];
			expcErrorMsg = test[r][3];
		          // System.out.print(test[r][c] + " ");
		       System.out.println();
		       try {

					webPage.findObjectById(elementID).sendKeys(inputData);
					webPage.findObjectById(elementID).sendKeys(Keys.TAB);
					String actualErrorMessage = webPage.findObjectById(errorMsgPath).getText();
					Assert.assertEquals(actualErrorMessage, expcErrorMsg, "errorText");

				} catch (Throwable e) {

					SoftAssertor.addVerificationFailure(e.getMessage());
					log.error("verifyFieldValidationErrorMessageWithInValidData failed");
					log.error(e.getMessage());

				}
		       
		       
		    }
		
	/*	String elementID = inputs.getParamMap().get("ElementID");
		String inputData = inputs.getParamMap().get("TestData");
		String errorMsgPath = inputs.getParamMap().get("ExpectedErrorMsgXpath");
		String expcErrorMsg = inputs.getParamMap().get("ExpectedErrorMsg");
*/
		

	}

	boolean submitFailedForNewUser = false;
	/**
	 * Test Case - 008 Verify user is successfully able to submit from after entering valid data in all mandatory fields
	 */
	@Test(dataProvider = "tafDataProvider", dataProviderClass = TafExcelDataProvider.class, priority = 8, enabled = false, description = "verify Error Msg With Blank Data")
	@ITafExcelDataProviderInputs(excelFile = "CreditAppData", excelsheet = "CreditApp", dataKey = "VerifySuccessfulSubmit")
	public void verifyValidUserSuccessfulSubmit(ITestContext context, TestParameters inputs) {
		log.info("testing verifyValidUserSuccessfulSubmit started------>");

		try {

			webPage.getDriver().manage().deleteAllCookies();
			webPage.getDriver().get(url);
			webPage.findObjectById(inputs.getParamMap().get("FirstNameID"))
					.sendKeys(inputs.getParamMap().get("FirstNameData"));
			webPage.findObjectById(inputs.getParamMap().get("LastNameID"))
					.sendKeys(inputs.getParamMap().get("LastNameData"));
			webPage.findObjectById(inputs.getParamMap().get("EmailAddressID"))
					.sendKeys(inputs.getParamMap().get("EmailAddressData") + new Date().getTime() + "@gmail.com");
			webPage.findObjectById(inputs.getParamMap().get("IdontWantToReciveSpecialOfferID")).click();

			webPage.findObjectById(inputs.getParamMap().get("PhysicalStreelAddressID"))
					.sendKeys(inputs.getParamMap().get("PhysicalStreelAddressData"));
			webPage.findObjectById(inputs.getParamMap().get("ApartmentID"))
					.sendKeys(inputs.getParamMap().get("ApartmentData"));
			webPage.findObjectById(inputs.getParamMap().get("OwnRentID")).click();

			log.info("Clicked on OwnRentID ");
			webPage.findObjectById(inputs.getParamMap().get("YearsThereID"))
					.sendKeys(inputs.getParamMap().get("YearsThereData"));
			log.info("Clicked on YearsThereID ");
			webPage.findObjectById(inputs.getParamMap().get("YearsThereID")).sendKeys(Keys.TAB);
			webPage.findObjectById(inputs.getParamMap().get("MonthlyRentID"))
					.sendKeys(inputs.getParamMap().get("MonthlyRentData"));
			webPage.findObjectById(inputs.getParamMap().get("ZipCodeID"))
					.sendKeys(inputs.getParamMap().get("ZipCodeData"));
			webPage.findObjectById(inputs.getParamMap().get("DOBMonthID"))
					.sendKeys(inputs.getParamMap().get("DOBMonthData"));
			webPage.findObjectById(inputs.getParamMap().get("DOBDayID"))
					.sendKeys(inputs.getParamMap().get("DOBDayData"));
			webPage.findObjectById(inputs.getParamMap().get("DOBYearID"))
					.selectDropDownList(inputs.getParamMap().get("DOBYearData"));
			webPage.findObjectById(inputs.getParamMap().get("CellPhoneID"))
					.sendKeys(inputs.getParamMap().get("CellphoneData"));
			webPage.findObjectById(inputs.getParamMap().get("HomePhoneID"))
					.sendKeys(inputs.getParamMap().get("HomePhoneData"));
			webPage.findObjectById(inputs.getParamMap().get("AlternatePhoneID"))
					.sendKeys(inputs.getParamMap().get("AlternatePhoneData"));
			log.info("AlternatePhoneID Entered ");
			webPage.findObjectByName(inputs.getParamMap().get("DriverLicenseNumberName"))
					.sendKeys(inputs.getParamMap().get("DriverLicenseNumberData"));
			webPage.findObjectByName(inputs.getParamMap().get("IssuingStateName"))
					.sendKeys(inputs.getParamMap().get("IssuingStateData"));
			webPage.findObjectById(inputs.getParamMap().get("SocialSecurityNumberID"))
					.sendKeys(inputs.getParamMap().get("SocialSecurityNumberData"));
			webPage.findObjectById(inputs.getParamMap().get("MainSourceOfIncomeID"))
					.selectDropDownList(inputs.getParamMap().get("MainSourceOfIncomeData"));
			log.info("MainSourceOfIncomeID Entered ");
			webPage.sleep(2000);
			webPage.findObjectById(inputs.getParamMap().get("MonthlyIncomeID")).clear();
			webPage.findObjectById(inputs.getParamMap().get("MonthlyIncomeID"))
					.sendKeys(inputs.getParamMap().get("MonthlyIncomeData"));
			log.info("MonthlyIncomeID Entered ");
			webPage.findObjectById(inputs.getParamMap().get("PresentEmployerID"))
					.sendKeys(inputs.getParamMap().get("PresentEmployerData"));
			webPage.findObjectById(inputs.getParamMap().get("NumberOfYearsThereID"))
					.sendKeys(inputs.getParamMap().get("NumberOfYearsData"));
			log.info("NumberOfYearsThereID Entered ");
			webPage.sleep(2000);
			webPage.findObjectById(inputs.getParamMap().get("WorkPhoneID"))
					.sendKeys(inputs.getParamMap().get("WorkPhoneData"));

			webPage.findObjectById(inputs.getParamMap().get("CreatePasswordID"))
					.sendKeys(inputs.getParamMap().get("CreatePasswordData"));
			webPage.findObjectById(inputs.getParamMap().get("ConfirmPasswordID"))
					.sendKeys(inputs.getParamMap().get("ConfirmPasswordData"));
			log.info("ConfirmPasswordID Entered ");
			webPage.findObjectById(inputs.getParamMap().get("TermsConditionID")).click();
			webPage.findObjectById(inputs.getParamMap().get("SubmitID")).click();
			webPage.sleep(8000);
			String currentURL = webPage.getCurrentUrl();
			log.info("After Registration Link URL:" + currentURL);
			Assert.assertTrue(currentURL.endsWith(inputs.getParamMap().get("SuccessLink")),
					"User Registration failed.");

			log.info("testing flow verifyValidUserSuccessfulSubmit Completed");

		} catch (Exception e) {
			submitFailedForNewUser = true;
			SoftAssertor.addVerificationFailure(e.getMessage());
			log.error("verifyValidUserSuccessfulSubmit failed");
			log.error(e.getMessage());
		}

	}
	
	/**
	 * Test Case - 009 Verify Start Shopping Link is displayed after user successfully submits Yes Money Credit Application form
	 */
	@Test(dataProvider = "tafDataProvider", dataProviderClass = TafExcelDataProvider.class, priority = 9, enabled = false, description = "verify Error Msg With Blank Data")
	@ITafExcelDataProviderInputs(excelFile = "CreditAppData", excelsheet = "CreditApp", dataKey = "VerifyStartShoppingLink")
	public void VerifyStartShoppingLinkOnSuccessPage(ITestContext context, TestParameters inputs) {
		log.info("testing flow VerifyStartShoppingLinkOnSuccessPage Started & submitFailedForNewUser:"
				+ submitFailedForNewUser);

		if (submitFailedForNewUser)
			throw new SkipException("This TC is skipped as Failed to Submit the applicationform for New user");
		try {

			webPage.findObjectByxPath(inputs.getParamMap().get("startShoppingButtonXpath")).click();
			webPage.sleep(10000);
			String currentURL = webPage.getCurrentUrl();
			log.info("Shopping Link URL:" + currentURL);
			String expectedText = inputs.getParamMap().get("ShoppingLink");
			Assert.assertTrue(currentURL.endsWith(expectedText), "Shopping page failed.");

			log.info("testing flow VerifyStartShoppingLinkOnSuccessPage Completed");
		} catch (Throwable e) {
			SoftAssertor.addVerificationFailure(e.getMessage());
			log.error("VerifyStartShoppingLinkOnSuccessPage failed");
			log.error(e.getMessage());
		}

	}

	boolean loginFailedForRegisteredUser = false;
	/**
	 * Test Case - 005 Verify registerd user is able to login successfully with valid data
	 * 
	 */
	@Test(dataProvider = "tafDataProvider", dataProviderClass = TafExcelDataProvider.class, priority = 10, enabled = false, description = "verify Login For Registered User")
	@ITafExcelDataProviderInputs(excelFile = "CreditAppData", excelsheet = "CreditApp", dataKey = "verifyLoginForRegisteredUser")
	public void verifyLoginRegisteredUser(ITestContext context, TestParameters inputs) {

		log.info("testing verifyLoginRegisteredUser started------>");
		
		try {
			webPage.getDriver().manage().deleteAllCookies();
			webPage.getDriver().get(url);
			webPage.findObjectByLink(inputs.getParamMap().get("LinkText")).click();
			webPage.sleep(8000);
			webPage.findObjectById(inputs.getParamMap().get("emailTextFieldID")).sendKeys(inputs.getParamMap().get("registeredemail"));
			webPage.findObjectById(inputs.getParamMap().get("PasswordFieldID")).sendKeys(inputs.getParamMap().get("registeredpassword"));
			webPage.findObjectById(inputs.getParamMap().get("LoginbuttonID")).click();
			webPage.sleep(12000);
			boolean returnValue = webPage.findObjectByLink(inputs.getParamMap().get("ChangepasswordLinkText")).isDisplayed();
			Assert.assertTrue(returnValue,"Verify Page content failed!!! " + inputs.getParamMap().get("ChangepasswordLinkText") + "Not rendered on page");

		} catch (Exception e) {

			SoftAssertor.addVerificationFailure(e.getMessage());
			log.error("verifyLoginRegisteredUser failed");
			log.error(e.getMessage());
		}

	}

	/**
	 * Test Case 011 - Verify auto Populates Form Fields for RegisteredUser
	 */
	@Test(dataProvider = "tafDataProvider", dataProviderClass = TafExcelDataProvider.class, priority = 11, enabled = false, description = "Verify Page Title")
	@ITafExcelDataProviderInputs(excelFile = "CreditAppData", excelsheet = "CreditApp", dataKey = "verifyAutoPopulateFieldRU")
	public void verifyAutoPopulatesFormFieldsForRegisteredUser(ITestContext context, TestParameters inputs) {
		log.info("testing flow verifyPageTitle");

		if (loginFailedForRegisteredUser)
			throw new SkipException("This TC is skipped as Failed to Login Registered user");
		try {

			//boolean returnValue = false;
			webPage.getDriver().get(url);
			webPage.sleep(5000);
		//	returnValue = verifyAutoPopulatesFormFieldsForRegisteredUser(inputs);
			

			List<ITafElement> textFields = webPage.findObjectsByCss(inputs.getParamMap().get("AllTextFieldCSSForRU"));
			List<String> populatedElement = new ArrayList<String>();
			for (ITafElement ele : textFields) {
				String value = ele.getAttribute("value");
				String id = ele.getAttribute("id");

				if (id.contains(inputs.getParamMap().get("FNameID")) || id.contains(inputs.getParamMap().get("LNameID"))
						|| id.contains(inputs.getParamMap().get("EmailID"))
						|| id.contains(inputs.getParamMap().get("NewsLetteID"))) {
					if (value.length() < 1)
						populatedElement.add(id);
					break;

				}

				if (value.length() > 0)
					populatedElement.add(id);
			}
			
			Assert.assertTrue(populatedElement.isEmpty(), "Verify Auto Populates Form Fields For Registered User failed.!!!");
			} catch (Exception e) {
			SoftAssertor.addVerificationFailure(e.getMessage());
			log.error("verifyLoginRegisteredUser failed");
			log.error(e.getMessage());
		}

	}

	/**
	 * Test Case 012 - Verify first last name fields are editable for registered user
	 */
	@Test(dataProvider = "tafDataProvider", dataProviderClass = TafExcelDataProvider.class, priority = 12, enabled = false, description = "Verify Page Title")
	@ITafExcelDataProviderInputs(excelFile = "CreditAppData", excelsheet = "CreditApp", dataKey = "verifyEditableFieldRU")
	public void verifyFirstNameLastNameIsEditableForExistingRegisteredUser(TestParameters inputs) {
		log.info("testing flow verifyFirstNameLastNameIsEditableForRegisteredUser");
		if (loginFailedForRegisteredUser)
			throw new SkipException("This TC is skipped as Failed to Login Registered user");
		try {
			boolean returnValue = false;
			ITafElement firstName = webPage.findObjectById(inputs.getParamMap().get("FNameID"));
			firstName.clear();
			firstName.sendKeys(inputs.getParamMap().get("FNameEditText"));
			webPage.sleep(3000);
			ITafElement lastName = webPage.findObjectById(inputs.getParamMap().get("LNameID"));
			lastName.clear();
			lastName.sendKeys(inputs.getParamMap().get("LNameEditText"));
			lastName.sendKeys(Keys.TAB);
			webPage.sleep(3000);

			String firstNameEditValue = webPage.findObjectById(inputs.getParamMap().get("FNameID")).getAttribute("value");
			String lastNameEditValue = webPage.findObjectById(inputs.getParamMap().get("LNameID")).getAttribute("value");
			
			Assert.assertTrue(firstNameEditValue.equals(inputs.getParamMap().get("FNameEditText")) && lastNameEditValue.equals(inputs.getParamMap().get("LNameEditText")), "Verify First Name Last Name Is Editable For Registered User failed.!!!!!");
			log.info("testing flow verifyFirstNameLastNameIsEditableForRegisteredUser Completed");
		} catch (Exception e) {
			log.info("verifyFirstNameLastNameIsEditableForRegisteredUser method as an exception");
			log.info(e.getMessage());
			e.printStackTrace();
			Assert.fail();
		}
	}

	/**
	 * Test Case 013 - Verify email field is not editable for registered user
	 */
	@Test(dataProvider = "tafDataProvider", dataProviderClass = TafExcelDataProvider.class, priority = 13, enabled = false, description = "Verify email field is not editable for registered user")
	@ITafExcelDataProviderInputs(excelFile = "CreditAppData", excelsheet = "CreditApp", dataKey = "verifyEmailAddNotEditRU")
	public void verifyEmailAddIsNotEditableForForExistingRegisteredUser(TestParameters inputs) {
		log.info("testing flow verifyEmailAddIsNotEditableForRegisteredUser started");
		if (loginFailedForRegisteredUser)
			throw new SkipException("This TC is skipped as Failed to Login Registered user");
		try {
									
			ITafElement emailAdd = webPage.findObjectById(inputs.getParamMap().get("EmailID"));
			String emailAddValue = emailAdd.getAttribute("value");
			emailAdd.sendKeys(inputs.getParamMap().get("EmailIDEditText"));
			webPage.sleep(3000);
			String editEmailAddValue = webPage.findObjectById(inputs.getParamMap().get("EmailID")).getAttribute("value");
			
			Assert.assertTrue(emailAddValue.equals(editEmailAddValue), "Verify Email Add Is Not Editable For Registered User failed.!!!!");
			log.info("testing flow verifyEmailAddIsNotEditableForRegisteredUser Completed");
		} catch (Exception e) {
			log.info("verifyEmailAddIsNotEditableForRegisteredUser method as an exception");
			log.info(e.getMessage());
			e.printStackTrace();
			Assert.fail();
		}
	}

	/**
	 * Test Case 014 - Verify Sign Link link is not rendered on form for registered user
	 */
	@Test(dataProvider = "tafDataProvider", dataProviderClass = TafExcelDataProvider.class, priority = 14, enabled = false, description = "Verify Sign Link link is not  rendered on form  for registered user")
	@ITafExcelDataProviderInputs(excelFile = "CreditAppData", excelsheet = "CreditApp", dataKey = "verifySignInLinkRenderForRU")
	public void verifySignInLinksNotRenderedForForExistingRegisteredUser(TestParameters inputs) {
		log.info("testing flow verifySignInLinksNotRenderedForRegisteredUser started");
		if (loginFailedForRegisteredUser)
			throw new SkipException("This TC is skipped as Failed to Login Registered user");
		boolean returnValue = false;
		try {
			returnValue = verifySignInLinkNotRenderedForRegisteredUser(inputs);
			Assert.assertTrue(returnValue,
					"Verify Sign Link link is not  rendered on form for registered user failed.!!!");
			log.info("testing flow verifySignInLinksNotRenderedForRegisteredUser Completed");
		} catch (Exception e) {
			log.info("verifySignInLinksNotRenderedForRegisteredUser method as an exception");
			log.info(e.getMessage());
			e.printStackTrace();
			Assert.fail();
		}
	}

	/**
	 *  Verify Create And Confirm Password Field is Not Rendered For registered user
	 * @param inputs
	 * @return
	 */
	public boolean verifySignInLinkNotRenderedForRegisteredUser(TestParameters inputs) {
		try {

			ITafElement signInLink = webPage.findObjectByLink(inputs.getParamMap().get("SingnInLinkText"));
			return signInLink.isDisplayed() ? false : true;

		} catch (PageException e) {
			return true;
		}
	}

}
