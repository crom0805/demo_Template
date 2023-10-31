package com.demo.reserve.lecture.dto;

import com.demo.reserve.lecture.domain.Lecture;
import com.querydsl.core.annotations.QueryProjection;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import lombok.Data;

/**
 * 강연정보조회 Response DTO
 */
@Data
public class LectureResponseDto {

	@Schema(description = "강연등록시 생성된 강연ID", example = "9")
	private Integer lectureId;

	@Schema(description = "강연자", example = "김선생님")
	private String lecturer;

	@Schema(description = "강연장", example = "글로벌룸")
	private String lectureRoom;

	@Schema(description = "강연내용", example = "신규입사자 OJT")
	private String lectureContent;

	@Schema(description = "강연인원", example = "10")
	private int lecturePeople;

	@Schema(description = "강연신청인원", example = "7")
	private int applicantNumber;

	@Schema(description = "강연시작일시", example = "20231018100555")
	private String startDt;

	@Schema(description = "강연종료일시", example = "20231025170000")
	private String endDt;

	public LectureResponseDto(Lecture lecture) {
		this.lectureId = lecture.getId();
		this.lecturer = lecture.getLecturer();
		this.lectureRoom = lecture.getLectureRoom();
		this.lectureContent = lecture.getLectureContent();
		this.lecturePeople = lecture.getLecturePeople();
		if (lecture.getApplicants() != null) {
			this.applicantNumber = lecture.getApplicants().size();
		}
		this.startDt = lecture.getStartDt().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
		this.endDt = lecture.getEndDt().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
	}

	@QueryProjection
	public LectureResponseDto(Integer lectureId, String lecturer, String lectureRoom, String lectureContent, int lecturePeople
			, int applicantNumber, LocalDateTime startDt, LocalDateTime endDt) {
		this.lectureId = lectureId;
		this.lecturer = lecturer;
		this.lectureRoom = lectureRoom;
		this.lectureContent = lectureContent;
		this.lecturePeople = lecturePeople;
		this.applicantNumber = applicantNumber;
		this.startDt = startDt.format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));;
		this.endDt = endDt.format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));;
	}
}
