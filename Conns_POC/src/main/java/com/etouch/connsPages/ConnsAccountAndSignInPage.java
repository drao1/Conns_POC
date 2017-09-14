package com.etouch.connsPages;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import org.apache.commons.logging.Log;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.testng.Assert;

import com.etouch.common.CommonPage;
import com.etouch.taf.core.exception.PageException;
import com.etouch.taf.util.CommonUtil;
import com.etouch.taf.util.LogUtil;
import com.etouch.taf.util.SoftAssertor;
import com.etouch.taf.webui.selenium.WebPage;

public class ConnsAccountAndSignInPage extends CommonPage {

	/**
	 *
	 * @param sbPageUrl
	 *            the sb page url
	 * @param webPage
	 *            the web page
	 */

	
	static Log log = LogUtil.getLog(ConnsAccountAndSignInPage.class);
	String testType;
	String testBedName;

	public ConnsAccountAndSignInPage(String sbPageUrl, WebPage webPage) {
		super(sbPageUrl, webPage);
		log.info("webDriver in Conns Page : " + webPage.getDriver());
		loadPage();
	}

	public void verifyPageTitle(String[][] testdata) {
		String expurl = testdata[0][0];
		String expTitle = testdata[0][1];

		try {
			log.info("Actual URL of the page is : " + webPage.getCurrentUrl());
			log.info("Actual Title of the page is : " + webPage.getPageTitle());

			SoftAssertor.assertTrue(webPage.getCurrentUrl().contains(expurl));
			SoftAssertor.assertEquals(expTitle, webPage.getPageTitle());

		} catch (Exception e) {
			SoftAssertor.addVerificationFailure(e.getMessage());
			log.error("verifyPageTitle failed");
			log.error(e.getMessage());
		}
	}

	public void verifyLoginFunctionality(String[][] testdata) {
		String EmailAddressLocator = testdata[0][0];
		String EmailAddress = testdata[0][1];
		String PasswordLocator = testdata[0][2];
		String Password = testdata[0][3];
		String LogInButtonLocator = testdata[0][4];
		String TitleOfPage = testdata[0][5];
		try {
			webPage.findObjectByxPath(EmailAddressLocator).sendKeys(EmailAddress);
			webPage.findObjectByxPath(PasswordLocator).sendKeys(Password);
			webPage.findObjectByxPath(LogInButtonLocator).click();

			SoftAssertor.assertEquals(webPage.getPageTitle(), TitleOfPage, "Title of the page does not match");

		} catch (Throwable e) {
			SoftAssertor.addVerificationFailure(e.getMessage());
			log.error("Login failed");
			log.error(e.getMessage());

		}

	}

	public void verifyContent(String[][] testdata) {
		List <String> content = new ArrayList<String>();
		for (int r = 0; r < testdata.length; r++) {
			try {

				String ActualElementName = webPage.findObjectByxPath(testdata[r][0]).getText();
				String ExpectedElementName = testdata[r][1];
				Assert.assertEquals(ActualElementName, ExpectedElementName, "content does not Match");
				log.info("Expected Content :" + testdata[r][1]);
				log.info("Actual Content :" + ActualElementName);
			} catch (Throwable e) {
				//continue;
				content.add(testdata[r][1] + " " + e.getLocalizedMessage());
				//SoftAssertor.addVerificationFailure(e.getMessage());
			/*	log.error("content verification failed");
				log.error(e.getMessage());*/
			}

		}
		if (content.size() > 0) {
			Assert.fail("Content " + Arrays.deepToString(content.toArray()) + " are not working as expected");
		}

	}

