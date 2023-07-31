package com.example.demo.member.dto;

import com.example.demo.member.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class MemberResponseDto {
	private String memberId;
	private String memberName;
	private String memberState;
	private String memberTel;
	private String refreshToken;

	public MemberResponseDto(Member member) {
		this.memberId = member.getMemberId();
		this.memberName = member.getMemberName();
		this.memberState = member.getMemberState();
		this.memberTel = member.getMemberTel();
		this.refreshToken = member.getRefreshToken();
	}
}
