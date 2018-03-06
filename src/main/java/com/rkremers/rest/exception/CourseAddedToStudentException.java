package com.rkremers.rest.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value=HttpStatus.CONFLICT)
public class CourseAddedToStudentException extends RuntimeException {

	private static final long serialVersionUID = -3762312183527949568L;

	public CourseAddedToStudentException() {
		super();
	}

	public CourseAddedToStudentException(String arg0) {
		super(arg0);
	}

}
