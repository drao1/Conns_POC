package com.etouch.taf.kd.config;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import com.etouch.taf.core.config.TestBedManagerConfiguration;
import com.etouch.taf.core.resources.KDConstants;
import com.etouch.taf.core.resources.XLFormat;
import com.etouch.taf.kd.exception.DuplicateDataKeyException;
import com.etouch.taf.kd.exception.InvalidActionException;
import com.etouch.taf.kd.exception.InvalidValueException;
import com.etouch.taf.kd.validator.ExcelValidator;
import com.etouch.taf.util.ExcelUtil;
import com.etouch.taf.util.KDPropertiesUtil;
import com.etouch.taf.util.LogUtil;

public class ReadExcel {

	private static Log log = LogUtil.getLog(ReadExcel.class);
	
	Workbook wb = null;
	private ReadDataset readDataset;
 
	private int numSheets = 0;
	
	private Map<String, LinkedList<ActionElement>> actionElementMap = new HashMap<>();
			
	KDPropertiesUtil kdPropertiesUtil = KDPropertiesUtil.getInstance();
	
	public ReadExcel() {}
		  
	public ReadExcel(String filePath) throws InvalidFormatException, IOException 
	{
		wb = WorkbookFactory.create(new FileInputStream(filePath));
		numSheets = wb.getNumberOfSheets();
		readDataset = new ReadDataset();
	}

	/**
	 * Read file information based on the the filePath
	 * 
	 * @param filePath
	 * @throws InvalidFormatException
	 * @throws IOException
	 * @throws InvalidActionException
	 * @throws InvalidValueException
	 * @throws DuplicateDataKeyException
	 */
	public List<TestSuite> readFile()
			throws InvalidFormatException, IOException, InvalidActionException,
			InvalidValueException, DuplicateDataKeyException {

		List<TestSuite> testSuites = getParallelTestSuites();;
		
		TestBedManagerConfiguration tbManagerConfig = TestBedManagerConfiguration.getInstance();
		
		String tsExcecutionStyle = tbManagerConfig.getKdConfig().getParallelMode();
		
		if( !tsExcecutionStyle.contains(KDConstants.PARALLEL_TESTSUITES.getValue()) && !tsExcecutionStyle.contains(KDConstants.PARALLEL_TESTCASES.getValue()) ){
			testSuites =  getIndexedTestSuites(testSuites);
		}
		
		return testSuites;
	}

	private Map<Sheet, TestSuite> testSuiteMap = new HashMap<>();
	
	/**
	 * 
	 * @return
	 * @throws InvalidActionException
	 * @throws InvalidValueException
	 * @throws DuplicateDataKeyException
	 */
	private List<TestSuite> getParallelTestSuites() throws InvalidActionException, InvalidValueException, DuplicateDataKeyException {
		
		List<TestSuite> testSuites = new ArrayList<>();
		
		if(!testSuiteMap.isEmpty())
			testSuiteMap.clear();
			
		for (int i = 0; i < wb.getNumberOfSheets(); i++) {
			
			Sheet sheet = wb.getSheetAt(i);
			
			buildTestSuites(testSuites, sheet);
		}
		
		populateReferenceAction();
		
		return testSuites;
	}
	

	/**
	 * @param testSuites
	 * @param testSuiteMap
	 * @param sheet
	 * @throws InvalidActionException
	 * @throws InvalidValueException
	 * @throws DuplicateDataKeyException
	 */
	private void buildTestSuites(List<TestSuite> testSuites, Sheet sheet)
			throws InvalidActionException, InvalidValueException,
			DuplicateDataKeyException {
		
		String sheetName = sheet.getSheetName();
		
		if(sheetName.indexOf(KDConstants.TS_SHEETNAME_POSTFIX.getValue()) > -1){
			
			sheetName = sheetName.substring(0, sheetName.indexOf(KDConstants.TS_SHEETNAME_POSTFIX.getValue()));
			log.debug("Sheet name ==>"+sheetName);
			
			TestSuite testSuite = new TestSuite();
			testSuite.setName(sheetName);

			ArrayList<TestAction> testActions = getTestActions(testSuite, sheet);
			
			testSuite.setTestActions(testActions);

			testSuiteMap.put(sheet, testSuite);
			
			testSuites.add(testSuite);
		}
		
	}
	
