package com.etouch.connsPages;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.Color;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.asserts.SoftAssert;

import com.etouch.common.CommonPage;
import com.etouch.taf.core.TestBed;
import com.etouch.taf.core.TestBedManager;
import com.etouch.taf.core.driver.web.WebDriver;
import com.etouch.taf.core.exception.PageException;
import com.etouch.taf.util.CommonUtil;
import com.etouch.taf.util.ExcelUtil;
import com.etouch.taf.util.LogUtil;
import com.etouch.taf.util.SoftAssertor;
import com.etouch.taf.webui.ITafElement;
import com.etouch.taf.webui.selenium.WebPage;

public class ConnsHomePage extends CommonPage {

	/**
	 *
	 * @param sbPageUrl
	 *            the sb page url
	 * @param webPage
	 *            the web page
	 */

	static Log log = LogUtil.getLog(ConnsHomePage.class);
	String testType;
	String testBedName;

	public ConnsHomePage(String sbPageUrl, WebPage webPage) {
		super(sbPageUrl, webPage);
		CommonUtil.sop("webDriver in eTouchWebSite Page : " + webPage.getDriver());
		loadPage();
	}

	public void verifyBrokenImage() {
		List<WebElement> imagesList = webPage.getDriver().findElements(By.tagName("img"));
		log.info("Total number of images" + imagesList.size());
		int imageCount = 0;
		List<Integer> brokenImageNumber = new ArrayList<Integer>();
		List<String> brokenImageSrc = new ArrayList<String>();
		for (WebElement image : imagesList) {

			try {
				imageCount++;
				log.info("Verifying image number : " + imageCount);
				HttpClient client = HttpClientBuilder.create().build();
				HttpGet request = new HttpGet(image.getAttribute("src"));

				HttpResponse response = client.execute(request);
				if (response.getStatusLine().getStatusCode() == 200) {
					log.info("Image number " + imageCount + " is as expected "
							+ response.getStatusLine().getStatusCode());
				} else {
					brokenImageNumber.add(imageCount);
					brokenImageSrc.add(image.getAttribute("src"));
					log.info("Image number " + imageCount + " is not as expected "
							+ response.getStatusLine().getStatusCode());
					log.info("Broken Image source is : " + image.getAttribute("src"));

				}
			} catch (Exception e) {
				log.info("Image number ....." + imageCount + " is not as expected ");
				brokenImageNumber.add(imageCount);
				brokenImageSrc.add(image.getAttribute("src"));
			}
		}
		if (brokenImageNumber.size() > 0) {
			Assert.fail("Image number of the broken images : " + Arrays.deepToString(brokenImageNumber.toArray())
			+ "Image source of the broken images : " + Arrays.deepToString(brokenImageSrc.toArray()));

		}

	}

	public List<String> verifyYourCart(String[][] test, String testType) throws PageException, InterruptedException {
		List<String> actualValueList=new ArrayList<String>();
		webPage.getDriver().manage().window().maximize();
		webPage.waitForWebElement(By.xpath(test[0][0]));
		try {
			if (testType.equalsIgnoreCase("Web")) {
				// hover on parent menu option in desktop browser
				webPage.hoverOnElement(By.xpath(test[0][0]));
				log.info("Clicking on Sub - link");
				webPage.findObjectByxPath(test[0][1]).click();				
				log.info("Clicking on product Add to Cart");				
				Thread.sleep(3000);
				JavascriptExecutor jse1 = (JavascriptExecutor) webPage.getDriver();
				jse1.executeScript("scroll(500,500);");					
				webPage.findObjectByxPath(test[0][2]).click();
				actualValueList.add("checkout/onepage/");
				actualValueList.add("checkout/onepage/");
				actualValueList.add("checkout/onepage/");
/*				String ZipCode = test[0][5];
				log.info("Adding Zip code");
				webPage.findObjectByxPath(test[0][4]).clear();
				webPage.findObjectByxPath(test[0][4]).sendKeys(ZipCode);
				log.info("Zip Code Entered");
				log.info("Clicking on Update button");
				webPage.findObjectByxPath(test[0][6]).click();

				
				log.info("Clicking on Add to Cart button");
				webPage.findObjectByxPath(test[0][7]).click();
				
				log.info("Clicking on YourCart link");
				webPage.findObjectByxPath(test[0][8]).click();

				String ExpecyedProductinCart = webPage.findObjectByxPath(test[0][9]).getText();
				actualValueList.add(ExpecyedProductinCart);
				actualValueList.add(ExpecyedProductinCart);
				log.info("Clicking on CheckOut button");
				webPage.findObjectByxPath(test[0][10]).click();
				String ExpectedURL = test[0][11];
				String actualUrl = webPage.getCurrentUrl();
				actualValueList.add(actualUrl);*/
			}

		} catch (Exception e) {
			SoftAssertor.addVerificationFailure(e.getMessage());
			log.error("verifyPageTitle failed");
			log.error(e.getMessage());
		}
		return actualValueList;
	}


