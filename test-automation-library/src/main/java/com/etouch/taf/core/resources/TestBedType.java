
package com.etouch.taf.core.resources;

import org.apache.commons.logging.Log;

import com.etouch.taf.util.LogUtil;


/**
 * List the valid Browsers supported by the test framework.
 *
 * @author eTouch Systems Corporation
 * @version 1.0
 *
 */
public enum TestBedType {

	/** The Firefox. */
	FIREFOX("fireFox"),

	/** The Internet explorer. */
	INTERNETEXPLORER("InternetExplorer"),

	/** The Chrome. */
	CHROME("Chrome"),
	
	/** The Chrome. */
	CHROME1("Chrome1"),

	/** The Safari. */
	SAFARI("Safari"),

	// iOS Simulators
	/** The i phone native sim. */
	IPHONENATIVESIM("iPhoneNativeSim"),

	/** The i pad native sim. */
	IPADNATIVESIM("iPadNativeSim"),

	/** The i phone web sim. */
	IPHONEWEBSIM("iPhoneWebSim"),

	/** The i pad web sim. */
	IPADWEBSIM("iPadWebSim"),

	//iOS real device
	/** The i phone native. */
	IPHONENATIVE("iPhoneNative"),

	/** The i pad native. */
	IPADNATIVE("iPadNative"),

	/** The i phone web. */
	IPHONEWEB("iPhoneWeb"),

	/** The i pad web. */
	IPADWEB("iPadWeb"),

	/** The i os. */
	IOS("iOS"),

    /** The Android native. */
    ANDROIDNATIVE("AndroidNative"),

	/** The Android web em. */
	ANDROIDWEBEM("AndroidWebEm"),

	/** The Android. */
	ANDROID("Android"),

	/** Micromax Device*/
	AndroidMicromax("AndroidMicromax"),

        /** Lenovo Device*/
	AndroidLenovo("AndroidLenovo"),

	/** MotoG Device*/
	AndroidMotoG("AndroidMotoG"),

	/** Motorola Device*/
	MOTOROLA("Motorola"),

	/** Karbonn Device*/
	KARBONN("Karbonn"),

	/** Lenovo Device*/
	LENOVO("Lenovo"),

	/** Micromax Device*/
	MICROMAX("Micromax"),

	/** Samsung Device*/
	AndroidSAMSUNG("AndroidSamsung"),

	/** Nexus Tab*/
	AndroidNexusTab("AndroidNexusTab"),
	
	/**  The Android Native Browser. */
	ANDROIDBROWSER("AndroidBrowser"),


	/**  The Android Chrome Browser. */
	ANDROIDCHROME("AndroidChrome"),


	/**  The Android Hybrid App */
	ANDROIDHYBRIDAPP("AndroidHybridApp"),

	ANDROID1("Android1"),

	IPADSIMULATOR("iPadSimulator");

	/** The log. */
	static Log log = LogUtil.getLog(TestBedType.class);

	/** The test bed name. */
	private String testBedName;

	/**
	 * Instantiates a new test bed type.
	 *
	 * @param testBedName the test bed name
	 */
	private TestBedType(String testBedName) {
		this.testBedName = testBedName;
	}

	/**
	 * Gets the test bed name.
	 *
	 * @return the test bed name
	 */
	public String getTestBedName() {
		return testBedName;
	}


	/**
	 * Checks if is supported.
	 *
	 * @param testBedType the platform name
	 * @return true, if is supported
	 */
	public static boolean isSupported(String testBedType){
		TestBedType[] testBedTypeList = TestBedType.values();
		for(TestBedType tbType : testBedTypeList){
			if(tbType.getTestBedName().equalsIgnoreCase(testBedType)){
				return true;
			}
		}
		return false;
	}


	@Override
	 /**
     * Returns the name of this enum constant, as contained in the
     * declaration.  This method may be overridden, though it typically
     * isn't necessary or desirable.  An enum type should override this
     * method when a more "programmer-friendly" string form exists.
     *
     * @return the name of this enum constant
     */
    public String toString() {
        return testBedName.toLowerCase();
    }

}




