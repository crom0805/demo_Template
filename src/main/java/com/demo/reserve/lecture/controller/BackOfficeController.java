package com.demo.reserve.lecture.controller;

import com.demo.common.api.ApiResult;
import com.demo.common.validation.ValidationSequence;
import com.demo.reserve.lecture.dto.LectureApplicantResponseDto;
import com.demo.reserve.lecture.dto.LectureResponseDto;
import com.demo.reserve.lecture.dto.LectureSaveRequestDto;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * BackOffice API Controller
 */
@RestController
@RequestMapping("/back")
@RequiredArgsConstructor
@Tag(name = "2. 강연 API")
public class BackOfficeController {

	private final LectureService lectureService;

	@GetMapping("/lectures")
	@Operation(summary = "강연 목록(전체 강연 목록)", description = "전체 강연목록을 조회합니다.")
	@ApiResponses(value = {@ApiResponse(responseCode = "200", description = "강연목록")})
	public ApiResult<List<LectureResponseDto>> findLectures() {
		List<LectureResponseDto> responseDto = lectureService.findAll();
		return ApiResult.createSuccess(responseDto);
	}

	@PostMapping("/lecture")
	@ResponseStatus(HttpStatus.CREATED)
	@Operation(summary = "강연 등록", description = "강연자, 강연장, 신청인원, 강연시간, 강연내용을 입력하여 강연정보를 등록합니다.")
	@ApiResponses(value = {@ApiResponse(responseCode = "201", description = "강연등록 성공")})
	public ApiResult<LectureResponseDto> saveLecture(@Parameter(description = "등록하려는 강연정보", required = true)
			@Validated(ValidationSequence.class) @RequestBody LectureSaveRequestDto requestDto) {
		LectureResponseDto responseDto = lectureService.saveLecture(requestDto);
		return ApiResult.createSuccess(responseDto);
	}

	@GetMapping("/lecture/applicant")
	@Operation(summary = "강연신청자 사번목록 (전체강연)", description = "각 강연별 신청한 사번목록을 조회합니다.")
	@ApiResponses(value = {@ApiResponse(responseCode = "200", description = "강연목록과 각 강연별 신청자사번목록")})
	public ApiResult<List<LectureApplicantResponseDto>> findLectureApplicant() {
		List<LectureApplicantResponseDto> responseDto = lectureService.findLectureApplicant();
		return ApiResult.createSuccess(responseDto);
	}

	@GetMapping("/applicant/{lectureId}")
	@Operation(summary = "강연신청자 사번목록(특정강연)", description = "특정 강연을 신청한 사번목록을 조회합니다.")
	@ApiResponses(value = {@ApiResponse(responseCode = "200", description = "신청자사번목록")})
	public ApiResult<List<String>> findApplicantByLecture(@Parameter(description = "조회하려는 강연ID", required = true, example = "1")
			@PathVariable("lectureId") Integer lectureId) {
		List<String> responseDto = lectureService.findApplicantByLecture(lectureId);
		return ApiResult.createSuccess(responseDto);
	}

}
