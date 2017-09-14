package com.etouch.taf.core.datamanager.excel;

import java.io.FileInputStream;
import java.io.FileOutputStream;

import org.apache.commons.logging.Log;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFCreationHelper;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFHyperlink;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.IndexedColors;

import com.etouch.taf.util.LogUtil;

/**
 * The Class Xls_Reader.
 */
public class XlsReader extends ExcelReader {

	/** The log. */
	private static Log log = LogUtil.getLog(XlsReader.class);

	/** The path. */
	private String path;

	/** The file out. */
	private FileOutputStream fileOut = null;

	/** The workbook. */
	private HSSFWorkbook workbook = null;

	/** The sheet. */
	private HSSFSheet sheet = null;

	/** The row. */
	private HSSFRow row = null;

	/** The cell. */
	private HSSFCell cell = null;

	/**
	 * Instantiates a new xls_ reader.
	 * 
	 * @param path
	 *            the path
	 */
	public XlsReader(String path) {

		this.path = path;
		try (FileInputStream fis = new FileInputStream(path)) {
			workbook = new HSSFWorkbook(fis);
			sheet = workbook.getSheetAt(0);

		} catch (Exception e) {
			log.error(e);
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
	 * returns the data from a cell using sheet name, column name and row number
	 * 
	 * @see com.etouch.taf.core.datamanager.excel.ExcelReader#getCellData(java.lang
	 *      .String, java.lang.String, int)
	 */
	@Override
	public String getCellData(String sheetName, String colName, int rowNum) {
		return getCellData(workbook, sheetName, colName, rowNum);
	}

	/**
	 * returns the data from a cell
	 * 
	 * @see com.etouch.taf.core.datamanager.excel.ExcelReader#getCellData(java.lang
	 *      .String, int, int)
	 */
	@Override
	public String getCellData(String sheetName, int colNum, int rowNum) {
		return getCellData(workbook, sheetName, colNum, rowNum);
	}

	/**
	 * Sets the cell data.returns true if data is set successfully else false
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
			workbook = new HSSFWorkbook(fis);
			isSet = setCellData(path, workbook, sheetName, colName, rowNum, data);

		} catch (Exception ex) {
			log.error(ex.getMessage(), ex);
			return false;
		}
		return isSet;
	}

	/**
	 * returns true if data is set successfully else false
	 * 
	 * @see com.etouch.taf.core.datamanager.excel.ExcelReader#setCellData(java.lang
	 *      .String, java.lang.String, int, java.lang.String, java.lang.String)
	 */
	@Override
	public boolean setCellData(String sheetName, String colName, int rowNum, String data, String url) {
		try (FileInputStream fis = new FileInputStream(path)) {
			workbook = new HSSFWorkbook(fis);
			sheet = (HSSFSheet) getSheet(workbook, sheetName);

			if (sheet != null) {
				int colNum = getColNumber(sheet, colName);

				if (rowNum <= 0 || colNum < 0)
					return false;

				cell = (HSSFCell) setCellValue(sheet, rowNum, colNum, data);
				setHyperlink(cell, url);
				fileOut = new FileOutputStream(path);
				workbook.write(fileOut);
			}

		} catch (Exception e) {
			log.error(e);
			return false;
		} finally {
			closeFileObjects(fileOut);
		}
		return true;
	}

	/**
	 * Sets the hyperlink.
	 * 
	 * @param cell
	 *            the cell
	 * @param url
	 *            the url
	 */
	public void setHyperlink(HSSFCell cell, String url) {
		HSSFCreationHelper createHelper = workbook.getCreationHelper();

		// cell style for hyperlinks
		// by default hypelrinks are blue and underlined
		CellStyle hlinkstyle = workbook.createCellStyle();
		HSSFFont hlinkfont = workbook.createFont();
		hlinkfont.setUnderline(HSSFFont.U_SINGLE);
		hlinkfont.setColor(IndexedColors.BLUE.getIndex());
		hlinkstyle.setFont(hlinkfont);

		HSSFHyperlink link = createHelper.createHyperlink(HSSFHyperlink.LINK_FILE);
		link.setAddress(url);
		cell.setHyperlink(link);
		cell.setCellStyle(hlinkstyle);
	}

	/**
	 * Adds the sheet.
	 * 
	 * @param sheetname
	 *            the sheetname
	 * @return true, if successful
	 */
	public boolean addSheet(String sheetname) {

		boolean isAdded = false;
		try (FileInputStream fis = new FileInputStream(path)) {
			workbook = new HSSFWorkbook(fis);
			isAdded = addSheet(path, workbook, sheetname);
		} catch (Exception ex) {
			log.error(ex);
		}
		return isAdded;
	}

	/**
	 * Removes the sheet.
	 * 
	 * @param sheetName
	 *            the sheet name
	 * @return true, if successful
	 */
	public boolean removeSheet(String sheetName) {
		boolean isRemoved = false;
		try (FileInputStream fis = new FileInputStream(path)) {
			workbook = new HSSFWorkbook(fis);
			isRemoved = removeSheet(path, workbook, sheetName);
		} catch (Exception ex) {
			log.error(ex);
		}
		return isRemoved;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.etouch.taf.core.datamanager.excel.ExcelReader#addColumn(java.lang
	 * .String, java.lang.String)
	 */
	@SuppressWarnings("deprecation")
	@Override
	public boolean addColumn(String sheetName, String colName) {
		try (FileInputStream fis = new FileInputStream(path)) {
			workbook = new HSSFWorkbook(fis);
			int index = workbook.getSheetIndex(sheetName);
			if (index == -1) {
				return false;
			}

			HSSFCellStyle style = workbook.createCellStyle();
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
			log.error(e);
			return false;
		} finally {
			closeFileObjects(fileOut);
		}
		return true;

	}

	/**
	 * (non-Javadoc)
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
			workbook = new HSSFWorkbook(fis);
			sheet = workbook.getSheet(sheetName);
			HSSFCellStyle style = workbook.createCellStyle();
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
			log.error(e);
			return false;
		} finally {
			closeFileObjects(fileOut);
		}

		return true;

	}

	/**
	 * Checks if is sheet exist.
	 * 
	 * @param sheetName the sheet name
	 * 
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
	 * (non-Javadoc)
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
