package com.etouch.taf.tools.jira.test;

import java.io.File;
import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.junit.Test;

import com.etouch.taf.core.TestBedManager;
import com.etouch.taf.core.config.TestBedManagerConfiguration;
import com.etouch.taf.core.resources.DefectToolType;
import com.etouch.taf.core.test.util.TafTestUtil;
import com.etouch.taf.kd.exception.DuplicateDataKeyException;
import com.etouch.taf.kd.exception.InvalidActionException;
import com.etouch.taf.kd.exception.InvalidValueException;
import com.etouch.taf.tools.defect.DefectTool;
import com.etouch.taf.util.LogUtil;

public class RallyTest {
	
	private static Log log = LogUtil.getLog(RallyTest.class);
	
	private DefectTool defectTool; 
	
	public RallyTest() throws Exception {
		TafTestUtil.initialize();
		
		defectTool = getDefectTool();
	}

	private DefectTool getDefectTool() throws Exception {

		String defectToolName = TestBedManagerConfiguration.INSTANCE.getDefectManagementTool();
		
		DefectTool defectTool = null;
		
		if(DefectToolType.isSupported(defectToolName)){
			defectTool = DefectToolType.getDefectTool(defectToolName);
		}
		else {
			throw new Exception("Defect Tool not supported");
		}
		
		return defectTool;
	}
	
	
	@Test
	public void rallyOpenConnectionTest(){
		log.debug("************************ START: rallyOpenConnectionTest ************************************");
		
			try {
				
				defectTool.openConnection();
				
			} catch (Exception e) {
				log.error(e.getCause());
				e.printStackTrace();
			}
		
		log.debug("*********************END: rallyOpenConnectionTest ***************************************");
	}
	
//	@Test
	public void rallyCreateDefect(){
		log.debug("***********************START: rallyCreateDefect **********************************************");
		
		if(TestBedManager.INSTANCE.getKdConfig().isExecute()){
			try {
				
				TestBedManager.INSTANCE.executeKDTestNG();
				
			} catch (ClassNotFoundException | InstantiationException
					| IllegalAccessException | InvalidFormatException
					| IOException | InvalidActionException
					| InvalidValueException | DuplicateDataKeyException e) {
				log.error(e.getCause());
				e.printStackTrace();
			}
		}
		
		log.debug("***********************END: rallyCreateDefect **********************************************");
		
	}
	
	@Test
	public void rallyFileAttachment(){
		log.debug("************************ START: rallyDefectLookup **********************************************");
		
		String defectTitle = "Firefox - Test2: scrollPage - scrollPage_data";
		
		try {
			
			boolean isDefectLogged = defectTool.isDefectLogged(defectTitle);
			
			String screenshotPath = TestBedManagerConfiguration.INSTANCE.getVideoConfig().getBaseScreenshotPath();
			
			String fileName = "/Error_Wed_Dec_16_15-27-14_PST.png";
			
			File attachmentFile = new File( screenshotPath+fileName);
			log.debug(" attachmentFile ==>"+attachmentFile);
			
			if(isDefectLogged){
				defectTool.updateDefect(new StringBuilder("Test File Attachment"), attachmentFile);
			}
			
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		
		
		log.debug("****************************END: rallyDefectLookup ******************************************");
	}
	
	@Test 
	public void rallyCloseConnectionTest(){
		log.debug("**********************************START: rallyCloseConnectionTest ******************************************");
		
		try {
			defectTool.closeConnection();
		} catch (Exception e) {
			log.error(e.getCause());
			e.printStackTrace();
		}
		
		log.debug("**********************************END: rallyCloseConnectionTest ******************************************");
		
	}
	
	
}
