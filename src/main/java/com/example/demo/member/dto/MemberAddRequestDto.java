package com.example.demo.member.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class MemberAddRequestDto {

	@NotNull
	private String memberId;
	@NotNull
	private String memberPwd;
	@NotNull
	private String memberName;
	private String memberState;
	private String memberTel;
}
