package com.etouch.taf.core.config;

import org.apache.commons.logging.Log;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

import com.etouch.taf.util.LogUtil;
import com.etouch.taf.webui.selenium.WebPage;

/**
 * 
 * @author etouch
 *
 */
@Aspect("percflow(execution(java.lang.String com.etouch.taf.kd.config.ElementIdentifier.get(..)))")
public class FluentWaitAssertAspectConfig {

	static Log log = LogUtil.getLog(FluentWaitAssertAspectConfig.class);
	
	@Pointcut("execution(java.lang.String com.etouch.taf.kd.config.ElementIdentifier.get(..))")
	public void elementIdentifierGetMethod() {}

	@Pointcut("elementIdentifierGetMethod()")
	public void fluentWaitOnGet() {}
	
	@Around("fluentWaitOnGet() && args(webPage, elementIdentifier, ..)")
	public String assertiveFluentWaitAspect(ProceedingJoinPoint joinPoint, WebPage webPage, String elementIdentifier) throws Throwable{
		
		String assertActualValue = null; 
		
		try{
			
			log.debug("["+this.hashCode()+"] Asserting for action ==>"+elementIdentifier);
			
			CustomFluentWaitConfig cFluentWaitConfig = new CustomFluentWaitConfig();
			
			// Wait till the pageLoad completes
			boolean isPageLoadComplete = cFluentWaitConfig.verifyPageLoadCompletes(webPage.getDriver());
			
			if(isPageLoadComplete){
				log.debug("["+this.hashCode()+"] Page load Completed!");
				assertActualValue = (String)joinPoint.proceed();
			}
		}
		catch(Exception exp){
			log.error("Exception occurred ==>"+ exp.getCause());
			throw exp;
		}
		
		return assertActualValue;
	}
}
