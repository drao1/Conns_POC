package com.etouch.taf.tools.jira;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import com.etouch.taf.core.TestBedManager;
import com.etouch.taf.core.datamanager.excel.TestParameters;
import com.etouch.taf.core.exception.DefectException;
import com.etouch.taf.core.resources.DefectParameters.IDefect;
import com.etouch.taf.tools.defect.IDefectManager;
import com.etouch.taf.util.CommonUtil;
import com.etouch.taf.util.LogUtil;
import com.etouch.taf.util.TestUtil;

/**
 * Jira Integration //added by sonam and refactored by Malathi //.
 * 
 * @author eTouch Systems Corporation
 * @version 1.0
 */

public class JiraForScripting implements IDefectManager {

	/** The log. */
	private static Log log = LogUtil.getLog(JiraForScripting.class);

	/** The jira info. */
	private JiraInfo jiraInfo;
	private String responseData;
	private HttpResponse httpResponse;
	private HttpEntity entitydata;
	private HttpPost httpPost;

	// constructor for Jira class
	/**
	 * Instantiates a new jira.
	 * 
	 * @param jiraInfo
	 *            the jira info
	 * @throws DefectException
	 *             the defect exception
	 */
	public JiraForScripting(JiraInfo jiraInfo) throws DefectException {
		this.jiraInfo = jiraInfo;

	}

	/**
	 * Sets the jira info.
	 * 
	 * @param jiraInfo
	 *            the new jira info
	 */
	public void setJiraInfo(JiraInfo jiraInfo) {
		this.jiraInfo = jiraInfo;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.etouch.taf.tools.defect.IDefectManager#createAJiraDefectBuilder(java.
	 * lang.String, java.lang.String, java.lang.String, java.lang.String,
	 * java.lang.String)
	 */
	@Override
	public void createAJiraDefectBuilder(TestParameters inputs, String errMsg, String issueUrl, String username, String password, String keys, String fullAttachmentPath) {
		try {

			Map<String, String> reqMap = new HashMap<>();
			reqMap.put(JiraConstantsForScripting.CREATE_ISSUE_URL, issueUrl);
			reqMap.put(JiraConstantsForScripting.USERNAME, username);
			reqMap.put(JiraConstantsForScripting.PASSWORD, password);
			reqMap.put(JiraConstantsForScripting.KEY, keys);

			log.info("done with building the defect builder");

			try {

				createIssueWithAttachment(inputs, errMsg, issueUrl, username, password, keys, fullAttachmentPath);

			} catch (Exception e) {

				log.debug(e);
			}

		} catch (NullPointerException e) {
			log.error(e.getMessage());
		}
	}

	/**
	 * Creates the issue.
	 * 
	 * @param inputs
	 *            the inputs
	 * @param errMsg
	 *            the err msg
	 * @param issueUrl
	 *            the issue url
	 * @param username
	 *            the username
	 * @param password
	 *            the password
	 * @param keys
	 *            the keys
	 * @param fullAttachmentPath
	 *            the full attachment path
	 */
	void createIssueWithAttachment(TestParameters inputs, String errMsg, String issueUrl, String username, String password, String keys, String fullAttachmentPath) {

		// Creates CloseableHttpClient instance with default configuration.
		CloseableHttpClient closeableHttpClient = HttpClients.createDefault();

		// creates an issue, Post it and get the response
		createIssue(inputs, errMsg, issueUrl, username, password, closeableHttpClient);

		// add the attachment to the above created issue
		addAttachment(username, password, fullAttachmentPath, closeableHttpClient);

		// Closes the stream and releases any system resources associated with
		// it
		try {
			closeableHttpClient.close();
		} catch (IOException e) {

			log.debug("IOException", e);
		}
	}

