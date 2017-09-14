package com.etouch.taf.core.exception;

import java.io.PrintWriter;

public class JSONParserException extends Exception {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 239819968763503953L;
	
	/** The message. */
	private String message;
		
	/**
	 * Instantiates a new validation exception.
	 *
	 * @param message the message
	 */
	public JSONParserException(String message) {
		super();
		this.message = message;
	}

	/* (non-Javadoc)
	 * @see java.lang.Throwable#getMessage()
	 */
	@Override
	public String getMessage() {
		return message;
	}

	/* (non-Javadoc)
	 * @see java.lang.Throwable#printStackTrace(java.io.PrintWriter)
	 */
	@Override
	public void printStackTrace(PrintWriter s) {
		super.printStackTrace(s);
	}

}
