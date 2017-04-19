package com.luotian.core.domain;

/**
 * Thrown while error occurs during the converting (typically, between Java and
 * JSON).
 * <p>
 * Note that it's a hard decision to make duplication of this class.
 * However, we need to simplify the Use of Andriod developing.
 * 
 * @author LIU Fangran
 * 
 */
public class ConvertException extends RuntimeException {

	public static final long serialVersionUID = 1L;

	public ConvertException() {
		super();

	}

	public ConvertException(String message, Throwable cause) {
		super(message, cause);

	}

	public ConvertException(String message) {
		super(message);

	}

	public ConvertException(Throwable cause) {
		super(cause);

	}

}
