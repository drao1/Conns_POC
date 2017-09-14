/*
 * 
 */
package com.etouch.common;

/*import org.apache.commons.logging.Log;
import org.monte.screenrecorder.ScreenRecorder;
import org.testng.ITestContext;

import com.etouch.taf.core.TestBed;
import com.etouch.taf.core.TestBedManager;
import com.etouch.taf.core.config.TestBedManagerConfiguration;
import com.etouch.taf.core.datamanager.excel.TestParameters;
import com.etouch.taf.core.driver.web.WebDriver;
import com.etouch.taf.tools.defect.IDefectManager;
import com.etouch.taf.tools.rally.VideoRecorder;
import com.etouch.taf.util.CommonUtil;
import com.etouch.taf.util.LogUtil;
import com.etouch.taf.util.SoftAssertor;
import com.etouch.taf.webui.selenium.MobileView;
import com.etouch.taf.webui.selenium.WebPage;
import com.etouch.taf.core.datamanager.excel.TestParameters;
*/

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.monte.screenrecorder.ScreenRecorder;
import org.openqa.selenium.By;
//import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.Color;
import org.testng.ITestContext;

import com.etouch.taf.core.TestBed;
import com.etouch.taf.core.TestBedManager;
import com.etouch.taf.core.config.TestBedManagerConfiguration;
import com.etouch.taf.core.datamanager.excel.TestParameters;
import com.etouch.taf.core.exception.PageException;
import com.etouch.taf.tools.defect.IDefectManager;
import com.etouch.taf.tools.rally.VideoRecorder;
import com.etouch.taf.util.CommonUtil;
import com.etouch.taf.util.LogUtil;
import com.etouch.taf.util.SoftAssertor;
import com.etouch.taf.util.TafPassword;
import com.etouch.taf.util.TestUtil;
import com.etouch.taf.webui.selenium.MobileView;
import com.etouch.taf.webui.selenium.WebPage;



/**
 * The Class CommonPage.
 *
 * @author eTouch Systems Corporation
 */
public abstract class CommonPage {

	/** The page url. */
	protected String pageUrl;

	/** The web page. */
	protected WebPage webPage;
	protected MobileView mobileView;

	/** The err message. */
	protected String errMessage;
	// required for screen recorder
	/** The screen recorder. */
	private ScreenRecorder screenRecorder;

	/** The log. */
	private static Log log = LogUtil.getLog(CommonPage.class);
	
	protected VideoRecorder videoRecorder = null;

	/**
	 * Instantiates a new common page.
	 */
	public CommonPage() {
		videoRecorder=new VideoRecorder(TestBedManagerConfiguration.INSTANCE.getVideoConfig().getVideoFileName(), TestBedManagerConfiguration.INSTANCE.getVideoConfig().getVideoPath());

	}

	/**
	 * Instantiates a new common page.
	 *
	 * @param sbPageUrl
	 *            the sb page url
	 * @param webPage
	 *            the web page
	 */
	public CommonPage(String sbPageUrl, WebPage webPage) {
		this.pageUrl = sbPageUrl;
		this.webPage = webPage;
		
		videoRecorder=new VideoRecorder(TestBedManagerConfiguration.INSTANCE.getVideoConfig().getVideoFileName(), TestBedManagerConfiguration.INSTANCE.getVideoConfig().getVideoPath());

	}
	public CommonPage(String sbPageUrl, MobileView mobileView) {
		this.pageUrl = sbPageUrl;
		this.mobileView = mobileView;
		
		videoRecorder=new VideoRecorder(TestBedManagerConfiguration.INSTANCE.getVideoConfig().getVideoFileName(), TestBedManagerConfiguration.INSTANCE.getVideoConfig().getVideoPath());

	}
	/**
	 * Gets the err message.
	 *
	 * @return the err message
	 */
	public String getErrMessage() {
		CommonUtil.sop("Error Message + " + errMessage);
		return errMessage;
	}

	
	public VideoRecorder getVideoRecorder(){
		return videoRecorder;
	}
	/**
	 * Sets the err message.
	 *
	 * @param errMessage
	 *            the new err message
	 */
	public void setErrMessage(String errMessage) {
		this.errMessage = errMessage;
	}

