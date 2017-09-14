package com.etouch.taf.webui.selenium;

import org.apache.commons.logging.Log;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.events.AbstractWebDriverEventListener;
import org.openqa.selenium.support.events.WebDriverEventListener;

import com.etouch.taf.core.config.SeleniumConfig;
import com.etouch.taf.util.LogUtil;

public class WebEventListener extends AbstractWebDriverEventListener implements WebDriverEventListener {

	private static Log log = LogUtil.getLog(WebEventListener.class);

	@Override
	public void afterNavigateTo(String url, WebDriver driver) {
		super.afterNavigateTo(url, driver);
		log.debug("@@@@@@@@@@@@@@@@@@@@ AfterNavigate to was invoked@@@@@@@@@@@@@@@@@@@");
	}

	@Override
	public void beforeNavigateTo(String url, WebDriver driver) {
		super.beforeNavigateTo(url, driver);
		pageloadtime(driver);
		log.debug("@@@@@@@@@@@@@@@@@@@@  beforeNavigateTo to was invoked@@@@@@@@@@@@@@@@@@@");
	}

	@Override
	public void beforeNavigateBack(WebDriver driver) {
		super.beforeNavigateBack(driver);
	}

	@Override
	public void afterNavigateBack(WebDriver driver) {
		super.afterNavigateBack(driver);
		pageloadtime(driver);
	}

	@Override
	public void beforeNavigateForward(WebDriver driver) {
		super.beforeNavigateBack(driver);
		pageloadtime(driver);
	}

	@Override
	public void afterNavigateForward(WebDriver driver) {
		super.beforeNavigateBack(driver);
	}

	@Override
	public void beforeFindBy(By by, WebElement element, WebDriver driver) {
		super.beforeFindBy(by, element, driver);
	}

	@Override
	public void afterFindBy(By by, WebElement element, WebDriver driver) {
		super.afterFindBy(by, element, driver);

	}

	@Override
	public void beforeClickOn(WebElement element, WebDriver driver) {
		super.beforeClickOn(element, driver);
	}

	@Override
	public void afterClickOn(WebElement element, WebDriver driver) {
		super.afterClickOn(element, driver);

	}

	/*@Override
	public void beforeChangeValueOf(WebElement element, WebDriver driver) {
		super.beforeChangeValueOf(element, driver);
	}

	@Override
	public void afterChangeValueOf(WebElement element, WebDriver driver) {
		super.afterChangeValueOf(element, driver);
	}
*/
	@Override
	public void beforeScript(String script, WebDriver driver) {
		super.beforeScript(script, driver);
	}

	@Override
	public void afterScript(String script, WebDriver driver) {
		super.afterScript(script, driver);
	}

	@Override
	public void onException(Throwable throwable, WebDriver driver) {
		super.onException(throwable, driver);
	}


	public void pageloadtime(WebDriver driver) {

		String currentURL = driver.getCurrentUrl();

		log.debug("Loading current URL ==>" + currentURL);

		JavascriptExecutor js = (JavascriptExecutor) driver;

		// Get the Load Event End
		long loadEventEnd = (Long) js.executeScript("return window.performance.timing.loadEventEnd;");

		// Get the Navigation Event Start
		long navigationStart = (Long) js.executeScript("return window.performance.timing.navigationStart;");

		// Difference between Load Event End and Navigation Event Start is Page
		// Load Time
		log.debug("navigationStart==>" + navigationStart + " :: loadEventEnd ==>" + loadEventEnd + " = Page Load Time is " + (loadEventEnd - navigationStart) + " milli-seconds.");

		long domCompleteTime = (Long) js.executeScript("return window.performance.timing.domComplete;");

		log.debug("Page load time ==>" + (domCompleteTime - navigationStart) + " milli-seconds");

		long loadTime = loadEventEnd - navigationStart;
		if (loadTime < 1) {
			loadTime = 1;
		}
		SeleniumConfig.setDefaultTimeOutWaitMsec(loadTime);
	}
}
