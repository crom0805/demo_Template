package com.demo.common.exception;

import lombok.Getter;

/**
 * Duplicated Custom Exception
 */
@Getter
public class DuplicatedException extends RuntimeException {

	private ExceptionEnum error;

	public DuplicatedException() {
		super(ExceptionEnum.DUPLICATED_LECTURE.getMessage());
		this.error = ExceptionEnum.DUPLICATED_LECTURE;
	}
}
