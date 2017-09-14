package com.etouch.taf.kd.validator;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.commons.logging.Log;

import com.etouch.taf.core.exception.ImageMatchException;
import com.etouch.taf.kd.config.ResponseStatusCode;
import com.etouch.taf.util.ImageCompare;
import com.etouch.taf.util.LogUtil;
import com.etouch.taf.util.SoftAssertor;

/**
 * Assert Action custom implementation
 * 
 * Every new assert actions required needs to have its implementation defined in
 * this class
 * 
 * @author eTouch System Corporation
 * 
 */
public class AssertActionElement {

	private static Log log = LogUtil.getLog(AssertActionElement.class);

	/**
	 * 
	 * Verify is the Element text 'has the expected value'.
	 * 
	 * Works as actualValue == expectedValue
	 * 
	 * @param actualValue
	 * @param expectedValue
	 * @throws Exception
	 */
	public void hasValue(String actualValue, Object expectedValue) {

		log.debug(" actualValue ==>" + actualValue + " && expectedValue ==>" + (String) expectedValue);
		SoftAssertor.assertEquals(actualValue, String.valueOf(expectedValue));
	}

	/**
	 * Verify if the Element text 'contains the expected value'
	 * 
	 * Works as actualValue.contains(expectedValue)
	 * 
	 * @param actualValue
	 * @param expectedValue
	 * @throws Exception
	 */
	public void contains(String actualValue, Object expectedValue) {

		String textSearch = (String) expectedValue;

		boolean isTextPresent = actualValue.contains(textSearch);

		log.debug(" complete text ==>" + actualValue + " && looking for ==>" + (String) expectedValue
				+ " :: isTextPresent ==>" + isTextPresent);
		SoftAssertor.assertTrue(isTextPresent);

	}

	/**
	 * Verify if actual and expected image URLs are same
	 * 
	 * @param actualValueURL
	 * @param expectedValueURL
	 * @throws IOException
	 * @throws ImageMatchException
	 */
	public void hasImage(String actualValueURL, String expectedValueURL) {

		URL actualImageURL = null;
		URL expectedImageURL = null;

		try {
			actualImageURL = new URL(actualValueURL.trim());
			expectedImageURL = new URL(expectedValueURL.trim());
			log.debug(" actualImageURL ==>" + actualImageURL + " && expectedImageURL ==>" + expectedImageURL);
			ImageCompare imageCompare = new ImageCompare(actualImageURL, expectedImageURL);
			imageCompare.compare();

			SoftAssertor.assertImageMatch(false, actualValueURL, expectedValueURL);

		} catch (MalformedURLException e) {
			log.error("MalformedURLException", e);

		} catch (IOException e) {
			log.error("IOException", e);
		} catch (ImageMatchException e) {
			log.error("ImageMatchException", e);
		}

	}

	/**
	 * Verify if the actual image and expected image are same
	 * 
	 * @param actualValue
	 * @param expectedValue
	 */
	public void hasImage(BufferedImage actualValue, BufferedImage expectedValue) {
		ImageCompare imageCompare = new ImageCompare(actualValue, expectedValue);
		imageCompare.compare();

		log.debug(" actualValue ==>" + actualValue + " && expectedValue ==>" + expectedValue);
		SoftAssertor.assertTrue(imageCompare.match());
	}

	/**
	 * Verify the HTML element has the expected value for 'Name' attribute
	 * 
	 * @param actualValue
	 * @param expectedValue
	 * @throws Exception
	 */
	public void hasName(String actualValue, Object expectedValue) {
		hasValue(actualValue, expectedValue);
	}

	/**
	 * Verify the HTML element has the expected value for 'Content' attribute
	 * 
	 * @param actualValue
	 * @param expectedValue
	 * @throws Exception
	 */
	public void hasContent(String actualValue, Object expectedValue) {
		hasValue(actualValue, expectedValue);
	}

	/**
	 * Verify the HTML element has the expected value for 'src' attribute
	 * 
	 * @param actualValue
	 * @param expectedValue
	 * @throws Exception
	 */
	public void hasSrc(String actualValue, Object expectedValue) {
		hasValue(actualValue, expectedValue);
	}

