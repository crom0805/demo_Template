package com.example.demo.common.exception;

import lombok.Getter;

@Getter
public class DuplicatedUserException extends RuntimeException {

	private ExceptionEnum error;

	public DuplicatedUserException() {
		super(ExceptionEnum.DUPLICATED_MEMBERID.getMessage());
		this.error = ExceptionEnum.DUPLICATED_MEMBERID;
	}
}
