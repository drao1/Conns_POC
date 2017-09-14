package com.etouch.taf.webui.selenium.Unit;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;

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
import org.openqa.selenium.Dimension;
import org.openqa.selenium.ScreenOrientation;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.ITestContext;
import org.testng.xml.XmlTest;
import org.mockito.Mock;
import org.mockito.Mockito;



import org.mockito.runners.MockitoJUnitRunner;

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

public class AndroidAppUnit {
	
	private static Log log = LogUtil.getLog(AndroidAppUnit.class);
	
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
		
		Mockito.when(mockContext.getCurrentXmlTest()).thenReturn(mockXmlTest);
		Mockito.when(mockXmlTest.getAllParameters()).thenReturn(mockMap);
		Mockito.when(mockMap.get("testBedName")).thenReturn("Android");
		
			
		//String[] currentTestbeds = TestBedManagerConfiguration.INSTANCE.getMobileConfig().getCurrentTestBeds();
		String currentTestBedName = prop.getProperty("android_native");
		TestBed currentTestbed = TafTestUtil.loadTestBedDetails(currentTestBedName);
		TestBedManager.INSTANCE.getCurrentTestBeds().get(currentTestBedName);		
		
	}
	
	@Before
	public void before() throws Exception
	{
		//appPackage: com.experitest.ExperiBank	 
	    //appActivity: .LoginActivity
		
		
		Mockito.when(mockContext.getCurrentXmlTest().getAllParameters().get("testBedName")).thenReturn("Android");
		mobileView = new MobileView(mockContext);
		driverObj=(AndroidDriver)mobileView.getDriver();
	}
	
	//Add double tap & start activity in mobile element
	
	//@Before
	/*public void before() throws DriverException
	{
		
		AndroidDriver andDriver = (AndroidDriver) driverObj;
		andDriver.findElementsByAndroidUIAutomator("UiSelector.description(\"UserName\")");
		
		//android.widget.FrameLayout[1]/android.widget.LinearLayout/android.widget.FrameLayout[2]/com.experitest.ExperiBank:id/loginView[1]/android.widget.LinearLayout[1]/com.experitest.ExperiBank:id/usernameTextField
	
		
		
	}*/

	/*
	 *  appPackage: com.experitest.ExperiBank	 
    	appActivity: .LoginActivity
	 */
	//@Test
	public void testClick_Sendkeys() throws PageException {
		
		//Find eribank user name field
		ITafElement element = mobileView.findObjectById(prop.getProperty("eribank_username_field"));
		
		//enter value
		element.sendKeys("company");
		
		//Find eribank password field
		element = mobileView.findObjectById(prop.getProperty("eribank_password_field"));
		
		//enter value
		element.sendKeys("company");
		
		//find login button
		element = mobileView.findObjectById(prop.getProperty("eribank_login_button"));
		
		//click on it
		element.click();
		
		//find make payment button
		element = mobileView.findObjectById(prop.getProperty("eribank_makePayment_button"));
		
		//Assert on it
		Assert.assertEquals("Make Payment", element.getText());
		
		//find logout button
		element = mobileView.findObjectById(prop.getProperty("eribank_logout_button"));
		
		//click on it
		element.click();
	}
	
	
	@Test
	public void testTap() throws PageException
	{
		//Find eribank user name field
		ITafElement element = mobileView.findObjectById(prop.getProperty("eribank_username_field"));
		
		//enter value
		element.sendKeys("company");
		
		//Find eribank password field
		element = mobileView.findObjectById(prop.getProperty("eribank_password_field"));
		
		//enter value
		element.sendKeys("company");
		
		//find login button
		element = mobileView.findObjectById(prop.getProperty("eribank_login_button"));
		
		//Tap on it
		element.tap(2, 5);
		
		//find make payment button
		element = mobileView.findObjectById(prop.getProperty("eribank_makePayment_button"));
		
		//Assert on it
		Assert.assertEquals("Make Payment", element.getText());
		
		//find logout button
		element = mobileView.findObjectById(prop.getProperty("eribank_logout_button"));
		
		//tap on it
		element.tap(2, 5);
		
	}
	
	
	
	@Test
	public void testScrollTo() throws PageException
	{
		//login to eribank
		ITafElement element = mobileView.findObjectById(prop.getProperty("eribank_username_field"));
		element.sendKeys("company");
		element = mobileView.findObjectById(prop.getProperty("eribank_password_field"));
		element.sendKeys("company");
		element = mobileView.findObjectById(prop.getProperty("eribank_login_button"));
		element.click();
		
		//click on Make Payment
		element = mobileView.findObjectById(prop.getProperty("eribank_makePayment_button"));
		element.click();
		
		//Click on country select box
		element = mobileView.findObjectById(prop.getProperty("eribank_country_select_button"));
		element.click();
		
		//Scroll To "China"
		element = mobileView.scrollTo("China");
		element.click();
		
		element = mobileView.findObjectById(prop.getProperty("eribank_country_box"));
		Assert.assertEquals("China", element.getText());
		
		//Click on Cancel button 
		element = mobileView.findObjectById(prop.getProperty("eribank_cancel_button"));
		element.click();
		
		//logout
		element = mobileView.findObjectById(prop.getProperty("eribank_logout_button"));
		element.click();
		
	}
	
	@Test
	public void testScrollToExact() throws PageException
	{
		//login to eribank
		ITafElement element = mobileView.findObjectById(prop.getProperty("eribank_username_field"));
		element.sendKeys("company");
		element = mobileView.findObjectById(prop.getProperty("eribank_password_field"));
		element.sendKeys("company");
		element = mobileView.findObjectById(prop.getProperty("eribank_login_button"));
		element.click();
		
		//click on Make Payment
		element = mobileView.findObjectById(prop.getProperty("eribank_makePayment_button"));
		element.click();
		
		//Click on country select box
		element = mobileView.findObjectById(prop.getProperty("eribank_country_select_button"));
		element.click();
		
		//Scroll To "France"
		element = mobileView.scrollToExact("France");
		element.click();
		
		element = mobileView.findObjectById(prop.getProperty("eribank_country_box"));
		Assert.assertEquals("France", element.getText());
		
		//Click on Cancel button 
		element = mobileView.findObjectById(prop.getProperty("eribank_cancel_button"));
		element.click();
		
		//logout
		element = mobileView.findObjectById(prop.getProperty("eribank_logout_button"));
		element.click();
		
	}
	
	@Test
	public void testSwipeUpAndDowninApp() throws PageException
	{
		//login to eribank
		ITafElement element = mobileView.findObjectById(prop.getProperty("eribank_username_field"));
		element.sendKeys("company");
		element = mobileView.findObjectById(prop.getProperty("eribank_password_field"));
		element.sendKeys("company");
		element = mobileView.findObjectById(prop.getProperty("eribank_login_button"));
		element.click();
		
		//click on Make Payment
		element = mobileView.findObjectById(prop.getProperty("eribank_makePayment_button"));
		element.click();
		
		//Click on country select box
		element = mobileView.findObjectById(prop.getProperty("eribank_country_select_button"));
		element.click();
		
		//Swipe up the screen
		mobileView.swipeUp();
		mobileView.swipeDown();
		
		//WebElement element2 = ((AndroidDriver)mobileView.getDriver()).findElementByAndroidUIAutomator("new UiSelector().text(\"India\")");
		element = mobileView.findElementByAndroidUIAutomator("new UiSelector().text(\"India\")");
		Assert.assertNotNull(element);
		mobileView.swipeUp();
		
		//Select "Canada"
		//element = mobileView.findObjectByxPath(prop.getProperty("eribank_country_canada"));
		//WebElement element1 = ((AndroidDriver)mobileView.getDriver()).findElementByAndroidUIAutomator("new UiSelector().text(\"Canada\")");
		
		element = mobileView.findElementByAndroidUIAutomator("new UiSelector().text(\"Canada\")");
		Assert.assertNotNull(element);
		element.click();
		
		element = mobileView.findObjectById(prop.getProperty("eribank_country_box"));
		Assert.assertEquals("Canada", element.getText());		
		
		//Click on Cancel button 
		element = mobileView.findObjectById(prop.getProperty("eribank_cancel_button"));
		element.click();
		
		//logout
		element = mobileView.findObjectById(prop.getProperty("eribank_logout_button"));
		element.click();
		
	}
	
	
	
	//Page method test cases
	
	@Test
	public void testGetOrientation()
	{
		ScreenOrientation screenOrientation = mobileView.getOrientation();
		Assert.assertEquals(ScreenOrientation.PORTRAIT.name(), screenOrientation.name());
	}
	
	@Test
	public void testRotate() throws InterruptedException
	{
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

	//Below test methods are specific for HTC device
	
	//@Test
	public void testSwipeRight() throws InterruptedException, PageException
	{
		mobileView.navigateBack();
		mobileView.swipeRight();		
		Thread.sleep(2000);
	    ITafElement element = mobileView.findObjectById("com.htc.launcher:id/feed_content_text");
	    String expected = "Connect and share with people from all your social networks. Tap here to start.";
	    Assert.assertEquals(expected, element.getText());	 
	    mobileView.swipeLeft();
	}
	
	@Test
	public void testSwipeLeft() throws PageException, InterruptedException
	{
		mobileView.navigateBack();
		mobileView.swipeLeft();
		Thread.sleep(2000);
		ITafElement element = mobileView.findObjectByClass("android.widget.TextView");
		Assert.assertNotNull(element);
		mobileView.swipeRight();
	}
	
	@Test
	public void testSwipeUp() throws InterruptedException, PageException
	{
		mobileView.navigateBack();
		mobileView.swipeUp();
		Thread.sleep(2000);
		ITafElement element = mobileView.findObjectById("com.google.android.googlequicksearchbox:id/search_box");
		String expected = "Search, or say /'Ok Google/'";
		Assert.assertEquals(expected, element.getText());
	}
	
	
	@Test
	public void testPinch() throws InterruptedException, PageException
	{
		((AndroidDriver)mobileView.getDriver()).startActivity("com.htc.album", ".AlbumMain.ActivityMainTabHost");
		Thread.sleep(1000);
		//ITafElement element = mobileView.findObjectByxPath("//android.widget.FrameLayout[0]/android.view.View[0]/android.widget.FrameLayout[0]/android.view.View[0]/com.htc.lib1.cc.view.viewpager.HtcViewPager[0]/android.widget.FrameLayout[0]/android.widget.RelativeLayout[0]/android.widget.RelativeLayout[0]/android.widget.ListView[0]/android.view.View[0]");
		//Find Camera Shots in Gallery app
		List<ITafElement>  elementList = mobileView.findObjectsByClass(prop.getProperty("gallery_camerashots_class"));
		
		//Click on Camera Shots
		elementList.get(3).click();
			
		//Find first image
		elementList = mobileView.findObjectsByClass(prop.getProperty("gallery_image_class"));
		
		//click on image
		elementList.get(1).click();
		Thread.sleep(1000);
		
		ITafElement element = mobileView.findObjectById(prop.getProperty("gallery_image_id"));
		
		element.tap(2, 2);
		
		Thread.sleep(1000);
		element.pinch();
		
		
		Thread.sleep(1000);
		//elementList.get(2).zoom();
		
		mobileView.zoom(50,50);
		
		Thread.sleep(1000);
		
		mobileView.pinch(50,50);
		
		Thread.sleep(1000);
		
		Assert.assertNotNull(element);
		
		
	}
	
	@After
	public void after()
	{
		driverObj = mobileView.getDriver();
		TestUtil.closeMobileDriver(driverObj);
	}
	
	

}
