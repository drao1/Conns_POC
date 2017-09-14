package com.etouch.connsTests;

import java.awt.AWTException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.log4j.Logger;
import org.openqa.selenium.Keys;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.SkipException;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.etouch.common.BaseTest;
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
public class TestConnsCreditAppPageOG extends BaseTest {
	static String platform;
	static Log log = LogUtil.getLog(TestConnsCreditAppPageOG.class);
	Logger logger = Logger.getLogger(TestConnsCreditAppPageOG.class.getName());
	// "http://connsecommdev-1365538477.us-east-1.elb.amazonaws.com/conns_rwd/yes-money-credit/application";
	private String url = "http://connsecommdev-1365538477.us-east-1.elb.amazonaws.com/conns_rwd/yes-money-credit/application";
	private WebPage webPage;
	private ConnsMainPage mainPage;
	VideoRecorder videoRecorder = null;
	AppiumDriver driver = null;
	MobileView mobileView = null;
	TouchAction act1;
	DesiredCapabilities capabilities;
	String videoLocation = "..\\src\\test\\resources\\testdata\\videos";
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
			Thread.sleep(2000);
			platform = testBed.getPlatform().getName().toUpperCase();
			if ((platform.equals("WINDOWS")) || (platform.equals("MAC") || (platform.equals("OS X")))) {

				try {
					//SpecializedScreenRecorder.startVideoRecordingForDesktopBrowser(videoLocation);
					url = TestBedManagerConfiguration.INSTANCE.getWebConfig().getURL();
					synchronized (this) {

						webPage = new WebPage(context);
						mainPage = new ConnsMainPage(url, webPage);
					}
				} catch (Exception e) {
					log.info("errr is " + e);
					SoftAssertor.addVerificationFailure(e.getMessage());
				}
			}

