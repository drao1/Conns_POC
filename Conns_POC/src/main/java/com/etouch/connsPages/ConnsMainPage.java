package com.etouch.connsPages;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;

import com.etouch.common.CommonPage;
import com.etouch.taf.util.CommonUtil;
import com.etouch.taf.util.SoftAssertor;
import com.etouch.taf.webui.selenium.WebPage;

/**
 * The Class ConnsMainPage.
 */
public class ConnsMainPage extends CommonPage {
	/**
	 * Instantiates a new conns main page.
	 *
	 * @param sbPageUrl
	 *            the sb page url
	 * @param webPage
	 *            the web page
	 */

	public ConnsMainPage(String sbPageUrl, WebPage webPage) {
		super(sbPageUrl, webPage);

		// webDriver = webPage.getDriver();
		CommonUtil.sop("webDriver in Conns Main Page " + webPage.getDriver());

		// startRecording();
		// if(TestBedManager.INSTANCE.getCurrentTestBed().getDevice().getName()
		// != null){

		loadPage();

		// }
	}

	public void contentVerification(String[][] contentData, String url) {
		try {
			for (int i = 0; i < contentData.length; i++) {
				System.out.println("Actual:  " + webPage.findObjectByxPath(contentData[i][0]).getText()
						+ "   Expected: " + contentData[i][1]);
				SoftAssertor.assertEquals(webPage.findObjectByxPath(contentData[i][0]).getText(), contentData[i][1],
						"expectedContent Failed to Match Actual");
			}
		} catch (Throwable e) {
			webPage.navigateToUrl(url);
			Assert.fail(e.getLocalizedMessage());
		}
	}

	/**
	 * Gets the page url.
	 *
	 * @return the page url
	 * @throws InterruptedException
	 */
	public String getPageUrl() throws InterruptedException {
		return webPage.getCurrentUrl();
	}

	public void getScreenShotForFailure(WebPage webPage, String methodName) {
		try {
			File scrFile = ((TakesScreenshot) webPage.getDriver()).getScreenshotAs(OutputType.FILE);
			String testEnv = System.getenv().get("Environment");
			File targetFile = new File("ConnsTestData/Output/" + testEnv + "/FailureImage/" + methodName + ".png");
			FileUtils.copyFile(scrFile, targetFile);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void catchBlock(Throwable e, WebDriver driver, String methodName) throws IOException {
		try {
			e.printStackTrace();
			File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
			String testEnv = System.getenv().get("Environment");
			File targetFile = new File("ConnsTestData/Output/" + testEnv + "/FailureImage/" + methodName + ".png");
			FileUtils.copyFile(scrFile, targetFile);
			Assert.fail("Test Case ::: " + methodName + " failed as :::" + e.getLocalizedMessage());
			driver.close();
		} catch (Exception et) {
			et.printStackTrace();
		}
	}
	public boolean isSorted(List<WebElement> list) {
		boolean sorted = true;
		for (int i = 1; i < list.size(); i++) {
			if (list.get(i - 1).getText().compareTo(list.get(i).getText()) > 0)
				sorted = false;
		}
		return sorted;
	}
}
