package com.etouch.taf.kd.config;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @author etouch
 *
 */
public enum ResponseStatusCode {


	OK("200", "isSuccess"),
	CREATED("201", "isCreated"),
	ACCEPTED("202", "isAccepted"),
	PARTIAL_INFORMATION("203","isInformationPartial"),
	NO_RESPONSE("204","hasNoResponse"),
	BAD_REQUEST("400", "isBadRequest"),
	UNAUTHORIZED("401", "isUnauthorized"),
	PAYMENT_REQUIRED("402","isPaymentRequired"),
	FORBIDDEN("403","isForbidden"),
	NOT_FOUND("404","isNotFound"),
	INTERNAL_ERROR("500", "hasInternalError"),
	NOT_IMPLEMENTED("501", "hasNotImplemented"),
	SERVICE_OVERLOADED("502", "isServiceOverloaded"),
	GATEWAY_TIMEOUT("503","isGatewayTimeout"),
	REDIRECTED("301","isURLRedirected"),
	MOVED("302", "isURLMoved");
	
	private static final Map<String, String> statusCodeMethodMap = Collections.unmodifiableMap(initializeMapping());	

	private String responseCode;

	private String responseAssertMethod;

	/**
	 * @param responseCode
	 */
	ResponseStatusCode(String responseCode, String responseAssertMethod) {
		this.responseCode = responseCode;
		this.responseAssertMethod = responseAssertMethod;
	}

	/**
	 * @return the responseCode
	 */
	public String getResponseCode() {
		return responseCode;
	}
	
	
	/**
	 * @return the responseAssertMethod
	 */
	public String getResponseAssertMethod() {
		return responseAssertMethod;
	}

	
	private static Map<String, String> initializeMapping() {
		Map<String, String> methodMap = new HashMap<>();
		
		for(ResponseStatusCode responseStatusCode: ResponseStatusCode.values()){
			methodMap.put(responseStatusCode.responseAssertMethod, responseStatusCode.responseCode);
		}
		
	    return methodMap;
	}
	
	
	public static boolean contains(String responseAssertMethod){
		return statusCodeMethodMap.containsKey(responseAssertMethod);
	}
	
	public static String getResponseCode(String responseAssertMethod){
		return statusCodeMethodMap.get(responseAssertMethod);
		
	}
	
}
