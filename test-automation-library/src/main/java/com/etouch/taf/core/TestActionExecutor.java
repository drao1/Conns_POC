package com.etouch.taf.core;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.StringTokenizer;

import org.apache.commons.logging.Log;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.ITestContext;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.etouch.taf.core.resources.TestBedType;
import com.etouch.taf.kd.config.ActionElement;
import com.etouch.taf.kd.config.ActionKeywordLibrary;
import com.etouch.taf.kd.config.AssertKeywordLibrary;
import com.etouch.taf.kd.config.ElementIdentifier;
import com.etouch.taf.kd.config.InputKeywordLibrary;
import com.etouch.taf.kd.config.KDDataset;
import com.etouch.taf.kd.config.PageActionKeywordLibrary;
import com.etouch.taf.kd.config.ResponseStatusCode;
import com.etouch.taf.kd.config.TestAction;
import com.etouch.taf.util.CommonUtil;
import com.etouch.taf.util.KeysMap;
import com.etouch.taf.util.LogUtil;
import com.etouch.taf.util.SoftAssertor;
import com.etouch.taf.webui.ITafElement;
import com.etouch.taf.webui.selenium.WebPage;

/**
 * <p>
 * <h2>The main class for executing from TestNg</h2>
 * </p>
 * 
 * <p>
 * The <b><i>execute</i></b> method is annotated with TestNG @test for TestNg to
 * recognise and execute.
 * </p>
 * 
 * <p>
 * This class have the following functionalities defined
 * <ol>
 * <li>Provides execute method for TestNG</li>
 * <li>Configures the TestActions from the Library of Action, Assert, PageAction
 * and Input Keyword</li>
 * <li>Provide interface to execute Java classes</li>
 * <li>Provide functionality for Image comparison</li>
 * <li>All the Selenium functionality are integrated in this class through
 * custom defined ITafElement, WebElement and WebPage classes</li>
 * </p>
 * 
 * 
 * @author eTouch Systems Corporation
 *
 */
public class TestActionExecutor {

	private static Log log = LogUtil.getLog(TestActionExecutor.class);

	private String testBedName;

	private String testSuiteName;

	private TestAction testAction;

	private KDDataset kdDataSet;

	private ITestContext context;

	private String url;

	/** The web page. */
	private WebPage webPage;
	
	private Class[] paramString = new Class[1];

	/**
	 * The constructor set the TestBed name, TestSuite name, TestAction object
	 * and Single record from KDDataSet object
	 * 
	 * @param testBedName
	 * @param testSuiteName
	 * @param testAction
	 * @param kdDataSet
	 */
	public TestActionExecutor(String testBedName, String testSuiteName, TestAction testAction, KDDataset kdDataSet) {
		this.testBedName = testBedName;
		this.testSuiteName = testSuiteName;
		this.testAction = testAction;
		this.kdDataSet = kdDataSet;
	}

	/**
	 * Initializing the TestNG context and setting up the Webpage
	 * 
	 * @param context
	 * @throws Exception
	 */
	@BeforeMethod
	public void init(ITestContext context) throws Exception {
		WebPage page = (WebPage) context.getAttribute("WebPage");
		if (page != null) {
			this.context = context;
			this.webPage = page;
		}

	}

	private void openBrowser(String url) {
		String testSuiteNamevalue = context.getCurrentXmlTest().getAllParameters().get("testSuiteName");

		try {

			WebPage page = new WebPage(context);
			this.webPage = page;
			log.debug("[" + testBedName + "] [" + testSuiteNamevalue + "] Loading web page...");
			page.loadPage(url);
			context.setAttribute("WebPage", page);

		} catch (Exception exp) {
			log.error(exp + " :: " + exp.getMessage());
			log.error(exp);
			SoftAssertor.addVerificationFailure(exp.getMessage());
		}
	}

	/**
	 * Main executor method for running all test actions
	 * 
	 * @throws Exception
	 */
	@Test()
	public void execute() throws Exception {
		log.debug(" ******[" + testBedName + "] [" + testSuiteName + "] Executing testAction ==>"
				+ testAction.getActionName() + " **********");

		LinkedList<ActionElement> actionElementLists = testAction.getActionElements();

		for (ActionElement actionElement : actionElementLists) {
			executeAction(actionElement);
		}

		if (kdDataSet != null) {
			Map<String, Object> kdDataMap = kdDataSet.getDataSetMap();

			if ((kdDataMap != null) && (kdDataMap.size() > 0)) {

				for (Map.Entry<String, Object> dataSet : kdDataMap.entrySet()) {
					log.debug("[" + dataSet.getKey() + "] ==>" + dataSet.getValue().toString());
				}
			}
		}
	}

