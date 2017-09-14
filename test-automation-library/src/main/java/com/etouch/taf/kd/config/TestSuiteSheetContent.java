package com.etouch.taf.kd.config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import com.etouch.taf.kd.exception.DuplicateDataKeyException;
import com.etouch.taf.util.KDPropertiesUtil;
import com.etouch.taf.util.LogUtil;

public class TestSuiteSheetContent {

	static Log log = LogUtil.getLog(TestSuiteSheetContent.class);

	private String name;
	private boolean isValid = true;
	private List<String> statusMessages = new ArrayList<>();

	// testSuite header fields
	private List<String> testSuiteHeaderElements = new ArrayList<>();

	// testData & required elements name
	private Map<String, List<String>> testDataMap = new HashMap<>();

	private List<String> indexList = new ArrayList<>();
	private Set indexset = new HashSet();
	// TestActionName & List of mandatory columns and its value
	// only mandatory columns list
	private Map<String, List<String[]>> mandatoryFieldMap = new HashMap<>();

	// TestActionName & TestData
	// all columns
	private Map<String, String> testActionDataMap = new HashMap<>();

	private List<String> testActionNameList = new ArrayList<>();
	private List<String> endTestActionNameList = new ArrayList<>();
	private List<String> preActionList = new ArrayList<>();
	private List<String> postActionList = new ArrayList<>();
	private List<String> actionList = new ArrayList<>();
	private List<String> eltIdentificationKeyList = new ArrayList<>();
	private List<String> eltIdentificationValueList = new ArrayList<>();

	KDPropertiesUtil kdPropertiesUtil = KDPropertiesUtil.getInstance();

	private TestDataSheetContent testDataSheetContent;

	/**
	 * 
	 * @param name
	 */
	public TestSuiteSheetContent(String name) {
		this.name = name;
	}

	/**
	 * 
	 * @param testDataContent
	 */
	public void setTestDataSheetContent(TestDataSheetContent testDataSheetContent) {
		this.testDataSheetContent = testDataSheetContent;
	}

	/**
	 * 
	 * @return
	 */
	public TestDataSheetContent getTestDataSheetContent() {
		return this.testDataSheetContent;
	}

	/**
	 * 
	 * @return
	 */
	public String getName() {
		return name;
	}

	/**
	 * 
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}

	public void setIsValid(boolean isValid) {
		this.isValid = isValid;
	}

	public boolean getIsValid() {
		return isValid;
	}

	/**
	 * 
	 * @param statusMessage
	 */
	public void updateStatusMessages(String statusMessage) {
		this.statusMessages.add(statusMessage);
	}

	/**
	 * 
	 * @return
	 */
	public List<String> getStatusMessages() {
		return this.statusMessages;
	}

	/**
	 * 
	 * @param testActionName
	 */
	public void updateTestActionNameList(String testActionName) {
		this.testActionNameList.add(testActionName);
	}

	/**
	 * 
	 * @return
	 */
	public List<String> testActionNameList() {
		return this.testActionNameList;
	}

	/**
	 * 
	 * @param preAction
	 */
	public void updatePreActionList(String preAction) {
		this.preActionList.add(preAction);
	}

	/**
	 * 
	 * @return
	 */
	public List<String> getPreActionList() {
		return this.preActionList;
	}

	/**
	 * 
	 * @return
	 */
	public List<String> getPostActionList() {
		return this.postActionList;
	}

	/**
	 * 
	 * @param postAction
	 */
	public void updatePostActionList(String postAction) {
		this.postActionList.add(postAction);
	}

	/**
	 * 
	 * @return
	 */
	public List<String> getActionList() {
		return this.actionList;
	}

	/**
	 * 
	 * @param postAction
	 */
	public void updateActionList(String action) {
		this.actionList.add(action);
	}

	/**
	 * 
	 * @return
	 */
	public List<String> getEndTestActionNameList() {
		return this.endTestActionNameList;
	}

	public List<String> getTestActionNameList() {
		return this.testActionNameList;
	}

	public List<String> getEltIdentificationKeyList() {
		return this.eltIdentificationKeyList;
	}

