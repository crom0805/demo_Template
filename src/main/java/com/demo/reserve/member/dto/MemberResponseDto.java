package com.demo.reserve.member.dto;

import com.demo.reserve.member.domain.Member;
import com.querydsl.core.annotations.QueryProjection;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MemberResponseDto {

	@Schema(description = "회원ID", nullable = false, example = "1")
	private Integer memberId;

	@Schema(description = "회원로그인ID(이메일)", nullable = false, example = "test@test.com")
	private String loginId;

	@Schema(description = "회원이름", nullable = false, example = "팜하니")
	private String memberName;

	@Schema(description = "연락처", example = "01012345678")
	private String memberTel;

	public MemberResponseDto(Member member) {
		this.memberId = member.getId();
		this.loginId = member.getLoginId();
		this.memberName = member.getMemberName();
		this.memberTel = member.getMemberTel();
	}

	@QueryProjection
	public MemberResponseDto(Integer memberId, String loginId, String memberName, String memberTel) {
		this.memberId = memberId;
		this.loginId = loginId;
		this.memberName = memberName;
		this.memberTel = memberTel;
	}
}
