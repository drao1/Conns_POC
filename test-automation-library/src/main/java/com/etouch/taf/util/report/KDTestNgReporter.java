package com.etouch.taf.util.report;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.testng.IReporter;
import org.testng.IResultMap;
import org.testng.ISuite;
import org.testng.ISuiteResult;
import org.testng.ITestContext;
import org.testng.ITestNGMethod;
import org.testng.ITestResult;
import org.testng.internal.Utils;
import org.testng.xml.XmlSuite;

import com.etouch.taf.core.KDManager;
import com.etouch.taf.core.TestActionExecutor;
import com.etouch.taf.core.TestBedManager;
import com.etouch.taf.core.config.KDConfig;
import com.etouch.taf.core.resources.KDConstants;
import com.etouch.taf.kd.config.KDDataset;
import com.etouch.taf.kd.config.TestAction;
import com.etouch.taf.kd.config.TestSuite;
import com.etouch.taf.kd.exception.DuplicateDataKeyException;
import com.etouch.taf.kd.exception.InvalidActionException;
import com.etouch.taf.kd.exception.InvalidValueException;
import com.etouch.taf.util.CommonUtil;
import com.etouch.taf.util.ConfigUtil;
import com.etouch.taf.util.LogUtil;

/**
 * <p>
 * <h2>Custom TestNG Report</h2>
 * </p>
 * 
 * <p>
 * It generates the TestNG report and formated the result as per the KD Excel
 * sheet test cases. The report also display the error messages if any issues
 * observed in the KD Excel sheets for Format / Junk Chars / Unmatched or
 * Incomplete Data set and/or Test Suites and Test Actions
 * </p>
 * 
 * @author eTouch Systems Corporation
 * 
 */
public class KDTestNgReporter implements IReporter, Observer {

	private static Log log = LogUtil.getLog(KDTestNgReporter.class);

	private List<XmlSuite> xmlSuites = null;
	private List<ISuite> iSuites = null;

	@Override
	public void generateReport(List<XmlSuite> xmlSuites, List<ISuite> suites, String outputDirectory) {

		this.xmlSuites = xmlSuites;
		this.iSuites = suites;

		// To be configured from devConfig.yml # PDF, EXCEl, DOC, EMAIL
		String reportFormat = "HTML";
		try {

			String reportTemplate = ReportTemplate.valueOf(reportFormat).getTemplate();
			buildReport(reportTemplate, outputDirectory);

		} catch (Exception exception) {

			log.error(exception);
		}

	}

	private KDConfig kdConfig = TestBedManager.INSTANCE.getKdConfig();

	private void buildReport(String reportTemplate, String outputDirectory) throws IOException, InvalidFormatException, InvalidActionException, InvalidValueException,
			DuplicateDataKeyException {

		String htmlReport = buildReportHeader(reportTemplate);

		htmlReport = CommonUtil.replaceText(htmlReport, "#executedTestSuites", getExecutedTestSuites());

		htmlReport = CommonUtil.replaceText(htmlReport, "#skippedTestCases", getSkippedTestCases());

		htmlReport = CommonUtil.replaceText(htmlReport, "#failTestCaseDetails", getFailedTestCases());

		htmlReport = CommonUtil.replaceText(htmlReport, "#summary", getSummary());

		Utils.writeFile(outputDirectory, "KDTestNgCustomReport.html", htmlReport);

	}

	/**
	 * @param reportTemplate
	 * @return
	 */
	private String buildReportHeader(String reportTemplate) {

		File kdFile = new File(kdConfig.getFilePath());
		String executionStyle = kdConfig.getParallelMode();

		String fileName = kdFile.getName();

		String htmlReport = CommonUtil.replaceText(reportTemplate, "#fileName", fileName);

		htmlReport = CommonUtil.replaceText(htmlReport, "#executionStyle", executionStyle);

		return htmlReport;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.testng.IReporter#generateReport(java.util.List, java.util.List,
	 * java.lang.String)
	 */
	@Override
	public void update(Observable o, Object arg) {

		String outputDirectory = "test-output";

		isDirectoryExists(outputDirectory);
		log.info("Displaying Excel validation error messages");

		try {

			@SuppressWarnings("unchecked")
			Map<String, List<String>> excelErrorMessagesMap = (Map<String, List<String>>) arg;
			String reportTemplate = ReportTemplate.EXCEL_ERRORS.getTemplate();
			buildErrorReport(reportTemplate, outputDirectory, excelErrorMessagesMap);

		} catch (InvalidFormatException | IOException | InvalidActionException | InvalidValueException | DuplicateDataKeyException e) {

			log.error("Fail to generate excell error report... " + e.getCause());

			log.debug(e);
		}

	}

