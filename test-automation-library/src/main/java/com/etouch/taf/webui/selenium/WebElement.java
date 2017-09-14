package com.etouch.taf.webui.selenium;

import io.appium.java_client.SwipeElementDirection;

import org.apache.commons.logging.Log;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Point;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.Select;
import org.testng.ITestContext;

import com.etouch.taf.core.TestBedManager;
import com.etouch.taf.core.exception.PageException;
import com.etouch.taf.util.BrowserInfoUtil;
import com.etouch.taf.util.CommonUtil;
import com.etouch.taf.util.LogUtil;
import com.etouch.taf.webui.ITafElement;

/**
 * The Class Element.
 */
public class WebElement implements ITafElement {

	/** The log. */
	private static Log log = LogUtil.getLog(WebElement.class);

	/** The driver. */
	protected WebDriver driver = null;

	/** The element. */
	protected org.openqa.selenium.WebElement webElement = null;

	String testBedName = null;

	/**
	 * Instantiates a new element.
	 * 
	 * @param webElement
	 *            the web element
	 */
	public WebElement(org.openqa.selenium.WebElement webElement, ITestContext context) {

		this.webElement = webElement;
		if (this.driver == null) {
			String tBName = context.getCurrentXmlTest().getAllParameters().get("testBedName");
			this.driver = (WebDriver) TestBedManager.INSTANCE.getCurrentTestBeds().get(tBName).getDriver();
			this.testBedName = tBName;
		}
	}

	/**
	 * Click on web element.
	 */
	public void click() {
		try {
			webElement.click();
			Thread.sleep(1000);
		} catch (StaleElementReferenceException e) {
			log.error("Exception in StaleElementReference message, " + e.toString());
		} catch (InterruptedException e) {
			log.error("Exception in thread sleep, message, " + e.toString());
		}
	}

	/**
	 * hovers on the element.
	 */
	public void hover() {
		System.out.println("In side Web Element class");
		if (new BrowserInfoUtil(testBedName).isSafari()) {
			String javaScript = "var evObj = document.createEvent('MouseEvents');"
					+ "evObj.initMouseEvent(\"mouseover\",true, false, window, 0, 0, 0, 0, 0, false, false, false, false, 0, null);" + "arguments[0].dispatchEvent(evObj);";
			JavascriptExecutor js = (JavascriptExecutor) driver;
			js.executeScript(javaScript, webElement);
			try {
				Thread.sleep(10000);
			} catch (InterruptedException e) {
				log.error("Exception in thread sleep, message, " + e.toString());
			}
			return;
		} else {
			Actions builder = new Actions(driver);
			builder.moveToElement(webElement).build().perform();
			try {
				Thread.sleep(10000);
			} catch (InterruptedException e) {
				log.debug(e);
			}
			return;
		}
	}

	/**
	 * Double click on web element.
	 */
	public void doubleClick() {
		Actions builder = new Actions(driver);
		Action mouseOverHome = builder.moveToElement(this.webElement).click().doubleClick(webElement).build();
		mouseOverHome.perform();
		try {
			Thread.sleep(100000);
		} catch (InterruptedException e) {
			log.error("Exception in thread sleep, message, " + e.toString());
		}
		return;
	}

	/**
	 * Clear the web element.
	 */
	public void clear() {
		try {
			webElement.clear();
			Thread.sleep(1000);
		} catch (StaleElementReferenceException e) {
			log.error("Exception in StaleElementReference message, " + e.toString());
		} catch (InterruptedException e) {
			log.error("Exception in thread sleep, message, " + e.toString());
		}
	}

	/**
	 * Enter the text in web element.
	 * 
	 * @param keysToSend
	 *            the CharSequence
	 */
	public void sendKeys(CharSequence... keysToSend) {
		try {
			webElement.sendKeys(keysToSend);
			Thread.sleep(1000);
		} catch (StaleElementReferenceException e) {
			log.error("Exception in StaleElementReference message, " + e.toString());
		} catch (InterruptedException e) {
			log.error("Exception in thread sleep, message, " + e.toString());
		}
	}

	/**
	 * Enter the text in web element.
	 * 
	 * @param txt
	 *            the txt
	 */
	public void sendKeys(String txt) {
		try {
			webElement.sendKeys(txt);
			Thread.sleep(1000);
		} catch (StaleElementReferenceException e) {
			log.error("Exception in StaleElementReference message, " + e.toString());
		} catch (InterruptedException e) {
			log.error("Exception in thread sleep, message, " + e.toString());
		}

	}

