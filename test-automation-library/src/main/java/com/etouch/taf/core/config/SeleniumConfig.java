package com.etouch.taf.core.config;

public class SeleniumConfig {

	/** The instance. */
	public static SeleniumConfig INSTANCE;
	private static long defaultTimeOutWaitMsec = 20760;
	private static long defaultPolingTimeMsec = 138;
	private static int defaultpollingCount = 50;
	private static long defaultimplicitWait = 10000;
	private static int defaultMaxAttempt = 20;

	private SeleniumConfig() {

	}

	/**
	 * Gets the single instance of TestBedManagerConfiguration.
	 * 
	 * @return single instance of TestBedManagerConfiguration
	 */
	public static SeleniumConfig getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new SeleniumConfig();

		}

		return INSTANCE;
	}

	public static long getDefaultTimeOutWaitMsec() {
		return defaultTimeOutWaitMsec;
	}

	public static void setDefaultTimeOutWaitMsec(long defaultTimeOutWaitMsecValue) {
		defaultTimeOutWaitMsec = defaultTimeOutWaitMsecValue;
	}

	public static long getDefaultPolingTimeMsec() {
		return defaultPolingTimeMsec;
	}

	public static void setDefaultPolingTimeMsec(long defaultPolingTimeMsecValue) {
		defaultPolingTimeMsec = defaultPolingTimeMsecValue;
	}

	public static int getDefaultPollingCount() {
		return defaultpollingCount;
	}

	public static void setDefaultpollingCount(int defaultpollingCountValue) {
		defaultpollingCount = defaultpollingCountValue;
	}

	public static long getDefaultImplicitWait() {
		return defaultimplicitWait;
	}

	public static void setDefaultImplicitWait(long defaultimplicitWaitValue) {
		defaultimplicitWait = defaultimplicitWaitValue;
	}

	public static int getDefaultMaxAttempt() {
		return defaultMaxAttempt;
	}

	public static void setDefaultMaxAttempt(int defaultMaxAttemptValue) {
		defaultMaxAttempt = defaultMaxAttemptValue;
	}

}
