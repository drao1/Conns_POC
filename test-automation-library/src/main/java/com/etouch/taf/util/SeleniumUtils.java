package com.etouch.taf.util;

import java.util.concurrent.TimeUnit;

import org.apache.commons.logging.Log;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;

import com.etouch.taf.core.config.SeleniumConfig;
import com.etouch.taf.core.exception.PageException;
import com.google.common.base.Function;

public class SeleniumUtils {

	public static final long DEFAULT_TIMEOUT_WAIT_MSEC = SeleniumConfig.getInstance().getDefaultTimeOutWaitMsec();
	public static final long DEFAULT_POLLING_TIME_MSEC = SeleniumConfig.getInstance().getDefaultPolingTimeMsec();
	public static final int DEFAULT_POLLING_COUNT = SeleniumConfig.getInstance().getDefaultPollingCount();
	public static final long DEFAULT_IMPLICIT_WAIT = SeleniumConfig.getInstance().getDefaultImplicitWait();
	public static final int DEFAULT_MAX_ATTEMPT = SeleniumConfig.getInstance().getDefaultMaxAttempt();

	private static Log logger = LogUtil.getLog(SeleniumUtils.class);

	/**
	 * Find the first WebElement using the given method.
	 * 
	 * @param driver
	 * @param locator
	 * @return
	 * @throws PageException
	 */
	public static WebElement waitForElementFoundByElement(final WebDriver driver, final By locator) throws PageException {
		return waitForElementFoundByElement(driver, locator, DEFAULT_TIMEOUT_WAIT_MSEC, SeleniumUtils.DEFAULT_POLLING_TIME_MSEC);
	}

	/**
	 * Find the first WebElement using the given method.
	 * 
	 * @param driver
	 * @param locator
	 * @param waitTimeOutInMSec
	 * @param pollingTimeInMSec
	 * @param maxAttempts
	 * @return
	 * @throws PageException
	 */
	public static WebElement waitForElementFoundByElement(final WebDriver driver, final By locator, long timeOutInMSec, long pollingTimeInMSec) throws PageException {
		logger.debug("Wait waitForElementFoundByElement: " + locator.toString());

		WebElement webElement = null;

		try {
			Wait<WebDriver> wait = new FluentWait<WebDriver>(driver).withTimeout(timeOutInMSec, TimeUnit.MILLISECONDS).pollingEvery(pollingTimeInMSec, TimeUnit.MILLISECONDS)
					.ignoring(NoSuchElementException.class);

			webElement = wait.until(new Function<WebDriver, WebElement>() {
				public WebElement apply(WebDriver driver) {
					return driver.findElement(locator);
				}
			});

		} catch (StaleElementReferenceException ser) {

			logger.info("ERROR: Stale element. " + locator.toString());
			throw new PageException("ERROR: Stale element. " + locator.toString() + " " + ser.getMessage());

		} catch (TimeoutException te) {

			logger.info("ERROR: Timeout No such element. " + locator.toString() + " - " + timeOutInMSec);

			throw new PageException("ERROR: Timeout No such element. " + locator.toString() + " " + te.getMessage());
		} catch (Exception e) {

			logger.info(e.getMessage());
			throw new PageException("ERROR: No such element. " + locator.toString() + " " + e.getMessage());

		}

		return webElement;
	}

	/**
	 * Determine whether or not this element is selected or not. This operation
	 * only applies to input elements such as checkboxes, options in a select
	 * and radio buttons.
	 * 
	 * @param webElement
	 * @return
	 * @throws PageException
	 */
	public static Boolean isElementToSelect(final WebElement webElement) throws PageException {
		return isElementToSelect(webElement, SeleniumUtils.DEFAULT_TIMEOUT_WAIT_MSEC, SeleniumUtils.DEFAULT_POLLING_TIME_MSEC);
	}

