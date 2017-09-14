package com.etouch.taf.util;

import org.apache.commons.logging.Log;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;

public class JavaScriptUtil {
	

	private static Log log = LogUtil.getLog(JavaScriptUtil.class);

	public static <T extends Object> T executeJavaScript(WebDriver driver, String javaScript) {
		T result = null;

		try {
			JavascriptExecutor js = (JavascriptExecutor) driver;
			result = (T) js.executeScript(javaScript);

			Thread.sleep(10000);
		} catch (InterruptedException e) {
			log.debug(e);
		}

		return result;
	}

}
