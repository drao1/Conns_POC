package com.etouch.taf.core;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Observable;

import org.apache.commons.logging.Log;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.testng.TestNG;
import org.testng.internal.Utils;

import com.etouch.taf.kd.config.ReadExcel;
import com.etouch.taf.kd.config.TestSuite;
import com.etouch.taf.kd.exception.DuplicateDataKeyException;
import com.etouch.taf.kd.exception.InvalidActionException;
import com.etouch.taf.kd.exception.InvalidValueException;
import com.etouch.taf.kd.validator.ExcelValidator;
import com.etouch.taf.util.FileUtils;
import com.etouch.taf.util.LogUtil;
import com.etouch.taf.util.report.KDTestNgReporter;

/**
 * <p><h2>A singleton class for controlling the workflow of KD Test case execution.</h2></p>
 * <p>
 *  Following objectives are achieved
 *  <ol>
 *  <li>Excel file is validated</li>
 *  <li>If there is no error then complete Excel file is read and TestSuite object with its object graph is created.</li>
 *  <li>In case of Error in the Format / presence of Junk char / Unmatched or incomplete DataSet and/or TestSuites - The error messages are consolidated and published in the KD Custom Report.</li> 
 * </p>
 * 
 *  @author eTouch Systems Corporation
 *
 */
public class KDManager extends Observable{
	
	private static Log log = LogUtil.getLog(KDManager.class);
	
	protected static KDManager INSTANCE;
	
	private Collection<TestSuite> testSuites= new ArrayList<>();
	
	private boolean isTestSuiteAvailable = false;
	
	/**
	 * 
	 * The Object of this class register the KDTestNgReporter as a listener.
	 * Everytime there is a error in validating the excel sheets, the errors are consolidated and published the in KD custom report.
	 * 
	 * Reading / Validating Excel file happens at the constructor.
	 * 
	 * @throws IOException 
	 * @throws InvalidFormatException 
	 * @throws DuplicateDataKeyException 
	 * @throws InvalidValueException 
	 * @throws InvalidActionException 
	 * 
	 */
	public KDManager() throws InvalidFormatException, IOException,
			InvalidActionException, InvalidValueException,
			DuplicateDataKeyException {
		
		log.debug("Reading excel sheet...");
		ReadExcel readExcel=new ReadExcel(TestBedManager.INSTANCE.getKdConfig().getFilePath());
	
        log.debug("Validating excel format...");
        ExcelValidator excelValidator = readExcel.validateExcelFileContent();
		
		KDTestNgReporter kdReport=new KDTestNgReporter();
		
		if (!excelValidator.getIsValid()) {
			
			log.debug("Registering Custom Report object");
			this.addObserver(kdReport);
			
			Map<String,List<String>> statusMessageMap=excelValidator.getStatusMessages();
			setChanged();
			notifyObservers(statusMessageMap);
			
			isTestSuiteAvailable = false;
  	   }
		else {
			log.debug("Clearing custom report for TestNg");
			this.deleteObserver(kdReport);
			//Clean the existing report
			cleanReport();
			
			testSuites=readExcel.readFile();
			isTestSuiteAvailable = true;
	    }
	}

	
	protected void cleanReport(){
		
		log.debug("deleting existing custom report for TestNg");
		TestNG tst=new TestNG();
		String outputDir=tst.getOutputDirectory();
		FileUtils.cleanFile(outputDir,  "KDTestNgCustomReport.html");
		
	}
	

	/**
	 * @param readExcel
	 * @param excelValidator
	 * @throws InvalidFormatException
	 * @throws IOException
	 * @throws InvalidActionException
	 * @throws InvalidValueException
	 * @throws DuplicateDataKeyException
	 */
	public boolean isTestSuitesAvailable() {
		return isTestSuiteAvailable;
	}
	
	/**
	 * 
	 * @return Singleton object of KDManager
	 * 
	 * @throws InvalidFormatException
	 * @throws IOException
	 * @throws InvalidActionException
	 * @throws InvalidValueException
	 * @throws DuplicateDataKeyException
	 */
	public static KDManager getInstance() throws InvalidFormatException, IOException, InvalidActionException, InvalidValueException, DuplicateDataKeyException{
		 if(INSTANCE == null){
			 INSTANCE= new KDManager();
		 }
		 	 return INSTANCE;
	 }
	
	
	/**
	 * @return the collection of TestSuites object
	 */
	public Collection<TestSuite> getTestSuites() {
		return testSuites;
	}

	/**
	 * 
	 * @return TestNG object configured with TestCases to be executed as the configuration setting.
	 * 
	 * @throws ClassNotFoundException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws InvalidFormatException
	 * @throws IOException
	 * @throws InvalidActionException
	 * @throws InvalidValueException
	 * @throws DuplicateDataKeyException
	 */
	public TestNG buildKDTestSuites() throws ClassNotFoundException, InstantiationException, IllegalAccessException, InvalidFormatException, IOException, InvalidActionException, InvalidValueException, DuplicateDataKeyException  {
		
		 KDTestSuiteManager kdTestSuiteManager = new KDTestSuiteManager() ;
 		 return kdTestSuiteManager.getTestNGSuite(testSuites);
	 }
	
}