	/**
	 * Is this element displayed or not? This method avoids the problem of
	 * having to parse an element's "style" attribute.
	 * 
	 * @param webElement
	 * @param waitTimeOutInMSec
	 * @param pollingTimeInMSec
	 * @return
	 * @throws PageException
	 */
	public static Boolean isElementToSelect(final WebElement webElement, long timeOutInMSec, long pollingTimeInMSec) throws PageException {
		logger.debug("Wait isElementToAppear");

		boolean found = false;
		try {

			Wait<WebElement> wait = new FluentWait<WebElement>(webElement).withTimeout(timeOutInMSec, TimeUnit.MILLISECONDS).pollingEvery(pollingTimeInMSec, TimeUnit.MILLISECONDS)
					.ignoring(NoSuchElementException.class);

			Boolean foo = wait.until(new Function<WebElement, Boolean>() {
				public Boolean apply(WebElement element) {
					return element.isSelected();
				}
			});
			found = foo;

		} catch (StaleElementReferenceException ser) {

			found = false;

			logger.info("ERROR: Stale element. ");
			throw new PageException("ERROR: Stale element. " + ser.getMessage());

		} catch (Exception e) {

			logger.info(e.getMessage());
			throw new PageException("ERROR: No such element. " + " " + e.getMessage());

		}

		return found;
	}

	/**
	 * Is this element displayed or not? This method avoids the problem of
	 * having to parse an element's "style" attribute.
	 * 
	 * @param webElement
	 * @return
	 * @throws PageException
	 */
	public static Boolean isElementToAppear(final WebElement webElement) throws PageException {
		return isElementToAppear(webElement, SeleniumUtils.DEFAULT_TIMEOUT_WAIT_MSEC, SeleniumUtils.DEFAULT_POLLING_TIME_MSEC);
	}

	/**
	 * Is this element displayed or not? This method avoids the problem of
	 * having to parse an element's "style" attribute.
	 * 
	 * @param webElement
	 * @param waitTimeOutInMSec
	 * @param pollingTimeInMSec
	 * @return
	 * @throws PageException
	 */
	public static Boolean isElementToAppear(final WebElement webElement, long timeOutInMSec, long pollingTimeInMSec) throws PageException {
		logger.debug("Wait isElementToAppear");
		int tries = 0;

		boolean found = false;

		logger.debug("timeOutInMSec: " + timeOutInMSec + "in milliseconds -  pollingTimeInMSec: " + pollingTimeInMSec + " milliseconds");
		try {

			logger.info("Searching for element. Try number " + tries);

			Wait<WebElement> wait = new FluentWait<WebElement>(webElement).withTimeout(timeOutInMSec, TimeUnit.MILLISECONDS).pollingEvery(pollingTimeInMSec, TimeUnit.MILLISECONDS)
					.ignoring(NoSuchElementException.class);

			Boolean foo = wait.until(new Function<WebElement, Boolean>() {
				public Boolean apply(WebElement element) {
					return element.isDisplayed();
				}
			});
			found = foo;

		} catch (StaleElementReferenceException ser) {

			found = false;
			logger.info("ERROR: Stale element. ");
			throw new PageException("ERROR: Stale element. " + ser.getMessage());

		} catch (Exception e) {

			logger.info(e.getMessage());
			throw new PageException("ERROR: No such element. " + " " + e.getMessage());

		}

		return found;
	}

	/**
	 * Is the element currently enabled or not? This will generally return true
	 * for everything but disabled input elements.
	 * 
	 * @param webElement
	 * @return
	 * @throws PageException
	 */
	public static Boolean isElementToEnable(final WebElement webElement) throws PageException {
		return isElementToEnable(webElement, SeleniumUtils.DEFAULT_TIMEOUT_WAIT_MSEC, SeleniumUtils.DEFAULT_POLLING_TIME_MSEC);
	}

