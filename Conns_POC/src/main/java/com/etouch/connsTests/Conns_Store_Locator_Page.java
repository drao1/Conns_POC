package com.etouch.connsTests;

import java.awt.AWTException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.apache.commons.logging.Log;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.etouch.common.BaseTest;
import com.etouch.common.CommonMethods;
import com.etouch.common.TafExecutor;
import com.etouch.connsPages.ConnsMainPage;
import com.etouch.connsPages.ConnsStoreLocatorPage;
import com.etouch.taf.core.TestBed;
import com.etouch.taf.core.TestBedManager;
import com.etouch.taf.core.config.TestBedManagerConfiguration;
import com.etouch.taf.core.datamanager.excel.annotations.IExcelDataFiles;
import com.etouch.taf.core.exception.PageException;
import com.etouch.taf.util.CommonUtil;
import com.etouch.taf.util.ExcelUtil;
import com.etouch.taf.util.LogUtil;
import com.etouch.taf.util.SoftAssertor;
import com.etouch.taf.webui.selenium.MobileView;
import com.etouch.taf.webui.selenium.WebPage;

//import mx4j.log.Logger;

//@Test(groups = "HomePage")
@IExcelDataFiles(excelDataFiles = { "CreditAppData=testData" })
public class Conns_Store_Locator_Page extends BaseTest {
	static String platform;
	static Log log = LogUtil.getLog(Conns_Store_Locator_Page.class);
	static String AbsolutePath = TafExecutor.class.getProtectionDomain().getCodeSource().getLocation().getPath();
	static String videoLocation = AbsolutePath.substring(0, AbsolutePath.indexOf("/target/classes/")).substring(1)
			.concat("/src/test/resources/testdata/videos");
	private String url = null;
	private WebPage webPage;
	private ConnsMainPage mainPage;
	String testBedName;
	TestBed testBed;
	MobileView mobileView;
	Path path;
	String DataFilePath;
	String testType;
	static protected String browserName;
	String testEnv; 
	static CommonMethods commonMethods;
	ConnsStoreLocatorPage connsStoreLocatorPage;
	String storeLocatorURL="";
	String[][] commonData;

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
				testEnv = System.getenv().get("Environment");
				System.out.println("testEnv is : " + testEnv);
				path = Paths.get(TestBedManager.INSTANCE.getProfile().getXlsDataConfig().get("testData"));
				DataFilePath = path.toAbsolutePath().toString().replace("Env", testEnv);
				System.out.println("DataFilePath After is : " + DataFilePath);
				commonData = ExcelUtil.readExcelData(DataFilePath, "StoreLocator", "storeLocatorCommonElements");
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
		// SpecializedScreenRecorder.stopVideoRecording();
		webPage.getDriver().quit();
	}


	@Test(priority = 101, enabled = true, description = "Verify Store Locator Page title")
	public void Verify_StoreLocator_PageTitle() {
		SoftAssert softAssert = new SoftAssert();
		try{
			String[][] test = ExcelUtil.readExcelData(DataFilePath, "StoreLocator", "verifyPageTitle");
			String ExpectedTitle = test[0][1];
			commonMethods.navigateToPage(webPage,storeLocatorURL, softAssert);
			connsStoreLocatorPage.closeLocationPopup(webPage,softAssert);
			CommonMethods.waitForWebElement(By.xpath(commonData[1][1]), webPage); //Added By Rajesh
			softAssert.assertEquals(ExpectedTitle, webPage.getPageTitle(),"Page Title verification failed. Expected title - " + ExpectedTitle + " Actual title - "+ webPage.getPageTitle());
			softAssert.assertAll();
		}catch(Throwable e){
			mainPage.getScreenShotForFailure(webPage, "Verify_StoreLocator_PageTitle");
			softAssert.assertAll();
			Assert.fail(e.getLocalizedMessage());
		}

	}

	@Test(priority = 102, enabled = true, description = "Verify Find Your Conn's HomePlus component")
	public void Verify_HomePlus_Component() {
		SoftAssert softAssert = new SoftAssert();
		try{
			String[][] test = ExcelUtil.readExcelData(DataFilePath, "StoreLocator", "verifyHomePlusComponent");
			commonMethods.navigateToPage(webPage, storeLocatorURL, softAssert);
			connsStoreLocatorPage.closeLocationPopup(webPage,softAssert);
			CommonMethods.waitForWebElement(By.xpath(commonData[1][1]), webPage);
			softAssert.assertTrue(commonMethods.verifyElementisPresent(webPage, test[0][0], softAssert),"Element not present using locator - " + test[0][0]);
			softAssert.assertTrue(commonMethods.verifyElementisPresent(webPage, test[1][0], softAssert),
					"Element not present using locator - " + test[1][0]);
			softAssert.assertTrue(commonMethods.verifyElementisPresent(webPage, test[2][0], softAssert),
					"Element not present using locator - " + test[2][0]);
			String homeplusText1 = commonMethods.getTextbyXpath(webPage, test[0][1], softAssert);
			softAssert.assertEquals(homeplusText1, test[0][2],
					"Text verification failed. Expected text : " + test[0][2] + " Actual text : " + homeplusText1);
			String homeplusText2 = commonMethods.getTextbyXpath(webPage, test[1][1], softAssert);
			softAssert.assertEquals(homeplusText2, test[1][2],
					"Text verification failed. Expected text : " + test[1][2] + " Actual text : " + homeplusText2);
			softAssert.assertAll();
		}catch(Throwable e){
			mainPage.getScreenShotForFailure(webPage, "Verify_HomePlus_Component");
			softAssert.assertAll();
			Assert.fail(e.getLocalizedMessage());
		}
	}

	@Test(priority = 103, enabled = true, description = "Verify Store locator Region links")
	public void Verify_ChoseYourRegion_Links() throws PageException, InterruptedException {
		SoftAssert softAssert = new SoftAssert();
		try{
			String[][] regionLinksData = ExcelUtil.readExcelData(DataFilePath, "StoreLocator",
					"verifyChoseYourRegionLinks");
			for (int i = 0; i < regionLinksData.length; i++) {
				commonMethods.navigateToPage(webPage, storeLocatorURL, softAssert);
				connsStoreLocatorPage.closeLocationPopup(webPage, softAssert);
				if (testType.equalsIgnoreCase("Web")) {
					connsStoreLocatorPage.closeLocationPopup(webPage, softAssert);
					String beforeLinkHover = commonMethods.getCssvaluebyXpath(webPage, regionLinksData[i][1], "color", softAssert);
					log.info("Region "+regionLinksData[i][0]+" before Hover : "+beforeLinkHover);
					commonMethods.hoverOnelementbyXpath(webPage, regionLinksData[i][1], softAssert);
					String afterLinkHover = commonMethods.getCssvaluebyXpath(webPage, regionLinksData[i][1], "color", softAssert);
					log.info("Region "+regionLinksData[i][0]+" after Hover : "+afterLinkHover);
					softAssert.assertNotEquals(afterLinkHover, beforeLinkHover,
							"CSS value verification failed for link " + regionLinksData[i][0] + ". Value before hover : "
									+ beforeLinkHover + " , Value after hover : " + afterLinkHover);
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

	@Test(priority = 104, enabled = true, description = "Verify Texas sub links")
	public void Verify_Texas_SubLinks() throws PageException, InterruptedException {
		SoftAssert softAssert = new SoftAssert();
		try{
			String[][] TexasSubLinksData = ExcelUtil.readExcelData(DataFilePath, "StoreLocator", "verifyTexasSubLinks");
			for (int i = 0; i < TexasSubLinksData.length; i++) {
				commonMethods.navigateToPage(webPage, storeLocatorURL, softAssert);
				connsStoreLocatorPage.closeLocationPopup(webPage, softAssert);
				commonMethods.clickElementbyXpath(webPage, TexasSubLinksData[i][1], softAssert);
				CommonMethods.waitForWebElement(By.xpath(commonData[3][1]), webPage);
				if (testType.equalsIgnoreCase("Web")) {
					String beforeLinkHover = commonMethods.getCssvaluebyXpath(webPage, TexasSubLinksData[i][2], "color", softAssert);
					commonMethods.hoverOnelementbyXpath(webPage, TexasSubLinksData[i][2], softAssert);
					String afterLinkHover = commonMethods.getCssvaluebyXpath(webPage, TexasSubLinksData[i][2], "color", softAssert);
					if(!TexasSubLinksData[i][0].equalsIgnoreCase("Laredo")){
						softAssert.assertNotEquals(afterLinkHover, beforeLinkHover,"CSS value verification failed for link " + TexasSubLinksData[i][0] + ". Value before hover : "+ beforeLinkHover + " , Value after hover : " + afterLinkHover);
					}
				}
				String textOnLink = commonMethods.getTextbyXpath(webPage, TexasSubLinksData[i][2], softAssert);
				softAssert.assertEquals(textOnLink, TexasSubLinksData[i][0],"Link text verification failed. Expected text : " + TexasSubLinksData[i][0] + " Actual text : "+ textOnLink);
				String actualUrl = commonMethods.clickAndGetPageURL(webPage, TexasSubLinksData[i][2],TexasSubLinksData[i][0], softAssert,commonData[2][1]);
				softAssert.assertEquals(actualUrl, TexasSubLinksData[i][3],"URL verification failed for link : '" + TexasSubLinksData[i][0] + "'. Expected URL : "+ TexasSubLinksData[i][3] + " Actual URL : " + actualUrl);
			}
			softAssert.assertAll();
		}catch(Throwable e){
			mainPage.getScreenShotForFailure(webPage, "Verify_Texas_SubLinks");
			softAssert.assertAll();
			Assert.fail(e.getLocalizedMessage());
		}
	}

	@Test(priority = 105, enabled = true, description = "Verify order of Store locator links")
	public void Verify_Order_of_Links() throws PageException, InterruptedException {
		SoftAssert softAssert = new SoftAssert();
		try{
		String[][] choseYourLinkData = ExcelUtil.readExcelData(DataFilePath, "StoreLocator", "verifyOrderofLinks");
		commonMethods.navigateToPage(webPage, storeLocatorURL, softAssert);
		connsStoreLocatorPage.closeLocationPopup(webPage,softAssert);
		CommonMethods.waitForWebElement(By.xpath(commonData[1][1]), webPage);
		for (int i = 0; i < choseYourLinkData.length; i++) {
			String textOnLink = commonMethods.getTextbyXpath(webPage, choseYourLinkData[i][1], softAssert);
			softAssert.assertEquals(textOnLink, choseYourLinkData[i][0],"Alphabetical order verification failed for links. Expected text : " + choseYourLinkData[i][0]+ " Actual text : " + textOnLink);
		}
		softAssert.assertAll();
		}catch(Throwable e){
			mainPage.getScreenShotForFailure(webPage, "Verify_Order_of_Links");
			softAssert.assertAll();
			Assert.fail(e.getLocalizedMessage());
		}
	}

	@Test(priority = 106, enabled = true, description = "Verify tool tip text on region links map")
	public void Verify_RegionMap_ToolTip() throws PageException, InterruptedException {
		SoftAssert softAssert = new SoftAssert();
		try{
			String[][] verifyRegionMapData = ExcelUtil.readExcelData(DataFilePath, "StoreLocator","verifyRegionMapToolTip");
			if (testType.equalsIgnoreCase("Web")) {
				commonMethods.navigateToPage(webPage, storeLocatorURL, softAssert);
				connsStoreLocatorPage.closeLocationPopup(webPage,softAssert);
				CommonMethods.waitForWebElement(By.xpath(commonData[1][1]), webPage);
				for (int i = 0; i < verifyRegionMapData.length; i++) {
					String attributeValue = commonMethods.getAttributebyXpath(webPage, verifyRegionMapData[i][1], "title", softAssert);
					softAssert.assertEquals(attributeValue, verifyRegionMapData[i][0],
							"Tooltip verification failed for link. Expected Text : " + verifyRegionMapData[i][0]
									+ " Actual text : " + attributeValue);
				}
				softAssert.assertAll();
			}
		}catch(Throwable e){
			mainPage.getScreenShotForFailure(webPage, "Verify_RegionMap_ToolTip");
			softAssert.assertAll();
			Assert.fail(e.getLocalizedMessage());
		}
	}

	@Test(priority = 107, enabled = true, description = "Verify region description for all region pages")
	public void Verify_AllRegion_PageDescription() throws PageException, InterruptedException {
		SoftAssert softAssert = new SoftAssert();
		try{
		String[][] allRegionDescriptiondata = ExcelUtil.readExcelData(DataFilePath, "StoreLocator","verifyAllRegionPageDescription");
		for (int i = 0; i < allRegionDescriptiondata.length; i++) {
			commonMethods.navigateToPage(webPage, storeLocatorURL, softAssert);
			connsStoreLocatorPage.closeLocationPopup(webPage, softAssert);
			CommonMethods.waitForWebElement(By.xpath(commonData[1][1]), webPage);
			commonMethods.clickWithChildElementbyXpath(webPage, allRegionDescriptiondata[i][1], allRegionDescriptiondata[i][2],allRegionDescriptiondata[i][0], softAssert);
			CommonMethods.waitForWebElement(By.xpath(commonData[1][1]), webPage);
			String storeDescriptionText = commonMethods.getTextbyXpath(webPage, allRegionDescriptiondata[i][3], softAssert);
			softAssert.assertTrue(storeDescriptionText.contains(allRegionDescriptiondata[i][4]),
					"Store locator description text verification failed. Expected text : "
							+ allRegionDescriptiondata[i][4] + " Actual text : " + storeDescriptionText);
			String breadCrumbsActualText = commonMethods.getTextbyXpath(webPage, allRegionDescriptiondata[i][7], softAssert);
			softAssert.assertTrue(breadCrumbsActualText.contains(allRegionDescriptiondata[i][8]),
					"Bread Crumbs verification failed. Expected text : "
							+ allRegionDescriptiondata[i][8] + " Actual text : " + breadCrumbsActualText);
			String yesmoneyLinkUrl = commonMethods.clickAndGetPageURL(webPage, allRegionDescriptiondata[i][5],allRegionDescriptiondata[i][0], softAssert,commonData[4][1]);
			softAssert.assertEquals(yesmoneyLinkUrl, allRegionDescriptiondata[i][6],
					"URL verification failed for link : '" + allRegionDescriptiondata[i][0] + "'. Expected URL : "
							+ allRegionDescriptiondata[i][6] + " Actual URL : " + yesmoneyLinkUrl);
		}
		softAssert.assertAll();
		}catch(Throwable e){
			mainPage.getScreenShotForFailure(webPage, "Verify_AllRegion_PageDescription");
			softAssert.assertAll();
			Assert.fail(e.getLocalizedMessage());
		}
	}

	@Test(enabled = true, priority = 108, description = "Verify content for all region pages")
	public void Verify_All_Regions_Page_Content() throws PageException, InterruptedException {
		SoftAssert softAssert = new SoftAssert();
		try{
			String[][] regionPageTextdata = ExcelUtil.readExcelData(DataFilePath, "StoreLocator","verifyAllRegionsPageTemplate");
			String key = "";
			for (int i = 0; i < regionPageTextdata.length; i++) {
				commonMethods.navigateToPage(webPage, regionPageTextdata[i][1], softAssert);
				connsStoreLocatorPage.closeLocationPopup(webPage,softAssert);
				key = regionPageTextdata[i][0];
				String[][] keyData = ExcelUtil.readExcelData(DataFilePath, "StoreLocator", key + "Region");
				for (int j = 0; j < keyData.length; j++) {
					String subkey = keyData[j][0];
					CommonMethods.waitForWebElement(By.xpath(commonData[2][1]), webPage);
					String pageContentText = commonMethods.getTextbyXpath(webPage, subkey, softAssert);
					if(testType.equalsIgnoreCase("Mobile")){
						pageContentText=pageContentText.replace("Mon-Fri", "Store Hours\nMon-Fri");
					}
					softAssert.assertTrue(pageContentText.contains(keyData[j][1]),"Text verification failed for region "+regionPageTextdata[i][1]+". Expected Text : " + keyData[j][1] + " Actual Text : " + pageContentText);
					String actualUrlGoogleMap = commonMethods.clickAndGetPageURL(webPage, keyData[j][2], "Get Directions Region - "+regionPageTextdata[i][0],softAssert);
					if(actualUrlGoogleMap.contains("/dir//")){
						actualUrlGoogleMap=actualUrlGoogleMap.replace("/dir//", "?daddr=");
					}
					if(actualUrlGoogleMap.contains("daddr=Conn's+HomePlus,+")){
						actualUrlGoogleMap=actualUrlGoogleMap.replace("daddr=Conn's+HomePlus,+", "?daddr=");
					}
					softAssert.assertTrue(actualUrlGoogleMap.startsWith(keyData[j][3]),"Url verification failed. Expected URL : "+keyData[j][3]+". Actual URL : "+actualUrlGoogleMap);
				}
			}
			softAssert.assertAll();
		}catch(Throwable e){
			mainPage.getScreenShotForFailure(webPage, "Verify_All_Regions_Page_Content");
			softAssert.assertAll();
			Assert.fail(e.getLocalizedMessage());
		}
	}

	@Test(enabled = true, priority = 109, description = "Verify alert box for empty Find Store field")
	public void Verify_FindStore_AlertBox() throws PageException, InterruptedException {
		SoftAssert softAssert = new SoftAssert();
		try{
		String[][] verifyFindStoreAlertBoxData = ExcelUtil.readExcelData(DataFilePath, "StoreLocator",
				"verifyFindStoreAlertBox");
		commonMethods.navigateToPage(webPage, storeLocatorURL, softAssert);
		connsStoreLocatorPage.closeLocationPopup(webPage,softAssert);
		CommonMethods.waitForWebElement(By.xpath(commonData[1][1]), webPage);
		commonMethods.clearTextBox(webPage, verifyFindStoreAlertBoxData[0][0], softAssert);
		commonMethods.clickElementbyXpath(webPage, verifyFindStoreAlertBoxData[1][0], softAssert);
		Alert alert = webPage.getDriver().switchTo().alert();
		String alertActualText = alert.getText();
		alert.accept();
		softAssert.assertEquals(alertActualText, verifyFindStoreAlertBoxData[0][1],
				"Expected Text : " + verifyFindStoreAlertBoxData[1][1] + " Actual Text : " + alertActualText);

		softAssert.assertAll();
		}catch(Throwable e){
			mainPage.getScreenShotForFailure(webPage, "Verify_FindStore_AlertBox");
			softAssert.assertAll();
			Assert.fail(e.getLocalizedMessage());
		}
	}

	@Test(enabled = true, priority = 110, description = "Verify Find Store field for Invalid input")
	public void Verify_FindStore_for_InvalidData() throws PageException, InterruptedException {
		SoftAssert softAssert = new SoftAssert();
		try{
			String[][] verifyFindStoreInvalidData = ExcelUtil.readExcelData(DataFilePath, "StoreLocator","verifyFindStoreInvalidData");
			commonMethods.navigateToPage(webPage, storeLocatorURL, softAssert);
			connsStoreLocatorPage.closeLocationPopup(webPage, softAssert);
			CommonMethods.waitForWebElement(By.xpath(commonData[1][1]), webPage);
			commonMethods.clickElementbyXpath(webPage, verifyFindStoreInvalidData[0][0], softAssert);
			connsStoreLocatorPage.closeLocationPopup(webPage, softAssert);
			commonMethods.clearTextBox(webPage, verifyFindStoreInvalidData[1][0], softAssert);
			commonMethods.sendKeysbyXpath(webPage, verifyFindStoreInvalidData[1][0], verifyFindStoreInvalidData[0][1], softAssert);
			commonMethods.clickElementbyXpath(webPage, verifyFindStoreInvalidData[2][0], softAssert);
			String errorMsgActualText = commonMethods.getTextbyXpath(webPage, verifyFindStoreInvalidData[3][0], softAssert);
			String errorMessageActualColor = commonMethods.getCssvaluebyXpath(webPage, verifyFindStoreInvalidData[3][0],"color", softAssert);
			softAssert.assertEquals(errorMessageActualColor, verifyFindStoreInvalidData[2][1],"Color attribute verification failed. Expected Color : " + verifyFindStoreInvalidData[2][1]+ " Actual Color : " + errorMessageActualColor);
			String errorBoxActualColor = commonMethods.getCssvaluebyXpath(webPage, verifyFindStoreInvalidData[1][0],"color", softAssert);
			softAssert.assertEquals(errorBoxActualColor, verifyFindStoreInvalidData[3][1],"Color attribute verification failed. Expected Color : " + verifyFindStoreInvalidData[3][1]+ " Actual Color : " + errorBoxActualColor);
			softAssert.assertEquals(errorMsgActualText, verifyFindStoreInvalidData[1][1],"Expected Text : " + verifyFindStoreInvalidData[1][1] + " Actual Text : " + errorMsgActualText);
			softAssert.assertAll();
		}catch(Throwable e){
			mainPage.getScreenShotForFailure(webPage, "Verify_FindStore_for_InvalidData");
			softAssert.assertAll();
			Assert.fail(e.getLocalizedMessage());
		}
	}

	@Test(enabled = true, priority = 111, description = "Verify Find Store functionality for valid input")
	public void Verify_FindStore_for_ValidData() throws PageException, InterruptedException {
		SoftAssert softAssert = new SoftAssert();
		try{
			String[][] verifyValidRegionSearchData = ExcelUtil.readExcelData(DataFilePath, "StoreLocator",
					"verifyValidRegionSearchData");
			commonMethods.navigateToPage(webPage, storeLocatorURL, softAssert);
			connsStoreLocatorPage.closeLocationPopup(webPage, softAssert);
			CommonMethods.waitForWebElement(By.xpath(commonData[1][1]), webPage);
			commonMethods.clickElementbyXpath(webPage, verifyValidRegionSearchData[0][0], softAssert);
			connsStoreLocatorPage.closeLocationPopup(webPage, softAssert);
			commonMethods.clearTextBox(webPage, verifyValidRegionSearchData[1][0], softAssert);
			commonMethods.sendKeysbyXpath(webPage, verifyValidRegionSearchData[1][0], verifyValidRegionSearchData[0][1], softAssert);
			commonMethods.clickElementbyXpath(webPage, verifyValidRegionSearchData[2][0], softAssert);
			String regionPageActualData = commonMethods.getTextbyXpath(webPage, verifyValidRegionSearchData[3][0], softAssert);
			softAssert.assertTrue(regionPageActualData.contains(verifyValidRegionSearchData[1][1]),
					"Text verification failed. Expected Text : " + verifyValidRegionSearchData[1][1] + " Actual Text : "
							+ regionPageActualData);

			softAssert.assertAll();
		}catch(Throwable e){
			mainPage.getScreenShotForFailure(webPage, "Verify_FindStore_for_ValidData");
			softAssert.assertAll();
			Assert.fail(e.getLocalizedMessage());
		}
	}

	@Test(enabled = true, priority = 112, description = "Verify Find Store functionality using zip code search")
	public void Verify_FindStore_with_Zipcode() throws PageException, InterruptedException {
		SoftAssert softAssert = new SoftAssert();
		try{
			String[][] verifyZipCodeRegionSearchData = ExcelUtil.readExcelData(DataFilePath, "StoreLocator",
					"verifyZipCodeRegionSearch");
			commonMethods.navigateToPage(webPage, storeLocatorURL, softAssert);
			connsStoreLocatorPage.closeLocationPopup(webPage, softAssert);
			CommonMethods.waitForWebElement(By.xpath(commonData[1][1]), webPage);
			commonMethods.clickElementbyXpath(webPage, verifyZipCodeRegionSearchData[0][0], softAssert);
			connsStoreLocatorPage.closeLocationPopup(webPage, softAssert);
			commonMethods.clearTextBox(webPage, verifyZipCodeRegionSearchData[1][0], softAssert);
			commonMethods.sendKeysbyXpath(webPage, verifyZipCodeRegionSearchData[1][0],
					verifyZipCodeRegionSearchData[0][1], softAssert);
			commonMethods.clickElementbyXpath(webPage, verifyZipCodeRegionSearchData[2][0], softAssert);
			String regionPageActualData = commonMethods.getTextbyXpath(webPage, verifyZipCodeRegionSearchData[3][0], softAssert);
			softAssert.assertTrue(regionPageActualData.contains(verifyZipCodeRegionSearchData[1][1]),
					"Text verification failed. Expected Text : " + verifyZipCodeRegionSearchData[1][1] + " Actual Text : "
							+ regionPageActualData);
			softAssert.assertAll();
		}catch(Throwable e){
			mainPage.getScreenShotForFailure(webPage, "Verify_FindStore_with_Zipcode");
			softAssert.assertAll();
			Assert.fail(e.getLocalizedMessage());
		}
	}

	@Test(enabled = true, priority = 113, description = "Verify Find Store functionality using zip code along with radius search")
	public void Verify_FindStore_with_Zipcode_and_Radius() throws PageException, InterruptedException {
		SoftAssert softAssert = new SoftAssert();
		try{
			String[][] verifyZipCodeRadiusSearchData = ExcelUtil.readExcelData(DataFilePath, "StoreLocator",
					"verifyZipCodeRadiusSearch");
			commonMethods.navigateToPage(webPage, storeLocatorURL, softAssert);
			connsStoreLocatorPage.closeLocationPopup(webPage, softAssert);
			CommonMethods.waitForWebElement(By.xpath(commonData[1][1]), webPage);
			commonMethods.clickElementbyXpath(webPage, verifyZipCodeRadiusSearchData[0][0], softAssert);
			connsStoreLocatorPage.closeLocationPopup(webPage, softAssert);
			commonMethods.clearTextBox(webPage, verifyZipCodeRadiusSearchData[1][0], softAssert);
			commonMethods.sendKeysbyXpath(webPage, verifyZipCodeRadiusSearchData[1][0],
					verifyZipCodeRadiusSearchData[0][1], softAssert);
			commonMethods.clickElementbyXpath(webPage, verifyZipCodeRadiusSearchData[2][0], softAssert);
			String search50MilesActualData = commonMethods.getTextbyXpath(webPage, verifyZipCodeRadiusSearchData[3][0], softAssert);
			softAssert.assertTrue(search50MilesActualData.contains(verifyZipCodeRadiusSearchData[1][1]),
					"Text verification failed. Expected Text : " + verifyZipCodeRadiusSearchData[1][1] + " Actual Text : "
							+ search50MilesActualData);
			Select select = new Select(commonMethods.getWebElementbyXpath(webPage, verifyZipCodeRadiusSearchData[4][0], softAssert));
			select.selectByValue(verifyZipCodeRadiusSearchData[2][1]);
			commonMethods.clickElementbyXpath(webPage, verifyZipCodeRadiusSearchData[5][0], softAssert);
			String search75MilesActualData = commonMethods.getTextbyXpath(webPage, verifyZipCodeRadiusSearchData[6][0], softAssert);
			softAssert.assertTrue(search75MilesActualData.contains(verifyZipCodeRadiusSearchData[4][1]),
					"Text verification failed. Expected Text : " + verifyZipCodeRadiusSearchData[4][1] + " Actual Text : "
							+ search75MilesActualData);
			select = new Select(commonMethods.getWebElementbyXpath(webPage, verifyZipCodeRadiusSearchData[4][0], softAssert));
			select.selectByValue(verifyZipCodeRadiusSearchData[3][1]);
			commonMethods.clickElementbyXpath(webPage, verifyZipCodeRadiusSearchData[5][0], softAssert);
			CommonMethods.waitForWebElement(By.xpath(commonData[3][1]), webPage);
			String search125MilesActualData = commonMethods.getTextbyXpath(webPage, verifyZipCodeRadiusSearchData[7][0], softAssert);
			softAssert.assertTrue(search125MilesActualData.contains(verifyZipCodeRadiusSearchData[5][1]),
					"Text verification failed. Expected Text : " + verifyZipCodeRadiusSearchData[5][1] + " Actual Text : "
							+ search125MilesActualData);
			softAssert.assertAll();
		}catch(Throwable e){
			mainPage.getScreenShotForFailure(webPage, "Verify_FindStore_with_Zipcode_and_Radius");
			softAssert.assertAll();
			Assert.fail(e.getLocalizedMessage());
		}
	}
	
	@Test(enabled = true, priority = 114, description = "Verify Find Store functionality using city search")
	public void Verify_FindStore_with_CityName() throws PageException, InterruptedException {
		SoftAssert softAssert = new SoftAssert();
		try{
			String[][] verifyCitySearchData = ExcelUtil.readExcelData(DataFilePath, "StoreLocator",
					"verifyCitySearch");
			commonMethods.navigateToPage(webPage, storeLocatorURL, softAssert);
			connsStoreLocatorPage.closeLocationPopup(webPage, softAssert);
			CommonMethods.waitForWebElement(By.xpath(commonData[1][1]), webPage);
			commonMethods.clickElementbyXpath(webPage, verifyCitySearchData[0][0], softAssert);
			connsStoreLocatorPage.closeLocationPopup(webPage, softAssert);
			commonMethods.clearTextBox(webPage, verifyCitySearchData[1][0], softAssert);
			commonMethods.sendKeysbyXpath(webPage, verifyCitySearchData[1][0],
					verifyCitySearchData[0][1], softAssert);
			commonMethods.clickElementbyXpath(webPage, verifyCitySearchData[2][0], softAssert);
			String cityPageActualData = commonMethods.getTextbyXpath(webPage, verifyCitySearchData[3][0], softAssert);
			softAssert.assertTrue(cityPageActualData.contains(verifyCitySearchData[1][1]),
					"Text verification failed. Expected Text : " + verifyCitySearchData[1][1] + " Actual Text : "
							+ cityPageActualData);
			softAssert.assertAll();
		}catch(Throwable e){
			mainPage.getScreenShotForFailure(webPage, "Verify_FindStore_with_CityName");
			softAssert.assertAll();
			Assert.fail(e.getLocalizedMessage());
		}
	}
	
	@Test(enabled = true, priority = 115, description = "Verify Find Store functionality using zip code along with radius search")
	public void Verify_FindStore_with_CityName_and_Radius() throws PageException, InterruptedException {
		SoftAssert softAssert = new SoftAssert();
		try{
			String[][] verifyCityRadiusSearchData = ExcelUtil.readExcelData(DataFilePath, "StoreLocator",
					"verifyCityRadiusSearch");
			commonMethods.navigateToPage(webPage, storeLocatorURL, softAssert);
			connsStoreLocatorPage.closeLocationPopup(webPage, softAssert);
			CommonMethods.waitForWebElement(By.xpath(commonData[1][1]), webPage);
			commonMethods.clickElementbyXpath(webPage, verifyCityRadiusSearchData[0][0], softAssert);
			connsStoreLocatorPage.closeLocationPopup(webPage, softAssert);
			commonMethods.clearTextBox(webPage, verifyCityRadiusSearchData[1][0], softAssert);
			commonMethods.sendKeysbyXpath(webPage, verifyCityRadiusSearchData[1][0],
					verifyCityRadiusSearchData[0][1], softAssert);
			commonMethods.clickElementbyXpath(webPage, verifyCityRadiusSearchData[2][0], softAssert);
			String search50MilesActualData = commonMethods.getTextbyXpath(webPage, verifyCityRadiusSearchData[3][0], softAssert);
			softAssert.assertTrue(search50MilesActualData.contains(verifyCityRadiusSearchData[1][1]),
					"Text verification failed. Expected Text : " + verifyCityRadiusSearchData[1][1] + " Actual Text : "
							+ search50MilesActualData);
			Select select = new Select(commonMethods.getWebElementbyXpath(webPage, verifyCityRadiusSearchData[4][0], softAssert));
			select.selectByValue(verifyCityRadiusSearchData[2][1]);
			commonMethods.clickElementbyXpath(webPage, verifyCityRadiusSearchData[5][0], softAssert);
			String search75MilesActualData = commonMethods.getTextbyXpath(webPage, verifyCityRadiusSearchData[6][0], softAssert);
			softAssert.assertTrue(search75MilesActualData.contains(verifyCityRadiusSearchData[4][1]),
					"Text verification failed. Expected Text : " + verifyCityRadiusSearchData[4][1] + " Actual Text : "
							+ search75MilesActualData);
			select = new Select(commonMethods.getWebElementbyXpath(webPage, verifyCityRadiusSearchData[4][0], softAssert));
			select.selectByValue(verifyCityRadiusSearchData[3][1]);
			commonMethods.clickElementbyXpath(webPage, verifyCityRadiusSearchData[5][0], softAssert);
			String search125MilesActualData = commonMethods.getTextbyXpath(webPage, verifyCityRadiusSearchData[7][0], softAssert);
			softAssert.assertTrue(search125MilesActualData.contains(verifyCityRadiusSearchData[5][1]),
					"Text verification failed. Expected Text : " + verifyCityRadiusSearchData[5][1] + " Actual Text : "
							+ search125MilesActualData);
			softAssert.assertAll();
		}catch(Throwable e){
			mainPage.getScreenShotForFailure(webPage, "Verify_FindStore_with_CityName_and_Radius");
			softAssert.assertAll();
			Assert.fail(e.getLocalizedMessage());
		}
	}
	
	@Test(enabled = true, priority = 116, description = "Verify functionality of VIEW ALL link")
	public void Verify_View_All_Link() throws PageException, InterruptedException {
		SoftAssert softAssert = new SoftAssert();
		try{
			commonMethods.navigateToPage(webPage, storeLocatorURL, softAssert);
			connsStoreLocatorPage.closeLocationPopup(webPage, softAssert);
			CommonMethods.waitForWebElement(By.xpath(commonData[1][1]), webPage);
			String[][] verifyViewAllLinkData = ExcelUtil.readExcelData(DataFilePath, "StoreLocator","verifyViewAllLink");
			String actualLinkText = commonMethods.getTextbyXpath(webPage, verifyViewAllLinkData[0][0], softAssert);
			softAssert.assertEquals(actualLinkText, verifyViewAllLinkData[0][1],"Text verification failed. Expected text : " + verifyViewAllLinkData[0][1] + " Actual text : " + actualLinkText);
			List<String> actualCssValues= commonMethods.getFontProperties(webPage, verifyViewAllLinkData[0][0], softAssert);
			if(testType.equalsIgnoreCase("Web")){
				softAssert.assertTrue(actualCssValues.get(0).contains(verifyViewAllLinkData[1][1]), "CSS value verification failed for link " + verifyViewAllLinkData[0][1] + "Expected Value : "+ verifyViewAllLinkData[1][1] + " Actual Value : " + actualCssValues.get(0));
			}else{
				softAssert.assertTrue(actualCssValues.get(0).contains(verifyViewAllLinkData[6][1]), "CSS value verification failed for link " + verifyViewAllLinkData[0][1] + "Expected Value : "+ verifyViewAllLinkData[1][1] + " Actual Value : " + actualCssValues.get(0));
			}
			softAssert.assertTrue(actualCssValues.get(1).contains(verifyViewAllLinkData[2][1]),"CSS value verification failed for link " + verifyViewAllLinkData[0][1] + "Expected Value : "+ verifyViewAllLinkData[2][1] + " Actual Value : " + actualCssValues.get(1));
			if(!browserName.equalsIgnoreCase("IE")){
				softAssert.assertTrue(actualCssValues.get(2).contains(verifyViewAllLinkData[3][1]),"CSS value verification failed for link " + verifyViewAllLinkData[0][1] + "Expected Value : "+ verifyViewAllLinkData[3][1] + " Actual Value : " + actualCssValues.get(2));				
			}else{
				softAssert.assertTrue(actualCssValues.get(2).replaceAll("'", "").contains(verifyViewAllLinkData[4][1]),"CSS value verification failed for link " + verifyViewAllLinkData[0][1] + "Expected Value : "+ verifyViewAllLinkData[4][1] + " Actual Value : " + actualCssValues.get(2));
			}
			String actualUrl = commonMethods.clickAndGetPageURL(webPage, verifyViewAllLinkData[0][0], verifyViewAllLinkData[0][1], softAssert,commonData[2][1]);
			softAssert.assertTrue(actualUrl.contains(verifyViewAllLinkData[5][1]),"URL verification failed for link : '" + verifyViewAllLinkData[0][1] + "'. Expected URL - "+ verifyViewAllLinkData[5][1] + " Actual URL - " + actualUrl);
			softAssert.assertAll();
		}catch(Throwable e){
			mainPage.getScreenShotForFailure(webPage, "Verify_View_All_Link");
			softAssert.assertAll();
			Assert.fail(e.getLocalizedMessage());
		}
	}
	
	@Test(enabled = true, priority = 117, description = "Verify functionality of all store locator links")
	public void Verify_All_Store_Locator_Links() throws PageException, InterruptedException {
		SoftAssert softAssert = new SoftAssert();
		try{
			commonMethods.navigateToPage(webPage, storeLocatorURL, softAssert);
			connsStoreLocatorPage.closeLocationPopup(webPage, softAssert);
			CommonMethods.waitForWebElement(By.xpath(commonData[1][1]), webPage);
			String[][] verifyAllStoreLocatorData = ExcelUtil.readExcelData(DataFilePath, "StoreLocator","verifyAllStoreLocatorLinks");
			for(int i=0;i<verifyAllStoreLocatorData.length;i++){
				commonMethods.navigateToPage(webPage, commonData[0][1], softAssert);
				String actualUrl = commonMethods.clickAndGetPageURL(webPage, verifyAllStoreLocatorData[i][2], verifyAllStoreLocatorData[i][1], softAssert,commonData[5][1]);
				softAssert.assertTrue(actualUrl.contains(verifyAllStoreLocatorData[i][3]),"Expected URL: "+verifyAllStoreLocatorData[i][3]+" Actual URL: "+actualUrl);
			}
			softAssert.assertAll();
		}catch(Throwable e){
			mainPage.getScreenShotForFailure(webPage, "Verify_All_Store_Locator_Links");
			softAssert.assertAll();
			Assert.fail(e.getLocalizedMessage());
		}
	}
	
	@Test(enabled = true, priority = 118, description = "Verify functionality of Store Page")
	public void Verify_Visit_Store_Page_Link() throws PageException, InterruptedException {
		SoftAssert softAssert = new SoftAssert();
		try{
			String[][] verifyStorePageData = ExcelUtil.readExcelData(DataFilePath, "StoreLocator","verifyVisitStorePageLink");
			String storeText="";
			for(int i=0;i<verifyStorePageData.length;i++){
				commonMethods.navigateToPage(webPage, storeLocatorURL, softAssert);
				connsStoreLocatorPage.closeLocationPopup(webPage, softAssert);
				CommonMethods.waitForWebElement(By.xpath(commonData[1][1]), webPage);
				commonMethods.clickElementbyXpath(webPage, verifyStorePageData[i][1],softAssert);
				CommonMethods.waitForWebElement(By.xpath(commonData[2][1]), webPage);
				String actualUrl = commonMethods.clickAndGetPageURL(webPage, verifyStorePageData[i][3], verifyStorePageData[i][0]+" -"+verifyStorePageData[i][2], softAssert,commonData[5][1]);
				softAssert.assertTrue(actualUrl.contains(verifyStorePageData[i][4]),"Expected - "+verifyStorePageData[i][4]+"Actual - "+actualUrl);
				if(testType.equalsIgnoreCase("Web")){
					storeText = commonMethods.getTextbyXpath(webPage, verifyStorePageData[i][5], softAssert);	
				}else{
					storeText = commonMethods.getTextbyXpath(webPage, verifyStorePageData[i][9], softAssert);
				}
				softAssert.assertEquals(storeText, verifyStorePageData[i][6],"Text verification failed. Expected text: " + verifyStorePageData[i][6] + " Actual text: " + storeText);
				String customerReviewText = commonMethods.getTextbyXpath(webPage, verifyStorePageData[i][7], softAssert);
				softAssert.assertTrue(customerReviewText.contains(verifyStorePageData[i][8]),"Text verification failed. Expected text: " + verifyStorePageData[i][8] + " Actual text: " + customerReviewText);
			}
			softAssert.assertAll();
		}catch(Throwable e){
			mainPage.getScreenShotForFailure(webPage, "Verify_Visit_Store_Page_Link");
			softAssert.assertAll();
			Assert.fail(e.getLocalizedMessage());
		}
	}
	
	@Test(enabled = true, priority = 119, description = "Verify functionality of Store Page")
	public void Verify_Store_Distance_In_Miles() throws PageException, InterruptedException {
		SoftAssert softAssert = new SoftAssert();
		try{
			String[][] verifyStoreDistanceInMilesData = ExcelUtil.readExcelData(DataFilePath, "StoreLocator","verifyStoreDistanceInMiles");
			commonMethods.navigateToPage(webPage, storeLocatorURL, softAssert);
			connsStoreLocatorPage.closeLocationPopup(webPage, softAssert);
			CommonMethods.waitForWebElement(By.xpath(commonData[1][1]), webPage);
			commonMethods.clearTextBox(webPage, verifyStoreDistanceInMilesData[1][0], softAssert);
			commonMethods.sendKeysbyXpath(webPage, verifyStoreDistanceInMilesData[1][0],verifyStoreDistanceInMilesData[0][1], softAssert);
			Select select = new Select(commonMethods.getWebElementbyXpath(webPage, verifyStoreDistanceInMilesData[4][0], softAssert));
			select.selectByValue(verifyStoreDistanceInMilesData[2][1]);
			commonMethods.clickElementbyXpath(webPage, verifyStoreDistanceInMilesData[2][0], softAssert);
			String actualUrl = commonMethods.getPageUrl(webPage, softAssert);
			softAssert.assertTrue(actualUrl.contains(verifyStoreDistanceInMilesData[5][1]),"URL mismatch. Expected URL: "+verifyStoreDistanceInMilesData[5][1]+", Actual URL: "+actualUrl);
			String search10MilesActualData = commonMethods.getTextbyXpath(webPage, verifyStoreDistanceInMilesData[3][0], softAssert);
			softAssert.assertTrue(search10MilesActualData.contains(verifyStoreDistanceInMilesData[1][1]),"Text verification failed. Expected Text : " + verifyStoreDistanceInMilesData[1][1] + " Actual Text : "+ search10MilesActualData);
			String search10MilesActualColor = commonMethods.getCssvaluebyXpath(webPage, verifyStoreDistanceInMilesData[3][0], "color", softAssert);
			softAssert.assertTrue(search10MilesActualColor.contains(verifyStoreDistanceInMilesData[6][1]),"CSS value verification failed for " + search10MilesActualData + ". Expected value: "+ verifyStoreDistanceInMilesData[6][1] + ", Actual value: " + search10MilesActualColor);
			select = new Select(commonMethods.getWebElementbyXpath(webPage, verifyStoreDistanceInMilesData[4][0], softAssert));
			select.selectByValue(verifyStoreDistanceInMilesData[3][1]);
			commonMethods.clickElementbyXpath(webPage, verifyStoreDistanceInMilesData[5][0], softAssert);
			String search100MilesActualData = commonMethods.getTextbyXpath(webPage, verifyStoreDistanceInMilesData[6][0], softAssert);
			softAssert.assertTrue(search100MilesActualData.contains(verifyStoreDistanceInMilesData[4][1]),"Text verification failed. Expected Text : " + verifyStoreDistanceInMilesData[4][1] + " Actual Text : "+ search100MilesActualData);
			String search100MilesActualColor = commonMethods.getCssvaluebyXpath(webPage, verifyStoreDistanceInMilesData[6][0], "color", softAssert);
			softAssert.assertTrue(search100MilesActualColor.contains(verifyStoreDistanceInMilesData[6][1]),"CSS value verification failed for " + search100MilesActualData + ". Expected value: "+ verifyStoreDistanceInMilesData[6][1] + ", Actual value: " + search100MilesActualColor);			
			softAssert.assertAll();
		}catch(Throwable e){
			mainPage.getScreenShotForFailure(webPage, "Verify_Store_Distance_In_Miles");
			softAssert.assertAll();
			Assert.fail(e.getLocalizedMessage());
		}
	}
	
	@Test(enabled = true, priority = 120, description = "Verify functionality of Store Page")
	public void Verify_All_Store_Locator_Page_Text() throws PageException, InterruptedException {
		SoftAssert softAssert = new SoftAssert();
		try{
			String[][] verifyAllStoreLocatorPageTextData = ExcelUtil.readExcelData(DataFilePath, "StoreLocator","verifyAllStoreLocatorPageText");
			commonMethods.navigateToPage(webPage, storeLocatorURL, softAssert);
			connsStoreLocatorPage.closeLocationPopup(webPage, softAssert);
			CommonMethods.waitForWebElement(By.xpath(commonData[1][1]), webPage);
			commonMethods.clickElementbyXpath(webPage, verifyAllStoreLocatorPageTextData[0][0],softAssert);
			String actualUrl = commonMethods.getPageUrl(webPage, softAssert);
			softAssert.assertTrue(actualUrl.contains(verifyAllStoreLocatorPageTextData[2][1]),"URL verification failed. Expected - "+verifyAllStoreLocatorPageTextData[2][1]+"Actual - "+actualUrl);
			String allStoreLocatorPageText = commonMethods.getTextbyXpath(webPage, verifyAllStoreLocatorPageTextData[1][0], softAssert);
			softAssert.assertEquals(allStoreLocatorPageText, verifyAllStoreLocatorPageTextData[1][1],"Text verification failed. Expected text: " + verifyAllStoreLocatorPageTextData[1][1] + " Actual text: " + allStoreLocatorPageText);
			softAssert.assertAll();
		}catch(Throwable e){
			mainPage.getScreenShotForFailure(webPage, "Verify_All_Store_Locator_Page_Text");
			softAssert.assertAll();
			Assert.fail(e.getLocalizedMessage());
		}
	}
	
	
}
