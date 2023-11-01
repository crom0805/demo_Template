package com.demo.reserve.member.controller;

import com.demo.common.api.ApiResult;
import com.demo.common.validation.ValidationSequence;
import com.demo.config.jwt.dto.TokenInfo;
import com.demo.reserve.member.dto.MemberAddRequestDto;
import com.demo.reserve.member.dto.MemberLoginRequestDto;
import com.demo.reserve.member.dto.MemberResponseDto;
import com.demo.reserve.member.dto.MemberSearchDto;
import com.demo.reserve.member.dto.MemberUpdateDto;
import com.demo.reserve.member.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "1. 회원 API")
public class MemberController {

	private final MemberService memberService;

	@PostMapping("/front/members/signup")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "201", description = "회원가입 성공")
	})
	@Operation(summary = "회원가입", description = "회원가입을 진행합니다.")
	@ResponseStatus(HttpStatus.CREATED)
	public ApiResult<MemberResponseDto> signup(@Parameter(description = "가입하려는 회원정보", required = true)
	@Validated(ValidationSequence.class) @RequestBody MemberAddRequestDto requestDto) {
		MemberResponseDto saved = memberService.save(requestDto);
		return ApiResult.createSuccess(saved);
	}

	@PostMapping("/front/members/login")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "로그인 성공")
	})
	@Operation(summary = "로그인", description = "ID와 비밀번호를 이용하여 로그인합니다.")
	public ApiResult<TokenInfo> login(@Parameter(description = "로그인하려는 ID/비밀번호", required = true)
			@Validated(ValidationSequence.class) @RequestBody MemberLoginRequestDto memberLoginRequestDto) {
		String loginId = memberLoginRequestDto.getLoginId();
		String loginPw = memberLoginRequestDto.getLoginPw();
		TokenInfo tokenInfo = memberService.login(loginId, loginPw);
		return ApiResult.createSuccess(tokenInfo);
	}

	@GetMapping("/back/member/{id}")
	@ApiResponses(value = {@ApiResponse(responseCode = "200", description = "회원상세정보")})
	@Operation(summary = "회원상세조회", description = "회원의 상세정보를 조회합니다.")
	public ApiResult<MemberResponseDto> findMemeber(@Parameter(description = "조회하려는 회원 로그인ID(이메일)", required = true, example = "test@test.com")
			@PathVariable("id") String memberId) {
		MemberResponseDto member = memberService.findByMemberId(memberId);
		return ApiResult.createSuccess(member);
	}

	@GetMapping("/back/members")
	@ApiResponses(value = {@ApiResponse(responseCode = "200", description = "회원목록")})
	@Operation(summary = "회원목록조회", description = "검색조건을 이용하여 회원목록을 조회합니다.")
	public ApiResult<List<MemberResponseDto>> findMembers(@ParameterObject MemberSearchDto memberSearchDto) {
		List<MemberResponseDto> members = memberService.findMembers(memberSearchDto);
		return ApiResult.createSuccess(members);
	}

	@PutMapping("/front/members/{id}")
	@ApiResponses(value = {@ApiResponse(responseCode = "200", description = "수정성공")})
	@Operation(summary = "회원수정", description = "회원정보를 수정합니다.")
	public ApiResult<?> update(@Parameter(description = "수정하려는 회원정보", required = true) @RequestBody MemberUpdateDto memberUpdateDto
		, @Parameter(description = "수정하려는 회원 로그인 ID", required = true, example = "test@test.com") @PathVariable("id") String memberId) {
		memberService.update(memberUpdateDto, memberId);
		return ApiResult.createSuccessWithNoContent();
	}
}
