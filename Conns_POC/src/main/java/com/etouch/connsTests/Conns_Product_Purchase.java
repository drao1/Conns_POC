package com.etouch.connsTests;

import java.awt.AWTException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.logging.Log;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
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
import com.etouch.connsPages.ConnsProductPurchasePage;
import com.etouch.taf.core.TestBed;
import com.etouch.taf.core.TestBedManager;
import com.etouch.taf.core.config.TestBedManagerConfiguration;
import com.etouch.taf.core.datamanager.excel.annotations.IExcelDataFiles;
import com.etouch.taf.util.CommonUtil;
import com.etouch.taf.util.ExcelUtil;
import com.etouch.taf.util.LogUtil;
import com.etouch.taf.util.SoftAssertor;
import com.etouch.taf.webui.ITafElement;
import com.etouch.taf.webui.selenium.WebPage;

//import mx4j.log.Logger;

//@Test(groups = "HomePage")
@IExcelDataFiles(excelDataFiles = { "CreditAppData=testData" })
public class Conns_Product_Purchase extends BaseTest {
	static String platform;
	static Log log = LogUtil.getLog(Conns_Product_Purchase.class);
	static String AbsolutePath = TafExecutor.class.getProtectionDomain().getCodeSource().getLocation().getPath();
	static String videoLocation = AbsolutePath.substring(0, AbsolutePath.indexOf("/target/classes/")).substring(1)
			.concat("/src/test/resources/testdata/videos");
	private String url = null;
	private WebPage webPage;
	private ConnsMainPage mainPage;
	ConnsProductPurchasePage connsProductPurchasePage;
	private ConnsHomePage ConnsHomePage;
	CommonMethods commonMethods;

	String testBedName;
	TestBed testBed;
	Path path;
	String DataFilePath;
	String testType;
	String testEnv;
	JavascriptExecutor executor;
	String[][] frenchDoor;
	String[][] checkoutGuest;
	String[][] checkoutRegister;
	String[][] addToCart;
	String[][] submitBillingInfo;
	String[][] submitShippingInfo;
	String[][] proceedToCheckout;
	String[][] paypalInfo;
	String[][] pickupOnlyAddToCart;
	String[][] inStockOnlyAddToCart;
	String[][] addToCartCommon;
	String[][] zipCodeInValid;
	String[][] zipCodeValid;
	String[][] checkoutFlowCommonLocators;
	String[][] billingFormValidation;
	String[][] ItemLink;
	String[][] SubmitLoginCrdentials;
	String[][] SubmitRegisterDetails;
	String[][] mobileMenuData;
	String[][] mobilePickupAvilable;

	String testUrl = "http://connsecommdev-1365538477.us-east-1.elb.amazonaws.com/conns_rwd";

	// String testUrl =
	// "http://connsecommdev-1365538477.us-east-1.elb.amazonaws.com/uat/";
	// String testUrl = "http://conns.com";