	private void isDirectoryExists(String filePath) {
		File file = new File(filePath);
		if (!file.exists()) {
			file.mkdir();
		}

	}

	private void buildErrorReport(String reportTemplate, String outputDirectory, Map<String, List<String>> excelErrorMessagesMap) throws IOException, InvalidFormatException,
			InvalidActionException, InvalidValueException, DuplicateDataKeyException {

		log.info("Building Error Report");

		String excelErrorReport = buildReportHeader(reportTemplate);

		excelErrorReport = CommonUtil.replaceText(excelErrorReport, "#errorMessageList", getXlErrorMessages(excelErrorMessagesMap));

		Utils.writeFile(outputDirectory, "KDTestNgCustomReport.html", excelErrorReport);

	}

	private String getXlErrorMessages(Map<String, List<String>> excelErrorMessagesMap) throws IOException {

		StringBuffer errorList = new StringBuffer();

		for (Map.Entry<String, List<String>> entry : excelErrorMessagesMap.entrySet()) {

			if (entry.getValue() != null && !entry.getValue().isEmpty()) {

				String xlSheetName = entry.getKey();

				String errorMessageListTemplate = ReportTemplate.ERROR_MESSAGE_LIST.getTemplate();

				errorMessageListTemplate = CommonUtil.replaceText(errorMessageListTemplate, "#sheetName", xlSheetName);

				StringBuffer errorMessages = new StringBuffer();

				List<String> errorMessageList = entry.getValue();

				if (errorMessageList.size() > 0) {
					for (String errorMessage : errorMessageList) {
						String errorMessageTemplate = ReportTemplate.ERROR_MESSAGE.getTemplate();
						errorMessageTemplate = CommonUtil.replaceText(errorMessageTemplate, "#errorMessage", errorMessage);
						errorMessages.append(errorMessageTemplate);
					}
				}

				errorMessageListTemplate = CommonUtil.replaceText(errorMessageListTemplate, "#errorMessages", errorMessages.toString());

				errorList.append(errorMessageListTemplate);

			}
		}

		return errorList.toString();
	}

	private String createExecutedTestCaseHeader(LinkedList<String> browserList) throws IOException {

		StringBuffer executedTestCaseRecordHeader = new StringBuffer();

		String executedTestCaseTemplate = ReportTemplate.EXECUTED_TEST_CASES.getTemplate();

		executedTestCaseRecordHeader.append(CommonUtil.replaceText(executedTestCaseTemplate, "#executedTestCase", "Test Case Name"));

		for (String browserName : browserList) {
			executedTestCaseRecordHeader.append(CommonUtil.replaceText(executedTestCaseTemplate, "#executedTestCase", browserName));
		}

		return executedTestCaseRecordHeader.toString();
	}

	private Map<String, LinkedList<Map<String, String>>> buildExecutedTestCases() {

		Map<String, LinkedList<Map<String, String>>> testActionBrowserStatus = new HashMap<String, LinkedList<Map<String, String>>>();

		for (ISuite suite : iSuites) {

			// Getting the results for the said suite
			Map<String, ISuiteResult> suiteResults = suite.getResults();

			getTestActionBrowserMap(suiteResults, testActionBrowserStatus);
		}

		return testActionBrowserStatus;
	}

	private void getTestActionBrowserMap(Map<String, ISuiteResult> suiteResults, Map<String, LinkedList<Map<String, String>>> testActionBrowserStatus) {

		for (ISuiteResult sr : suiteResults.values()) {

			ITestContext tc = sr.getTestContext();

			getTestCaseBrowserMap(tc, testActionBrowserStatus);

			log.debug("Execute test cases ==>" + testActionBrowserStatus.size());

		}
	}

	private void getTestCaseBrowserMap(ITestContext tc, Map<String, LinkedList<Map<String, String>>> testActionBrowserStatus) {

		ITestNGMethod[] allTestNGMethods = tc.getAllTestMethods();

		for (ITestNGMethod testNGMethod : allTestNGMethods) {

			TestActionExecutor testActionExecutor = (TestActionExecutor) testNGMethod.getInstance();

			String browserStatus = getBrowserStatus(tc, testNGMethod);

			populateTestActionResultMap(testActionBrowserStatus, testActionExecutor, browserStatus);
		}

	}