	/**
	 * @param testSuiteMap 
	 * 
	 */
	private void populateReferenceAction() {
		
		for(Map.Entry<Sheet, TestSuite> suitesEntry : testSuiteMap.entrySet()){
			
			TestSuite testSuite = suitesEntry.getValue();
			
			for(TestAction testAction : testSuite.getTestActions()){
				getReferenceActions(testAction, suitesEntry.getKey(),  XLFormat.TS_PRE_ACTION_COLUMN.getIndex() );
				getReferenceActions(testAction, suitesEntry.getKey(),  XLFormat.TS_POST_ACTION_COLUMN.getIndex());
			}
		}
	}
	
	
	/**
	 * 
	 * @param testSuiteList 
	 * @return
	 * @throws InvalidActionException
	 * @throws InvalidValueException
	 * @throws DuplicateDataKeyException
	 */
	private List<TestSuite> getIndexedTestSuites(List<TestSuite> testSuiteList) throws InvalidActionException, InvalidValueException, DuplicateDataKeyException {
		
		ArrayList<TestSuite> testSuites = new ArrayList<>();
		
		ArrayList<TestAction> indexedTestActions = new ArrayList<>();
		
		TestSuite testSuite = new TestSuite();
		testSuite.setName(KDConstants.TS_SHEETNAME_COMMON.getValue());
		
		reIndexAllTestActions(testSuiteList);
		
		for(TestSuite currentTestSuite : testSuiteList ){

			indexedTestActions.addAll(currentTestSuite.getTestActions());
		}
		
		testSuite.setTestActions(indexedTestActions);
		
		testSuites.add(testSuite);
		
		return testSuites;
	}

	/**
	 * @param testSuiteList
	 */
	private void reIndexAllTestActions(List<TestSuite> testSuiteList) {
		
		TestSuite firstTestSuite = testSuiteList.get(0);
		Double lastActionIndex = getLastActionIndex(firstTestSuite);

		for ( int testSuitePos = 1; testSuitePos < testSuiteList.size(); testSuitePos++ ){
			
			TestSuite nextTestSuite = testSuiteList.get(testSuitePos);
			
			for ( TestAction testAction : nextTestSuite.getTestActions() ){
				
				Double currentActionIndex = testAction.getIndex();
				
				lastActionIndex += currentActionIndex;
				
				testAction.setIndex(lastActionIndex);
				
			}
			
		}
	}
	
	/**
	 *  
	 * @param firstTestSuite
	 * @return
	 */
	private Double getLastActionIndex(TestSuite firstTestSuite) {
		
		Double lastActionIndex = 0.0;
		
		for( TestAction testAction : firstTestSuite.getTestActions() ){

			Double actionIndex = testAction.getIndex();
			
			if ( lastActionIndex < actionIndex )
				lastActionIndex = testAction.getIndex(); 
		}
		
		return lastActionIndex;
	}


	/**
	 * @param testSuite
	 * @param sheet
	 * @return
	 * @throws InvalidActionException
	 * @throws InvalidValueException
	 * @throws DuplicateDataKeyException
	 */
	private ArrayList<TestAction> getTestActions(TestSuite testSuite,
			Sheet sheet) throws InvalidActionException, InvalidValueException,
			DuplicateDataKeyException {
		
		// read only testActions from Xls and maintain it in list
		ArrayList<TestAction> testActions = parseTestActions(sheet, testSuite);

		testActions = assignTestAction(sheet, testActions);
		
		return testActions;
	}
	
	/**
	 * 
	 * @param sheet
	 * @param testSuite
	 * @return
	 */
	private ArrayList<TestAction> parseTestActions(Sheet sheet, TestSuite testSuite) {
		
		Collection<String> testActionNameColls = new ArrayList<>();
		
		for ( Row row : sheet){
		  	
			Cell firstCell = null;
			
			if(!ExcelUtil.isCellBlank(row.getCell(0))){
				
				firstCell = row.getCell(0);
				
				if ( ( row.getRowNum() > 0 ) && (firstCell.getCellType() == Cell.CELL_TYPE_STRING ) ){
					
					testActionNameColls.add(firstCell.getStringCellValue().trim());
				}
			}
		}
		
		ArrayList<TestAction> testActions = parseTestActionRows(sheet,testSuite,
				testActionNameColls);

		return testActions;
	}

