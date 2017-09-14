package com.etouch.taf.tools.jira;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.concurrent.ExecutionException;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;

import com.atlassian.jira.rest.client.api.IssueRestClient;
import com.atlassian.jira.rest.client.api.JiraRestClient;
import com.atlassian.jira.rest.client.api.JiraRestClientFactory;
import com.atlassian.jira.rest.client.api.domain.BasicIssue;
import com.atlassian.jira.rest.client.api.domain.Comment;
import com.atlassian.jira.rest.client.api.domain.Issue;
import com.atlassian.jira.rest.client.api.domain.SearchResult;
import com.atlassian.jira.rest.client.api.domain.Transition;
import com.atlassian.jira.rest.client.api.domain.input.ComplexIssueInputFieldValue;
import com.atlassian.jira.rest.client.api.domain.input.IssueInput;
import com.atlassian.jira.rest.client.api.domain.input.IssueInputBuilder;
import com.atlassian.jira.rest.client.api.domain.input.TransitionInput;
import com.atlassian.jira.rest.client.internal.async.AsynchronousJiraRestClientFactory;
import com.atlassian.jira.rest.client.internal.async.DisposableHttpClient;
import com.atlassian.util.concurrent.Promise;
import com.etouch.taf.core.TestBedManager;
import com.etouch.taf.core.config.JiraConfig;
import com.etouch.taf.core.config.TestBedManagerConfiguration;
import com.etouch.taf.tools.defect.DefectTool;
import com.etouch.taf.util.LogUtil;
import com.etouch.taf.util.TafPassword;

/**
 * @author etouch
 * 
 */
public class Jira extends DefectTool {

	private static Log log = LogUtil.getLog(Jira.class);

	private JiraConfig jiraConfig;

	private JiraRestClient restClient = null;

	private IssueRestClient issueRestClient = null;

	private DisposableHttpClient httpClient;

	private Issue loggedIssue = null;
	

	public Jira() {
		jiraConfig = TestBedManagerConfiguration.INSTANCE.getJiraConfig();
	}

	@Override
	public void openConnection() throws URISyntaxException {

		final JiraRestClientFactory factory = new AsynchronousJiraRestClientFactory();
		final URI jiraServerUri = new URI(jiraConfig.getUrl());
		TafPassword tfp = jiraConfig.getPassword();
		tfp.decryptPassword(tfp.getEncrypted());

		restClient = factory.createWithBasicHttpAuthentication(jiraServerUri, jiraConfig.getUsername(), tfp.getDecrypted());

		issueRestClient = restClient.getIssueClient();

	}

	@Override
	public void closeConnection() throws Exception {
		restClient.close();
		
	}

	@Override
	public boolean isDefectLogged(String defectTitle) throws InterruptedException, ExecutionException {

		boolean isDefectLogged = false;

		StringBuilder searchQuery = new StringBuilder();

		String project = addParams(JiraConstants.PROJECT.getValue(), JiraConstants.EQUAL.getValue(), jiraConfig.getProjectName());
		searchQuery.append(project.concat(JiraConstants.AND.getValue()));

		String issueType = addParams(JiraConstants.ISSUE_TYPE.getValue(), JiraConstants.EQUAL.getValue(), JiraConstants.BUG.getValue());
		searchQuery.append(issueType.concat(JiraConstants.AND.getValue()));

		String defectSummary = addParams(JiraConstants.SUMMARY.getValue(), JiraConstants.LIKE.getValue(), "\"\\\"" + defectTitle.trim() + "\\\"\"");
		searchQuery.append(defectSummary);

		log.debug("Searching for ==>" + defectTitle + " with query ==>" + searchQuery.toString());
		SearchResult searchResult = restClient.getSearchClient().searchJql(searchQuery.toString()).get();

		if (searchResult.getTotal() > 0) {
			Iterable<Issue> iterableIssue = searchResult.getIssues();

			for (Issue issue : iterableIssue) {

				log.debug("Defect Found for >>" + defectTitle + " with Id ==>" + issue.getKey());

				if (defectTitle.equals(issue.getSummary()) && (!issue.getStatus().getName().equalsIgnoreCase("Closed"))) {
					log.debug("Exact match found with id ==>" + issue.getKey());
					loggedIssue = issue;
					isDefectLogged = true;
				}
			}
		} else {
			isDefectLogged = false;
		}

		return isDefectLogged;
	}

	private String addParams(String key, String clause, String value) {
		return key.concat(clause).concat(value);
	}

	/**
	 * Retrieving the Defect status
	 * 
	 */
	@Override
	public String getDefectStatus() {

		String defectStatus = loggedIssue.getStatus().getName();
		log.debug("Defect status ==>" + defectStatus);

		String loggedDefectStatus = JiraDefectStatusMap.getCommonStatusName(defectStatus);
		if (StringUtils.isNotBlank(loggedDefectStatus))
			defectStatus = loggedDefectStatus;

		return defectStatus;

	}

