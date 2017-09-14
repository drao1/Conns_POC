package com.etouch.taf.webui.selenium.Unit;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.SwipeElementDirection;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;

import java.util.List;
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
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.ScreenOrientation;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
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
import com.etouch.taf.webui.selenium.MobileView;
import com.etouch.taf.webui.selenium.WebPage;

@RunWith(MockitoJUnitRunner.class)
@Ignore
public class IOSAppUnit {
	
	private static Log log = LogUtil.getLog(IOSAppUnit.class);
	
	private static AppiumDriver driverObj = null;
	
	private static MobileView mobileView = null;
	
	private static Properties prop = null;
	
	
	@Mock
	static
	ITestContext mockContext;
	
	@Mock
	static 
	XmlTest mockXmlTest;
	
	@Mock
	static
	Map<String, String> mockMap;
	
	@BeforeClass
	public static void beforeClass() throws DriverException
	{
		prop = TafTestUtil.loadProps(TafTestUtil.propFilePath);
		
		TafTestUtil.initialize();
			
		//String[] currentTestbeds = TestBedManagerConfiguration.INSTANCE.getMobileConfig().getCurrentTestBeds();
		String currentTestBedName = prop.getProperty("ios_native");
		//TestBed currentTestbed = TafTestUtil.loadTestBedDetails(currentTestBed);
		//TestBedManager.INSTANCE.setCurrentTestBed(currentTestbed);		
		TestBedManager.INSTANCE.getCurrentTestBeds().get(currentTestBedName);		
		
		Mockito.when(mockContext.getCurrentXmlTest()).thenReturn(mockXmlTest);
		Mockito.when(mockXmlTest.getAllParameters()).thenReturn(mockMap);
		Mockito.when(mockMap.get("testBedName")).thenReturn("iOSNative");
		
	
		
	}
	
	/*
	 *  appPath: /Users/etouch/Documents/Mobile_Testing/EriBank.ipa	 
    	bundleId: com.example.apple-samplecode.Recipes
	 */
	
	@Before
	public void before() throws Exception
	{
		//appPackage: com.experitest.ExperiBank	 
	    //appActivity: .LoginActivity
		Mockito.when(mockContext.getCurrentXmlTest().getAllParameters().get("testBedName")).thenReturn("iOSNative");

		mobileView = new MobileView(mockContext);
		driverObj = mobileView.getDriver();
	}
	
	//Add double tap & start activity in mobile element
	
	

	
	@Test
	public void testClick() throws PageException {
		
		//Find Chocolate Cake recipe
		ITafElement element = mobileView.findObjectByxPath(prop.getProperty("chocolate_cake_xpath"));
		
		//Click on it
		element.click();
		
		//Find Chocolate Cake recipe header
		element = mobileView.findObjectByxPath(prop.getProperty("chocolate_cake_recipe_header_xpath"));
		
		//Assert on it
		Assert.assertEquals("Chocolate Cake", element.getText());
		
		//Find instructions
		element = mobileView.findObjectByxPath(prop.getProperty("chocolate_cake_instructions_xpath"));
		
		//click on it
		element.click();
		
		//find Instructions
		element = mobileView.findObjectByxPath(prop.getProperty("chocolate_cake_instructions_hearder_xpath"));
		
		//Assert on it
		Assert.assertEquals("Chocolate Cake", element.getText());
		
	}
	
	
	@Test
	public void testTap() throws PageException
	{
		//Find Chocolate Cake recipe
		ITafElement element = mobileView.findObjectByxPath(prop.getProperty("chocolate_cake_xpath"));
		
		//Click on it
		element.tap(1,2);
		
		//Find Chocolate Cake recipe header
		element = mobileView.findObjectByxPath(prop.getProperty("chocolate_cake_recipe_header_xpath"));
		
		//Assert on it
		Assert.assertEquals("Chocolate Cake", element.getText());
		
		//Find instructions
		element = mobileView.findObjectByxPath(prop.getProperty("chocolate_cake_instructions_xpath"));
		
		//click on it
		element.tap(1,2);
		
		//find Instructions
		element = mobileView.findObjectByxPath(prop.getProperty("chocolate_cake_instructions_hearder_xpath"));
		
		//Assert on it
		Assert.assertEquals("Chocolate Cake", element.getText());
		
	}
	
