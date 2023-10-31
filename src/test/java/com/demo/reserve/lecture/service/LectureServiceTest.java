package com.demo.reserve.lecture.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.demo.common.exception.CustomException;
import com.demo.reserve.lecture.domain.Applicant;
import com.demo.reserve.lecture.domain.Lecture;
import com.demo.reserve.lecture.dto.ApplicantHisResponseDto;
import com.demo.reserve.lecture.dto.LectureApplicantResponseDto;
import com.demo.reserve.lecture.dto.LectureApplyRequestDto;
import com.demo.reserve.lecture.dto.LectureResponseDto;
import com.demo.reserve.lecture.dto.LectureSaveRequestDto;
import com.demo.reserve.lecture.repository.ApplicantRepository;
import com.demo.reserve.lecture.repository.LectureRepository;
import jakarta.validation.ConstraintViolationException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Unit Test
 */
@SpringBootTest
@Transactional
@ActiveProfiles("local")
class LectureServiceTest {

	@Autowired
	LectureService lectureService;

	@Autowired
	LectureRepository lectureRepository;

	@Autowired
	ApplicantRepository applicantRepository;

	@Test
	@DisplayName("[BackOffice] 강연목록")
	void findAll() {
		// given
		LectureSaveRequestDto requestDto = new LectureSaveRequestDto();
		requestDto.setLecturer("강연자");
		requestDto.setLectureRoom("테스트룸");
		requestDto.setLectureContent("테스트강연");
		requestDto.setLecturePeople(11);
		requestDto.setStartDt("20231021093000");
		requestDto.setEndDt("20231031130000");
		LectureResponseDto responseDto = lectureService.saveLecture(requestDto);

		// when
		List<LectureResponseDto> res = lectureService.findAll();

		// then
		assertThat(res).contains(responseDto);
	}

	@Test
	@DisplayName("[BackOffice] 강연등록")
	void saveLecture() {
		// given
		LectureSaveRequestDto requestDto = new LectureSaveRequestDto();
		requestDto.setLecturer("강연자");
		requestDto.setLectureRoom("테스트룸");
		requestDto.setLectureContent("테스트강연");
		requestDto.setLecturePeople(11);
		requestDto.setStartDt("20231021093000");
		requestDto.setEndDt("20231031130000");

		// when
		LectureResponseDto responseDto = lectureService.saveLecture(requestDto);

		// then
		Lecture findLecture = lectureRepository.findById(responseDto.getLectureId()).orElseThrow();
		assertThat(responseDto.getLecturer()).isEqualTo(findLecture.getLecturer());
		assertThat(responseDto.getLectureRoom()).isEqualTo(findLecture.getLectureRoom());
		assertThat(responseDto.getLectureContent()).isEqualTo(findLecture.getLectureContent());
		assertThat(responseDto.getLecturePeople()).isEqualTo(findLecture.getLecturePeople());
	}
	@Test
	@DisplayName("[BackOffice] 강연등록실패 - 필수파라미터 검증 실패")
	void saveLectureParamFail() {
		// given
		LectureSaveRequestDto requestDto = new LectureSaveRequestDto();
		requestDto.setLectureRoom("테스트룸");
		requestDto.setLectureContent("테스트강연");
		requestDto.setLecturePeople(11);
		requestDto.setStartDt("20231021093000");
		requestDto.setEndDt("20231031130000");

		// when
		assertThrows(ConstraintViolationException.class, () -> {
			LectureResponseDto responseDto = lectureService.saveLecture(requestDto);
		});
	}
	@Test
	@DisplayName("[BackOffice] 강연등록실패 - 파라미터 유효성 검증 실패")
	void saveLectureValidFail() {
		// given
		LectureSaveRequestDto requestDto = new LectureSaveRequestDto();
		requestDto.setLecturer("강연자");
		requestDto.setLectureRoom("테스트룸");
		requestDto.setLectureContent("테스트강연");
		requestDto.setLecturePeople(11);
		requestDto.setStartDt("20231021093000");
		requestDto.setEndDt("20231012130000");

		// when
		CustomException exception = assertThrows(CustomException.class, () -> {
			LectureResponseDto responseDto = lectureService.saveLecture(requestDto);
		});

		// then
		String message = exception.getMessage();
		assertThat(message).isEqualTo("강연시작일은 강연종료일 이전이어야 합니다.");
	}

