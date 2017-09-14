package com.etouch.taf.util.report;

import java.io.IOException;
import java.io.InputStream;

import com.etouch.taf.util.CommonUtil;

/**
 * @author etouch
 *
 */
public enum ReportTemplate {
	
	HTML("htmlReport"),
	PDF("pdfReport"),
	EXCEL("excelReport"),
	DOC("docReport"),
	MAIL("emailReport"), 
	
	SKIPPED_TEST_CASES("skippedTestCaseTable"), 
	SKIPPED_TEST_SUITES("skippedSuites"), 
	EXECUTED_TEST_CASES("executedTestCase"), 
	EXECUTED_TEST_CASE_LIST("executedTestCaseList"),
	EXECUTED_TEST_SUITES("executedTestSuites"),
	FAILED_TEST_CASE_DETAILS("failTestCaseDetails"),
	FAILED_TEST_CASE_LISTS("failTestCaseLists"),
	SUMMARY("summary"),
	TEST_SUITE_SUMMARY("testSuiteSummary"),
	BROWSER_NAME("browserName"),
	BROWSER_STATUS("browserStatus"),
	BROWSER_SUMMARY("browserSummary"),
	
	EXCEL_ERRORS("excelErrors"),
	ERROR_MESSAGE_LIST("errorMessageList"),
	ERROR_MESSAGE("errorMessage");
	
	private final String reportFormat;
	private final String templateDir = "template/";
	private final String templateSuffix = ".template";
	
	/**
	 * @param template
	 */
	private ReportTemplate(String reportFormat) {
		this.reportFormat = reportFormat;
	}

	/**
	 * @return the reportFormat
	 */
	public String getReportFormat() {
		return reportFormat;
	}
	
	/**
	 * @return the template
	 * @throws IOException 
	 */
	public String getTemplate() throws IOException {
	
		InputStream templateStream = getClass().getClassLoader().getResourceAsStream(templateDir.concat(reportFormat).concat(templateSuffix));
		
		return CommonUtil.formatInputStream(templateStream);
	}


}