	public void setVideoRecorder(VideoRecorder videoRecorder)
	{
		this.videoRecorder=videoRecorder;
	}
	public void logDefect(TestParameters inputs, String errMsg, String testBedName)
	{
		
	}
	
	public void cleanUp(WebDriver driver, ITestContext context, TestParameters inputs)
	{
		String testBedName=context.getCurrentXmlTest().getAllParameters().get("testBedName");
		TestBed testBed=TestBedManager.INSTANCE.getCurrentTestBeds().get(testBedName);
		
		String errors=SoftAssertor.readErrorsForTest();
		try{
			if(errors!=null && errors.length()>0)
				logDefect(inputs, errors, testBedName);
			else
				videoRecorder.deleteRecording();
		}catch(Exception ex)
		{
			log.error(ex.getMessage());
			ex.printStackTrace();
		}
		SoftAssertor.displayErrors();
	}
	/**
	 * Sets the err message.
	 *
	 * @param errorType
	 *            the error type
	 * @param pageElement
	 *            the page element
	 * @param pageUrl
	 *            the page url
	 * @param expectedResult
	 *            the expected result
	 * @param actualResult
	 *            the actual result
	 * @param messageStr
	 *            the message str
	 */
	// TODO: ...taf.core.logging
	// commented for safari test
	public void setErrMessage(String errorType, String pageElement, String pageUrl, String expectedResult,
			String actualResult, String messageStr) {

		CommonUtil.sop("\n\n\n!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!\nPage Exception in ????????????????????????? ::"
				+ Thread.currentThread().getStackTrace()[2].getMethodName()
				+ "\n!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!\n\n\n");
		String message = "";
		try {
			if (errorType != null) {
				message += "\n # Error Type: " + errorType;
			}
			if (pageElement != null) {
				message += "\n # Page Element: " + pageElement;
			}
			if (pageUrl != null) {
				message += "\n # Page URL: " + pageUrl;
			}
			if (expectedResult != null) {
				message += "\n # Expected Result: " + expectedResult;
			}
			if (actualResult != null) {
				message += "\n # Actual Result: " + actualResult;
			}
			if (messageStr != null) {
				message += "\n # Message: " + messageStr;
			}
			message += "";
		} catch (Exception ex) {
			message = "An error occured while setting Error Message: " + ex.toString();
		} finally {
			this.errMessage = message;
		}
		CommonUtil.sop("Got Page Exception:-" + errMessage);

	}

	/**
	 * Load page.
	 */
	protected void loadPage() {
		System.out.println("PAGE URL" + pageUrl);
		webPage.loadPage(pageUrl);

	}

	/*
	 * Log error and create/add a defect
	 */
	/**
	 * Log and create a defect.
	 *
	 * @param defect
	 *            the defect
	 * @param fileName
	 *            the file name
	 * @param testcaseId
	 *            the testcase id
	 * @param workspaceId
	 *            the workspace id
	 * @param projId
	 *            the proj id
	 * @param storyId
	 *            the story id
	 * @param defectName
	 *            the defect name
	 * @param defectSeverity
	 *            the defect severity
	 * @param defectOwner
	 *            the defect owner
	 * @param defectNotes
	 *            the defect notes
	 * @param errorMsg
	 *            the error msg
	 */
	public void logAndCreateADefect(IDefectManager defect, String fileName, String testcaseId, String workspaceId,
			String projId, String storyId, String defectName, String defectSeverity, String defectOwner,
			String defectNotes, String errorMsg, String attachmentPath) {
		try {
			String defectNote = defectNotes + " : " + errorMsg;
			// to create defect and add attatchment
			defect.createDefectBuilder(defectName, testcaseId, workspaceId, projId, defectSeverity, defectOwner,
					defectNote, storyId, attachmentPath);

			System.out.println("Defect Logged ");
			// added to update testcase result

			defect.updateTestCaseResult(defectName, testcaseId, workspaceId, projId, defectSeverity, defectOwner,
					defectNotes, storyId);
			System.out.println("Testcase Results updated ");

		} catch (Exception e) {
			System.out.println("exception in common page log method");

		}
	}

