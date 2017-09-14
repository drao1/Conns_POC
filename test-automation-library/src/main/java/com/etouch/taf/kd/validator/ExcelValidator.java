package com.etouch.taf.kd.validator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.logging.Log;

import com.etouch.taf.core.resources.KDConstants;
import com.etouch.taf.kd.config.ExcelContent;
import com.etouch.taf.kd.config.TestDataSheetContent;
import com.etouch.taf.kd.config.TestSuiteSheetContent;
import com.etouch.taf.util.KDPropertiesUtil;
import com.etouch.taf.util.LogUtil;

/**
 * 
 * @author ngoctran
 * 
 */
public class ExcelValidator {

	static Log log = LogUtil.getLog(ExcelValidator.class);
	private TestSuiteSheetValidator testSuiteSheetValidator;
	private TestDataSheetValidator testDataSheetValidator;

	private KDPropertiesUtil kdPropertiesUtil = KDPropertiesUtil.getInstance();
	private ExcelContent excelContent;
	private Map<String, List<String>> statusMessages;
	private boolean isValid = true;

	public ExcelValidator(ExcelContent excelContent) {
		statusMessages = new HashMap<>();
		this.testSuiteSheetValidator = new TestSuiteSheetValidator();
		this.testDataSheetValidator = new TestDataSheetValidator();
		this.excelContent = excelContent;
	}

	public TestSuiteSheetValidator getTestSuiteSheetValidator() {
		return this.testSuiteSheetValidator;
	}

	public TestDataSheetValidator getTestDataSheetValidator() {
		return this.testDataSheetValidator;
	}

	public void setTestSuiteSheetValidator(TestSuiteSheetValidator testSuiteSheetValidator) {
		this.testSuiteSheetValidator = testSuiteSheetValidator;
	}

	public void setTestDataSheetValidator(TestDataSheetValidator testDataSheetValidator) {
		this.testDataSheetValidator = testDataSheetValidator;
	}

	public boolean getIsValid() {
		if (!testSuiteSheetValidator.getIsAllValid() || !testDataSheetValidator.getIsAllValid()) {
			isValid = false;
		}
		return this.isValid;
	}

	public void setIsValid(boolean flag) {
		this.isValid = flag;
	}

	public Map<String, List<String>> getStatusMessages() {

		if (statusMessages == null) {
			statusMessages = new HashMap<>();
		}
		statusMessages.putAll(testSuiteSheetValidator.getStatusMessages());
		statusMessages.putAll(testDataSheetValidator.getStatusMessages());
		return statusMessages;
	}

	/**
	 * 
	 * @param sheetName
	 * @param messages
	 */
	public void updateStatusMessages(String sheetName, List<String> messages) {
		this.statusMessages.put(sheetName, messages);
	}

	public ExcelContent getExcelContent() {
		return this.excelContent;
	}

	public void displayStatusMessages() {

		Map<String, List<String>> statusMessageMap = this.getStatusMessages();
		for (Map.Entry<String, List<String>> entry : statusMessageMap.entrySet()) {
			String key = entry.getKey();

			List<String> value = entry.getValue();

			if (value != null && !value.isEmpty()) {
				log.info(key + " - " + value);
			}

		}

	}

	/**
	 * 
	 * @param testSuiteContentList
	 */
	public void validateTestSuiteSheetContent(TestSuiteSheetContent testSuiteSheetContent, Set<String> uniqueTestActionNames) {
		boolean flag = validateTestActionNameUnique(testSuiteSheetContent, uniqueTestActionNames);
		if (!flag) {
			testSuiteSheetValidator.setIsAllValid(false);
		}
		testSuiteSheetValidator.validateTestSuiteSheetContent(testSuiteSheetContent, excelContent);
	}

	/**
	 * 
	 * @param testDataContentList
	 * @param testDataRequiredMap
	 */
	public void validateTestDataSheetContent(TestDataSheetContent testDataSheetContent, Map<String, List<String>> testDataRequiredMap) {
		testDataSheetValidator.validateTestDataSheetContent(testDataSheetContent, testDataRequiredMap);

	}

	/**
	 * 
	 * @param testSuiteContent
	 * @return
	 */
	public boolean validateTestActionNameUnique(TestSuiteSheetContent testSuiteSheetContent, Set<String> uniqueTestActionNames) {

		boolean flag = true;
		if (testSuiteSheetContent == null)
			return false;
		List<String> testActioNameList = testSuiteSheetContent.getTestActionNameList();

		for (String each : testActioNameList) {
			if (each == null)
				continue;
			if (!uniqueTestActionNames.contains(each.trim())) {

				flag = false;
				testSuiteSheetContent.setIsValid(flag);
				testSuiteSheetContent.updateStatusMessages(kdPropertiesUtil.getProperty("duplicate_testactionname_message") + ": " + each);
			} else {

				excelContent.removeFromUniqueTestActionNames(each);
			}

		}
		return flag;
	}

