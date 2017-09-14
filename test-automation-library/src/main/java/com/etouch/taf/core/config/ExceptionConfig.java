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

@Aspect("perthis(execution(public * com.etouch.taf.core.driver.DriverManager.createDriver(..)))")
public class ExceptionConfig {
	static Log log = LogUtil.getLog(ExceptionConfig.class);

	
		@Pointcut("execution(public * com.etouch.taf.core.driver.DriverManager.createDriver(..))")
		public void logException() {
		}

		@Pointcut("logException()")
		public void logAndNotify() {
		}
		
		private DefectToolManager defectToolManager;
		
		
		@Around("logAndNotify()")
		public void observeExecution(ProceedingJoinPoint joinPoint) throws Throwable {

			try{
				
				beforeException(joinPoint);

				joinPoint.proceed();
				
				afterException(joinPoint);
			}
			catch( Exception throwableExp){
				//logging purpose only
				System.out.println(throwableExp.getMessage());
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
		
		public void beforeException(JoinPoint joinPoint) {
			
			System.out.println(" before");
		}
		
		public void afterException(JoinPoint joinPoint)throws Exception {
			 
			System.out.println(" Afffftttter");
		}
}