	/**
	 * @param kdDataset
	 * @return
	 */
	private String getDataLabel(KDDataset kdDataset) {

		Map<String, Object> kdDataMap = kdDataset.getDataSetMap();

		String dataLabel = "";

		if ((kdDataMap != null) && (kdDataMap.size() > 0)) {

			if (kdDataMap.containsKey(KDConstants.TD_LABEL.getValue()) && StringUtils.isNotBlank(kdDataMap.get(KDConstants.TD_LABEL.getValue()).toString())) {
				dataLabel = kdDataMap.get(KDConstants.TD_LABEL.getValue()).toString();
			} else {
				dataLabel = createLabel(kdDataMap);
			}
		}
		return dataLabel;
	}

	/**
	 * @param kdDataMap
	 * @param dataLabel
	 * @return
	 */
	private String createLabel(Map<String, Object> kdDataMap) {

		String dataLabel = "";

		for (Map.Entry<String, Object> dataSet : kdDataMap.entrySet()) {

			dataLabel = dataLabel.concat(dataSet.getValue().toString() + "/");

		}
		return dataLabel;
	}

	/**
	 * @param testActionBrowserStatus
	 * @param testActionExecutor
	 * @param dataLabel
	 * @param browserStatus
	 */
	private void populateTestActionResultMap(Map<String, LinkedList<Map<String, String>>> testActionBrowserStatus, TestActionExecutor testActionExecutor, String browserStatus) {

		Map<String, String> browserStatusMap = new HashMap<String, String>();
		browserStatusMap.put(testActionExecutor.getTestBedName(), browserStatus);

		String testActionName = formatTestActionName(testActionExecutor);

		// verify if the TestAction map exists
		if (testActionBrowserStatus.containsKey(testActionName)) {
			LinkedList<Map<String, String>> existingBrowserStatusList = testActionBrowserStatus.get(testActionName);
			existingBrowserStatusList.add(browserStatusMap);
		} else {
			// create new Test Action map
			LinkedList<Map<String, String>> newBrowserStatusList = new LinkedList<Map<String, String>>();
			newBrowserStatusList.add(browserStatusMap);
			testActionBrowserStatus.put(testActionName, newBrowserStatusList);
		}
	}

	/**
	 * @param testActionExecutor
	 * @return
	 */
	private String formatTestActionName(TestActionExecutor testActionExecutor) {

		String testActionName = testActionExecutor.getTestAction().getActionName();

		if (testActionExecutor.getKdDataSet() != null) {
			String dataLabel = getDataLabel(testActionExecutor.getKdDataSet());
			testActionName = testActionName + " { " + dataLabel + " } ";
		}

		return testActionName;
	}

	private String getBrowserStatus(ITestContext tc, ITestNGMethod testNGMethod) {

		String browserStatus;

		int status = getTestResultStatus(tc, testNGMethod);

		switch (status) {

		case ITestResult.SUCCESS:
			browserStatus = "passed";
			break;

		case ITestResult.FAILURE:
			browserStatus = "failed";
			break;

		case ITestResult.SKIP:
			browserStatus = "skipped";
			break;

		default:
			browserStatus = "skipped";
			break;
		}

		return browserStatus;
	}

	/**
	 * @param tc
	 * @param testNGMethod
	 * @return
	 */
	private int getTestResultStatus(ITestContext tc, ITestNGMethod testNGMethod) {

		IResultMap passedTestMap = tc.getPassedTests();
		Set<ITestResult> passedTestResultSet = passedTestMap.getResults(testNGMethod);

		IResultMap failedTestMap = tc.getFailedTests();
		Set<ITestResult> failedTestResultSet = failedTestMap.getResults(testNGMethod);

		IResultMap skippedTestMap = tc.getSkippedTests();
		Set<ITestResult> skippedTestResultSet = skippedTestMap.getResults(testNGMethod);

		int status = 0;

		if (passedTestResultSet.size() > 0) {
			status = passedTestResultSet.iterator().next().getStatus();
		} else if (failedTestResultSet.size() > 0) {
			status = failedTestResultSet.iterator().next().getStatus();
		} else if (skippedTestResultSet.size() > 0) {
			status = skippedTestResultSet.iterator().next().getStatus();
		}

		return status;
	}

