/**
 * 
 */
package com.etouch.taf.kd.config;

import org.springframework.util.StringUtils;

import com.etouch.taf.core.config.BooleanAssertKeywordLibrary;


/**
 * @author etouch
 *
 */
public enum ActionKeywordLibrary {

	EXECUTEJAVA("executeJava"), CLICK("click"),HOVER("hover"),
	DOUBLECLICK("doubleClick"), CLEAR("clear"), SENDKEYS("sendKeys"),
	SUBMIT("submit"), CHECK("check"),CLICKEVENT("clickEvent"),
	UNCHECK("uncheck"), DRAGANDDROP("dragAndDrop"), GETCOORDINATES("getCoordinates"), 
	GETSIZE("getSize"), GETTEXT("getText"), GETATTRIBUTE("getattribute"), GETCSSVALUE("getcssvalue"),
	GETWEBELEMENT("getWebElement"),  TAP("tap"), PINCH("pinch"),
	SWIPE("swipe"), CLOSEBROWSER("closeBrowser"), OPENBROWSER("openBrowser"),
	SWITCHTOIFRAME("switchToFrame"), SCROLLTOELEMENT("scrollToElement") ;
 
	
	String actionName;
	
	ActionKeywordLibrary(String actionName){
		this.actionName= actionName;
		
	}
	
	
	public static boolean contains(String key) {
		
		boolean isListed = false;
		
		try {
			
			for (ActionKeywordLibrary actionKeywordLibrary : ActionKeywordLibrary.values()){
 
				if (actionKeywordLibrary.name().equalsIgnoreCase(key.trim())){
					isListed = true;
					break;
				}
			}
			
		} catch (Exception e) {

			isListed = false;
		}
		
		return isListed;
	}

	
	public static boolean isRequiredInputActions(String key) {
		if ( key == null ) return false;
		try {
			 key = key.toLowerCase();
			 if (  key.trim().equalsIgnoreCase(InputKeywordLibrary.SELECTDROPDOWNLIST.toString()) 
					 	|| key.trim().equalsIgnoreCase(InputKeywordLibrary.ENTERVALUE.toString())
					 	
					 	|| key.trim().equalsIgnoreCase(PageActionKeywordLibrary.ZOOMIN.toString())
					 	|| key.trim().equalsIgnoreCase(PageActionKeywordLibrary.ZOOMOUT.toString())
					 						 	
					 	|| key.trim().equalsIgnoreCase(PageActionKeywordLibrary.SCROLLDOWN.toString())
					 	|| key.trim().equalsIgnoreCase(PageActionKeywordLibrary.SCROLLUP.toString())
					 			
					 	|| key.trim().equalsIgnoreCase(PageActionKeywordLibrary.RESIZE.toString())
					 	|| key.trim().equalsIgnoreCase(PageActionKeywordLibrary.NAVIGATETOURL.toString())
					 	|| key.trim().equalsIgnoreCase(PageActionKeywordLibrary.SWITCHWINDOW.toString())
					 	
					 	|| AssertKeywordLibrary.contains(key.trim())
					 	|| BooleanAssertKeywordLibrary.contains(key.trim())
					 					
					 ) {
				 return true;
			 }
			 else {
				 return false;
			 }

		} catch (Exception e) {

			return false;
		}
	}
	
	
	public String getActionName(){
		return this.actionName;
		
	}
	
	
	
	
	
}