	@BeforeClass(alwaysRun = true)
	public void setUp(ITestContext context) throws InterruptedException, FileNotFoundException, IOException {
		try {

			testBedName = context.getCurrentXmlTest().getAllParameters().get("testBedName");

			CommonUtil.sop("Test bed Name is " + testBedName);
			testBed = TestBedManager.INSTANCE.getCurrentTestBeds().get(testBedName);
			testType = TestBedManager.INSTANCE.getCurrentTestBeds().get(testBedName).getTestType();
			TestBedManagerConfiguration.INSTANCE.getTestTypes();
			System.out.println("Test Type is : " + testType);
			try {
				testEnv = System.getenv().get("Environment");
				System.out.println("testEnv is : " + testEnv);
				System.out.println("testEnv is : " + System.getenv().get("Environment"));
				System.out.println("testEnv is : " + System.getProperty("ENVIRONMENT"));
				path = Paths.get(TestBedManager.INSTANCE.getProfile().getXlsDataConfig().get("testData"));
				DataFilePath = path.toAbsolutePath().toString().replace("Env", testEnv);
				System.out.println("DataFilePath After is : " + DataFilePath);

				frenchDoor = ExcelUtil.readExcelData(DataFilePath, "ProductPurchase", "Click_On_French_Door");
				checkoutGuest = ExcelUtil.readExcelData(DataFilePath, "ProductPurchase", "Checkout_Guest");
				checkoutRegister = ExcelUtil.readExcelData(DataFilePath, "ProductPurchase", "Checkout_Register");
				addToCart = ExcelUtil.readExcelData(DataFilePath, "ProductPurchase",
						"Verify_Add_To_Cart_Button_Functionality");
				submitBillingInfo = ExcelUtil.readExcelData(DataFilePath, "ProductPurchase",
						"Submit_Billing_Information");
				submitShippingInfo = ExcelUtil.readExcelData(DataFilePath, "ProductPurchase", "Submit_Shipping_Info");
				proceedToCheckout = ExcelUtil.readExcelData(DataFilePath, "ProductPurchase",
						"Proceed_To_Checkout_Button");
				paypalInfo = ExcelUtil.readExcelData(DataFilePath, "ProductPurchase", "Submit_Paypal_Payment_Info");
				pickupOnlyAddToCart = ExcelUtil.readExcelData(DataFilePath, "ProductPurchase",
						"Click_On_PickUp_Only_Add_To_Cart_Button");
				inStockOnlyAddToCart = ExcelUtil.readExcelData(DataFilePath, "ProductPurchase",
						"Click_On_In_Stock_With_Delivery_Available");
				billingFormValidation = ExcelUtil.readExcelData(DataFilePath, "ProductPurchase",
						"Verify_BillingInfo_Field_Validation_Positive_Inputs");

				ItemLink = ExcelUtil.readExcelData(DataFilePath, "ProductPurchase",
						"Verify_Remove_Item_Link_Functionality_In_Cart_Page");

				SubmitLoginCrdentials = ExcelUtil.readExcelData(DataFilePath, "ProductPurchase",
						"SubmitLoginCrdentials");

				SubmitRegisterDetails = ExcelUtil.readExcelData(DataFilePath, "ProductPurchase",
						"SubmitRegisterDetails");

				checkoutFlowCommonLocators = ExcelUtil.readExcelData(DataFilePath, "ProductPurchase",
						"Checkout_Flow_Common_Locators");
				mobileMenuData = ExcelUtil.readExcelData(DataFilePath, "ProductPurchase",
						"Mobile_Menu_Details");
				mobilePickupAvilable = ExcelUtil.readExcelData(DataFilePath, "ProductPurchase",
						"Click_Add_To_Cart_As_Per_Avilability_Message2_Mobile");
				

				platform = testBed.getPlatform().getName().toUpperCase();
				if (testType.equalsIgnoreCase("Web")) {
					System.out.println("videoLocation" + videoLocation);

				} else {
				}
				url = TestBedManagerConfiguration.INSTANCE.getWebConfig().getURL();

				connsProductPurchasePage = new ConnsProductPurchasePage();
				commonMethods = new CommonMethods();
				synchronized (this) {

					webPage = new WebPage(context);
					mainPage = new ConnsMainPage(url, webPage);
					executor = (JavascriptExecutor) webPage.getDriver();
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
		// SpecializedScreenRecorder.stopVideoRecording();
	}

	/*
	 * UC_001 - Product Search Page
	 **********************************************************************************************************/

	/*
	 * @Madhukar
	 * 
	 * Test Case 001 - Verify Page title --> appliances --> Refrigerators -->
	 * French Door French Door Refrigerators : Fridges by LG, Samsung & more |
	 * Conn's
	 */
	@Test(priority = 201, enabled = true, description = "Verify Page title")
	public void Verify_Page_Title() {
		SoftAssert softAssert = new SoftAssert();
		String actualTitle = null;
		String ExpectedTitle = null;
		try {
			log.info("testing flow verifyPageTitle started");

			commonMethods.navigateToPage(webPage, testUrl, softAssert);

			if (testType.equalsIgnoreCase("Web")) {

				connsProductPurchasePage.Click_On_Element_JS(webPage, frenchDoor[1][1], softAssert);

			} else {

				connsProductPurchasePage.clickOnMobileMenuOption(webPage, mobileMenuData, softAssert);
			}
			
			actualTitle = commonMethods.getPageTitle(webPage, softAssert);
			ExpectedTitle = frenchDoor[0][3];
			System.out.println("Actual title is:::" + actualTitle);
			System.out.println("ExpectedTitle is:::" + ExpectedTitle);

			softAssert.assertEquals(actualTitle, ExpectedTitle, "Page tilte is not matching with expected title: "
					+ "Actual tilte is:" + actualTitle + "expected title is:" + ExpectedTitle);
			softAssert.assertAll();
		} catch (Exception e) {
			mainPage.getScreenShotForFailure(webPage, "Verify_Page_Title");
			SoftAssertor.addVerificationFailure(e.getMessage());
			log.error("Verify_Page_Title failed");
			log.error(e.getMessage());
			softAssert.assertAll();
			Assert.fail(e.getLocalizedMessage());

		}

	}

	/*
	 * 
	 * @Madhukar Test Case 002 - Verify ADD TO CART button functionality
	 */

	@Test(priority = 202, enabled = true, description = "verify Add To Cart Functionality")
	public void Verify_Add_To_Cart_Button_Functionality() {
		SoftAssert softAssert = new SoftAssert();

		try {

			String[][] addToCart = ExcelUtil.readExcelData(DataFilePath, "ProductPurchase",
					"Verify_Add_To_Cart_Button_Functionality");
			log.info("testing flow verifyAddToCartFunctionality started");
			commonMethods.navigateToPage(webPage, testUrl, softAssert);

			if (testType.equalsIgnoreCase("Web")) {

				connsProductPurchasePage.Click_On_Element_JS(webPage, frenchDoor[1][1], softAssert);
				connsProductPurchasePage.Add_To_Cart(webPage, addToCart, softAssert);

			} else {
				connsProductPurchasePage.clickOnMobileMenuOption(webPage, mobileMenuData, softAssert);
				connsProductPurchasePage.Add_To_Cart(webPage, addToCart, softAssert);
			}

			softAssert.assertAll();

		} catch (Throwable e) {

			mainPage.getScreenShotForFailure(webPage, "Verify_Add_To_Cart_Button_Functionality");
			SoftAssertor.addVerificationFailure(e.getMessage());
			log.error("Verify_Add_To_Cart_Button_Functionality failed");
			log.error(e.getMessage());
			softAssert.assertAll();
			Assert.fail(e.getLocalizedMessage());

		}

	}

	/**
	 * Test Case - 003 -Verify Page font size & style
	 * 
	 * 
	 */
	@Test(priority = 203, enabled = true, description = "Verify Page Font-Size And Style")
	public void Verify_Font_Size_And_Style() {
		SoftAssert softAssert = new SoftAssert();
		String[][] test = ExcelUtil.readExcelData(DataFilePath, "ProductPurchase", "Verify_Font_Size_And_Style");
		log.info("testing flow verifyFontSizeAndStyle started");
		commonMethods.navigateToPage(webPage, testUrl, softAssert);
		String pageHeadingClass = null;
		String fontAttribute = null;
		String expectedValue = null;
		int count = 0;
		List<Integer> failedElements = new ArrayList<Integer>();
		for (int r = 0; r < test.length; r++) {

			pageHeadingClass = test[r][0];
			fontAttribute = test[r][1];
			if (testType.equalsIgnoreCase("Web")) {
				expectedValue = test[r][2];
			} else {
				expectedValue = test[r][3];
			}
			try {
				log.info("Verifying font size and style for element no. " + (r + 1));
				ITafElement pageHeading = webPage.findObjectByCss(pageHeadingClass);
				String value = pageHeading.getCssValue(fontAttribute).replaceAll("\"", "").replaceAll(" ", "")
						.toLowerCase().trim();
				Assert.assertTrue(value.contains(expectedValue) || expectedValue.contains(value),
						"Verify Font Size and Style failed.!!!" + "Font Attribute name " + fontAttribute + "Actual : "
								+ value + " and Expected :" + expectedValue.trim());

			} catch (Throwable e) {
				count++;
				failedElements.add(count);
				SoftAssertor.addVerificationFailure(e.getMessage());
				log.error("verifyFontSizeAndStyle failed");
				log.error(e.getMessage());
			}

		}
		if (count > 0) {
			Assert.fail("Failed to verify element number : " + Arrays.deepToString(failedElements.toArray()));
		}
		log.info("Ending verifyFontSizeAndStyle");

	}

	/*
	 * @Madhukar - TC_003- Verify Zip Code textbox with invalid input
	 * 
	 */

	@Test(priority = 204, enabled = true, description = "verify Zip Code Text Box Error Message")
	public void Verify_Zip_Code_Text_Box_Error_Message() {
		SoftAssert softAssert = new SoftAssert();
		String actualValue = null;
		String expectedValue = null;
		try {
			String[][] test = ExcelUtil.readExcelData(DataFilePath, "ProductPurchase","Verify_Zip_Code_Text_Box_Error_Message");
			String[][] addToCart = ExcelUtil.readExcelData(DataFilePath, "ProductPurchase","Verify_Add_To_Cart_Button_Functionality");
			log.info("testing flow Verify_Zip_Code_Text_Box_Error_Message started");
			commonMethods.navigateToPage(webPage, testUrl, softAssert);
			if (testType.equalsIgnoreCase("Web")) {
				connsProductPurchasePage.Click_On_Element_JS(webPage, frenchDoor[1][1], softAssert);
				connsProductPurchasePage.Add_To_Cart(webPage, addToCart, softAssert);
			} else {
				connsProductPurchasePage.clickOnMobileMenuOption(webPage, mobileMenuData, softAssert);
				connsProductPurchasePage.Add_To_Cart(webPage, addToCart, softAssert);
			}
			boolean isOverLayBoxPresent = webPage.getDriver().findElements(By.xpath(test[0][1])).size() >= 1;
			System.out.println("isOverLayBoxPresent:::" + isOverLayBoxPresent);
			if (!isOverLayBoxPresent) {
				commonMethods.clickElementbyXpath(webPage, test[3][1], softAssert);
			}
			softAssert.assertTrue(webPage.findObjectByxPath(test[0][1]).isDisplayed(),
					"verification 1 failed: Over Lay Box is not displayed on clicking ADD to Cart Button" + "\n ");
			commonMethods.sendKeysbyXpath(webPage, test[1][1], test[1][3], softAssert);
			commonMethods.clickElementbyXpath(webPage, test[2][1], softAssert);
			actualValue = commonMethods.getTextbyXpath(webPage, test[3][1], softAssert);
			expectedValue = test[3][4];
			softAssert.assertTrue(expectedValue.contains(actualValue), "Zip code error message is not displayed:"
					+ "expected is:+" + expectedValue + "actual Value is:" + actualValue);
			softAssert.assertAll();
		} catch (Throwable e) {
			mainPage.getScreenShotForFailure(webPage, "Verify_Zip_Code_Text_Box_Error_Message");
			SoftAssertor.addVerificationFailure(e.getMessage());
			log.error("Verify_Zip_Code_Text_Box_Error_Message failed");
			log.error(e.getMessage());
			softAssert.assertAll();
			Assert.fail(e.getLocalizedMessage());
		}
	}
	/*
	 * @Madhukar TC_004 Verify ADD TO CART on overlay without entering input in
	 * Zip code
	 * 
	 */
	@Test(priority = 205, enabled = true, description = "Verify Add To Cart On Overlay Without Zip Code")
	public void Verify_Add_To_Cart_On_Overlay_Without_Zip_Code() {
		SoftAssert softAssert = new SoftAssert();
		try {
			String[][] test = ExcelUtil.readExcelData(DataFilePath, "ProductPurchase",
					"Verify_Add_To_Cart_On_Overlay_Without_Zip_Code");
			log.info("testing flow Verify_Add_To_Cart_On_Overlay_Without_Zip_Code started");
			commonMethods.navigateToPage(webPage, testUrl, softAssert);
			if (testType.equalsIgnoreCase("Web")) {
				connsProductPurchasePage.Click_On_Element_JS(webPage, frenchDoor[1][1], softAssert);
				connsProductPurchasePage.Add_To_Cart(webPage, addToCart, softAssert);
			} else {
				connsProductPurchasePage.clickOnMobileMenuOption(webPage, mobileMenuData, softAssert);
				connsProductPurchasePage.Add_To_Cart(webPage, addToCart, softAssert);
			}
			boolean isOverLayBoxPresent = webPage.getDriver().findElements(By.xpath(test[0][1])).size() >= 1;
			System.out.println("isOverLayBoxPresent:::" + isOverLayBoxPresent);
			if (!isOverLayBoxPresent) {
				commonMethods.clickElementbyXpath(webPage, test[4][1], softAssert);
			}
			softAssert.assertTrue(isOverLayBoxPresent,
					"verification 1 failed: Over Lay Box is not displayed on clicking ADD to Cart Button" + "\n ");
			commonMethods.clickElementbyXpath(webPage, test[2][1], softAssert);
			String actualErrorMessage = commonMethods.getTextbyXpath(webPage, test[3][1], softAssert);
			System.out.println("actualErrorMessage:::" + actualErrorMessage);
			String expectedErrorMessage = test[3][4];
			System.out.println("expectedErrorMessage:" + expectedErrorMessage);
			softAssert.assertTrue(expectedErrorMessage.contains(actualErrorMessage),
					"Expected Text: " + expectedErrorMessage + "Actual text: " + actualErrorMessage);
			softAssert.assertAll();
		} catch (Throwable e) {
			mainPage.getScreenShotForFailure(webPage, "Verify_Add_To_Cart_On_Overlay_Without_Zip_Code");
			log.error("Verify_Add_To_Cart_On_Overlay_Without_Zip_Code failed");
			log.error(e.getMessage());
			softAssert.assertAll();
			Assert.fail(e.getLocalizedMessage());
		}
	}
	/*
	 * @Madhukar TC_005 Verify ADD TO CART on overlay with valid input in Zip
	 * code
	 * 
	 */

	@Test(priority = 206, enabled = true, description = "verify Zip Code Text Box Error Message")
	public void Verify_Add_To_Cart_Valid_Zip_Code() {
		SoftAssert softAssert = new SoftAssert();
		try {
			String[][] test = ExcelUtil.readExcelData(DataFilePath, "ProductPurchase",
					"Click_Add_To_Cart_As_Per_Avilability_Message2");
			commonMethods.navigateToPage(webPage, testUrl, softAssert);
			log.info("testing flow Verify_Add_To_Cart_Valid_Zip_Code started");
			if (testType.equalsIgnoreCase("Web")) {
				connsProductPurchasePage.Click_On_Element_JS(webPage, frenchDoor[1][1], softAssert);
			} else {
				connsProductPurchasePage.clickOnMobileMenuOption(webPage, mobileMenuData, softAssert);
			}
			connsProductPurchasePage.Click_On_In_Stock_With_Delivery_Available(webPage, inStockOnlyAddToCart,
					softAssert);
			softAssert.assertAll();
		} catch (Throwable e) {
			mainPage.getScreenShotForFailure(webPage, "Verify_Add_To_Cart_On_Overlay_With_Valid_Zip_Code");
			log.error("verifyZipCodeTextBoxErrorMessage failed");
			log.error(e.getMessage());
			softAssert.assertAll();
			Assert.fail(e.getLocalizedMessage());
		}
	}

	/*
	 * UC_002 - Cart Features validation
	 *******************************************************************************************/

	/*
	 * 
	 * 
	 * update functionality is not seen on cart page (Dev envi): tested on
	 * 04/07/2017
	 */

	@Test(priority = 207, enabled = true, description = "Verify 'Update' functionality in cart page")
	public void Verify_Update_Functionality_Cart_Page() {
		SoftAssert softAssert = new SoftAssert();
		String actualquandityAfterUpdate = null;
		String productPriceCartPageAfterUpdate = null;
		int actualProductPrice = 0;
		int expectedProductPrice = 0;
		try {
			String[][] addToCartInStockValid = ExcelUtil.readExcelData(DataFilePath, "ProductPurchase",
					"Click_Add_To_Cart_As_Per_Avilability_Message2");
			log.info("testing flow Verify_Update_Functionality_Cart_Page started");
			commonMethods.navigateToPage(webPage, testUrl, softAssert);
			if (testType.equalsIgnoreCase("Web")) {
				connsProductPurchasePage.Click_On_Element_JS(webPage, frenchDoor[1][1], softAssert);
				commonMethods.selectDropdownByValue(webPage, checkoutFlowCommonLocators[17][1], "28", softAssert);
				connsProductPurchasePage.Click_On_In_Stock_With_Delivery_Available(webPage, addToCartInStockValid,softAssert);
			} else {
				connsProductPurchasePage.clickOnMobileMenuOption(webPage, mobileMenuData, softAssert);
				connsProductPurchasePage.Click_On_Element_JS(webPage, "(//div[@class='items']//li[@class='item' and contains(@data-val,'28')])[2]", softAssert);
				connsProductPurchasePage.Click_On_In_Stock_With_Delivery_Available_Mobile(webPage,mobilePickupAvilable,softAssert);
			}
			String productQuandityBeforeUpdate = commonMethods
					.getWebElementbyXpath(webPage, checkoutFlowCommonLocators[18][1], softAssert).getAttribute("value");
			System.out.println("productQuandityBeforeUpdateOnCart:" + productQuandityBeforeUpdate);
			// price when QTY=1
			System.out.println("price string:"+ commonMethods.getTextbyXpath(webPage, ItemLink[10][1], softAssert));
			expectedProductPrice = Integer.parseInt(
					commonMethods.getTextbyXpath(webPage, ItemLink[10][1], softAssert).replace("$", "").replace(",", "").replace(".", ""));
			System.out.println("expectedProductPrice:" + expectedProductPrice);
			commonMethods.getWebElementbyXpath(webPage, checkoutFlowCommonLocators[18][1], softAssert).clear();
			commonMethods.sendKeysbyXpath(webPage, checkoutFlowCommonLocators[18][1], "2", softAssert);
			commonMethods.clickElementbyXpath(webPage, checkoutFlowCommonLocators[19][1], softAssert);
			actualquandityAfterUpdate = commonMethods
					.getWebElementbyXpath(webPage, checkoutFlowCommonLocators[18][1], softAssert).getAttribute("value");
			// price when QTY=2
			actualProductPrice = Integer.parseInt(
					commonMethods.getTextbyXpath(webPage, ItemLink[11][1], softAssert).replace("$", "").replace(",", "").replace(".", ""));
			System.out.println("actualProductPrice:" + actualProductPrice);
			System.out.println("actualquandityAfterUpdate:" + actualquandityAfterUpdate);
			System.out.println("actualProductPrice:" + actualProductPrice);
			System.out.println("expectedProductPrice:" + expectedProductPrice);
			softAssert.assertEquals(actualProductPrice, expectedProductPrice * 2,
					"sub total price is not getting doubled on updatating number of units to 2");
			softAssert.assertNotEquals(actualquandityAfterUpdate, productQuandityBeforeUpdate,
					"Product Qty is not updated after clicking update link");
			softAssert.assertAll();
		} catch (Throwable e) {
			mainPage.getScreenShotForFailure(webPage, "Verify_Update_Functionality_Cart_Page");
			log.error("Verify_Update_Functionality_Cart_Page failed");
			log.error(e.getMessage());
			softAssert.assertAll();
			Assert.fail(e.getLocalizedMessage());
		}
	}
	@Test(priority = 208, enabled = true, description = "Verify 'Remove item link functionality in cart page")
	public void Verify_Remove_Item_Link_Functionality_In_Cart_Page() {
		SoftAssert softAssert = new SoftAssert();
		String expectedText = null;
		try {
			String[][] test = ExcelUtil.readExcelData(DataFilePath, "ProductPurchase",
					"Verify_Remove_Item_Link_Functionality_In_Cart_Page");
			String[][] addToCartInStockValid = ExcelUtil.readExcelData(DataFilePath, "ProductPurchase",
					"Click_Add_To_Cart_As_Per_Avilability_Message2");
			log.info("testing flow Verify_Remove_Item_Link_Functionality_In_Cart_Page started");
			commonMethods.navigateToPage(webPage, testUrl, softAssert);
			if (testType.equalsIgnoreCase("Web")) {
				connsProductPurchasePage.Click_On_Element_JS(webPage, frenchDoor[1][1], softAssert);
				commonMethods.selectDropdownByValue(webPage, "(//select[@class='hasCustomSelect'])[7]", "28", softAssert);
			} else {
				connsProductPurchasePage.clickOnMobileMenuOption(webPage, mobileMenuData, softAssert);
				connsProductPurchasePage.Click_On_Element_JS(webPage, "(//div[@class='items']//li[@class='item' and contains(@data-val,'28')])[2]", softAssert);
			}
			connsProductPurchasePage.Click_On_In_Stock_With_Delivery_Available(webPage, addToCartInStockValid,
					softAssert);
			
			// connsProductPurchasePage.Click_On_PickUp_Only_Add_To_Cart_Button(webPage,
			// pickupOnlyAddToCart, softAssert);

			boolean isProductAvilable = commonMethods.verifyElementisPresent(webPage, test[5][1], softAssert);

			softAssert.assertTrue(isProductAvilable, "Product Avilability message is not displayed:" + test[5][1]);

			if (isProductAvilable) {

				commonMethods.verifyElementisPresent(webPage, test[6][1], softAssert);

				commonMethods.clickElementbyXpath(webPage, test[7][1], softAssert);

				String actualText = commonMethods.getTextbyXpath(webPage, test[8][1], softAssert);
				expectedText = test[0][4];

				System.out.println("actualText:" + actualText);
				System.out.println("expectedText:" + expectedText);

				softAssert.assertEquals(actualText, expectedText, "Shopping Cart empty text message is not displayed");

			}

			softAssert.assertAll();

		} catch (Throwable e) {

			mainPage.getScreenShotForFailure(webPage, "Verify_Remove_Item_Link_Functionality_In_Cart_Page");
			log.error("Verify_Remove_Item_Link_Functionality_In_Cart_Page failed");
			log.error(e.getMessage());
			softAssert.assertAll();
			Assert.fail(e.getLocalizedMessage());
		}

	}

	@Test(priority = 209, enabled = true, description = "Verify product AVAILABILITY status in cart page")
	public void Verify_Product_Availability_status_In_Cart_Page() {
		SoftAssert softAssert = new SoftAssert();

		String expectedValue = null;
		try {
			String[][] addToCartInStockValid = ExcelUtil.readExcelData(DataFilePath, "ProductPurchase",
					"Click_Add_To_Cart_As_Per_Avilability_Message2");

			String[][] test = ExcelUtil.readExcelData(DataFilePath, "ProductPurchase",
					"Verify_Product_Availability_status_In_Cart_Page");

			String[][] addToCart = ExcelUtil.readExcelData(DataFilePath, "ProductPurchase",
					"Verify_Add_To_Cart_Button_Functionality");

			log.info("testing flow Verify_Product_Availability_status_In_Cart_Page started");

			commonMethods.navigateToPage(webPage, testUrl, softAssert);

			if (testType.equalsIgnoreCase("Web")) {
				connsProductPurchasePage.Click_On_Element_JS(webPage, frenchDoor[1][1], softAssert);
				commonMethods.selectDropdownByValue(webPage, "(//select[@class='hasCustomSelect'])[7]", "28", softAssert);	

			} else {
				connsProductPurchasePage.clickOnMobileMenuOption(webPage, mobileMenuData, softAssert);
				connsProductPurchasePage.Click_On_Element_JS(webPage, "(//div[@class='items']//li[@class='item' and contains(@data-val,'28')])[2]", softAssert);
				
			}
			connsProductPurchasePage.Click_On_In_Stock_With_Delivery_Available(webPage, addToCartInStockValid,softAssert);

			expectedValue = test[0][4];

			String ActualValue = commonMethods.getTextbyXpath(webPage, test[4][1], softAssert);
			System.out.println("ActualValue:" + ActualValue);
			System.out.println("expectedValue:" + expectedValue);
			softAssert.assertTrue(ActualValue.contains(expectedValue), " Expected Avilability Status : Expected  "
					+ expectedValue + " Actual Avilability Message: " + ActualValue);
			softAssert.assertAll();

		} catch (Throwable e) {

			mainPage.getScreenShotForFailure(webPage, "Verify_Product_Availability_status_In_Cart_Page");
			log.error("Verify_Product_Availability_status_In_Cart_Page failed");
			log.error(e.getMessage());
			softAssert.assertAll();
			Assert.fail(e.getLocalizedMessage());
		}

	}

	@Test(priority = 210, enabled = true, description = "Verify Zip Code textbox with invalid input")
	public void Verify_Apply_Code_Button_With_Invalid_Code() {
		SoftAssert softAssert = new SoftAssert();
		String expected_Coupon_Code_Error_Message;
		String actual_Coupon_Code_Error_Message;
		try {
			String[][] test = ExcelUtil.readExcelData(DataFilePath, "ProductPurchase",
					"Verify_Apply_Code_Button_With_Invalid_Code");

			log.info("testing flow Verify_Apply_Code_Button_With_Invalid_Code started");
			commonMethods.navigateToPage(webPage, testUrl, softAssert);

			if (testType.equalsIgnoreCase("Web")) {
				connsProductPurchasePage.Click_On_Element_JS(webPage, frenchDoor[1][1], softAssert);
				commonMethods.selectDropdownByValue(webPage, "(//select[@class='hasCustomSelect'])[7]", "28", softAssert);

			} else {
				connsProductPurchasePage.clickOnMobileMenuOption(webPage, mobileMenuData, softAssert);	
				connsProductPurchasePage.Click_On_Element_JS(webPage, "(//div[@class='items']//li[@class='item' and contains(@data-val,'28')])[2]", softAssert);
				
				
			}

			connsProductPurchasePage.Click_On_PickUp_Only_Add_To_Cart_Button(webPage, pickupOnlyAddToCart, softAssert);

			commonMethods.sendKeysbyXpath(webPage, test[0][1], test[0][3], softAssert);
			commonMethods.clickElementbyXpath(webPage, test[1][1], softAssert);
			actual_Coupon_Code_Error_Message = commonMethods.getTextbyXpath(webPage, test[2][1], softAssert)
					.replaceAll(" ", "");
			expected_Coupon_Code_Error_Message = test[0][4].replaceAll(" ", "");

			softAssert.assertTrue(actual_Coupon_Code_Error_Message.contains(expected_Coupon_Code_Error_Message),
					"Coupon_Code_Error_Messages are not matching::" + " Expected is:"
							+ expected_Coupon_Code_Error_Message + " Actual is: " + actual_Coupon_Code_Error_Message);

			softAssert.assertAll();

		} catch (Throwable e) {

			mainPage.getScreenShotForFailure(webPage, "Verify_Apply_Code_Button_With_Invalid_Code");
			log.error("Verify_Apply_Code_Button_With_Invalid_Code failed");
			log.error(e.getMessage());
			softAssert.assertAll();
			Assert.fail(e.getLocalizedMessage());
		}

	}

	/*
	 * 
	 * need test data
	 */
	@Test(priority = 211, enabled = true, description = "Verify the functionality of 'PROCEED TO CHECKOUT' button in cart page")
	public void Verify_Apply_Code_Button_With_valid_Code() {
		SoftAssert softAssert = new SoftAssert();
		String expected_Coupon_Code_Error_Message;
		String actual_Coupon_Code_Error_Message;
		try {
			String[][] test = ExcelUtil.readExcelData(DataFilePath, "ProductPurchase",
					"Verify_Apply_Code_Button_With_valid_Code");
			log.info("testing flow Verify_Apply_Code_Button_With_valid_Code started");
			commonMethods.navigateToPage(webPage, testUrl, softAssert);
			if (testType.equalsIgnoreCase("Web")) {
				connsProductPurchasePage.Click_On_Element_JS(webPage, frenchDoor[1][1], softAssert);
				commonMethods.selectDropdownByValue(webPage, "(//select[@class='hasCustomSelect'])[7]", "28", softAssert);
			} else {
				connsProductPurchasePage.clickOnMobileMenuOption(webPage, mobileMenuData, softAssert);
				connsProductPurchasePage.Click_On_Element_JS(webPage, "(//div[@class='items']//li[@class='item' and contains(@data-val,'28')])[2]", softAssert);
			}

			connsProductPurchasePage.Click_On_PickUp_Only_Add_To_Cart_Button(webPage, pickupOnlyAddToCart, softAssert);
			commonMethods.sendKeysbyXpath(webPage, test[0][1], test[0][3], softAssert);
			commonMethods.clickElementbyXpath(webPage, test[1][1], softAssert);
			actual_Coupon_Code_Error_Message = commonMethods.getTextbyXpath(webPage, test[2][1], softAssert)
					.replaceAll(" ", "");
			expected_Coupon_Code_Error_Message = test[0][4].replaceAll(" ", "");

			softAssert.assertTrue(actual_Coupon_Code_Error_Message.contains(expected_Coupon_Code_Error_Message),
					"Coupon_Code_Error_Messages are not matching::" + " Expected is:"
							+ expected_Coupon_Code_Error_Message + " Actual is: " + actual_Coupon_Code_Error_Message);

			softAssert.assertAll();

		} catch (Throwable e) {

			mainPage.getScreenShotForFailure(webPage, "Verify_Apply_Code_Button_With_valid_Code");
			log.error("Verify_Apply_Code_Button_With_valid_Code failed");
			log.error(e.getMessage());
			softAssert.assertAll();
			Assert.fail(e.getLocalizedMessage());
		}

	}

	@Test(priority = 212, enabled = true, description = "Verify the functionality of 'PROCEED TO CHECKOUT' button in cart page")
	public void Verify_Proceed_To_Checkout_Functionality() {
		SoftAssert softAssert = new SoftAssert();
		String expectedUrl = null;
		String actualUrl = null;
		try {

			String[][] test = ExcelUtil.readExcelData(DataFilePath, "ProductPurchase",
					"Verify_Proceed_To_Checkout_Functionality");
			log.info("testing flow Verify_Proceed_To_Checkout_Functionality started");
			commonMethods.navigateToPage(webPage, testUrl, softAssert);

			if (testType.equalsIgnoreCase("Web")) {
				connsProductPurchasePage.Click_On_Element_JS(webPage, frenchDoor[1][1], softAssert);

			} else {
				connsProductPurchasePage.clickOnMobileMenuOption(webPage, mobileMenuData, softAssert);
				connsProductPurchasePage.Click_On_Element_JS(webPage, "(//div[@class='items']//li[@class='item' and contains(@data-val,'28')])[2]", softAssert);
			}

			connsProductPurchasePage.Click_On_PickUp_Only_Add_To_Cart_Button(webPage, pickupOnlyAddToCart, softAssert);

			connsProductPurchasePage.Proceed_To_Checkout_Button(webPage, proceedToCheckout, softAssert);

			actualUrl = commonMethods.getPageUrl(webPage, softAssert);
			expectedUrl = test[0][4];
			System.out.println("actualUrl.contains(expectedUrl):" + actualUrl.contains(expectedUrl));

			softAssert.assertTrue(actualUrl.contains(expectedUrl), "Proceed to checkout functionality is failed:::"
					+ " Expected is: " + expectedUrl + "Actual is:::" + actualUrl);

			softAssert.assertAll();

		} catch (Throwable e) {

			mainPage.getScreenShotForFailure(webPage, "Verify_Proceed_To_Checkout_Functionality");
			log.error("Verify_Proceed_To_Checkout_Functionality failed");
			log.error(e.getMessage());
			softAssert.assertAll();
			Assert.fail(e.getLocalizedMessage());
		}

	}

	@Test(priority = 213, enabled = true, description = "Verify the 'Continue Shopping' link in cart page")
	public void Verify_Continue_Shopping_Link_Functionality_In_Cart_Page() {
		SoftAssert softAssert = new SoftAssert();
		String expectedUrl = null;
		String actualUrl = null;
		try {

			String[][] test = ExcelUtil.readExcelData(DataFilePath, "ProductPurchase",
					"Verify_Continue_Shopping_Link_Functionality_In_Cart_Page");

			log.info("testing flow Verify_Continue_Shopping_Link_Functionality_In_Cart_Page started");

			commonMethods.navigateToPage(webPage, testUrl, softAssert);

			if (testType.equalsIgnoreCase("Web")) {
				connsProductPurchasePage.Click_On_Element_JS(webPage, frenchDoor[1][1], softAssert);

			} else {
				connsProductPurchasePage.clickOnMobileMenuOption(webPage, mobileMenuData, softAssert);
				connsProductPurchasePage.Click_On_Element_JS(webPage, "(//div[@class='items']//li[@class='item' and contains(@data-val,'28')])[2]", softAssert);
			}
			connsProductPurchasePage.Click_On_PickUp_Only_Add_To_Cart_Button(webPage, pickupOnlyAddToCart, softAssert);

			commonMethods.clickElementbyXpath(webPage, test[0][1], softAssert);

			actualUrl = commonMethods.getPageUrl(webPage, softAssert);
			expectedUrl = test[0][4];
			System.out.println("actualUrl  :::" + actualUrl + "/n");
			System.out.println("expectedUrl:::" + expectedUrl);
			/*
			 * softAssert.assertEquals(expectedUrl, actualUrl,
			 * "Continue Shopping Link Functionality Is Failed:::" +
			 * " Expected is: " + expectedUrl + " Actual is:::" + actualUrl);
			 */

			softAssert.assertTrue(actualUrl.contains(expectedUrl), "Continue Shopping Link Functionality Is Failed:::"
					+ " : failed " + "Actual URL is  :" + actualUrl + " " + "Expected URL is  :" + expectedUrl);
			softAssert.assertAll();

		} catch (Throwable e) {

			mainPage.getScreenShotForFailure(webPage, "Verify_Continue_Shopping_Link_Functionality_In_Cart_Page");
			log.error("Verify_Continue_Shopping_Link_Functionality_In_Cart_Page failed");
			log.error(e.getMessage());
			softAssert.assertAll();
			Assert.fail(e.getLocalizedMessage());
		}

	}

	@Test(priority = 214, enabled = true, description = "Verify Get credit button in cart page")
	public void Verify_Get_Credit_Button_In_Cart_Page() {
		SoftAssert softAssert = new SoftAssert();

		try {

			String[][] test = ExcelUtil.readExcelData(DataFilePath, "ProductPurchase",
					"Verify_Get_Credit_Button_In_Cart_Page");

			log.info("testing flow Verify_Get_Credit_Button_In_Cart_Page started");

			commonMethods.navigateToPage(webPage, testUrl, softAssert);

			if (testType.equalsIgnoreCase("Web")) {
				connsProductPurchasePage.Click_On_Element_JS(webPage, frenchDoor[1][1], softAssert);

			} else {
				connsProductPurchasePage.clickOnMobileMenuOption(webPage, mobileMenuData, softAssert);
				connsProductPurchasePage.Click_On_Element_JS(webPage, "(//div[@class='items']//li[@class='item' and contains(@data-val,'28')])[2]", softAssert);
			}
			connsProductPurchasePage.Click_On_PickUp_Only_Add_To_Cart_Button(webPage, pickupOnlyAddToCart, softAssert);

			softAssert.assertTrue(commonMethods.verifyElementisPresent(webPage, test[0][1], softAssert),
					"Get credit button in cart page is not displayed");

			softAssert.assertAll();

		} catch (Throwable e) {

			mainPage.getScreenShotForFailure(webPage, "Verify_Get_Credit_Button_In_Cart_Page");
			log.error("Verify_Get_Credit_Button_In_Cart_Page failed");
			log.error(e.getMessage());
			softAssert.assertAll();
			Assert.fail(e.getLocalizedMessage());
		}

	}

	@Test(priority = 215, enabled = true, description = "Verify 'More Detail' link in product availability section in cart page -- seen only for Pick-up only Product")
	public void Verify_More_Detail_Link_In_Cart_Page() {
		SoftAssert softAssert = new SoftAssert();

		try {
			String[][] test = ExcelUtil.readExcelData(DataFilePath, "ProductPurchase",
					"Verify_More_Detail_Link_In_Cart_Page");

			log.info("testing flow Verify_More_Detail_Link_In_Cart_Page started");

			commonMethods.navigateToPage(webPage, testUrl, softAssert);

			if (testType.equalsIgnoreCase("Web")) {
				connsProductPurchasePage.Click_On_Element_JS(webPage, frenchDoor[1][1], softAssert);

			} else {
				connsProductPurchasePage.clickOnMobileMenuOption(webPage, mobileMenuData, softAssert);
				connsProductPurchasePage.Click_On_Element_JS(webPage, "(//div[@class='items']//li[@class='item' and contains(@data-val,'28')])[2]", softAssert);
			}
			connsProductPurchasePage.Click_On_PickUp_Only_Add_To_Cart_Button(webPage, pickupOnlyAddToCart, softAssert);

			boolean isMoreDetailLinkDisplayed = commonMethods.verifyElementisPresent(webPage, test[4][1], softAssert);

			System.out.println("isMoreDetailLinkDisplayed:" + isMoreDetailLinkDisplayed);

			softAssert.assertTrue(isMoreDetailLinkDisplayed, "More Detail Link Is Not Displayed In Cart Page");

			softAssert.assertAll();

		} catch (Throwable e) {

			mainPage.getScreenShotForFailure(webPage, "Verify_More_Detail_Link_In_Cart_Page");
			log.error("Verify_More_Detail_Link_In_Cart_Page failed");
			log.error(e.getMessage());
			softAssert.assertAll();
			Assert.fail(e.getLocalizedMessage());
		}

	}

	@Test(priority = 216, enabled = true, description = "Lock Icon & McAffe Logo should be seen in Cart at bottom")
	public void Verify_Lock_Icon_McAffe_Logo_In_Cart_At_Bottom() {
		SoftAssert softAssert = new SoftAssert();

		try {
			String[][] test = ExcelUtil.readExcelData(DataFilePath, "ProductPurchase",
					"Verify_Lock_Icon_McAffe_Logo_In_Cart_At_Bottom");

			log.info("testing flow Verify_Lock_Icon_McAffe_Logo_In_Cart_At_Bottom started");

			commonMethods.navigateToPage(webPage, testUrl, softAssert);

			if (testType.equalsIgnoreCase("Web")) {
				connsProductPurchasePage.Click_On_Element_JS(webPage, frenchDoor[1][1], softAssert);

			} else {
				connsProductPurchasePage.clickOnMobileMenuOption(webPage, mobileMenuData, softAssert);
				connsProductPurchasePage.Click_On_Element_JS(webPage, "(//div[@class='items']//li[@class='item' and contains(@data-val,'28')])[2]", softAssert);
			}
			connsProductPurchasePage.Click_On_PickUp_Only_Add_To_Cart_Button(webPage, pickupOnlyAddToCart, softAssert);

			// ConnsHomePage.waitPageToLoad();

			Thread.sleep(4000);
			boolean isLockIconDisplayed = commonMethods.verifyElementisPresent(webPage, test[0][1], softAssert);

			boolean isMcAfeeLogoDisplayed = commonMethods.verifyElementisPresent(webPage, test[1][1], softAssert);
			log.info("isLockIconDisplayed : " + isLockIconDisplayed);
			log.info("isMcAfeeLogoDisplayed : " + isMcAfeeLogoDisplayed);
			softAssert.assertTrue(isLockIconDisplayed, "Lock Icon is not displayed in cart page at bottom ");

			softAssert.assertTrue(isMcAfeeLogoDisplayed, "McAfee Logo is not displayed in cart page at bottom ");

			softAssert.assertAll();

			log.info("Verify_Lock_Icon_McAffe_Logo_In_Cart_At_Bottom ended ");

		} catch (Throwable e) {

			mainPage.getScreenShotForFailure(webPage, "Verify_Lock_Icon_McAffe_Logo_In_Cart_At_Bottom");
			log.error("Verify_Lock_Icon_McAffe_Logo_In_Cart_At_Bottom failed");
			log.error(e.getMessage());
			softAssert.assertAll();
			Assert.fail(e.getLocalizedMessage());
		}

	}

	@Test(priority = 217, enabled = true, description = "Verify 'Continue' button by selecting 'Checkout as Guest' in checkout page")
	public void Verify_UI_Checkout_Page() {
		SoftAssert softAssert = new SoftAssert();

		try {
			String[][] test = ExcelUtil.readExcelData(DataFilePath, "ProductPurchase", "Verify_UI_Checkout_Page");

			log.info("testing flow Verify_UI_Checkout_Page started");

			commonMethods.navigateToPage(webPage, testUrl, softAssert);

			if (testType.equalsIgnoreCase("Web")) {
				connsProductPurchasePage.Click_On_Element_JS(webPage, frenchDoor[1][1], softAssert);

			} else {
				connsProductPurchasePage.clickOnMobileMenuOption(webPage, mobileMenuData, softAssert);
				connsProductPurchasePage.Click_On_Element_JS(webPage, "(//div[@class='items']//li[@class='item' and contains(@data-val,'28')])[2]", softAssert);
			}
			connsProductPurchasePage.Click_On_PickUp_Only_Add_To_Cart_Button(webPage, pickupOnlyAddToCart, softAssert);

			connsProductPurchasePage.Proceed_To_Checkout_Button(webPage, proceedToCheckout, softAssert);

			boolean isCheckoutSectionIsExpandededModeByDefault = commonMethods.verifyElementisPresent(webPage,
					test[3][1], softAssert);
			softAssert.assertTrue(isCheckoutSectionIsExpandededModeByDefault,
					"By default checkout method is not in expanded mode");

			commonMethods.clickElementbyXpath(webPage, test[1][1], softAssert);
			commonMethods.clickElementbyXpath(webPage, test[2][1], softAssert);

			boolean isCheckoutSectionCollapsed = commonMethods.verifyElementisPresent(webPage, test[4][1], softAssert);

			softAssert.assertTrue(isCheckoutSectionCollapsed, "checkout method is not in collapsed mode");

			boolean isBillingInformationSectionInExpanded = commonMethods.verifyElementisPresent(webPage, test[5][1],
					softAssert);

			softAssert.assertTrue(isBillingInformationSectionInExpanded,
					"Billing Information Section Is Not In Expanded Mode");

			softAssert.assertAll();

		} catch (Throwable e) {

			mainPage.getScreenShotForFailure(webPage, "Verify_UI_Checkout_Page");
			log.error("Verify_UI_Checkout_Page failed");
			log.error(e.getMessage());
			softAssert.assertAll();
			Assert.fail(e.getLocalizedMessage());
		}

	}

	@Test(priority = 218, enabled = true, description = "Verify 'Continue' button by selecting 'Register' in checkout page")
	public void Verify_UI_After_Selecting_Register_Button() {
		SoftAssert softAssert = new SoftAssert();

		try {
			String[][] test = ExcelUtil.readExcelData(DataFilePath, "ProductPurchase",
					"Verify_UI_After_Selecting_Register_Button");

			log.info("testing flow Verify_UI_Checkout_Page started");

			commonMethods.navigateToPage(webPage, testUrl, softAssert);

			if (testType.equalsIgnoreCase("Web")) {
				connsProductPurchasePage.Click_On_Element_JS(webPage, frenchDoor[1][1], softAssert);

			} else {
				connsProductPurchasePage.clickOnMobileMenuOption(webPage, mobileMenuData, softAssert);
				connsProductPurchasePage.Click_On_Element_JS(webPage, "(//div[@class='items']//li[@class='item' and contains(@data-val,'28')])[2]", softAssert);
			}
			connsProductPurchasePage.Click_On_PickUp_Only_Add_To_Cart_Button(webPage, pickupOnlyAddToCart, softAssert);

			connsProductPurchasePage.Proceed_To_Checkout_Button(webPage, proceedToCheckout, softAssert);
			/*
			 * checking if checkout method is in expanded mode by default
			 */
			boolean isCheckoutSectionIsExpanded = commonMethods.verifyElementisPresent(webPage,
					"//li[@id='opc-login' and @class='section allow active']", softAssert);
			softAssert.assertTrue(isCheckoutSectionIsExpanded, "By default checkout method is not in expanded mode");

			commonMethods.clickElementbyXpath(webPage, test[1][1], softAssert);
			commonMethods.clickElementbyXpath(webPage, test[2][1], softAssert);

			/*
			 * 
			 * checking if checkout method is collapsed after clicking continue
			 * button
			 */
			boolean isCheckoutSectionCollapsed = commonMethods.verifyElementisPresent(webPage,
					"//li[@id='opc-login' and @class='section allow']", softAssert);

			softAssert.assertTrue(isCheckoutSectionCollapsed, "checkout method is not in collapsed mode");

			/*
			 * checking if checkout method is collapsed after clicking continue
			 * button
			 */
			boolean isBillingInformationSectionInExpandMode = commonMethods.verifyElementisPresent(webPage,
					"//li[@id='opc-billing' and @class='section allow active']", softAssert);

			softAssert.assertTrue(isBillingInformationSectionInExpandMode,
					"Billing Information Section Is Not In Expanded Mode");

			softAssert.assertAll();

		} catch (Throwable e) {

			mainPage.getScreenShotForFailure(webPage, "Verify_UI_After_Selecting_Register_Button");
			log.error("Verify_UI_After_Selecting_Register_Button failed");
			log.error(e.getMessage());
			softAssert.assertAll();
			Assert.fail(e.getLocalizedMessage());
		}

	}

	/*
	 * getting below URL after clicking login button - for more info open
	 * screenshot in output folder
	 * http://connsecommdev-1365538477.us-east-1.elb.amazonaws.com/uat/customer/
	 * account/loginPost/
	 */

	@Test(priority = 219, enabled = true, description = "Verify 'Continue' button by selecting 'Register' in checkout page")
	public void Verify_Login_Button_With_Valid_Credentials() {
		SoftAssert softAssert = new SoftAssert();

		try {

			String[][] test = ExcelUtil.readExcelData(DataFilePath, "ProductPurchase",
					"Verify_Login_Button_With_Valid_Credentials");

			String[][] pickupAvialableProduct = ExcelUtil.readExcelData(DataFilePath, "ProductPurchase",
					"Click_Add_To_Cart_As_Per_Avilability_Message_Pickup2");

			log.info("testing flow Verify_Login_Button_With_Valid_Credentials started");

			commonMethods.navigateToPage(webPage, testUrl, softAssert);

			if (testType.equalsIgnoreCase("Web")) {
				connsProductPurchasePage.Click_On_Element_JS(webPage, frenchDoor[1][1], softAssert);

			} else {
				connsProductPurchasePage.clickOnMobileMenuOption(webPage, mobileMenuData, softAssert);
				connsProductPurchasePage.Click_On_Element_JS(webPage, "(//div[@class='items']//li[@class='item' and contains(@data-val,'28')])[2]", softAssert);
			}
			connsProductPurchasePage.Click_On_In_Stock_With_Delivery_Available(webPage, pickupAvialableProduct,
					softAssert);

			connsProductPurchasePage.Proceed_To_Checkout_Button(webPage, proceedToCheckout, softAssert);
			/*
			 * checking if checkout method is in expanded mode by default
			 */
			boolean isCheckoutSectionIsExpanded = commonMethods.verifyElementisPresent(webPage, test[4][1], softAssert);

			softAssert.assertTrue(isCheckoutSectionIsExpanded,
					"Could Not logged in as Login window is in collapced mode");

			commonMethods.sendKeysbyXpath(webPage, test[1][1], test[1][3], softAssert);

			commonMethods.sendKeysbyXpath(webPage, test[2][1], test[2][3], softAssert);

			commonMethods.clickElementbyXpath(webPage, test[3][1], softAssert);

			boolean isBillingInformationSectionInExpandMode = commonMethods.verifyElementisPresent(webPage, test[5][1],
					softAssert);

			softAssert.assertTrue(isBillingInformationSectionInExpandMode, "Login is not successful");

			softAssert.assertAll();

		} catch (Throwable e) {

			mainPage.getScreenShotForFailure(webPage, "Verify_Login_Button_With_Valid_Credentials");
			log.error("Verify_Login_Button_With_Valid_Credentials failed");
			log.error(e.getMessage());
			softAssert.assertAll();
			Assert.fail(e.getLocalizedMessage());
		}

	}

	@Test(priority = 220, enabled = true, description = "Verify 'Forgot Your Password?' functionality in checkout page for login section")
	public void Verify_Forgot_Your_Password_Functionality() {
		SoftAssert softAssert = new SoftAssert();

		try {

			String[][] test = ExcelUtil.readExcelData(DataFilePath, "ProductPurchase",
					"Verify_Forgot_Your_Password_Functionality");

			log.info("Verify_Forgot_Your_Password_Functionality started");

			commonMethods.navigateToPage(webPage, testUrl, softAssert);

			if (testType.equalsIgnoreCase("Web")) {
				connsProductPurchasePage.Click_On_Element_JS(webPage, frenchDoor[1][1], softAssert);

			} else {
				connsProductPurchasePage.clickOnMobileMenuOption(webPage, mobileMenuData, softAssert);
				connsProductPurchasePage.Click_On_Element_JS(webPage, "(//div[@class='items']//li[@class='item' and contains(@data-val,'28')])[2]", softAssert);
			}
			connsProductPurchasePage.Click_On_PickUp_Only_Add_To_Cart_Button(webPage, pickupOnlyAddToCart, softAssert);

			connsProductPurchasePage.Proceed_To_Checkout_Button(webPage, proceedToCheckout, softAssert);

			boolean isForgotYourPasswordLinkDisplayed = commonMethods.verifyElementisPresent(webPage, test[1][1],
					softAssert);

			softAssert.assertTrue(isForgotYourPasswordLinkDisplayed, "forgot your password link is not displayed");

			if (isForgotYourPasswordLinkDisplayed) {

				commonMethods.clickElementbyXpath(webPage, test[1][1], softAssert);

				String ActualURL_ForgotYourPasswordPage = commonMethods.getPageUrl(webPage, softAssert);

				boolean isUserNavigatedToForgotYourPasswordPage = ActualURL_ForgotYourPasswordPage.contains(test[1][4]);

				softAssert.assertTrue(isUserNavigatedToForgotYourPasswordPage,
						"Forgot your password page url is not matching with expected url:" + "Expected Url is:"
								+ test[1][4] + "Actual url is:" + ActualURL_ForgotYourPasswordPage);
			}

			softAssert.assertAll();

		} catch (Throwable e) {

			mainPage.getScreenShotForFailure(webPage, "Verify_Forgot_Your_Password_Functionality");
			log.error("Verify_Forgot_Your_Password_Functionality failed");
			log.error(e.getMessage());
			softAssert.assertAll();
			Assert.fail(e.getLocalizedMessage());
		}

	}

	@Test(priority = 221, enabled = true, description = "Verify the 'continue' button without selecting shipping option")
	public void Verify_Continue_Button_Without_Selecting_Payment_Info() {
		SoftAssert softAssert = new SoftAssert();
		String actualText = null;
		String expectedText = null;

		try {

			String[][] test = ExcelUtil.readExcelData(DataFilePath, "ProductPurchase",
					"Verify_Continue_Button_Without_Selecting_Payment_Info");

			log.info("Verify_Continue_Button_Without_Selecting_Payment_Info started");

			commonMethods.navigateToPage(webPage, testUrl, softAssert);

			if (testType.equalsIgnoreCase("Web")) {
				connsProductPurchasePage.Click_On_Element_JS(webPage, frenchDoor[1][1], softAssert);

			} else {
				connsProductPurchasePage.clickOnMobileMenuOption(webPage, mobileMenuData, softAssert);
				connsProductPurchasePage.Click_On_Element_JS(webPage, "(//div[@class='items']//li[@class='item' and contains(@data-val,'28')])[2]", softAssert);
			}
			connsProductPurchasePage.Click_On_PickUp_Only_Add_To_Cart_Button(webPage, pickupOnlyAddToCart, softAssert);

			connsProductPurchasePage.Proceed_To_Checkout_Button(webPage, proceedToCheckout, softAssert);

			connsProductPurchasePage.Checkout_Guest(webPage, checkoutGuest, softAssert);

			connsProductPurchasePage.Submit_Billing_Information(webPage, submitBillingInfo, softAssert);

			// clicking on billing info continue button
			commonMethods.clickElementbyXpath(webPage, submitBillingInfo[9][1], softAssert);
			Thread.sleep(5000);
			// clicking on pickup location continue button
			// commonMethods.clickElementbyXpath(webPage,
			// submitBillingInfo[10][1], softAssert);
			connsProductPurchasePage.Click_On_Element_JS(webPage, submitBillingInfo[10][1], softAssert);
			// Thread.sleep(10000);

			commonMethods.clickElementbyXpath(webPage, test[0][1], softAssert);

			Alert alert = webPage.getDriver().switchTo().alert();
			actualText = alert.getText();

			expectedText = test[1][4];
			alert.accept();
			softAssert.assertTrue(expectedText.equalsIgnoreCase(actualText), "expected alert message is not displayed: "
					+ " Expected Text is:" + expectedText + " Actual text is:" + actualText);

			softAssert.assertAll();

		} catch (Throwable e) {

			mainPage.getScreenShotForFailure(webPage, "Verify_Continue_Button_Without_Selecting_Payment_Info");
			log.error("Verify_Continue_Button_Without_Selecting_Payment_Info failed");
			log.error(e.getMessage());
			softAssert.assertAll();
			Assert.fail(e.getLocalizedMessage());
		}

	}

	@Test(priority = 222, enabled = true, description = "Verify_Payment_Info_ConnsCredit")
	public void Verify_Payment_Info_ConnsCredit() {
		SoftAssert softAssert = new SoftAssert();
		String actualURL = null;
		String expectedURL = null;

		try {

			String[][] pickupAvialableProduct = ExcelUtil.readExcelData(DataFilePath, "ProductPurchase",
					"Click_Add_To_Cart_As_Per_Avilability_Message_Pickup2");

			System.out.println("after reading pickupAvialableProduct");

			String[][] test = ExcelUtil.readExcelData(DataFilePath, "ProductPurchase",
					"Verify_Payment_Info_Conns_Credit");

			log.info("Verify_Payment_Info_ConnsCredit started");

			commonMethods.navigateToPage(webPage, testUrl, softAssert);

			if (testType.equalsIgnoreCase("Web")) {
				connsProductPurchasePage.Click_On_Element_JS(webPage, frenchDoor[1][1], softAssert);

			} else {
				connsProductPurchasePage.clickOnMobileMenuOption(webPage, mobileMenuData, softAssert);
				connsProductPurchasePage.Click_On_Element_JS(webPage, "(//div[@class='items']//li[@class='item' and contains(@data-val,'28')])[2]", softAssert);
			}
			
			connsProductPurchasePage.Click_On_In_Stock_With_Delivery_Available(webPage, pickupAvialableProduct,
					softAssert);

			connsProductPurchasePage.Proceed_To_Checkout_Button(webPage, proceedToCheckout, softAssert);

			connsProductPurchasePage.Checkout_Guest(webPage, checkoutGuest, softAssert);

			connsProductPurchasePage.Submit_Billing_Information(webPage, submitBillingInfo, softAssert);

			commonMethods.clickElementbyXpath(webPage, submitBillingInfo[9][1], softAssert);

			Thread.sleep(3000);

			commonMethods.clickElementbyXpath(webPage, submitBillingInfo[10][1], softAssert);

			Thread.sleep(3000);

			commonMethods.clickElementbyXpath(webPage, test[0][1], softAssert);

			commonMethods.clickElementbyXpath(webPage, test[1][1], softAssert);

			Thread.sleep(3000);

			commonMethods.clickElementbyXpath(webPage, test[2][1], softAssert);

			Thread.sleep(3000);

			actualURL = commonMethods.getPageUrl(webPage, softAssert);

			expectedURL = test[2][4];

			softAssert.assertTrue(actualURL.contains(expectedURL), "expected alert message is not displayed: "
					+ " Expected Text is:" + expectedURL + " Actual text is:" + actualURL);

			softAssert.assertAll();

		} catch (Throwable e) {

			mainPage.getScreenShotForFailure(webPage, "Verify_Payment_Info_ConnsCredit");
			log.error("Verify_Payment_Info_ConnsCredit failed");
			log.error(e.getMessage());
			softAssert.assertAll();
			Assert.fail(e.getLocalizedMessage());
		}

	}

	@Test(priority = 223, enabled = true, description = "Verify the checkout flow using PayPal option in payment info section in checkout page")
	public void Verify_Checkout_Flow_Paypal() {
		SoftAssert softAssert = new SoftAssert();
		String actualPaypalURL = null;
		String expectedPaypalURL = null;

		try {

			String[][] test = ExcelUtil.readExcelData(DataFilePath, "ProductPurchase", "Verify_Checkout_Flow_Paypal");

			log.info("Verify_Checkout_Flow_Paypal started");

			commonMethods.navigateToPage(webPage, testUrl, softAssert);

			if (testType.equalsIgnoreCase("Web")) {
				connsProductPurchasePage.Click_On_Element_JS(webPage, frenchDoor[1][1], softAssert);

			} else {
				connsProductPurchasePage.clickOnMobileMenuOption(webPage, mobileMenuData, softAssert);
				connsProductPurchasePage.Click_On_Element_JS(webPage, "(//div[@class='items']//li[@class='item' and contains(@data-val,'28')])[2]", softAssert);
			}
			connsProductPurchasePage.Click_On_PickUp_Only_Add_To_Cart_Button(webPage, pickupOnlyAddToCart, softAssert);

			connsProductPurchasePage.Proceed_To_Checkout_Button(webPage, proceedToCheckout, softAssert);

			connsProductPurchasePage.Checkout_Guest(webPage, checkoutGuest, softAssert);

			connsProductPurchasePage.Submit_Billing_Information(webPage, submitBillingInfo, softAssert);

			// clicking on billing info continue button
			commonMethods.clickElementbyXpath(webPage, submitBillingInfo[9][1], softAssert);
			Thread.sleep(3000);

			// clicking on pickup location continue button
			commonMethods.clickElementbyXpath(webPage, submitBillingInfo[10][1], softAssert);
			Thread.sleep(6000);

			// commonMethods.clickElementbyXpath(webPage,
			// submitShippingInfo[9][1], softAssert);

			// Thread.sleep(3000);

			connsProductPurchasePage.Submit_Paypal_Payment_Info(webPage, paypalInfo, softAssert);
			Thread.sleep(3000);
			actualPaypalURL = commonMethods.getPageUrl(webPage, softAssert);

			expectedPaypalURL = test[0][4];

			System.out.println("actualPaypalURL:" + actualPaypalURL);
			System.out.println("expectedPaypalURL:" + expectedPaypalURL);

			softAssert.assertTrue(actualPaypalURL.contains(expectedPaypalURL),
					"expected alert message is not displayed: " + " Expected Text is:" + expectedPaypalURL
							+ " Actual text is:" + actualPaypalURL);

			softAssert.assertAll();

		} catch (Throwable e) {

			mainPage.getScreenShotForFailure(webPage, "Verify_Checkout_Flow_Paypal");
			log.error("Verify_Checkout_Flow_Paypal failed");
			log.error(e.getMessage());
			softAssert.assertAll();
			Assert.fail(e.getLocalizedMessage());
		}

	}

	@Test(priority = 224, enabled = true, description = "Verify the checkout flow using PayPal option in payment info section in checkout page")
	public void Verify_Edit_Your_Cart_Link_Functionality_In_Order_Review_Section() {
		SoftAssert softAssert = new SoftAssert();
		String actualURL = null;
		String expectedURL = null;

		try {

			String[][] test = ExcelUtil.readExcelData(DataFilePath, "ProductPurchase",
					"Verify_Edit_Your_Cart_Link_Functionality_In_Order_Review_Section");

			String[][] pickupAvialableProduct = ExcelUtil.readExcelData(DataFilePath, "ProductPurchase",
					"Click_Add_To_Cart_As_Per_Avilability_Message_Pickup2");

			log.info("Verify_Edit_Your_Cart_Link_Functionality_In_Order_Review_Section started");

			commonMethods.navigateToPage(webPage, testUrl, softAssert);

			if (testType.equalsIgnoreCase("Web")) {
				connsProductPurchasePage.Click_On_Element_JS(webPage, frenchDoor[1][1], softAssert);
				commonMethods.selectDropdownByValue(webPage, "(//select[@class='hasCustomSelect'])[7]", "28", softAssert);

			} else {
				connsProductPurchasePage.clickOnMobileMenuOption(webPage, mobileMenuData, softAssert);
				connsProductPurchasePage.Click_On_Element_JS(webPage, "(//div[@class='items']//li[@class='item' and contains(@data-val,'28')])[2]", softAssert);
			}
			
			// adding pickup only product to cart
			connsProductPurchasePage.Click_On_In_Stock_With_Delivery_Available(webPage, pickupAvialableProduct,
					softAssert);

			connsProductPurchasePage.Proceed_To_Checkout_Button(webPage, proceedToCheckout, softAssert);

			connsProductPurchasePage.Checkout_Guest(webPage, checkoutGuest, softAssert);

			connsProductPurchasePage.Submit_Billing_Information(webPage, submitBillingInfo, softAssert);

			// clicking on billing info continue button
			commonMethods.clickElementbyXpath(webPage, submitBillingInfo[9][1], softAssert);

			Thread.sleep(3000);

			// clicking on pickup location continue button
			// commonMethods.clickElementbyXpath(webPage,
			// submitBillingInfo[10][1], softAssert);
			connsProductPurchasePage.Click_On_Element_JS(webPage, submitBillingInfo[10][1], softAssert);
			Thread.sleep(3000);

			// clicking on cash on delivery radio button
			commonMethods.clickElementbyXpath(webPage, test[0][1], softAssert);
			// clicking on checkout method continue button
			commonMethods.clickElementbyXpath(webPage, test[1][1], softAssert);
			//
			commonMethods.clickElementbyXpath(webPage, test[2][1], softAssert);

			actualURL = commonMethods.getPageUrl(webPage, softAssert);
			expectedURL = test[2][4];

			softAssert.assertTrue(actualURL.contains(expectedURL),
					"'Edit Your Cart' link is not displayed in Order Review section: " + " expected is: " + expectedURL
							+ " actual is: " + actualURL);

			softAssert.assertAll();

		} catch (Throwable e) {

			mainPage.getScreenShotForFailure(webPage, "Verify_Payment_Info_Continue_Button_By_Selecting_ConnsCredit");
			log.error("Verify_Payment_Info_Continue_Button_By_Selecting_ConnsCredit failed");
			log.error(e.getMessage());
			softAssert.assertAll();
			Assert.fail(e.getLocalizedMessage());
		}

	}

	@Test(priority = 225, enabled = true, description = "Verify 'CONTINUE SHOPPING' button in order confirmation page")
	public void Verify_Continue_Shopping_Button_Order_Confirmation_Page() {
		SoftAssert softAssert = new SoftAssert();
		String actualContinueShoppingButtonNavigation = null;
		String expectedURL = null;

		try {

			String[][] test = ExcelUtil.readExcelData(DataFilePath, "ProductPurchase",
					"Verify_Continue_Shopping_Button_Order_Confirmation_Page");
			String[][] pickupAvialableProduct = ExcelUtil.readExcelData(DataFilePath, "ProductPurchase",
					"Click_Add_To_Cart_As_Per_Avilability_Message_Pickup2");
			log.info("Verify_Continue_Shopping_Button_Order_Confirmation_Page started");

			commonMethods.navigateToPage(webPage, testUrl, softAssert);

			if (testType.equalsIgnoreCase("Web")) {
				connsProductPurchasePage.Click_On_Element_JS(webPage, frenchDoor[1][1], softAssert);

			} else {
				connsProductPurchasePage.clickOnMobileMenuOption(webPage, mobileMenuData, softAssert);
				connsProductPurchasePage.Click_On_Element_JS(webPage, "(//div[@class='items']//li[@class='item' and contains(@data-val,'28')])[2]", softAssert);
			}
			connsProductPurchasePage.Click_On_In_Stock_With_Delivery_Available(webPage, pickupAvialableProduct,
					softAssert);

			connsProductPurchasePage.Proceed_To_Checkout_Button(webPage, proceedToCheckout, softAssert);

			connsProductPurchasePage.Checkout_Guest(webPage, checkoutGuest, softAssert);

			connsProductPurchasePage.Submit_Billing_Information(webPage, submitBillingInfo, softAssert);

			// clicking on billing info continue button
			commonMethods.clickElementbyXpath(webPage, submitBillingInfo[9][1], softAssert);

			Thread.sleep(3000);

			// clicking on pickup location continue button
			// commonMethods.clickElementbyXpath(webPage,
			// submitBillingInfo[10][1], softAssert);
			connsProductPurchasePage.Click_On_Element_JS(webPage, submitBillingInfo[10][1], softAssert);
			Thread.sleep(3000);

			// clicking on cash on delivery radio button
			commonMethods.clickElementbyXpath(webPage, test[0][1], softAssert);
			// clicking on checkout method continue button
			commonMethods.clickElementbyXpath(webPage, test[1][1], softAssert);
			//
			commonMethods.clickElementbyXpath(webPage, test[2][1], softAssert);

			// order review button
			commonMethods.clickElementbyXpath(webPage, ".//*[@id='review-buttons-container']/button", softAssert);

			// continue shopping button on order confirmation page
			commonMethods.clickElementbyXpath(webPage, "//button[@title='Continue Shopping']", softAssert);

			actualContinueShoppingButtonNavigation = commonMethods.getPageUrl(webPage, softAssert);
			expectedURL = testUrl;
			System.out.println("expectedURL:" + expectedURL);
			System.out.println("actualURL:" + actualContinueShoppingButtonNavigation);

			softAssert.assertEquals(actualContinueShoppingButtonNavigation, expectedURL,
					"continue shopping button functionality failed : " + "expectedURL is:" + expectedURL
							+ "actualURL is:" + actualContinueShoppingButtonNavigation);

			softAssert.assertAll();

		} catch (Throwable e) {

			mainPage.getScreenShotForFailure(webPage, "Verify_Continue_Shopping_Button_Order_Confirmation_Page");
			log.error("Verify_Continue_Shopping_Button_Order_Confirmation_Page failed");
			log.error(e.getMessage());
			softAssert.assertAll();
			Assert.fail(e.getLocalizedMessage());

		}

	}

	/*
	 * 
	 * UC - 004 - 2 test cases are clubbed in this test method ( TC_004 , TC_006
	 * )
	 */

	@Test(priority = 226, enabled = true, description = "Verify Zip Code Functionality for In-Stock Product and Verify ADD TO CART on overlay without entering input in Zip code")
	public void Verify_In_Stock_Product_Zip_Code_Functionality() {
		SoftAssert softAssert = new SoftAssert();
		/*
		 * String actualErrorMessage = null; String expectedErrorMessage = null;
		 */

		try {

			addToCartCommon = ExcelUtil.readExcelData(DataFilePath, "ProductPurchase",
					"Click_Add_To_Cart_As_Per_Avilability_Message");

			commonMethods.navigateToPage(webPage, testUrl, softAssert);

			if (testType.equalsIgnoreCase("Web")) {
				connsProductPurchasePage.Click_On_Element_JS(webPage, frenchDoor[1][1], softAssert);

			} else {
				connsProductPurchasePage.clickOnMobileMenuOption(webPage, mobileMenuData, softAssert);
				connsProductPurchasePage.Click_On_Element_JS(webPage, "(//div[@class='items']//li[@class='item' and contains(@data-val,'28')])[2]", softAssert);
			}
			connsProductPurchasePage.Click_Add_To_Cart_As_Per_Avilability_Message(webPage, addToCartCommon, softAssert);

			softAssert.assertAll();

		} catch (Throwable e) {

			mainPage.getScreenShotForFailure(webPage, "Verify_Continue_Shopping_Button_Order_Confirmation_Page");
			log.error("Verify_Continue_Shopping_Button_Order_Confirmation_Page failed");
			log.error(e.getMessage());
			softAssert.assertAll();
			Assert.fail(e.getLocalizedMessage());
		}

	}
	/*
	 * 
	 * TC_005
	 */

	@Test(priority = 227, enabled = true, description = "Verify Zip Code textbox with invalid input")
	public void Verify_In_Stock_Product_Zip_Code_Functionality_In_Valid_Data() {
		SoftAssert softAssert = new SoftAssert();
		String actualErrorMessage = null;
		String expectedErrorMessage = null;

		try {

			zipCodeInValid = ExcelUtil.readExcelData(DataFilePath, "ProductPurchase",
					"Click_Add_To_Cart_As_Per_Avilability_Message1");

			commonMethods.navigateToPage(webPage, testUrl, softAssert);

			if (testType.equalsIgnoreCase("Web")) {
				connsProductPurchasePage.Click_On_Element_JS(webPage, frenchDoor[1][1], softAssert);

			} else {
				connsProductPurchasePage.clickOnMobileMenuOption(webPage, mobileMenuData, softAssert);
				connsProductPurchasePage.Click_On_Element_JS(webPage, "(//div[@class='items']//li[@class='item' and contains(@data-val,'28')])[2]", softAssert);
			}
			connsProductPurchasePage.Click_Add_To_Cart_As_Per_Avilability_Message(webPage, zipCodeInValid, softAssert);

			softAssert.assertAll();

		} catch (Throwable e) {

			mainPage.getScreenShotForFailure(webPage, "Verify_In_Stock_Product_Zip_Code_Functionality_In_Valid_Data");
			log.error("Verify_In_Stock_Product_Zip_Code_Functionality_In_Valid_Data failed");
			log.error(e.getMessage());
			softAssert.assertAll();
			Assert.fail(e.getLocalizedMessage());
		}

	}

	/*
	 * 
	 * TC_007
	 */

	@Test(priority = 228, enabled = true, description = "Verify ADD TO CART on overlay with valid input in Zip code")
	public void Verify_In_Stock_Product_Zip_Code_Functionality_Valid_Data() {
		SoftAssert softAssert = new SoftAssert();

		String actualCartPageURL = null;
		String expectedCartPageURL = null;

		try {

			zipCodeValid = ExcelUtil.readExcelData(DataFilePath, "ProductPurchase",
					"Click_Add_To_Cart_As_Per_Avilability_Message2");

			commonMethods.navigateToPage(webPage, testUrl, softAssert);

			if (testType.equalsIgnoreCase("Web")) {
				connsProductPurchasePage.Click_On_Element_JS(webPage, frenchDoor[1][1], softAssert);
				commonMethods.selectDropdownByValue(webPage, "(//select[@class='hasCustomSelect'])[7]", "28", softAssert);

			} else {
				connsProductPurchasePage.clickOnMobileMenuOption(webPage, mobileMenuData, softAssert);
				connsProductPurchasePage.Click_On_Element_JS(webPage, "(//div[@class='items']//li[@class='item' and contains(@data-val,'28')])[2]", softAssert);
			}
			

			connsProductPurchasePage.Click_On_In_Stock_With_Delivery_Available(webPage, zipCodeValid, softAssert);

			actualCartPageURL = commonMethods.getPageUrl(webPage, softAssert);
			System.out.println("actualCartPageURL:" + actualCartPageURL);
			expectedCartPageURL = zipCodeValid[5][4];
			System.out.println("expectedCartPageURL:" + expectedCartPageURL);

			softAssert.assertTrue(actualCartPageURL.contains(zipCodeValid[5][4]),
					"cart page URL is not seen on clicking add to cart for avilable product:" + "Actual URL:"
							+ actualCartPageURL);

			softAssert.assertAll();

		} catch (Throwable e) {

			mainPage.getScreenShotForFailure(webPage, "Verify_In_Stock_Product_Zip_Code_Functionality_In_Valid_Data");
			log.error("Verify_In_Stock_Product_Zip_Code_Functionality_In_Valid_Data failed");
			log.error(e.getMessage());
			softAssert.assertAll();
			Assert.fail(e.getLocalizedMessage());
		}

	}

	/*
	 * ------------------------------------- UC-005
	 * -------------------------------------------------------------------------
	 * ------
	 */

	/*
	 * 
	 * UC - 005 - 2 test cases are clubbed in this test method ( TC_002 , TC_006
	 * )
	 */

	@Test(priority = 229, enabled = true, description = "Verify Zip code functionality for Pick-up only Product")
	public void Verify_Pickup_Only_Product_Zip_Code_Functionality() {
		SoftAssert softAssert = new SoftAssert();

		try {

			String[][] testData2 = ExcelUtil.readExcelData(DataFilePath, "ProductPurchase",
					"Click_Add_To_Cart_As_Per_Avilability_Message_Pickup");

			commonMethods.navigateToPage(webPage, testUrl, softAssert);

			if (testType.equalsIgnoreCase("Web")) {
				connsProductPurchasePage.Click_On_Element_JS(webPage, frenchDoor[1][1], softAssert);
				commonMethods.selectDropdownByValue(webPage, "(//select[@class='hasCustomSelect'])[7]", "28", softAssert);

			} else {
				connsProductPurchasePage.clickOnMobileMenuOption(webPage, mobileMenuData, softAssert);
				connsProductPurchasePage.Click_On_Element_JS(webPage, "(//div[@class='items']//li[@class='item' and contains(@data-val,'28')])[2]", softAssert);
			}
			

			/*
			 * below method will get In_Stock availability product where Add to
			 * cart is displayed
			 */
			connsProductPurchasePage.Click_Add_To_Cart_As_Per_Avilability_Message(webPage, testData2, softAssert);

			softAssert.assertAll();

		} catch (Throwable e) {

			mainPage.getScreenShotForFailure(webPage, "Verify_Pickup_Only_Product_Zip_Code_Functionality");
			log.error("Verify_Pickup_Only_Product_Zip_Code_Functionality failed");
			log.error(e.getMessage());
			softAssert.assertAll();
			Assert.fail(e.getLocalizedMessage());
		}

	}

	/*
	 * TC_005 - Verify Zip Code textbox with invalid input
	 */
	@Test(priority = 230, enabled = true, description = "Verify Zip Code textbox with invalid input")
	public void Verify_Pickup_Only_Product_Zip_Code_Functionality_Invalid_Data() {

		SoftAssert softAssert = new SoftAssert();

		try {
			String[][] testData2 = ExcelUtil.readExcelData(DataFilePath, "ProductPurchase",
					"Click_Add_To_Cart_As_Per_Avilability_Message_Pickup1");

			commonMethods.navigateToPage(webPage, testUrl, softAssert);

			if (testType.equalsIgnoreCase("Web")) {
				connsProductPurchasePage.Click_On_Element_JS(webPage, frenchDoor[1][1], softAssert);

			} else {
				connsProductPurchasePage.clickOnMobileMenuOption(webPage, mobileMenuData, softAssert);
				connsProductPurchasePage.Click_On_Element_JS(webPage, "(//div[@class='items']//li[@class='item' and contains(@data-val,'28')])[2]", softAssert);
			}

			/*
			 * below method will get In_Stock availability product where Add to
			 * cart is displayed
			 */
			connsProductPurchasePage.Click_Add_To_Cart_As_Per_Avilability_Message(webPage, testData2, softAssert);

			softAssert.assertAll();

		} catch (Throwable e) {

			mainPage.getScreenShotForFailure(webPage, "Verify_Pickup_Only_Product_Zip_Code_Functionality_Invalid_Data");
			log.error("Verify_Pickup_Only_Product_Zip_Code_Functionality_Invalid_Data failed");
			log.error(e.getMessage());
			softAssert.assertAll();
			Assert.fail(e.getLocalizedMessage());
		}

	}

	@Test(priority = 231, enabled = true, description = "Verify Zip Code textbox with invalid input")
	public void Verify_Pickup_Only_Product_Zip_Code_Functionality_valid_Data() {

		SoftAssert softAssert = new SoftAssert();
		String actualCartPageURL = null;
		String expectedCartPageURL = null;

		try {

			String[][] testData2 = ExcelUtil.readExcelData(DataFilePath, "ProductPurchase",
					"Click_Add_To_Cart_As_Per_Avilability_Message_Pickup2");

			commonMethods.navigateToPage(webPage, testUrl, softAssert);

			if (testType.equalsIgnoreCase("Web")) {
				connsProductPurchasePage.Click_On_Element_JS(webPage, frenchDoor[1][1], softAssert);
				commonMethods.selectDropdownByValue(webPage, "(//select[@class='hasCustomSelect'])[7]", "28", softAssert);
			} else {
				connsProductPurchasePage.clickOnMobileMenuOption(webPage, mobileMenuData, softAssert);
				connsProductPurchasePage.Click_On_Element_JS(webPage, "(//div[@class='items']//li[@class='item' and contains(@data-val,'28')])[2]", softAssert);
			}
			

			connsProductPurchasePage.Click_On_In_Stock_With_Delivery_Available(webPage, testData2, softAssert);

			actualCartPageURL = commonMethods.getPageUrl(webPage, softAssert);
			System.out.println("actualCartPageURL:" + actualCartPageURL);
			expectedCartPageURL = testData2[5][4];
			System.out.println("expectedCartPageURL:" + expectedCartPageURL);

			softAssert.assertTrue(actualCartPageURL.contains(expectedCartPageURL),
					"Cart Page redirection is not as expected " + " Expected is: " + expectedCartPageURL
							+ "Actual URL is: " + actualCartPageURL);

			softAssert.assertAll();

		} catch (Throwable e) {

			mainPage.getScreenShotForFailure(webPage, "Verify_Pickup_Only_Product_Zip_Code_Functionality_Invalid_Data");
			log.error("Verify_Pickup_Only_Product_Zip_Code_Functionality_Invalid_Data failed");
			log.error(e.getMessage());
			softAssert.assertAll();
			Assert.fail(e.getLocalizedMessage());
		}

	}

	/*
	 * 
	 * ---------------------------------------------- UC - 006
	 * ----------------------------------------------------------------------
	 */

	/*
	 * 
	 * TC - 002
	 */

	@Test(priority = 232, enabled = true, description = "Verify Zip Code textbox with invalid input")
	public void Verify_Zip_Code_Functionality_In_Stock_PickUp_Product() {

		SoftAssert softAssert = new SoftAssert();

		try {
			String[][] clickOnAddToCart = ExcelUtil.readExcelData(DataFilePath, "ProductPurchase",
					"Add_In_Stock_Pickup_Only_Product_To_Cart");

			commonMethods.navigateToPage(webPage, testUrl, softAssert);

			if (testType.equalsIgnoreCase("Web")) {
				connsProductPurchasePage.Click_On_Element_JS(webPage, frenchDoor[1][1], softAssert);
				commonMethods.selectDropdownByValue(webPage, "(//select[@class='hasCustomSelect'])[7]", "28", softAssert);

			} else {
				connsProductPurchasePage.clickOnMobileMenuOption(webPage, mobileMenuData, softAssert);
				connsProductPurchasePage.Click_On_Element_JS(webPage, "(//div[@class='items']//li[@class='item' and contains(@data-val,'28')])[2]", softAssert);
			}
			

			connsProductPurchasePage.Page_Zip_Code_Functionality_In_Stock_PickUp_Product(webPage, clickOnAddToCart,
					softAssert);

			softAssert.assertAll();

		} catch (Throwable e) {

			mainPage.getScreenShotForFailure(webPage, "Verify_Pickup_Only_Product_Zip_Code_Functionality_Invalid_Data");
			log.error("Verify_Pickup_Only_Product_Zip_Code_Functionality_Invalid_Data failed");
			log.error(e.getMessage());
			softAssert.assertAll();
			Assert.fail(e.getLocalizedMessage());
		}

	}

	@Test(priority = 233, enabled = true, description = "Verify Zip Code textbox with invalid input")
	public void Verify_Add_To_Cart_Functionality_In_Stock_PickUp_Product_Valid_Data() {

		SoftAssert softAssert = new SoftAssert();
		String actualPageURL = null;
		String expectedPageURL = null;

		try {

			String[][] clickOnAddToCart = ExcelUtil.readExcelData(DataFilePath, "ProductPurchase",
					"Add_In_Stock_Pickup_Only_Product_To_Cart");
			String[][] testData2 = ExcelUtil.readExcelData(DataFilePath, "ProductPurchase",
					"Verify_Add_To_Cart_Functionality_In_Stock_PickUp_Product_Valid_Data");

			commonMethods.navigateToPage(webPage, testUrl, softAssert);

			if (testType.equalsIgnoreCase("Web")) {
				connsProductPurchasePage.Click_On_Element_JS(webPage, frenchDoor[1][1], softAssert);
				commonMethods.selectDropdownByValue(webPage, "(//select[@class='hasCustomSelect'])[7]", "28", softAssert);

			} else {
				connsProductPurchasePage.clickOnMobileMenuOption(webPage, mobileMenuData, softAssert);
				connsProductPurchasePage.Click_On_Element_JS(webPage, "(//div[@class='items']//li[@class='item' and contains(@data-val,'28')])[2]", softAssert);
			}
			

			connsProductPurchasePage.Add_In_Stock_Pickup_Only_Product_To_Cart(webPage, clickOnAddToCart, softAssert);

			actualPageURL = commonMethods.getPageUrl(webPage, softAssert);
			expectedPageURL = testData2[0][4];

			softAssert.assertTrue(actualPageURL.contains(expectedPageURL),
					"Verify ADD TO CART on overlay with valid input in Zip code failed : " + "Expected is: "
							+ expectedPageURL + " Actual is: " + actualPageURL);

			softAssert.assertAll();

		} catch (Throwable e) {

			mainPage.getScreenShotForFailure(webPage, "Verify_Pickup_Only_Product_Zip_Code_Functionality_Invalid_Data");
			log.error("Verify_Pickup_Only_Product_Zip_Code_Functionality_Invalid_Data failed");
			log.error(e.getMessage());
			softAssert.assertAll();
			Assert.fail(e.getLocalizedMessage());
		}

	}

	/*
	 * 
	 * TC - 006
	 */
	/*
	 * Pick up link is not displayed for all in-stock products need specific
	 * product to test this
	 */
	@Test(priority = 234, enabled = true, description = "Verify 'Pick Up IN-STORE'(product with Pick Up IN-STORE) button in cart page")
	public void Verify_Pickup_In_Store_Option_Cart_Page() {

		SoftAssert softAssert = new SoftAssert();

		try {

			String[][] clickOnAddToCart = ExcelUtil.readExcelData(DataFilePath, "ProductPurchase",
					"Add_In_Stock_Pickup_Only_Product_To_Cart");

			String[][] test = ExcelUtil.readExcelData(DataFilePath, "ProductPurchase",
					"Verify_Pickup_In_Store_Option_Cart_Page");

			log.info("Verify_Pickup_In_Store_Option_Cart_Page started");

			commonMethods.navigateToPage(webPage, testUrl, softAssert);

			if (testType.equalsIgnoreCase("Web")) {
				connsProductPurchasePage.Click_On_Element_JS(webPage, frenchDoor[1][1], softAssert);
				commonMethods.selectDropdownByValue(webPage, "(//select[@class='hasCustomSelect'])[7]", "28", softAssert);
			} else {
				connsProductPurchasePage.clickOnMobileMenuOption(webPage, mobileMenuData, softAssert);
				connsProductPurchasePage.Click_On_Element_JS(webPage, "(//div[@class='items']//li[@class='item' and contains(@data-val,'28')])[2]", softAssert);
			}
			

			connsProductPurchasePage.Add_In_Stock_Pickup_Only_Product_To_Cart(webPage, clickOnAddToCart, softAssert);

			boolean isPickupLinkDisplayed = commonMethods.verifyElementisPresent(webPage, test[0][1], softAssert);

			softAssert.assertTrue(isPickupLinkDisplayed,
					"Pick-up link is not displayed in cart page by checking pickup in product display page");

			// clicking on pick up link
			commonMethods.clickElementbyXpath(webPage, test[0][1], softAssert);

			boolean isOverLayBoxDisplayed = commonMethods.verifyElementisPresent(webPage, test[1][1], softAssert);

			softAssert.assertTrue(isOverLayBoxDisplayed,
					"Overlay box is not displayed on clicking  Pick-up link in cart page");

			softAssert.assertAll();

			log.info("Verify_Pickup_In_Store_Option_Cart_Page verification done ");

		} catch (Throwable e) {

			mainPage.getScreenShotForFailure(webPage, "Verify_Pickup_Only_Product_Zip_Code_Functionality_Invalid_Data");
			log.error("Verify_Pickup_Only_Product_Zip_Code_Functionality_Invalid_Data failed");
			log.error(e.getMessage());
			softAssert.assertAll();
			Assert.fail(e.getLocalizedMessage());
		}

	}

	@Test(priority = 235, enabled = true, description = "Verify Zip Code textbox with invalid input")
	public void Verify_Get_A_Quote_Button_Cart_Page_Valid_Zip_Code() {

		SoftAssert softAssert = new SoftAssert();

		try {

			String[][] getAQuote = ExcelUtil.readExcelData(DataFilePath, "ProductPurchase",
					"Enter_Zip_Code_Click_On_Get_Quote_Button");
			zipCodeValid = ExcelUtil.readExcelData(DataFilePath, "ProductPurchase",
					"Click_Add_To_Cart_As_Per_Avilability_Message2");
			commonMethods.navigateToPage(webPage, testUrl, softAssert);

			if (testType.equalsIgnoreCase("Web")) {
				connsProductPurchasePage.Click_On_Element_JS(webPage, frenchDoor[1][1], softAssert);
				commonMethods.selectDropdownByValue(webPage, "(//select[@class='hasCustomSelect'])[7]", "28", softAssert);
			} else {
				connsProductPurchasePage.clickOnMobileMenuOption(webPage, mobileMenuData, softAssert);
				connsProductPurchasePage.Click_On_Element_JS(webPage, "(//div[@class='items']//li[@class='item' and contains(@data-val,'28')])[2]", softAssert);
				
			}
			

			connsProductPurchasePage.Click_On_In_Stock_With_Delivery_Available(webPage, zipCodeValid, softAssert);

			// send keys to get a quote text box
			commonMethods.sendKeysbyXpath(webPage, "//*[@id='postcode']", "77702", softAssert);

			// clicking on get a quote button
			commonMethods.clickElementbyXpath(webPage, "//*[@id='shipping-zip-form']//button", softAssert);

			// verifying whther shipping option with conns shipping is displayed
			// on passing valid zip code
			boolean isShippingOptionisDisplayed = commonMethods.verifyElementisPresent(webPage,
					".//*[@id='co-shipping-method-form']//label", softAssert);

			softAssert.assertTrue(isShippingOptionisDisplayed,
					"Shipping option is not displayed on clicking get a quote button");

			/*
			 * boolean isPickupLinkDisplayed =
			 * commonMethods.verifyElementisPresent(webPage, test[0][1],
			 * softAssert); softAssert.assertTrue(isPickupLinkDisplayed,
			 * "Closest Pick-up locations window is not displayed");
			 * 
			 * // clicking on pick up link
			 * commonMethods.clickElementbyXpath(webPage, test[0][1],
			 * softAssert);
			 * 
			 * boolean isOverLayBoxDisplayed =
			 * commonMethods.verifyElementisPresent(webPage, test[1][1],
			 * softAssert);
			 * 
			 * softAssert.assertTrue(isOverLayBoxDisplayed,
			 * "Closest Pick-up locations window is not displayed");
			 * 
			 * String priceBeforeClickingUpdateTotalButton =
			 * commonMethods.getTextbyXpath(webPage, test[8][1], softAssert);
			 * 
			 * System.out.println("priceBeforeClickingUpdateTotalButton:" +
			 * priceBeforeClickingUpdateTotalButton); if (isOverLayBoxDisplayed)
			 * {
			 * 
			 * 
			 * String[] testData=getAQuote[0][3].split(",");
			 * System.out.println(getAQuote[0][3].split(","));
			 * 
			 * 
			 * connsProductPurchasePage.Enter_Zip_Code_Click_On_Get_Quote_Button
			 * (webPage, getAQuote, softAssert);
			 * 
			 * } else {
			 * 
			 * log.info(
			 * "OverLay Box is not Displayed upon clicking pick up link in cart page"
			 * ); }
			 * 
			 * boolean isShippingOptionsUpdated =
			 * commonMethods.verifyElementisPresent(webPage,
			 * "//*[@id='co-shipping-method-form']/dl", softAssert);
			 * 
			 * softAssert.assertTrue(isShippingOptionsUpdated,
			 * " shipping options are not updated on clicking get a quote button"
			 * );
			 * 
			 * commonMethods.clickElementbyXpath(webPage,
			 * "//*[@id='co-shipping-method-form']/dl", softAssert);
			 * 
			 * String priceAfterClickingUpdateTotalButton =
			 * commonMethods.getTextbyXpath(webPage, test[8][1], softAssert);
			 * 
			 * softAssert.assertNotEquals(priceBeforeClickingUpdateTotalButton,
			 * priceAfterClickingUpdateTotalButton,
			 * "Pricing is not updated on clikcing Update Total Button");
			 */
			softAssert.assertAll();

		} catch (Throwable e) {

			mainPage.getScreenShotForFailure(webPage, "Verify_Get_A_Quote_Button_Cart_Page_Valid_Zip_Code");
			log.error("Verify_Get_A_Quote_Button_Cart_Page_Valid_Zip_Code failed");
			log.error(e.getMessage());
			softAssert.assertAll();
			Assert.fail(e.getLocalizedMessage());
		}

	}

	@Test(priority = 236, enabled = true, description = "Verify Zip Code textbox with invalid input")
	public void Verify_Get_A_Quote_Button_Cart_Page_In_Valid_Zip_Code() {

		SoftAssert softAssert = new SoftAssert();

		try {

			String[][] getAQuote = ExcelUtil.readExcelData(DataFilePath, "ProductPurchase",
					"Enter_Zip_Code_Click_On_Get_Quote_Button");
			zipCodeValid = ExcelUtil.readExcelData(DataFilePath, "ProductPurchase",
					"Click_Add_To_Cart_As_Per_Avilability_Message2");
			log.info("Verify_Get_A_Quote_Button_Cart_Page_In_Valid_Zip_Code started");

			commonMethods.navigateToPage(webPage, testUrl, softAssert);

			if (testType.equalsIgnoreCase("Web")) {
				connsProductPurchasePage.Click_On_Element_JS(webPage, frenchDoor[1][1], softAssert);
				commonMethods.selectDropdownByValue(webPage, "(//select[@class='hasCustomSelect'])[7]", "28", softAssert);
			} else {
				connsProductPurchasePage.clickOnMobileMenuOption(webPage, mobileMenuData, softAssert);
				connsProductPurchasePage.Click_On_Element_JS(webPage, "(//div[@class='items']//li[@class='item' and contains(@data-val,'28')])[2]", softAssert);
				
			}
			

			connsProductPurchasePage.Click_On_In_Stock_With_Delivery_Available(webPage, zipCodeValid, softAssert);

			// send keys to get a quote text box
			commonMethods.sendKeysbyXpath(webPage, "//*[@id='postcode']", "8888", softAssert);

			// clicking on get a quote button
			commonMethods.clickElementbyXpath(webPage, "//*[@id='shipping-zip-form']//button", softAssert);

			// verifying whether shipping option with conns shipping is
			// displayed on passing valid zip code
			boolean isErrorMessageDisplayedGetaQuote = commonMethods.verifyElementisPresent(webPage,
					"//*[@id='advice-validate-zip-postcode']", softAssert);

			softAssert.assertTrue(isErrorMessageDisplayedGetaQuote,
					"Error message is not displayed on passing invalid zip code:");

			/*
			 * // clicking on pick up link
			 * commonMethods.clickElementbyXpath(webPage, test[0][1],
			 * softAssert);
			 * 
			 * boolean isOverLayBoxDisplayed =
			 * commonMethods.verifyElementisPresent(webPage, test[1][1],
			 * softAssert);
			 * 
			 * softAssert.assertTrue(isOverLayBoxDisplayed,
			 * "Closest Pick-up locations window is not displayed");
			 * 
			 * if (isOverLayBoxDisplayed) {
			 * 
			 * 
			 * String[] testData=getAQuote[0][3].split(",");
			 * System.out.println(getAQuote[0][3].split(","));
			 * 
			 * 
			 * connsProductPurchasePage.Enter_Zip_Code_Click_On_Get_Quote_Button
			 * (webPage, getAQuote, softAssert);
			 * 
			 * } else {
			 * 
			 * log.info(
			 * "OverLay Box is not Displayed upon clicking pick up link in cart page"
			 * ); }
			 * 
			 * boolean isShippingOptionsUpdated =
			 * commonMethods.verifyElementisPresent(webPage,
			 * "//*[@id='co-shipping-method-form']/dl", softAssert);
			 * 
			 * softAssert.assertTrue(isShippingOptionsUpdated,
			 * " shipping options are not updated on clicking get a quote button"
			 * );
			 */

			softAssert.assertAll();

		} catch (Throwable e) {

			mainPage.getScreenShotForFailure(webPage, "Verify_Get_A_Quote_Button_Cart_Page_In_Valid_Zip_Code");
			log.error("Verify_Get_A_Quote_Button_Cart_Page_In_Valid_Zip_Code failed");
			log.error(e.getMessage());
			softAssert.assertAll();
			Assert.fail(e.getLocalizedMessage());
		}

	}

	/* get actual value from excel */
	@Test(priority = 237, enabled = true, description = "Verify Zip Code textbox with invalid input")
	public void Verify_Ship_To_This_Address_Radio_Button_Valid_Billing_Info() {

		SoftAssert softAssert = new SoftAssert();
		String billingInfoFirstName = null;
		String shippingInfoFirstName = null;

		try {
			zipCodeValid = ExcelUtil.readExcelData(DataFilePath, "ProductPurchase",
					"Click_Add_To_Cart_As_Per_Avilability_Message2");
			// String[][] clickOnAddToCart =
			// ExcelUtil.readExcelData(DataFilePath, "ProductPurchase",
			// "Add_In_Stock_Pickup_Only_Product_To_Cart");

			log.info("Verify_Ship_To_This_Address_Radio_Button_Valid_Billing_Info started");

			commonMethods.navigateToPage(webPage, testUrl, softAssert);

			if (testType.equalsIgnoreCase("Web")) {
				connsProductPurchasePage.Click_On_Element_JS(webPage, frenchDoor[1][1], softAssert);
				commonMethods.selectDropdownByValue(webPage, "(//select[@class='hasCustomSelect'])[7]", "28", softAssert);
			} else {
				connsProductPurchasePage.clickOnMobileMenuOption(webPage, mobileMenuData, softAssert);
				connsProductPurchasePage.Click_On_Element_JS(webPage, "(//div[@class='items']//li[@class='item' and contains(@data-val,'28')])[2]", softAssert);
				
			}

			connsProductPurchasePage.Click_On_In_Stock_With_Delivery_Available(webPage, zipCodeValid, softAssert);

			connsProductPurchasePage.Proceed_To_Checkout_Button(webPage, proceedToCheckout, softAssert);

			connsProductPurchasePage.Checkout_Guest(webPage, checkoutGuest, softAssert);

			connsProductPurchasePage.Submit_Billing_Information(webPage, submitBillingInfo, softAssert);

			// clicking on billing info continue button
			commonMethods.clickElementbyXpath(webPage, submitBillingInfo[9][1], softAssert);

			Thread.sleep(3000);

			connsProductPurchasePage.Click_On_Element_JS(webPage, "//*[@id='opc-shipping']/div[1]/h2/a", softAssert);

			shippingInfoFirstName = commonMethods
					.getWebElementbyXpath(webPage, "//*[@id='shipping:firstname']", softAssert).getAttribute("value");

			// shippingInfoFirstName=submitBillingInfo[0][3];

			System.out.println("shippingfirstName:" + shippingInfoFirstName);

			billingInfoFirstName = submitBillingInfo[0][3];
			softAssert.assertEquals(shippingInfoFirstName, billingInfoFirstName,
					"Billing Info and Shipping info first names are not matching ");
			softAssert.assertAll();

		} catch (Throwable e) {

			mainPage.getScreenShotForFailure(webPage, "Verify_Get_A_Quote_Button_Cart_Page_In_Valid_Zip_Code");
			log.error("Verify_Get_A_Quote_Button_Cart_Page_In_Valid_Zip_Code failed");
			log.error(e.getMessage());
			softAssert.assertAll();
			Assert.fail(e.getLocalizedMessage());
		}

	}

	@Test(priority = 238, enabled = true, description = "Verify Zip Code textbox with invalid input")
	public void Verify_Ship_To_Different_Address_Radio_Button_Valid_Billing_Info() {

		SoftAssert softAssert = new SoftAssert();
		String expectedValue = null;

		try {

			String[][] clickOnAddToCart = ExcelUtil.readExcelData(DataFilePath, "ProductPurchase",
					"Add_In_Stock_Pickup_Only_Product_To_Cart");

			log.info("Verify_Ship_To_Different_Address_Radio_Button_Valid_Billing_Info started");

			commonMethods.navigateToPage(webPage, testUrl, softAssert);

			if (testType.equalsIgnoreCase("Web")) {
				connsProductPurchasePage.Click_On_Element_JS(webPage, frenchDoor[1][1], softAssert);
				commonMethods.selectDropdownByValue(webPage, "(//select[@class='hasCustomSelect'])[7]", "28", softAssert);
			} else {
				connsProductPurchasePage.clickOnMobileMenuOption(webPage, mobileMenuData, softAssert);
				connsProductPurchasePage.Click_On_Element_JS(webPage, "(//div[@class='items']//li[@class='item' and contains(@data-val,'28')])[2]", softAssert);
				
			}

			connsProductPurchasePage.Add_In_Stock_Pickup_Only_Product_To_Cart(webPage, clickOnAddToCart, softAssert);

			connsProductPurchasePage.Proceed_To_Checkout_Button(webPage, proceedToCheckout, softAssert);

			connsProductPurchasePage.Checkout_Guest(webPage, checkoutGuest, softAssert);

			connsProductPurchasePage.Submit_Billing_Information(webPage, submitBillingInfo, softAssert);

			WebElement shipToDifferentAddressRadioButton = commonMethods.getWebElementbyXpath(webPage,
					"//*[@id='billing:use_for_shipping_no']", softAssert);

			shipToDifferentAddressRadioButton.click();

			// clicking on billing info continue button
			commonMethods.clickElementbyXpath(webPage, submitBillingInfo[9][1], softAssert);

			Thread.sleep(3000);

			// comparing shipping info first name with billing info first name

			String shippingfirstName = commonMethods
					.getWebElementbyXpath(webPage, "//*[@id='shipping:firstname']", softAssert).getAttribute("value");

			System.out.println("shippingfirstName:" + shippingfirstName);

			expectedValue = submitBillingInfo[0][3];

			softAssert.assertNotEquals(shippingfirstName, expectedValue,
					"Shipping Info Section is expanded after selecting ship to this address radio button");

			softAssert.assertAll();

		} catch (Throwable e) {

			mainPage.getScreenShotForFailure(webPage,
					"Verify_Ship_To_Different_Address_Radio_Button_Valid_Billing_Info");
			log.error("Verify_Ship_To_Different_Address_Radio_Button_Valid_Billing_Info failed");
			log.error(e.getMessage());
			softAssert.assertAll();
			Assert.fail(e.getLocalizedMessage());
		}

	}

	@Test(priority = 239, enabled = true, description = "Verify Zip Code textbox with invalid input")
	public void Verify_Checkout_Flow_With_Zip_Code_Outside_Conns_Network() {

		SoftAssert softAssert = new SoftAssert();

		try {

			String[][] clickOnAddToCart = ExcelUtil.readExcelData(DataFilePath, "ProductPurchase",
					"Add_In_Stock_Pickup_Only_Product_To_Cart");

			log.info("Verify_Checkout_Flow_With_Zip_Code_Outside_Conns_Network started");

			commonMethods.navigateToPage(webPage, testUrl, softAssert);

			if (testType.equalsIgnoreCase("Web")) {
				connsProductPurchasePage.Click_On_Element_JS(webPage, frenchDoor[1][1], softAssert);
				commonMethods.selectDropdownByValue(webPage, "(//select[@class='hasCustomSelect'])[7]", "28", softAssert);
			} else {
				connsProductPurchasePage.clickOnMobileMenuOption(webPage, mobileMenuData, softAssert);
				connsProductPurchasePage.Click_On_Element_JS(webPage, "(//div[@class='items']//li[@class='item' and contains(@data-val,'28')])[2]", softAssert);
				
			}

			connsProductPurchasePage.Add_In_Stock_Pickup_Only_Product_To_Cart(webPage, clickOnAddToCart, softAssert);

			connsProductPurchasePage.Proceed_To_Checkout_Button(webPage, proceedToCheckout, softAssert);

			connsProductPurchasePage.Checkout_Guest(webPage, checkoutGuest, softAssert);

			connsProductPurchasePage.Submit_Billing_Information(webPage, submitBillingInfo, softAssert);

			WebElement shipToDifferentAddressRadioButton = commonMethods.getWebElementbyXpath(webPage,
					"//*[@id='billing:use_for_shipping_no']", softAssert);

			shipToDifferentAddressRadioButton.click();

			// clicking on billing info continue button
			commonMethods.clickElementbyXpath(webPage, submitBillingInfo[9][1], softAssert);

			boolean isShippingInfoFirstNameEnabled = commonMethods
					.getWebElementbyXpath(webPage, "//*[@id='shipping:firstname']", softAssert).isEnabled();

			softAssert.assertTrue(isShippingInfoFirstNameEnabled,
					"Shipping Info Section is expanded after selecting ship to this address radio button");

			softAssert.assertAll();

		} catch (Throwable e) {

			mainPage.getScreenShotForFailure(webPage,
					"Verify_Ship_To_Different_Address_Radio_Button_Functionality_Valid_Billing_Info_Inputs");
			log.error("Verify_Ship_To_Different_Address_Radio_Button_Functionality_Valid_Billing_Info_Inputs failed");
			log.error(e.getMessage());
			softAssert.assertAll();
			Assert.fail(e.getLocalizedMessage());
		}

	}

	// Not getting 2 raiod buttons (shipToDifferentAddressRadioButton) so
	// skipping this test for now.
	// @Test(priority = 240, enabled = true, description = "Verify the 'Continue
	// ' button by providing valid inputs in shipping information")
	public void Verify_Shipping_Info_Continue_Button_Valid_Info() {

		SoftAssert softAssert = new SoftAssert();

		try {

			String[][] clickOnAddToCart = ExcelUtil.readExcelData(DataFilePath, "ProductPurchase",
					"Add_In_Stock_Pickup_Only_Product_To_Cart");

			log.info("Verify_Shipping_Info_Continue_Button_Valid_Info started");

			commonMethods.navigateToPage(webPage, testUrl, softAssert);

			if (testType.equalsIgnoreCase("Web")) {
				connsProductPurchasePage.Click_On_Element_JS(webPage, frenchDoor[1][1], softAssert);
				commonMethods.selectDropdownByValue(webPage, "(//select[@class='hasCustomSelect'])[7]", "28", softAssert);
			} else {
				connsProductPurchasePage.clickOnMobileMenuOption(webPage, mobileMenuData, softAssert);
				connsProductPurchasePage.Click_On_Element_JS(webPage, "(//div[@class='items']//li[@class='item' and contains(@data-val,'28')])[2]", softAssert);
				
			}

			connsProductPurchasePage.Add_In_Stock_Pickup_Only_Product_To_Cart(webPage, clickOnAddToCart, softAssert);

			connsProductPurchasePage.Proceed_To_Checkout_Button(webPage, proceedToCheckout, softAssert);

			connsProductPurchasePage.Checkout_Guest(webPage, checkoutGuest, softAssert);

			connsProductPurchasePage.Submit_Billing_Information(webPage, submitBillingInfo, softAssert);

			WebElement shipToDifferentAddressRadioButton = commonMethods.getWebElementbyXpath(webPage,
					"//*[@id='billing:use_for_shipping_no']", softAssert);

			shipToDifferentAddressRadioButton.click();

			// clicking on billing info continue button
			commonMethods.clickElementbyXpath(webPage, submitBillingInfo[9][1], softAssert);

			connsProductPurchasePage.Submit_Shipping_Info(webPage, submitBillingInfo, softAssert);

			// clicking on shipping info continue button
			commonMethods.clickElementbyXpath(webPage, submitBillingInfo[8][1], softAssert);

			boolean isShippingMethodDisplayed = commonMethods.verifyElementisPresent(webPage,
					"//*[@id='checkout-shipping-method-load']/dl/dt", softAssert);

			softAssert.assertTrue(isShippingMethodDisplayed,
					"Shipping Info Section is expanded after selecting ship to this address radio button");

			softAssert.assertAll();

		} catch (Throwable e) {

			mainPage.getScreenShotForFailure(webPage, "Verify_Shipping_Info_Continue_Button_Valid_Info");
			log.error("Verify_Shipping_Info_Continue_Button_Valid_Info failed");
			log.error(e.getMessage());
			softAssert.assertAll();
			Assert.fail(e.getLocalizedMessage());
		}

	}

	// Not getting 2 raiod buttons (shipToDifferentAddressRadioButton) so
	// skipping this test for now.
	// @Test(priority = 241, enabled = true, description = "Verify the 'Continue
	// ' button by providing invalid inputs in shipping information")
	public void Verify_Shipping_Info_Continue_Button_InValid_Info() {

		SoftAssert softAssert = new SoftAssert();
		submitShippingInfo[5][1] = "374637";
		try {

			String[][] clickOnAddToCart = ExcelUtil.readExcelData(DataFilePath, "ProductPurchase",
					"Add_In_Stock_Pickup_Only_Product_To_Cart");

			log.info("Verify_Shipping_Info_Continue_Button_In_Valid_Info started");

			commonMethods.navigateToPage(webPage, testUrl, softAssert);

			if (testType.equalsIgnoreCase("Web")) {
				connsProductPurchasePage.Click_On_Element_JS(webPage, frenchDoor[1][1], softAssert);
				commonMethods.selectDropdownByValue(webPage, "(//select[@class='hasCustomSelect'])[7]", "28", softAssert);
			} else {
				connsProductPurchasePage.clickOnMobileMenuOption(webPage, mobileMenuData, softAssert);
				connsProductPurchasePage.Click_On_Element_JS(webPage, "(//div[@class='items']//li[@class='item' and contains(@data-val,'28')])[2]", softAssert);
				
			}

			connsProductPurchasePage.Add_In_Stock_Pickup_Only_Product_To_Cart(webPage, clickOnAddToCart, softAssert);

			connsProductPurchasePage.Proceed_To_Checkout_Button(webPage, proceedToCheckout, softAssert);

			connsProductPurchasePage.Checkout_Guest(webPage, checkoutGuest, softAssert);

			connsProductPurchasePage.Submit_Billing_Information(webPage, submitBillingInfo, softAssert);

			WebElement shipToDifferentAddressRadioButton = commonMethods.getWebElementbyXpath(webPage,
					"//*[@id='billing:use_for_shipping_no']", softAssert);

			shipToDifferentAddressRadioButton.click();

			// clicking on billing info continue button
			commonMethods.clickElementbyXpath(webPage, submitBillingInfo[9][1], softAssert);

			connsProductPurchasePage.Submit_Shipping_Info(webPage, submitShippingInfo, softAssert);

			// clicking on shipping info continue button
			commonMethods.clickElementbyXpath(webPage, submitShippingInfo[8][1], softAssert);

			/*
			 * 
			 * verification pending - verify conns shipping option in shipping
			 * method
			 */

			softAssert.assertAll();

		} catch (Throwable e) {

			mainPage.getScreenShotForFailure(webPage, "Verify_Shipping_Info_Continue_Button_InValid_Info");
			log.error("Verify_Shipping_Info_Continue_Button_InValid_Info failed");
			log.error(e.getMessage());
			softAssert.assertAll();
			Assert.fail(e.getLocalizedMessage());
		}

	}

	/* need test data to get conns shipping option in Shipping Method section */
	@Test(priority = 242, enabled = true, description = "Verify the 'continue' button by selecting shipping option as \"Conn's shipping\"")
	public void Verify_Continue_Button_functionality_Conns_Shipping() {

		SoftAssert softAssert = new SoftAssert();
		// submitShippingInfo[5][3] = "(454) 4";

		try {
			zipCodeValid = ExcelUtil.readExcelData(DataFilePath, "ProductPurchase",
					"Click_Add_To_Cart_As_Per_Avilability_Message2");

			log.info("Verify_Continue_Button_functionality_Conns_Shipping started");

			commonMethods.navigateToPage(webPage, testUrl, softAssert);

			if (testType.equalsIgnoreCase("Web")) {

				connsProductPurchasePage.Click_On_Element_JS(webPage, frenchDoor[1][1], softAssert);

			} else {
				connsProductPurchasePage.clickOnMobileMenuOption(webPage, mobileMenuData, softAssert);
			}

			commonMethods.selectDropdownByValue(webPage, "(//select[@class='hasCustomSelect'])[7]", "28", softAssert);

			connsProductPurchasePage.Click_On_In_Stock_With_Delivery_Available(webPage, zipCodeValid, softAssert);

			connsProductPurchasePage.Proceed_To_Checkout_Button(webPage, proceedToCheckout, softAssert);

			connsProductPurchasePage.Checkout_Guest(webPage, checkoutGuest, softAssert);

			connsProductPurchasePage.Submit_Billing_Information(webPage, submitBillingInfo, softAssert);

			int shipToDifferentAddressRadioButton = commonMethods
					.getWebElementsbyXpath(webPage, "//*[@id='billing:use_for_shipping_no']", softAssert).size();
			System.out.println("shipToDifferentAddressRadioButton:" + shipToDifferentAddressRadioButton);
			log.info("checking ship To Different Address radio button presence");
			if (shipToDifferentAddressRadioButton != 0) {

				log.info("ship To Different Address radio button is displayed");
				WebElement shipToDifferentAddressRadio = commonMethods.getWebElementbyXpath(webPage,
						"//*[@id='billing:use_for_shipping_no']", softAssert);

				shipToDifferentAddressRadio.click();

				// clicking on billing info continue button
				commonMethods.clickElementbyXpath(webPage, submitBillingInfo[9][1], softAssert);

				Thread.sleep(3000);
				connsProductPurchasePage.Submit_Shipping_Info(webPage, submitShippingInfo, softAssert);

				// clicking on shipping info continue button
				commonMethods.clickElementbyXpath(webPage, submitShippingInfo[8][1], softAssert);

				Thread.sleep(3000);

				String shippingOptionText = commonMethods.getTextbyXpath(webPage, "//*[@id='co-shipping-method-form']",
						softAssert);
				System.out.println("shippingOptionText:" + shippingOptionText);

				softAssert.assertTrue(shippingOptionText.contains("Conns Shipping"),
						"Conns shipping is not displayed in Shipping method section");

				commonMethods.clickElementbyXpath(webPage, "//*[@id='shipping-method-buttons-container']/button",
						softAssert);

				String checkoutCarWrapperText = commonMethods.getTextbyXpath(webPage,
						"//*[@id='checkout-cart-wrapper']/div", softAssert);

				System.out.println("checkoutCarWrapperText:" + checkoutCarWrapperText);

				softAssert.assertTrue(checkoutCarWrapperText.contains("Conns Shipping"),
						"Conns Shipping is not displayed in checkout cart wrapper section");

			} else {

				log.info("ship To Different Address radio button is not displayed in Billing information section");
			}

			softAssert.assertAll();

		} catch (Throwable e) {

			mainPage.getScreenShotForFailure(webPage, "Verify_Shipping_Info_Continue_Button_In_Valid_Info");
			log.error("Verify_Shipping_Info_Continue_Button_In_Valid_Info failed");
			log.error(e.getMessage());
			softAssert.assertAll();
			Assert.fail(e.getLocalizedMessage());
		}

	}

	/*
	 * Actual failure: actual: pickup location section is Displayed for in_stock
	 * product expected: pickup location section should not displayed for
	 * In_Stock product
	 */
	@Test(priority = 243, enabled = true, description = "Verify the 'continue' button by selecting shipping option in checkout location as Free Delivery option")
	public void Verify_Continue_Button_functionality_Free_Shipping() {

		SoftAssert softAssert = new SoftAssert();

		try {

			String[][] clickOnAddToCart = ExcelUtil.readExcelData(DataFilePath, "ProductPurchase",
					"Add_In_Stock_Pickup_Only_Product_To_Cart");

			log.info("Verify_Continue_Button_functionality_Free_Shipping started");

			commonMethods.navigateToPage(webPage, testUrl, softAssert);

			if (testType.equalsIgnoreCase("Web")) {
				connsProductPurchasePage.Click_On_Element_JS(webPage, frenchDoor[1][1], softAssert);

			} else {
				connsProductPurchasePage.clickOnMobileMenuOption(webPage, mobileMenuData, softAssert);

			}
			commonMethods.selectDropdownByValue(webPage, "(//select[@class='hasCustomSelect'])[7]", "28", softAssert);

			connsProductPurchasePage.Add_In_Stock_Pickup_Only_Product_To_Cart(webPage, clickOnAddToCart, softAssert);

			connsProductPurchasePage.Proceed_To_Checkout_Button(webPage, proceedToCheckout, softAssert);

			connsProductPurchasePage.Checkout_Guest(webPage, checkoutGuest, softAssert);

			connsProductPurchasePage.Submit_Billing_Information(webPage, submitBillingInfo, softAssert);

			Thread.sleep(2000);

			int shipToDifferentAddressRadioButton = commonMethods
					.getWebElementsbyXpath(webPage, "//*[@id='billing:use_for_shipping_no']", softAssert).size();
			System.out.println("shipToDifferentAddressRadioButton:" + shipToDifferentAddressRadioButton);
			log.info("checking ship To Different Address radio button presence");
			if (shipToDifferentAddressRadioButton != 0) {

				log.info("ship To Different Address radio button is displayed");
				WebElement shipToDifferentAddressRadio = commonMethods.getWebElementbyXpath(webPage,
						"//*[@id='billing:use_for_shipping_no']", softAssert);

				shipToDifferentAddressRadio.click();

				// clicking on billing info continue button
				commonMethods.clickElementbyXpath(webPage, submitBillingInfo[9][1], softAssert);

				Thread.sleep(3000);

				connsProductPurchasePage.Submit_Shipping_Info(webPage, submitShippingInfo, softAssert);

				// clicking on shipping info continue button
				commonMethods.clickElementbyXpath(webPage, submitShippingInfo[8][1], softAssert);

				Thread.sleep(3000);

				String shippingOptionText = commonMethods.getTextbyXpath(webPage, "//*[@id='co-shipping-method-form']",
						softAssert);
				System.out.println("shippingOptionText:" + shippingOptionText);

				softAssert.assertTrue(shippingOptionText.contains("Free Delivery"),
						"Free Delivery shipping is not displayed in Shipping method section");

				commonMethods.clickElementbyXpath(webPage, "//*[@id='shipping-method-buttons-container']/button",
						softAssert);

				String checkoutCarWrapperText = commonMethods.getTextbyXpath(webPage,
						"//*[@id='checkout-cart-wrapper']/div", softAssert);

				System.out.println("checkoutCarWrapperText:" + checkoutCarWrapperText);

				softAssert.assertTrue(checkoutCarWrapperText.contains("Free Delivery"),
						"Free Delivery is not displayed in checkout cart wrapper section");

			} else {

				log.info("ship To Different Address radio button is not displayed in Billing information section");
			}

			softAssert.assertAll();
		} catch (Throwable e) {

			mainPage.getScreenShotForFailure(webPage, "Verify_Continue_Button_functionality_Free_Shipping");
			log.error("Verify_Continue_Button_functionality_Free_Shipping failed");
			log.error(e.getMessage());
			softAssert.assertAll();
			Assert.fail(e.getLocalizedMessage());
		}

	}

	@Test(priority = 244, enabled = true, description = "Verify the field validation with -ve inputs for Billing Information form in checkout page")
	public void Verify_BillingInfo_Field_Validation_Negative_Inputs() {
		SoftAssert softAssert = new SoftAssert();
		try {

			String[][] inStockAvialableProduct = ExcelUtil.readExcelData(DataFilePath, "ProductPurchase",
					"Click_Add_To_Cart_As_Per_Avilability_Message2");
			log.info("testing flow Verify_BillingInfo_Field_Validation_Negative_Inputs started");

			commonMethods.navigateToPage(webPage, testUrl, softAssert);

			if (testType.equalsIgnoreCase("Web")) {

				connsProductPurchasePage.Click_On_Element_JS(webPage, frenchDoor[1][1], softAssert);

			} else {

				connsProductPurchasePage.clickOnMobileMenuOption(webPage, mobileMenuData, softAssert);
			}

			commonMethods.selectDropdownByValue(webPage, "(//select[@class='hasCustomSelect'])[7]", "28", softAssert);

			// adding pickup only product to cart
			connsProductPurchasePage.Click_On_In_Stock_With_Delivery_Available(webPage, inStockAvialableProduct,
					softAssert);

			connsProductPurchasePage.Proceed_To_Checkout_Button(webPage, proceedToCheckout, softAssert);

			connsProductPurchasePage.Checkout_Guest(webPage, checkoutGuest, softAssert);

			WebElement emailField = commonMethods.getWebElementbyXpath(webPage, submitBillingInfo[2][1], softAssert);
			commonMethods.sendKeysbyXpath(webPage, submitBillingInfo[2][1], "dhfhsdbfj", softAssert);
			emailField.sendKeys(Keys.TAB);
			boolean isEmailErrorDisplayed = commonMethods.verifyElementisPresent(webPage, billingFormValidation[0][1],
					softAssert);
			System.out.println("isEmailErrorDisplayed:" + isEmailErrorDisplayed);
			softAssert.assertTrue(isEmailErrorDisplayed,
					"email address error message is not displayed for invalid email address");

			WebElement zipcodeField = commonMethods.getWebElementbyXpath(webPage, submitBillingInfo[5][1], softAssert);
			commonMethods.sendKeysbyXpath(webPage, submitBillingInfo[5][1], "88898878", softAssert);
			zipcodeField.sendKeys(Keys.TAB);
			boolean isZipcodeErrorDisplayed = commonMethods.verifyElementisPresent(webPage, billingFormValidation[1][1],
					softAssert);
			System.out.println("isZipcodeErrorDisplayed:" + isZipcodeErrorDisplayed);
			softAssert.assertTrue(isZipcodeErrorDisplayed,
					"Zip code  error message is not displayed for invalid zip code");

		} catch (Exception e) {
			mainPage.getScreenShotForFailure(webPage, "Verify_BillingInfo_Field_Validation_Negative_Inputs");
			SoftAssertor.addVerificationFailure(e.getMessage());
			log.error("Verify_BillingInfo_Field_Validation_Negative_Inputs failed");
			log.error(e.getMessage());
			softAssert.assertAll();
			Assert.fail(e.getLocalizedMessage());

		}

	}

	@Test(priority = 245, enabled = true, description = "Verify the field validation with -ve inputs for Billing Information form in checkout page")
	public void Verify_BillingInfo_Field_Validation_Positive_Inputs() {
		SoftAssert softAssert = new SoftAssert();
		try {

			String[][] inStockAvialableProduct = ExcelUtil.readExcelData(DataFilePath, "ProductPurchase",
					"Click_Add_To_Cart_As_Per_Avilability_Message2");
			log.info("testing flow Verify_BillingInfo_Field_Validation_Positive_Inputs started");

			commonMethods.navigateToPage(webPage, testUrl, softAssert);

			if (testType.equalsIgnoreCase("Web")) {

				connsProductPurchasePage.Click_On_Element_JS(webPage, frenchDoor[1][1], softAssert);

			} else {

				connsProductPurchasePage.clickOnMobileMenuOption(webPage, mobileMenuData, softAssert);
			}

			commonMethods.selectDropdownByValue(webPage, "(//select[@class='hasCustomSelect'])[7]", "28", softAssert);

			// adding pickup only product to cart
			connsProductPurchasePage.Click_On_In_Stock_With_Delivery_Available(webPage, inStockAvialableProduct,
					softAssert);

			connsProductPurchasePage.Proceed_To_Checkout_Button(webPage, proceedToCheckout, softAssert);

			connsProductPurchasePage.Checkout_Guest(webPage, checkoutGuest, softAssert);

			WebElement emailField = commonMethods.getWebElementbyXpath(webPage, submitBillingInfo[2][1], softAssert);
			commonMethods.sendKeysbyXpath(webPage, submitBillingInfo[2][1], "lee@gmail.com", softAssert);
			emailField.sendKeys(Keys.TAB);
			boolean isEmailErrorDisplayed = commonMethods.verifyElementisPresent(webPage, billingFormValidation[0][1],
					softAssert);
			System.out.println("isEmailErrorDisplayed:" + isEmailErrorDisplayed);
			softAssert.assertFalse(isEmailErrorDisplayed,
					"email address error message is displayed for valid email address");

			WebElement zipcodeField = commonMethods.getWebElementbyXpath(webPage, submitBillingInfo[5][1], softAssert);
			commonMethods.sendKeysbyXpath(webPage, submitBillingInfo[5][1], "99999", softAssert);
			zipcodeField.sendKeys(Keys.TAB);
			boolean isZipcodeErrorDisplayed = commonMethods.verifyElementisPresent(webPage, billingFormValidation[1][1],
					softAssert);
			System.out.println("isZipcodeErrorDisplayed:" + isZipcodeErrorDisplayed);
			softAssert.assertFalse(isZipcodeErrorDisplayed, "Zip code  error message is displayed for valid zip code");

		} catch (Exception e) {
			mainPage.getScreenShotForFailure(webPage, "Verify_BillingInfo_Field_Validation_Positive_Inputs");
			SoftAssertor.addVerificationFailure(e.getMessage());
			log.error("Verify_BillingInfo_Field_Validation_Positive_Inputs failed");
			log.error(e.getMessage());
			softAssert.assertAll();
			Assert.fail(e.getLocalizedMessage());

		}

	}

	@Test(priority = 246, enabled = true, description = "Verify the field validation with +ve inputs for Shipping Information form in checkout page")
	public void Verify_Shippinginfo_Field_Validation_Positive_Inputs() {
		SoftAssert softAssert = new SoftAssert();
		try {

			String[][] inStockAvialableProduct = ExcelUtil.readExcelData(DataFilePath, "ProductPurchase",
					"Click_Add_To_Cart_As_Per_Avilability_Message2");
			log.info("testing flow Verify_Shippinginfo_Field_Validation_Positive_Inputs started");

			commonMethods.navigateToPage(webPage, testUrl, softAssert);

			if (testType.equalsIgnoreCase("Web")) {

				connsProductPurchasePage.Click_On_Element_JS(webPage, frenchDoor[1][1], softAssert);

			} else {

				connsProductPurchasePage.clickOnMobileMenuOption(webPage, mobileMenuData, softAssert);
			}

			commonMethods.selectDropdownByValue(webPage, "(//select[@class='hasCustomSelect'])[7]", "28", softAssert);

			// adding pickup only product to cart
			connsProductPurchasePage.Click_On_In_Stock_With_Delivery_Available(webPage, inStockAvialableProduct,
					softAssert);

			connsProductPurchasePage.Proceed_To_Checkout_Button(webPage, proceedToCheckout, softAssert);

			connsProductPurchasePage.Checkout_Guest(webPage, checkoutGuest, softAssert);

			connsProductPurchasePage.Submit_Billing_Information(webPage, submitBillingInfo, softAssert);

			commonMethods.clickElementbyXpath(webPage, "//*[@id='billing:use_for_shipping_no']", softAssert);

			commonMethods.clickElementbyXpath(webPage, submitBillingInfo[9][1], softAssert);

		} catch (Exception e) {
			mainPage.getScreenShotForFailure(webPage, "Verify_Shippinginfo_Field_Validation_Positive_Inputs");
			SoftAssertor.addVerificationFailure(e.getMessage());
			log.error("Verify_Shippinginfo_Field_Validation_Positive_Inputs failed");
			log.error(e.getMessage());
			softAssert.assertAll();
			Assert.fail(e.getLocalizedMessage());

		}

	}

	@Test(priority = 247, enabled = true, description = "Verify the field validation with -ve inputs for shipping Information form in checkout page")
	public void Verify_ShippingInfo_Field_Validation_Negative_Inputs() {
		SoftAssert softAssert = new SoftAssert();
		try {

			String[][] inStockAvialableProduct = ExcelUtil.readExcelData(DataFilePath, "ProductPurchase",
					"Click_Add_To_Cart_As_Per_Avilability_Message2");
			log.info("testing flow Verify_Shippinginfo_Field_Validation_Positive_Inputs started");

			commonMethods.navigateToPage(webPage, testUrl, softAssert);

			if (testType.equalsIgnoreCase("Web")) {

				connsProductPurchasePage.Click_On_Element_JS(webPage, frenchDoor[1][1], softAssert);

			} else {

				connsProductPurchasePage.clickOnMobileMenuOption(webPage, mobileMenuData, softAssert);
			}

			commonMethods.selectDropdownByValue(webPage, "(//select[@class='hasCustomSelect'])[7]", "28", softAssert);

			// adding pickup only product to cart
			connsProductPurchasePage.Click_On_In_Stock_With_Delivery_Available(webPage, inStockAvialableProduct,
					softAssert);

			connsProductPurchasePage.Proceed_To_Checkout_Button(webPage, proceedToCheckout, softAssert);
			Thread.sleep(3000);
			connsProductPurchasePage.Checkout_Guest(webPage, checkoutGuest, softAssert);

			connsProductPurchasePage.Submit_Billing_Information(webPage, submitBillingInfo, softAssert);

			commonMethods.clickElementbyXpath(webPage, "//*[@id='billing:use_for_shipping_no']", softAssert);

			commonMethods.clickElementbyXpath(webPage, submitBillingInfo[9][1], softAssert);

		} catch (Exception e) {
			mainPage.getScreenShotForFailure(webPage, "Verify_ShippingInfo_Field_Validation_Negative_Inputs");
			SoftAssertor.addVerificationFailure(e.getMessage());
			log.error("Verify_ShippingInfo_Field_Validation_Negative_Inputs failed");
			log.error(e.getMessage());
			softAssert.assertAll();
			Assert.fail(e.getLocalizedMessage());

		}

	}

	@Test(priority = 247, enabled = true, description = "Verify all product related details in cart page with list page")
	public void Verify_Cart_Page_Product_Details() {
		SoftAssert softAssert = new SoftAssert();
		String productDetails = null;
		String productLinkTextCartPage = null;
		String productPriceCartPage = null;
		try {

			String[][] inStockAvialableProduct = ExcelUtil.readExcelData(DataFilePath, "ProductPurchase",
					"Click_Add_To_Cart_As_Per_Avilability_Message2");
			log.info("testing flow Verify_Cart_Page_Product_Details started");

			commonMethods.navigateToPage(webPage, testUrl, softAssert);

			if (testType.equalsIgnoreCase("Web")) {

				connsProductPurchasePage.Click_On_Element_JS(webPage, frenchDoor[1][1], softAssert);
				commonMethods.selectDropdownByValue(webPage, "(//select[@class='hasCustomSelect'])[7]", "28", softAssert);
			} else {

				connsProductPurchasePage.clickOnMobileMenuOption(webPage, mobileMenuData, softAssert);
			}

			

			// adding pickup only product to cart
			productDetails = connsProductPurchasePage.Click_On_In_Stock_With_Delivery_Available(webPage,
					inStockAvialableProduct, softAssert);
			System.out.println("productDetails:" + productDetails);

			productPriceCartPage = commonMethods.getTextbyXpath(webPage, ItemLink[10][1], softAssert);
			System.out.println("productPriceCartPage:" + productPriceCartPage);

			productLinkTextCartPage = commonMethods.getTextbyXpath(webPage, ItemLink[6][1], softAssert);

			System.out.println("productLinkTextCartPage:" + productLinkTextCartPage);

			softAssert.assertTrue(productDetails.contains(productPriceCartPage),
					"Cart price product is not matching with list price:" + "excpected is:" + productPriceCartPage);

			softAssert.assertTrue(productDetails.contains(productLinkTextCartPage),
					"Product list page name is not matching with cart page product name");

			softAssert.assertAll();

		} catch (Exception e) {
			mainPage.getScreenShotForFailure(webPage, "Verify_ShippingInfo_Field_Validation_Negative_Inputs");
			SoftAssertor.addVerificationFailure(e.getMessage());
			log.error("Verify_ShippingInfo_Field_Validation_Negative_Inputs failed");
			log.error(e.getMessage());
			softAssert.assertAll();
			Assert.fail(e.getLocalizedMessage());

		}

	}
	
	
	@Test(priority = 247, enabled = true, description = "Verify product name & price in different stages while performing checkout flow")
	public void Verify_Pickup_Checkout_Flow_Cash_On_Delivery() {
		SoftAssert softAssert = new SoftAssert();
		String productDetails = null;
		String productPriceCartPage = null;
		String expectedSuccessMessage=checkoutFlowCommonLocators[33][4];
		try {
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