	/**
	 * 
	 * Verify the HTML element has the expected value for 'Href' attribute
	 * 
	 * @param actualValue
	 * @param expectedValue
	 * @throws Exception
	 */
	public void hasHref(String actualValue, Object expectedValue) {
		hasValue(actualValue, expectedValue);
	}

	/**
	 * Matches the count of child elements (of same type) within the given
	 * Parent Element against the expected count
	 * 
	 * @param actualValue
	 * @param expectedValue
	 * @throws Exception
	 */
	public void hasChildCount(String actualValue, Object expectedValue) {
		hasValue(actualValue, expectedValue.toString());
	}

	/**
	 * Matches the
	 * 
	 * @param actualValue
	 * @param expectedValue
	 * @throws Exception
	 */
	public void containsLinkText(String actualValue, Object expectedValue) {
		contains(actualValue, expectedValue.toString());
	}

	/**
	 * Matches the current Elements display state with the expected state
	 * 
	 * @param actualValue
	 * @param expectedValue
	 * @throws Exception
	 */
	public void isDisplayed(String actualValue, Object expectedValue) {
		hasValue(actualValue, expectedValue);
	}

	/**
	 * Matches the current Elements enabled state with the expected state
	 * 
	 * @param actualValue
	 * @param expectedValue
	 * @throws Exception
	 */
	public void isEnabled(String actualValue, Object expectedValue) {
		hasValue(actualValue, expectedValue);
	}

	/**
	 * Matches the current Elements selected state with the expected state
	 * 
	 * @param actualValue
	 * @param expectedValue
	 * @throws Exception
	 */
	public void isSelected(String actualValue, Object expectedValue) {
		hasValue(actualValue, expectedValue);
	}

	/**
	 * 
	 * Matches the current Elements checked state with the expected state
	 * 
	 * @param actualValue
	 * @param expectedValue
	 * @throws Exception
	 */
	public void isChecked(String actualValue, Object expectedValue) {
		hasValue(actualValue, expectedValue);
	}

	/**
	 * Matches the current Elements visible state with the expected state
	 * 
	 * @param actualValue
	 * @param expectedValue
	 * @throws Exception
	 */
	public void isElementVisible(String actualValue, Object expectedValue) {
		hasValue(actualValue, expectedValue);
	}

	/**
	 * Verify if the retrieved value is greater than expected value
	 * 
	 * @param greaterValue
	 * @param lesserValue
	 * @throws Exception
	 */
	public void isGreaterThan(String greaterValue, Object lesserValue) {

		log.debug(" greaterValue ==>" + greaterValue + " && lesserValue ==>" + String.valueOf(lesserValue));

		Double greaterDoubleValue = Double.valueOf(greaterValue);
		Double lesserDoubleValue = getDoubleValue(lesserValue);

		boolean isGreaterThan = greaterDoubleValue > lesserDoubleValue ? true : false;
		SoftAssertor.assertTrue(isGreaterThan);

	}

	/**
	 * Verify if the retrieved value is less than the expected value
	 * 
	 * @param lesserValue
	 * @param greaterValue
	 * @throws Exception
	 */
	public void isLessThan(String lesserValue, Object greaterValue) {

		log.debug(" lesserValue ==>" + lesserValue + " && greaterValue ==>" + greaterValue.toString());

		Double lesserDoubleValue = Double.valueOf(lesserValue);

		Double greaterDoubleValue = getDoubleValue(greaterValue);

		boolean isGreaterThan = lesserDoubleValue < greaterDoubleValue ? true : false;
		SoftAssertor.assertTrue(isGreaterThan);
	}

	/**
	 * @param comparedValue
	 * @return
	 */
	private Double getDoubleValue(Object comparedValue) {

		Double doubleValue;

		if (comparedValue instanceof Long) {
			Long longValue = (Long) comparedValue;
			doubleValue = longValue.doubleValue();
		} else {
			doubleValue = (Double) comparedValue;
		}

		return doubleValue;
	}

