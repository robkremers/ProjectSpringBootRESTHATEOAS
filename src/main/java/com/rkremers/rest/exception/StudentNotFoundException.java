package com.rkremers.rest.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value=HttpStatus.CONFLICT)
public class StudentNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 1044265303159746367L;

	public StudentNotFoundException(String message) {
		super(message);
	}

}
