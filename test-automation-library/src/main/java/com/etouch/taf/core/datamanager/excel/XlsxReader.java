package com.etouch.taf.core.datamanager.excel;

import java.io.FileInputStream;
import java.io.FileOutputStream;

import org.apache.commons.logging.Log;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFCreationHelper;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFHyperlink;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.etouch.taf.util.LogUtil;

/**
 * The Class Xlsx_Reader.
 */
public class XlsxReader extends ExcelReader {

	/** The log. */
	private static Log log = LogUtil.getLog(XlsxReader.class);

	/** The path. */
	private String path;

	/** The file out. */
	private FileOutputStream fileOut = null;

	/** The workbook. */
	private XSSFWorkbook workbook = null;

	/** The sheet. */
	private XSSFSheet sheet = null;

	/** The row. */
	private XSSFRow row = null;

	/** The cell. */
	private XSSFCell cell = null;

	/**
	 * Instantiates a new xlsx_ reader.
	 * 
	 * @param path
	 *            the path
	 */
	public XlsxReader(String path) {

		this.path = path;
		try (FileInputStream fis = new FileInputStream(path)) {
			workbook = new XSSFWorkbook(fis);
			sheet = workbook.getSheetAt(0);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
	}

	
	/**
	 * Read xls data.
	 * 
	 * @param sheetName
	 *            the sheet name
	 * @return the string[][]
	 */
	public String[][] readXLSData(String sheetName) {
		return readXLSData(workbook, sheetName);
	}

	/**
	 * Gets the row count.
	 * 
	 * @param sheetName
	 *            the sheet name
	 * @return the row count
	 */
	public int getRowCount(String sheetName) {
		return getRowCount(workbook, sheetName);
	}

	
	/**
	 * returns the data from a cell
	 * 
	 * @see
	 * com.etouch.taf.core.datamanager.excel.ExcelReader#getCellData(java.lang
	 * .String, java.lang.String, int)
	 */
	@Override
	public String getCellData(String sheetName, String colName, int rowNum) {
		return getCellData(workbook, sheetName, colName, rowNum);
	}

	
	/**
	 * returns the data from a cell
	 * 
	 * @see
	 * com.etouch.taf.core.datamanager.excel.ExcelReader#getCellData(java.lang
	 * .String, int, int)
	 */
	@Override
	public String getCellData(String sheetName, int colNum, int rowNum) {
		return getCellData(workbook, sheetName, colNum, rowNum);
	}

	/**
	 * Sets the cell data.
	 * 
	 * @param sheetName
	 *            the sheet name
	 * @param colName
	 *            the col name
	 * @param rowNum
	 *            the row num
	 * @param data
	 *            the data
	 * @return true, if successful
	 */
	public boolean setCellData(String sheetName, String colName, int rowNum, String data) {
		boolean isSet = false;
		try (FileInputStream fis = new FileInputStream(path)) {
			workbook = new XSSFWorkbook(fis);
			isSet = setCellData(path, workbook, sheetName, colName, rowNum, data);
		} catch (Exception ex) {
			log.error(ex.getMessage(), ex);
		} 
		return isSet;
	}

	
	/**
	 * returns true if data is set successfully else false
	 * 
	 * @see
	 * com.etouch.taf.core.datamanager.excel.ExcelReader#setCellData(java.lang
	 * .String, java.lang.String, int, java.lang.String, java.lang.String)
	 */
	@Override
	public boolean setCellData(String sheetName, String colName, int rowNum, String data, String url) {
		boolean isSet = false;
		try (FileInputStream fis = new FileInputStream(path)) {
			workbook = new XSSFWorkbook(fis);
			sheet = (XSSFSheet) getSheet(workbook, sheetName);

			if (sheet != null) {
				int colNum = getColNumber(sheet, colName);

				if (rowNum <= 0 || colNum < 0)
					return false;

				cell = (XSSFCell) setCellValue(sheet, rowNum, colNum, data);
				setHyperlink(cell, url);

				fileOut = new FileOutputStream(path);
				workbook.write(fileOut);

				isSet = true;
			}

		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return false;
		} finally {
			closeFileObjects(fileOut);
		}
		return isSet;
	}

	/**
	 * Sets the hyperlink.
	 * 
	 * @param cell
	 *            the cell
	 * @param url
	 *            the url
	 */
	public void setHyperlink(XSSFCell cell, String url) {
		XSSFCreationHelper createHelper = workbook.getCreationHelper();

		// cell style for hyperlinks
		// by default hypelrinks are blue and underlined
		CellStyle hlinkStyle = workbook.createCellStyle();
		XSSFFont hlinkFont = workbook.createFont();
		hlinkFont.setUnderline(XSSFFont.U_SINGLE);
		hlinkFont.setColor(IndexedColors.BLUE.getIndex());
		hlinkStyle.setFont(hlinkFont);

		XSSFHyperlink link = createHelper.createHyperlink(XSSFHyperlink.LINK_FILE);
		link.setAddress(url);
		cell.setHyperlink(link);
		cell.setCellStyle(hlinkStyle);
	}

	
	/**
	 * Adds the sheet. returns true if sheet is created successfully else false
	 * 
	 * @param sheetName
	 *            the sheet name
	 * @return true, if successful
	 */
	public boolean addSheet(String sheetName) {
		boolean isSheetAdded = false;
		try (FileInputStream fis = new FileInputStream(path)) {
			workbook = new XSSFWorkbook(fis);
			isSheetAdded = addSheet(path, workbook, sheetName);
		} catch (Exception ex) {
			log.error(ex.getMessage(), ex);
		}
		return isSheetAdded;
	}

	/**
	 * Removes the sheet. returns true if sheet is removed successfully else false if sheet does not exist
	 * 
	 * @param sheetName
	 *            the sheet name
	 * @return true, if successful
	 */
	public boolean removeSheet(String sheetName) {
		boolean isSheetRemoved = false;
		try (FileInputStream fis = new FileInputStream(path)) {
			workbook = new XSSFWorkbook(fis);
			isSheetRemoved = removeSheet(path, workbook, sheetName);
		} catch (Exception ex) {
			log.error(ex.getMessage(), ex);
		}
		return isSheetRemoved;
	}

	
	/**
	 * returns true if column is created successfully
	 * 
	 * @see
	 * com.etouch.taf.core.datamanager.excel.ExcelReader#addColumn(java.lang
	 * .String, java.lang.String)
	 */
	@Override
	public boolean addColumn(String sheetName, String colName) {
		try (FileInputStream fis = new FileInputStream(path)) {
			workbook = new XSSFWorkbook(fis);
			int index = workbook.getSheetIndex(sheetName);
			if (index == -1) {
				return false;
			}

			XSSFCellStyle style = workbook.createCellStyle();
			style.setFillForegroundColor(HSSFColor.GREY_40_PERCENT.index);
			style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);

			sheet = workbook.getSheetAt(index);

			row = sheet.getRow(0);
			if (row == null) {
				row = sheet.createRow(0);
			}

			if (row.getLastCellNum() == -1) {
				cell = row.createCell(0);
			} else {
				cell = row.createCell(row.getLastCellNum());
			}

			cell.setCellValue(colName);
			cell.setCellStyle(style);

			fileOut = new FileOutputStream(path);
			workbook.write(fileOut);

		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return false;
		} finally {
			closeFileObjects(fileOut);
		}

		return true;

	}

	
	/**
	 * removes a column and all the contents
	 * 
	 * @see
	 * com.etouch.taf.core.datamanager.excel.ExcelReader#removeColumn(java.lang
	 * .String, int)
	 */
	@Override
	public boolean removeColumn(String sheetName, int colNum) {
		try (FileInputStream fis = new FileInputStream(path)) {
			if (!isSheetExist(sheetName)) {
				return false;
			}
			workbook = new XSSFWorkbook(fis);
			sheet = workbook.getSheet(sheetName);
			XSSFCellStyle style = workbook.createCellStyle();
			style.setFillForegroundColor(HSSFColor.GREY_40_PERCENT.index);
			style.setFillPattern(HSSFCellStyle.NO_FILL);

			for (int i = 0; i < getRowCount(sheetName); i++) {
				row = sheet.getRow(i);
				if (row != null) {
					cell = row.getCell(colNum);
					if (cell != null) {
						cell.setCellStyle(style);
						row.removeCell(cell);
					}
				}
			}
			fileOut = new FileOutputStream(path);
			workbook.write(fileOut);
		} catch (Exception e) {
			log.error(e.getMessage(),e);
			return false;
		} finally {
			closeFileObjects(fileOut);
		}
		return true;

	}

