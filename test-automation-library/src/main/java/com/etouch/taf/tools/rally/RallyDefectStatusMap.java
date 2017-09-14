/**
 * 
 */
package com.etouch.taf.tools.rally;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.etouch.taf.core.resources.KDConstants;

/**
 * @author etouch
 *
 */
public enum RallyDefectStatusMap {

	SUBMITTED("Submitted", KDConstants.NEW_DEFECT.getValue()),
	OPEN("Open", KDConstants.UPDATE_DEFECT.getValue()),
	FIXED("Fixed", KDConstants.CLOSE_DEFECT.getValue()),
	CLOSED("Closed", KDConstants.CLOSE_DEFECT.getValue());
	
	private final String rallyStatusName;
	
	private final String commonStatusName;
	
	RallyDefectStatusMap(String rallyStatusName, String commonStatusName) {
		this.rallyStatusName = rallyStatusName;
		this.commonStatusName = commonStatusName;
	}

	/**
	 * @return the rallyStatusName
	 */
	public String getRallyStatusName() {
		return rallyStatusName;
	}

	/**
	 * @return the commonStatusName
	 */
	public String getCommonStatusName() {
		return commonStatusName;
	}
	
	private static final Map<String, String> defectStatusMap = Collections.unmodifiableMap(initializeMapping());
	
	private static Map<String, String> initializeMapping() {
		Map<String, String> statusMap = new HashMap<String, String>();
	   
		for (RallyDefectStatusMap  rallyDefectStatusMap : RallyDefectStatusMap.values()){
			statusMap.put(rallyDefectStatusMap.rallyStatusName, rallyDefectStatusMap.commonStatusName);
		}
		
	    return statusMap;
	}
	
	public static String getCommonStatusName(String jiraStatusName){
		
		if (defectStatusMap.containsKey(jiraStatusName))
			return defectStatusMap.get(jiraStatusName);
		
		return null;
		
	}
}
