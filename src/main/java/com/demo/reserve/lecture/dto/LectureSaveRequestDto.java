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
 * 강연정보 등록 Request DTO
 */
@Data
public class LectureSaveRequestDto {

	@Size(min = 1, max = 10, message = "강연자는 10자이내로 입력해주세요.", groups = LengthCheckGroup.class)
	@NotEmpty(message = "강연자를 입력해주세요.", groups = NotEmptyGroup.class)
	@Schema(description = "강연자", example = "김선생님")
	private String lecturer;

	@Size(min = 1, max = 10, message = "강연장은 10자이내로 입력해주세요.", groups = LengthCheckGroup.class)
	@NotEmpty(message = "강연장을 입력해주세요.", groups = NotEmptyGroup.class)
	@Schema(description = "강연장", example = "글로벌룸")
	private String lectureRoom;

	@Size(min = 1, max = 30, message = "강연내용은 30자이내로 입력해주세요.", groups = LengthCheckGroup.class)
	@NotEmpty(message = "강연내용을 입력해주세요.", groups = NotEmptyGroup.class)
	@Schema(description = "강연내용", example = "신규입사자 OJT")
	private String lectureContent;

	@NotNull(message = "강연인원을 입력해주세요.", groups = NotNullGroup.class)
	@Schema(description = "강연인원", example = "15")
	private Integer lecturePeople;

	@NotNull(message = "시작일시를 입력해주세요.", groups = NotNullGroup.class)
	@Schema(description = "강연시작일시(yyyyMMdd HHmmss)", example = "20231101103000")
	private String startDt;

	@NotNull(message = "종료일시를 입력해주세요.", groups = NotNullGroup.class)
	@Schema(description = "강연종료일시(yyyyMMdd HHmmss)", example = "20231130170000")
	private String endDt;
}
