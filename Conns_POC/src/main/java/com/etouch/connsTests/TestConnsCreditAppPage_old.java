package com.etouch.connsTests;

import java.awt.AWTException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.SkipException;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.etouch.common.BaseTest;
import com.etouch.common.CommonMethods;
import com.etouch.common.TafExecutor;
import com.etouch.connsPages.ConnsMainPage;
import com.etouch.connsPages.ConnsStoreLocatorPage;
import com.etouch.connsPages.CreditAppPage;
import com.etouch.taf.core.TestBed;
import com.etouch.taf.core.TestBedManager;
import com.etouch.taf.core.config.TestBedManagerConfiguration;
import com.etouch.taf.core.datamanager.excel.TafExcelDataProvider;
import com.etouch.taf.core.datamanager.excel.TestParameters;
import com.etouch.taf.core.datamanager.excel.annotations.IExcelDataFiles;
import com.etouch.taf.core.datamanager.excel.annotations.ITafExcelDataProviderInputs;
import com.etouch.taf.core.exception.PageException;
//import com.etouch.taf.tools.rally.SpecializedScreenRecorder;
import com.etouch.taf.util.CommonUtil;
import com.etouch.taf.util.ExcelUtil;
import com.etouch.taf.util.LogUtil;
import com.etouch.taf.util.SoftAssertor;
import com.etouch.taf.webui.ITafElement;
import com.etouch.taf.webui.selenium.WebPage;


@Test(groups = "YesMoneyCreditApplication")
@IExcelDataFiles(excelDataFiles = { "CreditAppData=testData" })
public class TestConnsCreditAppPage_old extends BaseTest {
	static String platform;
	static Log log = LogUtil.getLog(TestConnsCreditAppPage_old.class);
	static String AbsolutePath= TafExecutor.class.getProtectionDomain().getCodeSource().getLocation().getPath();
	static String  videoLocation = AbsolutePath.substring(0,AbsolutePath.indexOf("/target/classes/")).substring(1).concat("/src/test/resources/testdata/videos");
	Logger logger = Logger.getLogger(TestConnsCreditAppPage_old.class.getName());
	protected static String url;
	protected static WebPage webPage;
	private ConnsMainPage mainPage;
	String testBedName;
	TestBed testBed;
	Path path;
	String DataFilePath;
	String testEnv;
	protected static CommonMethods commonMethods;
	ConnsStoreLocatorPage connsStoreLocatorPage;
	String storeLocatorURL="";
	protected static String[][] commonData;
	String testType,browserName;
	@BeforeClass(alwaysRun = true)
	public void setUp(ITestContext context) throws InterruptedException, FileNotFoundException, IOException {
		try {
			testBedName = context.getCurrentXmlTest().getAllParameters().get("testBedName");
			CommonUtil.sop("Test bed Name is " + testBedName);
			testBed = TestBedManager.INSTANCE.getCurrentTestBeds().get(testBedName);
			testType = TestBedManager.INSTANCE.getCurrentTestBeds().get(testBedName).getTestType();
			connsStoreLocatorPage= new ConnsStoreLocatorPage();
			commonMethods = new CommonMethods();
			browserName = TestBedManager.INSTANCE.getCurrentTestBeds().get(testBedName).getBrowser().getName();
			

			System.out.println("Test Type is : " + testType);
			try {
			//	testEnv = System.getenv().get("Environment");
				testEnv = System.getProperty("ENVIRONMENT");
				System.out.println("testEnv is : " + testEnv);
				path = Paths.get(TestBedManager.INSTANCE.getProfile().getXlsDataConfig().get("testData"));
				DataFilePath = path.toAbsolutePath().toString().replace("Env", testEnv);
				System.out.println("DataFilePath After is : " + DataFilePath);
				commonData = ExcelUtil.readExcelData(DataFilePath, "CreditApp", "CreditAppCommonElements");
				storeLocatorURL=commonData[0][0];
				platform = testBed.getPlatform().getName().toUpperCase();
				if (testType.equalsIgnoreCase("Web")) {
					System.out.println("videoLocation" + videoLocation.toString().replace("Env", testEnv));
				}

				url = TestBedManagerConfiguration.INSTANCE.getWebConfig().getURL();
				synchronized (this) {
					webPage = new WebPage(context);
					mainPage = new ConnsMainPage(url, webPage);
				}
				if(testType.equalsIgnoreCase("Web"))
				{
					log.info("Maximizing window");
					webPage.getDriver().manage().window().maximize();
					try{
				if(!TestBedManager.INSTANCE.getCurrentTestBeds().get(testBedName).getBrowser().getWidth().isEmpty()&&
						!TestBedManager.INSTANCE.getCurrentTestBeds().get(testBedName).getBrowser().getHeight().isEmpty())
				{
					
					int width = Integer.parseInt(TestBedManager.INSTANCE.getCurrentTestBeds().get(testBedName).getBrowser().getWidth());
					int height = Integer.parseInt(TestBedManager.INSTANCE.getCurrentTestBeds().get(testBedName).getBrowser().getHeight());
					log.info("Setting Window resolution to : "+width+" X "+height);
					webPage.resize(width,height,1000);
				}}
					catch(Exception e)
					{}
				}
				/*else if(TestBedManager.INSTANCE.getCurrentTestBeds().get(testBedName).getBrowser().getWidth().isEmpty()&&testType.equalsIgnoreCase("Web"))
				{
					log.info("Maximizing window");
					webPage.getDriver().manage().window().maximize();
				}*/
			} catch (Exception e) {
				log.info("errr is " + e);
				SoftAssertor.addVerificationFailure(e.getMessage());
			}
		}
		catch (Exception e) {

			CommonUtil.sop("Error is for" + testBedName + " -----------" + e);

			SoftAssertor.addVerificationFailure(e.getMessage());
		}
	//	webPage.getDriver().manage().window().maximize();
	}