	private void createIssue(TestParameters inputs, String errMsg, String issueUrl, String username, String password, CloseableHttpClient closeableHttpClient) {

		// creating a json object of defect info.
		String jsonObj = createJsonObject(inputs, errMsg);
		CommonUtil.sop(jsonObj);

		// Create the issue POST request
		createIssuePostRequest(issueUrl, jsonObj);

		// httpPost authentication
		getAuthentication(username, password);

		// Execute the post request and get the response
		executePostAndGetResponse(closeableHttpClient);

	}

	private void addAttachment(String username, String password, String fullAttachmentPath, CloseableHttpClient closeableHttpClient) {

		// gets the url for the attachment field (of posted issue)
		String issueAttachmentUrl = getIssueAttachmentUrl();

		// Creates the attachment POST request
		createAttachmentPostRequest(fullAttachmentPath, issueAttachmentUrl);

		// httpPost authentication
		getAuthentication(username, password);

		// Execute the post request and get the response
		executePostAndGetResponse(closeableHttpClient);
	}

	private void createIssuePostRequest(String issueUrl, String jsonObj) {
		httpPost = new HttpPost(issueUrl);
		StringEntity entity;
		try {
			entity = new StringEntity(jsonObj);
			entity.setContentType("application/json");
			httpPost.setEntity(entity);

		} catch (UnsupportedEncodingException e1) {

			log.debug("UnsupportedEncodingException", e1);
		}
	}

	private void executePostAndGetResponse(CloseableHttpClient closeableHttpClient) {
		try {
			httpResponse = closeableHttpClient.execute(httpPost);
			entitydata = httpResponse.getEntity();
			responseData = new String(EntityUtils.toByteArray(entitydata));
			log.info("Response data: " + responseData);

		} catch (ClientProtocolException e) {
			log.debug("ClientProtocolException", e);
		} catch (IOException e) {
			log.debug("IOException", e);
		}
	}

	private void createAttachmentPostRequest(String fullAttachmentPath, String issueAttachmentUrl) {

		httpPost = new HttpPost(issueAttachmentUrl);

		// building multipart entity
		File fileUpload = new File(fullAttachmentPath);
		MultipartEntityBuilder mpeBuilder = MultipartEntityBuilder.create();
		mpeBuilder.addPart("file", new FileBody(fileUpload));
		HttpEntity entity1 = mpeBuilder.build();

		httpPost.setEntity(entity1);
	}

	private String getIssueAttachmentUrl() {

		// get the current issue url from the response data of the posted issue
		int firstIndex = responseData.indexOf("https://");
		int lastIndex = responseData.indexOf("}");
		String updatedIssueUrl = responseData.substring(firstIndex, (lastIndex - 1));
		log.info("Current issue url: " + updatedIssueUrl);

		// url for attachments field
		String issueAttachmentUrl = updatedIssueUrl + "/" + "attachments";

		return issueAttachmentUrl;
	}

	private void getAuthentication(String username, String password) {
		String auth1 = new String(org.apache.commons.codec.binary.Base64.encodeBase64((username + ":" + password).getBytes()));
		httpPost.setHeader("X-Atlassian-Token", "nocheck");
		httpPost.setHeader("Authorization", "Basic " + auth1);
	}

