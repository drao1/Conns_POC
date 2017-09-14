package com.etouch.taf.core.config;

import java.util.concurrent.TimeUnit;

import org.apache.commons.logging.Log;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;

import com.etouch.taf.util.LogUtil;
import com.etouch.taf.webui.ITafElement;
import com.etouch.taf.webui.selenium.WebPage;
import com.google.common.base.Function;

/**
 * @author etouch
 *
 */
public class CustomFluentWaitConfig {

	private static Log log = LogUtil.getLog(CustomFluentWaitConfig.class);

	public ITafElement waitTillElementFound(ITafElement iTafElement, WebPage webPage) {

		int maxTimeCheck = 0;

		while (isWaitRequired(iTafElement) && maxTimeCheck < 5) {

			log.debug("Element not yet either visible or displayed or enabled... waiting...");

			wait(webPage.getDriver());

			maxTimeCheck++;
		}

		return iTafElement;
	}

	private boolean isWaitRequired(ITafElement iTafElement) {

		boolean isWaitRequried = true;

		if (iTafElement.isDisplayed() && iTafElement.isEnabled()) {
			log.debug("Element is Displayed and Enabled... wait not required!");
			isWaitRequried = false;
		} else if (iTafElement.isElementVisible() && iTafElement.isEnabled()) {
			log.debug("Element is Visible and Enabled... wait not required!");
			isWaitRequried = false;
		}

		return isWaitRequried;
	}

	/**
	 * Wait untill the page load completes
	 * 
	 * @return
	 * @throws InterruptedException
	 */
	public boolean verifyPageLoadCompletes(WebDriver driver) {

		while (!isPageLoadComplete(driver)) {
			log.debug("Complete page not yet loaded... waiting...");
			wait(driver);
		}

		return true;
	}

	/**
	 * @param driver
	 */
	private void wait(WebDriver driver) {
		try {
			Double maxWaitTime;

			if (driver.getClass().isInstance(SafariDriver.class))
				maxWaitTime = (double) 10000;
			else
				maxWaitTime = getMaxWaitTime(driver);

			Double pollingTime = maxWaitTime / 20; // 20 Times event will be
													// polling will happen
			Wait<WebDriver> wait = new FluentWait<WebDriver>(driver)
					.withTimeout(maxWaitTime.longValue(), TimeUnit.MILLISECONDS)
					.pollingEvery(pollingTime.longValue(), TimeUnit.MILLISECONDS)
					.ignoring(NoSuchElementException.class, TimeoutException.class);

			wait.until(new Function<WebDriver, Boolean>()

			{
				public Boolean apply(WebDriver driver) {
					return isPageLoadComplete(driver);
				}
			});
		} catch (TimeoutException timeoutException) {

			log.error(timeoutException);
			return;
		}
	}

	/**
	 * Calculate Max Wait Time 1 - If Page load Completed -- maxWaitTime =
	 * loadEventEnd 2 - Else If Page response end completed -- maxWaitTime =
	 * responseEndTime 3 - Else If Page response start completed -- maxWaitTime
	 * = responseStartTime 4 - Else If Page request start complete --
	 * maxWaitTime = requestStartTime
	 * 
	 * @param driver
	 * @return Wait time
	 */
	private double getMaxWaitTime(WebDriver driver) {

		Double pageLoadTime = 0.0;

		JavascriptExecutor js = (JavascriptExecutor) driver;

		// Get the Navigation Event Start
		Long navigationStart = (Long) js.executeScript("return window.performance.timing.navigationStart;");

		// Get the Load Event End
		Long loadEventEnd = (Long) js.executeScript("return window.performance.timing.loadEventEnd;");

		// Get the Response Completion time
		Long responseEndTime = (Long) js.executeScript("return window.performance.timing.responseEnd;");

		// Get the Response Start time
		Long responseStartTime = (Long) js.executeScript("return window.performance.timing.responseStart;");

		// Get the Request Start time
		Long requestStartTime = (Long) js.executeScript("return window.performance.timing.requestStart;");

		Long currentPageLoadTime = 0L;

		if (loadEventEnd > 0) {
			currentPageLoadTime = loadEventEnd;
			log.debug("Setting currentPageLoadTime == loadEventEnd ==>" + loadEventEnd);
		} else if (responseEndTime > 0) {
			currentPageLoadTime = responseEndTime;
			log.debug("Setting currentPageLoadTime == responseEndTime ==>" + responseEndTime);
		} else if (responseStartTime > 0) {
			currentPageLoadTime = responseStartTime;
			log.debug("Setting currentPageLoadTime == responseStartTime ==>" + responseStartTime);
		} else if (requestStartTime > 0) {
			currentPageLoadTime = requestStartTime;
			log.debug("Setting currentPageLoadTime == requestStartTime ==>" + requestStartTime);
		}

		pageLoadTime = currentPageLoadTime.doubleValue() - navigationStart.doubleValue();

		log.debug("[" + this.hashCode() + "] Page Load Time is " + (pageLoadTime / 1000) + " seconds.");

		return pageLoadTime;
	}

	/**
	 * 
	 * @param driver
	 * @return boolean
	 */
	private boolean isPageLoadComplete(WebDriver driver) {

		if (driver.getClass().isInstance(SafariDriver.class)) {

			log.info("Safari driver Object");
			try {
				Thread.sleep(9000);
			} catch (InterruptedException e) {
				
				log.error(e);
			}

			return true;
		} else {

			JavascriptExecutor js = (JavascriptExecutor) driver;
			Long loadEventEnd;

			try {
				// Get the Load Event End
				loadEventEnd = (Long) js.executeScript("return window.performance.timing.loadEventEnd;");
			} catch (Exception ex) {
				loadEventEnd = (long) 10000;
			}

			Double completePageLoadTime = loadEventEnd.doubleValue();

			return completePageLoadTime > 0 && driver.getCurrentUrl().contains("http");

		}

	}

}
