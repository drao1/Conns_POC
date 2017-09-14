package com.etouch.taf.webservice.validator;

import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;

import com.etouch.taf.core.exception.APIValidationException;

public interface IWebServiceValidator {

	public Object parse(HttpResponse response ) throws APIValidationException;
	
	public Map<String, Boolean> validate(Object responseObj, Map<String, Object> expected) throws APIValidationException;
	
	public Map<String, Object> readProperties(Object responseObj, List<String> propertyList) throws APIValidationException;
	
}