	public void verifyLinks(String[][] testdata) {

		List<String> brokenLinks = new ArrayList<String>();
		String ParentElementLocator = null;
		String ChildElementLocator = null;
		String ExpectedURL = null;
		String ExpectedElementName = null;
		String ElementTitle = null;
		String actualUrl = null;
		String ActualElementName = null;

		for (int r = 0; r < testdata.length; r++) {
			try {
				log.info("Verifying " + testdata[r][0]);
				ParentElementLocator = testdata[r][1];
				ChildElementLocator = testdata[r][2];
				ExpectedURL = testdata[r][3];
				ExpectedElementName = testdata[r][4];
				ElementTitle = testdata[r][5];
				log.info("Parent Locator is ..." + ParentElementLocator);
				
				if (!(ParentElementLocator.equalsIgnoreCase("NA"))) {
					webPage.hoverOnElement(By.xpath(testdata[r][0]));

				}
				ActualElementName = webPage.findObjectByxPath(ChildElementLocator).getText();
				SoftAssertor.assertEquals(ActualElementName, ExpectedElementName, "Element name does not match");
				webPage.findObjectByxPath(ChildElementLocator).click();
				String existingWindow = null;
				String newWindow = null;
				existingWindow = webPage.getDriver().getWindowHandle();
				Set<String> windows = webPage.getDriver().getWindowHandles();
				if (windows.size() >= 2) {
					windows.remove(existingWindow);
					newWindow = windows.iterator().next();
					log.info("Existing window id is" + existingWindow);
					log.info("New window id is" + newWindow);

					webPage.getDriver().switchTo().window(newWindow);
					Thread.sleep(3000);
					actualUrl = webPage.getCurrentUrl();
					webPage.getDriver().close();
					webPage.getDriver().switchTo().window(existingWindow);
					log.info("Expected URL" + ExpectedURL);
					log.info("Actual URL" + actualUrl);
					SoftAssertor.assertTrue(actualUrl.contains(ExpectedURL));
					SoftAssertor.assertEquals(webPage.getPageTitle(), ElementTitle, "Title of the page does not match");

				} else {
					actualUrl = webPage.getCurrentUrl();
					log.info("Expected URL" + ExpectedURL);
					log.info("Actual URL" + actualUrl);
					try {
						SoftAssertor.assertTrue(actualUrl.contains(ExpectedURL));
						SoftAssertor.assertEquals(webPage.getPageTitle(), ElementTitle,
								"Title of the page does not match");
						if (!ExpectedElementName.equalsIgnoreCase("« Go back")
								&& !ExpectedElementName.equalsIgnoreCase("SAVE ADDRESS")
								&& !ExpectedElementName.equalsIgnoreCase("Newsletter Subscription")) {
							webPage.getDriver().navigate().back();
						}
						
					} catch (Exception e) {
						webPage.getDriver().navigate().back();
					}
				}

			} catch (Exception e) {
				webPage.getDriver().navigate().back();
				brokenLinks.add(ExpectedElementName + " " + e.getLocalizedMessage());
				

			}

		}
		if (brokenLinks.size() > 0) {
			Assert.fail("Link " + Arrays.deepToString(brokenLinks.toArray()) + " are not working as expected");
		}

	}

	public void ClickonButton(String[][] testdata) throws PageException {

		webPage.findObjectByxPath(testdata[0][0]).click();
	}

	public void VerifyValidationMsg(String[][] testdata) {
		List<String> brokenItems = new ArrayList<String>();
		log.info("verification of Mandatory field validation message started");

		for (int r = 0; r < testdata.length; r++) {

			String FNLocator = testdata[r][1];
			String FNInput = testdata[r][2];
			String FNErrLocator = testdata[r][3];
			String LNLocator = testdata[r][4];
			String LNInput = testdata[r][5];
			String LNErrLocator = testdata[r][6];
			String EmailLocator = testdata[r][7];
			String EmailInput = testdata[r][8];
			String EmailErrLocator = testdata[r][9];
			String ExpectedValMsg = testdata[r][10];
			String ButtonLocator = testdata[r][11];
			boolean runflag = true;
			if (r == 2) {
				runflag = false;

			}

			try {
				if (FNInput.equalsIgnoreCase("NA") && LNInput.equalsIgnoreCase("NA")
						&& EmailInput.equalsIgnoreCase("NA")) {
					webPage.findObjectByxPath(FNLocator).click();
					webPage.findObjectByxPath(FNLocator).clear();
					webPage.findObjectByxPath(LNLocator).click();
					webPage.findObjectByxPath(LNLocator).clear();
					webPage.findObjectByxPath(EmailLocator).click();
					webPage.findObjectByxPath(EmailLocator).clear();
					webPage.findObjectByxPath(ButtonLocator).click();
					SoftAssertor.assertEquals(webPage.findObjectByxPath(FNErrLocator).getText(), ExpectedValMsg);
					SoftAssertor.assertEquals(webPage.findObjectByxPath(LNErrLocator).getText(), ExpectedValMsg);
					SoftAssertor.assertEquals(webPage.findObjectByxPath(EmailErrLocator).getText(), ExpectedValMsg);
				} else {
					webPage.findObjectByxPath(FNLocator).clear();
					webPage.findObjectByxPath(FNLocator).sendKeys(FNInput);
					webPage.findObjectByxPath(LNLocator).clear();
					webPage.findObjectByxPath(LNLocator).sendKeys(LNInput);
					webPage.findObjectByxPath(EmailLocator).clear();
					webPage.findObjectByxPath(EmailLocator).sendKeys(EmailInput);
					if (runflag) {
						webPage.findObjectByxPath(ButtonLocator).click();
					}
					log.info("in else part of validation msg method......");
					SoftAssertor.assertEquals(webPage.findObjectByxPath(EmailErrLocator).getText(), ExpectedValMsg);

				}

			} catch (Exception e) {
				brokenItems.add(FNLocator + " " + e.getLocalizedMessage());

			}
		}
		if (brokenItems.size() > 0) {
			Assert.fail("Link " + Arrays.deepToString(brokenItems.toArray()) + " are not working as expected");
		}

	}

