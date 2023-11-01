package com.demo.reserve.lecture.controller;

import com.demo.common.api.ApiResult;
import com.demo.common.validation.ValidationSequence;
import com.demo.reserve.lecture.dto.ApplicantHisResponseDto;
import com.demo.reserve.lecture.dto.ApplicantResponseDto;
import com.demo.reserve.lecture.dto.LectureApplyRequestDto;
import com.demo.reserve.lecture.dto.LectureResponseDto;
import com.demo.reserve.lecture.service.LectureService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * Front API Controller
 */
@RestController
@RequestMapping("/front")
@RequiredArgsConstructor
@Tag(name = "2. 강연 API")
public class FrontController {

	private final LectureService lectureService;

	/**
	 * 강연목록
	 */
	@GetMapping("/lectures")
	@Operation(summary = "강연 목록", description = "신청 가능한 싯점(=강연시작시간 1주일 전)터 강연시작시간 1일 후까지 조회합니다.")
	@ApiResponses(value = {@ApiResponse(responseCode = "200", description = "강연목록")})
	public ApiResult<List<LectureResponseDto>> findLectures() {
		List<LectureResponseDto> responseDto = lectureService.findLecturesByFront();
		return ApiResult.createSuccess(responseDto);
	}

	/**
	 * 강연신청
	 */
	@PostMapping("/applicant")
	@ResponseStatus(HttpStatus.CREATED)
	@Operation(summary = "강연 신청", description = "사번 입력, 같은 강연 중복 신청 제한")
	@ApiResponses(value = {@ApiResponse(responseCode = "201", description = "강연신청 성공")})
	public ApiResult<ApplicantResponseDto> applyLecture(@Parameter(description = "강연신청정보", required = true)
		@Validated(ValidationSequence.class) @RequestBody LectureApplyRequestDto requestDto) throws Exception {
		ApplicantResponseDto responseDto = lectureService.applyLecture(requestDto);
		return ApiResult.createSuccess(responseDto);
	}

	/**
	 * 강연신청내역
	 */
	@GetMapping("/applicant/{memberId}")
	@Operation(summary = "강연 신청내역 조회", description = "사번 입력하여 신청내역을 조회.")
	@ApiResponses(value = {@ApiResponse(responseCode = "200", description = "강연신청내역")})
	public ApiResult<List<ApplicantHisResponseDto>> findApplicantHis(@Parameter(description = "조회하려는 회원ID", required = true)
			@PathVariable("memberId") Integer memberId) {
		List<ApplicantHisResponseDto> responseDto = lectureService.findApplicantHis(memberId);
		return ApiResult.createSuccess(responseDto);
	}

	/**
	 * 신청취소
	 */
	@DeleteMapping("/lecture/applicant")
	@Operation(summary = "강연 신청 취소", description = "사번 입력하여 강연신청 취소.")
	@ApiResponses(value = {@ApiResponse(responseCode = "200", description = "강연신청 취소")})
	public ApiResult<String> cancelLecture(@Parameter(description = "취소하려는 강연신청 정보", required = true) @RequestBody LectureApplyRequestDto requestDto) {
		String result = lectureService.cancelLecture(requestDto);
		return ApiResult.createSuccess(result);
	}

	/**
	 * 실시간 인기강연
	 */
	@GetMapping("/lecture/popularity")
	@Operation(summary = "실시간 인기강연", description = "3일간 가장 신청이 많은 강연순으로 조회")
	@ApiResponses(value = {@ApiResponse(responseCode = "200", description = "인기강연목록")})
	public ApiResult<List<LectureResponseDto>> findPopularityLectures() {
		List<LectureResponseDto> responseDto = lectureService.findPopularityLectures();
		return ApiResult.createSuccess(responseDto);
	}
}
