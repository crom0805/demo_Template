package com.demo.reserve.lecture.dto;

import com.demo.reserve.lecture.domain.ApplicantHis;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.format.DateTimeFormatter;
import lombok.Data;

/**
 * 신청내역 Response DTO
 */
@Data
public class ApplicantHisResponseDto {

	@Schema(description = "신청시 생성된 신청정보ID", example = "11")
	private Integer applicantId;

	@Schema(description = "신청한 강연ID", example = "11")
	private Integer lectureId;

	@Schema(description = "신청한 강연명", example = "신규직원 OJT")
	private String lectureContent;

	@Schema(description = "사번", example = "A1234")
	private String empNo;

	@Schema(description = "신청/취소 구분", example = "A=신청, C=취소")
	private String applyDiv;

	@Schema(description = "신청일시", example = "20231019144723")
	private String applyDt;

	public ApplicantHisResponseDto(ApplicantHis applicant) {
		this.applicantId = applicant.getId();
		this.lectureContent = applicant.getLecture().getLectureContent();
		this.lectureId = applicant.getLecture().getId();
		this.empNo = applicant.getEmpNo();
		this.applyDiv = applicant.getApplyDiv();
		this.applyDt = applicant.getRegDt().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
	}

}
