package com.demo.reserve.member.dto;

import com.demo.reserve.member.domain.Member;
import com.querydsl.core.annotations.QueryProjection;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MemberResponseDto {

	@Schema(description = "로그인시에 사용할 이메일", nullable = false, example = "test@test.com")
	private String memberId;

	@Schema(description = "회원이름", nullable = false, example = "팜하니")
	private String memberName;

	//private String memberState;

	@Schema(description = "연락처", example = "01012345678")
	private String memberTel;

	public MemberResponseDto(Member member) {
		this.memberId = member.getMemberId();
		this.memberName = member.getMemberName();
		//this.memberState = member.getMemberState();
		this.memberTel = member.getMemberTel();
	}

	@QueryProjection
	public MemberResponseDto(String memberId, String memberName, String memberTel) {
		this.memberId = memberId;
		this.memberName = memberName;
		this.memberTel = memberTel;
	}
}
