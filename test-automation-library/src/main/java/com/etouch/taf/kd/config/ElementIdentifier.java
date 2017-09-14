package com.etouch.taf.kd.config;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.etouch.taf.core.config.BooleanAssertKeywordLibrary;
import com.etouch.taf.webui.ITafElement;
import com.etouch.taf.webui.selenium.WebPage;

/**
 * Provides Objects with methods to locate an Element on the Web page and
 * retrieves the text or count of child elements
 * 
 * @author eTouch Systems Corporation
 * 
 */
public enum ElementIdentifier {

	ID("id", "findObjectById"), NAME("name", "findObjectByName"), CSS("css", "findObjectByCss"), XPATH("xpath", "findObjectByxPath"), LINK("Link", "findObjectByLink"), URL("URL",
			"getCurrentUrl"), PAGE_TITLE("pageTitle", "getPageTitle"), PARTIAL_LINK("partialLink", "findObjectByPartialLink"), CLASS("class", "findObjectByClass"), COMPOUND_CLASS(
			"compoundClass", "findObjectByCompoundClass"), TAG("tag", "findObjectByTag"), MULTI_VALUE_CSS("multiValueCss", "findObjectsByCss"), MULTI_VALUE_XPATH(
			"multiValueXpath", "findObjectsByXpath"), MULTI_VALUE_LINK("multiValueLink", "findObjectsByLink"), MULTI_VALUE_PARTIAL_LINK("multiValuePartialLink",
			"findObjectsByPartialLink"), MULTI_VALUE_CLASS("multiValueClass", "findObjectsByClass"), MULTI_VALUE_TAG("multiValueTag", "findObjectsByTag"),

	ZOOMIN("window", "zoomIn"), ZOOMOUT("window", "zoomOut"), ZOOMTO100("window", "zoomTo100");

	private final String identifier;

	private final String methodName;

	private static Class[] paramString = new Class[1];

	ElementIdentifier(String identifier, String methodName) {
		this.identifier = identifier;
		this.methodName = methodName;
	}

	/**
	 * @return the identifier
	 */
	public String getIdentifier() {
		return identifier;
	}

	/**
	 * @return the methodName
	 */
	public String getMethodName() {
		return methodName;
	}

	private static final Map<String, String> identifierMethodMap = Collections.unmodifiableMap(initializeMapping());

	/**
	 * 
	 * @return
	 */
	private static Map<String, String> initializeMapping() {
		Map<String, String> methodMap = new HashMap<>();

		for (ElementIdentifier elementIdentifier : ElementIdentifier.values()) {
			methodMap.put(elementIdentifier.identifier, elementIdentifier.methodName);
		}

		return methodMap;
	}

	/**
	 * 
	 * @param identifier
	 * @return
	 */
	private static String getIdentifierMethod(String identifier) {

		if (identifierMethodMap.containsKey(identifier)) {
			return identifierMethodMap.get(identifier);
		}

		return null;

	}

	/**
	 * 
	 * @return
	 * @throws ClassNotFoundException
	 */
	private static Class<?> getWebPageClass() throws ClassNotFoundException {
		return Class.forName("com.etouch.taf.webui.selenium.WebPage");
	}

	/**
	 * Locate the Element on the web page with the given locater value
	 * 
	 * @param webPage
	 * @param elementIdentifier
	 * @param elementIdenficationValue
	 * 
	 * @return ITafElement object
	 * @throws Exception
	 */
	public static ITafElement find(WebPage webPage, String elementIdentifier, String elementIdenficationValue) throws Exception {

		String methodName = ElementIdentifier.getIdentifierMethod(elementIdentifier);

		Class<?> webPageClazz = getWebPageClass();

		paramString[0] = String.class;

		Method identifierMethod = webPageClazz.getDeclaredMethod(methodName, paramString);

		return (ITafElement) identifierMethod.invoke(webPage, elementIdenficationValue);

	}

	/**
	 * Locate the Element on the web page
	 * 
	 * @param webPage
	 * @param elementIdentifier
	 * 
	 * @return ITafElement object
	 * 
	 * @throws NoSuchMethodException
	 * @throws SecurityException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws InvocationTargetException
	 * @throws ClassNotFoundException
	 */
	public static ITafElement find(WebPage webPage, String elementIdentifier) throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException,
			InvocationTargetException, ClassNotFoundException {

		String methodName = ElementIdentifier.getIdentifierMethod(elementIdentifier);

		Class<?> webPageClazz = getWebPageClass();

		Method identifierMethod = webPageClazz.getDeclaredMethod(methodName);

		ITafElement iTafElement = (ITafElement) identifierMethod.invoke(webPage);

		return iTafElement;
	}

	/**
	 * Fetches the getText() of the ITafElement or count of child elements
	 * inside the Locator
	 * 
	 * The method is used for Assert calls.
	 * 
	 * @param webPage
	 * @param actionName
	 * @param elementIdentifier
	 * @param elementIdenficationValue
	 * 
	 * @return getText() or child node count
	 * 
	 * @throws ClassNotFoundException
	 * @throws NoSuchMethodException
	 * @throws SecurityException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws InvocationTargetException
	 */
	public static String get(WebPage webPage, String actionName, String elementIdentifier, String elementIdenficationValue) throws ClassNotFoundException, NoSuchMethodException,
			SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {

		String methodName = ElementIdentifier.getIdentifierMethod(elementIdentifier);

		Class<?> webPageClazz = getWebPageClass();

		String text = null;

		if (StringUtils.isNotBlank(elementIdenficationValue)) {

			paramString[0] = String.class;

			Method identifierMethod = webPageClazz.getDeclaredMethod(methodName, paramString);

			Object iTafElementObj = identifierMethod.invoke(webPage, elementIdenficationValue);

			List<ITafElement> iTafElements = getITafElements(iTafElementObj);

			text = getText(iTafElements, actionName);
		} else {

			Method identifierMethod = webPageClazz.getDeclaredMethod(methodName);
			text = (String) identifierMethod.invoke(webPage);

		}

		return text;

	}

	/**
	 * @param iTafElementObj
	 * 
	 * @return List of ITafElemtents
	 */
	@SuppressWarnings("unchecked")
	private static List<ITafElement> getITafElements(Object iTafElementObj) {
		List<ITafElement> iTafElements = new ArrayList<>();

		if (iTafElementObj instanceof List<?>) {
			iTafElements.addAll((List<ITafElement>) iTafElementObj);
		} else {
			iTafElements.add((ITafElement) iTafElementObj);
		}
		return iTafElements;
	}

	/**
	 * @param iTafElement
	 * @return getText or size count of the child count
	 * @throws SecurityException
	 * @throws NoSuchMethodException
	 * @throws InvocationTargetException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	private static String getText(List<ITafElement> iTafElements, String action) throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException,
			InvocationTargetException {

		String text = "";

		if (AttributeKeywordLibrary.contains(action)) {

			if (action.equalsIgnoreCase(AttributeKeywordLibrary.CHILD_COUNT.getAttributeAssertMethod())) {
				text = String.valueOf(iTafElements.size());
			} else {
				text = iTafElements.get(0).getAttribute(AttributeKeywordLibrary.getAttribute(action));
			}

		} else if (BooleanAssertKeywordLibrary.contains(action)) {
			ITafElement iTafElement = iTafElements.get(0);
			Method booleanMethod = iTafElement.getClass().getDeclaredMethod(action, null);
			boolean booleanValue = (boolean) booleanMethod.invoke(iTafElement, null);
			text = String.valueOf(booleanValue);

		} else {
			text = iTafElements.get(0).getText();
		}

		return text;
	}

}
