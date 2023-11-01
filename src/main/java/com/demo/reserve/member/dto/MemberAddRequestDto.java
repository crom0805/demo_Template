package com.demo.reserve.member.dto;

import com.demo.common.validation.ValidationGroups.NotEmptyGroup;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class MemberAddRequestDto {

	@NotEmpty(message = "아이디를 입력해주세요.", groups = NotEmptyGroup.class)
	@Schema(description = "로그인시에 사용할 이메일", nullable = false, example = "test@test.com")
	private String loginId;

	@NotEmpty(message = "비밀번호를 입력해주세요.", groups = NotEmptyGroup.class)
	@Schema(description = "로그인시에 사용할 비밀번호", nullable = false, example = "qwer1234")
	private String loginPw;

	@NotEmpty(message = "회원이름을 입력해주세요.", groups = NotEmptyGroup.class)
	@Schema(description = "회원이름", nullable = false, example = "팜하니")
	private String memberName;

	@NotEmpty(message = "연락처를 입력해주세요.", groups = NotEmptyGroup.class)
	@Schema(description = "연락처", nullable = false, example = "01012345678")
	private String memberTel;
}
