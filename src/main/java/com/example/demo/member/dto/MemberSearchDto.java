package com.example.demo.member.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class MemberSearchDto {

	@Schema(description = "로그인ID(이메일)", nullable = true, example = "test@test.com")
	private String memberId;

	@Schema(description = "회원명", nullable = true, example = "팜하니")
	private String memberName;

	@Schema(description = "전화번호", nullable = true, example = "01012345678")
	private String memberTel;
}
