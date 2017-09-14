package com.etouch.taf.kd.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import com.etouch.taf.util.KDPropertiesUtil;
import com.etouch.taf.util.LogUtil;

public class TestDataSheetContent {
	static Log log = LogUtil.getLog(TestDataSheetContent.class);
	private String name;

	private List<String> statusMessages = new ArrayList<>();

	// first column value that map to TestData value & list each column's value
	private Map<String, List<Object>> testDataMap = new HashMap<>();

	// first column value that map to TestData value & list each column's header
	private Map<String, List<Object>> testDataEltNameMap = new HashMap<>();

	KDPropertiesUtil kdPropertiesUtil = KDPropertiesUtil.getInstance();

	public TestDataSheetContent() {
	}

	public TestDataSheetContent(String name) {
		this.name = name;

	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void updateStatusMessages(String statusMessage) {
		this.statusMessages.add(statusMessage);
	}

	public List<String> getStatusMessages() {
		return this.statusMessages;
	}

	/**
	 * 
	 */
	public void displayStatusMessages() {
		for (String statusMessage : this.statusMessages) {
			log.debug(statusMessage);
		}
	}

	public void updateTestDataMap(String testData, List<Object> eltNameList) {

		if (testDataMap != null) {
			if (!testDataMap.containsKey(testData)) {
				testDataMap.put(testData, eltNameList);
			}

		}
	}

	/**
	 * Contains the element value for each row
	 * 
	 * @return
	 */
	public Map<String, List<Object>> getTestDataMap() {
		return this.testDataMap;

	}

	public void updateTestDataEltNameMap(String testData, List<Object> eltNameList) {

		if (testDataEltNameMap != null) {
			if (!testDataEltNameMap.containsKey(testData)) {
				testDataEltNameMap.put(testData, eltNameList);
			}

		}
	}

	/**
	 * Contains the header element name
	 * 
	 * @return
	 */
	public Map<String, List<Object>> getTestDataEltNameMap() {
		return this.testDataEltNameMap;

	}

	/**
	 * 
	 */
	public void displayTestDataMap() {
		log.debug("---displayTestDataMap");

		for (Map.Entry<String, List<Object>> entry : this.getTestDataMap().entrySet()) {
			String key = entry.getKey();
			log.info("key: " + key);
			List<Object> list = entry.getValue();
			log.info("value: " + list.toString());
		}

	}

	/**
	 * 
	 */
	public void displayTestDataEltNameMap() {
		log.debug("---displayTestDataHeaderMap");

		for (Map.Entry<String, List<Object>> entry : this.getTestDataEltNameMap().entrySet()) {
			String key = entry.getKey();
			log.info("key: " + key);
			List<Object> list = entry.getValue();
			log.info("value: " + list.toString());
		}

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

		String testDataName = "";
		List<Object> eltNameValueList = new ArrayList<>();
		for (Row row : curSheet) {
			// loop through each row
			boolean isNewDataName = false;

			List<Object> eltNameList = new ArrayList<>();

			// loop through each cell per row
			for (int cn = 0; cn < row.getLastCellNum(); cn++) {

				// If the cell is missing from the file, generate a blank one
				// (Works by specifying a MissingCellPolicy)
				Cell cell = row.getCell(cn, Row.CREATE_NULL_AS_BLANK);
				if (cell == null)
					continue;
				if (cn == 0) { // TestData
					if (!cell.toString().trim().equals("")) {
						testDataName = cell.toString().trim();
						isNewDataName = true;
					} else {
						isNewDataName = false;
					}
				} else { // the rest columns

					if (isNewDataName) { // store only the element data
						if (!cell.toString().trim().equals("")) {

							eltNameList.add(cell.toString().trim());

						}
					} else {

						Object cellValue = null;
						if (null == cell || cell.getCellType() == HSSFCell.CELL_TYPE_BLANK) {
							cellValue = "";
						} else {

							if (cell.getCellType() == HSSFCell.CELL_TYPE_STRING) {
								cellValue = cell.getStringCellValue();
							} else if (cell.getCellType() == HSSFCell.CELL_TYPE_NUMERIC) {
								cellValue = cell.getNumericCellValue();
							} else if (cell.getCellType() == HSSFCell.CELL_TYPE_BOOLEAN) {
								cellValue = cell.getBooleanCellValue();
							}

							eltNameValueList.add(cellValue);
							this.updateTestDataMap(testDataName, eltNameValueList);

						}
					}
					updateTestDataEltNameMap(testDataName, eltNameList);
				}
				rowCount++;

				if (isNewDataName) {

					eltNameValueList = new ArrayList<>();
				}
			}
		}
		if (rowCount == 0) {
			flag = false;
			this.updateStatusMessages(kdPropertiesUtil.getProperty("missing_sheetcontent_message") + ": '" + curSheetName + "'");
		}

		return flag;
	}

}
