package com.etouch.taf.webui;

import io.appium.java_client.SwipeElementDirection;

import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebElement;

import com.etouch.taf.core.exception.PageException;


/**
 * The Interface ITafElement.
 */
public interface ITafElement {
	
	/** The web test. */
	int WEB_TEST = 0;
	
	/** The mob test. */
	int MOB_TEST = 1;
	
	Object driver=null;

	/**
	 * Click.
	 */
	public void click();
	
	/**
	 * Clear.
	 */
	public void clear();
	
	/**
	 * Send keys.
	 *
	 * @param txt the txt
	 */
	public void sendKeys(String txt);
	
	public void sendKeys(CharSequence...KeyChar);
	
	/**
	 * Hover.
	 */
	public void hover();
	
	/**
	 * Zoom.
	 */
	public void zoom();
	
	
	/**
	 * Double click.
	 */
	public void doubleClick();
	
	public void submit();
	
	/**
	 * Checks if is element visible.
	 *
	 * @return true, if is element visible
	 */
	public boolean isElementVisible();
	
	/**
	 * Gets the coordinates.
	 *
	 * @return the coordinates
	 */
	public Point getCoordinates();
	
	/**
	 * Gets the size.
	 *
	 * @return the size
	 */
	public Dimension getSize();
	
	/**
	 * Gets the text.
	 *
	 * @return the text
	 */
	public String getText();
	
	/**
	 * Gets the attribute.
	 *
	 * @param attrName the attr name
	 * @return the attribute
	 */
	public String getAttribute(String attrName);
	
	public WebElement getWebElement();
	
	/**
	 * Checks if is displayed.
	 *
	 * @return true, if is displayed
	 */
	public boolean isDisplayed();	
	
	public boolean isEnabled();
	
	public boolean isSelected();
	
	public void check();
	
	public void uncheck();
	
	public void swipe(SwipeElementDirection direction, int duration);
	
	public void selectDropDownList(String selection) throws PageException, InterruptedException;
	
	public void dragAndDrop(ITafElement target);
	
	/**
	 * Gets the css value.
	 *
	 * @param property_name the property_name
	 * @return the css value
	 */
	public String getCssValue(String property_name);
	
	/**
	 * Click event.
	 */
	public void clickEvent();	
	
	public void tap(int fingers, int duration);
	
	public void pinch();

	public boolean isChecked();

	public void scroll(String direction);
	
	public void swipe(String direction);
	
	public Point getLocation();
	
	public void scrollToElement();

}
