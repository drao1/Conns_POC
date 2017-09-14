package com.etouch.taf.kd.config;

/**
 * 
 *  
 *
 */
public enum ElementIdentifyKeyLibrary {
	ID, XPATH, LINK, NAME, URL, GETCURRENTURL, WINDOW, METHODINVOKE, CSS, RESIZE, PAGETITLE, PARTIALLINK, TAG, MULTIVALUEXPATH, MULTIVALUECSS, MULTIVALUELINK, MULTIVALUEPARTIALLINK, MULTIVALUECLASS, MULTIVALUETAG;

	public static boolean contains(String key) {
		try {

			if (key.equalsIgnoreCase("class")) {
				return true;
			} else {
				ElementIdentifyKeyLibrary.valueOf(key.toUpperCase());
				return true;
			}

		} catch (Exception e) {
			return false;
		}
	}
}
