package com.etouch.taf.util;

import java.io.IOException;
import java.util.Properties;

import org.apache.commons.logging.Log;

public class KDPropertiesUtil {

	private static KDPropertiesUtil instance = null;
	private Properties properties;

	private static Log log = LogUtil.getLog(KDPropertiesUtil.class);

	protected KDPropertiesUtil() throws IOException {

		properties = new Properties();
		properties.load(getClass().getClassLoader().getResourceAsStream("KD.properties"));

	}

	public static KDPropertiesUtil getInstance() {
		if (instance == null) {
			try {
				instance = new KDPropertiesUtil();
			} catch (IOException ioe) {
				log.debug("IOException", ioe);

			}
		}
		return instance;
	}

	public String getProperty(String key) {
		return properties.getProperty(key);
	}

}
