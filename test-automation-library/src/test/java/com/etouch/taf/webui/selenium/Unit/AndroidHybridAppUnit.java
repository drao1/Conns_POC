package com.etouch.taf.webui.selenium.Unit;

import static org.junit.Assert.*;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
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
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.ITestContext;
import org.testng.xml.XmlTest;

import com.etouch.taf.core.TestBed;
import com.etouch.taf.core.TestBedManager;
import com.etouch.taf.core.config.CurrentTestBeds;
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
public class AndroidHybridAppUnit {
	
private static Log log = LogUtil.getLog(AndroidHybridAppUnit.class);
	
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
		public static void beforeClass() throws DriverException
		{
			prop = TafTestUtil.loadProps(TafTestUtil.propFilePath);
			
			TafTestUtil.initialize();
			
			Mockito.when(mockContext.getCurrentXmlTest()).thenReturn(mockXmlTest);
			Mockito.when(mockXmlTest.getAllParameters()).thenReturn(mockMap);
			Mockito.when(mockMap.get("testBedName")).thenReturn("AndroidHybrid");
			
				
			//String[] currentTestbeds = TestBedManagerConfiguration.INSTANCE.getMobileConfig().getCurrentTestBeds();
			String currentTestBedName = prop.getProperty("android_hybrid");
			//TestBed currentTestbed = TafTestUtil.loadTestBedDetails(currentTestBed);
			CurrentTestBeds currentTestBeds = new CurrentTestBeds();
			TestBedManager.INSTANCE.getCurrentTestBeds().get(currentTestBedName);		
			
		}
		
		@Before
		public void before() throws Exception
		{
			//appPackage: com.ihs.riginfomobile	 
		    //appActivity: riginfomobile.droid.views.LoginView
		    
			Mockito.when(mockContext.getCurrentXmlTest().getAllParameters().get("testBedName")).thenReturn("AndroidHybrid");
			mobileView = new MobileView(mockContext);
			driverObj = (AndroidDriver)mobileView.getDriver();
		}
	
		@Test
		public void testClick() {
			try{
				//Send keys for username
				ITafElement element = mobileView.findObjectById(prop.getProperty("ihs_activity_tracker_userName"));
				element.sendKeys("ihs123");		
				
				//send keys for password
				element = mobileView.findObjectById(prop.getProperty("ihs_activity_tracker_password"));
				element.sendKeys("ihs123");
				
				//Click on login button
				element = mobileView.findObjectById(prop.getProperty("ihs_activity_tracker_login_button"));
				element.click();
				
				//Validate alert message
				element = mobileView.findObjectById(prop.getProperty("ihs_activity_tracker_login_alert"));
				Assert.assertEquals("Upgrade Required", element.getText());
				
				//Click on Ok button
				element = mobileView.findObjectById(prop.getProperty("ihs_activity_tracker_ok_button"));
				element.click();
				
				CommonUtil.sop("test Click completed");
				
			}catch(Exception ex)
			{
				log.error(ex.getMessage());
			}
		}
		
		@Test
		public void testCheck()
		{
			try{
				
				//uncheck Remember me check box
				ITafElement element = mobileView.findObjectById(prop.getProperty("ihs_activity_tracker_remember_me_checkbox"));
				element.uncheck();
				
				//validate it
				Assert.assertFalse(element.isChecked());
				
				//Check Remember me check box
				element.check();
				
				//validate it
				Assert.assertTrue(element.isChecked());
				
				CommonUtil.sop("testCheck completed");
				
				
			}catch(Exception ex)
			{
				log.error(ex.getMessage());
			}
		}
	
		@Test
		public void testWebView() throws PageException, InterruptedException
		{
	       
			driverObj.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
			
			Thread.sleep(4000);
			
			//click on privacy policy on IHS app
			WebElement element = driverObj.findElementByXPath(prop.getProperty("ihs_activity_tracker_privacy_policy_link"));
			element.click();
			
			//Entered in to WebView
			
			Thread.sleep(4000);
			
			//click on Personal information privacy policy in web view
			element = driverObj.findElementByXPath(prop.getProperty("ihs_activity_tracker_personal_information_privacy_policy"));
			element.click();
			
			Thread.sleep(4000);
	
			//navigate back to app view
			driverObj.navigate().back();
			Thread.sleep(2000);
			
			driverObj.navigate().back();
			Thread.sleep(2000);
			
			//Entered in to Native view
			driverObj.context("NATIVE_APP");
			Thread.sleep(4000);
			
			// Click on login button
			//WebElement login=driverObj.findElement(By.xpath("//android.widget.LinearLayout[1]/android.widget.FrameLayout[1]/android.widget.ScrollView[1]/android.widget.LinearLayout[1]/android.widget.LinearLayout[2]/android.widget.FrameLayout[1]/android.widget.Button[1]"));
			WebElement login = driverObj.findElement(By.id(prop.getProperty("ihs_activity_tracker_login_button")));
			login.click();
			
			Thread.sleep(3000);
			
			CommonUtil.sop("testWebView completed");
			
		}
	
