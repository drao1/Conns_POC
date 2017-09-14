package com.etouch.taf.tools.rally;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.URISyntaxException;
import java.util.Date;
import java.util.concurrent.ExecutionException;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.logging.Log;

import com.etouch.taf.core.config.RallyConfig;
import com.etouch.taf.core.config.TestBedManagerConfiguration;
import com.etouch.taf.tools.defect.DefectTool;
import com.etouch.taf.util.LogUtil;
import com.etouch.taf.util.TafPassword;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.rallydev.rest.RallyRestApi;
import com.rallydev.rest.request.CreateRequest;
import com.rallydev.rest.request.QueryRequest;
import com.rallydev.rest.request.UpdateRequest;
import com.rallydev.rest.response.CreateResponse;
import com.rallydev.rest.response.QueryResponse;
import com.rallydev.rest.response.UpdateResponse;
import com.rallydev.rest.util.Fetch;
import com.rallydev.rest.util.QueryFilter;
import com.rallydev.rest.util.Ref;

/**
 * @author etouch
 * 
 */
public class Rally extends DefectTool {

	private static Log log = LogUtil.getLog(Rally.class);

	private RallyConfig rallyConfig;

	private RallyRestApi restApi;

	private JsonObject loggedDefect;

	public Rally() {
		rallyConfig = TestBedManagerConfiguration.INSTANCE.getRallyConfig();
	}

	@Override
	public void openConnection() throws IOException, URISyntaxException {
		TafPassword tfp = rallyConfig.getPassword();
		tfp.decryptPassword(tfp.getEncrypted());

		restApi = new RallyRestApi(rallyConfig.getUrl().toURI(), rallyConfig.getUsername(), tfp.getDecrypted());
		restApi.setApplicationName(rallyConfig.getRallyApp());

		log.debug("Connection established successfully...");
	}

	@Override
	public void closeConnection() throws IOException {
		log.debug("Closing Rally connection...");
		restApi.close();
	}

	@Override
	public boolean isDefectLogged(String defectTitle) throws InterruptedException, ExecutionException, IOException {

		boolean isDefectLogged = false;

		String queryType = RallyConstants.DEFECT.getValue();

		Fetch queryFetch = new Fetch(RallyConstants.ID.getValue(), RallyConstants.NAME.getValue(), RallyConstants.DESCRIPTION.getValue(), RallyConstants.STATE.getValue(),
				RallyConstants.SCHEDULE_STATE.getValue(), RallyConstants.PRIORITY.getValue(), RallyConstants.NOTES.getValue());

		QueryFilter queryFilter = new QueryFilter(RallyConstants.NAME.getValue(), RallyConstants.EQUALS.getValue(), defectTitle);

		QueryResponse queryResponse = getQueryResponse(queryType, queryFetch, queryFilter);

		isDefectLogged = buildQueryResponse(defectTitle, isDefectLogged, queryResponse);

		return isDefectLogged;
	}

	/**
	 * @param defectTitle
	 * @param isDefectLogged
	 * @param queryResponse
	 * @return
	 * @throws IOException
	 */
	private boolean buildQueryResponse(String defectTitle, boolean isDefectLogged, QueryResponse queryResponse) throws IOException {

		if (queryResponse.wasSuccessful()) {

			if (queryResponse.getTotalResultCount() > 0) {

				isDefectLogged = getLoggedDefect(defectTitle, isDefectLogged, queryResponse);
			} else {
				isDefectLogged = false;
			}

		} else {
			log.error("The following errors occurred: ");
			for (String err : queryResponse.getErrors()) {
				log.error(RallyConstants.TAB.getValue() + err);
			}

			throw new IOException();
		}
		return isDefectLogged;
	}

	/**
	 * @param defectTitle
	 * @param isDefectLogged
	 * @param queryResponse
	 * @return
	 */
	private boolean getLoggedDefect(String defectTitle, boolean isDefectLogged, QueryResponse queryResponse) {

		for (JsonElement result : queryResponse.getResults()) {
			JsonObject defect = result.getAsJsonObject();

			log.debug(String.format("\t%s - %s: Priority=%s, State=%s", defect.get(RallyConstants.ID.getValue()).getAsString(), defect.get(RallyConstants.NAME.getValue())
					.getAsString(), defect.get(RallyConstants.PRIORITY.getValue()).getAsString(), defect.get(RallyConstants.STATE.getValue()).getAsString()));

			String defectName = defect.get(RallyConstants.NAME.getValue()).getAsString();
			String defectId = defect.get(RallyConstants.ID.getValue()).getAsString();

			if (defectTitle.equals(defectName)) {
				log.debug("Exact match found with ID ==>" + defectId);
				loggedDefect = defect;
				isDefectLogged = true;
			}
		}
		return isDefectLogged;
	}

	/**
	 * @param queryType
	 * @param queryFetch
	 * @param queryFilter
	 * @return
	 * @throws IOException
	 */
	private QueryResponse getQueryResponse(String queryType, Fetch queryFetch, QueryFilter queryFilter) throws IOException {

		QueryRequest queryRequest = new QueryRequest(queryType);
		queryRequest.setFetch(queryFetch);
		queryRequest.setQueryFilter(queryFilter);

		queryRequest.setPageSize(Integer.parseInt(RallyConstants.DEFAULT_PAGE_SIZE.getValue()));

		return restApi.query(queryRequest);
	}

