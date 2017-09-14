/*
 * 
 */
package com.etouch.common;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.AssertJUnit;
import org.testng.ITestContext;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileDriver;
import io.appium.java_client.TouchAction;
import io.appium.java_client.ios.IOSDriver;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.Properties;

import org.junit.Ignore;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.Assert;

import com.etouch.taf.core.TestBed;
import com.etouch.taf.core.TestBedManager;
import com.etouch.taf.core.resources.ObjectType;
import com.etouch.taf.core.resources.ObjectValType;
import com.etouch.taf.core.resources.WaitCondition;
import com.etouch.taf.tools.rally.VideoRecorder;
import com.etouch.taf.util.CommonUtil;
import com.etouch.taf.util.SoftAssertor;
import com.etouch.taf.webui.selenium.WebPage;
import com.etouch.taf.webui.selenium.webelement.Button;
import com.etouch.taf.webui.selenium.webelement.Link;
import com.etouch.taf.webui.selenium.webelement.Text;


/**
 * The Class iOSDeviceTest.
 */
public class iOSDeviceTest extends BaseTest {
	
	
	/**
	 * Test google.
	 *
	 * @throws Exception the exception
	 */
	AppiumDriver driver=null;
	String testBedName=null;
	String deviceName=null;
	
	
	@BeforeClass
	public synchronized void setUp(ITestContext context) throws InterruptedException, FileNotFoundException, IOException {
		try {
			
			
			testBedName=context.getCurrentXmlTest().getAllParameters().get("testBedName");
			CommonUtil.sop("Test bed Name is " + testBedName);
			
			
			TestBed testBed=TestBedManager.INSTANCE.getCurrentTestBeds().get(testBedName);
			Thread.sleep(20000);
			driver=(AppiumDriver)(testBed.getDriver());
			
			
			deviceName =testBed.getDevice().getName();
			
		}

		catch (Exception e) {
			CommonUtil.sop("errr is for" +testBedName + " -----------"+ e);
			SoftAssertor.addVerificationFailure(e.getMessage());
		}
	}
	
	@Test
	 public  void testGoogle() throws Exception {
	final int MAX_WAIT = 20;
	  
	 Thread.sleep(3000);
	
	 
	if(deviceName.equals("iPhone")){ 
	 WebElement element = driver.findElement(By.xpath("//UIAApplication[1]/UIAWindow[1]/UIATableView[1]/UIASearchBar[1]"));
	 element.sendKeys("Coffee!");
	 Thread.sleep(2000);
	 
	 
	 //This code is with eTaap- which needs to be updated
	 WebElement button= driver.findElement(By.xpath(
				"//UIApplication[1]/UIAWindow[2]/UIAKeyboard[1]/UIAButton[4]"));
	button.click();

	 
	 WebElement searchElement=driver.findElement(By.xpath("//UIApplication[1]/UIAWindow[2]/UIAKeyboard[1]/UIAButton[4]"));
	 searchElement.click();
	 
	 Thread.sleep(2000);
	 
	 driver.findElement(By.xpath(
				"//UIAApplication[1]/UIAWindow[1]/UIATableView[1]/UIATableCell[6]/UIAStaticText[1]")).click();
	// WebElement resultElement=driver.findElement(By.xpath("//UIAApplication[1]/UIAWindow[1]/UIATableView[1]/UIATableCell[6]/UIAStaticText[1]"));
	// resultElement.click();
	 
	 Thread.sleep(2000);
	 WebElement mapElement=driver.findElement(By.xpath("//UIAApplication[1]/UIAWindow[1]/UIAMapView[1]/UIAPopover[1]/UIAStaticText[1]"));
	 mapElement.click();
	 CommonUtil.sop("map Result " + mapElement.getText());
	 AssertJUnit.assertEquals("Starbucks" ,mapElement.getText());
	 //SoftAssertor.assertEquals("Starbucks" ,mapElement.getText());
	}
	else{
		CommonUtil.sop(" I will not run iOS test in Android");
	}


	 
	}
	} 