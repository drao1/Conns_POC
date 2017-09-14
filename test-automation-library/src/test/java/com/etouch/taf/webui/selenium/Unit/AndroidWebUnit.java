package com.etouch.taf.webui.selenium.Unit;

import static org.junit.Assert.*;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.apache.commons.logging.Log;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.openqa.selenium.By;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
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

@RunWith(MockitoJUnitRunner.class)
@Ignore
public class AndroidWebUnit {
	
private static Log log = LogUtil.getLog(AndroidWebUnit.class);
	
	private static AndroidDriver driverObj = null;
	
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
	public static void beforeClass()
	{
		prop = TafTestUtil.loadProps(TafTestUtil.propFilePath);
		
		TafTestUtil.initialize();
		
		Mockito.when(mockContext.getCurrentXmlTest()).thenReturn(mockXmlTest);
		Mockito.when(mockXmlTest.getAllParameters()).thenReturn(mockMap);
		Mockito.when(mockMap.get("testBedName")).thenReturn("AndroidWeb");
	
			
		//String[] currentTestbeds = TestBedManagerConfiguration.INSTANCE.getMobileConfig().getCurrentTestBeds();
		String currentTestBedName = prop.getProperty("android_chrome");
		//TestBed currentTestbed = TafTestUtil.loadTestBedDetails(currentTestBed);
		//TestBedManager.INSTANCE.setCurrentTestBed(currentTestbed);		
		TestBedManager.INSTANCE.getCurrentTestBeds().get(currentTestBedName);
			
	}
	
	@Before
	public void before() throws Exception
	{
		Mockito.when(mockContext.getCurrentXmlTest().getAllParameters().get("testBedName")).thenReturn("AndroidWeb");
		
		

		mobileView = new MobileView(mockContext);
		driverObj=(AndroidDriver) mobileView.getDriver();
		mobileView.loadPage(prop.getProperty("w3schools_url"));
	}
	
	@Test
	public void testSendKeys() throws InterruptedException, PageException
	{
		
		Thread.sleep(2000);
		
		//Click on search button
		mobileView.findObjectById(prop.getProperty("w3schools_search_link_id")).click();	
		
		//send keys to search text box
		mobileView.findObjectById(prop.getProperty("w3schools_search_textbox_id")).sendKeys("Drag and drop");
		
		//click on search button
		mobileView.findObjectByClass(prop.getProperty("w3schools_search_button_class")).click();
		
		//validate the result
		ITafElement element = mobileView.findObjectByClass(prop.getProperty("w3schools_search_result_class"));
		Assert.assertNotNull(element);
		
		//scroll to the bottom
		mobileView.scrollBottom();
		
		
	}
	
	//Zoom, pinch functionalities are not working on Mobile web.Appium is not supporting that.
	
	@Test
	public void testZoom() throws PageException, InterruptedException
	{
		//mobileView.loadPage("http://www.w3schools.com");
		Thread.sleep(2000);
		
		//Find Learn HTML link element position before zoom
		ITafElement element1 = mobileView.findObjectByLink(prop.getProperty("w3schools_learn_html_linktext"));
		
		
		Thread.sleep(1000);
		
		//zoom the screen
		mobileView.zoomIn(2);
		
		//Find Learn HTML link element position after zoom
		ITafElement element2 = mobileView.findObjectByLink(prop.getProperty("w3schools_learn_html_linktext"));
		//Point p2 = element1.getCoordinates();
		
		//validate the positions
		
		//System.out.println(p1.getX() + "," + p1.getY() + "," + p2.getX() + "," + p2.getY());
		
	}
	
	
	@Test
	public void testScroll() throws InterruptedException, PageException
	{
		//mobileView.loadPage("http://www.w3schools.com");
		Thread.sleep(2000);
		
		//click on LEARN HTML link
		mobileView.findObjectByLink(prop.getProperty("w3schools_learn_html_linktext")).click();
		Thread.sleep(1000);
		
		//Scroll down thrice through the page
		mobileView.scrollDown(3);
		Thread.sleep(2000);
		
		//Find start learning html link
		ITafElement elemnt = mobileView.findObjectByLink(prop.getProperty("w3schools_start_learning_linktext"));
		
		//validate it
		Assert.assertNotNull(elemnt);
		
		//scroll to top
		mobileView.scrollTop();	
		
	}

	@After
	public void after()
	{
		driverObj = (AndroidDriver)mobileView.getDriver();
		TestUtil.closeMobileDriver(driverObj);
	}
	
	
}
