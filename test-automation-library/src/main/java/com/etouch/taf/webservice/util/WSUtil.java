package com.etouch.taf.webservice.util;

import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;

import com.etouch.taf.util.LogUtil;
import com.etouch.taf.webservice.common.ResponseData;

public class WSUtil {
	
	private static Log log = LogUtil.getLog(WSUtil.class);
	
	public static final String HTTP_GET = "get";
	public static final String HTTP_POST = "post";
	
	public static final String XML_TYPE = "application/xml";
	public static final String JSON_TYPE = "application/json";
	public static final String TEXT_TYPE = "text/html";
	
	public static final String COMPLETE = "complete"; 	
	
	public static final String REST_TYPE = "Rest";
	public static final String SOAP_TYPE = "Soap";

	
	public static void printResponseMap(Map<String, ResponseData> responseMap){
		
		if( ( responseMap != null ) && (responseMap.size() > 0) ){
			
			for( Map.Entry<String, ResponseData> resultMap : responseMap.entrySet()){
				log.debug("["+resultMap.getKey()+"] Available ==> "+
							resultMap.getValue().isAvailable()+" :: value ==> "+
							resultMap.getValue().getValue()+" :: Description ==>"+
							resultMap.getValue().getDescription());
			}
		}
		else{
			log.debug("Response Map is EMPTY");
		}
	}
	
	public static void printResults(Map<String, Object> expectedMap,
			Map<String, Boolean> resultantMap) {
		
		if( resultantMap != null && !resultantMap.isEmpty()){
			for(Map.Entry<String, Object> expected: expectedMap.entrySet()){
				log.debug(" Key '"+expected.getKey()+"' expected value ==> "+expected.getValue()+" :: Found ==> "+resultantMap.get(expected.getKey()));
			}
		}
		else {
			log.debug("No result found...");
		}
		
	}

	public static void printResults(List<String> expectedList,
			Map<String, Boolean> resultantMap) {
		if( resultantMap != null && !resultantMap.isEmpty()){
			
			for(String key : expectedList){
				log.debug("Key '"+key+"' found ==> "+resultantMap.get(key) );
			}
		}
		else {
			log.debug("No result found...");
		}
	}
}
