package com.etouch.taf.webservice.client;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;

import com.etouch.taf.core.config.TestBedManagerConfiguration;
import com.etouch.taf.core.config.WSAuthConfig;
import com.etouch.taf.core.exception.APIValidationException;
import com.etouch.taf.core.exception.JSONParserException;
import com.etouch.taf.util.LogUtil;
import com.etouch.taf.webservice.common.DataMapper;
import com.etouch.taf.webservice.common.ResponseData;
import com.etouch.taf.webservice.type.DataType;
import com.etouch.taf.webservice.util.WSUtil;
import com.etouch.taf.webservice.validator.IWebServiceValidator;
import com.etouch.taf.webservice.validator.ParserFactory;

public class RestClient implements ITafWebServiceClient {

	// Input data shall be Map:: DataMapper.format(Map<String, Object>inputData)
	// needs to be used for converting Map into relevant input object.

	private static Log log = LogUtil.getLog(RestClient.class);

	@Override
	public Map<String, ResponseData> validateAPIResponse(String requestType, String url, boolean authenticationFlag, String inputDataType, String inputData,
			Map<String, Object> expectedMap) throws APIValidationException {

		Map<String, ResponseData> result = new HashMap<>();
		try {
			HttpResponse response = fetchResponse(requestType, url, authenticationFlag, inputDataType, inputData);

			String contentType = response.getEntity().getContentType().getValue();
			DataMapper dataMapper = DataType.getDataMapper(contentType);
			result = dataMapper.validate(response, expectedMap);

		} catch (Exception ex) {
			throw new APIValidationException(ex.getMessage());
		}

		return result;
	}

	@Override
	public Map<String, Object> readAPIResponse(String requestType, String url, boolean authenticationFlag, String inputDataType, String inputData, List<String> propertyList)
			throws APIValidationException {
		Map<String, Object> result = null;
		try {
			HttpResponse response = fetchResponse(requestType, url, authenticationFlag, inputDataType, inputData);
			IWebServiceValidator wsValidator = ParserFactory.getParser(response);
			Object responseObj = wsValidator.parse(response);
			result = wsValidator.readProperties(responseObj, propertyList);
		} catch (Exception ex) {
			throw new APIValidationException(ex.getMessage());
		}

		return result;
	}

	private HttpResponse fetchResponse(String requestType, String url, boolean authenticationFlag, String inputDataType, String inputData) throws APIValidationException,
			UnsupportedOperationException, JSONParserException {

		HttpResponse response = null;

		if (requestType.equalsIgnoreCase(WSUtil.HTTP_GET))
			response = executeGet(url, authenticationFlag);
		else if (requestType.equalsIgnoreCase(WSUtil.HTTP_POST))
			response = executePost(url, authenticationFlag, inputDataType, inputData);
		else
			throw new APIValidationException("HTTP Method: " + requestType + " is not supported");

		return response;

	}

	private HttpResponse executePost(String url, boolean authenticationFlag, String inputDataType, String inputData) {
		HttpResponse response = null;
		try {

			String authToken = "";

			// create HTTP Client
			HttpClient httpClient = HttpClientBuilder.create().build();

			// Create new getRequest with below mentioned URL
			HttpPost postRequest = new HttpPost(url);

			// Add additional header to getRequest which accepts application/xml
			// data
			postRequest.addHeader("accept", "application/xml");
			postRequest.addHeader("accept", "application/json");

			if (authenticationFlag == true) {
				authToken = fetchAuthenticationToken();

				// Add header
				postRequest.addHeader("Content-Type", "\"" + TestBedManagerConfiguration.INSTANCE.getWsConfig().getWsAuthConfig().getInputDataType() + "\";charset=UTF-8");
				postRequest.addHeader("Authorization", TestBedManagerConfiguration.INSTANCE.getWsConfig().getWsAuthConfig().getAuthType() + " " + authToken);

			}

			StringEntity input = new StringEntity(inputData);
			input.setContentType(inputDataType);
			postRequest.setEntity(input);

			// Execute your request and catch response
			response = httpClient.execute(postRequest);

		} catch (ClientProtocolException e) {
			log.debug("ClientProtocolException", e);

		} catch (IOException e) {
			log.debug("IOException", e);
		}

		return response;
	}

	private HttpResponse executeGet(String url, boolean authenticationFlag) {
		HttpResponse response = null;
		try {

			String authToken = "";

			// create HTTP Client
			HttpClient httpClient = HttpClientBuilder.create().build();

			// Create new getRequest with below mentioned URL
			HttpGet getRequest = new HttpGet(url);

			// Add additional header to getRequest which accepts application/xml
			// data
			getRequest.addHeader("accept", "application/xml");
			getRequest.addHeader("accept", "application/json");

			if (authenticationFlag == true) {
				authToken = fetchAuthenticationToken();

				// Add header
				getRequest.addHeader("Content-Type", "\"" + TestBedManagerConfiguration.INSTANCE.getWsConfig().getWsAuthConfig().getInputDataType() + "\";charset=UTF-8");
				getRequest.addHeader("Authorization", TestBedManagerConfiguration.INSTANCE.getWsConfig().getWsAuthConfig().getAuthType() + " " + authToken);

			}

			// Execute your request and catch response
			response = httpClient.execute(getRequest);

		} catch (ClientProtocolException e) {
			log.debug("ClientProtocolException", e);

		} catch (IOException e) {
			log.debug("IOException", e);
		}

		return response;
	}

	private String fetchAuthenticationToken() {

		WSAuthConfig authConfig = TestBedManagerConfiguration.INSTANCE.getWsConfig().getWsAuthConfig();

		String url = authConfig.getAuthURI();
		String authKey = authConfig.getAuthKey();
		String httpMethod = authConfig.getAuthMethod();
		String inputDataType = "";
		String inputData = "";

		if (inputData == null) {
			inputDataType = "";
			inputData = "";
		}

		String authToken = "";

		List<String> propertyList = new ArrayList<>();
		propertyList.add(authKey);

		try {
			Map<String, Object> resultMap = WebServiceClientManager.getWebServiceClient().readAPIResponse(httpMethod, url, false, inputDataType, inputData, propertyList);
			authToken = (String) resultMap.get(authKey);
		} catch (APIValidationException e) {

			log.debug("APIValidationException", e);
		}

		return authToken;

	}
}
