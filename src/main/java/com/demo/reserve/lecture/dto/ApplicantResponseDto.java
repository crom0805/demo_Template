package com.demo.reserve.lecture.dto;

import com.demo.reserve.lecture.domain.Applicant;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.format.DateTimeFormatter;
import lombok.Data;

/**
 * 신청정보 Response DTO
 */
@Data
public class ApplicantResponseDto {

	@Schema(description = "신청시 생성된 신청정보ID", example = "11")
	private Integer applicantId;

	@Schema(description = "신청한 강연ID", example = "11")
	private Integer lectureId;

	@Schema(description = "신천한 회원ID", example = "1")
	private Integer memberId;

	@Schema(description = "신청일시", example = "20231019144723")
	private String applyDt;

	public ApplicantResponseDto(Applicant applicant) {
		this.applicantId = applicant.getId();
		this.lectureId = applicant.getLecture().getId();
		this.memberId = applicant.getMember().getId();
		this.applyDt = applicant.getApplyDt().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
	}

}
