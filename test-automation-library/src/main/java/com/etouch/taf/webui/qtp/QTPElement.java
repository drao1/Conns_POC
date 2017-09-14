package com.etouch.taf.webui.qtp;

import io.appium.java_client.SwipeElementDirection;

import org.apache.commons.logging.Log;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Point;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.testng.ITestContext;

import com.etouch.taf.core.TestBed;
import com.etouch.taf.core.TestBedManager;
import com.etouch.taf.core.exception.PageException;
import com.etouch.taf.util.BrowserInfoUtil;
import com.etouch.taf.util.LogUtil;
import com.etouch.taf.webui.ITafElement;

/**
 * The Class QTPElement.
 */
public class QTPElement implements ITafElement {

	/** The log. */
	private static Log log = LogUtil.getLog(WebElement.class);

	/** The element. */
	protected WebElement element = null;

	/** The driver. */
	protected WebDriver driver = null;

	String testBedName = null;

	/**
	 * Instantiates a new QTP element.
	 * 
	 * @param element
	 *            the element
	 */
	public QTPElement(WebElement element, ITestContext context) {
		this.element = element;
		if (this.driver == null) {
			String tbName = context.getCurrentXmlTest().getAllParameters().get("testBedName");

			TestBed testBed = TestBedManager.INSTANCE.getCurrentTestBeds().get(tbName);
			this.driver = (WebDriver) testBed.getDriver();
			this.testBedName = tbName;
		}
	}

	/**
	 * Click on web element.
	 */
	public void click() {
		try {
			this.element.click();
			Thread.sleep(1000);
		} catch (StaleElementReferenceException e) {
			log.error("Exception in StaleElementReference message, " + e.toString());
		} catch (InterruptedException e) {
			log.error("Exception in thread sleep, message, " + e.toString());
		}
	}

	/**
	 * Clear the web element.
	 */
	public void clear() {
		try {
			this.element.clear();
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
			this.element.sendKeys(txt);
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
	 * @param keys
	 *            the CharSequence
	 */
	public void sendKeys(CharSequence... keys) {
		try {
			this.element.sendKeys(keys);
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
		if (new BrowserInfoUtil(testBedName).isSafari()) {
			String javaScript = "var evObj = document.createEvent('MouseEvents');"
					+ "evObj.initMouseEvent(\"mouseover\",true, false, window, 0, 0, 0, 0, 0, false, false, false, false, 0, null);" + "arguments[0].dispatchEvent(evObj);";
			JavascriptExecutor js = (JavascriptExecutor) driver;
			js.executeScript(javaScript, this.element);
			try {
				Thread.sleep(10000);
			} catch (InterruptedException e) {
				log.error("Exception in thread sleep, message, " + e.toString());
			}
			return;
		} else {
			Actions builder = new Actions(driver);
			builder.moveToElement(this.element).build().perform();
			try {
				Thread.sleep(10000);
			} catch (InterruptedException e) {
				log.debug(e);
			}
			return;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.etouch.taf.webui.ITafElement#zoom()
	 */
	public void zoom() {

	}

	public void doubleClick() {

	}

	public boolean isElementVisible() {

		return false;
	}

	public Point getCoordinates() {

		return null;
	}

	public Dimension getSize() {

		return null;
	}

	public String getText() {

		return null;
	}

	public String getAttribute(String attrName) {

		return null;
	}

	public boolean isDisplayed() {

		return false;
	}

	public String getCssValue(String property_name) {

		return null;
	}

	public void clickEvent() {

	}

	public void submit() {

	}

	public boolean isEnabled() {

		return false;
	}

	public boolean isSelected() {

		return false;
	}

	public void check() {

	}

	public void uncheck() {

	}

	public void selectDropDownList(String selection) throws PageException {

	}

	public WebElement getWebElement() {

		return null;
	}

	public void tap(int fingers, int duration) {

	}

	public void pinch() {

	}

	public void dragAndDrop(ITafElement target) {

	}

	public boolean isChecked() {

		return false;
	}

	public void swipe(SwipeElementDirection direction, int duration) {

	}

	public void scroll(String direction) {

	}

	public void swipe(String direction) {

	}

	@Override
	public Point getLocation() {

		return null;
	}

	@Override
	public void scrollToElement() {

	}

}
