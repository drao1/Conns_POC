package com.etouch.taf.core.kd.config.test;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.etouch.taf.core.test.util.TafTestUtil;
import com.etouch.taf.kd.config.KDDataset;
import com.etouch.taf.kd.config.ReadDataset;
import com.etouch.taf.kd.exception.DuplicateDataKeyException;
import com.etouch.taf.util.ExcelUtil;
import com.etouch.taf.util.LogUtil;

@RunWith(MockitoJUnitRunner.class)
public class TestReadDataset {

	private static Log log = LogUtil.getLog(TestReadDataset.class);

	@Mock
	Cell startCell;

	@Mock
	Cell endCell;

	Workbook mockBook;
	Sheet mockSheet;

	@Before
	public void setup() throws InvalidFormatException, IOException {
		String current = System.getProperty("user.dir");
		String filePath = current + TafTestUtil.loadProps(TafTestUtil.propFilePath).getProperty("kdXlsFilePath");
		InputStream inp = new FileInputStream(filePath);

		mockBook = WorkbookFactory.create(inp);
		mockSheet = mockBook.getSheet("Test1_Data");

	}

	@Test
	public void testReadDataset() throws DuplicateDataKeyException {
		String dataKey = "LoginDataSet";
		
		ReadDataset readDataset = new ReadDataset();
		Collection<KDDataset> kdDataSetColls = readDataset.getDataSet(mockBook, dataKey, "Test1_Suite");

		for (KDDataset kdDataSet : kdDataSetColls) {

			for (Map.Entry<String, Object> dataSet : kdDataSet.getDataSetMap().entrySet()) {
				log.debug("[" + dataSet.getKey() + "] ==>" + dataSet.getValue().toString());
			}
		}
	}

	// @Test
	public void testParseDataset() throws DuplicateDataKeyException {
		String dataKey = "LoginDataSet";
		Cell cells[] = new Cell[2];
		cells = ExcelUtil.findCell(mockSheet, dataKey);

		ReadDataset readDataset = new ReadDataset();
		Collection<KDDataset> kdDatasetColls = readDataset.parseDataset(mockSheet, cells);

		for (KDDataset kdDataSet : kdDatasetColls) {

			for (Map.Entry<String, Object> dataSet : kdDataSet.getDataSetMap().entrySet()) {
				log.debug("[" + dataSet.getKey() + "] ==>" + dataSet.getValue().toString());
			}
		}

	}

}
