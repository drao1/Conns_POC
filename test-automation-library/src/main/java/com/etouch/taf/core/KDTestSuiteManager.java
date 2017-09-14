package com.etouch.taf.core;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.testng.ISuiteListener;
import org.testng.ITestListener;
import org.testng.TestNG;
import org.testng.xml.XmlClass;
import org.testng.xml.XmlSuite;
import org.testng.xml.XmlTest;

import com.etouch.taf.core.config.KDConfig;
import com.etouch.taf.core.config.TestBedManagerConfiguration;
import com.etouch.taf.core.listener.IndexMethodInterceptorListener;
import com.etouch.taf.core.listener.SuiteListener;
import com.etouch.taf.core.listener.TestListener;
import com.etouch.taf.core.resources.KDConstants;
import com.etouch.taf.kd.config.TestAction;
import com.etouch.taf.kd.config.TestSuite;
import com.etouch.taf.kd.exception.DuplicateDataKeyException;
import com.etouch.taf.kd.exception.InvalidActionException;
import com.etouch.taf.kd.exception.InvalidValueException;
import com.etouch.taf.util.ConfigUtil;
import com.etouch.taf.util.LogUtil;
import com.etouch.taf.util.report.KDTestNgReporter;

/**
 * <H2>Controller Class for Initiating TestNG executor for Keyword Driven tests
 * </H2>
 * <p>
 * Custom Listener classes -
 * <ol>
 * <li>TestSuiteListener - For Suite level test case execution and Test Bed
 * configuration</li>
 * <li>TestClassListener - For unit level Test Cases execution</li>
 * <li>IndexMethodInterceptorListener - For Indexing the Test Cases when
 * Parallel mode is false</li>
 * <li>IReporter - For Generating Custom report called -
 * KDTestNgCustomReport.html in the default 'test-output' directory</li>
 * </ol>
 * </p>
 * 
 * <p>
 * The TestNG Object is created using API provided, encapsulating the Test cases
 * generated with KD TestSuite Object graph
 * </p>
 * 
 * @author eTouch Systems Corporation
 *
 */
public class KDTestSuiteManager {

	private static Log log = LogUtil.getLog(KDTestSuiteManager.class);

	private KDConfig kdConfig = TestBedManagerConfiguration.getInstance().getKdConfig();

	/**
	 * <ol>
	 * <li>Creates the TestNG object.</li>
	 * <li>Configures the listner classes</li>
	 * <li>Set the TestNG thread count = ( Nos. of TestBeds) X ( Nos. of
	 * TestSuites )</li>
	 * </ol>
	 * 
	 * @param collection
	 *            of TestSuites
	 * 
	 * @return TestNG object configured with TestCases to be executed as the
	 *         configuration setting.
	 * 
	 * @throws ClassNotFoundException
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 * @throws DuplicateDataKeyException
	 * @throws InvalidValueException
	 * @throws InvalidActionException
	 * @throws IOException
	 * @throws InvalidFormatException
	 */
	@SuppressWarnings("unchecked")
	public TestNG getTestNGSuite(Collection<TestSuite> testSuites)
			throws ClassNotFoundException, InstantiationException, IllegalAccessException, InvalidFormatException,
			IOException, InvalidActionException, InvalidValueException, DuplicateDataKeyException {

		TestNG testng = this.getTestNG(testSuites);

		setListener(testng);

		int threadCount = ConfigUtil.getTestBeds().size() * testSuites.size();
		testng.setSuiteThreadPoolSize(threadCount);
		return testng;
	}

	/**
	 * Settup the Listener classes
	 * 
	 * @param testng
	 * @throws ClassNotFoundException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 */
	@SuppressWarnings("unchecked")
	private void setListener(TestNG testng)
			throws ClassNotFoundException, InstantiationException, IllegalAccessException {

		ISuiteListener suiteListener = new SuiteListener();
		testng.addListener(suiteListener);

		ITestListener testListener = new TestListener();
		testng.addListener(testListener);

		if (!kdConfig.getParallelMode().contains(KDConstants.PARALLEL_TESTCASES.getValue())) {
			testng.addListener(new IndexMethodInterceptorListener());
		}

		testng.addListener(new KDTestNgReporter());
	}

