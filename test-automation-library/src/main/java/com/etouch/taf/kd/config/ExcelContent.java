package com.etouch.taf.kd.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import com.etouch.taf.core.resources.KDConstants;
import com.etouch.taf.util.LogUtil;

/**
 * 
 * @author ngoctran
 * 
 */
public class ExcelContent {

	private Workbook wb = null;

	private Set<String> masterTestActionNames = new HashSet<>();
	// temporary collection for validation, at the end of the validation it will
	// be empty
	private Set<String> uniqueTestActionNames = new HashSet<>();
	// testData & required elements name
	private Map<String, List<String>> masterTestDataMap = new HashMap<>();

	// keep available Test Suite Name & TestSuiteContent Object
	private Map<String, TestSuiteSheetContent> testSuiteSheetContentMap;
	private Map<String, TestDataSheetContent> testDataSheetContentMap;

	private List<String> invalidTestSheetNames;

	private int numSheets = 0;

	/**
	 * 
	 * @param wb
	 * @param numSheets
	 */
	public ExcelContent(Workbook wb, int numSheets) {
		this.wb = wb;
		this.numSheets = numSheets;
		invalidTestSheetNames = new ArrayList<>();
		testSuiteSheetContentMap = new HashMap<>();
		testDataSheetContentMap = new HashMap<>();
	}

	/**
	 * 
	 * @param numSheets
	 */
	public void setNumSheets(int numSheets) {
		this.numSheets = numSheets;
	}

	/**
	 * 
	 * @return
	 */
	public int getNumSheets() {
		return this.numSheets;
	}

	/**
	 * 
	 * @return
	 */
	public Map<String, TestSuiteSheetContent> getTestSuiteSheetContentMap() {
		return this.testSuiteSheetContentMap;
	}

	/**
	 * 
	 * @return
	 */
	public Map<String, TestDataSheetContent> getTestDataSheetContentMap() {
		return this.testDataSheetContentMap;
	}

	/**
	 * 
	 * @return
	 */
	public List<String> getInvalidTestSheetNames() {
		return this.invalidTestSheetNames;
	}

	/**
	 * 
	 * @param testSheetNames
	 */
	public void updateInvalidTestSheets(String testSheetNames) {
		this.invalidTestSheetNames.add(testSheetNames);
	}

	/**
	 * 
	 * @return
	 */
	public Set<String> getMasterTestActionNames() {
		return this.masterTestActionNames;
	}

	/**
	 * 
	 * @param testActionName
	 */
	public void updateMasterTestActionNames(String testActionName) {
		this.masterTestActionNames.add(testActionName);
	}

	/**
	 * 
	 * @param testActionName
	 */
	public void updateMasterTestActionNames(List<String> testActionNameList) {
		for (String each : testActionNameList) {
			updateMasterTestActionNames(each);
		}
	}

	/**
	 * 
	 * @return
	 */
	public Map<String, List<String>> getMasterTestDataMap() {
		return this.masterTestDataMap;
	}

	public void updateMasterTestDataMap(Map<String, List<String>> eachTestDataMap) {
		Iterator<Entry<String, List<String>>> itr = eachTestDataMap.entrySet().iterator();

		while (itr.hasNext()) {
			Entry<String, List<String>> entry = itr.next();
			this.masterTestDataMap.put(entry.getKey(), entry.getValue());
		}

	}

	/**
	 * New
	 * 
	 * @param excelValidator
	 * @throws Exception
	 */
	public void parseExcelContent() throws Exception {
		String curSheetName = "";
		Sheet curSheet = null;
		boolean flag = true;
		// loop through each sheets
		for (int i = 0; i < numSheets; i++) {
			curSheetName = wb.getSheetName(i);
			curSheet = wb.getSheetAt(i);

			// read the suite content
			TestSuiteSheetContent testSuiteSheetContent;
			// Suite Content
			if (curSheetName.endsWith(KDConstants.TS_SHEETNAME_POSTFIX.getValue().trim()) || curSheetName.contains(KDConstants.TS_SHEETNAME_POSTFIX.getValue().trim())) {
				testSuiteSheetContent = new TestSuiteSheetContent(curSheetName);

				flag = testSuiteSheetContent.buildContent(curSheetName, curSheet);
				if (flag) {

					this.getTestSuiteSheetContentMap().put(curSheetName, testSuiteSheetContent);
					this.updateMasterTestActionNames(testSuiteSheetContent.getTestActionNameList());
				}
				this.updateMasterTestDataMap(testSuiteSheetContent.getTestDataMap());

			} else if (curSheetName.matches("(.+)[^Global]" + KDConstants.TD_SHEETNAME_POSTFIX.getValue().trim())
					|| curSheetName.matches("(.+)" + KDConstants.TD_SHEETNAME_GLOBAL_POSTFIX.getValue().trim())) {

				TestDataSheetContent testDataSheetContent = new TestDataSheetContent(curSheetName);
				flag = testDataSheetContent.buildContent(curSheetName, curSheet);
				if (flag) {
					this.getTestDataSheetContentMap().put(curSheetName, testDataSheetContent);
				}
			} else {
				this.updateInvalidTestSheets(curSheetName);
			}
		}

		// build uniqueTestActionNames
		for (Map.Entry<String, TestSuiteSheetContent> entry : getTestSuiteSheetContentMap().entrySet()) {
			TestSuiteSheetContent testSuiteSheetContent = entry.getValue();

			if (testSuiteSheetContent != null) {
				List<String> testActioNameList = testSuiteSheetContent.getTestActionNameList();

				for (String each : testActioNameList) {
					if (each == null) {
						continue;
					}
					if (!this.uniqueTestActionNames.contains(each.trim())) {
						this.updateUniqueTestActionNames(each);
					}
				}
			}

		}

	}

	/**
	 * 
	 * @param testActionNames
	 */
	public void updateUniqueTestActionNames(String testActionNames) {
		this.uniqueTestActionNames.add(testActionNames);
	}

	/**
	 * 
	 * @param testActionNames
	 */
	public void removeFromUniqueTestActionNames(String testActionNames) {
		this.uniqueTestActionNames.remove(testActionNames);
	}

	/**
	 * 
	 * @param testActionNameList
	 */
	public void updateUniqueTestActionNames(List<String> testActionNameList) {
		for (String each : testActionNameList) {
			updateUniqueTestActionNames(each);
		}
	}

	/**
	 * 
	 * @return
	 */
	public Set getUniqueTestActionNames() {
		return this.uniqueTestActionNames;
	}

	/**
	 * 
	 * @param testActionNames
	 */
	public void setUniqueTestActionNames(Set<String> testActionNames) {
		this.uniqueTestActionNames = testActionNames;
	}
}