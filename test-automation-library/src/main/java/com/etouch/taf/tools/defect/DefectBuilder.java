package com.etouch.taf.tools.defect;

import java.net.URL;
import java.util.Date;
import java.util.LinkedList;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;

import com.etouch.taf.core.TestActionExecutor;
import com.etouch.taf.core.resources.KDConstants;
import com.etouch.taf.kd.config.ActionElement;
import com.etouch.taf.kd.config.AssertKeywordLibrary;
import com.etouch.taf.util.CommonUtil;
import com.etouch.taf.util.LogUtil;

/**
 * @author etouch
 *
 */
public class DefectBuilder {

	private static Log log = LogUtil.getLog(DefectBuilder.class);
	
	private String defectTitle;
	
	private StringBuilder defectDescription;
	
	private StringBuilder defectNotes;
	
	private URL attachments;
	
	private TestActionExecutor testActionExecutor;
	
	private String defectPriority;
	
	private String defectStatus;
	
	private String defectResolution;
	
	/**
	 * 
	 * @param testActionExecutor
	 */
	public DefectBuilder(TestActionExecutor testActionExecutor) {
		this.testActionExecutor = testActionExecutor; 
		
		//Creating defect title
		buidDefectTitle();
	}

	/**
	 * @return the defectTitle
	 */
	public String getDefectTitle() {
		return defectTitle.trim();
	}

	/**
	 * @param defectTitle the defectTitle to set
	 */
	public void setDefectTitle(String defectTitle) {
		this.defectTitle = defectTitle;
	}

	/**
	 * @return the defectDescription
	 */
	public StringBuilder getDefectDescription() {
		return defectDescription;
	}

	/**
	 * @param defectDescription the defectDescription to set
	 */
	public void setDefectDescription(StringBuilder defectDescription) {
		this.defectDescription = defectDescription;
	}

	/**
	 * @return the defectNotes
	 */
	public StringBuilder getDefectNotes() {
		return defectNotes;
	}

	/**
	 * @param defectNotes the defectNotes to set
	 */
	public void setDefectNotes(StringBuilder defectNotes) {
		this.defectNotes = defectNotes;
	}

	/**
	 * @return the attachments
	 */
	public URL getAttachments() {
		return attachments;
	}

	/**
	 * @param attachments the attachments to set
	 */
	public void setAttachments(URL attachments) {
		this.attachments = attachments;
	}

	/**
	 * @return the testActionExecutor
	 */
	public TestActionExecutor getTestActionExecutor() {
		return testActionExecutor;
	}

	/**
	 * @param testActionExecutor the testActionExecutor to set
	 */
	public void setTestActionExecutor(TestActionExecutor testActionExecutor) {
		this.testActionExecutor = testActionExecutor;
	}

	/**
	 * @return the defectPriority
	 */
	public String getDefectPriority() {
		return defectPriority;
	}

	/**
	 * @param defectPriority the defectPriority to set
	 */
	public void setDefectPriority(String defectPriority) {
		this.defectPriority = defectPriority;
	}

	/**
	 * @return the defectStatus
	 */
	public String getDefectStatus() {
		return defectStatus;
	}

	/**
	 * @param defectStatus the defectStatus to set
	 */
	public void setDefectStatus(String defectStatus) {
		this.defectStatus = defectStatus;
	}

	/**
	 * @return the defectResolution
	 */
	public String getDefectResolution() {
		return defectResolution;
	}

	/**
	 * @param defectResolution the defectResolution to set
	 */
	public void setDefectResolution(String defectResolution) {
		this.defectResolution = defectResolution;
	}

	public void buildDefect(Throwable exception) {
		
		//creating description
		buildDefectDecription(exception);
		
		//setting priority
		setDefectPriority("Medium");
		
	}

