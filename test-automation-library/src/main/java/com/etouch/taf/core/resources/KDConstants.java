package com.etouch.taf.core.resources;

public enum KDConstants {

	TD_EXECUTION_LABEL("Run"),
	TD_LABEL("Label"),
	EXECUTE("Y"),
	DONT_EXECUTE("N"),
	TESTS("tests"),
	INSTANCES("instances"),
	PARALLEL("parallel"),
	PARALLEL_TESTBEDS("testbeds"),
	PARALLEL_TESTSUITES("testsuites"),
	PARALLEL_TESTCASES("testcases"),
	PARALLEL_FALSE("false"),
	TS_SHEETNAME_LIBRARY_POSTFIX("_Library"),
	TS_SHEETNAME_POSTFIX("_Suite"),
	TD_SHEETNAME_POSTFIX("_Data"),
	TS_SHEETNAME_KEYWORDLIBRARY("eTaapKeywordLibrary"),
	TD_SHEETNAME_GLOBAL_POSTFIX("_Global_Data"),
	TS_SHEETNAME_COMMON("Common_Test"),
	TB_SHEETNAME_COMMON("Common_TestBed"),
	TS_SUITENAME("_Suite_"),
	TS_TESTNAME("_Test_"),
	EXECUTION_PASSED("Passed"),
	EXECUTION_FAILED("Failed"),
	UPDATE_DEFECT("Update"),
	CLOSE_DEFECT("Closed"),
	REOPEN_DEFECT("Reopen"), 
	NEW_DEFECT("New");
	
	/**
	 * @param value
	 */
	private KDConstants(String value) {
		this.value = value;
	}

	private String value;

	/**
	 * @return the value
	 */
	public String getValue() {
		return value;
	}

	
	
	
}
