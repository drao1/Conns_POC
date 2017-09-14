package com.etouch.connsPages;

import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.testng.asserts.SoftAssert;

import com.etouch.connsTests.ConnsPOC;
import com.etouch.taf.core.exception.PageException;
import com.etouch.taf.util.LogUtil;
import com.etouch.taf.webui.selenium.WebPage;

public class CreditAppPage extends ConnsPOC {

	static Log log = LogUtil.getLog(CreditAppPage.class);


	/**
	 * Method to navigate to Credit App Page
	 * @author sjadhav
	 * @param webPage
	 * @param softAssert
	 * @throws Exception 
	 */
	public static void navigateToCreditAppPage(WebPage webPage, SoftAssert softAssert) throws Exception	
	{
		if(webPage.getPageTitle().equalsIgnoreCase(commonData[1][1]))
		{log.info("Already on Credit App Page");
		webPage.getDriver().navigate().refresh();
		}
		else{
			if(!webPage.getPageTitle().equalsIgnoreCase(commonData[0][1]))
			{
				System.out.println("aaaaaaaaaa "+url);
				webPage.navigateToUrl(url);
				log.info("Navigating to Home Page");
			}
			log.info("Navigating to Credit App Page");
			commonMethods.clickElementbyXpath(webPage, commonData[2][1], softAssert);
			commonMethods.waitForPageLoad(webPage, softAssert);
			commonMethods.clickElementbyXpath(webPage, commonData[3][1], softAssert);
			commonMethods.waitForPageLoad(webPage, softAssert);
			verifyPageTitle(webPage,commonData[1][1],softAssert);
			log.info("Navigated to Credit App Page");
		}
	}	
	/**
	 * Verify page title
	 * @author sjadhav
	 * @param webPage
	 * @param expectedTitle
	 * @param softAssert
	 * @return
	 */
	public static boolean verifyPageTitle(WebPage webPage,String expectedTitle, SoftAssert softAssert)
	{
		String actualTitle = commonMethods.getPageTitle(webPage, softAssert);
		log.info("Verifying Page Title");
		if(actualTitle.equalsIgnoreCase(expectedTitle))
		{
			log.info("Page Title successfully verified");
			return true;
		}else
		{
			softAssert.fail("Actual Page Title : "+actualTitle+" Expected Title : "+expectedTitle);
			return false;
		}

	}

	/**
	 * Navigates to link and verifies url and redirects back
	 * @author sjadhav
	 * @param webPage
	 * @param linkName
	 * @param locator
	 * @param expectedUrl
	 * @return
	 * @throws Exception
	 */
	public static boolean validateLinkRedirection(String linkName, String locator,String expectedUrl ) throws Exception
	{
		String ActualUrl;
		ActualUrl=clickAndGetPageURL(locator, linkName);
		if(ActualUrl.contains(expectedUrl))
		{log.info("Redirection for link "+linkName+" is successful");
		webPage.getBackToUrl();

		}
		else{
			throw new Exception("Redirection for link "+linkName+
					" failed. Actual URL does not contain Expected partial URL : Expected = "+expectedUrl+" Actual : "+ActualUrl);
		}
		return true;
	}

	/**
	 * @author Name - Deepak Bhambri
	 * The method used to click on link using x-path and return page url
	 * Return type is String
	 * Any structural modifications to the display of the link should be done by overriding this method.
	 * @throws Exception 
	 * @throws PageException  If an input or output exception occurred
	 **/
	public static String clickAndGetPageURL(String locator, String linkName) throws Exception{
		String pageUrl="";
		try{
			log.info("Clicking on link : "+linkName);
			String mainWindow = webPage.getDriver().getWindowHandle();
			webPage.findObjectByxPath(locator).click();
			Set<String> windowHandlesSet = webPage.getDriver().getWindowHandles();
			if(windowHandlesSet.size()>1){
				for(String winHandle:windowHandlesSet){
					webPage.getDriver().switchTo().window(winHandle);
					if(!winHandle.equalsIgnoreCase(mainWindow)){
						log.info("More than 1 window open after clicking on link : "+linkName);
						pageUrl=webPage.getCurrentUrl();
						webPage.getDriver().close();
						webPage.getDriver().switchTo().window(mainWindow);
					}
				}
			}else{
				pageUrl= webPage.getCurrentUrl();
			}
			log.info("Actual URL : "+pageUrl);
		}catch(Throwable e){
			log.info("Unable to click on link '"+linkName+". Localized Message: "+e.getLocalizedMessage());
			throw new Exception("Unable to click on link '"+linkName+". Localized Message: "+e.getLocalizedMessage());
		}
		return pageUrl;
	}