	@Override
	public String getDefectStatus() {
		String defectStatus = loggedDefect.get(RallyConstants.STATE.getValue()).getAsString();
		log.debug("Defect status ==>" + defectStatus);
		return RallyDefectStatusMap.getCommonStatusName(defectStatus);
	}

	@Override
	public void logDefect(String defectTitle, StringBuilder defectDescription, String defectPriority, File fileAttachment) throws Exception {

		JsonObject newDefect = new JsonObject();
		newDefect.addProperty(RallyConstants.NAME.getValue(), defectTitle);
		newDefect.addProperty(RallyConstants.PRIORITY.getValue(), (defectPriority.equals(RallyConstants.MEDIUM_PRIORITY.getValue()) ? RallyConstants.NORMAL_PRIORITY.getValue()
				: RallyConstants.LOW_PRIORITY.getValue()));
		newDefect.addProperty(RallyConstants.STATE.getValue(), RallyConstants.OPEN_STATE.getValue());
		newDefect.addProperty(RallyConstants.DESCRIPTION.getValue(), getFormattedText(defectDescription.toString()));
		CreateRequest createRequest = new CreateRequest(RallyConstants.DEFECT.getValue(), newDefect);
		CreateResponse createResponse = restApi.create(createRequest);

		if (!createResponse.wasSuccessful()) {
			log.error("The following errors occurred: ");

			for (String err : createResponse.getErrors()) {
				log.error(RallyConstants.TAB.getValue() + err);
			}
			throw new IOException();
		} else {
			loggedDefect = createResponse.getObject();
			log.debug("New issue created with id ==>" + loggedDefect.get(RallyConstants.ID.getValue()).getAsString());

			updateStatus(new StringBuilder("" + new Date()), RallyConstants.OPEN_STATE.getValue(), RallyConstants.DEFINED_SCHEDULE_STATE.getValue(), fileAttachment);
		}
	}

	@Override
	public void closeDefect(StringBuilder comments) throws URISyntaxException, IOException {
		updateStatus(comments, RallyConstants.CLOSED_STATE.getValue(), RallyConstants.ACCEPTED_SCHEDULE_STATE.getValue(), null);

	}

	@Override
	public void reOpenDefect(StringBuilder comments, File fileAttachment) throws URISyntaxException, IOException {
		updateStatus(comments, RallyConstants.OPEN_STATE.getValue(), RallyConstants.IN_PROGRESS_SCHEDULE_STATE.getValue(), fileAttachment);

	}

	@Override
	public void updateDefect(StringBuilder comments, File fileAttachment) throws URISyntaxException, IOException {
		updateStatus(comments, RallyConstants.OPEN_STATE.getValue(), RallyConstants.IN_PROGRESS_SCHEDULE_STATE.getValue(), fileAttachment);

	}

	/**
	 * @param comments
	 * @param scheduledState
	 * @throws IOException
	 */
	private void updateStatus(StringBuilder comments, String state, String scheduledState, File fileAttachment) throws IOException {
		log.debug("Updating defect to ==>" + state);

		String defectRef = Ref.getRelativeRef(loggedDefect.get(RallyConstants.REFERENCE.getValue()).getAsString());

		updateFileAttachment(defectRef, comments, fileAttachment);

		String existingNotes = loggedDefect.get(RallyConstants.NOTES.getValue()).getAsString();
		comments.append(RallyConstants.NEW_LINE.getValue() + RallyConstants.NEW_LINE.getValue() + existingNotes);

		postUpdates(comments, state, scheduledState, defectRef);
	}

	/**
	 * @param comments
	 * @param state
	 * @param scheduledState
	 * @param ref
	 * @throws IOException
	 */
	private void postUpdates(StringBuilder comments, String state, String scheduledState, String ref) throws IOException {

		JsonObject updateDefect = new JsonObject();
		updateDefect.addProperty(RallyConstants.STATE.getValue(), state);
		updateDefect.addProperty(RallyConstants.SCHEDULE_STATE.getValue(), scheduledState);
		updateDefect.addProperty(RallyConstants.NOTES.getValue(), getFormattedText(comments.toString()));
		UpdateRequest updateRequest = new UpdateRequest(ref, updateDefect);
		UpdateResponse updateResponse = restApi.update(updateRequest);

		if (!updateResponse.wasSuccessful()) {
			log.error("The following errors occurred: ");
			for (String err : updateResponse.getErrors()) {
				log.error(RallyConstants.TAB.getValue() + err);
			}

			throw new IOException();
		}
	}

	private void updateFileAttachment(String defectRef, StringBuilder comments, File fileAttachment) throws IOException {

		if (fileAttachment != null) {

			String fileBase64String = convertFileAttachment(fileAttachment);

			String userRef = getUserRef();

			buildFileAttachment(defectRef, comments, fileAttachment, fileBase64String, userRef);

		}
	}

