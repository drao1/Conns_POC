package com.etouch.taf.core.listener;

import java.util.Properties;

import org.apache.commons.logging.Log;
import org.testng.ISuite;
import org.testng.ISuiteListener;

import com.etouch.taf.util.LogUtil;

/**
 * The listener interface for receiving suite events. The class that is
 * interested in processing a suite event implements this interface, and the
 * object created with that class is registered with a component using the
 * component's <code>addSuiteListener<code> method. When the suite event occurs,
 * that object's appropriate method is invoked.
 *
 * @author eTouch Systems Corporation
 */

public class SuiteListener implements ISuiteListener {

	/** The log. */
	static Log log = LogUtil.getLog(SuiteListener.class);

	/** The page ur ls. */
	public static final Properties pageURLs = null;

	/** The rally property file. */
	public static final Properties rallyPropertyFile = null;

	/** The is initialize. */
	static boolean isInitialize = false;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.testng.ISuiteListener#onStart(org.testng.ISuite)
	 */
	@Override
	public void onStart(ISuite arg0) {
		log.info("Suite Name :" + arg0.getName() + " - Start");

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.testng.ISuiteListener#onFinish(org.testng.ISuite)
	 */
	@Override
	public void onFinish(ISuite arg0) {

		log.info("Suite Name :" + arg0.getName() + " - End");

		log.info("********Results*******");

	}

}