	private String createJsonObject(TestParameters inputs, String errMsg) {
		String jsonObj = "{" + "\"fields\": {" + "\"project\":" + "{" + "\"key\": \"" + TestBedManager.INSTANCE.getDefectConfig().getKeys().trim() + "\"" + "},"
				+ "\"summary\": \"" + inputs.getParamMap().get("TestcaseName").trim() + "\"," + "\"description\": \"" + TestUtil.formatJsonString(errMsg).trim() + "\"" + ","
				+ "\"issuetype\": {" + "\"name\": \"Bug\"" + "}," + "\"" + TestBedManager.INSTANCE.getDefectConfig().getJiraSeverityField() + "\":" + "{" + "\"value\": \""
				+ inputs.getParamMap().get("JiraSeverity").trim() + "\"" + "}," + "\"" + TestBedManager.INSTANCE.getDefectConfig().getJiraPriorityField() + "\":" + "{"
				+ "\"name\": \"" + inputs.getParamMap().get("JiraPriority").trim() + "\"" + "}," + "\"" + TestBedManager.INSTANCE.getDefectConfig().getJiraEnvField() + "\":" + "["
				+ "{" + "\"value\": \"" + TestBedManager.INSTANCE.getDefectConfig().getEnvironment().trim() + "\"" + "}" + "]" + "}" + "}";
		return jsonObj;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.etouch.taf.tools.defect.IDefectManager#createDefectBuilder(java.lang.
	 * String, java.lang.String, java.lang.String, java.lang.String,
	 * java.lang.String, java.lang.String, java.lang.String)
	 */
	/**
	 * Creates the defect builder.
	 * 
	 * @param defName
	 *            the def name
	 * @param testcaseId
	 *            the testcase id
	 * @param projId
	 *            the proj id
	 * @param DefSeverity
	 *            the def severity
	 * @param DefOwner
	 *            the def owner
	 * @param DefNotes
	 *            the def notes
	 * @param storyId
	 *            the story id
	 */
	public void createDefectBuilder(String defName, String testcaseId, String projId, String defSeverity, String defOwner, String defNotes, String storyId) {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.etouch.taf.tools.defect.IDefectManager#verifyDefectExists(java.lang.
	 * String, java.lang.String, java.lang.String, java.lang.String)
	 */
	public boolean verifyDefectExists(String testcaseId, String projId, String storyId, String defName) {
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.etouch.taf.tools.defect.IDefectManager#verifyIfdefectClosed()
	 */
	public boolean verifyIfdefectClosed() {

		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.etouch.taf.tools.defect.IDefectManager#reopenDefect()
	 */
	public void reopenDefect() {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.etouch.taf.tools.defect.IDefectManager#getDefectStatus()
	 */
	public String getDefectStatus() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.etouch.taf.tools.defect.IDefectManager#queryDefectID(java.lang.
	 * String, java.lang.String, java.lang.String, java.lang.String)
	 */
	public String queryDefectID(String testcaseId, String storyId, String projId, String defName) {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.etouch.taf.tools.defect.IDefectManager#queryDefectStatus(java.lang.
	 * String, java.lang.String, java.lang.String, java.lang.String)
	 */
	public String queryDefectStatus(String testcaseId, String storyId, String projId, String defName) {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.etouch.taf.tools.defect.IDefectManager#updateDefect(java.lang.String,
	 * java.lang.String, java.lang.String, java.lang.String,
	 * com.etouch.taf.core.resources.DefectParameters.IDefect, java.lang.String)
	 */
	public boolean updateDefect(String testcaseId, String projId, String storyId, String defName, IDefect updateKey, String updateValue) {
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.etouch.taf.tools.defect.IDefectManager#queryDefect(java.lang.String,
	 * java.lang.String, java.lang.String, java.lang.String,
	 * com.etouch.taf.core.resources.DefectParameters.IDefect)
	 */
	public String queryDefect(String testcaseId, String storyId, String projId, String defName, IDefect queryByParam) {
		return null;
	}

	// below method added for workspace support
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.etouch.taf.tools.defect.IDefectManager#createDefectBuilder(java.lang.
	 * String, java.lang.String, java.lang.String, java.lang.String,
	 * java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	public void createDefectBuilder(String defName, String workspaceId, String testcaseId, String projId, String defSeverity, String defOwner, String defNotes, String storyId,
			String attachmentPath) {
	}

	// updated for testcase result

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.etouch.taf.tools.defect.IDefectManager#updateTestCaseResult(java.lang
	 * .String, java.lang.String, java.lang.String, java.lang.String,
	 * java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	public void updateTestCaseResult(String defName, String testcaseId, String workspaceId, String projId, String defSeverity, String defOwner, String defNotes, String storyId) {
	}

}
