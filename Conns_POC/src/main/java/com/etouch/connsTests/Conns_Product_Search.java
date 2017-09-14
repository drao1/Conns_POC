package com.etouch.connsTests;

import java.awt.AWTException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.apache.commons.logging.Log;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.etouch.common.BaseTest;
import com.etouch.common.TafExecutor;
import com.etouch.connsPages.ConnsMainPage;
import com.etouch.taf.core.TestBed;
import com.etouch.taf.core.TestBedManager;
import com.etouch.taf.core.config.TestBedManagerConfiguration;
import com.etouch.taf.core.datamanager.excel.annotations.IExcelDataFiles;
import com.etouch.taf.core.exception.PageException;
import com.etouch.taf.util.CommonUtil;
import com.etouch.taf.util.ExcelUtil;
import com.etouch.taf.util.LogUtil;
import com.etouch.taf.util.SoftAssertor;
import com.etouch.taf.webui.selenium.LayoutManager;
import com.etouch.taf.webui.selenium.WebPage;

//@Test(groups = "Conns_Product_Search")
@IExcelDataFiles(excelDataFiles = { "CreditAppData=testData" })
public class Conns_Product_Search extends BaseTest {
	static String platform;
	static Log log = LogUtil.getLog(Conns_Product_Search.class);
	static String AbsolutePath = TafExecutor.class.getProtectionDomain().getCodeSource().getLocation().getPath();
	static String videoLocation = AbsolutePath.substring(0, AbsolutePath.indexOf("/target/classes/")).substring(1)
			.concat("/ConnsTestData/Output/Env/Video");
	private String url = null;
	private WebPage webPage;
	private ConnsMainPage mainPage;
	String testBedName;
	TestBed testBed;
	Path path;
	String DataFilePath;
	String testType;
	String testEnv;