	/*public static void verifyTextField(WebPage webPage, String[][] locator, SoftAssert softAssert) throws Exception
{
	List<WebElement> elements = new ArrayList<WebElement>();
	elements = webPage.getDriver().findElements(By.xpath(locator));
	if(elements.size()==0)
		throw new Exception("Unable to find TextBox with locater : "+locator);
	for(WebElement ele : elements)
	{
		if(!ele.getAttribute("value").isEmpty())
			softAssert.fail("TextBox With Xpath "+locator+" is not rendered empty. Value displayed is : "+ele.getAttribute("value"));
			ele.sendKeys("12");
	}
	}

}*/

	/**
	 * Verify if Text Field is displayed with expected Value
	 * @author sjadhav
	 * @param webPage
	 * @param locators
	 * @param softAssert
	 * @throws Exception
	 */
	public static void verifyTextField(String[][] locators, SoftAssert softAssert) throws Exception
	{

		Thread.sleep(3000);
		for(int i= 0; i<locators.length;i++)
		{
			WebElement element = commonMethods.getWebElementbyXpath(webPage, locators[i][1], softAssert);
			//webPage.getDriver().findElement(By.xpath(locators[i][1]));
			if(!element.getAttribute("value").equalsIgnoreCase(locators[i][2]))
				softAssert.fail("TextBox "+locators[i][0] + " With Xpath "+locators[i][0]+
						" is not rendered as expected. Expected value is : "+locators[i][2]+" Displayed Value : "+element.getAttribute("value"));
			else{
				log.info("TextBox "+locators[i][0] + " is rendered with expected value : "+locators[i][2]);
			}
		}
	}

	/**
	 * Clicks on Submit button on Credit Application Page
	 * @author sjadhav
	 * @param webPage
	 * @param softAssert
	 * @throws Exception
	 */
	public static void submitCreditApp(SoftAssert softAssert) throws Exception
	{
		//navigateToCreditAppPage(webPage, softAssert);
		commonMethods.clickElementbyXpath(webPage, commonData[4][1], softAssert);
	}

	/**
	 * Verify expected and actual error message
	 * @author sjadhav
	 * @param webPage
	 * @param softAssert
	 * @param errorMessageFieldName
	 * @param locator
	 * @param expectedErrorMessage
	 */
	public static void verifyErrorMessageByXpath(SoftAssert softAssert,String errorMessageFieldName, String locator, String expectedErrorMessage)
	{
		log.info("Verifiyikng error message for : "+errorMessageFieldName+" : Expected Message : "+expectedErrorMessage);
		String actualErrorMessage=commonMethods.getTextbyXpath(webPage, locator, softAssert);
		softAssert.assertTrue(expectedErrorMessage.equals(actualErrorMessage),"Failed to verify error field :"+errorMessageFieldName+
				" : Expected : "+expectedErrorMessage+" Actual : "+actualErrorMessage);

	}


	public static void verifyErrorMessageById(SoftAssert softAssert,String errorMessageFieldName, String locator, String expectedErrorMessage)
	{
		log.info("Verifiyikng error message for : "+errorMessageFieldName+" : Expected Message : "+expectedErrorMessage);
		String actualErrorMessage=commonMethods.getTextbyId(webPage, locator, softAssert);
		softAssert.assertTrue(expectedErrorMessage.equals(actualErrorMessage),"Failed to verify error field :"+errorMessageFieldName+
				" : Expected : "+expectedErrorMessage+" Actual : "+actualErrorMessage);

	}
	/**
	 * Verify field errors message by entering invalid data
	 * @author sjadhav
	 * @param softAssert
	 * @param FieldName
	 * @param locator
	 * @param inputText
	 * @param errorMessageLocator
	 * @param expectedErrorMessage
	 */
	public static void verifyErrorMessageWithInvalidDataById(SoftAssert softAssert,
			String FieldName, String locator, String inputText,String errorMessageLocator,  String expectedErrorMessage)
	{
		WebElement element = commonMethods.getWebElementbyID(webPage, locator, softAssert);
		element.sendKeys(inputText+Keys.TAB);
		verifyErrorMessageById(softAssert, FieldName, errorMessageLocator, expectedErrorMessage);
	}

