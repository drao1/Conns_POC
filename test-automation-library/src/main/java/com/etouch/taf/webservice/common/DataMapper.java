/**
 * 
 */
package com.etouch.taf.webservice.common;

import java.util.Map;

import org.apache.http.HttpResponse;

/**
 * @author etouch
 *
 */
public interface DataMapper {

	public <T> T format(Map<String, Object> inputData) throws Exception;
	
	public Map<String, ResponseData> validate(HttpResponse responseObj, Map<String, Object> expected) throws Exception;
}
