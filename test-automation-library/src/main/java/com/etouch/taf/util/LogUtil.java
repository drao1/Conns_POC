/*
 * 
 */
package com.etouch.taf.util;

import java.io.File;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.xml.DOMConfigurator;


/**
 * This class contains log methods.
 * 
 * @author eTouch Systems Corporation
 * @version 1.0
 *
 */
public class LogUtil {

	/**
	 * Gets the log.
	 *
	 * @param className the class name
	 * @param fileName the file name
	 * @return the log
	 */
	public static Log getLog (Class<?> className, String fileName) {
		Log logger = LogFactory.getLog(className);
		//File myFile = new File(fileName);
		DOMConfigurator.configure(fileName);
		return logger;
	}
	
	/**
	 * This will create the log with the default file name log4j.xml
	 *
	 * @param className the class name
	 * @return the log
	 */
	public static Log getLog (Class<?> className) {
		return getLog(className, "log4j.xml");
	}
	
	/*
	 *Method for log4j formatting of the message
	 *
	 */
	/**
	 * Prints the log message.
	 *
	 * @param message the message
	 * @return the string
	 */
	public String printLogMessage(String message){
		return message;
	}
  
}
