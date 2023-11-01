package com.demo.common.api;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ApiResult<T> {

	private static final String SUCCESS_STATUS = "success";
	private static final String FAIL_STATUS = "fail";
	private static final String ERROR_STATUS = "error";

	@Schema(description = "성공여부(success/fail)", nullable = false, example = "success")
	private String status;
	@Schema(description = "Response 데이터(없을경우 null)", nullable = false)
	private T data;
	@Schema(description = "status가 fail일 경우 메시지", nullable = false)
	private String message;

	public static <T> ApiResult<T> createSuccess(T data) {
		return new ApiResult<>(SUCCESS_STATUS, data, null);
	}

	public static ApiResult<?> createSuccessWithNoContent() {
		return new ApiResult<>(SUCCESS_STATUS, null, null);
	}

	// Hibernate Validator에 의해 유효하지 않은 데이터로 인해 API 호출이 거부될때 반환
	public static ApiResult<?> createFail(BindingResult bindingResult) {
		Map<String, String> errors = new HashMap<>();

		List<ObjectError> allErrors = bindingResult.getAllErrors();
		for (ObjectError error : allErrors) {
			if (error instanceof FieldError) {
				errors.put(((FieldError) error).getField(), error.getDefaultMessage());
			} else {
				errors.put(error.getObjectName(), error.getDefaultMessage());
			}
		}
		return new ApiResult<>(FAIL_STATUS, errors, "Validator 검증실패");
	}

	// 예외 발생으로 API 호출 실패시 반환
	public static ApiResult<?> createError(String message) {
		return new ApiResult<>(ERROR_STATUS, null, message);
	}

	private ApiResult(String status, T data, String message) {
		this.status = status;
		this.data = data;
		this.message = message;
	}
}
