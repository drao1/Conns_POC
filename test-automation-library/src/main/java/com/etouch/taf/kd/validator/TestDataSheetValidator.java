package com.etouch.taf.kd.validator;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.logging.Log;

import com.etouch.taf.core.resources.KDConstants;
import com.etouch.taf.kd.config.TestDataSheetContent;
import com.etouch.taf.kd.config.TestSuiteLibrary;
import com.etouch.taf.util.KDPropertiesUtil;
import com.etouch.taf.util.LogUtil;

public class TestDataSheetValidator {

	private static Log log = LogUtil.getLog(TestDataSheetValidator.class);

	private boolean isAllValid = true;
	private TestSuiteLibrary testSuiteLibrary = TestSuiteLibrary.getInstance();
	private KDPropertiesUtil kdPropertiesUtil = KDPropertiesUtil.getInstance();

	// TestData sheet name & list of TestData Messages
	private Map<String, List<String>> statusMessages = new HashMap<>();

	/**
	 * 
	 * @param testDataContent
	 */
	public void updateStatusMessages(TestDataSheetContent testDataSheetContent) {
		this.statusMessages.put(testDataSheetContent.getName(), testDataSheetContent.getStatusMessages());
	}

	/**
	 * 
	 * @param sheetName
	 * @param messages
	 */
	public void updateStatusMessages(String sheetName, List<String> messages) {
		this.statusMessages.put(sheetName, messages);
	}

	/**
	 * 
	 */
	public void displayStatusMessages() {

		Map<String, List<String>> statusMessages = getStatusMessages();
		Iterator<Entry<String, List<String>>> itr = statusMessages.entrySet().iterator();

		while (itr.hasNext()) {
			Entry<String, List<String>> entry = itr.next();
			String key = entry.getKey();

			List<String> list = entry.getValue();
			if (list != null && !list.isEmpty()) {
				log.info("Verify the TestData Sheet: " + key);
			}
			for (String each : list) {
				log.info("---" + each);
			}
		}
	}

	// TestData sheet name & list of TestData Messages
	public Map<String, List<String>> getStatusMessages() {
		return this.statusMessages;
	}

	/**
	 * 
	 * @param testDataContent
	 *            - TestData available in this TestDataContent (Data sheet)
	 * @param testDataRequiredMap
	 *            - TestData List in this TestSuiteContent (Suite sheet)
	 */
	public void validateTestDataSheetContent(TestDataSheetContent testDataSheetContent, Map<String, List<String>> testDataRequiredMap) {
		validateActionFields(testDataSheetContent, testDataRequiredMap);
		validateTestDataFields(testDataSheetContent);

	}

	/**
	 * 
	 * @param curSheetName
	 * @param curSheet
	 * @return
	 */
	public boolean getIsAllValid() {
		return this.isAllValid;
	}

	public void setIsAllValid(boolean isAllValid) {
		this.isAllValid = isAllValid;
	}

	/**
	 * Validate the Sheet Name - need to be ended with "_Data"
	 * 
	 * @return
	 */
	public boolean validateTestDataName(String testDataSheetName) {

		if (testDataSheetName == null)
			return false;
		boolean flag = true;
		int delimIndex = -1;
		if (testDataSheetName.contains("_")) {
			delimIndex = testDataSheetName.indexOf("_");
			String postFixedName = testDataSheetName.substring(delimIndex).trim();
			if (!KDConstants.TD_SHEETNAME_POSTFIX.getValue().equals(postFixedName)) {
				flag = false;
			}
		} else if (!KDConstants.TS_SHEETNAME_KEYWORDLIBRARY.getValue().equals(testDataSheetName)) {
			flag = false;
		}
		this.setIsAllValid(flag);
		return flag;
	}

	/**
	 * Validate the Action fields as ENTERVALUE,hasValue,SELECTDROPDOWNLIST
	 * Validate required "Run" field Validate the element name for each Action
	 * 
	 * @return
	 */
	public boolean validateActionFields(TestDataSheetContent testDataSheetContent, Map<String, List<String>> testDataRequiredMap) {
		if (testDataSheetContent == null)
			return false;

		return compareContent(testDataSheetContent, testDataRequiredMap);

	}