	// log and defect method for Jira defect --added by sonam
	/**
	 * Log and create a defect.
	 *
	 * @param defect
	 *            the defect
	 * @param url
	 *            the url
	 * @param issueUrl
	 *            the issue url
	 * @param username
	 *            the username
	 * @param password
	 *            the password
	 * @param keys
	 *            the keys
	 */
	public void logAndCreateADefect(TestParameters inputs, String errMsg, IDefectManager defect, String url,
			String issueUrl, String username, String password, String keys) {

		if (defect != null) {

			System.out.println("Executing the defect in Jira");

			defect.createAJiraDefectBuilder(inputs, errMsg, url, issueUrl, username, password, keys);

		}
	}
	
	public List<String> getInfo(String var_Heading_Css, String var_Text_ObjType, String var_IsAltPresent,
			String var_IsTitlePresent) throws PageException, InterruptedException {
		List<String> Profile = new ArrayList<String>();

		List<WebElement> Heading = webPage.getDriver().findElements(By.cssSelector(var_Heading_Css));
		int index = 0;
		while (index < (Heading.size())) {
			Profile.addAll(
					getProperties_v1(Heading.get(index), var_Text_ObjType, var_IsAltPresent, var_IsTitlePresent));
			index++;
		}
		// System.out.println("Profile1 : " + Profile);

		return Profile;
	}

	// Get Text properties
	public List<String> getProperties_v1(WebElement cssSelector, String ObjType, String IsAltPresent,
			String var_IsTitlePresent) throws PageException {
		List<String> Properties = new ArrayList<String>();
		if (ObjType.equalsIgnoreCase("Text") || ObjType.equalsIgnoreCase("Link")
				|| ObjType.equalsIgnoreCase("BgImage")) {
			Properties.add(cssSelector.getText());
			Properties.add(cssSelector.getCssValue("font-size"));
			Properties.add(cssSelector.getCssValue("font-weight"));
			Properties.add(Color.fromString(cssSelector.getCssValue("color")).asHex());
			Properties.add(cssSelector.getCssValue("font-family"));
		}
		if (ObjType.equalsIgnoreCase("Link") || ObjType.equalsIgnoreCase("Icon")) {
			Properties.add(cssSelector.getAttribute("href"));
			if (var_IsTitlePresent.equalsIgnoreCase("Yes")) {
				Properties.add(cssSelector.getAttribute("title"));
			}
		}
		if (ObjType.equalsIgnoreCase("Image")) {
			Properties.add(cssSelector.getAttribute("src"));
			if (IsAltPresent.equalsIgnoreCase("Yes")) {
				Properties.add(cssSelector.getAttribute("alt"));
			}
		}
		if (ObjType.equalsIgnoreCase("BgImage")) {
			Properties.add(cssSelector.getCssValue("background-image"));
		}
		if (ObjType.equalsIgnoreCase("Background")) {
			Properties.add(Color.fromString(cssSelector.getCssValue("background-color")).asHex());
		}
		return Properties;
	}

	public List<String> getExpectedInfo(String exp_Heading1_CRM, String testBedName) {
		List<String> Heading1 = null;
		Heading1 = TextSplit(exp_Heading1_CRM);

		List<String> ExpectedInfo = new ArrayList<String>();
		ExpectedInfo.addAll(Heading1);

		return ExpectedInfo;
	}