	private TestNG getTestNG(Collection<TestSuite> testSuites) throws InvalidFormatException, IOException,
			InvalidActionException, InvalidValueException, DuplicateDataKeyException {

		log.debug("Creating TestNG suites and classes...");

		List<XmlSuite> xmlSuites = new ArrayList<>();

		if (kdConfig.getParallelMode().contains(KDConstants.PARALLEL_TESTBEDS.getValue())) {
			xmlSuites = buildParallelTestBed(testSuites, xmlSuites);
		} else {
			xmlSuites = buildIndexedTestBed(testSuites, xmlSuites);
		}

		TestNG tng = new TestNG();
		tng.setXmlSuites(xmlSuites);

		log.debug("Returning TestNG object [Default Name ]==>" + tng.getDefaultSuiteName() + " :: ");
		return tng;
	}

	/**
	 * @param testSuites
	 * @param xmlSuites
	 * @return
	 * @throws InvalidFormatException
	 * @throws IOException
	 * @throws InvalidActionException
	 * @throws InvalidValueException
	 * @throws DuplicateDataKeyException
	 */
	private List<XmlSuite> buildIndexedTestBed(Collection<TestSuite> testSuites, List<XmlSuite> xmlSuites) {

		XmlSuite suite = new XmlSuite();
		suite.setName(KDConstants.TB_SHEETNAME_COMMON.getValue());

		for (String testBed : ConfigUtil.getTestBeds()) {
			for (TestSuite testSuite : testSuites) {
				if (validTestSuite(testSuite)) {
					buildTestCase(testBed, testSuite, suite);
				}
			}
		}

		if (kdConfig.getParallelMode().trim().equals(KDConstants.PARALLEL_TESTSUITES.getValue())) {
			suite.setParallel(KDConstants.TESTS.getValue());
			suite.setThreadCount(getTestSuiteThreadCount(testSuites));
		}

		log.debug(suite.toXml());
		xmlSuites.add(suite);

		return xmlSuites;

	}

	private int getTestSuiteThreadCount(Collection<TestSuite> testSuites) {
		int parallelTestSuiteThread = 0;

		for (TestSuite testSuite : testSuites) {
			if (validTestSuite(testSuite)) {
				parallelTestSuiteThread++;
			}
		}

		return parallelTestSuiteThread;
	}

	private List<XmlSuite> buildParallelTestBed(Collection<TestSuite> testSuites, List<XmlSuite> xmlSuites) {

		for (String testBed : ConfigUtil.getTestBeds()) {

			for (TestSuite testSuite : testSuites) {

				if (validTestSuite(testSuite)) {

					XmlSuite suite = new XmlSuite();
					suite.setName(testSuite.getName() + KDConstants.TS_SUITENAME.getValue() + testBed);

					buildTestCase(testBed, testSuite, suite);

					log.debug(suite.toXml());
					xmlSuites.add(suite);
				}
			}
		}
		return xmlSuites;

	}

	private boolean validTestSuite(TestSuite testSuite) {

		boolean isValidSuite = false;

		for (TestAction testAction : testSuite.getTestActions()) {
			if (testAction.isExecute()) {
				isValidSuite = true;
				break;
			}
		}

		return isValidSuite;
	}

	/**
	 * @param testBed
	 * @param testSuite
	 * @param suite
	 */
	private void buildTestCase(String testBed, TestSuite testSuite, XmlSuite suite) {

		XmlTest test = new XmlTest(suite);
		test.setName(testSuite.getName() + KDConstants.TS_TESTNAME.getValue() + testBed);

		Map<String, String> params = new HashMap<>();
		params.put("testBedName", testBed);
		params.put("testSuiteName", testSuite.getName());
		test.setParameters(params);

		List<XmlClass> classes = new ArrayList<>();
		classes.add(new XmlClass("com.etouch.taf.kd.config.KDTestActionFactory"));
		test.setXmlClasses(classes);

		if (kdConfig.getParallelMode().trim().contains(KDConstants.PARALLEL_TESTCASES.getValue())) {
			test.setParallel(KDConstants.INSTANCES.getValue());
			test.setThreadCount(getTestActionThreadCount(testSuite.getTestActions()));
		}

	}

	private int getTestActionThreadCount(ArrayList<TestAction> testActions) {

		int testActionThreadCount = 0;

		for (TestAction testAction : testActions) {
			testActionThreadCount = getTotalTestActions(testAction);
		}

		return testActionThreadCount;
	}

	/**
	 * @param testActionThreadCount
	 * @param testAction
	 * @return
	 */
	private int getTotalTestActions(TestAction testAction) {

		int totalTestActions = 0;

		if (testAction.isExecute()) {

			if (testAction.getKdDataSetColls() != null && testAction.getKdDataSetColls().isEmpty()) {

				totalTestActions = testAction.getKdDataSetColls().size();
			} else {
				totalTestActions++;
			}
		}
		return totalTestActions;
	}

}
