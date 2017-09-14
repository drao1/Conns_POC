package com.etouch.taf.core.config;

import org.apache.commons.logging.Log;

import com.etouch.taf.core.TestActionExecutor;
import com.etouch.taf.util.LogUtil;

/**
	 * @author etouch
	 * 
	 */
	public enum BooleanAssertKeywordLibrary {

		ISDISPLAYED, ISENABLED, ISSELECTED, ISCHECKED, ISELEMENTVISIBLE;

		private static Log log = LogUtil.getLog(TestActionExecutor.class);
		
		public static boolean contains(String assertKeyword) {

			boolean isListed = false;

			try {
				
				for(BooleanAssertKeywordLibrary bassertKeywordLibrary: BooleanAssertKeywordLibrary.values()){
					if(bassertKeywordLibrary.name().equalsIgnoreCase(assertKeyword.trim())){
						isListed = true;
						break;
					}
				}

			} catch (Exception e) {

				isListed = false;
				log.error(e.getMessage(),e);
			}

			return isListed;
		}

	}
