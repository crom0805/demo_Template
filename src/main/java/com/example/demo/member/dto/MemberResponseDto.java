package com.example.demo.member.dto;

import com.example.demo.member.entity.Member;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class MemberResponseDto {
	@Schema(description = "로그인시에 사용할 이메일", nullable = false, example = "test@test.com")
	private String memberId;

	@Schema(description = "회원이름", nullable = false, example = "팜하니")
	private String memberName;

	//private String memberState;

	@Schema(description = "연락처", example = "01012345678")
	private String memberTel;

	@Schema(description = "refreshToken", example = "eyJhbGciOiJIUzI1NiJ9.eyJleHAiOjE2OTA4NzYwOTZ9.O7zoMVk9-lUghNIFqPBCOT_sX7ppYhQBayZo6jaHjRQ")
	private String refreshToken;

	public MemberResponseDto(Member member) {
		this.memberId = member.getMemberId();
		this.memberName = member.getMemberName();
		//this.memberState = member.getMemberState();
		this.memberTel = member.getMemberTel();
		this.refreshToken = member.getRefreshToken();
	}
}
