package com.etouch.taf.tools.rally;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.logging.Log;

import com.etouch.taf.core.TestBedManager;
import com.etouch.taf.core.datamanager.excel.TestParameters;
import com.etouch.taf.core.exception.DefectException;
import com.etouch.taf.core.resources.DefectParameters;
import com.etouch.taf.tools.defect.IDefectManager;
import com.etouch.taf.util.CommonUtil;
import com.etouch.taf.util.DateUtil;
import com.etouch.taf.util.LogUtil;
import com.etouch.taf.util.TafPassword;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.rallydev.rest.RallyRestApi;
import com.rallydev.rest.request.CreateRequest;
import com.rallydev.rest.request.DeleteRequest;
import com.rallydev.rest.request.GetRequest;
import com.rallydev.rest.request.QueryRequest;
import com.rallydev.rest.request.UpdateRequest;
import com.rallydev.rest.response.CreateResponse;
import com.rallydev.rest.response.DeleteResponse;
import com.rallydev.rest.response.GetResponse;
import com.rallydev.rest.response.QueryResponse;
import com.rallydev.rest.response.UpdateResponse;
import com.rallydev.rest.util.Fetch;
import com.rallydev.rest.util.QueryFilter;
import com.rallydev.rest.util.Ref;

/**
 * RallyIntegration exposes API to integrate with Rally.
 * 
 * @author eTouch Systems Corporation
 * @version 1.0
 * 
 */

public class RallyForScripting implements IDefectManager {

	/** The log. */
	private static Log log = LogUtil.getLog(RallyForScripting.class);

	/** The rally info. */
	private RallyInfo rallyInfo;

	/** The rest api. */
	private RallyRestApi restApi;

	/** The defect status. */
	private String defectStatus;

	/** The defect oid. */
	private String defectOID;

	/**
	 * Instantiates a new rally.
	 * 
	 * @param rallyInfo
	 *            the rally info
	 * @throws DefectException
	 *             the defect exception
	 */
	public RallyForScripting(RallyInfo rallyInfo) throws DefectException {
		this.rallyInfo = rallyInfo;
	}