	private String getSkippedTestCases() throws IOException, InvalidFormatException, InvalidActionException, InvalidValueException, DuplicateDataKeyException {

		StringBuffer skippedTestCases = new StringBuffer();

		Collection<TestSuite> testSuiteColls = KDManager.getInstance().getTestSuites();

		for (TestSuite testSuite : testSuiteColls) {

			String skippedTestSuite = ReportTemplate.SKIPPED_TEST_SUITES.getTemplate();

			skippedTestSuite = CommonUtil.replaceText(skippedTestSuite, "#suiteName", testSuite.getName());

			StringBuffer skippedTestCaseTable = new StringBuffer();

			List<TestAction> testActionList = testSuite.getTestActions();

			for (TestAction testAction : testActionList) {

				if (!testAction.isExecute()) {

					String skippedTestCaseTemplate = ReportTemplate.SKIPPED_TEST_CASES.getTemplate();

					String skippedTestCaseRow = CommonUtil.replaceText(skippedTestCaseTemplate, "#skippedTestCases", testAction.getActionName());

					skippedTestCaseTable.append(skippedTestCaseRow);

				}
			}

			if (skippedTestCaseTable.length() > 0) {
				skippedTestCases.append(CommonUtil.replaceText(skippedTestSuite, "#skippedTestCaseTable", skippedTestCaseTable.toString()));
			}
		}

		return skippedTestCases.toString();
	}

	private String getExecutedTestSuites() throws InvalidFormatException, IOException, InvalidActionException, InvalidValueException, DuplicateDataKeyException {

		StringBuffer executedTestSuiteList = new StringBuffer();

		Map<String, LinkedList<Map<String, String>>> testActionBrowserStatusMap = buildExecutedTestCases();

		Collection<TestSuite> testSuiteColls = KDManager.getInstance().getTestSuites();

		Set<TestSuite> executedTestSuites = filterExecutedTestSuites(testSuiteColls, testActionBrowserStatusMap);

		for (TestSuite testSuite : executedTestSuites) {

			createExecutedTestSuites(executedTestSuiteList, testActionBrowserStatusMap, testSuite);

		}

		return executedTestSuiteList.toString();

	}

	private Set<TestSuite> filterExecutedTestSuites(Collection<TestSuite> testSuiteColls, Map<String, LinkedList<Map<String, String>>> testActionBrowserStatusMap) {

		Set<TestSuite> executedTestSuites = new HashSet<TestSuite>();

		for (TestSuite testSuite : testSuiteColls) {

			for (TestAction testAction : testSuite.getTestActions()) {

				Collection<String> exeTestActionNames = getExecutedTestActionNameList(testAction.getKdDataSetColls(), testAction.getActionName());

				for (String actionName : exeTestActionNames) {
					if (testActionBrowserStatusMap.containsKey(actionName)) {
						executedTestSuites.add(testSuite);
					}
				}
			}
		}

		return executedTestSuites;
	}

	/**
	 * @param executedTestSuiteList
	 * @param testActionBrowserStatusMap
	 * @param testSuite
	 * @throws IOException
	 */
	private void createExecutedTestSuites(StringBuffer executedTestSuiteList, Map<String, LinkedList<Map<String, String>>> testActionBrowserStatusMap, TestSuite testSuite)
			throws IOException {

		String executedTestSuite = ReportTemplate.EXECUTED_TEST_SUITES.getTemplate();
		executedTestSuite = CommonUtil.replaceText(executedTestSuite, "#suiteName", testSuite.getName());

		LinkedList<String> browserList = new LinkedList<String>();
		browserList.addAll(ConfigUtil.getTestBeds());

		StringBuffer executedTestCaseRecordList = new StringBuffer();

		// Adding Test Case table header
		executedTestCaseRecordList.append(createExecutedTestCaseHeader(browserList));

		List<TestAction> testActionList = testSuite.getTestActions();

		for (TestAction testAction : testActionList) {

			Collection<String> exeTestActionNames = getExecutedTestActionNameList(testAction.getKdDataSetColls(), testAction.getActionName());

			createExecutedTestCases(testActionBrowserStatusMap, browserList, executedTestCaseRecordList, exeTestActionNames);

		}

		executedTestSuite = CommonUtil.replaceText(executedTestSuite, "#executedTestCasesSuites", executedTestCaseRecordList.toString());

		executedTestSuiteList.append(executedTestSuite);
	}

