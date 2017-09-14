/**
 * 
 */
package com.etouch.taf.tools.jira;

/**
 * @author etouch
 *
 */
public enum JiraConstants {

	SUMMARY("summary"),
	PROJECT("project"),
	ISSUE_TYPE("issueType"),
	BUG("Bug"),
	LIKE(" ~ "),
	AND(" AND "),
	EQUAL(" = "),
	BUG_TYPE_ID("1"),
	
	// 	Jira Transition Map	
    //-- Code overwritten and added specfic for Splunk Only @ 02-25-2016
	CLOSE("Close issue"),
	NEW("Triage"),
	RESOLVE("Resolve issue"),
	UNRESOLVE("Put in progress"),
	NOT_COMPLETE("Not Complete"),
	//-- End of changes
	
	REOPEN("Reopen"),
	UPDATE("Start Progress"),
	REOPEN_START_PROGRESS("Reopen and start progress");
	
	
	/**
	 * @param value
	 */
	private JiraConstants(String value) {
		this.value = value;
	}

	private String value;

	/**
	 * @return the value
	 */
	public String getValue() {
		return value;
	}
	
	
}