	/**
	 * @param actionElement
	 * @throws Exception
	 * @throws NoSuchMethodException
	 * @throws ClassNotFoundException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws InvocationTargetException
	 * @throws InterruptedException
	 */
	private void executeAction(ActionElement actionElement) throws Exception {

		String actionElementName = actionElement.getName();
		String idType = actionElement.getIdType();
		String elementValue = actionElement.getValue();
		String action = actionElement.getKeyword().getAction();

		if (action.equalsIgnoreCase(ActionKeywordLibrary.EXECUTEJAVA.getActionName())) {
			executeJava(elementValue);
		}

		else if (action.equalsIgnoreCase(ActionKeywordLibrary.OPENBROWSER.getActionName())) {

			String urlValue = (String) kdDataSet.getDataSetMap().get(actionElementName);

			openBrowser(urlValue);

		} else if (action.equalsIgnoreCase(ActionKeywordLibrary.CLOSEBROWSER.getActionName())) {

			closeBrowser();
		}

		else if (PageActionKeywordLibrary.contains(action)) {
			TestPageActionExecutor testPageActionExecutor = new TestPageActionExecutor(webPage, testBedName,
					testSuiteName, testAction, kdDataSet);

			testPageActionExecutor.excuteActionOnPage(actionElementName, idType, elementValue, action);

		} else if (action.equalsIgnoreCase(ActionKeywordLibrary.SWITCHTOIFRAME.getActionName())) {

			switchToIFrame(elementValue);
		}

		else if (!AssertKeywordLibrary.contains(action)) {

			executeActionElement(actionElementName, idType, elementValue, action);

		} else if (action.equalsIgnoreCase(AssertKeywordLibrary.HASIMAGE.getActionName())) {

			verifyImage(actionElementName, idType, elementValue, action);

		} else {
			if (testBedName.equalsIgnoreCase(TestBedType.SAFARI.getTestBedName())
					&& action.equalsIgnoreCase(AssertKeywordLibrary.HASVALUE.getActionName())) {
				Thread.sleep(6000);
			}

			verify(actionElementName, idType, elementValue, action);
		}
	}

	private void executeJava(String qmethodName) throws ClassNotFoundException, NoSuchMethodException,
			IllegalAccessException, InvocationTargetException, InstantiationException {
		if (qmethodName != null) {

			StringTokenizer stk = new StringTokenizer(qmethodName, ".");
			String[] strList = new String[stk.countTokens()];

			int i = 0;
			while (stk.hasMoreTokens()) {
				strList[i] = stk.nextToken();
				i++;
			}

			String methodName = strList[strList.length - 1];
			int index = qmethodName.indexOf(methodName);
			String className = qmethodName.substring(0, index - 1);

			log.debug("ClassName --->" + className);
			log.debug("MethodName --->" + methodName);

			delegateMethod(methodName, className);
		}
	}

	/**
	 * @param methodName
	 * @param className
	 * @throws ClassNotFoundException
	 * @throws NoSuchMethodException
	 * @throws SecurityException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws InvocationTargetException
	 */
	private void delegateMethod(String methodName, String className) throws ClassNotFoundException,
			NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {

		Map<String, Object> kdDataMap = new HashMap<>();

		if (kdDataSet != null) {
			kdDataMap = kdDataSet.getDataSetMap();
		}

		Class<?> clazz = Class.forName(className);
		Constructor<?> classConstructor = clazz.getConstructor(WebPage.class);
		Object obj = classConstructor.newInstance(webPage);

		Method[] allMethods = clazz.getMethods();

		for (Method reqMethod : allMethods) {
			if (reqMethod.getName().equalsIgnoreCase(methodName)) {
				log.debug("nos of parameters required ==>" + reqMethod.getGenericParameterTypes().length);
				if (reqMethod.getGenericParameterTypes().length > 0) {
					Method funcMethod = clazz.getMethod(methodName, Map.class);
					funcMethod.invoke(obj, kdDataMap);
				} else {
					Method funcMethod = clazz.getMethod(methodName);
					funcMethod.invoke(obj);
				}
				break;
			}
		}
	}

	/**
	 * @param actionElementName
	 * @param idType
	 * @param elementValue
	 * @param action
	 * @throws Exception
	 
	 */
	private void executeActionElement(String actionElementName, String idType, String elementValue, String action)
			throws Exception {

		log.debug("[" + testBedName + "] [" + testSuiteName + "] [" + actionElementName
				+ "] invoking method with for ==>" + idType + " && value ==>" + elementValue);

		ITafElement iTafElement = ElementIdentifier.find(webPage, idType, elementValue);

		performEvent(iTafElement, actionElementName, action);
	}