	/**
	 * @param testActionBrowserStatusMap
	 * @param testActionBrowserStatusMap
	 * @param browserList
	 * @param executedTestCaseRecordList
	 * @param exeTestActionNames
	 * @throws IOException
	 */
	private void createExecutedTestCases(Map<String, LinkedList<Map<String, String>>> testActionBrowserStatusMap, LinkedList<String> browserList,
			StringBuffer executedTestCaseRecordList, Collection<String> exeTestActionNames) throws IOException {

		if (exeTestActionNames.size() > 0) {

			for (String testActionName : exeTestActionNames) {

				createExecutedTestCaseRecords(testActionBrowserStatusMap, browserList, executedTestCaseRecordList, testActionName);
			}
		}
	}

	/**
	 * @param testActionBrowserStatusMap
	 * @param testActionBrowserStatusMap
	 * @param browserList
	 * @param executedTestCaseRecordList
	 * @param testActionName
	 * @throws IOException
	 */
	private void createExecutedTestCaseRecords(Map<String, LinkedList<Map<String, String>>> testActionBrowserStatusMap, LinkedList<String> browserList,
			StringBuffer executedTestCaseRecordList, String testActionName) throws IOException {

		if (testActionBrowserStatusMap.containsKey(testActionName)) {

			StringBuffer executedTestCaseRecord = new StringBuffer();

			String executedTestCase = ReportTemplate.EXECUTED_TEST_CASES.getTemplate();

			executedTestCaseRecord.append(CommonUtil.replaceText(executedTestCase, "#executedTestCase", testActionName));

			LinkedList<Map<String, String>> browserStatusMapList = testActionBrowserStatusMap.get(testActionName);

			populateBrowserStatus(browserList, browserStatusMapList, executedTestCaseRecord, executedTestCase);

			String executedTestCaseResults = ReportTemplate.EXECUTED_TEST_CASE_LIST.getTemplate();

			executedTestCaseRecordList.append(CommonUtil.replaceText(executedTestCaseResults, "#executedTestCaseRecord", executedTestCaseRecord.toString()));

		}
	}

	/**
	 * @param browserList
	 * @param browserStatusMapList
	 * @param executedTestCaseRecord
	 * @param executedTestCase
	 * @return
	 * @throws IOException
	 */
	private void populateBrowserStatus(LinkedList<String> browserList, LinkedList<Map<String, String>> browserStatusMapList, StringBuffer executedTestCaseRecord,
			String executedTestCase) throws IOException {

		for (String browserName : browserList) {

			String browserStatus = ReportTemplate.BROWSER_STATUS.getTemplate();

			String browserResult = "";

			for (Map<String, String> browserStatusMap : browserStatusMapList) {

				if (browserStatusMap.containsKey(browserName)) {
					browserResult = browserStatusMap.get(browserName);
				}
			}

			browserStatus = CommonUtil.replaceText(browserStatus, "#browserStatus", browserResult);

			executedTestCaseRecord.append(CommonUtil.replaceText(executedTestCase, "#executedTestCase", browserStatus));
		}
	}

	private Collection<String> getExecutedTestActionNameList(Collection<KDDataset> kdDataSetColls, String actionName) {

		Collection<String> executedTestActionNameList = new ArrayList<String>();

		if (!kdDataSetColls.isEmpty()) {

			executedTestActionNameList = getAllExecutedTestActionName(kdDataSetColls, actionName);
		} else {
			executedTestActionNameList.add(actionName);
		}

		return executedTestActionNameList;
	}

	/**
	 * @param kdDataSetColls
	 * @param actionName
	 * @param executedTestActionNameList
	 * @return
	 */
	private Collection<String> getAllExecutedTestActionName(Collection<KDDataset> kdDataSetColls, String actionName) {

		Collection<String> allTestActionNameColls = new ArrayList<String>();

		for (KDDataset kdDataset : kdDataSetColls) {

			String dataLabel = getDataLabel(kdDataset);

			String actionDataLabelName = actionName + " { " + dataLabel + " } ";

			allTestActionNameColls.add(actionDataLabelName);
		}

		return allTestActionNameColls;
	}

	private String getFailedTestCases() throws IOException {

		String failedTestCases = "";

		String failTestCaseDetails = getFailTestCaseDetails();

		if (StringUtils.isNotBlank(failTestCaseDetails)) {
			failedTestCases = ReportTemplate.FAILED_TEST_CASE_DETAILS.getTemplate();
			failedTestCases = CommonUtil.replaceText(failedTestCases, "#failTestCaseLists", failTestCaseDetails);
		}

		return failedTestCases;
	}

	private String getFailTestCaseDetails() throws IOException {

		StringBuffer failedTestCaseDetails = new StringBuffer();

		for (ISuite suite : iSuites) {
			// Getting the results for the said suite
			Map<String, ISuiteResult> suiteResults = suite.getResults();

			for (ISuiteResult sr : suiteResults.values()) {

				buildFailedTestDetails(failedTestCaseDetails, sr);
			}
		}

		return failedTestCaseDetails.toString();
	}