	public List<String> getEltIdentificationValueList() {
		return this.eltIdentificationValueList;
	}

	public void updateEltIdentificationKeyList(String eltIdentificationKey) {
		if (!this.eltIdentificationKeyList.contains(eltIdentificationKey)) {
			this.eltIdentificationKeyList.add(eltIdentificationKey);
		}
	}

	public void updateEltIdentificationValueList(String eltIdentificationValue) {
		if (!this.eltIdentificationValueList.contains(eltIdentificationValue)) {
			this.eltIdentificationValueList.add(eltIdentificationValue);
		}

	}

	/**
	 * 
	 * @param
	 */
	public void updateEndTestActionNameList(String endTestActionName) {

		if (!endTestActionNameList.contains(endTestActionName)) {
			this.endTestActionNameList.add(endTestActionName);
		}
	}

	/**
	 * 
	 * @param key
	 * @param eltValues
	 */
	public void updateMandatoryFieldMap(String key, List<String[]> eltValues) {
		mandatoryFieldMap.put(key, eltValues);

	}

	/**
	 * 
	 * @param testActionName
	 * @param testData
	 */
	public void updateTestActionDataMap(String testActionName, String testData) {
		if (!testActionDataMap.containsKey(testActionName)) {
			this.testActionDataMap.put(testActionName, testData);
		}
	}

	/**
	 * 
	 * @return
	 */
	public Map<String, String> getTestActionDataMap() {
		return this.testActionDataMap;
	}

	/**
	 * 
	 * @return
	 */
	public Map<String, List<String>> getTestDataMap() {
		return this.testDataMap;
	}

	/**
	 * 
	 * @param key
	 *            TestData
	 * @param value
	 *            ElementName
	 * 
	 * @throws DuplicateDataKeyException
	 */
	public void updateTestDataMap(String key, List<String> value) {
		if (testDataMap != null) {
			if (!testDataMap.containsKey(key)) {
				testDataMap.put(key, value);
			} else if (testDataMap.containsKey(key)) {
				testDataMap.remove(key);
				testDataMap.put(key, value);
			}
		}
	}

	/**
	 * 
	 * @param key
	 * @return
	 */
	public List<String> getTestDataKeys(String key) {
		List<String> returnValue = null;
		if (testDataMap != null) {
			returnValue = testDataMap.get(key);
		}
		return returnValue;
	}

	/**
	 * 
	 * @return
	 */
	public Set<String> getTestDataKeys() {
		Set<String> returnValue = null;
		if (testDataMap != null) {
			returnValue = testDataMap.keySet();
		}
		return returnValue;
	}

	/**
	 * 
	 * @param headerStr
	 */
	public void updateTestSuiteHeaderElements(String headerStr) {
		this.testSuiteHeaderElements.add(headerStr);
	}

	/**
	 * 
	 * @return
	 */
	public List<String> getTestSuiteHeaderElements() {
		return this.testSuiteHeaderElements;
	}

	/**
	 * 
	 * @return
	 */
	public Map<String, List<String[]>> getMandatoryFieldMap() {
		return this.mandatoryFieldMap;
	}

