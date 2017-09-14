package com.etouch.taf.core.config;

import com.etouch.taf.util.TafPassword;


/**
 * The Class DefectConfig.
 */
public class DefectConfig {

	/** The project id. */
	private String projectId;
	
	/** The defect owner. */
	private String defectOwner;
	
	/** The defect tool. */
	private String defectTool;
	
	/** The username. */
	private String username;
	
	/** The password. */
	private TafPassword password;
	
	/** The url3. */
	private String url3;
	
	/** The issue url. */
	private String issueUrl;
	
	/** The keys. */
	private String keys;
	
	private String environment;
	
	/** The build. */
	private String build;
	
	/** The workspace id. */
	private String workspaceId;
	
	/** The jira env field. */
	private String jiraEnvField;
	
	/** The jira severity field. */
	private String jiraSeverityField;
	
	/** The jira priority field. */
	private String jiraPriorityField;

	

	public String getJiraEnvField() {
		return jiraEnvField;
	}
	
	public void setJiraEnvField(String jiraEnvField) {
		this.jiraEnvField = jiraEnvField;
	}

	public String getJiraSeverityField() {
		return jiraSeverityField;
	}

	public void setJiraSeverityField(String jiraSeverityField) {
		this.jiraSeverityField = jiraSeverityField;
	}
	
	public String getJiraPriorityField() {
		return jiraPriorityField;
	}

	public void setJiraPriorityField(String jiraPriorityField) {
		this.jiraPriorityField = jiraPriorityField;
	}

	/**
	 * Gets the project id.
	 *
	 * @return the project id
	 */
	public String getProjectId() {
		return projectId;
	}

	/**
	 * Sets the project id.
	 *
	 * @param projectId the new project id
	 */
	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}

	/**
	 * Gets the defect owner.
	 *
	 * @return the defect owner
	 */
	public String getDefectOwner() {
		return defectOwner;
	}

	/**
	 * Sets the defect owner.
	 *
	 * @param defectOwner the new defect owner
	 */
	public void setDefectOwner(String defectOwner) {
		this.defectOwner = defectOwner;
	}

	/**
	 * Gets the defect tool.
	 *
	 * @return the defect tool
	 */
	public String getDefectTool() {
		return defectTool;
	}

	/**
	 * Sets the defect tool.
	 *
	 * @param defectTool the new defect tool
	 */
	public void setDefectTool(String defectTool) {
		this.defectTool = defectTool;
	}
	
	/**
	 * Gets the username.
	 *
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * Sets the username.
	 *
	 * @param username the new username
	 */
	public void setUsername(String username) {
		this.username = username;
	}
	
	
	
	public TafPassword getPassword() {
		return password;
	}

	public void setPassword(TafPassword password) {
		this.password = password;
	}

	/**
	 * Gets the url3.
	 *
	 * @return the url3
	 */
	public String getUrl3() {
		return url3;
	}

	/**
	 * Sets the url3.
	 *
	 * @param url3 the new url3
	 */
	public void setUrl3(String url3) {
		this.url3 = url3;
	}
	
	/**
	 * Gets the issue url.
	 *
	 * @return the issue url
	 */
	public String getIssueUrl() {
		return issueUrl;
	}

	/**
	 * Sets the issue url.
	 *
	 * @param issueUrl the new issue url
	 */
	public void setIssueUrl(String issueUrl) {
		this.issueUrl = issueUrl;
	}
		
	/**
	 * Gets the keys.
	 *
	 * @return the keys
	 */
	public String getKeys() {
			return keys;
		}

	/**
	 * Sets the keys.
	 *
	 * @param keys the new keys
	 */
	public void setKeys(String keys) {
			this.keys = keys;
		}
	
	public String getEnvironment() {
		return environment;
	}

	public void setEnvironment(String environment) {
		this.environment = environment;
	}
	
	/**
	 * Gets the builds the.
	 *
	 * @return the builds the
	 */
	public String getBuild() {
		return build;
	}

	/**
	 * Sets the builds the.
	 *
	 * @param build the new builds the
	 */
	public void setBuild(String build) {
		this.build = build;
	}
	
	/**
	 * Gets the workspace id.
	 *
	 * @return the workspace id
	 */
	public String getWorkspaceId() {
		return workspaceId;
	}

	/**
	 * Sets the workspace id.
	 *
	 * @param workspaceId the new workspace id
	 */
	public void setWorkspaceId(String workspaceId) {
		this.workspaceId = workspaceId;
	}
}