	private ArrayList<TestAction> assignTestAction(Sheet sheet,
			ArrayList<TestAction> testActions) throws InvalidActionException,
			InvalidValueException, DuplicateDataKeyException {

		if (testActions != null) {
			for (TestAction testAction : testActions) {
				if (testAction != null) {
					assignTestActionValues(testAction, sheet);
				}
			}
		}
		
		return testActions;

	}

	private TestAction assignTestActionValues(TestAction testAction, Sheet sheet)
			throws InvalidActionException, InvalidValueException,
			DuplicateDataKeyException {
		if (testAction != null) {
			if (sheet != null) {

				testAction = assignIndex(testAction, sheet);
				testAction = assignActionElements(testAction, sheet);
				testAction = assignTestData(testAction, sheet);
				testAction = assignIsExecute(testAction, sheet);
				
				testAction = assignTestGroup(testAction, sheet);
				
			}
		}

		return testAction;
	}

	private TestAction assignIsExecute(TestAction testAction, Sheet sheet)
			throws InvalidValueException {
		
		Cell executeCell = ExcelUtil.findCell(sheet, testAction.getStartRow(), testAction.getEndRow(), XLFormat.TS_TEST_RUN.getIndex());
		
		if(!ExcelUtil.isCellBlank(executeCell)){
			
			String execute = executeCell.getStringCellValue();

			if (execute.equalsIgnoreCase( KDConstants.EXECUTE.getValue())) {
				testAction.setExecute(true);
			} else if (execute.equalsIgnoreCase( KDConstants.DONT_EXECUTE.getValue())) {
				testAction.setExecute(false);
			} else {
				throw new InvalidValueException();
			}
		}
		
		return testAction;
	}

	private TestAction assignTestGroup(TestAction testAction, Sheet sheet) {
		
		Cell testGroupCell = ExcelUtil.findCell(sheet, testAction.getStartRow(), testAction.getEndRow(), XLFormat.TS_TEST_GROUP_COLUMN.getIndex());
		
		if(!ExcelUtil.isCellBlank(testGroupCell)){
			
			String testGroups = testGroupCell.getStringCellValue();

			StringTokenizer stk = new StringTokenizer(testGroups, ",");
			LinkedList<String> testGroupList = new LinkedList<>();

			while (stk.hasMoreTokens()) {
				testGroupList.add(stk.nextToken());
			}
			
			testAction.setTestGroup(testGroupList);
		}
		
		return testAction;

	}

	/**
	 * Parse test data separately and assigned it to Dataset
	 * 
	 * @param testAction
	 * @param sheet
	 * @return
	 * @throws DuplicateDataKeyException
	 */
	private TestAction assignTestData(TestAction testAction, Sheet sheet)
			throws DuplicateDataKeyException {
		
		Cell testDataLabelCell = ExcelUtil.findCell(sheet, testAction.getStartRow(), testAction.getEndRow(),  XLFormat.TS_TEST_DATA_COLUMN.getIndex());
		
		if(!ExcelUtil.isCellBlank(testDataLabelCell)){
			
			String testData = testDataLabelCell.getStringCellValue();

			testAction.setKdDataSetColls(readDataset.getDataSet(wb, testData, sheet.getSheetName()));
		}
		
		return testAction;
	}

	/**
	 * Assign ActionElement to TestAction
	 * 
	 * @param testAction
	 * @param sheet
	 * @return
	 * @throws InvalidActionException
	 */
	private TestAction assignActionElements(TestAction testAction, Sheet sheet)
			throws InvalidActionException {

		LinkedList<ActionElement> actionElements = new LinkedList<>();

		for (int rowIndex = testAction.getStartRow(); rowIndex <= testAction.getEndRow(); rowIndex++) {

			ActionElement actionElement = new ActionElement();
			Row row = sheet.getRow(rowIndex);

			if( !ExcelUtil.isCellBlank(row.getCell( XLFormat.TS_ELEMENT_NAME_COLUMN.getIndex() )))
				actionElement.setName(row.getCell(XLFormat.TS_ELEMENT_NAME_COLUMN.getIndex()).getStringCellValue());
			
			if( !ExcelUtil.isCellBlank(row.getCell( XLFormat.TS_ELEMENT_ID_KEY_COLUMN.getIndex())))
				actionElement.setIdType(row.getCell(XLFormat.TS_ELEMENT_ID_KEY_COLUMN.getIndex()).getStringCellValue());
			
			if( !ExcelUtil.isCellBlank(row.getCell( XLFormat.TS_ELEMENT_ID_VALUE_COLUMN.getIndex())))
				actionElement.setValue(row.getCell( XLFormat.TS_ELEMENT_ID_VALUE_COLUMN.getIndex()).getStringCellValue());
			
			if( !ExcelUtil.isCellBlank(row.getCell( XLFormat.TS_ACTION_COLUMN.getIndex())))
				actionElement.setKeyword(new Keyword(row.getCell( XLFormat.TS_ACTION_COLUMN.getIndex()).getStringCellValue()));

			if (StringUtils.isNotBlank(actionElement.getName())
					&& StringUtils.isNotBlank(actionElement.getIdType())
					&& StringUtils.isNotBlank(actionElement.getValue())
					&& StringUtils.isNotBlank(actionElement.getKeyword().getAction()))
				actionElements.add(actionElement);

		}
		
		testAction.setActionElements(actionElements);
		
		actionElementMap.put(testAction.getActionName(), actionElements);
		
		return testAction;

	}

