package com.etouch.taf.kd.config;

 
/**
 * 
 *  
 *
 */
public enum TestSuiteHeaderElement implements Comparable<TestSuiteHeaderElement> {
  
		TESTACTIONNAME(1), 
		INDEX(2), 
		PREACTION(3), 
		ELEMENTNAME(4),
		ELEMENTIDENTIFICATIONKEY(5),
		ELEMENTIDENTIFICATIONVALUE(6),
		ACTION(7),
		TESTDATA(8),
		POSTACTION(9),
		TESTGROUP(10),
		TESTRUN(11),
		ENDTESTACTIONNAME(12);
		 
		private final int rank;

		private TestSuiteHeaderElement(int rank) {
		  this.rank = rank;
		}

		 public int getRank() {
			 return rank;
		 }
		 
		 
	}
	
 


 
	
 