	/**
	 * Validate begin/end TestData Fields column
	 * 
	 * @return
	 */
	public boolean validateTestDataFields(TestDataSheetContent testDataSheetContent) {
		boolean flag = true;
		if (isJunkCharaterPresent(testDataSheetContent)) {
			flag = false;
			this.setIsAllValid(flag);
		} else if (!validateEndDataMessagePresent(testDataSheetContent)) {
			flag = false;
			this.setIsAllValid(flag);
		}
		return flag;
	}

	/**
	 * 
	 * @param testDataRequiredMap
	 *            - the list of testData from the TestSuite
	 * @return
	 */
	public boolean compareContent(TestDataSheetContent testDataSheetContent, Map<String, List<String>> testDataRequiredMap) {
		// log.debug("compareContent");
		boolean isMatch = true;
		try {

			Map<String, List<Object>> testDataEltNameMap = testDataSheetContent.getTestDataEltNameMap();
			Iterator<Entry<String, List<Object>>> curTestDataEltNameItr = testDataEltNameMap.entrySet().iterator();

			// loop through the TestData List in this Data Sheet
			while (curTestDataEltNameItr.hasNext()) { 

				Entry<String, List<Object>> entry = curTestDataEltNameItr.next();
				// the TestData value i.e. LoginDataSet
				String key = entry.getKey(); 

				List<Object> curTestDataValueList = entry.getValue();

				if (key != null && key.isEmpty())
					continue;
				if (key == null)
					continue;

				// validate the header order
				if (curTestDataValueList != null && curTestDataValueList.get(0) != null) {

					if (!KDConstants.TD_EXECUTION_LABEL.getValue().equalsIgnoreCase(curTestDataValueList.get(0).toString().trim())) {

						isMatch = false;
						this.setIsAllValid(false);
						// Missing required "Run" column
						testDataSheetContent.updateStatusMessages("<b>" + key + "</b>" + " - " + kdPropertiesUtil.getProperty("missing_mandatorycolumn_message") + ": '"
								+ KDConstants.TD_EXECUTION_LABEL.getValue() + "'");

					}

				}

				if (curTestDataValueList != null && curTestDataValueList.get(1) != null) {

					if (!KDConstants.TD_LABEL.getValue().equalsIgnoreCase(curTestDataValueList.get(1).toString().trim())) {

						isMatch = false;
						this.setIsAllValid(false);

						// Missing required "Label" column
						testDataSheetContent.updateStatusMessages("<b>" + key + "</b>" + " - " + kdPropertiesUtil.getProperty("missing_mandatorycolumn_message") + ": '"
								+ KDConstants.TD_LABEL.getValue() + "'");
					}
				}

				if (!curTestDataValueList.contains(KDConstants.TD_EXECUTION_LABEL.getValue()) && !curTestDataValueList.contains(KDConstants.TD_LABEL.getValue())) {

					isMatch = false;
					this.setIsAllValid(false);
					// Missing required "Run" column
					testDataSheetContent.updateStatusMessages("<b>" + key + "</b>" + " - " + kdPropertiesUtil.getProperty("missing_mandatorycolumn_message") + ": '"
							+ KDConstants.TD_EXECUTION_LABEL.getValue() + "'");
				} else {
					// check the mandatory "Run" column
					if (curTestDataValueList.contains(KDConstants.TD_EXECUTION_LABEL.getValue())) {
						// filter out the "Run" column
						curTestDataValueList.remove(KDConstants.TD_EXECUTION_LABEL.getValue());

					}
					if (curTestDataValueList.contains(KDConstants.TD_LABEL.getValue())) {
						// filter out the "Label" column
						curTestDataValueList.remove(KDConstants.TD_LABEL.getValue());
					}
				}

				List<String> requiredElts = testDataRequiredMap.get(key);

				if (requiredElts == null) {
					continue;

				} else if (requiredElts.size() == curTestDataValueList.size()) {

					for (String each : requiredElts) {

						if (!curTestDataValueList.contains(each)) {
							isMatch = false;
							this.setIsAllValid(false);
							testDataSheetContent.updateStatusMessages("<b>" + key + "</b>" + " - " + kdPropertiesUtil.getProperty("missing_elementname_message") + ": '" + each
									+ "'");
						}
					}

					for (Object each : curTestDataValueList) {

						if (!requiredElts.contains(each)) {

							isMatch = false;
							this.setIsAllValid(false);
							testDataSheetContent.updateStatusMessages("<b>" + key + "</b>" + " - " + kdPropertiesUtil.getProperty("invalid_column_message") + ": '" + each + "'");
						}

					}

				} else {

					for (String each : requiredElts) {

						if (!curTestDataValueList.contains(each)) {
							isMatch = false;
							this.setIsAllValid(false);
							testDataSheetContent.updateStatusMessages("<b>" + key + "</b>" + " - " + kdPropertiesUtil.getProperty("missing_elementname_message") + ": '" + each
									+ "'");
						}

					}

				}

			}

		} catch (Exception ex) {
			log.debug(ex);
		}
		return isMatch;
	}