	/**
	 * @param failedTestCaseDetails
	 * @param sr
	 * @throws IOException
	 */
	private void buildFailedTestDetails(StringBuffer failedTestCaseDetails, ISuiteResult sr) throws IOException {
		ITestContext tc = sr.getTestContext();

		IResultMap failedTestMap = tc.getFailedTests();

		if (failedTestMap.getAllResults().size() > 0) {

			ITestNGMethod[] allTestNGMethods = tc.getAllTestMethods();

			for (ITestNGMethod testNGMethod : allTestNGMethods) {

				populateFailedTestDetails(failedTestCaseDetails, failedTestMap, testNGMethod);
			}
		}
	}

	/**
	 * @param failedTestCaseDetails
	 * @param failedTestMap
	 * @param testNGMethod
	 * @throws IOException
	 */
	private void populateFailedTestDetails(StringBuffer failedTestCaseDetails, IResultMap failedTestMap, ITestNGMethod testNGMethod) throws IOException {

		TestActionExecutor testActionExecutor = (TestActionExecutor) testNGMethod.getInstance();

		Set<ITestResult> failedTestResultSet = failedTestMap.getResults(testNGMethod);

		if (!failedTestResultSet.isEmpty()) {

			Throwable error = failedTestResultSet.iterator().next().getThrowable();

			String testCaseRecord = ReportTemplate.FAILED_TEST_CASE_LISTS.getTemplate();

			testCaseRecord = CommonUtil.replaceText(testCaseRecord, "#testSuiteName", testActionExecutor.getTestSuiteName());

			testCaseRecord = CommonUtil.replaceText(testCaseRecord, "#testCaseName", testActionExecutor.getTestAction().getActionName());

			testCaseRecord = CommonUtil.replaceText(testCaseRecord, "#browser", testActionExecutor.getTestBedName());

			testCaseRecord = CommonUtil.replaceText(testCaseRecord, "#errorDetails", CommonUtil.getErrorDetails(error));

			failedTestCaseDetails.append(testCaseRecord);
		}

	}

	/**
	 * @return
	 * @throws IOException
	 * @throws InvalidFormatException
	 * @throws InvalidActionException
	 * @throws InvalidValueException
	 * @throws DuplicateDataKeyException
	 */
	private String getSummary() throws IOException, InvalidFormatException, InvalidActionException, InvalidValueException, DuplicateDataKeyException {

		String summary = ReportTemplate.SUMMARY.getTemplate();

		summary = formatBrowserHeader(summary);

		summary = CommonUtil.replaceText(summary, "#testSuiteSummary", getTestSuiteSummary());

		return summary;
	}

	/**
	 * @param summary
	 * @return
	 * @throws IOException
	 */
	private String formatBrowserHeader(String summary) throws IOException {

		Map<String, LinkedList<Map<String, String>>> testActionBrowserStatusMap = buildExecutedTestCases();

		Collection<LinkedList<Map<String, String>>> browserMapColl = testActionBrowserStatusMap.values();

		LinkedList<Map<String, String>> browserMapList = getBrowserNames(browserMapColl);

		StringBuffer browserHdrNames = new StringBuffer();
		StringBuffer testCaseStatusHdr = new StringBuffer();

		for (Map<String, String> browserMap : browserMapList) {

			String browserName = browserMap.entrySet().iterator().next().getKey();

			browserHdrNames.append(getBrowserHeaderName(browserName));

			testCaseStatusHdr.append(getTestCaseStatusHeader());
		}

		summary = CommonUtil.replaceText(summary, "#browserHeaderName", browserHdrNames.toString());

		summary = CommonUtil.replaceText(summary, "#browserSummary", testCaseStatusHdr.toString());

		return summary;
	}

	/**
	 * @param browserMapColl
	 * @return
	 */
	private LinkedList<Map<String, String>> getBrowserNames(Collection<LinkedList<Map<String, String>>> browserMapColl) {

		LinkedList<Map<String, String>> browserMapList;

		if (!browserMapColl.isEmpty()) {
			browserMapList = browserMapColl.iterator().next();
		} else {
			browserMapList = new LinkedList<Map<String, String>>();

			Collection<String> testBedNames = ConfigUtil.getTestBeds();
			for (String testBedName : testBedNames) {
				Map<String, String> emptyBrowserHeader = new HashMap<String, String>();
				emptyBrowserHeader.put(testBedName, "");
				browserMapList.add(emptyBrowserHeader);
			}

		}
		return browserMapList;
	}

