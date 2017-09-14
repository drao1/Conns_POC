/**
 * 
 */
package com.etouch.taf.kd.config;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.testng.annotations.Factory;
import org.testng.annotations.Parameters;

import com.etouch.taf.core.KDManager;
import com.etouch.taf.core.TestActionExecutor;
import com.etouch.taf.core.config.TestBedManagerConfiguration;
import com.etouch.taf.kd.exception.DuplicateDataKeyException;
import com.etouch.taf.kd.exception.InvalidActionException;
import com.etouch.taf.kd.exception.InvalidValueException;
import com.etouch.taf.util.LogUtil;

/**
 * <p>
 * Creates object of TestActionExecutor classes.
 * Each object is configured with individual Test Cases from the TestSuites.
 * Also Each of these object has only One DataSet record ( if DataSet is required )
 * </p>
 * 
 * @author eTouch Systems Corporation
 *
 */
public class KDTestActionFactory {

	private static Log log = LogUtil.getLog(KDTestActionFactory.class);

	/**
	 * @param testSuiteName
	 * @param testBedName
	 * 
	 * @return Instances array of TestActionExecutor class
	 * 
	 * @throws InvalidFormatException
	 * @throws IOException
	 * @throws InvalidActionException
	 * @throws InvalidValueException
	 * @throws DuplicateDataKeyException
	 */
	@Parameters({ "testSuiteName", "testBedName" })
	@Factory
	public Object[] createInstances(String testSuiteName, String testBedName)
			throws InvalidFormatException, IOException, InvalidActionException,
			InvalidValueException, DuplicateDataKeyException {

		List<TestAction> testActionLists = getTestAction(testSuiteName);

		List<TestActionExecutor> testActionExecutorLists = new ArrayList<TestActionExecutor>();

		for (TestAction testAction : testActionLists) {
			if (testAction.getKdDataSetColls() != null
					&& testAction.getKdDataSetColls().size() > 0) {

				for (KDDataset kdDataSet : testAction.getKdDataSetColls()) {

					TestActionExecutor actionExecutor = new TestActionExecutor(
							testBedName, testSuiteName, testAction, kdDataSet);
					testActionExecutorLists.add(actionExecutor);
				}
			} else {

				TestActionExecutor actionExecutor = new TestActionExecutor(
						testBedName, testSuiteName, testAction, null);
				testActionExecutorLists.add(actionExecutor);
			}

		}

		return testActionExecutorLists.toArray();
	}

	private List<TestAction> getTestAction(String testSuiteName)
			throws InvalidFormatException, IOException, InvalidActionException,
			InvalidValueException, DuplicateDataKeyException {

		List<TestAction> executableTestActionList = new ArrayList<TestAction>();

		Collection<TestSuite> testSuiteColls = KDManager.getInstance()
				.getTestSuites();

		for (TestSuite testSuite : testSuiteColls) {
			if (testSuite.getName().equals(testSuiteName)) {

				List<TestAction> testActionList = testSuite.getTestActions();

				for (TestAction testAction : testActionList) {

					// Check whether the TestRun is "Yes" then add it
					if (testAction.isExecute) {
						if(isValidTestGroup(testAction)){
							executableTestActionList.add(testAction);
						}
					}
				}

				break;
			}
		}

		return executableTestActionList;
	}
	
	
	
	private boolean isValidTestGroup(TestAction testAction){
		
		 boolean resultValue=false;
		 String testGroupName= TestBedManagerConfiguration.getInstance().getTestngConfig().getGroupName();
		 if(testGroupName==null){
			 return true;
		 }
		 LinkedList<String> grpList= testAction.getTestGroup();
		 
		 if((grpList!=null ) && (grpList.contains(testGroupName.trim())) ){
			 resultValue=true;
		 }
		
		return resultValue;
		
	}

}
