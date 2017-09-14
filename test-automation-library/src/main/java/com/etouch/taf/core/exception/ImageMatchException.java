package com.etouch.taf.core.exception;

public class ImageMatchException extends Throwable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String actualImageURL;
	private String expectedImageURL;

	private String message;

	public ImageMatchException(String actualImageURL, String expectedImageURL) {
		this.actualImageURL = actualImageURL;
		this.expectedImageURL = expectedImageURL;
	}

	@Override
	public String getMessage() {
		// URL has to be retrived from config file
		message = "http://localhost:8080/ImageComparison?actual=" + actualImageURL + "&expected=" + expectedImageURL;
		return message;
	}

	@Override
	public Throwable getCause() {
		return new Throwable(getMessage());
	}

	@Override
	public String getLocalizedMessage() {
		return getMessage();
	}

}
