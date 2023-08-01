package com.example.demo.member.controller;

import com.example.demo.common.api.ApiResult;
import com.example.demo.common.validation.ValidationSequence;
import com.example.demo.config.jwt.dto.TokenInfo;
import com.example.demo.member.dto.MemberAddRequestDto;
import com.example.demo.member.dto.MemberLoginRequestDto;
import com.example.demo.member.dto.MemberResponseDto;
import com.example.demo.member.dto.MemberUpdateDto;
import com.example.demo.member.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
@Tag(name = "member", description = "회원관련 API")
public class MemberController {

	private final MemberService memberService;


	@Operation(summary = "회원가입", description = "회원가입을 위한 API", tags = { "member"} )
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "회원가입 성공"),
		@ApiResponse(responseCode = "409", description = "회원ID 중복")
	})
	@ResponseStatus(HttpStatus.CREATED)
	@PostMapping(value = "/members/signup", produces = MediaType.APPLICATION_JSON_VALUE)
	public ApiResult<MemberResponseDto> signup(@Parameter(description = "가입하려는 회원정보", required = true)
			@Validated(ValidationSequence.class) @RequestBody MemberAddRequestDto requestDto) {
		MemberResponseDto saved = memberService.save(requestDto);
		return ApiResult.createSuccess(saved);
	}

	@Operation(summary = "로그인", description = "로그인을 위한 API", tags = { "member"} )
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "로그인 성공"),
		@ApiResponse(responseCode = "404", description = "로그인실패")
	})
	@PostMapping(value = "/members/login", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiResult<TokenInfo> login(@Parameter(description = "로그인하려는 ID/비밀번호", required = true)
			@Validated(ValidationSequence.class) @RequestBody MemberLoginRequestDto memberLoginRequestDto) {
        String memberId = memberLoginRequestDto.getMemberId();
        String memberPwd = memberLoginRequestDto.getMemberPwd();
		TokenInfo tokenInfo = memberService.login(memberId, memberPwd);
		return ApiResult.createSuccess(tokenInfo);
	}

	@Operation(summary = "회원조회", description = "회원정보를 조회하기위한 API", tags = { "member"} )
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "회원상세정보") })
	@GetMapping(value = "/members/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ApiResult<MemberResponseDto> findMemeber(@Parameter(description = "조회하려는 회원ID", required = true) @PathVariable("id") String memberId) {
		MemberResponseDto member = memberService.findByMemberId(memberId);
		return ApiResult.createSuccess(member);
	}

	@Operation(summary = "회원전체 목록조회", description = "회원전체 목록을 조회하기위한 API", tags = { "member"} )
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "회원목록") })
	@GetMapping(value = "/members", produces = MediaType.APPLICATION_JSON_VALUE)
	public ApiResult<List<MemberResponseDto>> findAll() {
		List<MemberResponseDto> members = memberService.findAll();
		return ApiResult.createSuccess(members);
	}

	@Operation(summary = "회원정보 수정", description = "회원정보를 수정하기위한 API", tags = { "member"} )
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "수정성공") })
	@PutMapping(value = "/members/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ApiResult<?> update(@Parameter(description = "수정하려는 회원정보", required = true) @RequestBody MemberUpdateDto memberUpdateDto
		, @Parameter(description = "수정하려는 회원 ID", required = true) @PathVariable("id") String memberId) {
		memberService.update(memberUpdateDto, memberId);
		return ApiResult.createSuccessWithNoContent();
	}
}