	/**
	 * Checks if is sheet exist.
	 * 
	 * @param sheetName
	 *            the sheet name
	 * @return true, if is sheet exist
	 */
	public boolean isSheetExist(String sheetName) {
		return isSheetExist(workbook, sheetName);
	}

	/**
	 * Gets the column count.
	 * 
	 * @param sheetName
	 *            the sheet name
	 * @return the column count
	 */
	public int getColumnCount(String sheetName) {
		return getColumnCount(workbook, sheetName);
	}

	
	/**
	 * String sheetName, String testCaseName,String keyword ,String URL,String message
	 * 
	 * @see
	 * com.etouch.taf.core.datamanager.excel.ExcelReader#addHyperLink(java.lang
	 * .String, java.lang.String, java.lang.String, int, java.lang.String,
	 * java.lang.String)
	 */
	@Override
	public boolean addHyperLink(String sheetName, String screenShotColName, String testCaseName, int index, String urlValue, String message) {
		String url = urlValue.replace('\\', '/');
		if (!isSheetExist(sheetName)) {
			return false;
		}
		sheet = workbook.getSheet(sheetName);

		for (int i = 2; i <= getRowCount(sheetName); i++) {
			if (getCellData(sheetName, 0, i).equalsIgnoreCase(testCaseName)) {
				setCellData(sheetName, screenShotColName, i + index, message, url);
				break;
			}
		}

		return true;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.etouch.taf.core.datamanager.excel.ExcelReader#getCellRowNum(java.
	 * lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public int getCellRowNum(String sheetName, String colName, String cellValue) {

		for (int i = 2; i <= getRowCount(sheetName); i++) {
			if (getCellData(sheetName, colName, i).equalsIgnoreCase(cellValue)) {
				return i;
			}
		}
		return -1;

	}
}
