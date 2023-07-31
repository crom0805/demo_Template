package com.example.demo.member.controller;

import com.example.demo.config.jwt.dto.TokenInfo;
import com.example.demo.member.dto.MemberAddRequestDto;
import com.example.demo.member.dto.MemberLoginRequestDto;
import com.example.demo.member.dto.MemberResponseDto;
import com.example.demo.member.dto.MemberUpdateDto;
import com.example.demo.member.service.MemberService;
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
public class MemberController {

	private final MemberService memberService;

	@PostMapping("/members/signup")
	public MemberResponseDto signup(@RequestBody MemberAddRequestDto requestDto) {
		return memberService.save(requestDto);
	}

	@PostMapping("/members/login")
    public TokenInfo login(@RequestBody MemberLoginRequestDto memberLoginRequestDto) {
        String memberId = memberLoginRequestDto.getMemberId();
        String memberPwd = memberLoginRequestDto.getMemberPwd();
		return memberService.login(memberId, memberPwd);
    }

	@GetMapping("/members/{id}")
	public MemberResponseDto findMemeber(@PathVariable("id") String memberId) {
		return memberService.findByMemberId(memberId);
	}

	@GetMapping("/members")
	public List<MemberResponseDto> findAll() {
		return memberService.findAll();
	}

	@PutMapping("/members/{id}")
	public void update(@RequestBody MemberUpdateDto memberUpdateDto, @PathVariable("id") String memberId) {
		memberService.update(memberUpdateDto, memberId);
	}
}