	/**
	 * Is the element currently enabled or not? This will generally return true
	 * for everything but disabled input elements.
	 * 
	 * @param webElement
	 * @param waitTimeOutInMSec
	 * @param pollingTimeInMSec
	 * @return
	 * @throws PageException
	 */
	public static Boolean isElementToEnable(final WebElement webElement, long timeOutInMSec, long pollingTimeInMSec) throws PageException {
		logger.debug("##########Wait isElementToEnable: " + webElement.getText());
		boolean found = false;

		logger.debug("timeOutInMSec: " + timeOutInMSec + "in milliseconds -  pollingTimeInMSec: " + pollingTimeInMSec + " milliseconds");
		try {

			Wait<WebElement> wait = new FluentWait<WebElement>(webElement).withTimeout(timeOutInMSec, TimeUnit.MILLISECONDS).pollingEvery(pollingTimeInMSec, TimeUnit.MILLISECONDS)
					.ignoring(NoSuchElementException.class);
			Boolean foo = wait.until(new Function<WebElement, Boolean>() {
				public Boolean apply(WebElement element) {
					return element != null && element.isEnabled();
				}
			});
			found = foo;

		} catch (StaleElementReferenceException ser) {
			found = false;
			throw new PageException("ERROR: Stale element. " + ser.getMessage());

		} catch (Exception e) {

			logger.info(e.getMessage());
			throw new PageException("ERROR: No such element. " + " " + e.getMessage());

		}

		return found;
	}

	/**
	 * An expectation for checking that an element is present on the DOM of a
	 * page.
	 * 
	 * @param driver
	 * @param locator
	 * @param waitTimeOutInMSec
	 * @param pollingTimeInMSec
	 * @return
	 * @throws PageException
	 */
	public static WebElement checkPresenceOfElementLocated(final WebDriver driver, final By locator, long timeOutInMSec, long pollingTimeInMSec) throws PageException {
		logger.debug("Wait checkPresenceOfElementLocated: " + locator.toString());

		WebElement webElement = null;

		logger.debug("timeOutInMSec: " + timeOutInMSec + "in milliseconds -  pollingTimeInMSec: " + pollingTimeInMSec + " milliseconds");
		try {
			Wait<WebDriver> wait = new FluentWait<WebDriver>(driver).withTimeout(timeOutInMSec, TimeUnit.MILLISECONDS).pollingEvery(pollingTimeInMSec, TimeUnit.MILLISECONDS)
					.ignoring(NoSuchElementException.class);
			webElement = wait.until(ExpectedConditions.presenceOfElementLocated(locator));

		} catch (StaleElementReferenceException ser) {

			logger.info("ERROR: Stale element. " + locator.toString());
			throw new PageException("ERROR: Stale element. " + locator.toString() + " " + ser.getMessage());

		} catch (Exception e) {

			logger.info(e.getMessage());
			throw new PageException("ERROR: No such element. " + locator.toString() + " " + e.getMessage());

		}

		return webElement;
	}

	/**
	 * An expectation for checking that an element is present on the DOM of a
	 * page.
	 * 
	 * @param driver
	 * @param locator
	 * @return
	 * @throws PageException
	 */
	public static WebElement checkPresenceOfElementLocated(final WebDriver driver, final By locator) throws PageException {
		return checkPresenceOfElementLocated(driver, locator, SeleniumUtils.DEFAULT_TIMEOUT_WAIT_MSEC, SeleniumUtils.DEFAULT_POLLING_TIME_MSEC);
	}

	/**
	 * An expectation for checking an element is visible and enabled such that
	 * you can click it.
	 * 
	 * @param driver
	 * @param locator
	 * @return
	 * @throws PageException
	 */
	public static WebElement checkElementToBeClickable(final WebDriver driver, final By locator) throws PageException {
		return checkElementToBeClickable(driver, locator, SeleniumUtils.DEFAULT_TIMEOUT_WAIT_MSEC, SeleniumUtils.DEFAULT_POLLING_TIME_MSEC);
	}