		@Test
		public void testWebViewWithMobileView() throws PageException, InterruptedException
		{
	       
			//driverObj.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
			
			Thread.sleep(4000);
			
			//click on privacy policy on IHS app
			ITafElement element = mobileView.findObjectByxPath(prop.getProperty("ihs_activity_tracker_privacy_policy_link"));
			element.click();
			
			//Entered in to WebView
			
			Thread.sleep(4000);
			
			//click on Personal information privacy policy in web view
			element = mobileView.findObjectByxPath(prop.getProperty("ihs_activity_tracker_personal_information_privacy_policy"));
			element.click();
			Thread.sleep(4000);
			
			//validate Personal Information Privacy Policy header
			element = mobileView.findObjectByxPath(prop.getProperty("ihs_activity_tracker_personal_information_privacy_policy_header"));
			Assert.assertNotNull(element);
			
			Thread.sleep(4000);
	
			//navigate back to app view
			mobileView.navigateBack();
			Thread.sleep(2000);
			
			mobileView.navigateBack();
			Thread.sleep(2000);
			
			//Entered in to Native view
			driverObj.context("NATIVE_APP");
			Thread.sleep(4000);
			
			// Uncheck Remember me checkbox
			element=mobileView.findObjectById(prop.getProperty("ihs_activity_tracker_remember_me_checkbox"));
			element.uncheck(); 
			
			//validate the checkbox
			Assert.assertFalse(element.isChecked());
			
			Thread.sleep(3000);
			
		}
	
		@Test
		public void testWebViewWithAndroidDriver() throws InterruptedException, MalformedURLException
		{
			driverObj.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
			
			Thread.sleep(4000);
			
			//Click on Activity tracker license link
			driverObj.findElement(By.name(prop.getProperty("ihs_activity_tracker_Activity_Tracker_License_Link"))).click();
			
			//Entered in to WebView
			
			Thread.sleep(2000);
			//wd.context("WEBVIEW_1");
			
			//Thread.sleep(2000);
			
			//Click on Search Text box
			WebElement search=driverObj.findElement(By.xpath(prop.getProperty("ihs_activity_tracker_Activity_Tracker_Search_Link")));
			search.click();
			
			//Enter Search Text
			WebElement searchText=driverObj.findElement(By.xpath(prop.getProperty("ihs_activity_tracker_Activity_Tracker_Search_Textbox")));
			searchText.sendKeys("wells");
			
			//Click on Search Button
			WebElement searchButton=driverObj.findElement(By.xpath(prop.getProperty("ihs_activity_tracker_Activity_Tracker_Search_Button")));
			searchButton.click();
			
			Thread.sleep(4000);
	
			//navigate back to App view		
			driverObj.navigate().back();
			Thread.sleep(2000);
			
			driverObj.navigate().back();
			Thread.sleep(2000);
			
			
			//Entered in to Native view
			driverObj.context("NATIVE_APP");
			Thread.sleep(2000);
			
			//Click on login button
			//WebElement login=driverObj.findElement(By.xpath("//android.widget.LinearLayout[1]/android.widget.FrameLayout[1]/android.widget.ScrollView[1]/android.widget.LinearLayout[1]/android.widget.LinearLayout[2]/android.widget.FrameLayout[1]/android.widget.Button[1]"));
			WebElement login = driverObj.findElement(By.id(prop.getProperty("ihs_activity_tracker_login_button")));
			login.click();
			
			Thread.sleep(3000);
			 
				
			}
		
		@After
		public void after()
		{
			driverObj = (AndroidDriver)mobileView.getDriver();
			TestUtil.closeMobileDriver(driverObj);
		}



}
