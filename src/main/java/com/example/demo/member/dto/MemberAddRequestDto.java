package com.example.demo.member.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class MemberAddRequestDto {

	@NotNull
	@Schema(description = "로그인시에 사용할 이메일", nullable = false, example = "test@test.com")
	private String memberId;

	@NotNull
	@Schema(description = "로그인시에 사용할 비밀번호", nullable = false, example = "qwer1234")
	private String memberPwd;

	@NotNull
	@Schema(description = "회원이름", nullable = false, example = "팜하니")
	private String memberName;

	@Schema(description = "연락처", example = "01012345678")
	private String memberTel;
}