	/**
	 * An expectation for checking an element is visible and enabled such that
	 * you can click it.
	 * 
	 * @param driver
	 * @param locator
	 * @param waitTimeOutInSec
	 * @param pollingTimeInSec
	 * @return
	 * @throws PageException
	 */
	public static WebElement checkElementToBeClickable(final WebDriver driver, final By locator, long timeOutInMSec, long pollingTimeInMSec) throws PageException {
		logger.debug("Wait checkElementToBeClickable: " + locator.toString());

		boolean found = false;
		WebElement webElement = null;

		logger.debug("timeOutInMSec: " + timeOutInMSec + "in milliseconds -  pollingTimeInMSec: " + pollingTimeInMSec + " milliseconds");
		try {

			Wait<WebDriver> wait = new FluentWait<WebDriver>(driver).withTimeout(timeOutInMSec, TimeUnit.MILLISECONDS).pollingEvery(pollingTimeInMSec, TimeUnit.MILLISECONDS)
					.ignoring(NoSuchElementException.class);

			webElement = wait.until(ExpectedConditions.elementToBeClickable(locator));

		} catch (StaleElementReferenceException ser) {

			found = false;
			logger.info("ERROR: Stale element. " + locator.toString());
			throw new PageException("ERROR: Stale element. " + locator.toString() + " " + ser.getMessage());

		} catch (Exception e) {

			logger.info(e.getMessage());
			throw new PageException("ERROR: No such element. " + locator.toString() + " " + e.getMessage());

		}

		return webElement;
	}

	/**
	 * An expectation for checking that an element is present on the DOM of a
	 * page and visible.
	 * 
	 * @param driver
	 * @param locator
	 * @return
	 * @throws PageException
	 */
	public static WebElement checkVisibilityOfElementLocated(final WebDriver driver, final By locator) throws PageException {
		return checkVisibilityOfElementLocated(driver, locator, SeleniumUtils.DEFAULT_TIMEOUT_WAIT_MSEC, SeleniumUtils.DEFAULT_POLLING_TIME_MSEC);
	}

	/**
	 * An expectation for checking that an element is present on the DOM of a
	 * page and visible.
	 * 
	 * @param driver
	 * @param locator
	 * @param waitTimeOutInSec
	 * @param pollingTimeInSec
	 * @return
	 * @throws PageException
	 */
	public static WebElement checkVisibilityOfElementLocated(final WebDriver driver, final By locator, long timeOutInMSec, long pollingTimeInMSec) throws PageException {
		logger.debug("Wait checkVisibilityOfElementLocated: " + locator.toString());

		WebElement webElement = null;

		logger.debug("timeOutInMSec: " + timeOutInMSec + "in milliseconds -  pollingTimeInMSec: " + pollingTimeInMSec + " milliseconds");
		try {

			Wait<WebDriver> wait = new FluentWait<WebDriver>(driver).withTimeout(timeOutInMSec, TimeUnit.MILLISECONDS).pollingEvery(pollingTimeInMSec, TimeUnit.MILLISECONDS)
					.ignoring(NoSuchElementException.class);

			webElement = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));

		} catch (StaleElementReferenceException ser) {
			// The element has been deleted entirely.
			// The element is no longer attached to the DOM.

			logger.info("ERROR: Stale element. " + locator.toString());
			throw new PageException("ERROR: Stale element. " + locator.toString() + " " + ser.getMessage());

		} catch (TimeoutException te) {

			logger.info("ERROR: Timeout No such element. " + locator.toString() + " - " + timeOutInMSec);
			throw new PageException("ERROR: Timeout No such element. " + locator.toString() + " " + te.getMessage());

		} catch (Exception e) {

			logger.info(e.getMessage());
			throw new PageException("ERROR: No such element. " + locator.toString() + " " + e.getMessage());

		}

		return webElement;
	}

	/**
	 * 
	 * @param driver
	 * @param implicitwait_ms
	 */
	public static void delayPageLoad(final WebDriver driver) {
		delayPageLoad(driver, DEFAULT_IMPLICIT_WAIT);
	}

	/**
	 * 
	 * @param driver
	 * @param implicitwait_ms
	 */
	public static void delayPageLoad(final WebDriver driver, long implicitwait_ms) {
		driver.manage().timeouts().implicitlyWait(implicitwait_ms, TimeUnit.MILLISECONDS);
	}

}