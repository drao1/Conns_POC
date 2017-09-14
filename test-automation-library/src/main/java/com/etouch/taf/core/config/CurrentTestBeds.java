package com.etouch.taf.core.config;

import java.util.HashMap;
import java.util.LinkedHashMap;

import org.apache.commons.logging.Log;

import com.etouch.taf.core.TestBed;
import com.etouch.taf.core.driver.DriverBuilder;
import com.etouch.taf.core.driver.DriverManager;
import com.etouch.taf.core.exception.DriverException;
import com.etouch.taf.util.CommonUtil;
import com.etouch.taf.util.LogUtil;
import com.etouch.taf.util.TestBedUtil;

public class CurrentTestBeds {
	HashMap<String, TestBed> testBeds = null;
	int size = 0;

	private static Log log = LogUtil.getLog(CurrentTestBeds.class);

	public CurrentTestBeds() {
		testBeds = new LinkedHashMap<String, TestBed>();

		CommonUtil.sop(" A set of CurrentTestBeds is created ");
	}

	public int size() {
		return testBeds.size();
	}

	/**
	 * Returns <tt>true</tt> if this map contains no key-value mappings.
	 * 
	 * @return <tt>true</tt> if this map contains no key-value mappings
	 */
	public boolean isEmpty() {
		return testBeds.isEmpty();
	}

	/**
	 * Returns the value to which the specified key is mapped, or {@code null}
	 * if this map contains no mapping for the key.
	 * 
	 * <p>
	 * More formally, if this map contains a mapping from a key {@code k} to a
	 * value {@code v} such that {@code (key==null ? k==null :
	 * key.equals(k))}, then this method returns {@code v}; otherwise it returns
	 * {@code null}. (There can be at most one such mapping.)
	 * 
	 * <p>
	 * A return value of {@code null} does not <i>necessarily</i> indicate that
	 * the map contains no mapping for the key; it's also possible that the map
	 * explicitly maps the key to {@code null}. The {@link #containsKey
	 * containsKey} operation may be used to distinguish these two cases.
	 * 
	 * @see #put(Object, Object)
	 */
	public TestBed get(String testBedName) {
		TestBed testBed = testBeds.get(testBedName);
		if (testBed == null)
			testBed = TestBedUtil.loadTestBedDetails(testBedName);

		return testBed;
	}

	public Object createDriver(String testBedName) throws Exception {
		DriverBuilder db;
		Object driver = null;
		try {
			db = DriverManager.buildDriver(testBedName);
			driver = db.getDriver();
			CommonUtil.sop("**************Driver has been created*********");
		} catch (DriverException e) {

			log.debug("DriverException", e);
		}

		return driver;
	}

	/**
	 * Returns <tt>true</tt> if this map contains a mapping for the specified
	 * key.
	 * 
	 * @param key
	 *            The key whose presence in this map is to be tested
	 * @return <tt>true</tt> if this map contains a mapping for the specified
	 *         key.
	 */
	public boolean containsKey(Object key) {
		return testBeds.containsKey(key);
	}

	/**
	 * Associates the specified value with the specified key in this map. If the
	 * map previously contained a mapping for the key, the old value is
	 * replaced.
	 * 
	 * @param key
	 *            key with which the specified value is to be associated
	 * @param value
	 *            value to be associated with the specified key
	 * @return the previous value associated with <tt>key</tt>, or <tt>null</tt>
	 *         if there was no mapping for <tt>key</tt>. (A <tt>null</tt> return
	 *         can also indicate that the map previously associated
	 *         <tt>null</tt> with <tt>key</tt>.)
	 */
	public TestBed put(String key, TestBed value) {
		return testBeds.put(key, value);
	}

	/**
	 * Removes the mapping for the specified key from this map if present.
	 * 
	 * @param key
	 *            key whose mapping is to be removed from the map
	 * @return the previous value associated with <tt>key</tt>, or <tt>null</tt>
	 *         if there was no mapping for <tt>key</tt>. (A <tt>null</tt> return
	 *         can also indicate that the map previously associated
	 *         <tt>null</tt> with <tt>key</tt>.)
	 */
	public TestBed remove(String key) {
		return testBeds.remove(key);
	}

}
