package com.etouch.taf.kd.config;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;

public class TestAction {
	
	TestSuite suite;
	
	Double index = 0.0;
	
	String actionName;
	
	int startRow;
	
	int endRow;
	
	LinkedList<ActionElement> actionElements;
	
	KDDataset dataset;
	
	Collection<KDDataset> kdDataSetColls = new ArrayList<KDDataset>();
	
	LinkedList<String>  testGroup;
	
	boolean isExecute;
	
	public TestSuite getSuite() {
		return suite;
	}
	public void setSuite(TestSuite suite) {
		this.suite = suite;
	}
	
	/**
	 * @return the index
	 */
	public Double getIndex() {
		return index;
	}
	/**
	 * @param index the index to set
	 */
	public void setIndex(Double index) {
		this.index = index;
	}
	public String getActionName() {
		return actionName;
	}
	public void setActionName(String actionName) {
		this.actionName = actionName;
	}
	
	
	public KDDataset getDataset() {
		return dataset;
	}
	public void setDataset(KDDataset dataset) {
		this.dataset = dataset;
	}

	public boolean isExecute() {
		return isExecute;
	}
	public void setExecute(boolean isExecute) {
		this.isExecute = isExecute;
	}
	public int getStartRow() {
		return startRow;
	}
	public void setStartRow(int startRow) {
		this.startRow = startRow;
	}
	public int getEndRow() {
		return endRow;
	}
	public void setEndRow(int endRow) {
		this.endRow = endRow;
	}
	public LinkedList<ActionElement> getActionElements() {
		return actionElements;
	}
	public void setActionElements(LinkedList<ActionElement> actionElements) {
		this.actionElements = actionElements;
	}
	public LinkedList<String> getTestGroup() {
		return testGroup;
	}
	public void setTestGroup(LinkedList<String> testGroup) {
		this.testGroup = testGroup;
	}
	
	/**
	 * @return the kdDataSetColls
	 */
	public Collection<KDDataset> getKdDataSetColls() {
		return kdDataSetColls;
	}
	/**
	 * @param kdDataSetColls the kdDataSetColls to set
	 */
	public void setKdDataSetColls(Collection<KDDataset> kdDataSetColls) {
		this.kdDataSetColls = kdDataSetColls;
	}
	
}
