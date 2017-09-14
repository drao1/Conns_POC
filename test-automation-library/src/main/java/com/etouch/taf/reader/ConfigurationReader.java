/*
 * 
 */
package com.etouch.taf.reader;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.logging.Log;
import org.yaml.snakeyaml.Yaml;

import com.etouch.taf.core.config.TestBedManagerConfiguration;
import com.etouch.taf.util.LogUtil;


/**
 * This Class ConfigurationReader, helps to read the yaml configuration file and
 * load the values as POJO class(TestBedManagerConfiguration) It is a Singleton
 * class.
 */
public class ConfigurationReader {

	static Log log = LogUtil.getLog(ConfigurationReader.class);

	/**
	 * Read config.
	 * 
	 * @param ipStream
	 *            the ip stream
	 * @return the test bed manager configuration
	 */
	public static TestBedManagerConfiguration readConfig(InputStream ipStream) {
		TestBedManagerConfiguration config = null;
		try {

			Yaml yaml = new Yaml();
			// read the config file and load it into TestBedManagerConfiguration

			config = yaml.loadAs(ipStream, TestBedManagerConfiguration.class);

		} catch (Exception e) {
			log.debug(" Error occured during file load " + e.getMessage());
			log.debug(e);
		} finally {
			// Close the ipStreams
			if (ipStream != null) {
				try {
					ipStream.close();
				} catch (IOException e) {
					
					log.debug("IOException", e);
				}
			}
		}
		return config;
	}
}
