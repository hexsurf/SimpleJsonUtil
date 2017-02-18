package com.ericjin.opensource.util.json;

/**
 * A runtime exception for JSON processing.
 * 
 * @author Eric Jin
 *
 */
public class JsonConversionException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public JsonConversionException() {
		super();
	}

	public JsonConversionException(String message) {
		super(message);
	}

	public JsonConversionException(String message, Throwable cause) {
		super(message, cause);
	}

}
