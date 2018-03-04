package com.rkremers.rest.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value=HttpStatus.CONFLICT)
public class StudyConfigurationNotFound extends RuntimeException {

	private static final long serialVersionUID = -1813502685141289590L;

	public StudyConfigurationNotFound() {
		super();
		// TODO Auto-generated constructor stub
	}

	public StudyConfigurationNotFound(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

}
