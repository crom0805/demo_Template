package com.example.demo.member.dto;

import lombok.Data;

@Data
public class MemberLoginRequestDto {
	private String memberId;
    private String memberPwd;
}
