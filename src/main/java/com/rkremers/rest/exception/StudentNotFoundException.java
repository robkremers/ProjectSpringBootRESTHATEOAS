package com.rkremers.rest.exception;

public class StudentNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 1044265303159746367L;

	public StudentNotFoundException(String message) {
		super(message);
	}

}
