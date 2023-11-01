package com.demo.reserve.lecture.dto;

import com.demo.reserve.lecture.domain.Applicant;
import com.demo.reserve.lecture.domain.Lecture;
import com.demo.reserve.member.domain.Member;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Data;

/**
 * 강연별 신청자목록 Response DTO
 */
@Data
public class LectureApplicantResponseDto {

	@Schema(description = "강연등록시 생성된 강연ID", example = "9")
	private Integer lectureId;

	@Schema(description = "강연자", example = "김선생님")
	private String lecturer;

	@Schema(description = "강연장", example = "글로벌룸")
	private String lectureRoom;

	@Schema(description = "강연내용", example = "신규입사자 OJT")
	private String lectureContent;

	@Schema(description = "신청자 ID목록", nullable = true, example = "[\"test@test.com\",\"test2@test.com\"]")
	private List<String> applicants;

	public LectureApplicantResponseDto(Lecture lecture) {
		this.lectureId = lecture.getId();
		this.lecturer = lecture.getLecturer();
		this.lectureRoom = lecture.getLectureRoom();
		this.lectureContent = lecture.getLectureContent();
		this.applicants = lecture.getApplicants().stream().map(applicant -> applicant.getMember().getLoginId()).collect(Collectors.toList());
	}

}
