package com.etouch.taf.kd.validator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;

import com.etouch.taf.core.config.BooleanAssertKeywordLibrary;
import com.etouch.taf.core.resources.KDConstants;
import com.etouch.taf.kd.config.ActionKeywordLibrary;
import com.etouch.taf.kd.config.AssertKeywordLibrary;
import com.etouch.taf.kd.config.ElementIdentifyKeyLibrary;
import com.etouch.taf.kd.config.ExcelContent;
import com.etouch.taf.kd.config.InputKeywordLibrary;
import com.etouch.taf.kd.config.PageActionKeywordLibrary;
import com.etouch.taf.kd.config.TestSuiteHeaderElement;
import com.etouch.taf.kd.config.TestSuiteLibrary;
import com.etouch.taf.kd.config.TestSuiteSheetContent;
import com.etouch.taf.util.KDPropertiesUtil;
import com.etouch.taf.util.LogUtil;

/**
 * 
 *  
 */
public class TestSuiteSheetValidator {
	static Log log = LogUtil.getLog(TestSuiteSheetValidator.class);

	private boolean isAllValid = true;

	// TestSuite sheet name, TestSuite Message
	private Map<String, List<String>> statusMessages = new HashMap<>();

	private KDPropertiesUtil kdPropertiesUtil = KDPropertiesUtil.getInstance();

	/**
	 * 
	 * @param testSuiteContent
	 */
	public void updateStatusMessages(TestSuiteSheetContent testSuiteSheetContent) {
		this.statusMessages.put(testSuiteSheetContent.getName(), testSuiteSheetContent.getStatusMessages());
	}

	/**
	 * 
	 * @param sheetName
	 * @param messages
	 */
	public void updateStatusMessages(String sheetName, List<String> messages) {
		this.statusMessages.put(sheetName, messages);
	}

	// testSuite sheet name and TestSuite Messages
	public Map<String, List<String>> getStatusMessages() {
		return this.statusMessages;
	}

	/**
	 * 
	 * @return
	 */
	public boolean getIsAllValid() {
		return this.isAllValid;
	}