	/**
	 * Main buildContent
	 * 
	 * @param curSheetName
	 * @param curSheet
	 */
	public boolean buildContent(String curSheetName, Sheet curSheet) {
		boolean flag = true;
		int rowCount = 0;
		String testDataValue = "";
		List<String> tmpActionList = null;
		List<String> tmpElementNameList = null;
		String testActionName = "";

		String curTestActionName;
		// = new ArrayList<String[]>(); mandatory value in these fields
		List<String[]> selectedTestActionNameEltList = new ArrayList<String[]>();
		Map<String, List<String>> tempTestDataMap;
		boolean isNewActionName = false;

		for (Row row : curSheet) {
			if (rowCount > 0) { // start with second row
				// loop through each cell per row
				for (int cn = 0; cn < row.getLastCellNum(); cn++) {
					// If the cell is missing from the file, generate a blank
					// one
					// (Works by specifying a MissingCellPolicy)
					Cell cell = row.getCell(cn, Row.CREATE_NULL_AS_BLANK);

					if (cell == null) {
						continue;
					}
					// TestActionName
					if (cn == 0) {
						if (!cell.toString().trim().equals("")) {
							//Re-initialing selectedTestActionNameEltList for testActionName
							selectedTestActionNameEltList =  new ArrayList<String[]>();
							// for each TestAction
							tempTestDataMap = new HashMap<>();

							this.updateTestActionNameList(cell.toString().trim());
							isNewActionName = true;
							testActionName = cell.toString().trim();
							testDataValue = "";
							tmpActionList = new ArrayList<>();
							tmpElementNameList = new ArrayList<>();
							// special case for loadLoginPage
							if (rowCount == 1) {
								curTestActionName = testActionName;
							}
						} else {

							isNewActionName = false;
							curTestActionName = testActionName;
						}
					} else if (cn == 1) {

						indexList.add(cell.toString().trim());
						indexList.removeAll(Arrays.asList(""));

						if (cell.toString().trim() != "") {
							indexset.add(cell.toString().trim());
						}

						if (indexset.size() != indexList.size()) {
							break;
						}

						if (isNewActionName) {
							selectedTestActionNameEltList.add(new String[] { "Index", cell.toString().trim() });
							indexset.add(cell.toString());

						}
					} else if (cn == 2) {
						if (!cell.toString().trim().equals("")) {
							this.updatePreActionList(cell.toString().trim());
							// only added if it is configured
							if (isNewActionName) {
								selectedTestActionNameEltList.add(new String[] { "PreAction", cell.toString().trim() });
							}

						}
					} else if (cn == 3) { // ElementName

						if (!cell.toString().trim().equals("")) {

							if (tmpElementNameList != null)
								// keep track of the Element Name value
								tmpElementNameList.add(cell.toString().trim());

							selectedTestActionNameEltList.add(new String[] { "ElementName", cell.toString().trim() });
						}

					} else if (cn == 4) { // ElementIdentificationKey

						if (!cell.toString().trim().equals("")) {
							updateEltIdentificationKeyList(cell.toString().trim());

							selectedTestActionNameEltList.add(new String[] { "ElementIdentificationKey", cell.toString().trim() });
						}

					} else if (cn == 5) { // ElementIdentificationValue

						if (!cell.toString().trim().equals("")) {
							updateEltIdentificationValueList(cell.toString().trim());
							selectedTestActionNameEltList.add(new String[] { "ElementIdentificationValue", cell.toString().trim() });
						}
					} else if (cn == 6) { // Action

						if (!cell.toString().trim().equals("")) {
							if (tmpActionList != null) {
								tmpActionList.add(cell.toString().trim());
							}
							this.updateActionList(cell.toString().trim());
							selectedTestActionNameEltList.add(new String[] { "Action", cell.toString().trim() });
						}

					} else if (cn == 7) { // TestData

						// keep track of mandatory TestData
						if (isNewActionName) {
							selectedTestActionNameEltList.add(new String[] { "TestData", cell.toString().trim() });

						}
						if (!cell.toString().trim().equals("")) {
							testDataValue = cell.toString().trim();
						}
					} else if (cn == 8) { // PostAction
						if (!cell.toString().trim().equals("")) {
							this.updatePostActionList(cell.toString().trim());
							// only added if it is configured
							if (isNewActionName) {
								selectedTestActionNameEltList.add(new String[] { "PostAction", cell.toString().trim() });
							}
						}
					} else if (cn == 10) { // TestRun
						if (isNewActionName) {
							selectedTestActionNameEltList.add(new String[] { "TestRun", cell.toString().trim() });
						}

					} else if (cn == 11) { // EndTestActionName

						if (!cell.toString().trim().equals("")) {
							this.updateEndTestActionNameList(cell.toString().trim());
							int i = 0;
							List<String> testDataEltNameList = new ArrayList<>();
							for (String eachAction : tmpActionList) {
								if (ActionKeywordLibrary.isRequiredInputActions(eachAction.trim())) {

									if (tmpElementNameList != null && tmpElementNameList.size() > 0) {
										testDataEltNameList.add(tmpElementNameList.get(i).trim());
									}
								}
								if (!testDataValue.trim().isEmpty() && !testDataEltNameList.isEmpty()) {

									this.updateTestDataMap(testDataValue, testDataEltNameList);

									this.updateTestActionDataMap(testActionName, testDataValue);
								}
								i++;
							}

							// Beginning of combining the ElementName from
							// PrevAction with the existing ElementName of the
							// current TestActionName
							List<String[]> tempMandatoryColumns = this.getMandatoryFieldMap().get(testActionName);
							List<String> combinedEltList = testDataEltNameList;
							if (tempMandatoryColumns != null) {
								for (String[] eColumn : tempMandatoryColumns) {
									if (eColumn != null & eColumn.length == 2) {

										if (eColumn[1] != null && eColumn[1].length() < 1) {
											continue;
										}

										if ("PreAction".equalsIgnoreCase(eColumn[0]) || "PostAction".equalsIgnoreCase(eColumn[0])) {

											String eColValue = eColumn[1];

											String[] eColValueStr = eColValue.split(",");

											String testData = "";

											for (String eTestActionName : eColValueStr) {

												if (eTestActionName != null && eTestActionName.length() > 0) {
													testData = this.getTestActionDataMap().get(eTestActionName);
													if (testData != null) {
														List<String> tempEltList = this.getTestDataMap().get(testData);
														if (tempEltList != null) {
															combinedEltList.addAll(tempEltList);
														}
													}
												}
												if (testData != null && testData.length() > 0) {
													updateTestDataMap(testDataValue, combinedEltList);
												}
											}

											break;
										}
									}
								}

							}
						}
					}

				}
			} else { // the first row - header row
				for (int cn = 0; cn < row.getLastCellNum(); cn++) {
					// If the cell is missing from the file, generate a blank
					// one
					// (Works by specifying a MissingCellPolicy)
					Cell cell = row.getCell(cn, Row.CREATE_NULL_AS_BLANK);
					this.updateTestSuiteHeaderElements(cell.toString().trim());
				}

			}

			if (!testActionName.trim().isEmpty() && selectedTestActionNameEltList != null) {
				updateMandatoryFieldMap(testActionName, selectedTestActionNameEltList);

			}
			rowCount++;
		}
		if (rowCount == 0) {
			flag = false;
			this.updateStatusMessages(kdPropertiesUtil.getProperty("missing_sheetcontent_message") + ": '" + curSheetName + "'");
		}
		return flag;
	}

