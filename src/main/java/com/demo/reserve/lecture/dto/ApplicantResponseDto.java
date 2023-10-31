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

	@Schema(description = "사번", example = "A1234")
	private String empNo;

	@Schema(description = "신청일시", example = "20231019144723")
	private String applyDt;

	public ApplicantResponseDto(Applicant applicant) {
		this.applicantId = applicant.getId();
		this.lectureId = applicant.getLecture().getId();
		this.empNo = applicant.getEmpNo();
		this.applyDt = applicant.getApplyDt().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
	}

}
