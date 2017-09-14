/**
 * 
 */
package com.etouch.taf.tools.jira.test;


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

/**
 * @author etouch
 *
 */
public class JiraTest {
	
	private static Log log = LogUtil.getLog(JiraTest.class);

	private DefectTool defectTool; 
	
	/**
	 * @throws Exception 
	 * 
	 */
	public JiraTest() throws Exception {
		TafTestUtil.initialize();
		
		defectTool = getDefectTool();
		defectTool.openConnection();
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
	
//	@Test
	public void OpenJiraConnectionTest() throws Exception {
		log.debug("******************  Start: OpenJiraConnectionTest  *******************************************");
		
		defectTool.openConnection();
		
		log.debug("********************** End: OpenJiraConnectionTest *****************************************");
	}
	
//	@Test
	public void AspectExceptionHandlingTest() {
		log.debug("**************************START: AspectExceptionHandlingTest********************************************************");
		
		if(TestBedManager.INSTANCE.getKdConfig().isExecute()){
			try {
				TestBedManager.INSTANCE.executeKDTestNG();
				
				String defectToolName = TestBedManagerConfiguration.INSTANCE.getDefectManagementTool();
				
				DefectTool defectTool = null;
				
				if(DefectToolType.isSupported(defectToolName)){
					defectTool = DefectToolType.getDefectTool(defectToolName);
				}
				else {
					throw new Exception("Defect Tool not supported");
				}
				
				
			} catch (ClassNotFoundException | InstantiationException
					| IllegalAccessException | InvalidFormatException
					| IOException | InvalidActionException
					| InvalidValueException | DuplicateDataKeyException e) {

				log.error(e.getMessage());
				e.printStackTrace();
			} catch (Exception e) {
				
				e.printStackTrace();
			}
		}
		
		log.debug("*****************************End: AspectExceptionHandlingTest*****************************************************");
	}
	
	@Test()
	public void JQLSearchTest() {
		log.debug("****************************START: JQLSearchTest ********************************************************");
		
		String defectTitle = "Firefox - Test1: validateLoginPage - ValidLogin7";
		
		try {
			
			boolean defectStatus = defectTool.isDefectLogged(defectTitle);
			
			log.debug("defectStatus ==>"+defectStatus);
			
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		
		log.debug("******************************END: JQLSearchTest ******************************************************");
	}
	
}