	/**
	 * @param actionElementName
	 * @param idType
	 * @param elementValue
	 * @param action
	 * @throws ClassNotFoundException
	 * @throws NoSuchMethodException
	 * @throws SecurityException
	 * @throws IllegalArgumentException
	 * @throws InvocationTargetException
	 * @throws InstantiationException
	 * @throws IOException
	 * @throws MalformedURLException
	 */
	private void verify(String actionElementName, String idType, String elementValue, String action)
			throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException,
		InvocationTargetException, InstantiationException,
			IOException {

		log.debug(
				"[" + testBedName + "] [" + testSuiteName + "] [" + actionElementName + "] asserting for ==>" + idType);

		String assertActual;

		if (idType.equalsIgnoreCase(ElementIdentifier.URL.getIdentifier())
				|| idType.equalsIgnoreCase(ElementIdentifier.PAGE_TITLE.getIdentifier())) {
			assertActual = ElementIdentifier.get(webPage, action, idType, null);
		} else {
			assertActual = ElementIdentifier.get(webPage, action, idType, elementValue);
		}

		Object assertExpected = kdDataSet.getDataSetMap().get(actionElementName);

		if (ResponseStatusCode.contains(action)) {

			String newUrl = assertExpected.toString();

			// Page is loaded with expected URL for visual understanding only,
			// it has no dependency for Response Status code verification.
			this.webPage.loadPage(newUrl);

			assertActual = CommonUtil.getResponseCode(newUrl);
			assertCheck(action, assertActual, ResponseStatusCode.getResponseCode(action));
		} else {
			assertCheck(action, assertActual, assertExpected);
		}

	}

	private void verifyImage(String actionElementName, String idType, String elementValue, String action)
			throws Exception {

		log.debug("[" + testBedName + "] [" + testSuiteName + "] [" + actionElementName + "] asserting for Image ==>"
				+ idType + " && value ==>" + elementValue);

		String actualImageURL = elementValue;

		// get expected ImageURL
		Object expectedImageURL = kdDataSet.getDataSetMap().get(actionElementName);

		assertImage(action, actualImageURL, expectedImageURL.toString());

	}

	/**
	 * Assert on Image
	 * 
	 * @param action
	 * @param actualImage
	 * @param assertExpected
	 * @throws InstantiationException
	 * @throws InvocationTargetException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws SecurityException
	 * @throws NoSuchMethodException
	 * @throws ClassNotFoundException
	 */
	private void assertImage(String action, String actualImageURL, String expectedImageURL)
			throws IllegalAccessException, InvocationTargetException, InstantiationException,
			NoSuchMethodException, ClassNotFoundException {

		Class<?> assertActionElementClazz = Class.forName("com.etouch.taf.kd.validator.AssertActionElement");

		Class[] paramObject = new Class[2];
		paramObject[0] = String.class;
		paramObject[1] = String.class;

		Method assertMethod = assertActionElementClazz.getDeclaredMethod(action, paramObject);

		assertMethod.invoke(assertActionElementClazz.newInstance(), actualImageURL, expectedImageURL);
	}

	@SuppressWarnings("rawtypes")
	private void assertCheck(String action, String assertActual, Object assertExpected)
			throws IllegalAccessException, InvocationTargetException, InstantiationException,
			NoSuchMethodException, ClassNotFoundException {

		Class<?> assertActionElementClazz = Class.forName("com.etouch.taf.kd.validator.AssertActionElement");

		Class[] paramObject = new Class[2];
		paramObject[0] = String.class;
		paramObject[1] = Object.class;

		Method assertMethod = assertActionElementClazz.getDeclaredMethod(action, paramObject);

		assertMethod.invoke(assertActionElementClazz.newInstance(), assertActual, assertExpected);
	}

