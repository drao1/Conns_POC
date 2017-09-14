package com.etouch.taf.webui.selenium.webelement;

import org.apache.commons.logging.Log;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.interactions.HasInputDevices;
import org.openqa.selenium.interactions.Mouse;
import org.openqa.selenium.internal.Locatable;
import org.openqa.selenium.support.Color;

import com.etouch.taf.util.BrowserInfoUtil;
import com.etouch.taf.util.CommonUtil;
import com.etouch.taf.util.LogUtil;

/**
 * Renders Text PageObject. .
 * 
 * @author eTouch Systems Corporation
 * @version 1.0
 * 
 */
public class Text extends Element {

	/** The log. */
	static Log log = LogUtil.getLog(Text.class);

	/** The action. */
	Actions action = new Actions(driver);

	/**
	 * Instantiates a new text.
	 * 
	 * @param webElement
	 *            the web element
	 */
	public Text(WebElement webElement) {
		super(webElement);
	}

	/**
	 * Text hover.
	 */
	public void textHover() {
		Mouse mouse = ((HasInputDevices) driver).getMouse();
		mouse.mouseMove(((Locatable) this.webElement).getCoordinates());
	}

	/**
	 * Text color.
	 * 
	 * @return the string
	 */
	public String textColor() {
		if (new BrowserInfoUtil(testBedName).isSafari()) {
			action.moveToElement(webElement);
			String mouseOverScript = "if(document.createEvent){var evObj = document.createEvent('MouseEvents');evObj.initEvent('mouseover', true, false); arguments[0].dispatchEvent(evObj);} else if(document.createEventObject) { arguments[0].fireEvent('onmouseover');}";
			JavascriptExecutor js = (JavascriptExecutor) driver;
			js.executeScript(mouseOverScript, webElement);
			System.out.println("In hover");
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {

				log.debug(e);
			}
			CommonUtil.sop("OUT hover");
		} else {
			textHover();
		}
		return Color.fromString(webElement.getCssValue("color")).asHex();
	}

	/**
	 * Text style.
	 * 
	 * @return the string
	 */
	public String textStyle() {
		return null;
	}

	/**
	 * Text font.
	 * 
	 * @return the string
	 */
	public String textFont() {
		return null;
	}

	/**
	 * Text title.
	 * 
	 * @return the string
	 */
	public String textTitle() {
		textHover();
		return webElement.getAttribute("title");
	}

	/**
	 * Border color.
	 * 
	 * @return the string
	 */
	public String borderColor() {
		textHover();
		return Color.fromString(webElement.getCssValue("color")).asHex();
	}

}
