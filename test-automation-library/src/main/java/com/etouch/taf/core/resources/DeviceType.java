/*
 * 
 */
package com.etouch.taf.core.resources;


/**
 * The Enum DeviceType.
 */
public enum DeviceType {

	/** The Device. */
	DEVICE("Device"),
	
	/** The Emulator. */
	EMULATOR("Emulator"),
	
	/** The Simulator. */
	SIMULATOR("Simulator"),
	
	/** The i phone. */
	IPHONE("iPhone"),
	
	/** The i pad. */
	IPAD("iPad");
	
	/** The device type. */
	private String deviceType;
	
	/**
	 * Instantiates a new device type.
	 *
	 * @param type the type
	 */
	private DeviceType(String type){
		this.deviceType=type;
	}
	
	/**
	 * Gets the device type.
	 *
	 * @return the device type
	 */
	public String getDeviceType(){
		return deviceType;
	}
}