	@AfterClass
	public void releaseResources() throws IOException, AWTException {
		//	SpecializedScreenRecorder.stopVideoRecording();
	}
	/**
	 * Test Case 001 - Verify Navigation to Yes Money Credit Application Page and Verify Page title
	 * @throws Exception 
	 * 
	 */
	@Test(priority = 1, enabled = true, description = "Verify Page Title")
	public void verifyPageTitle()throws Exception {
		SoftAssert softAssert = new SoftAssert();
		try{
			CreditAppPage.navigateToCreditAppPage(webPage, softAssert);

		}catch(Throwable e){
			softAssert.fail(e.getLocalizedMessage());
			CreditAppPage.navigateToCreditAppPage(webPage, softAssert); 
		}
		log.info("Ending verifyPageTitle");
		softAssert.assertAll();
	}
	/**
	 * Test Case - 002 - Verify Font Size and Style of specified on element on Yes Money Credit Application page
	 * @throws Exception 
	 * 
	 */

	@Test(priority = 2, enabled = true, description = "verify Font Size And Style")
	public void verifyFontSizeAndStyle() throws Exception {
		SoftAssert softAssert = new SoftAssert();
		CreditAppPage.navigateToCreditAppPage(webPage, softAssert);  
		String[][] test= ExcelUtil.readExcelData(DataFilePath, "CreditApp", "verifyFont");
		log.info("testing flow verifyFontSizeAndStyle started");
		String pageHeadingClass= null;
		String fontAttribute= null;
		String expectedValue= null;
		int s;
		if(testType.equalsIgnoreCase("Web"))
		{s=3;}
		else{s=2;}
		for(int r=0; r<test.length; r++) {

			pageHeadingClass = test[r][0];
			fontAttribute = test[r][1];
			expectedValue = test[r][s];

			try {
				log.info("Verifying font size and style for element no. " +r);
				ITafElement pageHeading = webPage.findObjectByClass(pageHeadingClass);
				String value = pageHeading.getCssValue(fontAttribute).replaceAll("\"", "")
						.replaceAll(" ", "").toLowerCase().trim();
				softAssert.assertTrue(
						value.contains(expectedValue)
						|| expectedValue.contains(value),
						"Verify Font Size and Style failed.!!!" + "Font Attribute name "
								+ fontAttribute + "Actual : " + value + " and Expected :"
								+ expectedValue.trim());

			} catch (Throwable e) {

				softAssert.fail(e.getLocalizedMessage());
				CreditAppPage.navigateToCreditAppPage(webPage, softAssert); 
			}
		}
		log.info("Ending verifyFontSizeAndStyle");
		softAssert.assertAll();
	}

