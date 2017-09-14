package com.etouch.taf.kd.config;


import org.apache.commons.logging.Log;

import com.etouch.taf.core.config.BooleanAssertKeywordLibrary;
import com.etouch.taf.kd.exception.InvalidActionException;
import com.etouch.taf.util.LogUtil;

public class Keyword {
	
	private static Log log = LogUtil.getLog(Keyword.class);
	
	public Keyword(String action) throws InvalidActionException{
		this.setAction(action);
	}
	
	private String action;

	public String getAction() {
		return action.trim();
	}

	private void setAction(String action) throws InvalidActionException {
		if((action!=null )&& (!action.isEmpty()) ){
			
			boolean isSet=false;

			if( InputKeywordLibrary.contains(action) || 
					AssertKeywordLibrary.contains(action) || 
					ActionKeywordLibrary.contains(action) ||
					PageActionKeywordLibrary.contains(action) ||
					BooleanAssertKeywordLibrary.contains(action)){
				this.action=action;
				isSet = true;
			}
			
			if(!isSet){
				log.debug( action + " is invalid Keyword");
				throw new InvalidActionException(action + " is invalid keyword");
			}
			
		}
	}
}
