package com.etouch.taf.kd.config;

/**
 * @author etouch
 * 
 */
public enum InputKeywordLibrary {

	ENTERVALUE("enterValue"), ENTERKEY("enterKey"), SELECTDROPDOWNLIST("selectDropDownList"), URL("url");
	
	String actionName;
	
	
	InputKeywordLibrary(String actionName){
		this.actionName=actionName;
	}

	public static boolean contains(String key) {

		boolean isListed = false;

		try {

			for (InputKeywordLibrary inputKeywordLibrary : InputKeywordLibrary.values()) {
				if (inputKeywordLibrary.name().equalsIgnoreCase(key.trim())) {
					isListed = true;
					break;
				}
			}

		} catch (Exception e) {

			isListed = false;
		}

		return isListed;
	}
	
	public String getActionName(){
		return this.actionName;
		
	}

}
