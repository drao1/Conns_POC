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
import com.etouch.connsPages.ConnsProductPurchasePage;
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
public class ConnsPOC extends BaseTest {
	static String platform;
	static Log log = LogUtil.getLog(ConnsPOC.class);
	static String AbsolutePath= TafExecutor.class.getProtectionDomain().getCodeSource().getLocation().getPath();
	static String  videoLocation = AbsolutePath.substring(0,AbsolutePath.indexOf("/target/classes/")).substring(1).concat("/src/test/resources/testdata/videos");
	Logger logger = Logger.getLogger(ConnsPOC.class.getName());
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
	String storeLocatorURL="http://connsecommdev-1365538477.us-east-1.elb.amazonaws.com/uat/store-locator/";
	protected static String[][] commonData;
	String[][] checkoutFlowCommonLocators;
	String testType,browserName;
	ConnsProductPurchasePage connsProductPurchasePage;
	String testUrl = "http://connsecommdev-1365538477.us-east-1.elb.amazonaws.com/conns_rwd";
	//String testUrl = "http://www.conns.com/";
	String[][] frenchDoor;
	String[][] mobileMenuData;
	String[][] proceedToCheckout;	
	String[][] checkoutGuest;
	String[][] submitBillingInfo;
	
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
				//testEnv = System.getenv().get("Environment");
				testEnv = System.getProperty("ENVIRONMENT");
				System.out.println("testEnv is : " + testEnv);
				path = Paths.get(TestBedManager.INSTANCE.getProfile().getXlsDataConfig().get("testData"));
				DataFilePath = path.toAbsolutePath().toString().replace("Env", testEnv);
				
				System.out.println("DataFilePath After is : " + DataFilePath);
				commonData = ExcelUtil.readExcelData(DataFilePath, "CreditApp", "CreditAppCommonElements");
				
				
				checkoutFlowCommonLocators = ExcelUtil.readExcelData(DataFilePath, "ProductPurchase",
						"Checkout_Flow_Common_Locators");
				System.out.println("checkoutFlowCommonLocators[33][4] : "+checkoutFlowCommonLocators[33][4]);
				frenchDoor = ExcelUtil.readExcelData(DataFilePath, "ProductPurchase", "Click_On_French_Door");
				mobileMenuData = ExcelUtil.readExcelData(DataFilePath, "ProductPurchase",
						"Mobile_Menu_Details");
				proceedToCheckout = ExcelUtil.readExcelData(DataFilePath, "ProductPurchase",
						"Proceed_To_Checkout_Button");
				checkoutGuest = ExcelUtil.readExcelData(DataFilePath, "ProductPurchase", "Checkout_Guest");
				submitBillingInfo = ExcelUtil.readExcelData(DataFilePath, "ProductPurchase",
						"Submit_Billing_Information");
				connsProductPurchasePage = new ConnsProductPurchasePage();
			//	storeLocatorURL=commonData[0][0];
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

	@AfterClass
	public void releaseResources() throws IOException, AWTException {
		//	SpecializedScreenRecorder.stopVideoRecording();
	}
	
	