	/**
	 * Build defect title in the format Test_Bed_Name - Test_Suite_Name Test_Action_Name - Data_Label_Name
	 */
	private void buidDefectTitle() {
		String testSuite = testActionExecutor.getTestSuiteName();
		String testBed = testActionExecutor.getTestBedName();
		String testAction = testActionExecutor.getTestAction().getActionName();

		String defectName = ""+testBed+" - "+testSuite+": "+testAction+" "+getDataLabel();

		this.setDefectTitle(defectName);
	}

	/**
	 * @param dataLabel
	 * @return
	 */
	private String getDataLabel() {
		String dataLabel = "";
		
		if( testActionExecutor.getKdDataSet() != null ){
			
			Map<String, Object> kdDataMap = testActionExecutor.getKdDataSet().getDataSetMap();
			
			if( ( kdDataMap != null ) && ( kdDataMap.size() > 0 ))  {
				
				if(kdDataMap.containsKey(KDConstants.TD_LABEL.getValue()) && StringUtils.isNotBlank(kdDataMap.get(KDConstants.TD_LABEL.getValue()).toString()) ){
					dataLabel = "- "+kdDataMap.get(KDConstants.TD_LABEL.getValue()).toString();
				}
			}
		}
		
		return dataLabel;
	}

	/**
	 * @param exception
	 */
	private void buildDefectDecription(Throwable exception) {
		
		LinkedList<ActionElement> actionElements = testActionExecutor.getTestAction().getActionElements();
		
		StringBuilder description = new StringBuilder();
		
		description.append("Step to follow: \n\n");
		
		for(ActionElement actionElement : actionElements){
			
			buildTestActionSteps(actionElements.indexOf(actionElement), actionElements, description, actionElement);
		}
		
		description.append("\n Expected: "+testActionExecutor.getTestAction().getActionName()+" should pass \n");
		
		description.append("\n Actual: \n");
		
		description.append(CommonUtil.getErrorDetails(exception)+" \n\n");
		
		setDefectDescription(description);
		
	}

	/**
	 * @param index 
	 * @param actionElements
	 * @param description
	 * @param actionElement
	 */
	private void buildTestActionSteps(int index, LinkedList<ActionElement> actionElements,
			StringBuilder description, ActionElement actionElement) {
		
		description.append( (index + 1 )+ " - ");
		
		String action = actionElement.getKeyword().getAction();
		
		if( AssertKeywordLibrary.contains(action)) {
			
			String assertValue = String.valueOf( testActionExecutor.getKdDataSet().getDataSetMap().get(actionElement.getName()).toString());
			description.append("Verify "+actionElement.getIdType()+" "+action+" "+assertValue+"\n");
		}
		else {
			description.append ( action + " "+actionElement.getValue() );
			
			if( testActionExecutor.getKdDataSet().getDataSetMap().containsKey(actionElement.getName())  ){
				String dataValue = String.valueOf( testActionExecutor.getKdDataSet().getDataSetMap().get(actionElement.getName()).toString());
				description.append(" = "+dataValue+" \n");
			}
			else {
				description.append("\n");
			}
		}
	}
	
	
	public void buildDefectNotes(String defectWorkflowType, Throwable throwable) {
		
		StringBuilder defectNotes = new StringBuilder();
		
		if( defectWorkflowType.equalsIgnoreCase(KDConstants.UPDATE_DEFECT.getValue())){
			defectNotes.append("Defect still existing as on "+ new Date());
		}
		else if( defectWorkflowType.equalsIgnoreCase(KDConstants.CLOSE_DEFECT.getValue())){
			defectNotes.append("Defect resolved. Closing on "+ new Date());
		}
		else if( defectWorkflowType.equalsIgnoreCase(KDConstants.REOPEN_DEFECT.getValue())){
			defectNotes.append("Defect reoccured on "+new Date()+" : Reopening");
		}
		
		if(throwable != null){
			defectNotes.append("\n Issue observed this time ==>"+CommonUtil.getErrorDetails(throwable));	
		}
		
		setDefectNotes(defectNotes);
	}

	
	
}
