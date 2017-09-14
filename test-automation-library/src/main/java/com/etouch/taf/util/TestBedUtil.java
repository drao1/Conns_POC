package com.etouch.taf.util;

import com.etouch.taf.core.TestBed;
import com.etouch.taf.core.config.TestBedConfig;
import com.etouch.taf.core.config.TestBedManagerConfiguration;
import com.etouch.taf.core.resources.TestBedType;
import com.etouch.taf.core.resources.TestTypes;
import com.etouch.taf.webui.ITafElementFactory;
import com.etouch.taf.webui.MobileElementFactory;
import com.etouch.taf.webui.TafElementFactoryManager;
import com.etouch.taf.webui.WebElementFactory;

/**
 * A helper class to interface TestNG concurrency usage.
 * 
 * @author <a href="mailto:the_mindstorm@evolva.ro>Alex Popescu</a>
 */
public class TestBedUtil {

	/**
	 * @return true if the current thread was created by TestNG.
	 */
	public static String identifyTestBed() {
		String testBedName = null;

		ThreadGroup currentGroup = Thread.currentThread().getThreadGroup();

		int noThreads = currentGroup.activeCount();

		Thread[] lstThreads = new Thread[noThreads];
		currentGroup.enumerate(lstThreads);

		for (int i = 0; i < noThreads; i++) {
			System.out.println("Thread No:" + i + " = " + lstThreads[i].getName());

			if (TestBedType.isSupported(lstThreads[i].getName())) {
				testBedName = lstThreads[i].getName();
				break;
			}
		}

		return testBedName;

	}

	public static final TestBed currentTestBedInfo() {
		TestBed testBed = null;
		String testBedName = identifyTestBed();

		if (testBedName != null) {
			testBed = loadTestBedDetails(testBedName);
		}
		return testBed;
	}

	/**
	 * 
	 * Loads Web Testbed details from devConfig.xml
	 * 
	 * @param testBedName
	 *            the test bed name
	 * @return the test bed
	 */
	public static TestBed loadTestBedDetails(String testBedName) {

		TestBed currentTestBed = null;
		TestBedManagerConfiguration testBedMgrConfig = TestBedManagerConfiguration.getInstance();
		ITafElementFactory webElementFactory = new WebElementFactory();
		ITafElementFactory mobileElementFactory = new MobileElementFactory();

		if (ConfigUtil.isWebTestTypeEnabled()) {
			populateFactoryManager(TestTypes.WEB.getTestType(), webElementFactory);
			for (TestBedConfig tbConfig : testBedMgrConfig.getWebConfig().getTestBeds()) {

				if (tbConfig.getTestBedName().equalsIgnoreCase(testBedName)) {
					currentTestBed = copyTestBedDetails(tbConfig, TestTypes.WEB.getTestType());
					break;
				}
			}
		}
		if (ConfigUtil.isMobileTestTypeEnabled()) {
			populateFactoryManager(TestTypes.MOBILE.getTestType(), mobileElementFactory);
			if (currentTestBed == null) {
				for (TestBedConfig tbConfig : testBedMgrConfig.getMobileConfig().getTestBeds()) {
					if (tbConfig.getTestBedName().equalsIgnoreCase(testBedName)) {
						currentTestBed = copyTestBedDetails(tbConfig, TestTypes.MOBILE.getTestType());
						break;
					}
				}
			}
		}
		return currentTestBed;
	}

	private static void populateFactoryManager(String testType, ITafElementFactory factory) {
		TafElementFactoryManager.setFactory(testType, factory);
	}

	/**
	 * Copy test bed details.
	 * 
	 * @param testBedConfig
	 *            the test bed config
	 * @return the test bed
	 */
	private static TestBed copyTestBedDetails(TestBedConfig testBedConfig, String testType) {
		TestBed currentTestBed = new TestBed();
		if (testBedConfig != null) {

			currentTestBed.setTestBedName(testBedConfig.getTestBedName());
			currentTestBed.setPlatform(testBedConfig.getPlatform());
			currentTestBed.setBrowser(testBedConfig.getBrowser());
			currentTestBed.setApp(testBedConfig.getApp());
			currentTestBed.setDevice(testBedConfig.getDevice());
			currentTestBed.setTestBedName(testBedConfig.getTestBedName());
			currentTestBed.setTestbedClassName(testBedConfig.getTestbedClassName());
			currentTestBed.setPort(testBedConfig.getPort());
			currentTestBed.setTestType(testType);

		}

		return currentTestBed;
	}

}
