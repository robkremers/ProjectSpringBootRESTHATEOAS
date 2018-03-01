package com.rkremers.rest.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value=HttpStatus.EXPECTATION_FAILED)
public class MissingPassPortNumberException extends RuntimeException{

	private static final long serialVersionUID = 5928903565832642538L;

	public MissingPassPortNumberException(String message) {
		super(message);
	}

}