	/**
	 * Sets the rally info.
	 * 
	 * @param rallyInfo
	 *            the new rally info
	 */
	public void setRallyInfo(RallyInfo rallyInfo) {
		this.rallyInfo = rallyInfo;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.etouch.taf.tools.defect.IDefectManager#getDefectStatus()
	 */
	public String getDefectStatus() {
		return defectStatus;
	}

	/**
	 * Sets the defect status.
	 * 
	 * @param rallyStatus
	 *            the new defect status
	 */
	public void setDefectStatus(String rallyStatus) {
		this.defectStatus = rallyStatus;
	}

	/**
	 * Gets the defect oid.
	 * 
	 * @return the defect oid
	 */
	private String getDefectOID() {
		return defectOID;
	}

	/**
	 * Sets the defect oid.
	 * 
	 * @param defectOID
	 *            the new defect oid
	 */
	private void setDefectOID(String defectOID) {
		this.defectOID = defectOID;
	}

	/**
	 * Build a RequestInfo and create a rally defect .
	 * 
	 * @param defName
	 *            the def name
	 * @param testcaseId
	 *            the testcase id
	 * @param workspaceId
	 *            the workspace id
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
	public void createDefectBuilder(String defName, String testcaseId, String workspaceId, String projId, String defSeverity, String defOwner, String defNotes, String storyId,
			String attachmentPath) {

		try {

			Map<String, String> reqMap = new HashMap<>();
			reqMap.put(RallyConstantsForScripting.NAME, defName);
			reqMap.put(RallyConstantsForScripting.CREATEDATE, DateUtil.getCurrentDateTime(RallyConstantsForScripting.CREATION_DATE_FORMAT));
			reqMap.put(RallyConstantsForScripting.TESTCASE, testcaseId);
			reqMap.put(RallyConstantsForScripting.PROJECT, projId);
			reqMap.put(RallyConstantsForScripting.SEVERITY, defSeverity);
			reqMap.put(RallyConstantsForScripting.OWNER, defOwner);
			reqMap.put(RallyConstantsForScripting.NOTES, defNotes);
			reqMap.put(RallyConstantsForScripting.WORKSPACE, workspaceId);

			RequestInfo reqInfo = new RequestInfo(RallyConstantsForScripting.DEFECT, RallyConstantsForScripting.CREATE, reqMap);
			try {
				create(reqInfo, attachmentPath);
				log.debug("done with logging defect. Now updating the testcase result");

			} catch (DefectException ex) {
				log.error(ex.getMessage());
			} catch (IOException e) {

				log.debug("IOException", e);
			}
		} catch (NullPointerException e) {
			log.error(e);
		}
	}

	/**
	 * /** Verify if a defect already exists in Rally. If the defect exists,
	 * save the testcase formatted id, defect id and defect status
	 * 
	 * @param ri
	 * 
	 * @param fileName
	 * 
	 * @param testcaseId
	 * 
	 * @param projId
	 * 
	 * @param storyId
	 * 
	 * @param defName
	 */
	/**
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.etouch.taf.tools.defect.IDefectManager#verifyDefectExists(java.lang
	 * .String, java.lang.String, java.lang.String, java.lang.String)
	 */
	public boolean verifyDefectExists(String testcaseId, String projId, String storyId, String defName) {
		String formattedID = null;
		String testCaseRegex = "^/testcase/";
		String formatRegex = "^FormattedID[\\s]*:[\\s]*";
		String stateRegex = "^.*State[\\s]*:[\\s]*";
		String objPreRegex = "^ObjectID[\\s]*:[\\s]*";
		String objPostRegex = "[\\s]*State[\\s]*:[\\s]*[\\w]*";
		RequestInfo rInfo = new RequestInfo();

		// Retrieve the TestCase FormattedID using the TestCase OID
		String[] fetchParam = { RallyConstantsForScripting.FORMATTED_ID };
		String[] filterArray = { RallyConstantsForScripting.OBJECTID, RallyConstantsForScripting.EQUALTO, testcaseId.replaceFirst(testCaseRegex, "") };
		buildQueryReqInfo(rInfo, RallyConstantsForScripting.TESTCASE, fetchParam, projId, filterArray);
		try {
			List<String> list = getList(rInfo);
			if (list != null) {
				formattedID = getList(rInfo).get(0);
			}
		} catch (DefectException e) {
			return false;
		}
		if (formattedID != null) {
			formattedID = formattedID.replaceFirst(formatRegex, "");
		} else {
			log.error("Failure to verify if defect exists: failure to retrieve testcase details for OID - " + testcaseId);
			return false;
		}

		// Retrieve all the defects related to the TestCase FormattedID
		fetchParam = new String[] { RallyConstantsForScripting.OBJECTID, RallyConstantsForScripting.STATE };
		if (formattedID != null) {
			filterArray = new String[] { RallyConstantsForScripting.TESTCASEFID, RallyConstantsForScripting.EQUALTO, formattedID, RallyConstantsForScripting.AND_OPERATOR,
					RallyConstantsForScripting.NAME, RallyConstantsForScripting.EQUALTO, defName };
		} else {

			filterArray = new String[] { RallyConstantsForScripting.NAME, RallyConstantsForScripting.EQUALTO, defName };
		}

		buildQueryReqInfo(rInfo, RallyConstantsForScripting.DEFECT, fetchParam, projId, filterArray);
		try {
			List<String> qlist = getList(rInfo);
			if (qlist != null && !qlist.isEmpty()) {
				String objIDStr = qlist.get(0).replaceFirst(objPreRegex, "");
				setDefectOID(objIDStr.replaceFirst(objPostRegex, "").trim());
				setDefectStatus(qlist.get(0).replaceFirst(stateRegex, "").trim());
				return true;
			}
		} catch (DefectException e) {
			log.error(e);
			return false;
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.etouch.taf.tools.defect.IDefectManager#verifyIfdefectClosed()
	 */
	public boolean verifyIfdefectClosed() {
		if (getDefectStatus() != null && (getDefectStatus().equals(RallyConstantsForScripting.STATUS_FIXED) || getDefectStatus().equals(RallyConstantsForScripting.STATUS_CLOSED))) {
			return true;
		}
		return false;
	}

	/**
	 * Query test case format id.
	 * 
	 * @param testcaseId
	 *            the testcase id
	 * @param projId
	 *            the proj id
	 * @param storyId
	 *            the story id
	 * @param defName
	 *            the def name
	 * @return the string
	 */
	public String queryTestCaseFormatID(String testcaseId, String projId, String storyId, String defName) {
		String formattedID = "";
		String testCaseRegex = "^/testcase/";
		String formatRegex = "^FormattedID[\\s]*:[\\s]*";
		RequestInfo rInfo = new RequestInfo();

		// Retrieve the TestCase FormattedID using the TestCase OID
		String[] fetchParam = { RallyConstantsForScripting.FORMATTED_ID };
		String[] filterArray = { RallyConstantsForScripting.OBJECTID, RallyConstantsForScripting.EQUALTO, testcaseId.replaceFirst(testCaseRegex, "") };
		buildQueryReqInfo(rInfo, RallyConstantsForScripting.TESTCASE, fetchParam, projId, filterArray);
		try {
			if (getList(rInfo) != null && !getList(rInfo).isEmpty()) {
				formattedID = getList(rInfo).get(0);
			}
		} catch (DefectException e) {
			log.error(e);
		}
		if (formattedID != null) {
			formattedID = formattedID.replaceFirst(formatRegex, "");
		}
		return formattedID;
	}

	/**
	 * Retrieve the defect object ID .
	 * 
	 * @param testcaseId
	 *            the testcase id
	 * @param storyId
	 *            the story id
	 * @param projId
	 *            the proj id
	 * @param defName
	 *            the def name
	 * @return the string
	 */
	public String queryDefectID(String testcaseId, String storyId, String projId, String defName) {
		String objPreRegex = "^ObjectID[\\s]*:[\\s]*";
		RequestInfo rInfo = new RequestInfo();
		String[] fetchParam = new String[] { RallyConstantsForScripting.OBJECTID };

		// Retrieve the testcase format id
		String tcFormattedID = queryTestCaseFormatID(testcaseId, projId, storyId, defName);
		// Retrieve all the defects related to the TestCase FormattedID
		if (tcFormattedID != null && !tcFormattedID.isEmpty()) {
			String[] filterArray = new String[] { RallyConstantsForScripting.TESTCASEFID, RallyConstantsForScripting.EQUALTO, tcFormattedID,
					RallyConstantsForScripting.AND_OPERATOR, RallyConstantsForScripting.NAME, RallyConstantsForScripting.EQUALTO, defName };
			buildQueryReqInfo(rInfo, RallyConstantsForScripting.DEFECT, fetchParam, projId, filterArray);
			try {
				List<String> qlist = getList(rInfo);
				if ((qlist != null) && !qlist.isEmpty()) {
					setDefectOID(qlist.get(0).replaceFirst(objPreRegex, "").trim());
				}
			} catch (DefectException e) {
				log.error(e);
			}
		}
		return getDefectOID();
	}

	/**
	 * Retrieve the defect object ID .
	 * 
	 * @param testcaseId
	 *            the testcase id
	 * @param storyId
	 *            the story id
	 * @param projId
	 *            the proj id
	 * @param defName
	 *            the def name
	 * @return the string
	 */
	public String queryDefectStatus(String testcaseId, String storyId, String projId, String defName) {
		String stateRegex = "^.*State[\\s]*:[\\s]*";
		RequestInfo rInfo = new RequestInfo();

		// Retrieve the testcase format id
		String tcFormattedID = queryTestCaseFormatID(testcaseId, projId, storyId, defName);
		// Retrieve all the defects related to the TestCase FormattedID
		String[] fetchParam = new String[] { RallyConstantsForScripting.STATE };
		String[] filterArray = new String[] { RallyConstantsForScripting.TESTCASEFID, RallyConstantsForScripting.EQUALTO, tcFormattedID, RallyConstantsForScripting.AND_OPERATOR,
				RallyConstantsForScripting.NAME, RallyConstantsForScripting.EQUALTO, defName };
		buildQueryReqInfo(rInfo, RallyConstantsForScripting.DEFECT, fetchParam, projId, filterArray);
		try {
			List<String> qlist = getList(rInfo);
			if ((qlist != null) && !qlist.isEmpty()) {
				setDefectStatus(qlist.get(0).replaceFirst(stateRegex, "").trim());
			}
		} catch (DefectException e) {
			log.error(e);
		}

		return getDefectStatus();
	}

	/**
	 * Retrieve the defect object ID Note: example of queryByParam: String[]
	 * queryByParam = new String[]{IRallyParam.OBJECTID}; example of
	 * fetchQueryRegex: String fetchQueryRegex = "^ObjectID[\\s]*:[\\s]*";
	 * 
	 * @param testcaseId
	 *            the testcase id
	 * @param storyId
	 *            the story id
	 * @param projId
	 *            the proj id
	 * @param defName
	 *            the def name
	 * @param queryByParam
	 *            the query by param
	 * @return the string
	 */
	public String queryDefect(String testcaseId, String storyId, String projId, final String defName, DefectParameters.IDefect queryByParam) {
		RequestInfo rInfo = new RequestInfo();
		String fetchQueryVal = null;
		String fetchQueryRegex = "^.*" + queryByParam.getParamType() + "[\\s]*:[\\s]*";

		// Retrieve the testcase format id
		String tcFormattedID = queryTestCaseFormatID(testcaseId, projId, storyId, defName);
		// Retrieve all the defects related to the TestCase FormattedID
		if (tcFormattedID != null && !tcFormattedID.isEmpty()) {
			String[] fetchParamArray = new String[] { queryByParam.getParamType() };
			String[] filterArray = new String[] { RallyConstantsForScripting.TESTCASEFID, RallyConstantsForScripting.EQUALTO, tcFormattedID,
					RallyConstantsForScripting.AND_OPERATOR, RallyConstantsForScripting.NAME, RallyConstantsForScripting.EQUALTO, defName };
			buildQueryReqInfo(rInfo, RallyConstantsForScripting.DEFECT, fetchParamArray, projId, filterArray);
			try {
				List<String> qlist = getList(rInfo);
				if ((qlist != null) && !qlist.isEmpty()) {
					fetchQueryVal = qlist.get(0).replaceFirst(fetchQueryRegex, "").trim();
				}
			} catch (DefectException e) {
				log.error(e);
			}
		}
		return fetchQueryVal;
	}

	/**
	 * Build a RequestInfo and update a rally defect .
	 */
	public void reopenDefect() {
		try {
			String ref = "/" + RallyConstantsForScripting.DEFECT + "/" + getDefectOID();
			RequestInfo reqInfo = new RequestInfo();
			reqInfo.setRefField(ref);
			reqInfo.addEntry(RallyConstantsForScripting.UPDATE, RallyConstantsForScripting.STATE, RallyConstantsForScripting.STATUS_OPEN);
			try {
				update(reqInfo);
			} catch (DefectException e) {
				log.error(e);
			}
		} finally {
			setDefectOID(null);
			setDefectStatus(null);
		}
	}

	/**
	 * Build a RequestInfo and update a rally defect field/attribute.
	 * 
	 * @param testcaseId
	 *            the testcase id
	 * @param projId
	 *            the proj id
	 * @param storyId
	 *            the story id
	 * @param defName
	 *            the def name
	 * @param updateKey
	 *            the update key
	 * @param updateValue
	 *            the update value
	 * @return true, if successful
	 */
	public boolean updateDefect(String testcaseId, String projId, String storyId, String defName, DefectParameters.IDefect updateKey, String updateValue) {
		try {
			if (verifyDefectExists(testcaseId, projId, storyId, defName)) {

				String ref = "/" + RallyConstantsForScripting.DEFECT + "/" + getDefectOID();
				RequestInfo reqInfo = new RequestInfo();
				reqInfo.setRefField(ref);
				reqInfo.addEntry(RallyConstantsForScripting.UPDATE, updateKey.getParamType(), updateValue);
				update(reqInfo);
				log.info("Successfully updated:" + updateKey.getParamType());
				return true;
			}
		} catch (DefectException e) {
			log.error("Error when updating defect: " + e.getMessage());
		} finally {
			setDefectOID(null);
		}
		return false;
	}

	/**
	 * Close rally connection.
	 * 
	 * @throws DefectException
	 *             the defect exception
	 */
	public void closeDefect() throws DefectException {
		if (!CommonUtil.isNull(restApi)) {
			// Release resources
			try {
				restApi.close();
			} catch (IOException e) {
				log.error("failed to close rally connection, message : " + e.toString());
				throw new DefectException("failed to close rally connection, message : " + e.toString());
			} finally {
				restApi = null;
			}
		}
	}

	/**
	 * Build the RequestInfo with arguments provided.
	 * 
	 * @param rInfo
	 *            the r info
	 * @param requestType
	 *            the request type
	 * @param fetchParam
	 *            the fetch param
	 * @param projId
	 *            the proj id
	 * @param filterArray
	 *            the filter array
	 */
	private void buildQueryReqInfo(RequestInfo rInfo, String requestType, String[] fetchParam, String projId, String[] filterArray) {
		rInfo.setRequestType(requestType);
		rInfo.setProjectOID(projId);
		rInfo.setScopeDown(true);
		rInfo.setFetch(fetchParam);
		ArrayList<String> filterList = new ArrayList<>(Arrays.asList(filterArray));
		rInfo.setQueryFilter(filterList);
		log.debug(requestType + " -- " + projId + " -- " + fetchParam + " -- " + filterList);
	}

	/**
	 * Returns list of query result based on query parameters supplied using
	 * request info.
	 * 
	 * @param requestInfo
	 *            Information related to request.
	 * @return list of query result if successful.
	 * @throws DefectException
	 *             the defect exception
	 */
	private List<String> getList(RequestInfo requestInfo) throws DefectException {
		log.info("Start - getList");
		createInstance();
		List<String> lstResult = new ArrayList<>();
		if (CommonUtil.isNull(requestInfo)) {
			log.error("Request Info is null - " + requestInfo);
			throw new DefectException("failed to get list, request information is missing");
		}

		try {

			log.info("Querying for top highest priority unfixed defects...");
			log.debug("Query Request Info " + requestInfo.getQueryFilter());
			QueryResponse qryResponse = getQueryResponse(requestInfo);

			if (qryResponse.wasSuccessful()) {
				log.info(String.format("%nTotal results: %d", qryResponse.getTotalResultCount()));
				for (JsonElement result : qryResponse.getResults()) {
					JsonObject qryObject = result.getAsJsonObject();
					String strFetch[] = requestInfo.getFetch();
					String strPrintInfo = "";
					if (strFetch != null && strFetch.length != 0) {
						for (int i = 0; i < strFetch.length; i++) {
							strPrintInfo = strPrintInfo + strFetch[i] + " : " + qryObject.get(strFetch[i]).getAsString() + " ";
						}
					}
					log.info(strPrintInfo);
					lstResult.add(strPrintInfo);
				}
			} else {
				for (String err : qryResponse.getErrors()) {
					log.error("\t" + err);
					lstResult.add(err);
				}
				log.error("Error in getting list : " + lstResult);
				throw new DefectException("Error in getting list" + lstResult);
			}
		} catch (Exception e) {
			log.error("Error in getting list, message : " + e.toString());
			throw new DefectException("Error in getting list, message : " + e.toString());
		} finally {
			closeDefect();
			log.info("End - getList");
		}
		return lstResult;
	}

	/**
	 * Creates TestCase,Defects with or with out attachment etc.
	 * 
	 * @param requestInfo
	 *            the request info
	 * @throws DefectException
	 *             the defect exception
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public void create(RequestInfo requestInfo, String fullImageFile) throws DefectException, IOException {
		log.info("Start - create");
		createInstance();

		if (CommonUtil.isNull(requestInfo)) {
			log.error("Request Info is null - " + requestInfo);
			throw new DefectException("failed to create, request information is missing");
		}
		try {

			JsonObject newEntry = convertMapToJson(requestInfo.getEntry().get(RallyConstantsForScripting.CREATE));
			CreateRequest createRequest = new CreateRequest(requestInfo.getRequestType(), newEntry);
			CreateResponse createResponse = null;

			try {
				createResponse = restApi.create(createRequest);
			} catch (IOException e) {
				log.error("failed to create new entry, message : " + e.toString() + "RallyInfo -" + rallyInfo);
				throw new DefectException("failed to create new entry, message : " + e.toString());
			}

			if (createResponse.wasSuccessful()) {
				// Read defect
				String ref = Ref.getRelativeRef(createResponse.getObject().get("_ref").getAsString());
				String imageBase64String;
				long attachmentSize;

				// Open file
				try (RandomAccessFile myImageFileHandle = new RandomAccessFile(fullImageFile, "r")) {

					long longLength = myImageFileHandle.length();
					long maxLength = 5000000;
					if (longLength >= maxLength) {
						throw new IOException("File size >= 5 MB Upper limit for Rally.");
					}
					int fileLength = (int) longLength;

					// Read file and return data
					byte[] fileBytes = new byte[fileLength];
					myImageFileHandle.readFully(fileBytes);
					imageBase64String = new String(Base64.encodeBase64(fileBytes));
					attachmentSize = fileLength;

					String workspaceRef = "/" + RallyConstantsForScripting.WORKSPACE + "/" + TestBedManager.INSTANCE.getDefectConfig().getWorkspaceId();

					// First create AttachmentContent from image string
					JsonObject myAttachmentContent = new JsonObject();

					myAttachmentContent.addProperty("Workspace", workspaceRef);

					myAttachmentContent.addProperty("Content", imageBase64String);
					CreateRequest attachmentContentCreateRequest = new CreateRequest("AttachmentContent", myAttachmentContent);
					CreateResponse attachmentContentResponse = restApi.create(attachmentContentCreateRequest);
					String myAttachmentContentRef = attachmentContentResponse.getObject().get("_ref").getAsString();

					// Create the Attachment
					JsonObject myAttachment = new JsonObject();
					myAttachment.addProperty("Artifact", ref);
					myAttachment.addProperty("Content", myAttachmentContentRef);
					myAttachment.addProperty("Workspace", workspaceRef);
					myAttachment.addProperty("Name", RallyConstantsForScripting.NAME);
					myAttachment.addProperty("Description", RallyConstantsForScripting.DESCRIPTION);
					myAttachment.addProperty("ContentType", RallyConstantsForScripting.CONTENTTYPE);
					myAttachment.addProperty("Size", attachmentSize);

					CreateRequest attachmentCreateRequest = new CreateRequest("Attachment", myAttachment);
					CreateResponse attachmentResponse = restApi.create(attachmentCreateRequest);

					String myAttachmentRef = attachmentResponse.getObject().get("_ref").getAsString();

					if (attachmentResponse.wasSuccessful()) {
						log.info("Successfully created Attachment");

					} else {
						String[] attachmentContentErrors;
						attachmentContentErrors = attachmentResponse.getErrors();
						log.debug("Error occurred creating Attachment: ");
						for (int j = 0; j < attachmentContentErrors.length; j++) {
							log.debug(attachmentContentErrors[j]);
						}
					}

				} catch (FileNotFoundException e1) {

					log.debug("FileNotFoundException", e1);
				} catch (Exception e) {
					log.debug("Exception occurred while attempting to create Content and/or Attachment: ");
					log.debug(e);
				}

			} else {
				String[] createErrors;
				createErrors = createResponse.getErrors();
				log.debug("Error occurred creating a defect: ");
				for (int j = 0; j < createErrors.length; j++) {
					log.debug(createErrors[j]);
				}
			}

		} finally {

			try {
				restApi.close();
			} catch (IOException e) {

				log.debug("IOException", e);
			}
		}
	}

	/**
	 * Updates TestCase results based on test runs of the test cases .
	 * 
	 * @param defName
	 *            the def name
	 * @param testcaseId
	 *            the testcase id
	 * @param workspaceId
	 *            the workspace id
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
	// added by sonam

	public void updateTestCaseResult(String defName, String testcaseId, String workspaceId, String projId, String defSeverity, String defOwner, String defNotes, String storyId) {

		log.info("Start - create");

		try {
			createInstance();
			String workspaceRef = "/" + RallyConstantsForScripting.WORKSPACE + "/" + workspaceId;
			String projectRef = "/" + RallyConstantsForScripting.WORKSPACE + "/" + projId;
			// Read User
			QueryRequest userRequest = new QueryRequest("User");
			userRequest.setFetch(new Fetch("UserName", "Subscription", "DisplayName", "SubscriptionAdmin"));
			userRequest.setQueryFilter(new QueryFilter("UserName", "=", rallyInfo.getUserName()));

			QueryResponse userQueryResponse = restApi.query(userRequest);
			JsonArray userQueryResults = userQueryResponse.getResults();
			JsonElement userQueryElement = userQueryResults.get(0);
			JsonObject userQueryObject = userQueryElement.getAsJsonObject();
			String userRef = userQueryObject.get("_ref").getAsString();

			// to get tescase formatted ID
			QueryRequest request = new QueryRequest("TestCase");
			request.setWorkspace(workspaceRef);
			request.setFetch(new Fetch("FormattedID"));
			request.setQueryFilter(new QueryFilter("ObjectID", "=", testcaseId));
			QueryResponse response = restApi.query(request);
			String testcaseform = "";
			for (int j = 0; j < response.getTotalResultCount(); j++) {
				JsonObject jsonObject = response.getResults().get(j).getAsJsonObject();
				JsonElement FormattedTestcaseId = jsonObject.get("FormattedID");
				testcaseform = FormattedTestcaseId.getAsString();
			}

			// Query for Test Case to which we want to add results
			QueryRequest testCaseRequest = new QueryRequest("TestCase");
			testCaseRequest.setFetch(new Fetch("FormattedID", "Name"));
			testCaseRequest.setWorkspace(workspaceRef);
			testCaseRequest.setQueryFilter(new QueryFilter("FormattedID", "=", testcaseform));

			QueryResponse testCaseQueryResponse = restApi.query(testCaseRequest);
			JsonObject testCaseJsonObject = testCaseQueryResponse.getResults().get(0).getAsJsonObject();
			String testCaseRef = testCaseQueryResponse.getResults().get(0).getAsJsonObject().get("_ref").getAsString();

			for (int i = 0; i < 1; i++) {

				// Add a Test Case Result
				log.info("Creating Test Case Result..");
				JsonObject newTestCaseResult = new JsonObject();
				newTestCaseResult.addProperty("Verdict", RallyConstantsForScripting.VERDICT);
				newTestCaseResult.addProperty("Date", DateUtil.getCurrentDateTime(RallyConstantsForScripting.CREATION_DATE_FORMAT));
				newTestCaseResult.addProperty("Notes", defNotes);
				newTestCaseResult.addProperty("Build", TestBedManager.INSTANCE.getDefectConfig().getBuild());
				newTestCaseResult.addProperty("Tester", userRef);
				newTestCaseResult.addProperty("TestCase", testCaseRef);
				newTestCaseResult.addProperty("Workspace", workspaceRef);

				CreateRequest createRequest = new CreateRequest("testcaseresult", newTestCaseResult);
				CreateResponse createResponse = restApi.create(createRequest);
				if (createResponse.wasSuccessful()) {

					// Read Test Case
					String ref = Ref.getRelativeRef(createResponse.getObject().get("_ref").getAsString());
					GetRequest getRequest = new GetRequest(ref);
					getRequest.setFetch(new Fetch("Date", "Verdict"));
					GetResponse getResponse = restApi.get(getRequest);
					JsonObject obj = getResponse.getObject();
					log.debug(String.format("my Read Test Case Result. Date = %s, Verdict = %s", obj.get("Date").getAsString(), obj.get("Verdict").getAsString()));
				} else {
					String[] createErrors;
					createErrors = createResponse.getErrors();
					log.debug("Error occurred creating Test Case Result: ");
					for (int k = 0; i < createErrors.length; k++) {
						log.debug(createErrors[k]);
					}
				}
			}

		} catch (DefectException e) {

			log.debug("DefectException", e);
		} catch (IOException e) {

			log.debug("IOException", e);
		} finally {
			// Release all resources
			try {
				restApi.close();
			} catch (IOException e) {

				log.debug("IOException", e);
			}
		}

	}

	/**
	 * Updates test case or defect.
	 * 
	 * @param requestInfo
	 *            the request info
	 * @throws DefectException
	 *             the defect exception
	 */
	private void update(RequestInfo requestInfo) throws DefectException {
		log.info("Start - update");
		createInstance();
		if (CommonUtil.isNull(requestInfo)) {
			log.error("Request Info is null - " + requestInfo);
			throw new DefectException("failed to update, request information is missing");
		}
		try {

			String ref = requestInfo.getRefField();

			// Update
			JsonObject updateEntry = convertMapToJson(requestInfo.getEntry().get(RallyConstantsForScripting.UPDATE));
			UpdateRequest updateRequest = new UpdateRequest(ref, updateEntry);
			UpdateResponse updateResponse = null;

			try {
				updateResponse = restApi.update(updateRequest);
			} catch (IOException e) {
				log.error("failed to update, message : " + e.toString() + "RequestInfo -" + requestInfo);
				throw new DefectException("failed to update, message : " + e.toString());
			}

			if (updateResponse.wasSuccessful()) {
				log.info(String.format("Updated %s", updateResponse.getObject().get(RallyConstantsForScripting._REF).getAsString()));
			} else {
				List<String> lstResult = new ArrayList<String>();
				for (String err : updateResponse.getErrors()) {
					log.error("\t" + err);
					lstResult.add(err);
				}
				log.error("Error in updating : " + lstResult);
				throw new DefectException("Error in updating : " + lstResult);
			}
		} finally {
			closeDefect();
			log.info("End - update");
		}
	}

	/**
	 * Delete Test case or Defect. create a wrapper method 'deleteDefectBuilder'
	 * for this method
	 * 
	 * @param requestInfo
	 *            the request info
	 * @throws DefectException
	 *             the defect exception
	 */
	private void delete(RequestInfo requestInfo) throws DefectException {
		log.info("Start - delete");
		createInstance();
		if (CommonUtil.isNull(requestInfo)) {
			log.error("Request Info is null - " + requestInfo);
			throw new DefectException("failed to create, request information is missing");
		}
		try {

			String ref = getReference(requestInfo);

			DeleteRequest deleteRequest = new DeleteRequest(ref);
			DeleteResponse deleteResponse = null;

			try {
				deleteResponse = restApi.delete(deleteRequest);
			} catch (IOException e) {
				log.error("failed to delete, message : " + e.toString() + "RequestInfo -" + requestInfo);
				throw new DefectException("failed to delete, message : " + e.toString());
			}

			if (deleteResponse.wasSuccessful()) {
				log.info("Deleted");
			} else {

				log.error("Error in update : ");
				List<String> lstResult = new ArrayList<String>();
				for (String err : deleteResponse.getErrors()) {
					log.error("\t" + err);
					lstResult.add(err);
				}
				log.error("Error in deleting : " + lstResult);
				throw new DefectException("Error in deleting" + lstResult);
			}
		} finally {
			closeDefect();
			log.info("End - delete");
		}
	}

	/**
	 * Returns reference of user, workspace , project, tags.
	 * 
	 * @param requestInfo
	 *            the request info
	 * @return reference of user, workspace , project, tags
	 * @throws DefectException
	 *             the defect exception
	 */
	private String getReference(RequestInfo requestInfo) throws DefectException {
		log.info("Start - getReference");
		if (CommonUtil.isNull(requestInfo)) {
			log.error("Request Info is null - " + requestInfo);
			throw new DefectException("failed to read, request information is missing");
		}
		if (CommonUtil.isNull(restApi))
			createInstance();

		String ref = null;
		try {

			log.info("Getting Query Response");

			QueryResponse qryResponse = getQueryResponse(requestInfo);

			log.info("Got Query Response : " + qryResponse);

			JsonArray qryResults = qryResponse.getResults();
			JsonElement qryElement = qryResults.get(0);
			JsonObject qryObject = qryElement.getAsJsonObject();
			ref = qryObject.get((requestInfo.getRefField() != null) ? requestInfo.getRefField() : RallyConstantsForScripting._REF).toString();

		} finally {
			closeDefect();
			log.info("End - getReference");
			log.debug("value of ref is" + ref);
		}

		return ref;
	}

	/**
	 * Returns query response based on query request.
	 * 
	 * @param qryRequestInfo
	 *            Query Request information is stored.
	 * @return query response
	 * @throws DefectException
	 *             the defect exception
	 */
	private QueryResponse getQueryResponse(RequestInfo qryRequestInfo) throws DefectException {

		if (CommonUtil.isNull(qryRequestInfo)) {
			log.error("failed to make query request, Query Request Info-" + qryRequestInfo);
			throw new DefectException("failed to make query request, required query request info is missing");
		}

		// Request type can be User,defect etc.
		QueryRequest qryRequest = new QueryRequest(qryRequestInfo.getRequestType());

		log.info("Request Type : " + qryRequestInfo.getRequestType());

		qryRequest.setFetch(new Fetch(qryRequestInfo.getFetch()));
		try {
			if (qryRequestInfo.getQueryFilter() != null) {
				QueryFilter qfilter = null;
				ArrayList<String> filterList = qryRequestInfo.getQueryFilter();
				if (!filterList.isEmpty() && filterList.size() >= 3) {
					if ((filterList.get(2) == null) || (filterList.get(2).length() == 0)) {
						filterList.set(2, "null");
					}
					qfilter = new QueryFilter(filterList.get(0), filterList.get(1), filterList.get(2));
					for (int i = 3; i + 3 < filterList.size(); i = i + 4) {
						if (filterList.get(i) == "AND") {
							qfilter = QueryFilter.and(qfilter, new QueryFilter(filterList.get(i + 1), filterList.get(i + 2), filterList.get(i + 3)));
						} else if (filterList.get(i) == "OR") {
							qfilter = QueryFilter.or(qfilter, new QueryFilter(filterList.get(i + 1), filterList.get(i + 2), filterList.get(i + 3)));
						} else {
							throw new DefectException("Invalid query filter string.");
						}
					}
				}
				qryRequest.setQueryFilter(qfilter);
			}
		} catch (IndexOutOfBoundsException e) {
			log.error(e);
		} catch (NullPointerException e) {
			log.error(e);
		}
		if (qryRequestInfo.getQueryOrder() != null) {
			qryRequest.setOrder(convertMapToString(qryRequestInfo.getQueryOrder()));
		}

		if (qryRequestInfo.getPageSize() != -1)
			qryRequest.setPageSize(qryRequestInfo.getPageSize());

		if (qryRequestInfo.getLimit() != -1)
			qryRequest.setPageSize(qryRequestInfo.getLimit());

		QueryResponse qryResponse = null;
		try {
			qryResponse = restApi.query(qryRequest);
		} catch (IOException e) {
			log.error("failed to query , message : " + e.toString() + " Query Response -" + qryResponse);
			throw new DefectException("failed to query , message : " + e.toString());
		}

		return qryResponse;
	}

	/**
	 * Connect to rally instance.
	 * 
	 * @throws DefectException
	 *             the defect exception
	 */
	private void createInstance() throws DefectException {
		try {

			String username = TestBedManager.INSTANCE.getDefectConfig().getUsername();

			TafPassword tfp = TestBedManager.INSTANCE.getDefectConfig().getPassword();
			tfp.decryptPassword(tfp.getEncrypted());
			String password = tfp.getDecrypted();
			restApi = new RallyRestApi(new URI(rallyInfo.getUrl()), username, password);
		} catch (URISyntaxException e) {
			log.error("failed to connect to rally instance, message : " + e.toString() + "RallyInfo -" + rallyInfo);
			throw new DefectException("failed to connect to rally instance, message : " + e.toString());
		}
		if (rallyInfo.getWsapiVersion() != null) {
			restApi.setWsapiVersion(rallyInfo.getWsapiVersion());
		}
		restApi.setApplicationName(rallyInfo.getAppName());

	}

	/**
	 * Convert map to json.
	 * 
	 * @param entry
	 *            the entry
	 * @return the json object
	 */
	private JsonObject convertMapToJson(Map<String, String> entry) {
		JsonObject jsonObj = new JsonObject();
		Set<String> keySet = entry.keySet();

		for (String key : keySet) {
			jsonObj.addProperty(key, entry.get(key));
		}
		return jsonObj;
	}

	/**
	 * Convert map to string.
	 * 
	 * @param map
	 *            the map
	 * @return the string
	 */
	private String convertMapToString(Map<String, String> map) {
		String str = "";
		Set<String> keySet = map.keySet();
		int count = 1;
		for (String key : keySet) {
			str = str + key + " " + map.get(key);
			if (keySet.size() != count) {
				str = str + ",";
			}
			count++;
		}
		return str;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.etouch.taf.tools.defect.IDefectManager#createAJiraDefectBuilder(java
	 * .lang.String, java.lang.String, java.lang.String, java.lang.String,
	 * java.lang.String)
	 */
	public void createAJiraDefectBuilder(TestParameters inputs, String errMsg, String url, String issueUrl, String username, String password, String keys) {

	}

}