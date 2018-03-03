package com.rkremers.rest.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value=HttpStatus.CONFLICT)
public class PrecursorCourseNotPassedException extends RuntimeException {

	private static final long serialVersionUID = 2692156031154511765L;

	public PrecursorCourseNotPassedException(String message) {
		super(message);
	}
}
