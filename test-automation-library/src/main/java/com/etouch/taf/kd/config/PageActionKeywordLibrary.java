package com.etouch.taf.kd.config;

public enum PageActionKeywordLibrary {

	SCROLLUP("scrollUp"),SCROLLDOWN("scrollDown"), SCROLLRIGHT("scrollRight"), SCROLLLEFT("scrollLeft"),
	SCROLLTOP("scrollTop"), SCROLLBOTTOM("scrollBottom"),ZOOMIN("zoomIn"), ZOOMOUT("zoomOut"),
	ZOOMTO100("zoomTo100"), SLEEP("sleep"), CERTIFICATEERRORCLICK("certificateErrorClick"), 
	LOADPAGE("loadPage"), RESIZE("resize"), NAVIGATETOURL("navigateToUrl"), OPENNEWTAB("openNewTab"),
	OPENURLINNEWTAB("openURLInNewTab"), OPENURLINNEWWINDOW("openURLInNewWindow"),
	GETBACKTOURL("getBacktoUrl"), TOGGLEWINDOW("toggleWindow"),TOGGLETAB("toggleTab"),
	SWITCHWINDOW("switchWindow"), CLOSETOGGLEWINDOW("closeToggleWindow"), FULLSCROLLINSLOWMOTION("fullScrollInSlowMotion");
	
	
	String actionName;
	
	PageActionKeywordLibrary(String actionName){
		this.actionName=actionName;
		
	}
 
		public static boolean contains(String key) {
			
			boolean isListed = false;
			
			try {
				
				for (PageActionKeywordLibrary actionKeywordLibrary : PageActionKeywordLibrary.values()){
	 
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

		
		//TO-DO do we have to consider loadPage(""), zoomIN(""), zoomOUt(""), resize()(""), sleep (""),scrollToELement(""), navigateURL as isRequiredInputActions
		
		
		public static boolean isRequiredInputActions(String key) {
			if ( key == null ) return false;
			try {
				 key = key.toLowerCase();
				 if ( key.trim().equalsIgnoreCase(PageActionKeywordLibrary.LOADPAGE.toString()) ) {
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
