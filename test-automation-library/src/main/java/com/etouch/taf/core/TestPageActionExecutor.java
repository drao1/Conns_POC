package com.etouch.taf.core;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.StringTokenizer;

import org.apache.commons.logging.Log;

import com.etouch.taf.kd.config.KDDataset;
import com.etouch.taf.kd.config.PageActionKeywordLibrary;
import com.etouch.taf.kd.config.TestAction;
import com.etouch.taf.util.LogUtil;
import com.etouch.taf.webui.selenium.WebPage;

public class TestPageActionExecutor {

	private static Log log = LogUtil.getLog(TestPageActionExecutor.class);

	private String testBedName, testSuiteName;

	private TestAction testAction;

	private KDDataset kdDataSet;

	private String url;

	/** The web page. */
	private WebPage webPage;

	/**
	 * 
	 * @param testBedName
	 * @param testSuiteName
	 * @param testAction
	 * @param kdDataSet
	 */
	public TestPageActionExecutor(WebPage webPage, String testBedName, String testSuiteName, TestAction testAction,
			KDDataset kdDataSet) {

		this.webPage = webPage;
		this.testBedName = testBedName;
		this.testSuiteName = testSuiteName;
		this.testAction = testAction;
		this.kdDataSet = kdDataSet;
	}

	/**
	 * 
	 * @param actionElementName
	 * @param idType
	 * @param elementValue
	 * @param action
	 * @throws Exception
	 * @throws NoSuchMethodException
	 * @throws ClassNotFoundException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws InvocationTargetException
	 * @throws InterruptedException
	 */
	public void excuteActionOnPage(String actionElementName, String idType, String elementValue, String action)
			throws Exception, NoSuchMethodException, ClassNotFoundException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException, InterruptedException {

		log.debug("[" + actionElementName + "] invoking method with for ==>" + idType + " && value ==>" + elementValue);

		performPageAction(webPage, actionElementName, action);
	}

	/**
	 * 
	 * @param iTafElement
	 * @param action
	 * @throws Exception
	 */
	private void performPageAction(WebPage webPage, String actionElementName, String action) throws Exception {

		if (action.equalsIgnoreCase(PageActionKeywordLibrary.ZOOMIN.name())
				|| action.equalsIgnoreCase(PageActionKeywordLibrary.ZOOMOUT.name())
				|| action.equalsIgnoreCase(PageActionKeywordLibrary.SCROLLUP.name())
				|| action.equalsIgnoreCase(PageActionKeywordLibrary.SCROLLDOWN.name())) {
			Class<?> webPageClaszz = getWebPageClass();

			Double dou = Double.parseDouble(kdDataSet.getDataSetMap().get(actionElementName).toString());
			Integer intValue = dou.intValue();

			log.debug("Page action on window ==>" + actionElementName + " >>> " + intValue);

			Method actionMethod = webPageClaszz.getDeclaredMethod(action, int.class);

			actionMethod.invoke(webPage, intValue);

		} else if (action.equalsIgnoreCase(PageActionKeywordLibrary.RESIZE.getActionName())) {

			performResize(webPage, actionElementName, action);

		} else if ((action.equalsIgnoreCase(PageActionKeywordLibrary.NAVIGATETOURL.getActionName())
				|| action.startsWith("open")) || action.equalsIgnoreCase(PageActionKeywordLibrary.SWITCHWINDOW.getActionName()))  {

			performNavigate(webPage, actionElementName, action);

		} else {

			performAction(webPage, actionElementName, action);
		}

	}

	/**
	 * 
	 * @param webPage2
	 * @param actionElementName
	 * @param action
	 * @throws Exception
	 */
	private void performNavigate(WebPage webPage, String actionElementName, String action) throws Exception {

		Class<?> webPageClaszz = getWebPageClass();

		String strValue = String.valueOf(kdDataSet.getDataSetMap().get(actionElementName));

		Class[] paramClazz = new Class[1];
		paramClazz[0] = String.class;

		try {
			
			log.debug("Page action on window ==>" + actionElementName + " >>> " + strValue);

			Method actionMethod = webPageClaszz.getDeclaredMethod(action, paramClazz);

			actionMethod.invoke(webPage, strValue);
		} catch (Exception e) {
			log.debug(" Exception occured." + e.getCause());
			throw e;
		}

	}

	private void performResize(WebPage webPage2, String actionElementName, String action) throws Exception {

		Class<?> webPageClaszz = getWebPageClass();

		String strValue = String.valueOf(kdDataSet.getDataSetMap().get(actionElementName));
		StringTokenizer str = new StringTokenizer(strValue, ",");
		int int1 = Integer.parseInt(str.nextToken().toString().trim());
		int int2 = Integer.parseInt(str.nextToken().toString().trim());

		Class[] paramClazz = new Class[2];
		paramClazz[0] = int.class;
		paramClazz[1] = int.class;

		log.debug("Page action on window ==>" + actionElementName + " >>> " + int1 + ">>>>" + int2);

		Method actionMethod = webPageClaszz.getDeclaredMethod(action, paramClazz);

		actionMethod.invoke(webPage, int1, int2);

	}

	private void performAction(WebPage webPage, String actionElementName, String action)
			throws ClassNotFoundException, NoSuchMethodException, SecurityException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException {

		Class<?> webPageClaszz = getWebPageClass();

		log.debug("Page action on window ==>" + actionElementName);

		Method actionMethod = webPageClaszz.getDeclaredMethod(action, null);

		actionMethod.invoke(webPage);
	}

	/**
	 * 
	 * @return iTafElement class object
	 * @throws ClassNotFoundException
	 */
	private Class<?> getWebPageClass() throws ClassNotFoundException {
		return Class.forName("com.etouch.taf.webui.selenium.WebPage");
	}

}
