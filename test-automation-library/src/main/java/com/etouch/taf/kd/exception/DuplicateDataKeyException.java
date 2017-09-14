package com.etouch.taf.kd.exception;

public class DuplicateDataKeyException extends Exception{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public DuplicateDataKeyException(){
		super();
	}

	public DuplicateDataKeyException(String message){
		super(message);
	}
}