	@BeforeClass(alwaysRun = true)
	public void setUp(ITestContext context) throws InterruptedException, FileNotFoundException, IOException {
		try {
			testBedName = context.getCurrentXmlTest().getAllParameters().get("testBedName");
			CommonUtil.sop("Test bed Name is " + testBedName);
			testBed = TestBedManager.INSTANCE.getCurrentTestBeds().get(testBedName);
			testType = TestBedManager.INSTANCE.getCurrentTestBeds().get(testBedName).getTestType();
			System.out.println("Test Type is : " + testType);
			try {
				testEnv = System.getenv().get("Environment");
				System.out.println("testEnv is : " + testEnv);
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
					mainPage = new ConnsMainPage(url, webPage);

				}
			} catch (Exception e) {
				log.info("errr is " + e);
				SoftAssertor.addVerificationFailure(e.getMessage());
			}
		} catch (Exception e) {
			CommonUtil.sop("Error is for" + testBedName + " -----------" + e);
			SoftAssertor.addVerificationFailure(e.getMessage());
		}
	}

	@AfterTest
	public void releaseResources() throws IOException, AWTException {
		// SpecializedScreenRecorder.stopVideoRecording();
	}

	@Test(priority = 1, enabled = true)
	public void Verify_Search_Functionality_And_Results_Contents() {
		try {
			String[][] test = ExcelUtil.readExcelData(DataFilePath, "ProductSearch", "verifyProductSearchUsingKeyword");
			String Identifier = test[0][0];
			String ProductName = test[0][1];
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
	}

	@Test(priority = 2, enabled = true)
	public void Verify_Product_Search_And_Shorting_By_Product_Name() throws InterruptedException {
		try {
			String[][] test = ExcelUtil.readExcelData(DataFilePath, "ProductSearch", "verifyProductSearchAndShortByName");
			String Identifier = test[0][0];
			String ProductName = test[0][1];
			webPage.findObjectById(Identifier).sendKeys(ProductName);
			webPage.findObjectByClass(test[0][2]).click();
			log.info("Clicked on element " + test[0][2]);
			String productDescription = webPage.findObjectByxPath(test[0][3]).getText();
			log.info("productDescription" + productDescription);
			Assert.assertTrue(productDescription.contains(ProductName),
					"Product description: " + productDescription + " not having: " + ProductName);
			if (testType.equalsIgnoreCase("Web")) {
				Select s = new Select(webPage.getDriver().findElement(By.xpath((test[0][5]))));
				Thread.sleep(18000);
				s.selectByVisibleText(test[0][6]);
				Thread.sleep(8000);
				List<WebElement> elementList = webPage.getDriver().findElements(By.xpath(test[0][7]));
				log.info("element " + elementList.size() + "elementList: " + elementList);
				log.info("element is shorted: " + mainPage.isSorted(elementList));
				Assert.assertEquals(mainPage.isSorted(elementList), true, "element is Not shorted");
				webPage.findObjectByxPath(test[0][8]).click();
				Thread.sleep(5000);
				webPage.getBackToUrl();
			}
		} catch (PageException e) {
			mainPage.getScreenShotForFailure(webPage, "Verify_Product_Search_And_Shorting_By_Product_Name");
			e.printStackTrace();
		}
	}

	@Test(priority = 3, enabled = true)
	public void Verify_Product_Search_And_Number_Of_Product_Displayed() throws InterruptedException {
		try {
			String[][] test = ExcelUtil.readExcelData(DataFilePath, "ProductSearch", "verifyProductSearchAndNumberPerPage");
			String Identifier = test[0][0];
			String ProductName = test[0][1];
			webPage.findObjectById(Identifier).sendKeys(ProductName);
			webPage.findObjectByClass(test[0][2]).click();
			log.info("Clicked on element " + test[0][2]);
			String productDescription = webPage.findObjectByxPath(test[0][3]).getText();
			log.info("productDescription" + productDescription);
			Assert.assertTrue(productDescription.contains(ProductName),
					"Product description: " + productDescription + " not having: " + ProductName);
			if (testType.equalsIgnoreCase("Web")) {
				Select s = new Select(webPage.getDriver()
						.findElement(By.xpath((test[0][5]))));
				List<WebElement> list = s.getOptions();
				String str[] = { list.get(0).getText().toString(), list.get(1).getText().toString(),
						list.get(2).getText().toString() };
				String str2[]=test[0][6].split(",");
				int number;
				for (int i=0;i<str.length;i++) {
					number = Integer.parseInt(str[i].trim());
					Assert.assertEquals(number,Integer.parseInt(str2[i]),"Number List:  ");
					log.info("Started iteration for -->" + number);
					s.selectByVisibleText(String.valueOf(number));
					Thread.sleep(5000);
					List<WebElement> elementList = webPage.getDriver().findElements(By.xpath(test[0][7]));
					log.info("Number: " + number + "    element Size-->" + elementList.size());
					Assert.assertEquals(elementList.size() <= number, true, "element is Not As Expected");
					log.info("Completed for iteration-->");
					s = new Select(webPage.getDriver()
							.findElement(By.xpath((test[0][5]))));
				}
				webPage.getBackToUrl();
			}
		} catch (PageException e) {
			mainPage.getScreenShotForFailure(webPage, "Verify_Product_Search_And_Number_Of_Product_Displayed");
			e.printStackTrace();
		}
	}

	@Test(priority = 4, enabled = true)
	public void Verify_Column_Layout_For_Product_Search() throws PageException, InterruptedException {
		try {
			String[][] test = ExcelUtil.readExcelData(DataFilePath, "ProductSearch", "verifyProductSearchAndShortByName");
			if (testType.equalsIgnoreCase("Web")) {
				LayoutManager layoutManager = new LayoutManager();
				int height[] = { 500, 500, 500 };
				int width[] = { 350, 700, 1050 };
				for (int i = 0; i < width.length; i++) {
					webPage.resize(width[i], height[i]);
					Thread.sleep(4000);
					int cols = layoutManager.getColumnLayout(width[i], height[i]);
					log.info("Column Layout " + cols);
					if (cols == 1 || cols == 2) {
						log.info("Column Layout equivalent to Mobile or Tablets for column layout = " + cols);
						Assert.assertEquals(true, webPage.findObjectByxPath(test[0][4]).isDisplayed(),
								"Main Menu not displayed");
					} else {
						log.info("Column Layout equivalent to browser for column layout= " + cols);
						Assert.assertEquals(webPage.findObjectByxPath(test[0][4]).isDisplayed(), false,
								"Main Menu displayed");
					}
				}
				webPage.getDriver().manage().window().maximize();
			} else {
				log.info("Column layout testing can not be done for Devices");
			}
		} catch (Exception e) {
			mainPage.getScreenShotForFailure(webPage, "Verify_Column_Layout_For_Product_Search");
			e.printStackTrace();
		}
	}

	public int getColumnLayout(int width, int height) {
		log.info("Width: " + width);
		int cols = -1;
		if (width >= 980) {
			log.info("Layout 3 Columns, Width : " + width);
			cols = 3;
		} else if (width < 980 && width > 400) {
			log.info("Layout 2 Columns, width : " + width);
			cols = 2;
		} else if (width <= 400) {
			log.info("Layout 1 Columns, width : " + width);
			cols = 1;
		}

		return cols;
	}

}