	public static String getSelectedValueFromDropDown(SoftAssert softAssert,
			String FieldName, String locator)
	{
		Select select = new Select(webPage.getDriver().findElement(By.id(locator)));
		String value = select.getFirstSelectedOption().getText();
		log.info("value selected in Drop Down is : "+value);
		return value;

	}

	public static void verifyDropDownValuesById(SoftAssert softAssert,String FieldName, String locator, String[][] expectedValue)
	{
		Select select = new Select(commonMethods.getWebElementbyID(webPage, locator, softAssert));//webPage.getDriver().findElement(By.id(locator))
		List<WebElement> listOptions = select.getOptions();
		//List<String> listOptionsText= new ArrayList<String>();
		for(int i=0;i<listOptions.size();i++)
		{
			String actual = listOptions.get(i).getText().trim();
			String expected = expectedValue[i][1].trim();
			log.info("DropDown values : "+actual+" Expected : "+ expected);
			softAssert.assertTrue(actual.equals(expected),"Failed to match DropDown Values : Actual : "+actual+" Expected : "+ expected);
		}

	}

	public static void verifyTextFieldIsEditable(SoftAssert softAssert,String FieldName, String locator, String newValue)
	{
		if(commonMethods.getWebElementbyID(webPage, locator, softAssert).isEnabled())
		{
			log.info("TextBox is enabled");
			log.info("Setting TextBox Value to : "+newValue);
			commonMethods.clearTextBoxById(webPage, locator, softAssert);
			commonMethods.sendKeysById(webPage, locator, newValue, softAssert);
			String actual = commonMethods.getTextbyId(webPage, locator, softAssert);
			softAssert.assertTrue(actual.equals(newValue),"Failed to Update TextBox value, New Value : "+newValue+" Existing Value : "+actual);
		}
		else{
			log.info("TextBox is Disabled");
			softAssert.fail(" Text Field "+FieldName+" is disabled ");
		}
	}

	public static void verifyDropDownFieldIsEditable(SoftAssert softAssert,String FieldName, String locator, String newValue)
	{
		if(commonMethods.getWebElementbyID(webPage, locator, softAssert).isEnabled())
		{
			log.info("DropDown is enabled");
			log.info("Setting DropDown Value to : "+newValue);
			Select select = new Select(commonMethods.getWebElementbyID(webPage, locator, softAssert));
			//select.selectByVisibleText(newValue.trim());
			selectValueFromDropDownByID(softAssert, FieldName, locator, newValue);
			String actual = select.getFirstSelectedOption().getText().trim();
			softAssert.assertTrue(actual.equals(newValue),"Failed to Update Drop Down Value, New Value : "+newValue+" Existing Value : "+actual);
		}
		else{
			log.info("DropDown is Disabled");
			softAssert.fail(" DropDown Field "+FieldName+" is disabled ");
		}
	}
	
	public static void selectValueFromDropDownByID(SoftAssert softAssert,String FieldName, String locator, String newValue)
	{
			log.info("Setting DropDown Value to : "+newValue);
			Select select = new Select(commonMethods.getWebElementbyID(webPage, locator, softAssert));
			select.selectByVisibleText(newValue.trim());
		}

public static void sendTextToTextFieldsById(SoftAssert softAssert,String[][] textFieldData)
{
	for(int i =0;i<textFieldData.length;i++)
	{
		log.info("Setting TextField "+textFieldData[i][0]+ " value to : "+textFieldData[i][2]);
		verifyTextFieldIsEditable(softAssert, textFieldData[i][0], textFieldData[i][1], textFieldData[i][2]);
	}
	}
}
	/*public static void verifyAutoPopulateCityState()
	{
		WebElement element = commonMethods.getWebElementbyID(webPage, locator, softAssert);
		element.sendKeys(inputText+Keys.TAB);

	}*/
