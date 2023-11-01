package com.demo.common.exception;

import lombok.Getter;
import lombok.ToString;
import org.springframework.http.HttpStatus;

@Getter
@ToString
public enum ExceptionEnum {

	// System Exception
	RUNTIME_EXCEPTION(HttpStatus.BAD_REQUEST, "E0001", "잘못된 요청입니다."),
	ACCESS_DENIED_EXCEPTION(HttpStatus.UNAUTHORIZED, "E0002", "접근권한이 없습니다."),
	INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "E0003", "시스템오류가 발생했습니다."),
	FORBIDDEN(HttpStatus.FORBIDDEN, "E0004", "자격 증명에 실패하였습니다."),
	INVALID_JWT_TOKEN(HttpStatus.UNAUTHORIZED, "E0005", "JWT 토큰이 잘못되었습니다."),
	EXPIRED_JWT_EXCEPTION(HttpStatus.UNAUTHORIZED, "E0006", "JWT 토큰이 만료되었습니다."),
	UNSUPPORTED_JWT_EXCEPTION(HttpStatus.UNAUTHORIZED, "E0007", "지원되지 않는 토큰입니다."),
	EMPTY_JWT_EXCEPTION(HttpStatus.UNAUTHORIZED, "E0008", "토큰정보가 없습니다."),

	// Custom Exception
	DUPLICATED_MEMBERID(HttpStatus.CONFLICT, "CE0001", "이미 존재하는 회원ID입니다."),
	SECURITY(HttpStatus.UNAUTHORIZED, "CE0002", "엑세스토큰이 없습니다."),
	MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "CE0003", "일치하는 회원정보를 찾을수 없습니다."),
	DUPLICATED_LECTURE(HttpStatus.CONFLICT, "CE0004", "해당 강연에 이미 등록된 사번입니다.");


	private final HttpStatus status;
	private final String code;
	private String message;

	ExceptionEnum(HttpStatus status, String code) {
		this.status = status;
		this.code = code;
	}

	ExceptionEnum(HttpStatus status, String code, String message) {
		this.status = status;
		this.code = code;
		this.message = message;
	}
}
