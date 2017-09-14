package com.etouch.taf.kd.config;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import com.etouch.taf.core.resources.KDConstants;
import com.etouch.taf.kd.exception.DuplicateDataKeyException;
import com.etouch.taf.util.ExcelUtil;

public class ReadDataset {

	/**
	 * Reads the Dataset from Excel.
	 * 
	 * @param wb
	 * @param dataKey
	 * @param testSuiteSheetName
	 * @throws DuplicateDataKeyException
	 */
	public Collection<KDDataset> getDataSet(Workbook wb, String dataKey, String testSuiteSheetName) throws DuplicateDataKeyException {

		int sheetCount = wb.getNumberOfSheets();

		Collection<KDDataset> kdDataSetColl = new ArrayList<>();

		for (int index = 0; index < sheetCount; index++) {
			String dataSetSheetName = wb.getSheetName(index);
			Sheet sheet = wb.getSheetAt(index);

			kdDataSetColl = buildKdDataSet(sheet, dataKey, dataSetSheetName, testSuiteSheetName, kdDataSetColl);
		}

		return kdDataSetColl;

	}

	/**
	 * @param wb
	 * @param dataKey
	 * @param kdDataSetColl
	 * @param index
	 * @param dataSetSheetName
	 * @param testSuiteSheetName
	 * @param kdDataSetColl
	 * @return
	 * @throws DuplicateDataKeyException
	 */
	private Collection<KDDataset> buildKdDataSet(Sheet sheet, String dataKey, String dataSetSheetName, String testSuiteSheetName, Collection<KDDataset> kdDataSetColl)
			throws DuplicateDataKeyException {

		Collection<KDDataset> overriddenDataSet = new ArrayList<>();

		String testSuiteName = testSuiteSheetName.substring(0, testSuiteSheetName.indexOf(KDConstants.TS_SHEETNAME_POSTFIX.getValue()));

		if (dataSetSheetName.equals(testSuiteName.concat(KDConstants.TD_SHEETNAME_POSTFIX.getValue()))) {

			overriddenDataSet = populateDataSetColl(sheet, dataKey);

		} else if ((kdDataSetColl.isEmpty()) && (dataSetSheetName.indexOf(KDConstants.TD_SHEETNAME_GLOBAL_POSTFIX.getValue()) > -1)) {

			overriddenDataSet = populateDataSetColl(sheet, dataKey);
		}

		if (!overriddenDataSet.isEmpty()) {
			kdDataSetColl = overriddenDataSet;
		}

		return kdDataSetColl;
	}

	/**
	 * @param sheet
	 * @param dataKey
	 * @param kdDataSetColl
	 * @param isDataOverridden
	 * @return
	 * @throws DuplicateDataKeyException
	 */
	private Collection<KDDataset> populateDataSetColl(Sheet sheet, String dataKey) throws DuplicateDataKeyException {

		Collection<KDDataset> kdDataSetColl = new ArrayList<>();

		Cell[] cells = findDataKey(sheet, dataKey);

		if (!ExcelUtil.isCellBlank(cells[0]) && !ExcelUtil.isCellBlank(cells[1])) {
			kdDataSetColl = parseDataset(sheet, cells);
		}

		return kdDataSetColl;
	}

	/**
	 * Reads particular dataset based on the given datakey
	 * 
	 * @param cells
	 * @throws DuplicateDataKeyException
	 */
	public Collection<KDDataset> parseDataset(Sheet dataSheet, Cell[] cells) throws DuplicateDataKeyException {

		Row headerRow = dataSheet.getRow(cells[0].getRowIndex());

		Map<Integer, String> headerRowMap = new HashMap<>();

		for (int colIndex = 1; colIndex < cells[1].getColumnIndex(); colIndex++) {
			String dataSetKey = headerRow.getCell(colIndex).getStringCellValue();

			headerRowMap.put(new Integer(colIndex), dataSetKey);
		}

		Collection<KDDataset> kdDataSetColls = new ArrayList<>();

		int startIndex = cells[0].getRowIndex();
		int endIndex = cells[1].getRowIndex();

		for (int rowIndex = startIndex + 1; rowIndex <= endIndex; rowIndex++) {

			Map<String, Object> dataMap = new HashMap<>();

			String executionHeader = headerRowMap.get(1);
			String executionValue = getDataValue(dataSheet, rowIndex, 1).toString();

			if (executionHeader.equalsIgnoreCase(KDConstants.TD_EXECUTION_LABEL.getValue()) && executionValue.equalsIgnoreCase(KDConstants.EXECUTE.getValue())) {

				for (int colIndex = 2; colIndex < cells[1].getColumnIndex(); colIndex++) {

					dataMap.put(headerRowMap.get(colIndex), getDataValue(dataSheet, rowIndex, colIndex));
				}

				KDDataset kdDataset = new KDDataset(dataMap);
				kdDataSetColls.add(kdDataset);
			}

		}

		return kdDataSetColls;
	}

	private Object getDataValue(Sheet dataSheet, int rowIndex, int colIndex) {

		Object dataValue = null;

		Row dataRow = dataSheet.getRow(rowIndex);
		Cell dataCell = dataRow.getCell(colIndex);

		if (null == dataCell || dataCell.getCellType() == HSSFCell.CELL_TYPE_BLANK) {

			dataValue = new String("");

		} else {
			if (dataCell.getCellType() == HSSFCell.CELL_TYPE_STRING) {
				dataValue = dataCell.getStringCellValue();

			} else if (dataCell.getCellType() == HSSFCell.CELL_TYPE_NUMERIC) {
				dataValue = ExcelUtil.parserNumberFormat(dataCell.getNumericCellValue());

			} else if (dataCell.getCellType() == HSSFCell.CELL_TYPE_BOOLEAN) {
				dataValue = String.valueOf(dataCell.getBooleanCellValue());
			}
		}

		return dataValue;
	}

	private Cell[] findDataKey(Sheet dataSheet, String dataKey) {
		return ExcelUtil.findCell(dataSheet, dataKey);
	}

}