	/**
	 * @param summary
	 * @param browserName
	 * @return
	 * @throws IOException
	 */
	private String getBrowserHeaderName(String browserName) throws IOException {

		String browserHeaderName = ReportTemplate.BROWSER_NAME.getTemplate();
		if (browserName != null) {
			browserHeaderName = CommonUtil.replaceText(browserHeaderName, "#browserName", browserName);
		}

		return browserHeaderName;
	}

	/**
	 * @param summary
	 * @param browserMap
	 * @return
	 * @throws IOException
	 */
	private String getTestCaseStatusHeader() throws IOException {

		String testCaseStatusHeader = ReportTemplate.BROWSER_SUMMARY.getTemplate();

		testCaseStatusHeader = CommonUtil.replaceText(testCaseStatusHeader, "#passed", "PASSED");

		testCaseStatusHeader = CommonUtil.replaceText(testCaseStatusHeader, "#failed", "FAILED");

		testCaseStatusHeader = CommonUtil.replaceText(testCaseStatusHeader, "#skipped", "SKIPPED");

		return testCaseStatusHeader;
	}

	/**
	 * 
	 * @return
	 * @throws InvalidFormatException
	 * @throws IOException
	 * @throws InvalidActionException
	 * @throws InvalidValueException
	 * @throws DuplicateDataKeyException
	 */
	private String getTestSuiteSummary() throws InvalidFormatException, IOException, InvalidActionException, InvalidValueException, DuplicateDataKeyException {

		StringBuffer testSuiteStats = new StringBuffer();

		Collection<TestSuite> testSuiteColls = KDManager.getInstance().getTestSuites();

		for (TestSuite testSuite : testSuiteColls) {

			buildTestSuiteStats(testSuiteStats, testSuite);
		}

		return testSuiteStats.toString();
	}

	/**
	 * @param testSuiteStats
	 * @param testSuite
	 * @throws IOException
	 */
	private void buildTestSuiteStats(StringBuffer testSuiteStats, TestSuite testSuite) throws IOException {

		int totalTestCases = testSuite.getTestActions().size();

		int totalExcludedTestCases = 0;

		Map<String, LinkedList<Map<String, String>>> testActionBrowserStatusMap = buildExecutedTestCases();

		int browserCount = 0;

		int browserStatusArray[][];

		if (!testActionBrowserStatusMap.isEmpty()) {

			browserCount = testActionBrowserStatusMap.values().iterator().next().size();
		} else {

			browserCount = ConfigUtil.getTestBeds().size();
		}

		browserStatusArray = new int[3][browserCount];

		String testSuiteSummary = formatTestSuiteSummaryStats(testSuiteStats, testSuite, totalTestCases, totalExcludedTestCases, testActionBrowserStatusMap, browserCount,
				browserStatusArray);

		testSuiteStats.append(testSuiteSummary);

	}

	/**
	 * @param testSuiteStats
	 * @param testSuite
	 * @param totalTestCases
	 * @param totalExcludedTestCases
	 * @param testActionBrowserStatusMap
	 * @param browserCount
	 * @param browserStatusArray
	 * @return
	 * @throws IOException
	 */
	private String formatTestSuiteSummaryStats(StringBuffer testSuiteStats, TestSuite testSuite, int totalTestCases, int totalExcludedTestCases,
			Map<String, LinkedList<Map<String, String>>> testActionBrowserStatusMap, int browserCount, int[][] browserStatusArray) throws IOException {

		for (TestAction testAction : testSuite.getTestActions()) {

			if (!testAction.isExecute()) {
				totalExcludedTestCases++;
			} else {
				buildExecutedTestCaseBrowserStats(testActionBrowserStatusMap, browserStatusArray, testAction);
			}
		}

		int totalExecutedTestCases = totalTestCases - totalExcludedTestCases;

		String testSuiteSummary = getTestSuiteStats(testSuiteStats, testSuite.getName(), totalTestCases, totalExcludedTestCases, totalExecutedTestCases, browserCount,
				browserStatusArray);
		return testSuiteSummary;
	}

