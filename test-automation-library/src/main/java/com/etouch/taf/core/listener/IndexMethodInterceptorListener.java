/**
 * 
 */
package com.etouch.taf.core.listener;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.testng.IMethodInstance;
import org.testng.IMethodInterceptor;
import org.testng.ITestContext;

import com.etouch.taf.core.TestActionExecutor;

/**
 * @author etouch
 *
 */
public class IndexMethodInterceptorListener implements IMethodInterceptor {

	/* (non-Javadoc)
	 * @see org.testng.IMethodInterceptor#intercept(java.util.List, org.testng.ITestContext)
	 */
	@Override
	public List<IMethodInstance> intercept(List<IMethodInstance> methods,
			ITestContext context) {
		
		Comparator<IMethodInstance> indexComparator = new Comparator<IMethodInstance>(){
			
			public int compare(IMethodInstance instance1, IMethodInstance instance2){
				TestActionExecutor testActionExecutor1 = (TestActionExecutor)instance1.getInstance();
				Double testActionIndex1 = testActionExecutor1.getTestAction().getIndex();
				
				TestActionExecutor testActionExecutor2 = (TestActionExecutor)instance2.getInstance();
				Double testActionIndex2 = testActionExecutor2.getTestAction().getIndex();
				
				if( testActionIndex1 < testActionIndex2  ) return -1;
				
				if( testActionIndex1 > testActionIndex2 ) return 1;
				
				return 0;
			}
		};

		Collections.sort(methods, indexComparator);
		
		return methods;
	}

}