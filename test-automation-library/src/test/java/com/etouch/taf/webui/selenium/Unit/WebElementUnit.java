package com.etouch.taf.webui.selenium.Unit;

import java.util.Map;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.xml.XmlTest;

import com.etouch.taf.core.TestBed;
import com.etouch.taf.core.TestBedManager;
import com.etouch.taf.core.config.TestBedManagerConfiguration;
import com.etouch.taf.core.exception.DriverException;
import com.etouch.taf.core.exception.PageException;
import com.etouch.taf.core.test.util.TafTestUtil;
import com.etouch.taf.util.CommonUtil;
import com.etouch.taf.util.LogUtil;
import com.etouch.taf.util.TestUtil;
import com.etouch.taf.webui.ITafElement;
import com.etouch.taf.webui.selenium.WebElement;
import com.etouch.taf.webui.selenium.WebPage;

@RunWith(MockitoJUnitRunner.class)
public class WebElementUnit {

	private static Log log = LogUtil.getLog(WebElementUnit.class);

	private static WebPage webpage = null;

	private static WebDriver driverObj = null;

	private static Properties prop = null;

	@Mock
	ITestContext mockContext;

	@Mock
	XmlTest mockXmlTest;

	@Mock
	Map<String, String> mockMap;

	@Before
	public void before() throws Exception {
		prop = TafTestUtil.loadProps(TafTestUtil.propFilePath);

		TafTestUtil.initialize();

		String[] currentTestbeds = TestBedManagerConfiguration.INSTANCE.getWebConfig().getCurrentTestBeds();
		String currentTestbedName = currentTestbeds[0];
		TestBedManager.INSTANCE.getCurrentTestBeds().get(currentTestbedName);

		Mockito.when(mockContext.getCurrentXmlTest()).thenReturn(mockXmlTest);
		Mockito.when(mockXmlTest.getAllParameters()).thenReturn(mockMap);
		Mockito.when(mockMap.get("testBedName")).thenReturn("Firefox");

		webpage = new WebPage(mockContext);
		webpage.loadPage("http://www.amazon.com");
		driverObj = webpage.getDriver();

		// Mockito.verify(mockContext);
		// Mockito.verify(mockXmlTest);
		// Mockito.verify(mockMap);

	}

	@Test
	public void testClick() {
		try {
			// Find sign in button
			ITafElement element = webpage.findObjectById(prop.getProperty("amazon_signin_link"));
			Assert.assertEquals("rgba(0, 102, 192, 1)", element.getCssValue("color"));

			// hover on it
			element.hover();

			// Click on it
			element.click();

			// find the sign in element
			String text = webpage.findObjectById(prop.getProperty("amazon_signin_title")).getText();

			Assert.assertEquals(text, "Sign In");
		} catch (Exception ex) {
			log.error(ex.getMessage());
		}
	}

	@Test
	public void testSendKeys() {
		try {

			// Find sign in button
			ITafElement signInElement = webpage.findObjectById(prop.getProperty("amazon_signin_link"));
			Assert.assertEquals("rgba(0, 102, 192, 1)", signInElement.getCssValue("color"));

			// hover on it
			signInElement.hover();

			// Click on it
			signInElement.click();

			// Find email field
			ITafElement element = webpage.findObjectById(prop.getProperty("amazon_email_field"));

			// enter text
			element.sendKeys("Text");

			Assert.assertEquals("Text", element.getAttribute("value"));
			CommonUtil.sop(element.getAttribute("style"));

			// clear email field
			element.clear();

			Assert.assertEquals("", element.getAttribute("value"));
		} catch (Exception ex) {
			log.error(ex.getMessage());
		}
	}

	@Test
	public void testHover() {
		try {
			// find wishlist link
			ITafElement element = webpage.findObjectById(prop.getProperty("amazon_wishlist_link"));

			// hover on it
			element.hover();

			// find hovered box
			ITafElement hoveredItem = webpage.findObjectById(prop.getProperty("amazon_wishlist_hover_box"));

			// assert
			Assert.assertTrue(hoveredItem.isDisplayed());

		} catch (Exception ex) {
			log.error(ex.getMessage());
		}
	}

	@Test
	public void testDoubleClick() {
		try {
			// find sign in link
			ITafElement element = webpage.findObjectById(prop.getProperty("amazon_signin_link"));

			// assert on css value
			Assert.assertEquals("rgba(0, 102, 192, 1)", element.getCssValue("color"));

			// double click on signin link
			element.doubleClick();
			;

			// Assert on Sign in text
			String text = webpage.findObjectById(prop.getProperty("amazon_signin_title")).getText();
			Assert.assertEquals(text, "Sign In");
		} catch (Exception ex) {
			log.error(ex.getMessage());
		}
	}