	/**
	 * Test Case - 004 Verify all Link on Yes Money credit application page are functional 
	 * @throws Exception 
	 * 
	 */
/*	@Test(priority = 4, enabled = true, description = "verify Link Navigation")
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
	}*/

/*	@Test(priority = 6, enabled = true, description = "verify Mandatory Field Validation WithoutData")
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
		webPage.resize(412,632,2000);
		log.info("testing verifyFieldValidationErrorMessageWithInValidData started------>");
		String[][] test= ExcelUtil.readExcelData(DataFilePath, "CreditApp", "verifyFieldValidationErrorMessageWithInValidData");
		CreditAppPage.navigateToCreditAppPage(webPage, softAssert);
		for(int r=0; r<test.length; r++) {
			CreditAppPage.verifyErrorMessageWithInvalidDataById(softAssert, test[r][0], test[r][1], test[r][2], test[r][3],test[r][4]);
		}
		softAssert.assertAll();
	}
	
	@Test(priority = 103, enabled = true, description = "Verify Store locator Region links")
	public void Verify_ChoseYourRegion_Links() throws PageException, InterruptedException {
		SoftAssert softAssert = new SoftAssert();
		try{
			CreditAppPage.navigateToCreditAppPage(webPage, softAssert);
			webPage.resize(768,1024,2000);
			
			String[][] regionLinksData = ExcelUtil.readExcelData(DataFilePath, "StoreLocator",
					"verifyChoseYourRegionLinks");
			for (int i = 0; i < regionLinksData.length; i++) {
				commonMethods.navigateToPage(webPage, storeLocatorURL, softAssert);
				connsStoreLocatorPage.closeLocationPopup(webPage, softAssert);
				if (testType.equalsIgnoreCase("Web")) {
					connsStoreLocatorPage.closeLocationPopup(webPage, softAssert);
					commonMethods.hoverOnelementbyXpath(webPage, regionLinksData[i][1], softAssert);
					String afterLinkHover = commonMethods.getCssvaluebyXpath(webPage, regionLinksData[i][1], "color", softAssert);
					log.info("Region "+regionLinksData[i][0]+" after hover color attribute value is : "+afterLinkHover);
					if(!regionLinksData[i][3].equalsIgnoreCase("NA")){
						softAssert.assertEquals(afterLinkHover, regionLinksData[i][3],"Hover functionality failed for link "+regionLinksData[i][0]+" Expected color: "+regionLinksData[i][3]+" Actual color: "+afterLinkHover);	
					}
				}
				String actualUrl = commonMethods.clickAndGetPageURL(webPage, regionLinksData[i][1], regionLinksData[i][0],softAssert,commonData[2][1]);
				softAssert.assertEquals(actualUrl, regionLinksData[i][2],"URL verification failed for link : '" + regionLinksData[i][0] + "'. Expected URL - "+ regionLinksData[i][2] + " Actual URL - " + actualUrl);
			}
			softAssert.assertAll();
		}catch(Throwable e){
			mainPage.getScreenShotForFailure(webPage, "Verify_ChoseYourRegion_Links");
			softAssert.assertAll();
			Assert.fail(e.getLocalizedMessage());
		}
	}
	
	/*@Test(priority = 104, enabled = true)
	public void Verify_Search_Functionality_And_Results_Contents() throws InterruptedException {
		try {
			webPage.getDriver().manage().window().maximize();
			Thread.sleep(2000);
			SoftAssert softAssert = new SoftAssert();
			String[][] test = ExcelUtil.readExcelData(DataFilePath, "ProductSearch", "verifyProductSearchUsingKeyword");
			String Identifier = test[0][0];
			String ProductName = test[0][1];
			commonMethods.navigateToPage(webPage, url, softAssert);
			webPage.findObjectById(Identifier).sendKeys(ProductName);
			webPage.findObjectByClass(test[0][2]).click();
			log.info("Clicked on element " + test[0][2]);
			String productDescription = webPage.findObjectByxPath(test[0][3]).getText();
			log.info("productDescription" + productDescription);
			Assert.assertTrue(productDescription.contains(ProductName),
					"Product description: " + productDescription + " not having: " + ProductName);
			if (testType.equalsIgnoreCase("Web")) {
				String[][] contentData = ExcelUtil.readExcelData(DataFilePath, "ProductSearch", "verifyContent");
				mainPage.contentVerification(contentData, url);
				webPage.getBackToUrl();
			}
		} catch (PageException e) {
			mainPage.getScreenShotForFailure(webPage, "Verify_Search_Functionality_And_Results_Contents");
			e.printStackTrace();
		}
	}*/
	
	
	@Test(priority = 247, enabled = true, description = "Verify product name & price in different stages while performing checkout flow")
	public void Verify_Pickup_Checkout_Flow_Cash_On_Delivery() {
		SoftAssert softAssert = new SoftAssert();
		String productDetails = null;
		String productPriceCartPage = null;
		String expectedSuccessMessage=checkoutFlowCommonLocators[33][4];
		try {
			webPage.getDriver().manage().window().maximize();
			String[][] pickupAvialableProduct = ExcelUtil.readExcelData(DataFilePath, "ProductPurchase","Click_Add_To_Cart_As_Per_Avilability_Message_Pickup2");
			log.info("testing flow Verify_Checkout_Flow_Cash_On_Delivery started");
			commonMethods.navigateToPage(webPage, testUrl, softAssert);
			if (testType.equalsIgnoreCase("Web")) {
				connsProductPurchasePage.Click_On_Element_JS(webPage, frenchDoor[1][1], softAssert);
				commonMethods.selectDropdownByValue(webPage, checkoutFlowCommonLocators[17][1] , "28", softAssert);
			} else {
				connsProductPurchasePage.clickOnMobileMenuOption(webPage, mobileMenuData, softAssert);
			}
			// adding pickup only product to cart
			productDetails = connsProductPurchasePage.Click_On_In_Stock_With_Delivery_Available(webPage,pickupAvialableProduct, softAssert);
			List<String> actualValue=connsProductPurchasePage.page_Verify_Product_Details_Cart(webPage,checkoutFlowCommonLocators, softAssert);
			System.out.println("Product name:" + actualValue.get(0));
			System.out.println("Product price:" + actualValue.get(1));
			softAssert.assertTrue(productDetails.contains(actualValue.get(0)),"Product list page name is not matching with cart page product name");
			softAssert.assertTrue(productDetails.contains(actualValue.get(1)),"Cart price product is not matching with list price:" + "excpected is:" + productPriceCartPage);
			
			connsProductPurchasePage.Proceed_To_Checkout_Button(webPage, proceedToCheckout, softAssert);
			connsProductPurchasePage.Checkout_Guest(webPage, checkoutGuest, softAssert);
			connsProductPurchasePage.Submit_Billing_Information(webPage, submitBillingInfo, softAssert);
			connsProductPurchasePage.click_Billing_Info_Continue_Button(webPage, checkoutFlowCommonLocators, softAssert);
			CommonMethods.waitForWebElement(By.xpath(checkoutFlowCommonLocators[6][1]), webPage);
			
			//commonMethods.scrollToElement(checkoutFlowCommonLocators[6][1], webPage,softAssert);
			
			String productNamePickupLocationSection=connsProductPurchasePage.get_Pickup_Location_Product_Name(webPage, checkoutFlowCommonLocators, softAssert);
			softAssert.assertTrue(productDetails.contains(productNamePickupLocationSection),
					"product name displayed in pickup location section is not matching is not matching with list page "+"expected is:"+productNamePickupLocationSection);
			CommonMethods.waitForWebElement(By.xpath(checkoutFlowCommonLocators[5][1]), webPage);
			
			connsProductPurchasePage.click_Pickup_Location_Continue_Button(webPage, checkoutFlowCommonLocators, softAssert);
			
		//	CommonMethods.waitForWebElement(By.xpath(checkoutFlowCommonLocators[26][1]), webPage);
			
			//connsProductPurchasePage.click_Shipping_Method_Continue_Button(webPage, checkoutFlowCommonLocators, softAssert);
			CommonMethods.waitForWebElement(By.xpath(checkoutFlowCommonLocators[9][1]), webPage);
			connsProductPurchasePage.click_Cash_On_Delivery_Radio_Button(webPage, checkoutFlowCommonLocators, softAssert);
			connsProductPurchasePage.click_Payment_Info_Continue_Button(webPage, checkoutFlowCommonLocators, softAssert);
			
			//CommonMethods.waitForWebElement(By.xpath(checkoutFlowCommonLocators[11][1]), webPage);
			//List<String> orderreviewActualValue=connsProductPurchasePage.page_Verify_Order_Review_Details(webPage,checkoutFlowCommonLocators, softAssert);
			//softAssert.assertTrue(productDetails.contains(orderreviewActualValue.get(0)), "product name in order review section is not matching");
			//softAssert.assertTrue(productDetails.contains(orderreviewActualValue.get(1)), "product price in order review section is not matching");
			//connsProductPurchasePage.click_Place_Order_Button(webPage, checkoutFlowCommonLocators, softAssert);
			//List<String> cartSideBarActualValue=connsProductPurchasePage.page_Verify_Cart_Sidebar_Checkout(webPage, checkoutFlowCommonLocators, softAssert);
			//softAssert.assertTrue(productDetails.contains(cartSideBarActualValue.get(0)), "product name in order review section is not matching");
			//softAssert.assertTrue(productDetails.contains(cartSideBarActualValue.get(1)), "product price in order review section is not matching");
			//CommonMethods.waitForWebElement(By.xpath(checkoutFlowCommonLocators[30][1]), webPage);
			//List<String> cartHeaderActualValue=connsProductPurchasePage.page_Verify_Cart_Header_Details(webPage, checkoutFlowCommonLocators, softAssert);
			//softAssert.assertTrue(productDetails.contains(cartHeaderActualValue.get(0)), "product name in cart header section is not matching");
			//softAssert.assertTrue(productDetails.contains(cartHeaderActualValue.get(1)), "product name in cart header section is not matching");
			
			CommonMethods.waitForWebElement(By.xpath(checkoutFlowCommonLocators[15][1]), webPage);
			connsProductPurchasePage.click_Place_Order_Button(webPage, checkoutFlowCommonLocators, softAssert);
			
			String orderConfirmationText=commonMethods.getTextbyXpath(webPage, checkoutFlowCommonLocators[33][1], softAssert);
			
			softAssert.assertEquals(orderConfirmationText, expectedSuccessMessage,
					"order confirmation message is not matching:" + " expected is:" + expectedSuccessMessage
							+ "actual is:" + orderConfirmationText);
			
			softAssert.assertAll();

		} catch (Exception e) {
			mainPage.getScreenShotForFailure(webPage, "Verify_Checkout_Flow_Cash_On_Delivery");
			SoftAssertor.addVerificationFailure(e.getMessage());
			log.error("Verify_Checkout_Flow_Cash_On_Delivery failed");
			log.error(e.getMessage());
			softAssert.assertAll();
			Assert.fail(e.getLocalizedMessage());

		}

	}

}
