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
public class MemberController {

	private final MemberService memberService;

	@Tag(name = "1-회원가입")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "201", description = "회원가입 성공")
	})
	@ResponseStatus(HttpStatus.CREATED)
	@PostMapping("/members/signup")
	public ApiResult<MemberResponseDto> signup(@Parameter(description = "가입하려는 회원정보", required = true)
	@Validated(ValidationSequence.class) @RequestBody MemberAddRequestDto requestDto) {
		MemberResponseDto saved = memberService.save(requestDto);
		return ApiResult.createSuccess(saved);
	}

	@Tag(name = "2-로그인")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "로그인 성공")
	})
	@PostMapping("/members/login")
	public ApiResult<TokenInfo> login(@Parameter(description = "로그인하려는 ID/비밀번호", required = true)
	@Validated(ValidationSequence.class) @RequestBody MemberLoginRequestDto memberLoginRequestDto) {
		String memberId = memberLoginRequestDto.getMemberId();
		String memberPwd = memberLoginRequestDto.getMemberPwd();
		TokenInfo tokenInfo = memberService.login(memberId, memberPwd);
		return ApiResult.createSuccess(tokenInfo);
	}

	@Tag(name = "3-회원상세조회")
	@ApiResponses(value = {@ApiResponse(responseCode = "200", description = "회원상세정보")})
	@GetMapping("/member/{id}")
	public ApiResult<MemberResponseDto> findMemeber(@Parameter(description = "조회하려는 회원ID", required = true) @PathVariable("id") String memberId) {
		MemberResponseDto member = memberService.findByMemberId(memberId);
		return ApiResult.createSuccess(member);
	}

//	@Tag(name = "4-회원전체조회")
//	@ApiResponses(value = {@ApiResponse(responseCode = "200", description = "회원목록")})
//	@GetMapping("/members")
//	public ApiResult<List<MemberResponseDto>> findAll() {
//		List<MemberResponseDto> members = memberService.findAll();
//		return ApiResult.createSuccess(members);
//	}

	@Tag(name = "4-회원목록조회_검색조건")
	@ApiResponses(value = {@ApiResponse(responseCode = "200", description = "회원목록")})
	@GetMapping("/members")
	public ApiResult<List<MemberResponseDto>> findMembers(@ParameterObject MemberSearchDto memberSearchDto) {
		List<MemberResponseDto> members = memberService.findMembers(memberSearchDto);
		return ApiResult.createSuccess(members);
	}

	@Tag(name = "6-회원수정")
	@ApiResponses(value = {@ApiResponse(responseCode = "200", description = "수정성공")})
	@PutMapping("/members/{id}")
	public ApiResult<?> update(@Parameter(description = "수정하려는 회원정보", required = true) @RequestBody MemberUpdateDto memberUpdateDto
		, @Parameter(description = "수정하려는 회원 ID", required = true) @PathVariable("id") String memberId) {
		memberService.update(memberUpdateDto, memberId);
		return ApiResult.createSuccessWithNoContent();
	}
}
