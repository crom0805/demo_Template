package com.example.demo.common.exception;

import lombok.Getter;

@Getter
public class UserNotFoundException extends RuntimeException {

	private ExceptionEnum error;

	public UserNotFoundException() {
        super(ExceptionEnum.MEMBER_NOT_FOUND.getMessage());
		this.error = ExceptionEnum.MEMBER_NOT_FOUND;
    }
}
