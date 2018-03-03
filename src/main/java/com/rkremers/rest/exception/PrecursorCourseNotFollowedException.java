package com.rkremers.rest.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value=HttpStatus.CONFLICT)
public class PrecursorCourseNotFollowedException extends RuntimeException {

	private static final long serialVersionUID = 5159660029543025915L;

	public PrecursorCourseNotFollowedException(String message) {
		super(message);
	}

}
