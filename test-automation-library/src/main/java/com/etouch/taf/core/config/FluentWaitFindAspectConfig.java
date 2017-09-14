package com.etouch.taf.core.config;


import org.apache.commons.logging.Log;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

import com.etouch.taf.util.LogUtil;
import com.etouch.taf.webui.ITafElement;
import com.etouch.taf.webui.selenium.WebPage;

/**
 * @author etouch
 *   
 */
@Aspect("percflow(execution(com.etouch.taf.webui.ITafElement com.etouch.taf.kd.config.ElementIdentifier.find(..)))")
public class FluentWaitFindAspectConfig {

	 static Log log = LogUtil.getLog(FluentWaitFindAspectConfig.class);
	
	@Pointcut("execution(com.etouch.taf.webui.ITafElement com.etouch.taf.kd.config.ElementIdentifier.find(..))")
	public void elementIdentifierFindMethod() {}

	@Pointcut("elementIdentifierFindMethod()")
	public void fluentWaitOnFind() {}
	
	@Around("fluentWaitOnFind() && args(webPage, elementIdentifier, ..)")
	public ITafElement fluentWaitFindAspect(ProceedingJoinPoint joinPoint, WebPage webPage, String elementIdentifier) throws Throwable{
		ITafElement iTafElement = null;
		
		try{
		
			log.debug("["+this.hashCode()+"] Finding for ==>"+elementIdentifier+" on the URL ==>"+webPage.getDriver().getCurrentUrl());
			
			
			/**
			 *  1 - Wait till the pageLoad completes
			 *  2 - Keep Executing ElementIdentifier for Finite time AFTER pageLoad Completes -- Throw ERROR if NOT FOUND
			 */
			
			CustomFluentWaitConfig cFluentWaitConfig = new CustomFluentWaitConfig();
			
			boolean isPageLoadComplete = cFluentWaitConfig.verifyPageLoadCompletes(webPage.getDriver());
			
			if(isPageLoadComplete){
				log.debug("["+this.hashCode()+"] Page load Completed!");
				
				iTafElement = (ITafElement) joinPoint.proceed();	
				
				iTafElement = cFluentWaitConfig.waitTillElementFound(iTafElement, webPage);
			}
		}
		catch(Exception exp){
			log.error("Exception occurred ==>"+ exp.getCause());
			throw exp;
		}
		return iTafElement;
	}
	
	
}
