package com.etouch.taf.core.config;

import org.apache.commons.logging.Log;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

import com.etouch.taf.core.TestActionExecutor;
import com.etouch.taf.core.resources.KDConstants;
import com.etouch.taf.tools.defect.DefectToolManager;
import com.etouch.taf.util.LogUtil;

/**
 * @author etouch
 * 
 */
@Aspect("perthis(execution(public * com.etouch.taf.core.TestActionExecutor.execute(..)))")
public class AspectConfig {
	static Log log = LogUtil.getLog(AspectConfig.class);

	@Pointcut("execution(public * com.etouch.taf.core.TestActionExecutor.execute(..))")
	public void logTestAnnotatedMethod() {
	}

	@Pointcut("logTestAnnotatedMethod()")
	public void logDefect() {
	}
	
	private DefectToolManager defectToolManager;
	
	
	@Around("logDefect()")
	public void observeTestCaseExecution(ProceedingJoinPoint joinPoint) throws Throwable {

		try{
			
			beforeTestCaseExecution(joinPoint);

			joinPoint.proceed();
			
			afterTestCaseExecution(joinPoint);
		}
		catch( Exception throwableExp){
			//logging purpose only
			logErrorStackTrace(throwableExp);
			logDefectOnError(joinPoint, throwableExp);
			throw throwableExp;
		}
		finally{
			
			log.debug("Cleaning up temp files...");
			if( !defectToolManager.cleanTempFiles()){
				log.error("Error in deleting files...");
			}
			defectToolManager.getDefectTool().closeConnection();
		}
		
	}
	
	public void beforeTestCaseExecution(JoinPoint joinPoint) {
		log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> Monitoring execution for Defect management ...");
		
		TestActionExecutor testActionExecutor = (TestActionExecutor)joinPoint.getThis();
		
		try {
			defectToolManager = new DefectToolManager(testActionExecutor);
		} catch (Exception e) {
			
			log.error(e.getMessage(),e);
		}
		
		//Starting the Video recording
		defectToolManager.startRecording();
	}
	
	public void afterTestCaseExecution(JoinPoint joinPoint)throws Exception {
		 
		//Stopping the video recording
		defectToolManager.stopRecording();
		
		//Closing defect if Open when No error occurred
		
			defectToolManager.manageDefect(KDConstants.EXECUTION_PASSED.getValue());
		
		
		log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> Completed Monitoring execution ...");
	}
	
	public void logDefectOnError(JoinPoint joinPoint, Throwable exception)throws Exception  {
		log.debug(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> Error occured...");
		
		//Stopping the video recording
		defectToolManager.stopRecording();
		
		//Managing defect when Error occurred
		defectToolManager.setException(exception);
		
			defectToolManager.manageDefect(KDConstants.EXECUTION_FAILED.getValue());
		
		
		log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> Completed Logging defect for this execution ...");
			
	}
	
	/**
	 * @param joinPoint 
	 * @param exception
	 */
	private void logErrorStackTrace(Throwable exception) {
		
		log.error("Exception message ==>"+exception.getMessage());
		log.error("Exception cause ==>"+exception.getCause());
		
		StackTraceElement[] stackTraceElements = exception.getStackTrace();
		
		StringBuilder stackTraceBuffer = new StringBuilder();
		for(StackTraceElement stackTraceElement : stackTraceElements ){
			stackTraceBuffer.append("\t\t\t"+stackTraceElement.toString()+"\n");
		}
		
		log.error(stackTraceBuffer.toString());
	}

}
