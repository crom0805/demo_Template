package com.demo.reserve.lecture.dto;

import com.demo.common.validation.ValidationGroups.LengthCheckGroup;
import com.demo.common.validation.ValidationGroups.NotEmptyGroup;
import com.demo.common.validation.ValidationGroups.NotNullGroup;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * 강연신청 Request DTO
 */
@Data
public class LectureApplyRequestDto {

	@NotNull(message = "회원ID를 입력해주세요.", groups = NotEmptyGroup.class)
	@Schema(description = "회원ID", example = "1")
	private Integer memberId;

	@NotNull(message = "강연ID를 입력해주세요.", groups = NotNullGroup.class)
	@Schema(description = "강연ID", example = "9")
	private Integer lectureId;
}