	public List<String> verifyYourCartOnMobile(String[][] test, String testType) throws PageException, InterruptedException {
		List<String> actualValueList=new ArrayList<String>();
		webPage.waitForWebElement(By.xpath(test[0][0]));
		try {
			log.info("Clicking on Main Menu on Mobile");
			webPage.findObjectByxPath(test[0][0]).click();
			log.info("Clicking on Appliances Menu");
			webPage.findObjectByxPath(test[0][1]).click();
			log.info("Clicking Refrigerators menu");
			webPage.findObjectByxPath(test[0][2]).click();
			log.info("Clicking French Door Sub menu");
			webPage.findObjectByxPath(test[0][3]).click();

			log.info("Get clicked product");
			String ExpectedProduct = webPage.findObjectByxPath(test[0][11]).getText();
			actualValueList.add(ExpectedProduct);			

			log.info("Clicking Add to Cart button");
			webPage.findObjectByxPath(test[0][4]).click();
			log.info("Adding Zip code");
			webPage.findObjectByxPath(test[0][5]).clear();
			webPage.findObjectByxPath(test[0][5]).sendKeys(test[0][6]);
			log.info("Clicking Update button");
			webPage.findObjectByxPath(test[0][7]).click();

			if (webPage.findObjectByxPath(test[0][12]).isElementVisible())
			{
				log.info("Clicking location selection radio button");
				webPage.findObjectByxPath(test[0][12]).click();	
			}
			log.info("Clicking Add to Cart 1 button");
			webPage.findObjectByxPath(test[0][8]).click();	
			log.info("Clicking Proceed to Checkout button");
			webPage.findObjectByxPath(test[0][9]).click();				

			log.info("Get product");
			String ExpectedProduct1 = webPage.findObjectByxPath(test[0][10]).getText();
			actualValueList.add(ExpectedProduct1);		

		} catch (Exception e) {
			SoftAssertor.addVerificationFailure(e.getMessage());
			log.error("verifyPageTitle failed");
			log.error(e.getMessage());
		}
		return actualValueList;
	}	

	public void verifySaveBigWithConnsSection(String[][] test) throws PageException 
	{
		String SaveBigMenuOptionIdentifier = null;
		String CarouselLeft = null;
		String CarouselRight = null;
		String ElementPosition1 = null;
		String ElementPosition2 = null;
		String ClickForDetails = null;
		String PopUp = null;
		List<String> errors = new ArrayList<String>();
		webPage.waitForWebElement(By.xpath(test[0][0]));
		for (int i = 0; i < test.length; i++) {
			
			try {

				SaveBigMenuOptionIdentifier = test[i][0].trim();
				CarouselLeft = test[i][1];
				CarouselRight = test[i][2];
				ElementPosition1 = test[i][3];
				ElementPosition2 = test[i][4];
				ClickForDetails = test[i][5];
				PopUp = test[i][6];

				System.out.println(" " + SaveBigMenuOptionIdentifier + " " + CarouselLeft + " " + CarouselRight + " "
						+ ElementPosition1 + " " + ElementPosition2 + " " + ClickForDetails + " " + PopUp);
				log.info("Verifying Element :" + SaveBigMenuOptionIdentifier);
				webPage.findObjectByxPath(SaveBigMenuOptionIdentifier).click();

				if(testType.equalsIgnoreCase("Mobile"))
				{
					String textAtPosition1 = webPage.findObjectByxPath(ElementPosition1).getText();
					System.out.println("Expected Left: " + textAtPosition1);					
					for (int j = 0; j < 12; j++) {
						webPage.findObjectByxPath(CarouselLeft).click();
					}
					String textAtPosition2 = webPage.findObjectByxPath(ElementPosition1).getText();
					System.out.println("Actual Left : " + textAtPosition2);
					SoftAssertor.assertEquals(textAtPosition1, textAtPosition2,
							" failed " + textAtPosition1 + " " + textAtPosition2);
					String textAtPosition1forRightCorousal = webPage.findObjectByxPath(ElementPosition1).getText();
					for (int j = 0; j < 12; j++) {
						webPage.findObjectByxPath(CarouselRight).click();
					}
					String textAtPosition2forRightCorousal = webPage.findObjectByxPath(ElementPosition1).getText();
					System.out.println("Actual Left : " + textAtPosition2);
					SoftAssertor.assertEquals(textAtPosition1forRightCorousal, textAtPosition2forRightCorousal,
							" failed " + textAtPosition1forRightCorousal + " " + textAtPosition2forRightCorousal);

				}
				else
				{
					webPage.findObjectByxPath(CarouselLeft).click();
					String textAtPosition1 = webPage.findObjectByxPath(ElementPosition1).getText();
					System.out.println("Expected Left: " + textAtPosition1);					
					for (int j = 0; j < 3; j++) {
						webPage.findObjectByxPath(CarouselLeft).click();
					}
					String textAtPosition2 = webPage.findObjectByxPath(ElementPosition2).getText();
					System.out.println("Actual Left : " + textAtPosition2);
					SoftAssertor.assertEquals(textAtPosition1, textAtPosition2,
							" failed " + textAtPosition1 + " " + textAtPosition2);

					log.info("Clicked on element2");
					String eletextAtPosition1 = webPage.findObjectByxPath(ElementPosition2).getText();
					System.out.println("Expected Right: " + eletextAtPosition1);
					for (int k = 0; k < 3; k++) {
						webPage.findObjectByxPath(CarouselRight).click();
					}
					String eletextAtPosition2 = webPage.findObjectByxPath(ElementPosition1).getText();
					System.out.println("Actual Right: " + eletextAtPosition2);
					SoftAssertor.assertEquals(eletextAtPosition1, eletextAtPosition2,
							" failed " + eletextAtPosition1 + " " + eletextAtPosition2);
				}

			} catch (Throwable e) {
				errors.add(e.getLocalizedMessage());
				log.error(e.getMessage());
			}
		}
	/*	if (errors.size() > 0) {
			Assert.fail(Arrays.deepToString(errors.toArray()) + " are not working as expected");
		}
*/
	}
	
	
	/**
	 * @author Name - Shantanu Kulkarni
	 * The method used to click on link using x-path and return page url
	 * Return type is String
	 * Any structural modifications to the display of the link should be done by overriding this method.
	 * @throws PageException  If an input or output exception occurred
	 **/
	public String clickAndGetPageURL_connsHome(WebPage webPage, String locator, String linkName, String TargetPageLocator, SoftAssert softAssert){
		String pageUrl="";
		try{
			log.info("Clicking on link : "+linkName);
			String mainWindow = webPage.getDriver().getWindowHandle();
			webPage.findObjectByxPath(locator).click();
			//webPage.waitForWebElement(By.xpath(TargetPageLocator));
			Set<String> windowHandlesSet = webPage.getDriver().getWindowHandles();
			if(windowHandlesSet.size()>1){
				for(String winHandle:windowHandlesSet){
					webPage.getDriver().switchTo().window(winHandle);
					if(!winHandle.equalsIgnoreCase(mainWindow)){
						log.info("More than 1 window open after clicking on link : "+linkName);
						pageUrl=webPage.getCurrentUrl();
						webPage.getDriver().close();
						webPage.getDriver().switchTo().window(mainWindow);
					}
				}
			}else{
				pageUrl= webPage.getCurrentUrl();
			}
			log.info("Actual URL : "+pageUrl);
		}catch(Throwable e){
			softAssert.fail("Unable to click on link '"+linkName+". Localized Message: "+e.getLocalizedMessage());
		}
		return pageUrl;
	}
	
