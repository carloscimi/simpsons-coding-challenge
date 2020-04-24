package com.example.simpsonschallengeapi.service.execption;

import java.util.NoSuchElementException;

public class EmptyResultDataAccessException extends NoSuchElementException {

	private static final long serialVersionUID = 1L;
	
	public EmptyResultDataAccessException(String obs) {
		super("EmptyResultDataAccessException: " + obs);
	}

}
