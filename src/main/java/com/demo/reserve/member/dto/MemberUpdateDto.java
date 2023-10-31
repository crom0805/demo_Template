package com.demo.reserve.member.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class MemberUpdateDto {

	@Schema(description = "비밀번호", nullable = false, example = "1q2w3e4r")
	private String memberPwd;

	@Schema(description = "회원명", nullable = false, example = "팜하니")
	private String memberName;

	@Schema(description = "전화번호", nullable = false, example = "01012345678")
	private String memberTel;
}
