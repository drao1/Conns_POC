package com.etouch.taf.webui;

import org.openqa.selenium.WebElement;
import org.testng.ITestContext;


/**
 * A factory for creating MobileElement objects.
 */
public class MobileElementFactory implements ITafElementFactory {

	/* (non-Javadoc)
	 * @see com.etouch.taf.webui.ITafElementFactory#createElement(org.openqa.selenium.WebElement)
	 */
	public ITafElement createElement(WebElement webElement, ITestContext context) {
		
		return new com.etouch.taf.webui.selenium.MobileElement(webElement, context);
	}

}
