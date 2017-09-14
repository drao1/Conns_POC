package com.etouch.taf.webui.selenium.Unit;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.SwipeElementDirection;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;

import java.util.HashMap;
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
import org.openqa.selenium.JavascriptExecutor;
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
import com.etouch.taf.mobile.appium.PageView;
import com.etouch.taf.util.CommonUtil;
import com.etouch.taf.util.LogUtil;
import com.etouch.taf.util.TestUtil;
import com.etouch.taf.webui.ITafElement;
import com.etouch.taf.webui.selenium.MobileView;
import com.etouch.taf.webui.selenium.WebPage;

@RunWith(MockitoJUnitRunner.class)
@Ignore
public class IOSHybridAppUnit {
	
	
	private static Log log = LogUtil.getLog(IOSHybridAppUnit.class);
	
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
		Mockito.when(mockMap.get("testBedName")).thenReturn("iOSHybrid");
		
			
		//String[] currentTestbeds = TestBedManagerConfiguration.INSTANCE.getMobileConfig().getCurrentTestBeds();
		String currentTestBedName = prop.getProperty("ios_sim_hybrid");
		//TestBed currentTestbed = TafTestUtil.loadTestBedDetails(currentTestBed);
		//TestBedManager.INSTANCE.setCurrentTestBed(currentTestbed);		
		TestBedManager.INSTANCE.getCurrentTestBeds().get(currentTestBedName);	
		
	}
	
	
	//appPath: /Users/etouch/Documents/Mobile_Testing/FoxBigBoardApp/Bigboard.app	 
    //bundleId: com.foxsports.Bigboard
	
	//start activity in mobile element
	
	@Test
	public void testClick() throws Exception {
		
		
		Mockito.when(mockContext.getCurrentXmlTest().getAllParameters().get("testBedName")).thenReturn("iOSHybrid");
		

		mobileView = new MobileView(mockContext);
		driverObj=(IOSDriver)mobileView.getDriver();
		
		//Find Chocolate Cake recipe
		ITafElement element = mobileView.findObjectByxPath(prop.getProperty("bigboard_skip_for_now_text"));
		
		//Click on it
		element.click();
		
		Thread.sleep(3000);
		
		//Find Chocolate Cake recipe header
		element = mobileView.findObjectByxPath(prop.getProperty("bigboard_dont_allow_location_button"));
		
		//Assert on it
		Assert.assertEquals("Don’t Allow", element.getText());
		
		TestUtil.closeMobileDriver(driverObj);
		
	}
	
	
	@Test
	public void testTap() throws Exception
	{
		mobileView = new MobileView(mockContext);
		driverObj=(IOSDriver)mobileView.getDriver();
		//Find skip for now text
		ITafElement element = mobileView.findObjectByxPath(prop.getProperty("bigboard_skip_for_now_text"));
		
		//Click on it
		element.tap(1, 2);
		
		Thread.sleep(3000);
		
		//Find don't allow button
		element = mobileView.findObjectByxPath(prop.getProperty("bigboard_dont_allow_location_button"));
		
		//Assert on it
		Assert.assertEquals("Don’t Allow", element.getText());
		
		TestUtil.closeMobileDriver(driverObj);
		
	}
	
	@Test
	public void testSelectDropDown() throws Exception
	{
		mobileView = new MobileView(mockContext);
		driverObj=(IOSDriver)mobileView.getDriver();
		
		goToApp();
		
		Thread.sleep(3000);
		
		addNFLTeams();
		
		addMLBTeamsForWebView();
		
		addMyRivals();
		
		Thread.sleep(5000);
		
		ITafElement element = mobileView.findObjectByxPath(prop.getProperty("bigboard_scores_dropdown_button"));
		element.tap(1,2);
		
		element = mobileView.findObjectByName("NBA");
		element.tap(1, 2);
		
		Thread.sleep(2000);
		element = mobileView.findObjectByxPath(prop.getProperty("bigboard_scores_myteams_text"));
		Assert.assertEquals("NBA", element.getText());
		
	}
	
	@Test
	public void testScroll() throws Exception
	{
		mobileView = new MobileView(mockContext);
		driverObj=(IOSDriver)mobileView.getDriver();
		
		goToApp();
		
		Thread.sleep(3000);
		
		addNFLTeams();
		
		//assert on my teams.
		ITafElement element = mobileView.findObjectByxPath(prop.getProperty("bigboard_myteams_buffalo_bills_text"));
		Assert.assertEquals("Buffalo Bills", element.getText());
		
		//assert on my teams.
		element = mobileView.findObjectByxPath(prop.getProperty("bigboard_myteams_sandiego_chargers_text"));
		Assert.assertEquals("San Diego Chargers", element.getText());
		
		TestUtil.closeMobileDriver(driverObj);
		
		
	}
	
	@Test
	public void testScrollTo() throws Exception
	{

		mobileView = new MobileView(mockContext);
		driverObj=(IOSDriver)mobileView.getDriver();
		
		goToApp();
		
		Thread.sleep(3000);
		
		addMLBTeams();
		
		//assert on my teams.
		ITafElement element = mobileView.findObjectByxPath(prop.getProperty("bigboard_myteams_mlb_chicago_cubs_text"));
		Assert.assertEquals("Chicago Cubs", element.getText());
		
		//assert on my teams.
		element = mobileView.findObjectByxPath(prop.getProperty("bigboard_myteams_mlb_seattle_mariners_text"));
		Assert.assertEquals("Seattle Mariners", element.getText());
		
		TestUtil.closeMobileDriver(driverObj);
		
		
	}
	
	@Test
	public void testWebNavigation() throws Exception
	{
		mobileView = new MobileView(mockContext);
		driverObj=(IOSDriver)mobileView.getDriver();
		
		goToApp();
		
		Thread.sleep(3000);
		
		addNFLTeams();
		
		addMLBTeamsForWebView();
		
		addMyRivals();
		
		Thread.sleep(5000);
		
		navigteToWebView();
		
		Thread.sleep(5000);
		
		navigateBackToApp();
		
		TestUtil.closeMobileDriver(driverObj);
		
		
		
	}

	@Test
	public void testSwipeRightAndLeft() throws Exception
	{
		mobileView = new MobileView(mockContext);
		driverObj=(IOSDriver)mobileView.getDriver();
		
		goToApp();
		
		Thread.sleep(3000);
		
		addNFLTeams();
		
		addMLBTeamsForWebView();
		
		addMyRivals();
		
		Thread.sleep(5000);
		
		//Swipe the screen to the right using mobile element swipe method and assert on an element
		ITafElement element = mobileView.findObjectByxPath(prop.getProperty("bigboard_scores_view_xpath"));
		element.swipe(SwipeElementDirection.RIGHT, 1000);
		Thread.sleep(1000);
		element = mobileView.findObjectByxPath(prop.getProperty("bigboard_thebigboard_text"));
		Assert.assertEquals("THE BIG BOARD", element.getText());
		
		
		//Swipe the screen to the left using mobile element swipe method and assert on an element
		element = mobileView.findObjectByxPath(prop.getProperty("bigboard_scores_view_second_xpath")) ;
		element.swipe(SwipeElementDirection.LEFT, 10000);
		Thread.sleep(1000);
		element = mobileView.findObjectByxPath(prop.getProperty("bigboard_scores_share_button"));
		Assert.assertEquals("SHARE", element.getText());
		
		//Swipe the screen to the right using mobile view swipe method and assert on an element
		mobileView.swipeRight();
		Thread.sleep(1000);
		element = mobileView.findObjectByxPath(prop.getProperty("bigboard_thebigboard_text"));
		Assert.assertEquals("THE BIG BOARD", element.getText());
		
		//Swipe the screen to the left using mobile view swipe method and assert on an element
		mobileView.swipeLeft();
		element = mobileView.findObjectByxPath(prop.getProperty("bigboard_scores_share_button"));
		Assert.assertEquals("SHARE", element.getText());
		
		TestUtil.closeMobileDriver(driverObj);
	
	}
	
	@Test
	public void testPinch() throws Exception
	{
		mobileView = new MobileView(mockContext);
		driverObj=(IOSDriver)mobileView.getDriver();
		
		goToApp();
		Thread.sleep(1000);
		addNFLTeams();
		addMLBTeamsForWebView();
		addMyRivals();
		
		Thread.sleep(5000);
		
		mobileView.pinch(50, 50);
		
		Thread.sleep(3000);
		
		TestUtil.closeMobileDriver(driverObj);
		
		
	}
	
	@Test
	public void testZoom() throws Exception
	{
		mobileView = new MobileView(mockContext);
		driverObj=(IOSDriver)mobileView.getDriver();
		
		goToApp();
		Thread.sleep(1000);
		addNFLTeams();
		addMLBTeamsForWebView();
		addMyRivals();
		
		Thread.sleep(5000);
		mobileView.zoom(50, 50);
		
		Thread.sleep(3000);
		
		TestUtil.closeMobileDriver(driverObj);
		
		
	}
	
	@Test
	public void testScrollToRightAndLeft() throws Exception
	{
		mobileView = new MobileView(mockContext);
		driverObj=(IOSDriver)mobileView.getDriver();
		
		goToApp();
		Thread.sleep(1000);
		addNFLTeams();
		addMLBTeamsForWebView();
		addMyRivals();
		
		Thread.sleep(5000);
		
		ITafElement element = mobileView.findObjectByxPath(prop.getProperty("bigboard_scores_swipe_element_xpath"));
		//element.swipe(SwipeElementDirection.RIGHT, 1000);
		//element.scroll(mobileView.RIGHT);
		
		element = mobileView.findObjectByxPath(prop.getProperty("bigboard_scores_scrollable_view_xpath"));
		element.swipe(SwipeElementDirection.RIGHT, 1000);
		
		Thread.sleep(3000);
		
		element.swipe(SwipeElementDirection.LEFT,1000);
		
		Thread.sleep(3000);
	
	}
	
	public void goToApp() throws PageException, InterruptedException
	{
		//Find skip for now text
		ITafElement element = mobileView.findObjectByxPath(prop.getProperty("bigboard_skip_for_now_text"));
		
		//tap on it
		element.tap(1, 2);
		
		Thread.sleep(5000);
		
		//Find don't allow button
		element = mobileView.findObjectByxPath(prop.getProperty("bigboard_dont_allow_location_button"));
		element.tap(1, 2);
	}
	
	
	public void addNFLTeams() throws PageException, InterruptedException
	{
		//Find NFL team
		ITafElement element = mobileView.findObjectByxPath(prop.getProperty("bigboard_pick_my_teams_NFL_text"));
		element.tap(1, 2);
		
		Thread.sleep(3000);
		
		//scroll down
		//
		element = mobileView.findObjectByxPath(prop.getProperty("bigboard_application_view_xpath"));
		element.scroll(mobileView.DOWN);
		//mobileView.scrollTo("San Diego Chargers");
		
		//choose sandiego chargers
		element = mobileView.findObjectByxPath(prop.getProperty("bigboard_NFL_sandiego_chargers_text"));
		element.tap(1, 2);
		
		//scroll up
		element = mobileView.findObjectByxPath(prop.getProperty("bigboard_application_view_xpath"));
		element.scroll(mobileView.UP);
		
		//choose sandiego chargers
		element = mobileView.findObjectByxPath(prop.getProperty("bigboard_NFL_buffalo_bills_text"));
		element.tap(1, 2);
		
		//find back button
		element = mobileView.findObjectByxPath(prop.getProperty("bigboard_back_button"));
		element.tap(1, 2);
	}
	
	public void addMLBTeams() throws PageException, InterruptedException
	{
		//Find MLB team
		ITafElement element = mobileView.findObjectByxPath(prop.getProperty("bigboard_pick_my_teams_MLB_text"));
		element.tap(1, 2);
		
		Thread.sleep(3000);
		
		//scroll to Seattle Mariners
		mobileView.scrollTo("Seattle");
		
		//choose Seattle Mariners
		element = mobileView.findObjectByxPath(prop.getProperty("bigboard_mlb_seattle_mariners_text"));
		element.tap(1, 2);
		
		//scroll up
		mobileView.scrollToExact("Chicago Cubs");
		
		//choose Chicago Cubs
		element = mobileView.findObjectByxPath(prop.getProperty("bigboard_mlb_chicago_cubs_text"));
		element.tap(1, 2);
		
		//go back
		element = mobileView.findObjectByxPath(prop.getProperty("bigboard_back_button"));
		element.tap(1, 2);
	}
	
	public void addMLBTeamsForWebView() throws PageException, InterruptedException
	{
		//Find MLB team
		//ITafElement element = mobileView.findObjectByxPath(prop.getProperty("bigboard_myteams_mlb_second_xpath"));
		//div[contains(text(), "' + text + '")]'
		//ITafElement element = mobileView.findObjectByxPath("//UIAApplication[1]/UIAWindow[1]/UIATableView[1][@Text, 'MLB']");
		
		Thread.sleep(1000);
		
		ITafElement element =  mobileView.findObjectByName("MLB");
		element.tap(1, 2);
		
		Thread.sleep(3000);
		
		//scroll to Seattle Mariners
		mobileView.scrollTo("Seattle");
		
		//choose Seattle Mariners
		element = mobileView.findObjectByxPath(prop.getProperty("bigboard_mlb_seattle_mariners_text"));
		element.tap(1, 2);
		
		//scroll up
		//mobileView.scrollToExact("Chicago Cubs");
		mobileView.scrollTo("Chicago Cubs");
		
		//choose Chicago Cubs
		element = mobileView.findObjectByxPath(prop.getProperty("bigboard_mlb_chicago_cubs_text"));
		element.tap(1, 2);
		
		//go back
		element = mobileView.findObjectByxPath(prop.getProperty("bigboard_back_button"));
		element.tap(1, 2);
	}
	
	public void addMyRivals() throws PageException, InterruptedException
	{
		//click on NEXT button after adding my teams
		ITafElement element = mobileView.findObjectByxPath(prop.getProperty("bigboard_next_button"));
		element.tap(1, 2);
		
		Thread.sleep(1000);
		
		//switch on the toggle button
		element = mobileView.findObjectByxPath(prop.getProperty("bigboard_show_rivals_switch"));
		element.tap(1, 2);
		
		Thread.sleep(2000);
		
		//pick rivals.
		element = mobileView.findObjectByName("Oakland Raiders");
		element.tap(1, 2);
		
		element = mobileView.findObjectByName("Texas Rangers");
		element.tap(1, 2);
		
		//assert on my rivals
		
		element = mobileView.findObjectByxPath(prop.getProperty("bigboard_myrivals_oakland_riders_text"));
		Assert.assertEquals("Oakland Raiders", element.getText());
		
		element = mobileView.findObjectByxPath(prop.getProperty("bigboard_myrivals_texasrangers_text"));
		Assert.assertEquals("Texas Rangers", element.getText());
		
		//tap on DONE
		element = mobileView.findObjectByxPath(prop.getProperty("bigboard_done_button"));
		element.tap(1, 2);
	
	}
	
	public void navigteToWebView() throws PageException, InterruptedException
	{
		//tap on Recap button
		//ITafElement element = mobileView.findObjectByxPath(prop.getProperty("bigboard_myteams_recap_button"));
		ITafElement element = mobileView.findObjectByName("RECAP");
		element.tap(1, 2);
		
		Thread.sleep(7000);
		
		//wd.context("WEBVIEW_1");
		//mobileView.getDriver().context("WEBVIEW_1");
		//mobileView.getDriver().switchTo().window("WEBVIEW_1");
		
		element = mobileView.findObjectByxPath("//UIAApplication[1]/UIAWindow[1]/UIAScrollView[1]/UIAWebView[1]/UIALink[3]/UIALink[1]/UIAStaticText[1]");
		Assert.assertEquals("NFL", element.getText());
		
		//Click on facebook link
		element = mobileView.findObjectByxPath(prop.getProperty("bigboard_recap_facebook_link"));
		//element = mobileView.findObjectByLink("f");
		element.tap(1, 2);
		
		Thread.sleep(5000);
		
		//Assert on facebook text
		element = mobileView.findObjectByxPath(prop.getProperty("bigboard_webview_facebook_text"));
		Assert.assertEquals("facebook", element.getText());
		
		Thread.sleep(5000);
		
		//click on help center
		element = mobileView.findObjectByxPath(prop.getProperty("bigboard_webview_facebook_help_center"));
		element.click();
		
		//click on help question 4
		element = mobileView.findObjectByxPath(prop.getProperty("bigboard_webview_help_question"));
		element.tap(1, 2);;
		
		//click on Community Standards
		element = mobileView.findObjectByxPath(prop.getProperty("bigboard_webview_help_community_standards"));
		element.tap(1, 2);
		
		Thread.sleep(5000);
		
		//Assert on Community Text
		element = mobileView.findObjectByxPath(prop.getProperty("bigboard_webview_help_community_standards_text"));
		Assert.assertEquals("Community Standards", element.getText());
		
		element = mobileView.findObjectByxPath(prop.getProperty("bigboard_webview_xpath"));
		
		element.scroll(mobileView.DOWN);
		//element.pinch();
		//element.zoom();
		element.scroll(mobileView.UP);
	}
	
	public void navigateBackToApp() throws PageException, InterruptedException
	{
		//navigate back
		mobileView.navigateBack();
		mobileView.navigateBack();
		
		//Close recap window
		ITafElement element = mobileView.findObjectByxPath(prop.getProperty("bigboard_webview_recap_close_button"));
		element.tap(1, 2);
		
		//Assert on MyTeams Text
		element = mobileView.findObjectByxPath(prop.getProperty("bigboard_scores_myteams_text"));
		Assert.assertEquals("MY TEAMS", element.getText());
		
		
	}

	
	//Page method test cases
	@Test
	public void testGetOrientation() throws Exception
	{
		mobileView = new MobileView(mockContext);
		driverObj=(IOSDriver)mobileView.getDriver();
		ScreenOrientation screenOrientation = mobileView.getOrientation();
		Assert.assertEquals(ScreenOrientation.PORTRAIT.name(), screenOrientation.name());
		
		TestUtil.closeMobileDriver(driverObj);
	}
	
	@Test
	public void testRotate() throws Exception
	{
		mobileView = new MobileView(mockContext);
		driverObj=(IOSDriver)mobileView.getDriver();
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
		
		TestUtil.closeMobileDriver(driverObj);
	}
	
	@Test
	public void testLockScreen() throws Exception
	{
		mobileView = new MobileView(mockContext);
		driverObj=(IOSDriver)mobileView.getDriver();
		
		mobileView.rotate(ScreenOrientation.LANDSCAPE);
		Thread.sleep(1000);
		mobileView.lockScreen(5);	
		mobileView.rotate(ScreenOrientation.PORTRAIT);
		Thread.sleep(1000);
		Assert.assertEquals(ScreenOrientation.PORTRAIT.name(), mobileView.getOrientation().name());
		
		TestUtil.closeMobileDriver(driverObj);
	}



}
