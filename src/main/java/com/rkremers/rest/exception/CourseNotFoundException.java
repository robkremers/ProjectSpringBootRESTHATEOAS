package com.rkremers.rest.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value=HttpStatus.CONFLICT)
public class CourseNotFoundException extends RuntimeException {
	
	private static final long serialVersionUID = -3345474393180045323L;

	public CourseNotFoundException(String message) {
		super(message);
	}

}