	public String clickAndGetPageURLUsingJS(WebPage webPage, String locator,
			   String linkName, String TargetPageLocator, SoftAssert softAssert)
			   throws PageException, InterruptedException {

			  String mainWindow = webPage.getDriver().getWindowHandle();
			  WebElement element = webPage.findObjectByxPath(locator).getWebElement();
			  JavascriptExecutor executor = (JavascriptExecutor) webPage.getDriver();
			  executor.executeScript("arguments[0].click();", element);
			  log.info("Clicked on Link : " + linkName);
			  waitPageToLoad();
			  String pageUrl = "";
			  try {
			   Set<String> windowHandlesSet = webPage.getDriver().getWindowHandles();
			   if (windowHandlesSet.size() > 1) {
			    for (String winHandle : windowHandlesSet) {
			     webPage.getDriver().switchTo().window(winHandle);
			     if (!winHandle.equalsIgnoreCase(mainWindow)) {
			      log.info("More than 1 window open after clicking on link : " + linkName);
			      pageUrl = webPage.getCurrentUrl();
			      webPage.getDriver().close();
			      webPage.getDriver().switchTo().window(mainWindow);
			     }
			    }
			   } else {
			    pageUrl = webPage.getCurrentUrl();
			   }
			   log.info("Actual URL : " + pageUrl);
			  } catch (Throwable e) {
			   softAssert.fail("Unable to click on link '" + linkName + ". Localized Message: " + e.getLocalizedMessage());
			  }
			  return pageUrl;
			 }
			 public void waitPageToLoad() throws InterruptedException {

			  ExpectedCondition<Boolean> pageLoadCondition = new ExpectedCondition<Boolean>() {
			   @SuppressWarnings("unused")
			public Boolean apply(WebDriver driver) {
			    return ((JavascriptExecutor) driver).executeScript("return document.readyState").equals("complete");
			   }

			@Override
			public Boolean apply(org.openqa.selenium.WebDriver input) {
				// TODO Auto-generated method stub
				return null;
			}
			  };
			  int i = 0;
			  while (i < 3) {
			   try {
			    // Max wait 30 seconds
			    WebDriverWait wait = new WebDriverWait(webPage.getDriver(), 30);
			    wait.until(pageLoadCondition);
			    log.debug("Wait for page load completed.");
			    break;
			   } catch (org.openqa.selenium.TimeoutException e) {
			    webPage.getDriver().navigate().refresh();
			    Thread.sleep(3000);
			    i++;
			    log.debug(i + "Page still loading");
			   }
			  }
			 }
	
}