	/**
	 * 
	 * @param key
	 * @return
	 */
	public boolean containsIdentificationKeys(String key) {
		return ElementIdentifyKeyLibrary.contains(key);

	}

	public void displayMandatoryField() {
		log.debug("---displayMandatoryField");
		Map<String, List<String[]>> mandatoryFields = getMandatoryFieldMap();
		Iterator<Entry<String, List<String[]>>> itr = mandatoryFields.entrySet().iterator();

		while (itr.hasNext()) {
			Entry<String, List<String[]>> entry = itr.next();
			String key = entry.getKey();
			log.info("TestActionName: " + key);
			List<String[]> list = entry.getValue();
			for (String[] each : list) {
				log.info(each[0] + " - " + each[1]);
			}
		}
	}

	/**
	 * 
	 */
	public void displayTestDataMap() {
		log.debug("---displayTestDataMap: " + this.getName());
		Map<String, List<String>> a = this.getTestDataMap();
		Iterator<Entry<String, List<String>>> itr = a.entrySet().iterator();

		while (itr.hasNext()) {
			Entry<String, List<String>> entry = itr.next();
			String key = entry.getKey();
			List<String> list = entry.getValue();
			log.info("TestData: " + key + " Elements: " + list);
		}
	}

	/**
	 * 
	 */
	public void displayTestActionDataMap() {
		log.debug("---displayTestActionDataMap: ");
		Map<String, String> a = this.getTestActionDataMap();
		Iterator<Entry<String, String>> itr = a.entrySet().iterator();

		while (itr.hasNext()) {
			Entry<String, String> entry = itr.next();
			String key = entry.getKey();
			String value = entry.getValue();
			log.info("TestActionName: " + key + " TestData: " + value);
		}
	}
}



