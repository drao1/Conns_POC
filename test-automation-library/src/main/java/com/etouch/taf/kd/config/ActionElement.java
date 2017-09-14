package com.etouch.taf.kd.config;


/** 
 * A POJO class for KeywordDriven ActionElement
 * @author eTouch
 *
 */
public class ActionElement {
	
	String name;
	String idType;
	String value;
	Keyword keyword;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getIdType() {
		return idType;
	}
	public void setIdType(String idType) {
		this.idType = idType;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	
	/**
	 * @return the keyword
	 */
	public Keyword getKeyword() {
		return keyword;
	}
	
	/**
	 * @param keyword the keyword to set
	 */
	public void setKeyword(Keyword keyword) {
		this.keyword = keyword;
	}
	
}
