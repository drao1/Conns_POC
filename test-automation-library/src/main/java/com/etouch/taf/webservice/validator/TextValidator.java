package com.etouch.taf.webservice.validator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;

import com.etouch.taf.core.exception.APIValidationException;

public class TextValidator implements IWebServiceValidator {
	
	@Override
	public Object parse(HttpResponse response) {
		return response;
		
		
	}

	@Override
	public Map<String, Boolean> validate(Object responseObj, Map<String, Object> expected) {
		
		return null;
	}

	public static Object buildTextResponse(String content) {
		
		return null;
	}

	public static HashMap<String, Boolean> validateProperties(Object object,
			HashMap<String, Object> expectedPropertyMap) {
		
		return null;
	}

	public static boolean validateTextResponse(Object object,
			String expectedFilePath) {
		
		return false;
	}

	@Override
	public Map<String, Object> readProperties(Object responseObj,
			List<String> propertyList) throws APIValidationException {
		
		return null;
	}

	

}