	@Test
	public void testSendKeys() throws PageException
	{
		//Find add button and tap on it
		ITafElement element = mobileView.findObjectByxPath(prop.getProperty("recipes_add_button"));
		element.tap(1, 2);
		
		//Find recipe name text box and send keys
		element = mobileView.findObjectByxPath(prop.getProperty("recipe_name_text_box"));
		element.sendKeys("Veggie Pizza");
		
		//tap on cancel button
		element = mobileView.findObjectByxPath(prop.getProperty("recipe_cancel_button"));
		element.tap(1, 2);
		
		//Assert on Chocolate Cake in home page
		element = mobileView.findObjectByxPath(prop.getProperty("chocolate_cake_xpath"));
		Assert.assertNotNull(element);
	}
	
	
	@Test
	public void testSwipeRight() throws InterruptedException, PageException
	{
		//Find Unit coversion image and tap on it
		ITafElement element = mobileView.findObjectByxPath(prop.getProperty("unit_conversion_xpath"));
		element.tap(1, 2);
		
		//tap on temparature
		element = mobileView.findObjectByxPath(prop.getProperty("unit_conversion_temparature_xpath"));
		element.tap(1, 2);
		
		//assert on Centigrade text
		element = mobileView.findObjectByxPath(prop.getProperty("unit_conversion_temp_centigrade_xpath"));
		Assert.assertEquals("Centigrade", element.getText());
		
		//swipe the view to the right
		//mobileView.swipeRight();	
		element = mobileView.findObjectByxPath(prop.getProperty("recipe_table_view_xpath"));
		element.swipe(SwipeElementDirection.RIGHT,1000);
		Thread.sleep(1000);
		
		//assert on temparature text in the view
	    element = mobileView.findObjectByxPath(prop.getProperty("unit_conversion_temparature_xpath"));
	    Assert.assertEquals("Temperature", element.getText());
	   
	}
	
	
	@Test
	public void testSwipeLeft() throws PageException, InterruptedException
	{
		ITafElement element = mobileView.findObjectByxPath(prop.getProperty("three_berry_cobbler_xpath"));
		//ITafElement element = mobileView.findObjectByName("Three Berry Cobbler");
		element.swipe(SwipeElementDirection.LEFT, 1000);
		
		element = mobileView.findObjectByxPath(prop.getProperty("recipes_delete_button"));
		//element = mobileView.findObjectByName("delete");
		Assert.assertTrue(element.isDisplayed());
		
		element = mobileView.findObjectByxPath(prop.getProperty("three_berry_cobbler_xpath"));
		//element = mobileView.findObjectByxPath(prop.getProperty("Three Berry Cobbler"));
		element.swipe(SwipeElementDirection.RIGHT, 1000);
		
		Assert.assertFalse(element.isDisplayed());
	}
	
	
	//Page method test cases
	
	@Test
	public void testSwipeUpAndDowninApp() throws PageException
	{
		//Find Three Berry Cobbler Recipe
		ITafElement element = mobileView.findObjectByxPath(prop.getProperty("three_berry_cobbler_xpath"));
		//ITafElement element = mobileView.findObjectByName("Three Berry Cobbler");
		Point p1 = element.getCoordinates();
		System.out.println(p1.getX() + "," + p1.getY());
		
		//Swipe up the screen
		mobileView.swipeUp();
		
		//Find Three Berry Cobbler Recipe
		element = mobileView.findObjectByxPath(prop.getProperty("three_berry_cobbler_xpath"));
		//element = mobileView.findObjectByName("Three Berry Cobbler");
		Point p2 =element.getCoordinates();
		Assert.assertEquals(p1.getX(), p2.getX());
		
		
		//Swipe down the screen
		mobileView.swipeDown();
		
		//Find Three Berry Cobbler Recipe
		element = mobileView.findObjectByxPath(prop.getProperty("three_berry_cobbler_xpath"));
		//element = mobileView.findObjectByName("Three Berry Cobbler");
		Point p3 =element.getCoordinates();
		Assert.assertEquals(p1.getX(), p3.getX());
		
	}

	@Test
	public void testGetOrientation()
	{
		ScreenOrientation screenOrientation = mobileView.getOrientation();
		Assert.assertEquals(ScreenOrientation.PORTRAIT.name(), screenOrientation.name());
	}
	
	@Test
	public void testRotate() throws InterruptedException
	{
		mobileView.getDriver().navigate().back();
		//Rotate mobile view to Landscape mode
		mobileView.rotate(ScreenOrientation.LANDSCAPE);
		Thread.sleep(1000);
		
		//Assert the screen orientation
		Assert.assertEquals(ScreenOrientation.LANDSCAPE.name(), mobileView.getOrientation().name());
		
		//Rotate mobile view to Portrait mode
		mobileView.rotate(ScreenOrientation.PORTRAIT);
		Thread.sleep(1000);
		Assert.assertEquals(ScreenOrientation.PORTRAIT.name(), mobileView.getOrientation().name());
	}
	
	@Test
	public void testLockScreen() throws InterruptedException
	{
		mobileView.rotate(ScreenOrientation.LANDSCAPE);
		Thread.sleep(1000);
		mobileView.lockScreen(5);	
		mobileView.rotate(ScreenOrientation.PORTRAIT);
		Thread.sleep(1000);
		Assert.assertEquals(ScreenOrientation.PORTRAIT.name(), mobileView.getOrientation().name());
	}

	
	
	//@After
	public void after()
	{
		driverObj = mobileView.getDriver();
		TestUtil.closeMobileDriver(driverObj);
	}
	
	

}
