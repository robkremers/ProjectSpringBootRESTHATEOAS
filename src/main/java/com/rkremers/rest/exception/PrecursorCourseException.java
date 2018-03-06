package com.rkremers.rest.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value=HttpStatus.CONFLICT)
public class PrecursorCourseException extends RuntimeException {

	private static final long serialVersionUID = 6310131008384058078L;

	public PrecursorCourseException() {
	}

	public PrecursorCourseException(String message) {
		super(message);
	}

	public PrecursorCourseException(Throwable cause) {
		super(cause);
	}

	public PrecursorCourseException(String message, Throwable cause) {
		super(message, cause);
	}

	public PrecursorCourseException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
