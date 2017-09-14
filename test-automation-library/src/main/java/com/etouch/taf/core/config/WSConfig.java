package com.etouch.taf.core.config;

/**
 * The Class TestngConfig.
 */
public class WSConfig {

	private String wsType;

	private WSAuthConfig wsAuth;

	public String getWsType() {
		return wsType;
	}

	public void setWsType(String wsType) {
		this.wsType = wsType;
	}

	public WSAuthConfig getWsAuthConfig() {
		return wsAuth;
	}

	public void setWsAuthConfig(WSAuthConfig wsAuthConfig) {
		this.wsAuth = wsAuthConfig;
	}

}