	/**
	 * Test Case - 003 Verify Page Content are rendered
	 * @throws Exception 
	 * 
	 */

	@Test(priority = 3, enabled = true, description = "Verify Page Content")
	public void verifyPageContent() throws Exception {
		SoftAssert softAssert = new SoftAssert();
		CreditAppPage.navigateToCreditAppPage(webPage, softAssert);  
		log.info("testing verifyPageContent started------>");
		String[][] test= ExcelUtil.readExcelData(DataFilePath, "CreditApp", "verifyPageContent");
		boolean returnValue = false;
		String FieldName;
		String Fieldxpath;
		for(int r=0; r<test.length; r++) {

			FieldName = test[r][0];
			Fieldxpath = test[r][1];

			try {
				log.info("testing verifying Page Content for element no. "+r);
				returnValue = webPage.findObjectByxPath(Fieldxpath).isDisplayed();
				softAssert.assertTrue(returnValue, "Verify Page content failed!!! " + FieldName + "Not rendered on page");
				log.info("testing verifyPageContent Completed------>");
			} catch (Exception e) {
				log.info("Failed to verifying Page Content for element no. "+r);
				softAssert.fail(e.getMessage());
				CreditAppPage.navigateToCreditAppPage(webPage, softAssert); 
			}

			log.info("Ending verifyPageContent");
		}
		softAssert.assertAll();
	}





	/**
	 * Test Case - 004 Verify all Link on Yes Money credit application page are functional 
	 * @throws Exception 
	 * 
	 */
	@Test(priority = 4, enabled = true, description = "verify Link Navigation")
	public void verifyLinkNavigation() throws Exception {
		log.info("testing verifyLinkNavigation started------>");
		SoftAssert softAssert = new SoftAssert();
		CreditAppPage.navigateToCreditAppPage(webPage, softAssert); 
		String[][] test= ExcelUtil.readExcelData(DataFilePath, "CreditApp", "verifyLinksforNewUser");
		String linkName = null;
		String locator = null;
		String ExpectedURL = null;
		String actualUrl = "";
		for(int r=0; r<test.length; r++) {

			linkName = test[r][0];
			locator = test[r][1];
			ExpectedURL = test[r][2];

			try {
				log.info("Verifying Link --->" +linkName);
				CreditAppPage.validateLinkRedirection(linkName, locator, ExpectedURL);
				log.info("testing verifyLinkNavigation completed------>");

			} catch (Throwable e) {
				softAssert.fail(e.getLocalizedMessage());
				CreditAppPage.navigateToCreditAppPage(webPage, softAssert); 
			}
		}
		softAssert.assertAll();
	}

	@Test(priority = 5, enabled = true, description = "verify form is rendered with blank fields")
	public void verifyFormIsDisplayedWithBlankField() throws Exception
	{
		log.info("testing verifyLinkNavigation started------>");
		SoftAssert softAssert = new SoftAssert();
		CreditAppPage.navigateToCreditAppPage(webPage, softAssert); 
		String[][] test= ExcelUtil.readExcelData(DataFilePath, "CreditApp", "verifyFormIsRenderedWithBlankFields");
		CreditAppPage.verifyTextField(test, softAssert);
		log.info("testing verifyLinkNavigation completed------>");
		softAssert.assertAll();
	}