	/**
	 * Matches the Image Title from the Web Element to the asserted value.
	 * 
	 * @param actualValue
	 * @param expectedValue
	 * @throws Exception
	 */
	public void hasImageTitle(String actualValue, Object expectedValue) {
		hasValue(actualValue, expectedValue);
	}

	/**
	 * Matches the Image Alt Text from the Web Element to the Assered value.
	 * 
	 * @param actualValue
	 * @param expectedValue
	 * @throws Exception
	 */
	public void hasImageAltText(String actualValue, Object expectedValue) {
		hasValue(actualValue, expectedValue);
	}

	/**
	 * Matches the style defined for the WebElement against the asserted value.
	 * 
	 * @param actualValue
	 * @param expectedValue
	 * @throws Exception
	 */
	public void hasStyle(String actualValue, Object expectedValue) {
		hasValue(actualValue, expectedValue);
	}

	/**
	 * Searches for the specfied style attribute with the retrieved style of Web
	 * Element.
	 * 
	 * @param actualValue
	 * @param expectedValue
	 * @throws Exception
	 */
	public void styleContains(String actualValue, Object expectedValue) {
		contains(actualValue, expectedValue);
	}

	/**
	 * Verify the response code for status = 200
	 * 
	 * @param actualValue
	 * @param expectedValue
	 * @throws Exception
	 */
	public void isSuccess(String actualValue) {
		hasValue(actualValue, ResponseStatusCode.OK.getResponseCode());
	}

	public void isCreated(String actualValue) {
		hasValue(actualValue, ResponseStatusCode.CREATED.getResponseCode());
	}

	public void isAccepted(String actualValue) {
		hasValue(actualValue, ResponseStatusCode.ACCEPTED.getResponseCode());
	}

	public void isInformationPartial(String actualValue) {
		hasValue(actualValue, ResponseStatusCode.PARTIAL_INFORMATION.getResponseCode());
	}

	public void hasNoResponse(String actualValue) {
		hasValue(actualValue, ResponseStatusCode.NO_RESPONSE.getResponseCode());
	}

	public void isBadRequest(String actualValue) {
		hasValue(actualValue, ResponseStatusCode.BAD_REQUEST.getResponseCode());
	}

	public void isUnauthorized(String actualValue) {
		hasValue(actualValue, ResponseStatusCode.UNAUTHORIZED.getResponseCode());
	}

	public void isPaymentRequired(String actualValue) {
		hasValue(actualValue, ResponseStatusCode.PAYMENT_REQUIRED.getResponseCode());
	}

	public void isForbidden(String actualValue) {
		hasValue(actualValue, ResponseStatusCode.FORBIDDEN.getResponseCode());
	}

	public void isNotFound(String actualValue) {
		hasValue(actualValue, ResponseStatusCode.NOT_FOUND.getResponseCode());
	}

	public void hasInternalError(String actualValue) {
		hasValue(actualValue, ResponseStatusCode.INTERNAL_ERROR.getResponseCode());
	}

	public void hasNotImplemented(String actualValue) {
		hasValue(actualValue, ResponseStatusCode.NOT_IMPLEMENTED.getResponseCode());
	}

	public void isServiceOverloaded(String actualValue) {
		hasValue(actualValue, ResponseStatusCode.SERVICE_OVERLOADED.getResponseCode());
	}

	public void isGatewayTimeout(String actualValue) {
		hasValue(actualValue, ResponseStatusCode.GATEWAY_TIMEOUT.getResponseCode());
	}

	public void isURLRedirected(String actualValue) {
		hasValue(actualValue, ResponseStatusCode.REDIRECTED.getResponseCode());
	}

	public void isURLMoved(String actualValue) {
		hasValue(actualValue, ResponseStatusCode.MOVED.getResponseCode());
	}

	/**
	 * Matches the value of the Input tag defined for the WebElement against the
	 * asserted value.
	 * 
	 * @param actualValue
	 * @param expectedValue
	 * @throws Exception
	 */
	public void hasInputValue(String actualValue, Object expectedValue) {
		hasValue(actualValue, expectedValue);
	}
}