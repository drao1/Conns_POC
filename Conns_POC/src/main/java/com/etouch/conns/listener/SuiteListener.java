
/*
 * 
 */
package com.etouch.conns.listener;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.openqa.selenium.remote.server.DriverFactory;
import org.testng.ISuite;
import org.testng.ISuiteListener;
import org.testng.xml.XmlSuite;
import org.testng.xml.XmlTest;

import com.etouch.taf.core.driver.DriverManager;
import com.etouch.taf.core.TestBed;
import com.etouch.taf.core.TestBedManager;
import com.etouch.taf.core.config.AppConfig;
import com.etouch.taf.core.config.BrowserConfig;
import com.etouch.taf.core.config.DeviceConfig;
import com.etouch.taf.core.config.PlatformConfig;
import com.etouch.taf.core.config.TestBedConfig;
import com.etouch.taf.core.config.TestBedManagerConfiguration;
import com.etouch.taf.core.exception.DefectException;
import com.etouch.taf.core.exception.DriverException;
import com.etouch.taf.core.resources.TestTypes;
import com.etouch.taf.util.AppiumLauncher;
import com.etouch.taf.util.CommonUtil;
import com.etouch.taf.util.ConfigUtil;
import com.etouch.taf.util.FileUtils;
import com.etouch.taf.util.LogUtil;
import com.etouch.taf.webui.ITafElementFactory;
import com.etouch.taf.webui.MobileElementFactory;
import com.etouch.taf.webui.TafElementFactoryManager;
import com.etouch.taf.webui.WebElementFactory;


/**
 * The listener interface for receiving suite events.
 * The class that is interested in processing a suite
 * event implements this interface, and the object created
 * with that class is registered with a component using the
 * component's <code>addSuiteListener<code> method. When
 * the suite event occurs, that object's appropriate
 * method is invoked.
 *
 * @author eTouch Systems Corporation
 */

public class SuiteListener implements ISuiteListener {
	
	/** The log. */
	static Log log = LogUtil.getLog(SuiteListener.class);
	
	/** The page ur ls. */
	static public Properties pageURLs = null;
	
	/** The rally property file. */
	static public Properties rallyPropertyFile = null;
	
	/** The is initialize. */
	static boolean isInitialize=false;
	
    /* (non-Javadoc)
     * @see org.testng.ISuiteListener#onStart(org.testng.ISuite)
     */
    public void onStart(ISuite arg0) {
    	log.info("Suite Name :"+ arg0.getName() + " - Start");
    	
    }
   
    /* (non-Javadoc)
     * @see org.testng.ISuiteListener#onFinish(org.testng.ISuite)
     */
    public void onFinish(ISuite arg0) {
    	log.info("Suite Name :"+ arg0.getName() + " - End");
    	log.info("********Results*******");    
    }

    
}