	@Test(priority = 6, enabled = true, description = "verify Mandatory Field Validation WithoutData")
	public void verifyMandatoryFieldValidationWithoutData() throws Exception
	{
		log.info("testing verifyMandatoryFieldValidationWithoutData started------>");
		SoftAssert softAssert = new SoftAssert();
		CreditAppPage.navigateToCreditAppPage(webPage, softAssert); 
		CreditAppPage.submitCreditApp(softAssert);
		String[][] test= ExcelUtil.readExcelData(DataFilePath, "CreditApp", "verifyMandatoryFieldValidationWithoutData");
		for(int i=0;i<test.length;i++)
		{
			CreditAppPage.verifyErrorMessageByXpath(softAssert,test[i][0], test[i][1], test[i][2]);
		}
		log.info("testing verifyMandatoryFieldValidationWithoutData completed------>");
		softAssert.assertAll();
	}
	/**
	 * Test Case - 005 Verify error message displayed for submitting application without data
	 * @throws Exception 
	 * 
	 */
	/*	@Test(dataProvider = "tafDataProvider", dataProviderClass = TafExcelDataProvider.class, priority = 6, enabled = true, description = "verify Error Msg With Blank Data")
	@ITafExcelDataProviderInputs(excelFile = "CreditAppData", excelsheet = "CreditApp", dataKey = "verifyErrorMsgWithBlankData")
	public void verifySubmitWithoutData(ITestContext context, TestParameters inputs) throws Exception {
		log.info("testing verifySubmitWithoutData started------>");
		SoftAssert softAssert = new SoftAssert();
		CreditAppPage.navigateToCreditAppPage(webPage, softAssert); 
		String SubmitID = inputs.getParamMap().get("SubmitID");
		String FirstNameErrorMessageID = inputs.getParamMap().get("FirstNameErrorMessageID");
		String expectedErrorText = inputs.getParamMap().get("expectedErrorText");

		try {
			webPage.navigateToUrl(url);
			webPage.findObjectById(SubmitID).click();
			String actualErrorMessage = webPage.findObjectById(FirstNameErrorMessageID).getText();
			Assert.assertEquals(actualErrorMessage, expectedErrorText, "errorText");
			log.info("testing verifySubmitWithoutData Completed------>");
		} catch (Exception e) {
			SoftAssertor.addVerificationFailure(e.getMessage());
			log.error("verifySubmitWithoutData failed");
			log.error(e.getMessage());
		}

	}*/

	/**
	 * Test Case 007 - Verify field validation Error Message for fields with invalid data
	 * : Email,city,ZipCode,Cell Phone,Home phone,Alternate Phone,Monthly
	 * Mortage Rent,Monthly Income,Other income
	 * @throws Exception 
	 */
	@Test(priority=7, enabled = true, description = "verify Error Msg With Blank Data")
	public void verifyFieldValidationErrorMessageWithInValidData() throws Exception {
		SoftAssert softAssert = new SoftAssert();
		log.info("testing verifyFieldValidationErrorMessageWithInValidData started------>");
		String[][] test= ExcelUtil.readExcelData(DataFilePath, "CreditApp", "verifyFieldValidationErrorMessageWithInValidData");
		CreditAppPage.navigateToCreditAppPage(webPage, softAssert);
		for(int r=0; r<test.length; r++) {
			CreditAppPage.verifyErrorMessageWithInvalidDataById(softAssert, test[r][0], test[r][1], test[r][2], test[r][3],test[r][4]);
		}
		softAssert.assertAll();
	}
	/**
	 * Test Case 008 - Verify City and State Auto populates after entering valid Zip Code
	 * @throws Exception
	 */
	@Test(priority=8, enabled = true, description = "Verify Field Auto Populates")
	public void verifyFieldAutoPopulates() throws Exception {
		SoftAssert softAssert = new SoftAssert();
		log.info("testing verifyFieldValidationErrorMessageWithInValidData started------>");
		String[][] test= ExcelUtil.readExcelData(DataFilePath, "CreditApp", "verifyFieldAutoPopulates");
		CreditAppPage.navigateToCreditAppPage(webPage, softAssert);
		commonMethods.sendKeysById(webPage, test[0][0], test[0][1], softAssert);
		commonMethods.clickElementById(webPage, test[0][2], softAssert);
		softAssert.assertEquals(test[0][3], commonMethods.getTextbyId(webPage, test[0][2], softAssert));
		softAssert.assertEquals(test[0][5], CreditAppPage.getSelectedValueFromDropDown(softAssert, test[0][0], test[0][4]));
	}

	
	/**
	 * Test Case 009 - Verify Verify Years There Drop Down Values
	 * @throws Exception
	 */
	@Test(priority=9, enabled = true, description = "verify Years There Drop Down Values")
	public void verifyYearsThereDropDownValues() throws Exception {
		SoftAssert softAssert = new SoftAssert();
		log.info("testing verifyYearsThereDropDownValues started------>");
		String[][] test= ExcelUtil.readExcelData(DataFilePath, "CreditApp", "verifyYearsThereDropDownValues");
		String[][] yearsThereDropDownValues= ExcelUtil.readExcelData(DataFilePath, "CreditApp", "yearsThereDropDownValues");
		CreditAppPage.navigateToCreditAppPage(webPage, softAssert);
		CreditAppPage.verifyDropDownValuesById(softAssert, test[0][0], test[0][1], yearsThereDropDownValues);	
		softAssert.assertAll();
	}
	
