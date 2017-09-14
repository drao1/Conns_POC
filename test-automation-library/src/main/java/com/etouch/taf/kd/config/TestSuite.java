package com.etouch.taf.kd.config;

import java.util.ArrayList;

public class TestSuite {
	
	String name;
	
	ArrayList<TestAction> testActions = new ArrayList<TestAction>();

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the testActions
	 */
	public ArrayList<TestAction> getTestActions() {
		return testActions;
	}

	/**
	 * @param testActions the testActions to set
	 */
	public void setTestActions(ArrayList<TestAction> testActions) {
		this.testActions = testActions;
	}

}