	// This method is giving error as uncheck is not working as expected
	@Test
	public void testScrollcheck() {
		try {
			// hover on shop by department in amazon.com
			// webpage.findObjectById("nav-shop").hover();
			webpage.findObjectByxPath(prop.getProperty("amazon_shop_by_department")).hover();

			// Thread.sleep(1000);

			// click on clothing, shoes & jewellery
			webpage.findObjectByxPath(prop.getProperty("amazon_clothing_shoes_jewellery")).hover();

			// Thread.sleep(1000);

			// click on Men
			webpage.findObjectByxPath(prop.getProperty("amazon_men")).click();

			// click on watches
			webpage.findObjectByxPath(prop.getProperty("amazon_watches")).click();

			// check casual
			webpage.scrollDown(3);
			webpage.findObjectByxPath(prop.getProperty("amazon_watches_casual")).check();
			Thread.sleep(2000);

			// check Luxury
			webpage.scrollDown(3);
			Assert.assertTrue(webpage.findObjectByxPath(prop.getProperty("amazon_watches_casual")).isEnabled());
			webpage.findObjectByxPath(prop.getProperty("amazon_watches_fashion")).check();
			Thread.sleep(2000);

			// uncheck Luxury
			webpage.scrollDown(3);
			webpage.findObjectByxPath(prop.getProperty("amazon_watches_fashion")).uncheck();

			webpage.scrollDown(1);
			Assert.assertFalse(webpage.findObjectByxPath(prop.getProperty("amazon_watches_fashion")).isEnabled());
			Thread.sleep(2000);

		} catch (Exception ex) {
			log.error(ex.getMessage());
		}
	}

	@Test
	public void testScrollTopAndBottom() throws PageException {
		// scroll to bottom
		webpage.scrollBottom();

		// find "Amazon Payment Products" element and assert on it.
		ITafElement element = webpage.findObjectByxPath(prop.getProperty("amazon_payment_products"));
		Assert.assertEquals("Amazon Payment Products", element.getText());

		// Scroll to Top
		webpage.scrollTop();

		// find sign in text and assert
		element = webpage.findObjectByxPath(prop.getProperty("amazon_signin_text"));
		Assert.assertEquals("Hello. Sign in", element.getText());
		element.hover();

	}

	@Test
	public void testScrollToElement() throws PageException {
		// find the element "Amazon.com Rewards Visa Card"
		ITafElement element = webpage.findObjectByxPath(prop.getProperty("amazon_rewards_visa_card"));

		// Scroll to the element
		webpage.scrollToElement(element);

		// Assert and click on it
		Assert.assertEquals("Amazon.com Rewards Visa Card", element.getText());
		element.click();

	}

	@Test
	public void testSubmit() throws PageException {
		// Find sign in link
		ITafElement signInElement = webpage.findObjectById(prop.getProperty("amazon_signin_link"));

		// hover and click on it
		signInElement.hover();
		signInElement.click();

		// find email field
		ITafElement element = webpage.findObjectById(prop.getProperty("amazon_email_field"));

		// enter text
		element.sendKeys("Text");

		// find password
		element = webpage.findObjectById(prop.getProperty("amazon_password_field"));

		// enter text
		element.sendKeys("Text");

		// find the sign in button
		element = webpage.findObjectById(prop.getProperty("amazon_signin_submit_button"));

		// submit it
		element.submit();

		// find the warning message
		element = webpage.findObjectByxPath(prop.getProperty("amazon_signin_warning_msg"));
		Assert.assertEquals("Important Message!", element.getText());
	}

	@Test
	public void testFullScrollInSlowMotion() throws InterruptedException, PageException {
		// Scroll the web page in slow motion
		webpage.fullScrollInSlowMotion();

		// find the text "Amazon Payment Products"
		ITafElement element = webpage.findObjectByxPath(prop.getProperty("amazon_payment_products"));

		Assert.assertEquals("Amazon Payment Products", element.getText());
	}