	/**
	 * 
	 * @param isAllValid
	 */
	public void setIsAllValid(boolean isAllValid) {
		this.isAllValid = isAllValid;
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
				log.info("Verify the TestSuite Sheet: " + key);
			}
			for (String each : list) {
				log.info("---" + each);
			}
		}
	}

	/**
	 * Main validate method
	 * 
	 * @param curSheetName
	 * @param curSheet
	 */
	public void validateTestSuiteSheetContent(TestSuiteSheetContent testSuiteSheetContent, ExcelContent excelContent) {
		validateActionKeyword(testSuiteSheetContent);
		validateHeaderFields(testSuiteSheetContent);
		validateElementKeyFields(testSuiteSheetContent);
		validateElementValueFields(testSuiteSheetContent);
		validateMandatoryFields(testSuiteSheetContent);
		validateEndTestActionFields(testSuiteSheetContent);
		validateTestActionRules(testSuiteSheetContent, excelContent);

	}

	/**
	 * Validate the Sheet Name - need to be ended with "_Suite"
	 * 
	 * @return
	 */
	public boolean validateTestSuiteName(String testSuiteSheetName) {

		if (testSuiteSheetName == null) {
			return false;
		}
		boolean flag = true;
		int delimIndex = -1;
		if (testSuiteSheetName.contains("_")) {
			delimIndex = testSuiteSheetName.indexOf("_");
			String postFixedName = testSuiteSheetName.substring(delimIndex).trim();
			if (!KDConstants.TS_SHEETNAME_POSTFIX.getValue().equals(postFixedName)) {
				flag = false;
			}
		} else if (!testSuiteSheetName.equals(KDConstants.TS_SHEETNAME_KEYWORDLIBRARY.getValue())) {
			flag = false;
		}
		this.setIsAllValid(flag);
		return flag;
	}

	/**
	 * Validate mandatory fields with no empty value:
	 * Index,ElementName,ElementIdentificationKey,ElementIdentificationValue,
	 * Action, TestRun, EndTestActionName
	 * 
	 * 
	 * @return
	 */
	public boolean validateMandatoryFields(TestSuiteSheetContent testSuiteSheetContent) {

		if (testSuiteSheetContent == null) {
			return false;
		}
		boolean flag = true;
		Map<String, List<String[]>> mandatoryFields = testSuiteSheetContent.getMandatoryFieldMap();
		// keep track of TestActionName that need the required TestData
		Set<String> requiredTestDataList = new TreeSet<>();
		// loop through TestActionName
		for (Map.Entry<String, List<String[]>> entry : mandatoryFields.entrySet()) {

			String testActionName = entry.getKey();
			// TestActionName
			String colName = "";
			String colValue = "";
			for (String[] each : entry.getValue()) {
				if (each == null) {
					continue;
				}
				if (each.length < 2) {
					continue;
				}
				if (each[0] != null) {
					colName = each[0].trim();
				}
				if (each[1] != null) {
					colValue = each[1].trim();
				}
				if (!colValue.isEmpty()) {
					if (colName != null && colName.equalsIgnoreCase("Action")) {
						if (ActionKeywordLibrary.isRequiredInputActions(colValue)) {
							requiredTestDataList.add(testActionName);
						}

					} else if ("PreAction".equalsIgnoreCase(colName)) {
						if (colValue != null) {
							String[] preActionValues = colValue.split(",");
							for (String eachTestAction : preActionValues) {
								if (mandatoryFields.containsKey(eachTestAction)) {
									List<String[]> eltList = mandatoryFields.get(eachTestAction);
									for (String[] elt : eltList) {
										if (elt != null && "TestData".equalsIgnoreCase(elt[0])) {
											if (elt[1] != null && elt[1].trim().length() > 0) {
												requiredTestDataList.add(testActionName);
												break;
											}
										}

									}
								}

							}
						}

					} else if ("PostAction".equalsIgnoreCase(colName)) {
						if (colValue != null) {
							String[] postActionValues = colValue.split(",");
							for (String eachTestAction : postActionValues) {
								if (mandatoryFields.containsKey(eachTestAction)) {
									List<String[]> eltList = mandatoryFields.get(eachTestAction);
									for (String[] elt : eltList) {
										if (elt != null && "TestData".equalsIgnoreCase(elt[0])) {
											if (elt[1] != null && elt[1].trim().length() > 0) {
												requiredTestDataList.add(testActionName);
												break;
											}
										}

									}
								}

							}
						}

					} else if ("TestRun".equalsIgnoreCase(colName)) {
						if (colValue != null) {
							if (!colValue.trim().equalsIgnoreCase("Y") && !colValue.trim().equalsIgnoreCase("N")) {
								flag = false;
								this.setIsAllValid(false);
								testSuiteSheetContent.setIsValid(false);
								testSuiteSheetContent.updateStatusMessages(
										testActionName + ": " + kdPropertiesUtil.getProperty("invalid_expected_input")
												+ ": " + colValue + " under " + colName);
							}
						}
					}
					continue;
				} else if (colValue.isEmpty()) {
					if ("LoadLoginPage".equals(testActionName)) {
						if ("ElementIdentificationKey".equals(colName) || "ElementIdenficationValue".equals(colName)) {
							continue;
						}
					}

					else {
						if (!"TestData".equalsIgnoreCase(colName)) {
							if (!"PreAction".equalsIgnoreCase(colName)) {
								if (!"PostAction".equalsIgnoreCase(colName)) {
									flag = false;
									this.setIsAllValid(false);
									testSuiteSheetContent.setIsValid(false);
									testSuiteSheetContent.updateStatusMessages(testActionName + ": "
											+ kdPropertiesUtil.getProperty("missing_mandatoryvalue_message") + " under "
											+ colName);
								}

							}

						}

					}
				}
			}
		}

		String colName = "";
		String colValue = "";
		Map<String, String> testActionDataMap = testSuiteSheetContent.getTestActionDataMap();

		// validate the TestData field
		for (Map.Entry<String, List<String[]>> entry : mandatoryFields.entrySet()) {
			String testActionName = entry.getKey();
			if (!requiredTestDataList.contains(testActionName)) {
				continue;
			}

			colName = "";
			colValue = "";
			boolean isTestDataAbsent = false;
			String[] eltIdKeyValues = null;

			List<String[]> eltIdKeyValueList = new ArrayList<>();

			for (String[] each : entry.getValue()) {

				if (each == null) {
					continue;
				}
				if (each.length < 2) {
					continue;
				}
				if (each[0] != null) {
					colName = each[0].trim();
				}
				if (each[1] != null) {
					colValue = each[1].trim();
				}

				if (colName != null && colName.equalsIgnoreCase("Action")) {
					if (colValue != null && colValue.length() > 0) {

						if (isTestDataRequired(colValue) && !testActionDataMap.containsKey(testActionName)) {
							isTestDataAbsent = true;
						}
					}
				} else if (colName != null && colName.equalsIgnoreCase("ElementIdentificationKey")) {
					if (colValue != null && colValue.length() > 0) {
						eltIdKeyValues = new String[2]; // build the ElementId
														// Key & Value
						eltIdKeyValues[0] = colValue;
					}
				} else if (colName != null && colName.equalsIgnoreCase("ElementIdentificationValue")) {
					if (colValue != null && colValue.length() > 0) {
						if (eltIdKeyValues != null && eltIdKeyValues[0].trim().length() > 0) {
							eltIdKeyValues[1] = colValue;
						}
					}
				}

				// Build the List of ElementIdentificationKey and
				// ElementIdentificationValue
				if (eltIdKeyValues != null && eltIdKeyValues[0] != null && eltIdKeyValues[1] != null) {
					eltIdKeyValueList.add(eltIdKeyValues);
					eltIdKeyValues = null;
				}
			}

			if (isTestDataAbsent) {
				flag = false;
				this.setIsAllValid(false);
				testSuiteSheetContent.setIsValid(false);
				testSuiteSheetContent.updateStatusMessages(kdPropertiesUtil.getProperty("missing_testdata_message")
						+ " in TestActionName: " + testActionName);
			}

			boolean isWrongFormat = false;
			if (eltIdKeyValueList != null && !eltIdKeyValueList.isEmpty()) {
				String eltIdValue = "";
				for (String[] eachEltId : eltIdKeyValueList) {
					if (!"reSize".equalsIgnoreCase(eachEltId[0])) {
						continue;
					}

					eltIdValue = eachEltId[1];

					String[] eltValues = eltIdValue.split(",");

					if (eltValues != null && eltValues.length > 2) {
						isWrongFormat = true;
					} else {
						for (String each : eltValues) {
							if (!Pattern.matches("-?[0-9]+", each)) {
								isWrongFormat = true;
							}
						}
					}

					if (isWrongFormat) {
						flag = false;
						this.setIsAllValid(false);
						testSuiteSheetContent.setIsValid(false);
						testSuiteSheetContent.updateStatusMessages(testActionName + "- " + eachEltId[0] + ": "
								+ kdPropertiesUtil.getProperty("wrongformat_elementidenficationkey_message"));
					}

				}
			}

		}

		return flag;

	}

	private boolean isTestDataRequired(String testAction) {

		boolean testDataRequired = false;

		if (AssertKeywordLibrary.contains(testAction.trim()) || InputKeywordLibrary.contains(testAction.trim())
				|| PageActionKeywordLibrary.contains(testAction.trim())) {
			testDataRequired = true;
		}

		return testDataRequired;
	}

	/**
	 * Validate matching EndTestActionName for each TestActionName
	 * 
	 * @param testSuiteSheetContent
	 * @return
	 */
	public boolean validateEndTestActionFields(TestSuiteSheetContent testSuiteSheetContent) {

		if (testSuiteSheetContent == null) {
			return false;
		}
		boolean flag = true;

		List<String> endTestActionNameList = testSuiteSheetContent.getEndTestActionNameList();
		if (endTestActionNameList.size() < testSuiteSheetContent.getTestActionNameList().size()) {
			flag = false;
			this.setIsAllValid(false);
			testSuiteSheetContent.setIsValid(false);
			testSuiteSheetContent
					.updateStatusMessages(kdPropertiesUtil.getProperty("missing_endtestactionname_message"));

		} else {

			for (String eachEndTestActionName : endTestActionNameList) {

				if (!testSuiteSheetContent.getTestActionNameList().contains(eachEndTestActionName)) {
					flag = false;
					this.setIsAllValid(false);
					testSuiteSheetContent.setIsValid(false);
					testSuiteSheetContent
							.updateStatusMessages(kdPropertiesUtil.getProperty("invalid_endtestactionname_message")
									+ ": " + eachEndTestActionName);

				}
			}
		}

		return flag;

	}

	/**
	 * Validate if the headers match with TestSuiteHeaderElement The first row
	 * in the Suite Sheet
	 * 
	 * @return
	 */
	public boolean validateHeaderFields(TestSuiteSheetContent testSuiteSheetContent) {

		if (testSuiteSheetContent == null) {
			return false;
		}
		boolean flag = true;
		TreeSet<TestSuiteHeaderElement> testSuiteHeaderEltsRequiredSet = TestSuiteLibrary.getInstance()
				.getTestSuiteHeaderElements();

		List<String> testSuiteContentHeaderSet = testSuiteSheetContent.getTestSuiteHeaderElements();

		java.util.Iterator<String> testSuiteContentHeaderElements = testSuiteContentHeaderSet.iterator();

		int counter = 0;
		while (testSuiteContentHeaderElements.hasNext()) {
			String eachHeaderElt = testSuiteContentHeaderElements.next();

			// validate if existed in the required header list
			boolean isValidTestSuiteHeader = false;
			for (TestSuiteHeaderElement eachTestSuiteHeaderElt : TestSuiteHeaderElement.values()) {
				if (eachTestSuiteHeaderElt.name().equalsIgnoreCase(eachHeaderElt.trim())) {
					isValidTestSuiteHeader = true;
				}
			}

			java.util.Iterator<TestSuiteHeaderElement> testSuiteHeaderEltsRequired = testSuiteHeaderEltsRequiredSet
					.iterator();
			while (testSuiteHeaderEltsRequired.hasNext()) {
				TestSuiteHeaderElement tsHeader = testSuiteHeaderEltsRequired.next();
				String requiredSuiteHeaderElt = (String) tsHeader.toString().trim();
				int curRank = tsHeader.getRank() - 1;

				if (counter == curRank) {

					if (!requiredSuiteHeaderElt.equalsIgnoreCase(eachHeaderElt.trim())) {
						flag = false;
						this.setIsAllValid(false);
						testSuiteSheetContent.setIsValid(false);
						if (isValidTestSuiteHeader) {
							testSuiteSheetContent.updateStatusMessages(
									kdPropertiesUtil.getProperty("wrongorder_column_message") + ": " + eachHeaderElt);
						} else {
							testSuiteSheetContent.updateStatusMessages(
									kdPropertiesUtil.getProperty("invalid_value_message") + ": " + eachHeaderElt);
						}

					}
					break;
				}
			}
			counter++;
		}

		return flag;
	}

	/**
	 * Validate Action fields if it belongs to KeywordLibrary
	 * 
	 * @return
	 */
	public boolean validateActionKeyword(TestSuiteSheetContent testSuiteSheetContent) {

		boolean flag = true;
		if (testSuiteSheetContent == null) {
			return false;
		}
		List<String> actionKeyWords = testSuiteSheetContent.getActionList();

		for (String eachKeyword : actionKeyWords) {
			if (!AssertKeywordLibrary.contains(eachKeyword.trim()) && !ActionKeywordLibrary.contains(eachKeyword.trim())
					&& !InputKeywordLibrary.contains(eachKeyword.trim())
					&& !BooleanAssertKeywordLibrary.contains(eachKeyword.trim())
					&& !PageActionKeywordLibrary.contains(eachKeyword.trim())) {
				flag = false;
				this.setIsAllValid(false);
				testSuiteSheetContent.setIsValid(false);
				testSuiteSheetContent.updateStatusMessages(
						kdPropertiesUtil.getProperty("invalid_actionkeyword_message") + ": " + eachKeyword);

			}

		}
		return flag;

	}

	/**
	 * Validate the PreAction/PostAction rules === PreAction & PostAction with
	 * the TestActionName in all of the SuiteSheets instead of just its
	 * SuiteContent ===
	 * 
	 * Validate begin/end TestAction Fields
	 * 
	 * @return
	 */
	public boolean validateTestActionRules(TestSuiteSheetContent testSuiteSheetContent, ExcelContent excelContent) {

		boolean flag = true;
		if (testSuiteSheetContent == null) {
			return false;
		}

		List<String> preActionList = testSuiteSheetContent.getPreActionList();
		List<String> postActionList = testSuiteSheetContent.getPostActionList();

		for (String eachPreAction : preActionList) {
			if (eachPreAction == null) {
				continue;
			}
			String[] preActionTokens = eachPreAction.split(",");
			for (String eachPreToken : preActionTokens) {

				if (!excelContent.getMasterTestActionNames().contains(eachPreToken.trim())) {

					flag = false;
					this.setIsAllValid(false);
					testSuiteSheetContent.setIsValid(false);
					testSuiteSheetContent.updateStatusMessages(
							kdPropertiesUtil.getProperty("invalid_preaction_message") + ": " + eachPreToken);
				}
			}
		}

		for (String eachPostAction : postActionList) {
			if (eachPostAction == null) {
				continue;
			}
			String[] postActionTokens = eachPostAction.split(",");
			for (String eachPostToken : postActionTokens) {
					if (!excelContent.getMasterTestActionNames().contains(eachPostToken.trim())) {
					flag = false;
					this.setIsAllValid(false);
					testSuiteSheetContent.setIsValid(false);
					testSuiteSheetContent.updateStatusMessages(
							kdPropertiesUtil.getProperty("invalid_postaction_message") + ": " + eachPostToken);

				}
			}
		}

		return flag;
	}

	/**
	 * 
	 * @param testSuiteContent
	 * @return
	 */
	public boolean validateElementKeyFields(TestSuiteSheetContent testSuiteSheetContent) {

		boolean flag = true;
		if (testSuiteSheetContent == null) {
			return false;
		}
		List<String> eltIdentificationKeyList = testSuiteSheetContent.getEltIdentificationKeyList();
		for (String each : eltIdentificationKeyList) {
			if (each == null) {
				continue;
			}
			if (!ElementIdentifyKeyLibrary.contains(each.trim())) {
				flag = false;
				this.setIsAllValid(false);
				testSuiteSheetContent.setIsValid(false);
				testSuiteSheetContent.updateStatusMessages(
						kdPropertiesUtil.getProperty("invalid_elementidenficationkey_message") + ": " + each);
			}
		}
		testSuiteSheetContent.getEltIdentificationKeyList();
		return flag;
	}

	public boolean validateElementValueFields(TestSuiteSheetContent testSuiteSheetContent) {
		boolean flag = true;
		long totalNumberofJunkChar = 0;
		if (testSuiteSheetContent == null) {
			return false;
		}
		List<String> eltIdentificationKeyList = testSuiteSheetContent.getEltIdentificationValueList();
		for (String each : eltIdentificationKeyList) {
			if (!UnwantedCharacterValidation.isAsciiPrintable(each)) {
				long numberofNonWhiteSpace = UnwantedCharacterValidation.getNumberofNonWhiteSpace(each);
				if (numberofNonWhiteSpace > 0) {
					testSuiteSheetContent
							.updateStatusMessages(kdPropertiesUtil.getProperty("non_white_space_testsuite_message")
									+ " " + "<b>" + numberofNonWhiteSpace + "</b>" + " "
									+ kdPropertiesUtil.getProperty("non_whitee_space_testsuite_message_2") + "</b>"
									+ "   " + "<b>" + each + "</b>");
				} else {
					testSuiteSheetContent
							.updateStatusMessages(kdPropertiesUtil.getProperty("junkchar_present_testsuite_message")
									+ "  " + "<b>" + each + "</b>");
				}
				totalNumberofJunkChar++;
			}
		}
		if (totalNumberofJunkChar > 0) {
			flag = false;
			this.setIsAllValid(false);
		}

		return flag;
	}
}