			else {
				if (testBed.getDevice().getName().startsWith("i")) {
					driver = (IOSDriver) (testBed.getDriver());

				} else {
					driver = (AndroidDriver) (testBed.getDriver());
				}
			//	SpecializedScreenRecorder.startVideoRecordingForMobile(videoLocation);
				mobileView = new MobileView(context);
				System.out.println("Browser Open started Loading page");
				mobileView.loadPage(url);
			}

		}

		catch (Exception e) {

			CommonUtil.sop("errr is for" + testBedName + " -----------" + e);
			SoftAssertor.addVerificationFailure(e.getMessage());
		}
	}

	@AfterTest
	public void releaseResources() throws IOException, AWTException {
//		SpecializedScreenRecorder.stopVideoRecording();
	}

	@Test(dataProvider = "tafDataProvider", dataProviderClass = TafExcelDataProvider.class, priority = 1, enabled = true, description = "Verify Page Title")
	@ITafExcelDataProviderInputs(excelFile = "CreditAppData", excelsheet = "CreditApp", dataKey = "verifyPageTitle")
	public void verifyPageTitle(ITestContext context, TestParameters inputs)
			throws PageException, InterruptedException {

		String expectedPageTitle = inputs.getParamMap().get("Title");

		log.info("testing flow verifyPageTitle");

		try {
			if ((platform.equals("WINDOWS")) || (platform.equals("MAC") || (platform.equals("OS X")))) {
				String actualPageTitle = webPage.getPageTitle();
				System.out.println("expectedPageTitle:" + expectedPageTitle + "   actualPageTitle:" + actualPageTitle);
				Assert.assertEquals(expectedPageTitle, actualPageTitle);
			} else {
				String actualPageTitle = mobileView.getPageTitle();
				Assert.assertEquals(expectedPageTitle, actualPageTitle);
			}

		} catch (Exception e) {
			SoftAssertor.addVerificationFailure(e.getMessage());
			log.error("verifyPageTitle failed");
			log.error(e.getMessage());
		}

		log.info("Ending verifyPageTitle");

	}

	@Test(dataProvider = "tafDataProvider", dataProviderClass = TafExcelDataProvider.class, priority = 2, enabled = true, description = "Verify Page Title")
	@ITafExcelDataProviderInputs(excelFile = "CreditAppData", excelsheet = "CreditApp", dataKey = "verifyFont")
	public void verifyFontSizeAndStyle(ITestContext context, TestParameters inputs) {

		log.info("testing flow verifyFontSizeAndStyle started");

		try {
			if ((platform.equals("WINDOWS")) || (platform.equals("MAC") || (platform.equals("OS X")))) {
				ITafElement pageHeading = webPage.findObjectByClass(inputs.getParamMap().get("pageHeadingClass"));
				String value = pageHeading.getCssValue(inputs.getParamMap().get("fontAttribute")).replaceAll("\"", "")
						.replaceAll(" ", "").toLowerCase().trim();
				Assert.assertTrue(
						value.contains(inputs.getParamMap().get("expectedValueW"))
								|| inputs.getParamMap().get("expectedValueW").contains(value),
						"Verify Font Size and Style failed.!!!" + "Font Attribute name "
								+ inputs.getParamMap().get("fontAttribute") + "Actual : " + value + " and Expected :"
								+ inputs.getParamMap().get("expectedValueW").trim());
				System.out.println("testing flow verifyFontSizeAndStyle completed");
			} else {
				ITafElement pageHeading = mobileView.findObjectByClass(inputs.getParamMap().get("pageHeadingClass"));
				String value = pageHeading.getCssValue(inputs.getParamMap().get("fontAttribute")).replaceAll("\"", "")
						.replaceAll(" ", "").toLowerCase().trim();
				Assert.assertTrue(
						value.contains(inputs.getParamMap().get("expectedValue"))
								|| inputs.getParamMap().get("expectedValue").contains(value),
						"Verify Font Size and Style failed.!!!" + "Font Attribute name "
								+ inputs.getParamMap().get("fontAttribute") + "Actual : " + value + " and Expected :"
								+ inputs.getParamMap().get("expectedValue").trim());
				System.out.println("testing flow verifyFontSizeAndStyle completed");
			}
		} catch (Throwable e) {

			SoftAssertor.addVerificationFailure(e.getMessage());
			log.error("verifyFontSizeAndStyle failed");
			log.error(e.getMessage());
		}
		
		log.info("Ending verifyFontSizeAndStyle");

	}

	@Test(dataProvider = "tafDataProvider", dataProviderClass = TafExcelDataProvider.class, priority = 3, enabled = true, description = "Verify Page Content")
	@ITafExcelDataProviderInputs(excelFile = "CreditAppData", excelsheet = "CreditApp", dataKey = "verifyPageContent")
	public void verifyPageContent(ITestContext context, TestParameters inputs) {
		log.info("testing verifyPageContent started------>");

		boolean returnValue = false;
		String FieldName = inputs.getParamMap().get("FieldName");
		String Fieldxpath = inputs.getParamMap().get("Fieldxpath");

		try {
			if ((platform.equals("WINDOWS")) || (platform.equals("MAC") || (platform.equals("OS X")))) {
				returnValue = webPage.findObjectByxPath(Fieldxpath).isDisplayed();
				Assert.assertTrue(returnValue, "Verify Page content failed!!! " + FieldName + "Not rendered on page");
			} else {
				returnValue = mobileView.findObjectByxPath(Fieldxpath).isDisplayed();
				Assert.assertTrue(returnValue, "Verify Page content failed!!! " + FieldName + "Not rendered on page");
			}
			log.info("testing verifyPageContent Completed------>");
		} catch (Exception e) {
			SoftAssertor.addVerificationFailure(e.getMessage());
			log.error("verifyPageContent failed");
			log.error(e.getMessage());
		}

		log.info("Ending verifyPageContent");
	}

	@Test(dataProvider = "tafDataProvider", dataProviderClass = TafExcelDataProvider.class, priority = 4, enabled = true, description = "verify Link Navigation")
	@ITafExcelDataProviderInputs(excelFile = "CreditAppData", excelsheet = "CreditApp", dataKey = "verifyLinksforNewUser")
	public void verifyLinkNavigation(ITestContext context, TestParameters inputs) {
		log.info("testing verifyLinkNavigation started------>");

		String LinkText = inputs.getParamMap().get("LinkText");
		String ExpectedURL = inputs.getParamMap().get("ExpectedURL");
		String actualUrl = "";
		try {
			if ((platform.equals("WINDOWS")) || (platform.equals("MAC") || (platform.equals("OS X")))) {
				webPage.findObjectByLink(LinkText).click();
				webPage.sleep(10000);
				actualUrl = webPage.getCurrentUrl();
				Assert.assertTrue(actualUrl.endsWith(ExpectedURL), "Error for" + actualUrl + "Expected" + ExpectedURL);
				webPage.sleep(5000);
				webPage.getBackToUrl();
				webPage.sleep(4000);
			} else {
				mobileView.findObjectByLink(LinkText).click();
				mobileView.sleep(10000);
				actualUrl = mobileView.getCurrentUrl();
				Assert.assertTrue(actualUrl.endsWith(ExpectedURL));
				mobileView.sleep(5000);
				mobileView.navigateBack();
				mobileView.sleep(4000);
			}

			log.info("testing verifyLinkNavigation completed------>");

		} catch (Exception e) {
			SoftAssertor.addVerificationFailure(e.getMessage());
			log.error("verifyLinkNavigation failed");
			log.error(e.getMessage());
		}
		
	}

	@Test(dataProvider = "tafDataProvider", dataProviderClass = TafExcelDataProvider.class, priority = 5, enabled = true, description = "verify Reference Code")
	@ITafExcelDataProviderInputs(excelFile = "CreditAppData", excelsheet = "CreditApp", dataKey = "verifyReferenceCode")
	public void verifyReferenceCodeFields(ITestContext context, TestParameters inputs) {
		log.info("testing verifyLinkNavigation started------>");
		String ReferenceCodeID = inputs.getParamMap().get("ReferenceCodeID");
		String ReferenceCodeValue = inputs.getParamMap().get("ReferenceCodeValue");
		

		try {
			if ((platform.equals("WINDOWS")) || (platform.equals("MAC") || (platform.equals("OS X")))) {
				webPage.findObjectById(ReferenceCodeID).sendKeys(ReferenceCodeValue);
				webPage.findObjectById(ReferenceCodeID).sendKeys(Keys.TAB);
				webPage.sleep(1000);
				String actualReferenceValue = webPage.findObjectById("applicant:referencecode").getAttribute("value");
				Assert.assertEquals(actualReferenceValue, ReferenceCodeValue, "Reference code");
			} else {
				mobileView.findObjectById(ReferenceCodeID).sendKeys(ReferenceCodeValue);
				mobileView.findObjectById(ReferenceCodeID).sendKeys(Keys.TAB);
				mobileView.sleep(1000);
				String actualReferenceValue = mobileView.findObjectById("applicant:referencecode")
						.getAttribute("value");
				Assert.assertEquals(actualReferenceValue, ReferenceCodeValue, "Reference code");
			}
			log.info("testing verifyLinkNavigation completed------>");
		} catch (Exception e) {
			SoftAssertor.addVerificationFailure(e.getMessage());
			log.error("verifyReferenceCodeFields failed");
			log.error(e.getMessage());
		}
		
	}

	@Test(dataProvider = "tafDataProvider", dataProviderClass = TafExcelDataProvider.class, priority = 6, enabled = true, description = "verify Error Msg With Blank Data")
	@ITafExcelDataProviderInputs(excelFile = "CreditAppData", excelsheet = "CreditApp", dataKey = "verifyErrorMsgWithBlankData")
	public void verifySubmitWithoutData(ITestContext context, TestParameters inputs) {
		log.info("testing verifySubmitWithoutData started------>");
		String SubmitID = inputs.getParamMap().get("SubmitID");
		String FirstNameErrorMessageID = inputs.getParamMap().get("FirstNameErrorMessageID");
		String expectedErrorText = inputs.getParamMap().get("expectedErrorText");
	

		try {

			if ((platform.equals("WINDOWS")) || (platform.equals("MAC") || (platform.equals("OS X")))) {
				webPage.findObjectById(SubmitID).click();
				String actualErrorMessage = webPage.findObjectById(FirstNameErrorMessageID).getText();
				Assert.assertEquals(actualErrorMessage, expectedErrorText, "errorText");
			} else {
				mobileView.findObjectById(SubmitID).click();
				String actualErrorMessage = mobileView.findObjectById(FirstNameErrorMessageID).getText();
				Assert.assertEquals(actualErrorMessage, expectedErrorText, "errorText");
			}
			log.info("testing verifySubmitWithoutData Completed------>");
		} catch (Exception e) {
			SoftAssertor.addVerificationFailure(e.getMessage());
			log.error("verifySubmitWithoutData failed");
			log.error(e.getMessage());
		}
		

	}

	/**
	 * TC_009 Verify field validation Error Message for fields with invalid data
	 * : Email,city,ZipCode,Cell Phone,Home phone,Alternate Phone,Monthly
	 * Mortage Rent,Monthly Income,Other income
	 */
	@Test(dataProvider = "tafDataProvider", dataProviderClass = TafExcelDataProvider.class, priority = 7, enabled = true, description = "verify Error Msg With Blank Data")
	@ITafExcelDataProviderInputs(excelFile = "CreditAppData", excelsheet = "CreditApp", dataKey = "verifyFieldValidation")
	public void verifyFieldValidationErrorMessageWithInValidData(ITestContext context, TestParameters inputs) {
		log.info("testing verifyFieldValidationErrorMessageWithInValidData started------>");

		String elementID = inputs.getParamMap().get("ElementID");
		String inputData = inputs.getParamMap().get("TestData");
		String errorMsgPath = inputs.getParamMap().get("ExpectedErrorMsgXpath");
		String expcErrorMsg = inputs.getParamMap().get("ExpectedErrorMsg");
		

		try {
			if ((platform.equals("WINDOWS")) || (platform.equals("MAC") || (platform.equals("OS X")))) {
				webPage.findObjectById(elementID).sendKeys(inputData);
				webPage.findObjectById(elementID).sendKeys(Keys.TAB);
				String actualErrorMessage = webPage.findObjectById(errorMsgPath).getText();
				Assert.assertEquals(actualErrorMessage, expcErrorMsg, "errorText");
				System.out.println("Actual Value*****" + actualErrorMessage + "Expected Error*****" + expcErrorMsg);
			} else {
				mobileView.findObjectById(elementID).sendKeys(inputData);
				mobileView.findObjectById(elementID).sendKeys(Keys.TAB);
				String actualErrorMessage = mobileView.findObjectById(errorMsgPath).getText();
				Assert.assertEquals(actualErrorMessage, expcErrorMsg, "errorText");

			}
		} catch (Throwable e) {

			SoftAssertor.addVerificationFailure(e.getMessage());
			log.error("verifyFieldValidationErrorMessageWithInValidData failed");
			log.error(e.getMessage());

		}
		
	}

	boolean submitFailedForNewUser = false;

	@Test(dataProvider = "tafDataProvider", dataProviderClass = TafExcelDataProvider.class, priority = 8, enabled = false, description = "verify Error Msg With Blank Data")
	@ITafExcelDataProviderInputs(excelFile = "CreditAppData", excelsheet = "CreditApp", dataKey = "VerifySuccessfulSubmit")
	public void verifyValidUserSuccessfulSubmit(ITestContext context, TestParameters inputs) {
		log.info("testing verifyValidUserSuccessfulSubmit started------>");
		
		try {
			if ((platform.equals("WINDOWS")) || (platform.equals("MAC") || (platform.equals("OS X")))) {
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

				System.out.println("Clicked on OwnRentID ");
				webPage.findObjectById(inputs.getParamMap().get("YearsThereID"))
						.sendKeys(inputs.getParamMap().get("YearsThereData"));
				System.out.println("Clicked on YearsThereID ");
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
				System.out.println("AlternatePhoneID Entered ");
				webPage.findObjectByName(inputs.getParamMap().get("DriverLicenseNumberName"))
						.sendKeys(inputs.getParamMap().get("DriverLicenseNumberData"));
				webPage.findObjectByName(inputs.getParamMap().get("IssuingStateName"))
						.sendKeys(inputs.getParamMap().get("IssuingStateData"));
				webPage.findObjectById(inputs.getParamMap().get("SocialSecurityNumberID"))
						.sendKeys(inputs.getParamMap().get("SocialSecurityNumberData"));
				webPage.findObjectById(inputs.getParamMap().get("MainSourceOfIncomeID"))
						.selectDropDownList(inputs.getParamMap().get("MainSourceOfIncomeData"));
				System.out.println("MainSourceOfIncomeID Entered ");
				webPage.sleep(2000);
				webPage.findObjectById(inputs.getParamMap().get("MonthlyIncomeID")).clear();
				webPage.findObjectById(inputs.getParamMap().get("MonthlyIncomeID"))
						.sendKeys(inputs.getParamMap().get("MonthlyIncomeData"));
				System.out.println("MonthlyIncomeID Entered ");
				webPage.findObjectById(inputs.getParamMap().get("PresentEmployerID"))
						.sendKeys(inputs.getParamMap().get("PresentEmployerData"));
				webPage.findObjectById(inputs.getParamMap().get("NumberOfYearsThereID"))
						.sendKeys(inputs.getParamMap().get("NumberOfYearsData"));
				System.out.println("NumberOfYearsThereID Entered ");
				webPage.sleep(2000);
				webPage.findObjectById(inputs.getParamMap().get("WorkPhoneID"))
						.sendKeys(inputs.getParamMap().get("WorkPhoneData"));

				webPage.findObjectById(inputs.getParamMap().get("CreatePasswordID"))
						.sendKeys(inputs.getParamMap().get("CreatePasswordData"));
				webPage.findObjectById(inputs.getParamMap().get("ConfirmPasswordID"))
						.sendKeys(inputs.getParamMap().get("ConfirmPasswordData"));
				System.out.println("ConfirmPasswordID Entered ");
				webPage.findObjectById(inputs.getParamMap().get("TermsConditionID")).click();
				webPage.findObjectById(inputs.getParamMap().get("SubmitID")).click();
				webPage.sleep(8000);
				String currentURL = webPage.getCurrentUrl();
				System.out.println("After Registration Link URL:" + currentURL);
				Assert.assertTrue(currentURL.endsWith(inputs.getParamMap().get("SuccessLink")),
						"User Registration failed.");
			} else {
				mobileView.getDriver().manage().deleteAllCookies();
				mobileView.getDriver().get(url);
				mobileView.findObjectById(inputs.getParamMap().get("FirstNameID"))
						.sendKeys(inputs.getParamMap().get("FirstNameData"));
				mobileView.findObjectById(inputs.getParamMap().get("LastNameID"))
						.sendKeys(inputs.getParamMap().get("LastNameData"));
				mobileView.findObjectById(inputs.getParamMap().get("EmailAddressID"))
						.sendKeys(inputs.getParamMap().get("EmailAddressData") + new Date().getTime() + "@gmail.com");
				mobileView.findObjectById(inputs.getParamMap().get("IdontWantToReciveSpecialOfferID")).click();

				mobileView.findObjectById(inputs.getParamMap().get("PhysicalStreelAddressID"))
						.sendKeys(inputs.getParamMap().get("PhysicalStreelAddressData"));
				mobileView.findObjectById(inputs.getParamMap().get("ApartmentID"))
						.sendKeys(inputs.getParamMap().get("ApartmentData"));
				mobileView.findObjectById(inputs.getParamMap().get("OwnRentID")).click();

				System.out.println("Clicked on OwnRentID ");
				mobileView.findObjectById(inputs.getParamMap().get("YearsThereID"))
						.sendKeys(inputs.getParamMap().get("YearsThereData"));
				System.out.println("Clicked on YearsThereID ");
				mobileView.findObjectById(inputs.getParamMap().get("YearsThereID")).sendKeys(Keys.TAB);
				mobileView.findObjectById(inputs.getParamMap().get("MonthlyRentID"))
						.sendKeys(inputs.getParamMap().get("MonthlyRentData"));
				mobileView.findObjectById(inputs.getParamMap().get("ZipCodeID"))
						.sendKeys(inputs.getParamMap().get("ZipCodeData"));
				mobileView.findObjectById(inputs.getParamMap().get("DOBMonthID"))
						.sendKeys(inputs.getParamMap().get("DOBMonthData"));
				mobileView.findObjectById(inputs.getParamMap().get("DOBDayID"))
						.sendKeys(inputs.getParamMap().get("DOBDayData"));
				mobileView.findObjectById(inputs.getParamMap().get("DOBYearID"))
						.selectDropDownList(inputs.getParamMap().get("DOBYearData"));
				mobileView.findObjectById(inputs.getParamMap().get("CellPhoneID"))
						.sendKeys(inputs.getParamMap().get("CellphoneData"));
				mobileView.findObjectById(inputs.getParamMap().get("HomePhoneID"))
						.sendKeys(inputs.getParamMap().get("HomePhoneData"));
				mobileView.findObjectById(inputs.getParamMap().get("AlternatePhoneID"))
						.sendKeys(inputs.getParamMap().get("AlternatePhoneData"));
				System.out.println("AlternatePhoneID Entered ");
				mobileView.findObjectByName(inputs.getParamMap().get("DriverLicenseNumberName"))
						.sendKeys(inputs.getParamMap().get("DriverLicenseNumberData"));
				mobileView.findObjectByName(inputs.getParamMap().get("IssuingStateName"))
						.sendKeys(inputs.getParamMap().get("IssuingStateData"));
				mobileView.findObjectById(inputs.getParamMap().get("SocialSecurityNumberID"))
						.sendKeys(inputs.getParamMap().get("SocialSecurityNumberData"));
				mobileView.findObjectById(inputs.getParamMap().get("MainSourceOfIncomeID"))
						.selectDropDownList(inputs.getParamMap().get("MainSourceOfIncomeData"));
				System.out.println("MainSourceOfIncomeID Entered ");
				mobileView.sleep(2000);
				mobileView.findObjectById(inputs.getParamMap().get("MonthlyIncomeID")).clear();
				mobileView.findObjectById(inputs.getParamMap().get("MonthlyIncomeID"))
						.sendKeys(inputs.getParamMap().get("MonthlyIncomeData"));
				System.out.println("MonthlyIncomeID Entered ");
				mobileView.findObjectById(inputs.getParamMap().get("PresentEmployerID"))
						.sendKeys(inputs.getParamMap().get("PresentEmployerData"));
				mobileView.findObjectById(inputs.getParamMap().get("NumberOfYearsThereID"))
						.sendKeys(inputs.getParamMap().get("NumberOfYearsData"));
				System.out.println("NumberOfYearsThereID Entered ");
				mobileView.sleep(2000);
				mobileView.findObjectById(inputs.getParamMap().get("WorkPhoneID"))
						.sendKeys(inputs.getParamMap().get("WorkPhoneData"));

				mobileView.findObjectById(inputs.getParamMap().get("CreatePasswordID"))
						.sendKeys(inputs.getParamMap().get("CreatePasswordData"));
				mobileView.findObjectById(inputs.getParamMap().get("ConfirmPasswordID"))
						.sendKeys(inputs.getParamMap().get("ConfirmPasswordData"));
				System.out.println("ConfirmPasswordID Entered ");
				mobileView.findObjectById(inputs.getParamMap().get("TermsConditionID")).click();
				mobileView.findObjectById(inputs.getParamMap().get("SubmitID")).click();
				mobileView.sleep(8000);
				String currentURL = mobileView.getCurrentUrl();
				System.out.println("After Registration Link URL:" + currentURL);
				Assert.assertTrue(currentURL.endsWith(inputs.getParamMap().get("SuccessLink")),
						"User Registration failed.");
			}

			log.info("testing flow verifyValidUserSuccessfulSubmit Completed");

		} catch (Exception e) {
			submitFailedForNewUser = true;
			SoftAssertor.addVerificationFailure(e.getMessage());
			log.error("verifyValidUserSuccessfulSubmit failed");
			log.error(e.getMessage());
		}
		
	}

	@Test(dataProvider = "tafDataProvider", dataProviderClass = TafExcelDataProvider.class, priority = 9, enabled = false, description = "verify Error Msg With Blank Data")
	@ITafExcelDataProviderInputs(excelFile = "CreditAppData", excelsheet = "CreditApp", dataKey = "VerifyStartShoppingLink")
	public void VerifyStartShoppingLinkOnSuccessPage(ITestContext context, TestParameters inputs) {
		log.info("testing flow VerifyStartShoppingLinkOnSuccessPage Started & submitFailedForNewUser:"
				+ submitFailedForNewUser);

		if (submitFailedForNewUser)
			throw new SkipException("This TC is skipped as Failed to Submit the applicationform for New user");

		try {
			if ((platform.equals("WINDOWS")) || (platform.equals("MAC") || (platform.equals("OS X")))) {
				webPage.findObjectByxPath(inputs.getParamMap().get("startShoppingButtonXpath")).click();
				webPage.sleep(10000);
				String currentURL = webPage.getCurrentUrl();
				System.out.println("Shopping Link URL:" + currentURL);
				String expectedText = inputs.getParamMap().get("ShoppingLink");
				System.out.println("expectedText in URL:" + expectedText);
				Assert.assertTrue(currentURL.endsWith(expectedText), "Shopping page failed.");
			} else {
				mobileView.findObjectByxPath(inputs.getParamMap().get("startShoppingButtonXpath")).click();
				mobileView.sleep(10000);
				String currentURL = mobileView.getCurrentUrl();
				System.out.println("Shopping Link URL:" + currentURL);
				String expectedText = inputs.getParamMap().get("ShoppingLink");
				System.out.println("expectedText in URL:" + expectedText);
				Assert.assertTrue(currentURL.endsWith(expectedText), "Shopping page failed.");
			}
			System.out.println("testing flow VerifyStartShoppingLinkOnSuccessPage Completed");
		} catch (Throwable e) {
			SoftAssertor.addVerificationFailure(e.getMessage());
			log.error("VerifyStartShoppingLinkOnSuccessPage failed");
			log.error(e.getMessage());
		}
		
	}

	boolean loginFailedForRegisteredUser = false;

	@Test(dataProvider = "tafDataProvider", dataProviderClass = TafExcelDataProvider.class, priority = 10, enabled = true, description = "verify Login For Registered User")
	@ITafExcelDataProviderInputs(excelFile = "CreditAppData", excelsheet = "CreditApp", dataKey = "verifyLoginForRegisteredUser")
	public void verifyLoginRegisteredUser(ITestContext context, TestParameters inputs) {

		log.info("testing verifyLoginRegisteredUser started------>");
		String LinkText = inputs.getParamMap().get("LinkText");
		String emailTextFieldID = inputs.getParamMap().get("emailTextFieldID");
		String PasswordFieldID = inputs.getParamMap().get("PasswordFieldID");
		String registeredemail = inputs.getParamMap().get("registeredemail");
		String registeredpassword = inputs.getParamMap().get("registeredpassword");
		String LoginbuttonID = inputs.getParamMap().get("LoginbuttonID");
		String ChangepasswordLinkText = inputs.getParamMap().get("ChangepasswordLinkText");
	

		System.out.println("LinkText: " + LinkText + "    & emailTextFieldID: " + emailTextFieldID
				+ "  PasswordFieldID: " + PasswordFieldID + "    & registeredemail: " + registeredemail
				+ "    registeredpassword: " + registeredpassword + "    & LoginbuttonID: " + LoginbuttonID
				+ "  ChangepasswordLinkText" + ChangepasswordLinkText);
		try {
			if ((platform.equals("WINDOWS")) || (platform.equals("MAC") || (platform.equals("OS X")))) {
				webPage.getDriver().manage().deleteAllCookies();
				webPage.getDriver().get(url);
				webPage.findObjectByLink(LinkText).click();
				;
				webPage.sleep(8000);
				webPage.findObjectById(emailTextFieldID).sendKeys(registeredemail);
				webPage.findObjectById(PasswordFieldID).sendKeys(registeredpassword);
				webPage.findObjectById(LoginbuttonID).click();
				webPage.sleep(12000);
				boolean returnValue = webPage.findObjectByLink(ChangepasswordLinkText).isDisplayed();
				System.out.println("Is ChangepasswordLinkText displayed----->" + returnValue);
				Assert.assertTrue(returnValue,
						"Verify Page content failed!!! " + ChangepasswordLinkText + "Not rendered on page");
			} else {
				mobileView.getDriver().manage().deleteAllCookies();
				mobileView.getDriver().get(url);
				mobileView.findObjectByLink(LinkText).click();
				mobileView.sleep(8000);
				mobileView.findObjectById(emailTextFieldID).sendKeys(registeredemail);
				mobileView.findObjectById(PasswordFieldID).sendKeys(registeredpassword);
				mobileView.findObjectById(LoginbuttonID).click();
				mobileView.sleep(12000);
				boolean returnValue = mobileView.findObjectByLink(ChangepasswordLinkText).isDisplayed();
				System.out.println("Is ChangepasswordLinkText displayed----->" + returnValue);
				Assert.assertTrue(returnValue,
						"Verify Page content failed!!! " + ChangepasswordLinkText + "Not rendered on page");
			}

		} catch (Exception e) {

			SoftAssertor.addVerificationFailure(e.getMessage());
			log.error("verifyLoginRegisteredUser failed");
			log.error(e.getMessage());
		}
		
	}

	/**
	 * TC_022 Verify auto Populates Form Fields for RegisteredUser
	 * 
	 * @param allTextFieldsXpath
	 * @param firstNameID
	 * @param lastNameID
	 * @param emailAddID
	 * @param newsLetterID
	 */
	@Test(dataProvider = "tafDataProvider", dataProviderClass = TafExcelDataProvider.class, priority = 11, enabled = true, description = "Verify Page Title")
	@ITafExcelDataProviderInputs(excelFile = "CreditAppData", excelsheet = "CreditApp", dataKey = "verifyAutoPopulateFieldRU")
	public void verifyAutoPopulatesFormFieldsForRegisteredUser(ITestContext context, TestParameters inputs) {
		log.info("testing flow verifyPageTitle");


		if (loginFailedForRegisteredUser)
			throw new SkipException("This TC is skipped as Failed to Login Registered user");
		try {
			if ((platform.equals("WINDOWS")) || (platform.equals("MAC") || (platform.equals("OS X")))) {
				boolean returnValue = false;
				webPage.getDriver().get(url);
				webPage.sleep(5000);
				returnValue = verifyAutoPopulatesFormFieldsForRegisteredUser(inputs);
				Assert.assertTrue(returnValue, "Verify Auto Populates Form Fields For Registered User failed.!!!");
				System.out.println("testing flow verifyPageTitle Completed");
			} else {
				boolean returnValue = false;
				mobileView.getDriver().get(url);
				mobileView.sleep(5000);
				returnValue = verifyAutoPopulatesFormFieldsForRegisteredUser(inputs);
				Assert.assertTrue(returnValue, "Verify Auto Populates Form Fields For Registered User failed.!!!");
				System.out.println("testing flow verifyPageTitle Completed");
			}
		} catch (Exception e) {
			SoftAssertor.addVerificationFailure(e.getMessage());
			log.error("verifyLoginRegisteredUser failed");
			log.error(e.getMessage());
		}
	
	}

	/**
	 * TC_023 Verify first last name fields are editable for registered user
	 * 
	 * @param firstNameXpath
	 * @param firstNameEditData
	 * @param lastNameXpath
	 * @param lastNameEditData
	 */
	@Test(dataProvider = "tafDataProvider", dataProviderClass = TafExcelDataProvider.class, priority = 12, enabled = true, description = "Verify Page Title")
	@ITafExcelDataProviderInputs(excelFile = "CreditAppData", excelsheet = "CreditApp", dataKey = "verifyEditableFieldRU")
	public void verifyFirstNameLastNameIsEditableForExistingRegisteredUser(TestParameters inputs) {
		System.out.println("testing flow verifyFirstNameLastNameIsEditableForRegisteredUser");
		if (loginFailedForRegisteredUser)
			throw new SkipException("This TC is skipped as Failed to Login Registered user");
		try {
			boolean returnValue = false;
			returnValue = verifyFirstNameLastNameIsEditableForRegisteredUser(inputs);
			Assert.assertTrue(returnValue, "Verify First Name Last Name Is Editable For Registered User failed.!!!!!");
			System.out.println("testing flow verifyFirstNameLastNameIsEditableForRegisteredUser Completed");
		} catch (Exception e) {
			log.info("verifyFirstNameLastNameIsEditableForRegisteredUser method as an exception");
			System.out.println(e.getMessage());
			e.printStackTrace();
			Assert.fail();
		}
	}

	/**
	 * TC_024 Verify email field is not editable for registered user
	 * 
	 * @param emailAddXpath
	 * @param emailAddData
	 * @throws PageException
	 */
	@Test(dataProvider = "tafDataProvider", dataProviderClass = TafExcelDataProvider.class, priority = 13, enabled = true, description = "Verify email field is not editable for registered user")
	@ITafExcelDataProviderInputs(excelFile = "CreditAppData", excelsheet = "CreditApp", dataKey = "verifyEmailAddNotEditRU")
	public void verifyEmailAddIsNotEditableForForExistingRegisteredUser(TestParameters inputs) {
		System.out.println("testing flow verifyEmailAddIsNotEditableForRegisteredUser started");
		if (loginFailedForRegisteredUser)
			throw new SkipException("This TC is skipped as Failed to Login Registered user");
		try {
			boolean returnValue = false;
			returnValue = verifyEmailAddIsNotEditableForRegisteredUser(inputs);
			Assert.assertTrue(returnValue, "Verify Email Add Is Not Editable For Registered User failed.!!!!");
			System.out.println("testing flow verifyEmailAddIsNotEditableForRegisteredUser Completed");
		} catch (Exception e) {
			log.info("verifyEmailAddIsNotEditableForRegisteredUser method as an exception");
			System.out.println(e.getMessage());
			e.printStackTrace();
			Assert.fail();
		}
	}

	/**
	 * TC_025 Verify Sign Link link is not rendered on form for registered user
	 * 
	 * @param signInLinkXpath
	 */
	@Test(dataProvider = "tafDataProvider", dataProviderClass = TafExcelDataProvider.class, priority = 14, enabled = true, description = "Verify Sign Link link is not  rendered on form  for registered user")
	@ITafExcelDataProviderInputs(excelFile = "CreditAppData", excelsheet = "CreditApp", dataKey = "verifySignInLinkRenderForRU")
	public void verifySignInLinksNotRenderedForForExistingRegisteredUser(TestParameters inputs) {
		System.out.println("testing flow verifySignInLinksNotRenderedForRegisteredUser started");
		if (loginFailedForRegisteredUser)
			throw new SkipException("This TC is skipped as Failed to Login Registered user");
		boolean returnValue = false;
		try {
			returnValue = verifySignInLinkNotRenderedForRegisteredUser1(inputs);
			Assert.assertTrue(returnValue,
					"Verify Sign Link link is not  rendered on form for registered user failed.!!!");
			System.out.println("testing flow verifySignInLinksNotRenderedForRegisteredUser Completed");
		} catch (Exception e) {
			log.info("verifySignInLinksNotRenderedForRegisteredUser method as an exception");
			System.out.println(e.getMessage());
			e.printStackTrace();
			Assert.fail();
		}
	}

	/**
	 * TC_026 Verify Create And Confirm Password Field is Not Rendered For
	 * registered user
	 * 
	 * @param createPasswordXpath
	 * @param confirmPasswordXpath
	 * @throws PageException
	 */

	
	public boolean verifySignInLinkNotRenderedForRegisteredUser1(TestParameters inputs) {
		try {
			if ((platform.equals("WINDOWS")) || (platform.equals("MAC") || (platform.equals("OS X")))) {
				ITafElement signInLink = webPage.findObjectByLink(inputs.getParamMap().get("SingnInLinkText"));
				return signInLink.isDisplayed() ? false : true;

			} else {
				ITafElement signInLink = mobileView.findObjectByLink(inputs.getParamMap().get("SingnInLinkText"));
				return signInLink.isDisplayed() ? false : true;
			}
		} catch (PageException e) {
			return true;
		}
	}

	public boolean verifyAutoPopulatesFormFieldsForRegisteredUser(TestParameters inputs) throws PageException {

		if ((platform.equals("WINDOWS")) || (platform.equals("MAC") || (platform.equals("OS X")))) {
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
			return populatedElement.isEmpty();
		} else {
			List<ITafElement> textFields = mobileView
					.findObjectsByCss(inputs.getParamMap().get("AllTextFieldCSSForRU"));
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
			return populatedElement.isEmpty();
		}
	}

	/**
	 * 
	 * @param firstNameXpath
	 * @param firstNameEditData
	 * @param lastNameXpath
	 * @param lastNameEditData
	 * @return
	 * @throws PageException
	 */
	public boolean verifyFirstNameLastNameIsEditableForRegisteredUser(TestParameters inputs) throws PageException {
		String firstNameID = inputs.getParamMap().get("FNameID");
		String lastNameID = inputs.getParamMap().get("LNameID");
		String FNameEditText = inputs.getParamMap().get("FNameEditText");
		String LNameEditText = inputs.getParamMap().get("LNameEditText");
		System.out.println(firstNameID);

		if ((platform.equals("WINDOWS")) || (platform.equals("MAC") || (platform.equals("OS X")))) {
			ITafElement firstName = webPage.findObjectById(firstNameID);
			firstName.clear();
			firstName.sendKeys(FNameEditText);
			webPage.sleep(3000);
			ITafElement lastName = webPage.findObjectById(lastNameID);
			lastName.clear();
			lastName.sendKeys(LNameEditText);
			lastName.sendKeys(Keys.TAB);
			webPage.sleep(3000);

			String firstNameEditValue = webPage.findObjectById(firstNameID).getAttribute("value");
			String lastNameEditValue = webPage.findObjectById(lastNameID).getAttribute("value");

			return (firstNameEditValue.equals(FNameEditText) && lastNameEditValue.equals(LNameEditText));

		} else {
			ITafElement firstName = mobileView.findObjectById(firstNameID);
			firstName.clear();
			firstName.sendKeys(FNameEditText);
			mobileView.sleep(3000);
			ITafElement lastName = mobileView.findObjectById(lastNameID);
			lastName.clear();
			lastName.sendKeys(LNameEditText);
			lastName.sendKeys(Keys.TAB);
			mobileView.sleep(3000);

			String firstNameEditValue = mobileView.findObjectById(firstNameID).getAttribute("value");
			String lastNameEditValue = mobileView.findObjectById(lastNameID).getAttribute("value");

			return (firstNameEditValue.equals(FNameEditText) && lastNameEditValue.equals(LNameEditText));
		}
	}

	public boolean verifyEmailAddIsNotEditableForRegisteredUser(TestParameters inputs) throws PageException {
		if ((platform.equals("WINDOWS")) || (platform.equals("MAC") || (platform.equals("OS X")))) {
			String EmailID = inputs.getParamMap().get("EmailID");
			String EmailIDEditText = inputs.getParamMap().get("EmailIDEditText");
			ITafElement emailAdd = webPage.findObjectById(EmailID);
			String emailAddValue = emailAdd.getAttribute("value");
			emailAdd.sendKeys(EmailIDEditText);
			webPage.sleep(3000);
			String editEmailAddValue = webPage.findObjectById(EmailID).getAttribute("value");
			return emailAddValue.equals(editEmailAddValue);
		} else {
			String EmailID = inputs.getParamMap().get("EmailID");
			String EmailIDEditText = inputs.getParamMap().get("EmailIDEditText");
			ITafElement emailAdd = mobileView.findObjectById(EmailID);
			String emailAddValue = emailAdd.getAttribute("value");
			emailAdd.sendKeys(EmailIDEditText);
			mobileView.sleep(3000);
			String editEmailAddValue = mobileView.findObjectById(EmailID).getAttribute("value");
			return emailAddValue.equals(editEmailAddValue);
		}
	}

}
