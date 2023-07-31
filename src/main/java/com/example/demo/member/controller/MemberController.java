package com.example.demo.member.controller;

import com.example.demo.config.jwt.dto.TokenInfo;
import com.example.demo.member.dto.MemberAddRequestDto;
import com.example.demo.member.dto.MemberLoginRequestDto;
import com.example.demo.member.dto.MemberResponseDto;
import com.example.demo.member.dto.MemberUpdateDto;
import com.example.demo.member.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "member", description = "회원관련 API")
public class MemberController {

	private final MemberService memberService;


	@Operation(summary = "회원가입", description = "회원가입을 위한 API", tags = { "member"} )
	@ApiResponses(value = {
		@ApiResponse(description = "회원가입 성공"
			, content = { @Content(mediaType = "application/json", schema = @Schema(implementation = MemberResponseDto.class)) }
		)
	})
	@PostMapping("/members/signup")
	public MemberResponseDto signup(@Parameter(description = "가입하려는 회원정보", required = true) @RequestBody MemberAddRequestDto requestDto) {
		return memberService.save(requestDto);
	}

	@Operation(summary = "로그인", description = "로그인을 위한 API", tags = { "member"} )
	@ApiResponses(value = {
		@ApiResponse(description = "로그인 성공"
			, content = { @Content(mediaType = "application/json", schema = @Schema(implementation = TokenInfo.class)) }
		)
	})
	@PostMapping("/members/login")
    public TokenInfo login(@Parameter(description = "로그인하려는 ID/비밀번호", required = true) @RequestBody MemberLoginRequestDto memberLoginRequestDto) {
        String memberId = memberLoginRequestDto.getMemberId();
        String memberPwd = memberLoginRequestDto.getMemberPwd();
		return memberService.login(memberId, memberPwd);
    }

	@Operation(summary = "회원조회", description = "회원정보를 조회하기위한 API", tags = { "member"} )
	@ApiResponses(value = {
		@ApiResponse(description = "회원상세정보"
			, content = { @Content(mediaType = "application/json", schema = @Schema(implementation = MemberResponseDto.class)) }
		),
		@ApiResponse(responseCode = "403", description = "Forbidden - AccessToken이 없거나 잘못되었습니다.")
	})
	@GetMapping("/members/{id}")
	public MemberResponseDto findMemeber(@Parameter(description = "조회하려는 회원ID", required = true) @PathVariable("id") String memberId) {
		return memberService.findByMemberId(memberId);
	}

	@Operation(summary = "회원전체 목록조회", description = "회원전체 목록을 조회하기위한 API", tags = { "member"} )
	@ApiResponses(value = {
		@ApiResponse(description = "회원목록"
			, content = { @Content(mediaType = "application/json", schema = @Schema(implementation = MemberResponseDto.class)) }
		),
		@ApiResponse(responseCode = "403", description = "Forbidden - AccessToken이 없거나 잘못되었습니다.")
	})
	@GetMapping("/members")
	public List<MemberResponseDto> findAll() {
		return memberService.findAll();
	}

	@Operation(summary = "회원정보 수정", description = "회원정보를 수정하기위한 API", tags = { "member"} )
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "수정성공", content = { @Content(mediaType = "application/json") }),
		@ApiResponse(responseCode = "403", description = "Forbidden - AccessToken이 없거나 잘못되었습니다.")
	})
	@PutMapping("/members/{id}")
	public void update(@Parameter(description = "수정하려는 회원정보", required = true) @RequestBody MemberUpdateDto memberUpdateDto
		, @Parameter(description = "수정하려는 회원 ID", required = true) @PathVariable("id") String memberId) {
		memberService.update(memberUpdateDto, memberId);
	}
}