	/**
	 * @param defectRef
	 * @param comments
	 * @param fileAttachment
	 * @param fileBase64String
	 * @param userRef
	 * @throws IOException
	 */
	private void buildFileAttachment(String defectRef, StringBuilder comments, File fileAttachment, String fileBase64String, String userRef) throws IOException {

		// Create AttachmentContent from image string
		JsonObject fileAttachmentContent = new JsonObject();
		fileAttachmentContent.addProperty(RallyConstants.CONTENT.getValue(), fileBase64String);
		CreateRequest attachmentContentCreateRequest = new CreateRequest(RallyConstants.ATTACHMENT_CONTENT.getValue(), fileAttachmentContent);
		CreateResponse attachmentContentResponse = restApi.create(attachmentContentCreateRequest);

		if (attachmentContentResponse.wasSuccessful()) {
			postAttachment(defectRef, comments, fileAttachment, userRef, attachmentContentResponse);
		} else {
			log.error("The following errors occurred: ");
			for (String err : attachmentContentResponse.getErrors()) {
				log.error(RallyConstants.TAB.getValue() + err);
			}
			throw new IOException();
		}
	}

	/**
	 * @param defectRef
	 * @param comments
	 * @param fileAttachment
	 * @param userRef
	 * @param attachmentContentResponse
	 * @throws IOException
	 */
	private void postAttachment(String defectRef, StringBuilder comments, File fileAttachment, String userRef, CreateResponse attachmentContentResponse) throws IOException {

		String fileAttachmentContentRef = attachmentContentResponse.getObject().get(RallyConstants.REFERENCE.getValue()).getAsString();

		// Creating the attachment to Defect Object
		JsonObject attachmentObject = new JsonObject();
		attachmentObject.addProperty(RallyConstants.ARTIFACT.getValue(), defectRef);
		attachmentObject.addProperty(RallyConstants.CONTENT.getValue(), fileAttachmentContentRef);
		attachmentObject.addProperty(RallyConstants.NAME.getValue(), fileAttachment.getName());
		attachmentObject.addProperty(RallyConstants.CONTENT_TYPE.getValue(), RallyConstants.IMAGE_CONTENT_TYPE.getValue());
		attachmentObject.addProperty(RallyConstants.ATTACHMENT_SIZE.getValue(), fileAttachmentSize);
		attachmentObject.addProperty(RallyConstants.USER.getValue(), userRef);

		CreateRequest attachmentCreateRequest = new CreateRequest(RallyConstants.ATTACHMENT.getValue(), attachmentObject);
		CreateResponse attachmentResponse = restApi.create(attachmentCreateRequest);

		if (attachmentResponse.wasSuccessful()) {
			log.debug("Attachment  created: " + attachmentResponse.getObject().get(RallyConstants.REFERENCE.getValue()).getAsString());
		} else {
			log.error("The following errors occurred: ");
			for (String err : attachmentContentResponse.getErrors()) {
				log.error(RallyConstants.TAB.getValue() + err);
			}
			throw new IOException();
		}

		comments.append(RallyConstants.NEW_LINE.getValue() + " Screenshot attached for reference");
	}

	private int fileAttachmentSize = 0;

	private String convertFileAttachment(File fileAttachment) throws IOException {

		String fileBase64String = "";

		RandomAccessFile fileReader = new RandomAccessFile(fileAttachment.getAbsolutePath(), "r");
		try {

			byte[] fileBytes = new byte[(int) fileReader.length()];

			fileReader.readFully(fileBytes);

			if (fileReader.length() >= Long.parseLong(RallyConstants.MAX_FILE_SIZE.getValue())) {
				throw new IOException("File size cannot be greater than 5MB.");
			} else {
				fileAttachmentSize = (int) fileReader.length();
			}

			fileBase64String = new String(Base64.encodeBase64(fileBytes));
		} catch (IOException ioException) {
			log.error("Error in attaching file ==>" + fileAttachment.getName() + " :: " + ioException.getMessage());
			throw ioException;
		} finally {
			fileReader.close();
		}

		return fileBase64String;
	}

	/**
	 * @return
	 * @throws IOException
	 */
	private String getUserRef() throws IOException {
		// Read User
		String requestType = RallyConstants.USER.getValue();
		Fetch userFetch = new Fetch(RallyConstants.USER_NAME.getValue(), RallyConstants.SUBSCRIPTION.getValue(), RallyConstants.USER_DISPLAY_NAME.getValue());
		QueryFilter userQueryFilter = new QueryFilter(RallyConstants.USER_NAME.getValue(), RallyConstants.EQUALS.getValue(), rallyConfig.getUsername());
		QueryResponse userQueryResponse = getQueryResponse(requestType, userFetch, userQueryFilter);

		JsonElement userQueryElement = userQueryResponse.getResults().get(0);
		JsonObject userQueryObject = userQueryElement.getAsJsonObject();
		return userQueryObject.get(RallyConstants.REFERENCE.getValue()).getAsString();
	}

	private String getFormattedText(String string) {
		return string.replace(RallyConstants.NEW_LINE.getValue(), RallyConstants.HTML_BREAK.getValue());
	}

}
