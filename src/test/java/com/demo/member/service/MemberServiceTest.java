package com.demo.member.service;


import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.demo.common.exception.DuplicatedUserException;
import com.demo.config.jwt.dto.TokenInfo;
import com.demo.reserve.member.dto.MemberAddRequestDto;
import com.demo.reserve.member.dto.MemberResponseDto;
import com.demo.reserve.member.dto.MemberSearchDto;
import com.demo.reserve.member.dto.MemberUpdateDto;
import com.demo.reserve.member.domain.Member;
import com.demo.reserve.member.repository.MemberRepository;
import com.demo.reserve.member.service.MemberService;
import java.util.List;
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
class MemberServiceTest {

	@Autowired
	private MemberService memberService;

	@Autowired
	private MemberRepository memberRepository;

	@Test
	@DisplayName("회원가입")
	void save() {
		// given
		MemberAddRequestDto requestDto = new MemberAddRequestDto();
		requestDto.setMemberId("test@junit.com");
		requestDto.setMemberPwd("qwer1234!@");
		requestDto.setMemberName("테스트유저");

		// when
		memberService.save(requestDto);

		// then
		Member findMember = memberRepository.findByMemberId(requestDto.getMemberId()).orElseThrow();
		assertThat(findMember.getMemberId()).isEqualTo("test@junit.com");
		assertThat(findMember.getMemberName()).isEqualTo("테스트유저");
	}

	@Test
	@DisplayName("회원가입실패-아이디중복")
	void saveFail() {
		// given
		MemberAddRequestDto requestDto = new MemberAddRequestDto();
		requestDto.setMemberId("test@test.com");
		requestDto.setMemberPwd("qwer1234!@");
		requestDto.setMemberName("테스트유저");

		// when
		assertThrows(DuplicatedUserException.class, () -> {
			memberService.save(requestDto);
		});
	}

	@Test
	@DisplayName("로그인")
	void login() {
		// given
		String id = "test@test.com";
		String pwd = "1q2w3e4r";

		// when
		TokenInfo tokenInfo = memberService.login(id, pwd);

		// then
		assertThat(tokenInfo).isNotNull();
		assertThat(tokenInfo.getAccessToken()).isNotNull();
		assertThat(tokenInfo.getRefreshToken()).isNotNull();
	}

	@Test
	@DisplayName("회원상세조회")
	void findByMemberId() {
		// given
		String id = "test@test.com";

		// when
		MemberResponseDto findMember = memberService.findByMemberId(id);

		// then
		assertThat(findMember.getMemberId()).isEqualTo(id);
	}

	@Test
	@DisplayName("회원목록조회_검색조건")
	void findMembers() {
		// given
		MemberSearchDto requestDto = new MemberSearchDto();
		requestDto.setMemberName("팜하니");
		requestDto.setMemberTel("01012345678");

		// when
		List<MemberResponseDto> findMembers = memberService.findMembers(requestDto);

		// then
		assertThat(findMembers).extracting("memberName").contains("팜하니");
	}

	@Test
	@DisplayName("회원수정")
	void update() {
		// given
		String id = "test@test.com";
		MemberUpdateDto updateDto = new MemberUpdateDto();
		updateDto.setMemberName("팜하니_수정");
		updateDto.setMemberPwd("1q2w3e4r");
		updateDto.setMemberTel("01099995555");

		// when
		memberService.update(updateDto, id);

		// then
		MemberResponseDto findMember = memberService.findByMemberId(id);
		assertThat(findMember.getMemberName()).isEqualTo("팜하니_수정");
		assertThat(findMember.getMemberTel()).isEqualTo("01099995555");
	}
}
