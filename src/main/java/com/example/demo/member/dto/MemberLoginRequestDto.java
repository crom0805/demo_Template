package com.example.demo.member.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class MemberLoginRequestDto {
	@Schema(description = "로그인ID(이메일)", nullable = false, example = "test@test.com")
	private String memberId;
	@Schema(description = "비밀번호", nullable = false, example = "1q2w3e4r")
    private String memberPwd;
}
