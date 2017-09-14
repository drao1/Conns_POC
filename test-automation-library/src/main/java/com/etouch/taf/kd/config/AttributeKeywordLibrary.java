/**
 * 
 */
package com.etouch.taf.kd.config;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * List of all the Attributes an HTML tag can have.
 * Example: <a> has an attribute 'href'<br>
 * 			<meta> has attribute 'content'
 * 
 * @author eTouch Systems Corporation
 *
 */
public enum AttributeKeywordLibrary {

	NAME("name", "hasName"),
	CONTENT("content", "hasContent"),
	SRC("src", "hasSrc"),
	HREF("href", "hasHref"),
	CHILD_COUNT("childCount", "hasChildCount"),
	IMAGE_TITLE("title", "hasImageTitle"),
	IMAGE_ALT_TEXT("alt", "hasImageAltText"),
	STYLE("style","hasStyle"),
	STYLE_CONTAINS("style", "styleContains"),
	TEXT("text","containLinkText"),
	VALUE("value", "hasInputValue");
	
	
	private String attributeName;
	
	private String attributeAssertMethod;
	
	/**
	 * @param attributeName
	 * @param ordinal
	 */
	AttributeKeywordLibrary(String attributeName, String attributeAssertMethod) {
	
		this.attributeName = attributeName;
		this.attributeAssertMethod = attributeAssertMethod;
		
	}

	/**
	 * @return the attributeName
	 */
	public String getAttributeName() {
		return attributeName;
	}

	/**
	 * @return the attributeAssertMethod
	 */
	public String getAttributeAssertMethod() {
		return attributeAssertMethod;
	}

	
	private static final Map<String, String> attributeAssertMap = Collections.unmodifiableMap(initializeMapping());

	private static Map<String, String> initializeMapping() {
		Map<String, String> assertMethodMap = new HashMap<String, String>();
		
		for(AttributeKeywordLibrary attributeKeywordLibrary: AttributeKeywordLibrary.values()){
			assertMethodMap.put(attributeKeywordLibrary.attributeAssertMethod, attributeKeywordLibrary.attributeName);
		}
		
	    return assertMethodMap;
	}
	
	public static String getAttribute(String attributeAssertMethod){
		
		if (attributeAssertMap.containsKey(attributeAssertMethod))
			return attributeAssertMap.get(attributeAssertMethod);
		
		return null;
		
	}
	
	public static boolean contains(String attributeAssertMethod) {

		boolean isListed = false;

		try {
			
			for(AttributeKeywordLibrary attributeKeywordLibrary: AttributeKeywordLibrary.values()){
				if(attributeKeywordLibrary.getAttributeAssertMethod().equalsIgnoreCase(attributeAssertMethod)){
					isListed = true;
					break;
				}
			}

		} catch (Exception e) {

			isListed = false;
		}

		return isListed;
	}
	
}
