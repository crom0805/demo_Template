package com.demo.reserve.lecture.service;

import com.demo.common.exception.CustomException;
import com.demo.common.exception.DuplicatedException;
import com.demo.reserve.lecture.domain.Applicant;
import com.demo.reserve.lecture.domain.ApplicantHis;
import com.demo.reserve.lecture.domain.Lecture;
import com.demo.reserve.lecture.dto.ApplicantHisResponseDto;
import com.demo.reserve.lecture.dto.ApplicantResponseDto;
import com.demo.reserve.lecture.dto.LectureApplicantResponseDto;
import com.demo.reserve.lecture.dto.LectureApplyRequestDto;
import com.demo.reserve.lecture.dto.LectureResponseDto;
import com.demo.reserve.lecture.dto.LectureSaveRequestDto;
import com.demo.reserve.lecture.repository.ApplicantHisRepository;
import com.demo.reserve.lecture.repository.ApplicantRepository;
import com.demo.reserve.lecture.repository.LectureRepository;
import com.demo.reserve.member.domain.Member;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 강연정보관련 Service
 */
@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class LectureService {

	private final LectureRepository lectureRepository;
	private final ApplicantRepository applicantRepository;
	private final ApplicantHisRepository applicantHisRepository;

	/**
	 * BackOffice_강연(전체)목록 조회
	 */
	public List<LectureResponseDto> findAll() {
		return lectureRepository.findAll()
			.stream()
			.map(LectureResponseDto::new)
			.collect(Collectors.toList());
	}

	/**
	 * BackOffice_강연등록,,
	 */
	@Transactional
	public LectureResponseDto saveLecture(LectureSaveRequestDto requestDto) {
		DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

		// validation check
		LocalDateTime startDt = LocalDateTime.parse(requestDto.getStartDt(), format);
		LocalDateTime endDt = LocalDateTime.parse(requestDto.getEndDt(), format);
		if (startDt.isAfter(endDt)) {
			log.error("강연시작일은 강연종료일 이전이어야 합니다.");
			throw new CustomException("강연시작일은 강연종료일 이전이어야 합니다.");
		}

		Lecture saveLecture = lectureRepository.save(
			Lecture.builder()
				.lecturer(requestDto.getLecturer())
				.lectureRoom(requestDto.getLectureRoom())
				.lectureContent(requestDto.getLectureContent())
				.lecturePeople(requestDto.getLecturePeople())
				.startDt(startDt)
				.endDt(endDt)
				.build()
		);

		return new LectureResponseDto(saveLecture);
	}

	/**
	 * BackOffice_강연별 신청자 목록 조회
	 */
	public List<LectureApplicantResponseDto> findLectureApplicant() {
		return lectureRepository.findAll()
			.stream()
			.map(LectureApplicantResponseDto::new)
			.collect(Collectors.toList());
	}

	/**
	 * BackOffice_특정강연 신청자 목록조회
	 */
	public List<String> findApplicantByLecture(Integer lectureId) {
		return applicantRepository.findByLectureId(lectureId)
			.stream()
			.map(applicant -> applicant.getMember().getLoginId())
			.collect(Collectors.toList());
	}

	/**
	 * Front_강연목록조회 (검색조건)
	 */
	public List<LectureResponseDto> findLecturesByFront() {
		return lectureRepository.findLecturesByFront();
	}

	/**
	 * Front_강연신청
	 */
	@Transactional
	public ApplicantResponseDto applyLecture(LectureApplyRequestDto requestDto) {
		// validation check
		Lecture lecture = lectureRepository.findByIdForUpdate(requestDto.getLectureId());
		if (lecture == null) {
			throw new CustomException("존재하지 않는 강연입니다.");
		}

		if (lecture.getApplicants() != null) {
			// 1. 최대인원 체크
			if (lecture.getLecturePeople() == lecture.getApplicants().size()) {
				log.error("해당 강연의 빈좌석이 없습니다. memberID=" + requestDto.getMemberId());
				throw new CustomException("해당 강연의 빈좌석이 없습니다. memberId=" + requestDto.getMemberId());
			}

			// 2. 중복등록체크
			List<Applicant> applicantList = lecture.getApplicants();
			long count = applicantList.stream().filter(applicant -> applicant.getMember().getId().equals(requestDto.getMemberId())).count();
			if (count > 0) {
				log.error("이미 등록된 회원입니다. memberId=" + requestDto.getMemberId());
				throw new DuplicatedException();
			}
		}

		// 강연신청
		Applicant applicant = applicantRepository.save(
			Applicant.builder()
				.lecture(new Lecture(requestDto.getLectureId()))
				.member(new Member(requestDto.getMemberId()))
				.build()
		);

		// 신청이력 저장
		applicantHisRepository.save(
			ApplicantHis.builder()
				.lecture(new Lecture(requestDto.getLectureId()))
				.member(new Member(requestDto.getMemberId()))
				.applyDiv("A")
				.build()
		);

		return new ApplicantResponseDto(applicant);
	}

	/**
	 * Front_신청내역조회
	 */
	public List<ApplicantHisResponseDto> findApplicantHis(Integer memberId) {
		return applicantHisRepository.findByMemberId(memberId)
			.stream()
			.map(ApplicantHisResponseDto::new)
			.collect(Collectors.toList());
	}

	/**
	 * Front_강연취소
	 */
	@Transactional
	public String cancelLecture(LectureApplyRequestDto requestDto) {

		Applicant applicant = applicantRepository.findByMemberIdAndLectureId(requestDto.getMemberId(), requestDto.getLectureId())
			.orElseThrow(() -> new CustomException("해당 신청정보가 없습니다."));

		// 신청정보 삭제
		applicantRepository.deleteById(applicant.getId());

		// 취소이력 저장
		applicantHisRepository.save(
			ApplicantHis.builder()
				.lecture(applicant.getLecture())
				.member(applicant.getMember())
				.applyDiv("C")
				.build()
		);

		return "취소성공";
	}

	/**
	 * 실시간 인기 강연
	 */
	public List<LectureResponseDto> findPopularityLectures() {
		return lectureRepository.findPopularityLectures();
	}


}