	@Test
	@DisplayName("[BackOffice] 강연별 신청자 목록 조회 (전체강연)")
	void findLectureApplicant() {
		// given
		Applicant applicant = applicantRepository.save(
			Applicant.builder()
				.lecture(new Lecture(1))
				.empNo("TEST1")
				.build()
		);

		// when
		List<LectureApplicantResponseDto> response = lectureService.findLectureApplicant();

		// then
		assertThat(response).filteredOn(lecture -> lecture.getLectureId().equals(1))
			.filteredOn(applicants -> applicants.getApplicants().contains("TEST1"))
			.isNotEmpty();
	}

	@Test
	@DisplayName("[BackOffice] 강연별 신청자 목록 조회 (특정강연)")
	void findApplicantByLecture() {
		// given
		Applicant applicant = applicantRepository.save(
			Applicant.builder()
				.lecture(new Lecture(1))
				.empNo("TEST1")
				.build()
		);

		// when
		List<String> response = lectureService.findApplicantByLecture(1);

		// then
		assertThat(response).contains("TEST1");
	}

	@Test
	@DisplayName("[Front] 강연목록")
	void findLecturesByFront() {
		// given
		DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
		LocalDateTime today = LocalDateTime.now();

		// when
		List<LectureResponseDto> response = lectureService.findLecturesByFront();

		// then
		assertThat(response)
			.filteredOn(lecture -> LocalDateTime.parse(lecture.getStartDt(), format).isBefore(today.minusDays(1)))
			.isEmpty();
		assertThat(response)
			.filteredOn(lecture -> LocalDateTime.parse(lecture.getStartDt(), format).isAfter(today.plusDays(7)))
			.isEmpty();
	}

	@Test
	@DisplayName("[Front] 강연신청")
	void applyLecture() {
		// given

		LectureSaveRequestDto requestDto = new LectureSaveRequestDto();
		requestDto.setLecturer("강연자");
		requestDto.setLectureRoom("테스트룸");
		requestDto.setLectureContent("테스트강연");
		requestDto.setLecturePeople(11);
		requestDto.setStartDt("20231021093000");
		requestDto.setEndDt("20231031130000");
		LectureResponseDto responseDto = lectureService.saveLecture(requestDto);

		LectureApplyRequestDto applyRequestDto = new LectureApplyRequestDto();
		applyRequestDto.setLectureId(responseDto.getLectureId());
		applyRequestDto.setEmpNo("TEST1");

		// when
		lectureService.applyLecture(applyRequestDto);

		// then
		List<String> findEmpNo = lectureService.findApplicantByLecture(responseDto.getLectureId());
		assertThat(findEmpNo).contains("TEST1");
	}

	@Test
	@DisplayName("[Front] 강연신청실패 - 동시성검증")
	void applyLectureFail() throws Exception {
		//given
		int thread = 10;
		ExecutorService executorService = Executors.newFixedThreadPool(10);
		CountDownLatch latch = new CountDownLatch(thread);

		//when
		// 최대정원 5명의 강연에 10명의 강연신청을 병렬로 처리
		for (int i = 0; i < thread ; i++) {
			executorService.submit(() -> {
				String randomStr = RandomStringUtils.random(5, true, true);
				try {
					LectureApplyRequestDto applyDto = new LectureApplyRequestDto();
					applyDto.setLectureId(1);
					applyDto.setEmpNo(randomStr);
					lectureService.applyLecture(applyDto);
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					latch.countDown();
				}
			});
		}

		latch.await();

		//then
		List<String> response = lectureService.findApplicantByLecture(1);
		assertThat(response.size()).isEqualTo(5);

	}

	@Test
	@DisplayName("[Front] 신청내역조회")
	void findApplicantHis() {
		// given
		String empNo = "A2345";

		// when
		List<ApplicantHisResponseDto> response = lectureService.findApplicantHis(empNo);

		// then
		assertThat(response)
			.filteredOn(applicant -> applicant.getEmpNo().equals("A2345")).size()
			.isEqualTo(response.size());
	}

	@Test
	@DisplayName("[Front] 신청한 강연 취소")
	void cancelLecture() {
		// given
		Applicant applicant = applicantRepository.save(
			Applicant.builder()
				.lecture(new Lecture(1))
				.empNo("TEST1")
				.build()
		);

		LectureApplyRequestDto requestDto = new LectureApplyRequestDto();
		requestDto.setLectureId(1);
		requestDto.setEmpNo("TEST1");

		// when
		String response = lectureService.cancelLecture(requestDto);

		// then
		assertThat(response).isEqualTo("취소성공");
	}

	@Test
	@DisplayName("[Front] 실시간 인기 강연")
	void findPopularityLectures() {
		// given
		// when
		List<LectureResponseDto> response = lectureService.findPopularityLectures();

		// then
		assertThat(response).extracting("applicantNumber").isNotEqualTo(0);

	}
}