	/**
	 * Test Case 010 - verify City And State Fields Are Editable
	 * @throws Exception
	 */
	@Test(priority=10, enabled = true, description = "verify City And State Fields Are Editable")
	public void verifyCityAndStateFieldAreEditable() throws Exception {
		SoftAssert softAssert = new SoftAssert();
		log.info("testing verifyFieldValidationErrorMessageWithInValidData started------>");
		LinkedHashMap<String, String> testData= commonMethods.getDataInHashMap(DataFilePath, "CreditApp", "verifyCityAndStateFieldAreEditable");
		CreditAppPage.navigateToCreditAppPage(webPage, softAssert);
		commonMethods.sendKeysById(webPage, testData.get("ZipcodeID"), testData.get("ZipcodeValue"), softAssert);
		commonMethods.clickElementById(webPage, testData.get("CityID"), softAssert);
		CreditAppPage.verifyTextFieldIsEditable(softAssert, "City", testData.get("CityID"), testData.get("CityValue"));
		CreditAppPage.verifyDropDownFieldIsEditable(softAssert, "State", testData.get("StateID"), testData.get("StateValue"));
		log.info("testing verifyFieldValidationErrorMessageWithInValidData completed------>");
	}
	
	/**
	 * Test Case 011 - verify Main Source Of Income Field
	 * @throws Exception
	 */
	@Test(priority=10, enabled = true, description = "verify Main Source Of Income Field")
	public void verifyMainSourceOfIncomeField() throws Exception {
		SoftAssert softAssert = new SoftAssert();
		CreditAppPage.navigateToCreditAppPage(webPage, softAssert);
		log.info("testing verifyFieldValidationErrorMessageWithInValidData started------>");
		String[][] testData= ExcelUtil.readExcelData(DataFilePath, "CreditApp", "verifyMainSourceOfIncomeField");
		for(int i=0;i<testData.length;i++)
		{
			CreditAppPage.selectValueFromDropDownByID(softAssert, testData[0][0], testData[0][1], testData[i][2]);
			String[][] MonthlyIncomeTestData= ExcelUtil.readExcelData(DataFilePath, "CreditApp",  testData[i][2]);
			CreditAppPage.sendTextToTextFieldsById(softAssert, MonthlyIncomeTestData);
			Thread.sleep(3000);
		}
		log.info("testing verifyMainSourceOfIncomeField completed------>");
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
			webPage.navigateToUrl(url);
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
			webPage.navigateToUrl(url);
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


			webPage.getDriver().get(url);
			webPage.sleep(5000);
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
			webPage.navigateToUrl(url);
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
			webPage.navigateToUrl(url);					
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
			webPage.navigateToUrl(url);
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