	public List<String> TextSplit(String inputs) {
		List<String> list1 = new ArrayList<String>();
		String[] TextSplit = null;
		TextSplit = inputs.split("/::/");
		int i = 0;
		System.out.println("TextSplit.length : " + TextSplit.length);
		while (i < TextSplit.length) {
			list1.add(TextSplit[i]);
			i++;
		}
		System.out.println(list1);
		return list1;
	}

	public void verifyGivenValues(List<String> directors, List<String> Expected_Profile, String message) {
		for (int i = 0; i < directors.size(); i++) {
			int j = i + 1;
			log.info("Iteration getting executed : " + j);
			SoftAssertor.assertEquals(directors.get(i), Expected_Profile.get(i), message + " is Not matched");
		}
	}

	///////// to start and stop video recording // -- added by sonam

	/* *//**
			 * Start recording.
			 */
	/*
	 * public void startRecording() { String videoFilePath =
	 * "..\\AmazonPOC\\src\\test\\resources\\testdata\\videos\\"; File
	 * videoFolder = new File(videoFilePath); try{ GraphicsConfiguration gc =
	 * GraphicsEnvironment .getLocalGraphicsEnvironment()
	 * .getDefaultScreenDevice() .getDefaultConfiguration();
	 * 
	 * this.screenRecorder = new ScreenRecorder(gc, new Format(MediaTypeKey,
	 * MediaType.FILE, MimeTypeKey, MIME_AVI), new Format(MediaTypeKey,
	 * MediaType.VIDEO, EncodingKey, ENCODING_AVI_TECHSMITH_SCREEN_CAPTURE,
	 * CompressorNameKey, ENCODING_AVI_TECHSMITH_SCREEN_CAPTURE, DepthKey, 24,
	 * FrameRateKey, Rational.valueOf(15), QualityKey, 1.0f,
	 * KeyFrameIntervalKey, 15 * 60), new Format(MediaTypeKey, MediaType.VIDEO,
	 * EncodingKey, "black", FrameRateKey, Rational.valueOf(30)), null);
	 * 
	 * this.screenRecorder = new ScreenRecorder(gc,null, new
	 * Format(MediaTypeKey, MediaType.FILE, MimeTypeKey, MIME_AVI), new
	 * Format(MediaTypeKey, MediaType.VIDEO, EncodingKey,
	 * ENCODING_AVI_TECHSMITH_SCREEN_CAPTURE, CompressorNameKey,
	 * ENCODING_AVI_TECHSMITH_SCREEN_CAPTURE, DepthKey, 24, FrameRateKey,
	 * Rational.valueOf(15), QualityKey, 1.0f, KeyFrameIntervalKey, 15 * 60),
	 * new Format(MediaTypeKey, MediaType.VIDEO, EncodingKey, "black",
	 * FrameRateKey, Rational.valueOf(30)), null,videoFolder);
	 * 
	 * 
	 * this.screenRecorder.start();
	 * 
	 * }catch(IOException e){ e.printStackTrace(); }catch(AWTException e){
	 * e.printStackTrace(); } }
	 * 
	 *//**
		 * Stop recording.
		 *//*
		 * public void stopRecording() { try{ this.screenRecorder.stop();
		 * }catch(Exception e){ e.printStackTrace(); } }
		 */

	///////////////// Screenshot //////// -- added by sonam
	/*       
	       *//**
				 * Gets the screenshot.
				 *
				 * @return the screenshot
				 *//*
				 * public void getScreenshot() { try{ // Thread.sleep(10000);
				 * BufferedImage image = new Robot().createScreenCapture(new
				 * Rectangle(Toolkit.getDefaultToolkit().getScreenSize()));
				 * ImageIO.write(image, "jpg", new File(
				 * "..\\AmazonPOC\\src\\test\\resources\\testdata\\screenshots\\sample2.png"
				 * ));
				 * 
				 * }catch(IOException e){ e.printStackTrace();
				 * }catch(AWTException e){ e.printStackTrace(); } }
				 */

}
