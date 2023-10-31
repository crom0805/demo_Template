package com.demo.common.exception;

import lombok.Getter;

@Getter
public class EmptyTokenException extends RuntimeException {

	private ExceptionEnum error;

	public EmptyTokenException() {
		super(ExceptionEnum.EMPTY_JWT_EXCEPTION.getMessage());
		this.error = ExceptionEnum.EMPTY_JWT_EXCEPTION;
	}
}
