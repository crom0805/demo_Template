package com.demo.reserve.member.dto;

import com.demo.common.validation.ValidationGroups.NotEmptyGroup;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class MemberLoginRequestDto {

	@NotEmpty(message = "아이디를 입력해주세요.", groups = NotEmptyGroup.class)
	@Schema(description = "로그인ID(이메일)", nullable = false, example = "test@test.com")
	private String loginId;

	@NotEmpty(message = "비밀번호를 입력해주세요.", groups = NotEmptyGroup.class)
	@Schema(description = "비밀번호", nullable = false, example = "qwer1234")
	private String loginPw;
}