	public void validateInvalidTestSheetNames() {
		List<String> messages = new ArrayList<>();
		for (String curSheetName : excelContent.getInvalidTestSheetNames()) {

			messages.add(kdPropertiesUtil.getProperty("invalid_sheetname_message") + ": " + curSheetName);
			if (!curSheetName.endsWith(KDConstants.TS_SHEETNAME_POSTFIX.getValue())) {
				testSuiteSheetValidator.validateTestSuiteName(curSheetName);
				testSuiteSheetValidator.updateStatusMessages(curSheetName, messages);

			}

			this.setTestSuiteSheetValidator(testSuiteSheetValidator);

			if (!curSheetName.endsWith(KDConstants.TD_SHEETNAME_POSTFIX.getValue())) {
				testDataSheetValidator.validateTestDataName(curSheetName);
				testDataSheetValidator.updateStatusMessages(curSheetName, messages);
			}
			this.setTestDataSheetValidator(testDataSheetValidator);
		}

	}

	/**
	 * New Main method to parse the Excel File
	 * 
	 * @return
	 */
	public void validateExcelContent() {

		try {
			// contains multiple suite sheets
			TestSuiteSheetValidator testSuiteSheetValidator = getTestSuiteSheetValidator();

			// contains mulitple data sheets
			TestDataSheetValidator testDataSheetValidator = getTestDataSheetValidator();

			// TestDataRequiredMap for all TestActionNames
			Map<String, List<String>> testDataRequiredMap = new HashMap<>();

			// validate for each TestSuite Content
			for (Map.Entry<String, TestSuiteSheetContent> entry : excelContent.getTestSuiteSheetContentMap().entrySet()) {
				String key = entry.getKey();

				TestSuiteSheetContent testSuiteSheetContent = entry.getValue();

				if (testSuiteSheetContent != null) {
					validateTestSuiteSheetContent(testSuiteSheetContent, excelContent.getUniqueTestActionNames());
					testSuiteSheetValidator.updateStatusMessages(testSuiteSheetContent);
					setTestSuiteSheetValidator(testSuiteSheetValidator);
					testDataRequiredMap.putAll(testSuiteSheetContent.getTestDataMap());
				}
			}

			Set<String> availableTestDataList = new HashSet<>();

			for (Map.Entry<String, TestDataSheetContent> entry : excelContent.getTestDataSheetContentMap().entrySet()) {
				String key = entry.getKey();

				TestDataSheetContent testDataSheetContent = entry.getValue();
				String curSheetName = testDataSheetContent.getName();

				int delimIndex = curSheetName.indexOf("_");
				if (testDataSheetContent != null) {

					String curDataName = curSheetName.substring(0, delimIndex);

					testDataSheetValidator.validateTestDataSheetContent(testDataSheetContent, testDataRequiredMap);
					availableTestDataList.addAll(testDataSheetContent.getTestDataMap().keySet());

					testDataSheetValidator.updateStatusMessages(testDataSheetContent);
					setTestDataSheetValidator(testDataSheetValidator);
				}

			}

			if (availableTestDataList != null) {
				Map<String, List<String>> masterTestDataMap = excelContent.getMasterTestDataMap();

				Iterator<Entry<String, List<String>>> itr = masterTestDataMap.entrySet().iterator();
				List<String> errorMsgList = new ArrayList<>();
				boolean isFound = true;
				while (itr.hasNext()) {
					Entry<String, List<String>> eEntry = itr.next();
					String eKey = eEntry.getKey();

					if (!availableTestDataList.contains(eKey)) {
						if (eKey != null && eKey.length() > 0) {
							isFound = false;

							errorMsgList.add(kdPropertiesUtil.getProperty("missing_testdata_info_message") + ": " + eKey);
						}
					}

				}
				if (!isFound) {
					this.setIsValid(false);
					this.updateStatusMessages("Need to verify Data Sheets", errorMsgList);

				}

			}

			validateInvalidTestSheetNames();

		} catch (Exception ex) {
			log.debug(ex);
		}
	}

	/**
	 * Main class
	 */
	public void validateExcelFileContent() {
		try {
			this.excelContent.parseExcelContent();
			validateExcelContent();

		} catch (Exception ex) {
			log.debug(ex);
		}

	}

}
