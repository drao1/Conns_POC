package com.etouch.taf.core.config;

public class WSAuthConfig {
	
	private String authType;
	
	private String authURI;
	
	private String authKey;
	
	private String authMethod;

	private String inputDataType;

	private String inputData;

	public String getAuthType() {
		return authType;
	}

	public void setAuthType(String authType) {
		this.authType = authType;
	}

	public String getAuthURI() {
		return authURI;
	}

	public void setAuthURI(String authURI) {
		this.authURI = authURI;
	}

	public String getAuthKey() {
		return authKey;
	}

	public void setAuthKey(String authKey) {
		this.authKey = authKey;
	}

	public String getAuthMethod() {
		
		return authMethod;
	}
	
	public void setAuthMethod(String authMethod) {
		this.authMethod = authMethod;
	}

	public String getInputDataType() {
		
		return inputDataType;
	}

	public String getInputData() {
		
		return inputData;
	}

	public void setInputDataType(String inputDataType) {
		this.inputDataType = inputDataType;
	}

	public void setInputData(String inputData) {
		this.inputData = inputData;
	}

}
