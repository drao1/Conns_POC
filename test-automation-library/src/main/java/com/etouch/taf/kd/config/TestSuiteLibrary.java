package com.etouch.taf.kd.config;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.TreeSet;

import org.apache.commons.logging.Log;

import com.etouch.taf.util.LogUtil;

 
/**
 *  
 *
 */
public class TestSuiteLibrary {
 
	static Log log = LogUtil.getLog(TestSuiteLibrary.class);
	
	// testSuite header fields
	private TreeSet<TestSuiteHeaderElement> testSuiteHeaderElements; 
	
	private static TestSuiteLibrary instance = null;
	   protected TestSuiteLibrary() {
	      // Exists only to defeat instantiation.
	   }
	   public static TestSuiteLibrary getInstance() {
	      if(instance == null) {
	         instance = new TestSuiteLibrary();
	      }
	      return instance;
	   }
 
	/**
	 * Get list of TestSuiteHeader
	 * 
	 * @return
	 */
	public TreeSet<TestSuiteHeaderElement> getTestSuiteHeaderElements() {
		if ( testSuiteHeaderElements == null) {
			final List<TestSuiteHeaderElement> elementList = Arrays.asList(TestSuiteHeaderElement.values());
			testSuiteHeaderElements = new TreeSet<TestSuiteHeaderElement>();
			testSuiteHeaderElements = new TreeSet<TestSuiteHeaderElement>(new TestSuiteHeaderElementComparator());
			testSuiteHeaderElements.addAll(elementList);
		}
		return testSuiteHeaderElements;
	}
	
	 
	/**
	 * Force the header in the restricted sequence order
	 * 
	 *
	 */
	private class TestSuiteHeaderElementComparator implements Comparator<TestSuiteHeaderElement> {

		 @Override
		 public int compare(TestSuiteHeaderElement o1, TestSuiteHeaderElement o2) {
			 return o1.getRank() - o2.getRank();
		 }
	}
 
}


 
	
 