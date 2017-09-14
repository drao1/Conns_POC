package com.etouch.taf.mobile.appium;

import java.util.HashMap;

import org.apache.commons.logging.Log;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.RemoteWebElement;

import com.etouch.taf.core.config.ScreenOrientation;
import com.etouch.taf.core.driver.IDriver;
import com.etouch.taf.util.LogUtil;
import com.etouch.taf.webui.selenium.WebPage;

/**
 * This class finds and renders page objects , drivers.
 * 
 * @author eTouch Systems Corporation
 * @version 1.0
 * 
 */
public class PageView extends WebPage {

	/** The log. */
	private static Log log = LogUtil.getLog(PageView.class);

	/**
	 * Tap.
	 * 
	 * @param driver
	 *            the driver
	 * @param element
	 *            the element
	 * @param duration
	 *            the duration
	 */
	public void tap(IDriver driver, WebElement element, Double duration) {
		JavascriptExecutor js = (JavascriptExecutor) driver;
		HashMap<String, Double> tapObject = new HashMap<String, Double>();
		tapObject.put("x", (double) element.getLocation().getX());
		tapObject.put("y", (double) element.getLocation().getY());
		tapObject.put("duration", duration);
		js.executeScript("mobile: tap", tapObject);
	}

	/**
	 * Tap element by id.
	 * 
	 * @param driver
	 *            the driver
	 * @param element
	 *            the element
	 */
	public void tapElementById(IDriver driver, RemoteWebElement element) {
		JavascriptExecutor js = (JavascriptExecutor) driver;
		HashMap<String, Object> tapObject = new HashMap<String, Object>();
		// in pixels from left
		tapObject.put("x", 150);
		// in pixels from top
		tapObject.put("y", 30);
		// the id of the element we want to tap
		tapObject.put("element", ((RemoteWebElement) element).getId());
		js.executeScript("mobile: tap", tapObject);
	}

	/**
	 * Swipe.
	 * 
	 * @param startX
	 *            the start x
	 * @param startY
	 *            the start y
	 * @param endX
	 *            the end x
	 * @param endY
	 *            the end y
	 * @param duration
	 *            the duration
	 */
	public void swipe(Double startX, Double startY, Double endX, Double endY, Double duration) {
		JavascriptExecutor js = (JavascriptExecutor) driver;
		HashMap<String, Double> swipeObject = new HashMap<String, Double>();
		swipeObject.put("startX", startX);
		swipeObject.put("startY", startY);
		swipeObject.put("endX", endX);
		swipeObject.put("endY", endY);
		swipeObject.put("duration", duration);
		js.executeScript("mobile: swipe", swipeObject);
	}

	/**
	 * Flick.
	 */
	public void flick() {
		JavascriptExecutor js = (JavascriptExecutor) driver;
		HashMap<String, Object> flickObject = new HashMap<String, Object>();
		flickObject.put("endX", 0);
		flickObject.put("endY", 0);
		flickObject.put("touchCount", 2);
		js.executeScript("mobile: flick", flickObject);
	}

	/**
	 * Slider.
	 * 
	 * @param element
	 *            the element
	 * @param slideValue
	 *            the slide value
	 */
	public void slider(WebElement element, float slideValue) {
		if ((slideValue >= 0.0) && (slideValue <= 1)) {
			element.sendKeys(String.valueOf(slideValue));
		}
	}

	/**
	 * Sets the orientation.
	 * 
	 * @param driver
	 *            the driver
	 * @param orientation
	 *            the orientation
	 */
	public void setOrientation(RemoteWebDriver driver, ScreenOrientation orientation) {

	}

}