	public boolean validateTestData(TestDataSheetContent testDataSheetContent, Map<String, List<String>> masterTestDataMap, Set<String> availableTestDataList) {
		boolean flag = true;

		Iterator<Entry<String, List<String>>> itr = masterTestDataMap.entrySet().iterator();

		while (itr.hasNext()) {
			Entry<String, List<String>> entry = itr.next();
			String key = entry.getKey();
			if (!availableTestDataList.contains(key)) {
				flag = false;
				this.setIsAllValid(false);
				log.debug("!!! in TestDataSheetValidator");
				testDataSheetContent.updateStatusMessages(kdPropertiesUtil.getProperty("missing_testdata_info_message") + ": " + "<b>" + key + "</b>");
			}

		}
		return flag;
	}

	public boolean isJunkCharaterPresent(TestDataSheetContent testDataSheetContent) {
		int totalNumberofJunkChar = 0;
		log.debug("!!!Validating junk character in TestDataSheetValidator");
		if (testDataSheetContent == null)
			return false;
		Map<String, List<Object>> testDataMap = testDataSheetContent.getTestDataMap();
		for (Map.Entry<String, List<Object>> entry : testDataMap.entrySet()) {
			List<Object> value = entry.getValue();
			String dataSet = entry.getKey();
			for (Object dataObj : value) {
				String data = dataObj.toString();
				if (!UnwantedCharacterValidation.isAsciiPrintable(data)) {
					long numberofNonWhiteSpace = UnwantedCharacterValidation.getNumberofNonWhiteSpace(data);
					if (numberofNonWhiteSpace > 0) {
						testDataSheetContent.updateStatusMessages("<b>" + dataSet + "</b>" + " - Contains " + "<b>" + numberofNonWhiteSpace + "</b>" + " "
								+ kdPropertiesUtil.getProperty("non_white_space_testdata_message") + "   " + "<b>" + data + "</b>");
					} else {
						testDataSheetContent.updateStatusMessages("<b>" + dataSet + "</b>" + " - " + kdPropertiesUtil.getProperty("junkchar_present_testdata_message") + "   "
								+ "<b>" + data + "</b>");
					}
					totalNumberofJunkChar++;
				}
			}
		}

		if (totalNumberofJunkChar > 0) {
			return true;
		} else {
			return false;
		}
	}

	public boolean validateEndDataMessagePresent(TestDataSheetContent testDataSheetContent) {
		int totalNumberCellMissingEndValue = 0;
		log.debug("!!!Validating Ending Messagage TestData in TestDataSheetValidator");
		if (testDataSheetContent == null)
			return false;
		Map<String, List<Object>> testDataMap = testDataSheetContent.getTestDataMap();
		for (Map.Entry<String, List<Object>> entry : testDataMap.entrySet()) {
			String key = entry.getKey().trim();
			List<Object> value = entry.getValue();
			if (!value.contains(key)) {
				testDataSheetContent.updateStatusMessages(kdPropertiesUtil.getProperty("missing_ending_testdata_message") + ": " + "<b>" + key + "</b>");
				totalNumberCellMissingEndValue++;
			}
		}

		if (totalNumberCellMissingEndValue > 0) {
			return false;
		} else {
			return true;
		}
	}
}