	@Override
	public void logDefect(String defectTitle, StringBuilder defectDescription, String defectPriority, File fileAttachment) throws Exception {

		IssueInputBuilder issueBuilder = new IssueInputBuilder(jiraConfig.getKey(), Long.valueOf(JiraConstants.BUG_TYPE_ID.getValue()), defectTitle.trim());
		issueBuilder.setDescription(defectDescription.toString());
		
		//*** set Severity ***//
		issueBuilder.setFieldValue("customfield_10301", ComplexIssueInputFieldValue.with("value", "Minor"));
				
		//Set Priority ***//
		issueBuilder.setFieldValue("priority", ComplexIssueInputFieldValue.with("name", "P2: Medium"));
		
		//*** Set environment 
		//issueBuilder.setFieldValue("customfield_10402", "[{\"value\": \"QA\"}]");***//
		String environment = TestBedManagerConfiguration.INSTANCE.getJiraConfig().getEnvironment();
		ComplexIssueInputFieldValue value = new ComplexIssueInputFieldValue(Collections.singletonMap("value", (Object) environment));
		issueBuilder.setFieldValue("customfield_10402", Collections.singletonList(value));

		IssueInput issueInput = issueBuilder.build();
		BasicIssue issue = issueRestClient.createIssue(issueInput).claim();
		loggedIssue = issueRestClient.getIssue(issue.getKey()).claim();

		// --Changes specfic for Splunk Only @ 02-25-2016
		updateTransition(JiraConstants.NEW.getValue());
		// -- End of Changes

		attachScreenshot(fileAttachment, new StringBuilder());

		log.debug("New issue created with id ==>" + issue.getKey());

	}

	@Override
	public void closeDefect(StringBuilder comments) throws URISyntaxException {
		addComments(comments);

		// --Changes specfic for Splunk Only @ 02-25-2016
		updateTransition(JiraConstants.RESOLVE.getValue());
		// -- End of Changes

		String defectStatus = loggedIssue.getStatus().getName();

		if (!defectStatus.equalsIgnoreCase(JiraConstants.CLOSE.getValue())) {
			updateTransition(JiraConstants.CLOSE.getValue());
		}

	}

	@Override
	public void reOpenDefect(StringBuilder comments, File fileAttachment) throws Exception {
		updateTransition(JiraConstants.REOPEN.getValue());
		attachScreenshot(fileAttachment, comments);
	}

	@Override
	public void updateDefect(StringBuilder comments, File fileAttachment) throws Exception {

		// --Changes specfic for Splunk Only @ 02-25-2016
		String updateTransitionState = JiraConstants.UPDATE.getValue();
		String defectStatus = loggedIssue.getStatus().getName();

		if (defectStatus.equalsIgnoreCase("New")) {
			updateTransitionState = JiraConstants.UPDATE.getValue();
		} else if (defectStatus.equalsIgnoreCase("Resolved")) {
			updateTransitionState = JiraConstants.UNRESOLVE.getValue();
		} else if (defectStatus.equalsIgnoreCase("In Review")) {
			updateTransitionState = JiraConstants.NOT_COMPLETE.getValue();
		}

		updateTransition(updateTransitionState);
		// -- End of Changes

		attachScreenshot(fileAttachment, comments);
	}

	/**
	 * @param fileAttachment
	 * @throws Exception
	 */
	private void attachScreenshot(File fileAttachment, StringBuilder comments) throws Exception {
		if (jiraConfig.isScreenshotAttachment()) {
			addAttachment(fileAttachment);
			comments.append("\n Screenshot attached for reference");
		}

		addComments(comments);
	}

	/**
	 * @param comments
	 * @throws URISyntaxException
	 */
	private void addComments(StringBuilder comments) throws URISyntaxException {

		try {

			URI issueUri = new URI(loggedIssue.getSelf().toString() + "/comment/");

			Comment comment = Comment.valueOf(comments.toString());

			Promise<Void> commentAdded = issueRestClient.addComment(issueUri, comment);
			commentAdded.claim();
		} catch (URISyntaxException uriSyntaxException) {
			log.error("Could not add comment to Jira Issue" + loggedIssue.getKey() + " :: " + uriSyntaxException.getMessage());
			throw uriSyntaxException;
		}

	}

	private void addAttachment(File attachmentFile) throws Exception {

		try {

			Promise<Void> attachementAdded = issueRestClient.addAttachments(loggedIssue.getAttachmentsUri(), attachmentFile);
			attachementAdded.claim();
		} catch (Exception exp) {
			log.error("Unable to attach file ==>" + attachmentFile.getName() + " to Jira on issueId ==> " + loggedIssue.getKey() + " :: " + exp.getCause());
			throw exp;
		}
	}

	/**
	 * @param transitionName
	 * 
	 */
	private void updateTransition(String transitionName) {

		log.debug("Updating the workflow -- " + transitionName);

		// updating the workflow
		Promise<Iterable<Transition>> transitions = issueRestClient.getTransitions(loggedIssue.getTransitionsUri());
		Transition progressTransition = getTransitionByName(transitions, transitionName);

		if (progressTransition != null) {
			TransitionInput transitionInput = new TransitionInput(progressTransition.getId());
			issueRestClient.transition(loggedIssue.getTransitionsUri(), transitionInput).claim();
		}

	}

	private Transition getTransitionByName(Promise<Iterable<Transition>> transitions, String transitionName) {

		for (Transition transition : transitions.claim()) {

			log.debug("Transition name ==>" + transition.getName());

			if (transition.getName().equals(transitionName)) {
				return transition;
			}
		}
		return null;
	}

}