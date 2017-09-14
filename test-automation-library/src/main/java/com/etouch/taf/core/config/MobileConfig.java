package com.etouch.taf.core.config;

/**
 * The Class MobileConfig.
 */
public class MobileConfig extends TafConfig {
	
	private AppiumConfig appiumConfig;
		
		public AppiumConfig getAppiumConfig() {
			return appiumConfig;
		}
	
		public void setAppiumConfig(AppiumConfig appiumConfig) {
			this.appiumConfig = appiumConfig;
		}

}
