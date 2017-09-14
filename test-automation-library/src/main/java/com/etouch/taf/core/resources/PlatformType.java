/*
 * 
 */
package com.etouch.taf.core.resources;

import org.apache.commons.logging.Log;

import com.etouch.taf.util.LogUtil;


/**
 * The Enum PlatformType.
 */
public enum PlatformType {
	
	/** The Windows. */
	WINDOWS("Windows"),
	
	/** The Mac. */
	MAC("Mac"),
	
	/** The Linux. */
	LINUX("Linux"),
	
	/** The Unix. */
	UNIX("Unix"),
	
	/** The Android. */
	ANDROID("Android"),
	
	/** The Firefox os. */
	FIREFOXOS("FirefoxOS"),
	
	/** The i os. */
	IOS("iOS");
    
	/** The log. */
	static Log log = LogUtil.getLog(PlatformType.class);
	
	/** The name. */
	private String name;

	/**
	 * Gets the name.
	 *
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Instantiates a new platform type.
	 *
	 * @param name the name
	 */
	private PlatformType(String name) {
		this.name = name;
	}
	
	/**
	 * Checks if is supported.
	 *
	 * @param platformName the platform name
	 * @return true, if is supported
	 */
	public static boolean isSupported(String platformName){
		PlatformType[] platformList = PlatformType.values();
		for(PlatformType platform : platformList){
			if(platform.getName().equalsIgnoreCase(platformName)){
				return true;
			}
		}
		return false;
	}

	
}
