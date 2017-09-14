package com.etouch.taf.webservice.client;

import java.util.List;
import java.util.Map;

import com.etouch.taf.core.exception.APIValidationException;
import com.etouch.taf.webservice.common.ResponseData;

public interface ITafWebServiceClient {

	public Map<String, ResponseData> validateAPIResponse(String requestType, String url, boolean authenticationFlag, String inputDataType, String inputData,
			Map<String, Object> expected) throws APIValidationException;

	public Map<String, Object> readAPIResponse(String requestType, String url, boolean authenticationFlag, String inputDataType, String inputData, List<String> propertyList)
			throws APIValidationException;

}