	private void performEvent(ITafElement iTafElement, String actionElementName, String action)
			throws Exception {

		if (action.equalsIgnoreCase(InputKeywordLibrary.ENTERVALUE.getActionName())) {

			String text = String.valueOf(kdDataSet.getDataSetMap().get(actionElementName));

			log.debug("Entering value for ==>" + actionElementName  + text);
			type(iTafElement, text);

		}

		else if (action.equalsIgnoreCase(InputKeywordLibrary.ENTERKEY.getActionName())) {

			String value = String.valueOf(kdDataSet.getDataSetMap().get(actionElementName));
			value = value.substring(5);
			// Convert the Key value to Upper case
			value = value.toUpperCase();
			CharSequence[] charSequence = new CharSequence[1];
			charSequence[0] = KeysMap.get(value);

			log.debug("Entering value for ==>" + actionElementName + " " + charSequence);
			type(iTafElement, charSequence);

		} else if (action.equalsIgnoreCase(InputKeywordLibrary.SELECTDROPDOWNLIST.getActionName())) {

			String text = String.valueOf(kdDataSet.getDataSetMap().get(actionElementName));

			log.debug("Selecting value for ==>" + actionElementName + " " + text);
			select(iTafElement, text);

		}

		else {
			log.debug("Performing event ==><" + action + "> on " + actionElementName);
			perform(iTafElement, action);
		}

	}

	/**
	 * 
	 * @param iTafElement
	 * @param action
	 * @throws Exception
	 */
	private void perform(ITafElement iTafElement, String action) throws Exception {

		Class<?> iTafElementClaszz = getITafElementClass();

		Method actionMethod = iTafElementClaszz.getDeclaredMethod(action);

		actionMethod.invoke(iTafElement, null);
	}

	

	/**
	 * 
	 * @param iTafElement
	 * @param ENTERVALUE
	 * @throws NoSuchMethodException
	 * @throws ClassNotFoundException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws InvocationTargetException
	 */
	private void select(ITafElement iTafElement, String enterValue) throws NoSuchMethodException,
			ClassNotFoundException, IllegalAccessException, InvocationTargetException {

		Class<?> iTafElementClaszz = getITafElementClass();

		paramString[0] = String.class;

		Method sendKeyMethod = iTafElementClaszz.getDeclaredMethod(InputKeywordLibrary.SELECTDROPDOWNLIST.getActionName(),
				paramString);

		sendKeyMethod.invoke(iTafElement, enterValue.trim());
	}

	/**
	 * 
	 * @param iTafElement
	 * @param ENTERVALUE
	 * @throws NoSuchMethodException
	 * @throws ClassNotFoundException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws InvocationTargetException
	 */
	private void type(ITafElement iTafElement, String enterValue) throws NoSuchMethodException, ClassNotFoundException,
			IllegalAccessException, InvocationTargetException {

		Class<?> iTafElementClaszz = getITafElementClass();

		paramString[0] = String.class;

		Method sendKeyMethod = iTafElementClaszz.getDeclaredMethod(ActionKeywordLibrary.SENDKEYS.getActionName(),
				paramString);

		sendKeyMethod.invoke(iTafElement, enterValue.trim());
	}

	private void type(ITafElement iTafElement, CharSequence[] enterKey) throws NoSuchMethodException,
			ClassNotFoundException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {

		Class<?> iTafElementClaszz = getITafElementClass();

		paramString[0] = CharSequence[].class;

		Method sendKeyMethod = iTafElementClaszz.getDeclaredMethod(ActionKeywordLibrary.SENDKEYS.getActionName(),
				paramString);

		sendKeyMethod.invoke(iTafElement, new Object[] { enterKey });
	}

	/**
	 * 
	 * @return iTafElement class object
	 * @throws ClassNotFoundException
	 */
	private Class<?> getITafElementClass() throws ClassNotFoundException {
		return Class.forName("com.etouch.taf.webui.ITafElement");
	}

	/**
	 * @return the testBedName
	 */
	public String getTestBedName() {
		return testBedName;
	}

	/**
	 * @return the testSuiteName
	 */
	public String getTestSuiteName() {
		return testSuiteName;
	}

	/**
	 * @return the kdDataSet
	 */
	public KDDataset getKdDataSet() {
		return kdDataSet;
	}

	/**
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * @return the testAction
	 */
	public TestAction getTestAction() {
		return testAction;
	}

	/**
	 * @return the webPage
	 */
	public WebPage getWebPage() {
		return webPage;
	}

	/**
	 * 
	 */
	private void switchToIFrame(String elementValue) {

		WebElement iFrame = webPage.getDriver().findElement(By.xpath(elementValue));
		webPage.getDriver().switchTo().frame(iFrame);
		log.debug("Switched to iFrame" + elementValue);

	}

	private void closeBrowser() {
		
		// Before closing the driver, remove it from list of currentDrivers in TestBedManager
		TestBedManager.INSTANCE.getCurrentDrivers().removeDriver(testBedName, webPage.getDriver());
		webPage.getDriver().close();
	}
	@AfterMethod
	public void afterClass(ITestContext context) {
		log.info("Test Name :" + context.getName() + " - End");
		log.info("********Results*******");
	}

}
