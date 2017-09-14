/*
 * 
 */
package com.etouch.taf.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.commons.logging.Log;


/**
 * The Class PropertyReader.
 */
public class PropertyReader {

	/** The object rep. */
	public Properties objectRep;

	private static Log log = LogUtil.getLog(PropertyReader.class);

	/**
	 * Gets the object rep.
	 * 
	 * @param fileName
	 *            the file name
	 * @return the object rep
	 */
	public Properties getObjectRep(String fileName) {
		if (objectRep == null) {
			objectRep = createRepository(fileName);

		}
		return objectRep;
	}

	/**
	 * Creates the repository.
	 * 
	 * @param fileName
	 *            the file name
	 * @return the properties
	 */
	private Properties createRepository(String fileName) {
		objectRep = new Properties();
		InputStream ip = null;
		try {
			ip = new FileInputStream(fileName);

			objectRep.load(ip);
		} catch (FileNotFoundException e) {
			
			log.debug("FileNotFoundException", e);
		} catch (IOException e) {
			
			log.debug("IOException", e);
		}

		return objectRep;

	}

}