	/**
	 * @param testActionBrowserStatusMap
	 * @param browserStatusArray
	 * @param testAction
	 */
	private void buildExecutedTestCaseBrowserStats(Map<String, LinkedList<Map<String, String>>> testActionBrowserStatusMap, int[][] browserStatusArray, TestAction testAction) {

		if (!testAction.getKdDataSetColls().isEmpty()) {

			buildMultiDataBrowserStats(testActionBrowserStatusMap, browserStatusArray, testAction);
		} else {

			LinkedList<Map<String, String>> testActionBrowserList = testActionBrowserStatusMap.get(testAction.getActionName());

			getBrowserStatusCounts(testActionBrowserList, browserStatusArray);
		}
	}

	/**
	 * @param testActionBrowserStatusMap
	 * @param browserStatusArray
	 * @param testAction
	 */
	private void buildMultiDataBrowserStats(Map<String, LinkedList<Map<String, String>>> testActionBrowserStatusMap, int[][] browserStatusArray, TestAction testAction) {

		for (KDDataset kdDataset : testAction.getKdDataSetColls()) {

			String dataLabel = getDataLabel(kdDataset);

			String formattedTestActionName = testAction.getActionName() + " { " + dataLabel + " } ";

			LinkedList<Map<String, String>> testActionBrowserList = testActionBrowserStatusMap.get(formattedTestActionName);

			getBrowserStatusCounts(testActionBrowserList, browserStatusArray);

		}
	}

	/**
	 * @param testSuiteStats
	 * @param testSuite
	 * @param totalTestCases
	 * @param totalExcludedTestCases
	 * @param testActionBrowserStatusMap
	 * @param browserStatusArray
	 * @param totalExecutedTestCases
	 * @return
	 * @throws IOException
	 */
	private String getTestSuiteStats(StringBuffer testSuiteStats, String testSuiteName, int totalTestCases, int totalExcludedTestCases, int totalExecutedTestCases,
			int totalBrowsers, int[][] browserStatusArray) throws IOException {

		String testSuiteSummary = ReportTemplate.TEST_SUITE_SUMMARY.getTemplate();

		testSuiteSummary = CommonUtil.replaceText(testSuiteSummary, "#testSuiteName", testSuiteName);

		testSuiteSummary = CommonUtil.replaceText(testSuiteSummary, "#totalTestCases", String.valueOf(totalTestCases));

		testSuiteSummary = CommonUtil.replaceText(testSuiteSummary, "#executedTestCaseCount", String.valueOf(totalExecutedTestCases));

		testSuiteSummary = CommonUtil.replaceText(testSuiteSummary, "#excludedTestCaseCount", String.valueOf(totalExcludedTestCases));

		testSuiteSummary = CommonUtil.replaceText(testSuiteSummary, "#browserSummary", getBrowserSummary(browserStatusArray, totalBrowsers));

		return testSuiteSummary;

	}

	/**
	 * @param testActionBrowserStatusMap
	 * @param browserStatusArray
	 * @param formattedTestActionName
	 */
	private void getBrowserStatusCounts(LinkedList<Map<String, String>> testActionBrowserList, int[][] browserStatusArray) {

		int browserType = 0;

		for (Map<String, String> browserList : testActionBrowserList) {

			String browserStatus = browserList.entrySet().iterator().next().getValue();

			if (browserStatus.equalsIgnoreCase("passed")) {
				browserStatusArray[0][browserType]++;

			} else if (browserStatus.equalsIgnoreCase("failed")) {
				browserStatusArray[1][browserType]++;
			} else if (browserStatus.equalsIgnoreCase("skipped")) {
				browserStatusArray[2][browserType]++;
			}

			browserType++;
		}
	}

	/**
	 * 
	 * @param browserStatusArray
	 * @param browserCount
	 * @return
	 * @throws IOException
	 */
	private String getBrowserSummary(int[][] browserStatusArray, int browserCount) throws IOException {

		StringBuffer browserSummary = new StringBuffer();

		for (int browserIndex = 0; browserIndex < browserCount; browserIndex++) {

			String browserSpecificSummary = ReportTemplate.BROWSER_SUMMARY.getTemplate();

			browserSpecificSummary = CommonUtil.replaceText(browserSpecificSummary, "#passed", String.valueOf(browserStatusArray[0][browserIndex]));

			browserSpecificSummary = CommonUtil.replaceText(browserSpecificSummary, "#failed", String.valueOf(browserStatusArray[1][browserIndex]));

			browserSpecificSummary = CommonUtil.replaceText(browserSpecificSummary, "#skipped", String.valueOf(browserStatusArray[2][browserIndex]));

			browserSummary.append(browserSpecificSummary);
		}

		return browserSummary.toString();
	}

}