	public void VerifyChangePasswordfun(String[][] inputdata) throws PageException {

		List<String> brokenItems = new ArrayList<String>();
		webPage.findObjectByxPath(inputdata[0][0]).click();

		for (int r = 0; r < inputdata.length; r++) {

			String CurrPwdLocator = inputdata[r][1];
			String CurrPwdinput = inputdata[r][2];
			String CurrPwdErrMsgLocator = inputdata[r][3];
			String NewPwdLocator = inputdata[r][4];
			String NewPwdinput = inputdata[r][5];
			String NewPwdErrMsgLocator = inputdata[r][6];
			String ConfPwdLocator = inputdata[r][7];
			String ConfPwdinput = inputdata[r][8];
			String ConfPwdErrMsgLocator = inputdata[r][9];
			String ExpectedValMsg = inputdata[r][10];
			String ButtonLocator = inputdata[r][11];
			String NameofTestCase = inputdata[r][12];
			try {
				if (CurrPwdinput.equalsIgnoreCase("NA") && NewPwdinput.equalsIgnoreCase("NA")
						&& ConfPwdinput.equalsIgnoreCase("NA")) {
					webPage.findObjectByxPath(ButtonLocator).click();
					SoftAssertor.assertEquals(webPage.findObjectByxPath(CurrPwdErrMsgLocator).getText(),
							ExpectedValMsg);
					SoftAssertor.assertEquals(webPage.findObjectByxPath(NewPwdErrMsgLocator).getText(), ExpectedValMsg);
					SoftAssertor.assertEquals(webPage.findObjectByxPath(ConfPwdErrMsgLocator).getText(),
							ExpectedValMsg);
				} else {
					log.info("in else part of change password method......");
					
					JavascriptExecutor jse = (JavascriptExecutor)webPage.getDriver();
					jse.executeScript("scroll(0, 250);");
					webPage.findObjectByxPath(CurrPwdLocator).click();
					webPage.findObjectByxPath(CurrPwdLocator).clear();
					webPage.findObjectByxPath(CurrPwdLocator).sendKeys(CurrPwdinput);
					webPage.findObjectByxPath(NewPwdLocator).click();
					webPage.findObjectByxPath(NewPwdLocator).clear();
					webPage.findObjectByxPath(NewPwdLocator).sendKeys(NewPwdinput);
					webPage.findObjectByxPath(ConfPwdLocator).click();
					webPage.findObjectByxPath(ConfPwdLocator).clear();
					webPage.findObjectByxPath(ConfPwdLocator).sendKeys(ConfPwdinput);
					webPage.findObjectByxPath(ButtonLocator).click();
					Thread.sleep(3000);
					if (NameofTestCase.equalsIgnoreCase("ShortPassword")) {
						SoftAssertor.assertEquals(webPage.findObjectByxPath(NewPwdErrMsgLocator).getText(),
								ExpectedValMsg);
					} else if (NameofTestCase.equalsIgnoreCase("diffvalues")) {
						SoftAssertor.assertEquals(webPage.findObjectByxPath(NewPwdErrMsgLocator).getText(),
								ExpectedValMsg);
						SoftAssertor.assertEquals(webPage.findObjectByxPath(ConfPwdErrMsgLocator).getText(),
								ExpectedValMsg);

					} else if (NameofTestCase.equalsIgnoreCase("invalidCrrpwd")) {
						SoftAssertor.assertEquals(webPage.findObjectByxPath(ConfPwdErrMsgLocator).getText(),
								ExpectedValMsg);

					} else {
						SoftAssertor.assertEquals(webPage.findObjectByxPath(ConfPwdErrMsgLocator).getText(),
								ExpectedValMsg);
					}
				}

			} catch (Exception e) {
				brokenItems.add(NameofTestCase + " " + e.getLocalizedMessage());

			}
		}
		if (brokenItems.size() > 0) {
			Assert.fail("Link " + Arrays.deepToString(brokenItems.toArray()) + " are not working as expected");
		}

	}
	
	public void verifyValidationMessageforNewAddressform(String[][] testdata) throws PageException {

		for (int i = 0; i < 2; i++) {
			try {
				webPage.findObjectByxPath(testdata[0][i]).click();
			} catch (Throwable e) {
				log.info(e.getLocalizedMessage());
			}
		}
		for (int i = 2; i < 4; i++) {
			try {
				webPage.findObjectByxPath(testdata[0][i]).clear();
			} catch (Throwable e) {
				log.info(e.getLocalizedMessage());
			}

		}
		webPage.findObjectByxPath(testdata[0][13]).click();
		for (int i = 4; i < 10; i++) {
			try {
				SoftAssertor.assertEquals(webPage.findObjectByxPath(testdata[0][i]).getText(), testdata[0][11],
						"Error message does not match for element");
			} catch (Throwable e) {
				log.info(e.getLocalizedMessage());
			}

		}
		SoftAssertor.assertEquals(webPage.findObjectByxPath(testdata[0][10]).getText(), testdata[0][12],
				"Error message does not match for element");

	}