	@Test
	public void testselectDropDownList() throws PageException, InterruptedException {
		// find the search drop down box
		ITafElement dorpdown = webpage.findObjectById(prop.getProperty("amazon_search_dropdown_box"));

		// select "Books" in dropdown
		dorpdown.selectDropDownList("Books");

		// find "Books" element
		ITafElement element = webpage.findObjectByxPath(prop.getProperty("amazon_search_dropdown_books"));

		// assert that it is selected
		Assert.assertTrue(element.isSelected());

		// Find the search text box
		element = webpage.findObjectById(prop.getProperty("amazon_search_text_box"));

		// enter "fiction novels"
		element.sendKeys("fiction novels");

		// find search button
		element = webpage.findObjectByxPath(prop.getProperty("amazon_search_submit_button"));

		// submit it
		element.submit();

		// find the text "fiction novels" in the search results
		element = webpage.findObjectByxPath(prop.getProperty("amazon_search_result_text"));

		// Assert on the text
		Assert.assertEquals("\"fiction novels\"", element.getText());

	}

	@Test
	public void testDrapAndDrop() throws PageException {
		try {
			// open w3 schools tutorial
			driverObj.get(prop.getProperty("w3tutorials_url"));
			// driverObj.get("file:///C:/Users/P8-03GPQ0/Desktop/Drag_Drop.html");
			Thread.sleep(2000);

			// find draggable element
			// ITafElement draggable =
			// webpage.findObjectByxPath(".//*[@id='drag1']");
			ITafElement draggable = webpage.findObjectById(prop.getProperty("w3tutorials_draggable_text"));

			Point point1 = draggable.getCoordinates();

			// find the drop area
			ITafElement dropArea = webpage.findObjectById(prop.getProperty("w3tutorials_drop_area"));

			// drag and drop the text
			draggable.dragAndDrop(dropArea);

			Point point2 = draggable.getCoordinates();

			Assert.assertNotEquals(point1.getY(), point2.getY());
		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}

	@Test
	public void testZoomIn() throws PageException {
		// find signin element
		ITafElement element = webpage.findObjectById(prop.getProperty("amazon_signin_link"));

		// get coordinates of signin element
		Point point1 = element.getCoordinates();
		CommonUtil.sop(point1.getX() + ", " + point1.getY());

		// Zoom in the web page
		webpage.zoomIn(3);

		// find the signin element
		element = webpage.findObjectById(prop.getProperty("amazon_signin_link"));

		// get its cordinates
		Point point2 = element.getCoordinates();
		CommonUtil.sop(point2.getX() + ", " + point2.getY());

		// Assert on element location
		Assert.assertNotEquals(point1.getX(), point2.getX());

		// zoom it back to 100%
		webpage.zoomTo100();

	}

	@Test
	public void testZoomOut() throws PageException {
		// find signin element
		ITafElement element = webpage.findObjectById(prop.getProperty("amazon_signin_link"));

		// get coordinates of signin element
		Point point1 = element.getCoordinates();
		CommonUtil.sop(point1.getX() + ", " + point1.getY());

		// Zoom out the web page
		webpage.zoomOut(2);

		// find the signin element
		element = webpage.findObjectById(prop.getProperty("amazon_signin_link"));

		// get its cordinates
		Point point2 = element.getCoordinates();
		CommonUtil.sop(point2.getX() + ", " + point2.getY());

		// Assert on element location
		Assert.assertNotEquals(point1.getX(), point2.getX());

		// zoom it back to 100%
		webpage.zoomTo100();
	}

	@Test
	public void testZoomTo100() throws PageException {
		// find signin element
		ITafElement element = webpage.findObjectById(prop.getProperty("amazon_signin_link"));

		// get coordinates of signin element
		Point point1 = element.getCoordinates();
		CommonUtil.sop(point1.getX() + ", " + point1.getY());

		// Zoom out the web page
		webpage.zoomOut(2);

		// find the signin element
		element = webpage.findObjectById(prop.getProperty("amazon_signin_link"));

		// get its cordinates
		Point point2 = element.getCoordinates();
		CommonUtil.sop(point2.getX() + ", " + point2.getY());

		// zoom it to 100%
		webpage.zoomTo100();

		// find the signin element
		element = webpage.findObjectById(prop.getProperty("amazon_signin_link"));

		// get its cordinates
		Point point3 = element.getCoordinates();
		CommonUtil.sop(point3.getX() + ", " + point3.getY());

		// Assert on element location
		Assert.assertNotEquals(point1.getX(), point2.getX());
		Assert.assertEquals(point1.getX(), point3.getX());

	}

	@After
	public void tearDown() {
		TestUtil.closeDriver(driverObj);
	}

}
