/**
 * 
 */
package com.etouch.taf.kd.config;

/**
 * @author etouch
 * 
 */
public enum AssertKeywordLibrary {

	ISDISPLAYED("isDisplayed"), ISENABLED("isEnabled"), ISSELECTED("isSelected"), ISCHECKED("isChecked"), HASVALUE("hasValue"),
	HASIMAGE("hasImage"), ISGREATERTHAN("isGreaterThan"), ISLESSTHAN("isLessThan"), ISELEMENTVISIBLE("isElementVisible"), CONTAINS("contains"),
	HASCONTENT("hasContent"), HASNAME("hasName"), HASSRC("hasSrc"), HASHREF("hasHref"), HASCHILDCOUNT("hasChildCount"), HASIMAGETITLE("hasImageTitle"),
	CONTAINSLINKTEXT("containsLinkText"), HASIMAGEALTTEXT("hasImagealTtext"), HASSTYLE("hasStyle"), STYLECONTAINS("styleContains"), HASINPUTVALUE("hasInputValue"),
	

	// Response code assert actions
	ISSUCCESS("isSuccess"), ISCREATED("isCreated"), ISACCEPTED("isAccepted"), ISINFORMATIONPARTIAL("isInformationPartial"), HASNORESPONSE("hasNoResponse"),
	ISBADREQUEST("isBadRequest"), ISUNAUTHORIZED("isUnauthorized"), ISPAYMENTREQUIRED("isPaymentRequired"), ISFORBIDDEN("isForbidden"), ISNOTFOUND("isNotFound"),
	HASINTERNALERROR("hasInternalError"), HASNOTIMPLEMENTED("hasNotImplemented"), ISSERVICEOVERLOADED("isServiceOverloaded"), ISGATEWAYTIMEOUT("isGatewayTimeout"),
	ISURLREDIRECTED("isURLRedirected"), ISURLMOVED("isURLMoved");
	
	
	String actionName;
	
	
	AssertKeywordLibrary(String actionName){
		this.actionName=actionName;
		
	}

	public static boolean contains(String assertKeyword) {

		boolean isListed = false;

		try {

			for (AssertKeywordLibrary assertKeywordLibrary : AssertKeywordLibrary.values()) {
				if (assertKeywordLibrary.name().equalsIgnoreCase(assertKeyword.trim())) {
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