	/**
	 * Enter the text in web element.
	 */
	public void submit() {
		try {
			webElement.submit();
			;
			Thread.sleep(1000);
		} catch (StaleElementReferenceException e) {
			log.error("Exception in StaleElementReference message, " + e.toString());
		} catch (InterruptedException e) {
			log.error("Exception in thread sleep, message, " + e.toString());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.etouch.taf.webui.ITafElement#check()
	 */
	public void check() {
		if (!(isChecked()))
			webElement.click();

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.etouch.taf.webui.ITafElement#uncheck()
	 */
	public void uncheck() {
		if (isChecked()) {
			webElement.click();
		}
	}

	/**
	 * Drag and drop.
	 * 
	 * @param target
	 *            the target
	 */
	public void dragAndDrop(ITafElement target) {
		org.openqa.selenium.WebElement targetToDrop = target.getWebElement();

		Actions builder = new Actions(driver);

		Action dragAndDrop = builder.clickAndHold(webElement).moveToElement(targetToDrop).release(targetToDrop).build();

		dragAndDrop.perform();
	}

	/**
	 * Selects value from the list .
	 * 
	 * @param selection
	 *            the text for the element to be selected from the selection
	 *            list
	 * @return true if the selection is found
	 * @throws InterruptedException
	 * @throws PageException
	 *             the page exception
	 */

	public void selectDropDownList(String selection) throws InterruptedException {
		Select select = new Select(this.webElement);
		select.selectByVisibleText(selection);
		Thread.sleep(1000);
	}

	public void zoom() {
		CommonUtil.sop("Element: Inside zoom()");
	}

	/**
	 * Returns true if element is visible.
	 * 
	 * @return true if elements is visible.
	 */
	public boolean isElementVisible() {
		try {
			if (webElement == null) {
				return false;
			}
			if (!webElement.isEnabled()) {
				return false;
			}
			webElement.sendKeys(" ");
		} catch (org.openqa.selenium.ElementNotVisibleException ex) {
			log.error("Element No Visible - Error is " + ex.getMessage());
			return false;
		} catch (org.openqa.selenium.NoSuchElementException ex) {
			log.error("No Such Element - Error is " + ex.getMessage());
			return false;
		} catch (org.openqa.selenium.StaleElementReferenceException ex) {
			log.error("Stale Element Reference for element - Error is " + ex.getMessage());
			return false;
		}
		return true;
	}

	/**
	 * Return coordinates for given web element.
	 * 
	 * @return coordinates
	 */
	public Point getCoordinates() {
		return webElement.getLocation();
	}

	/**
	 * Returns size of the element.
	 * 
	 * @return size of web element.
	 */
	public Dimension getSize() {
		return webElement.getSize();
	}

	/**
	 * Returns element text.
	 * 
	 * @return element text
	 */
	public String getText() {
		return webElement.getText();
	}

	/**
	 * Return attribute value.
	 * 
	 * @param attrName
	 *            the attr name
	 * @return attribute value
	 */
	public String getAttribute(String attrName) {
		return webElement.getAttribute(attrName);
	}

	/**
	 * Returns the css property of a text.
	 * 
	 * @param property_name
	 *            the property_name
	 * @return property value
	 */
	public String getCssValue(String property_name) {
		return webElement.getCssValue(property_name);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.etouch.taf.webui.ITafElement#getWebElement()
	 */
	public org.openqa.selenium.WebElement getWebElement() {
		return webElement;
	}

	/**
	 * Returns true if element is displayed.
	 * 
	 * @return true if element displayed
	 */
	public boolean isDisplayed() {
		return webElement.isDisplayed();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.etouch.taf.webui.ITafElement#isEnabled()
	 */
	public boolean isEnabled() {
		return webElement.isEnabled();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.etouch.taf.webui.ITafElement#isSelected()
	 */
	public boolean isSelected() {
		return webElement.isSelected();
	}

	/**
	 * Click event.
	 */
	public void clickEvent() {
		String javaScript = "var evObj = document.createEvent('MouseEvents');"
				+ "evObj.initMouseEvent(\"click\",true, false, window, 0, 0, 0, 0, 0, false, false, false, false, 0, null);" + "arguments[0].dispatchEvent(evObj);";
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript(javaScript, webElement);
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			log.debug(e);
		}
	}

	/**
	 * Scroll to element.
	 * 
	 * @param element
	 *            the element
	 */
	public void scrollToElement() {
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("arguments[0].scrollIntoView();", webElement);
	}

	public void tap(int fingers, int duration) {

	}

	public void pinch() {

	}

	public boolean isChecked() {

		boolean isChecked = false;

		if ((webElement.getAttribute("checked").equalsIgnoreCase("true")) || (webElement.getAttribute("aria-checked").equalsIgnoreCase("true"))) {
			isChecked = true;
		}

		return new Boolean(isChecked);
	}

	public String getTestBedName() {
		return testBedName;
	}

	public void swipe(SwipeElementDirection direction, int duration) {

	}

	public void scroll(String direction) {

	}

	public void swipe(String direction) {

	}

	public void setWebElement(org.openqa.selenium.WebElement webElement) {
		this.webElement = webElement;
	}

	@Override
	public Point getLocation() {

		return null;
	}

}