	private void getReferenceActions(TestAction testAction, Sheet sheet, int collIndex){
		
		Cell actionCell = ExcelUtil.findCell(sheet, testAction.getStartRow(), testAction.getEndRow(), collIndex);
		
		if(!ExcelUtil.isCellBlank(actionCell)){
			
			String refActionNames = actionCell.getStringCellValue();

			StringTokenizer stk = new StringTokenizer(refActionNames, ",");

			LinkedList<ActionElement> newActionElementLists = new LinkedList<>();
			
			LinkedList<ActionElement> refActionElementList = new LinkedList<>();
			
			while (stk.hasMoreTokens()) {
				String refActionName = stk.nextToken();
				refActionElementList.addAll( actionElementMap.get(refActionName.trim()));
			}
			
			LinkedList<ActionElement> actionElementLists = testAction.getActionElements(); 
			
			if( collIndex == XLFormat.TS_PRE_ACTION_COLUMN.getIndex()){
				newActionElementLists.addAll(refActionElementList);
				newActionElementLists.addAll(actionElementLists);
			}
			else if( collIndex == XLFormat.TS_POST_ACTION_COLUMN.getIndex()) {
				newActionElementLists.addAll(actionElementLists);
				newActionElementLists.addAll(refActionElementList);
			}
			
			testAction.setActionElements(newActionElementLists);

		}
		
	}

	/**
	 * 
	 * @param testAction
	 * @param sheet
	 * @return
	 */
	private TestAction assignIndex(TestAction testAction, Sheet sheet) {

		Cell indexCell = ExcelUtil.findCell(sheet, testAction.getStartRow(), testAction.getEndRow(), XLFormat.TS_INDEX_COLUMN.getIndex());
		
		if(!ExcelUtil.isCellBlank(indexCell)){
			Double index = (double) indexCell.getNumericCellValue();
			testAction.setIndex(index);
		}

		return testAction;

	}

	/**
	 * 
	 * @param sheet
	 * @param testSuite
	 * @param testActionNameColls
	 * @return
	 */
	private ArrayList<TestAction> parseTestActionRows(Sheet sheet, TestSuite testSuite, 
			Collection<String> testActionNameColls) {

		ArrayList<TestAction> testActions = new ArrayList<>();
		
		for (String testAction : testActionNameColls){
			
			TestAction action = new TestAction();
			
			action.setSuite(testSuite);
			action.setActionName(testAction);
			
			Cell[] cell = ExcelUtil.findCell(sheet, testAction, XLFormat.TS_START_COLUMN.getIndex(), XLFormat.TS_END_COLUMN.getIndex() );
			action.setStartRow(cell[0].getRowIndex());
			action.setEndRow(cell[1].getRowIndex());
			
			testActions.add(action);
			
		}
		
		return testActions;

	}
	
	/**
	  * Main method to parse the Excel File
	  * @return
	  */
	public ExcelValidator validateExcelFileContent() {
		return validateExcelFileContent(wb,numSheets);
	
	}
	
	/**
	  * Main method to parse the Excel File
	  * @return
	  */
	public ExcelValidator validateExcelFileContent(Workbook wb, int numSheets) {
	    ExcelValidator excelValidator = new ExcelValidator(new ExcelContent(wb, numSheets));
		excelValidator.validateExcelFileContent();
	    return excelValidator;
	
	}
}