	public void verifyValidformSubmission(String[][] data) throws PageException {
		for (int i = 0; i < 16; i++) {
			try {
				String locator = data[0][i];
				if (!(i == 10 || i == 14)) {
					webPage.findObjectByxPath(locator).click();
					webPage.findObjectByxPath(locator).clear();
				}
				i = i + 1;
				webPage.findObjectByxPath(locator).sendKeys(data[0][i]);
			} catch (Throwable e) {
				log.info(e.getLocalizedMessage());
			}

		}

	}

	public void LoginfuncInvalidInput(String[][] testdata) {

		for (int r = 0; r < testdata.length; r++) {

			String EmailAddlocator = testdata[r][0];
			String EmailErrlocator = testdata[r][1];
			String EmailAddInput = testdata[r][2];
			String ExpErrMsgEmail = testdata[r][3];
			String Pwdlocator = testdata[r][4];
			String PwdErrlocator = testdata[r][5];
			String PwdInput = testdata[r][6];
			String ExpErrMsgPwd = testdata[r][7];
			String LoginButtLocator = testdata[r][8];
			try {
				if (!(EmailAddInput.equalsIgnoreCase("NA") && PwdInput.equalsIgnoreCase("NA"))) {
					webPage.findObjectByxPath(EmailAddlocator).sendKeys(EmailAddInput);
					webPage.findObjectByxPath(Pwdlocator).sendKeys(PwdInput);
				}
				webPage.findObjectByxPath(LoginButtLocator).click();
				String ActualEmailErrMsg = webPage.findObjectByxPath(EmailErrlocator).getText();
				SoftAssertor.assertEquals(ActualEmailErrMsg, ExpErrMsgEmail, "Email address error msg does not match");
				String ActualPwdErrMsg = webPage.findObjectByxPath(PwdErrlocator).getText();
				SoftAssertor.assertEquals(ActualPwdErrMsg, ExpErrMsgPwd, "Password Error msg does not match");
			}

			catch (Throwable e) {
				SoftAssertor.addVerificationFailure(e.getMessage());
				log.error("Login funcationlity for Invalid Input Failed");
				log.error(e.getMessage());
			}
		}
	}

	public void WhatsThisOverlay(String[][] testdata) {

		String Locator = testdata[0][0];
		String contentonoverlaylocator = testdata[0][1];
		String expectedCotent = testdata[0][2];
		String closebutlocator = testdata[0][3];
		try {
			webPage.findObjectByxPath(Locator).click();
			String ActualContent = webPage.findObjectByxPath(contentonoverlaylocator).getText();
			SoftAssertor.assertEquals(ActualContent, expectedCotent, "content on overlay does not match");
			webPage.findObjectByxPath(closebutlocator).click();
		} catch (Throwable e) {
			SoftAssertor.addVerificationFailure(e.getMessage());
			
			log.error("What's This Overlay failed");
			log.error(e.getMessage());
		}
		finally
		{
			try {
				webPage.findObjectByxPath(closebutlocator).click();
			} catch (PageException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public void ForgotPasswordPageTitle(String[][] testdata) {

		try {
			String ForgotPwdlink = testdata[0][2];
			webPage.findObjectByxPath(ForgotPwdlink).click();
			verifyPageTitle(testdata);
		} catch (Throwable e) {
			SoftAssertor.addVerificationFailure(e.getMessage());
			log.error("Forgot Password Page Title verification failed");
			log.error(e.getMessage());
		}
	}

	public void verfifyforgotPasswordFucwithInvaliddata(String[][] testingdata) {

		String EmailAddlocator = testingdata[0][0];
		String EmailAddInput = testingdata[0][1];
		String EmailAddError = testingdata[0][2];
		String SubmitButt = testingdata[0][3];
		String ExpErrMsg = testingdata[0][4];

		try {
			if (!(EmailAddInput.equalsIgnoreCase("NA"))) {
				webPage.findObjectByxPath(EmailAddlocator).sendKeys(EmailAddInput);

			}
			webPage.findObjectByxPath(SubmitButt).click();
			String ActualErr = webPage.findObjectByxPath(EmailAddError).getText();
			SoftAssertor.assertEquals(ActualErr, ExpErrMsg, "Error message does not match");
		} catch (Throwable e) {
			SoftAssertor.addVerificationFailure(e.getMessage());
			log.error("Forgot Password Page Title verification failed");
			log.error(e.getMessage());
		}

	}
}