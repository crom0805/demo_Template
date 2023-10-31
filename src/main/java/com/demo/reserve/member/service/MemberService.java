package com.demo.reserve.member.service;

import com.demo.common.exception.DuplicatedUserException;
import com.demo.common.exception.UserNotFoundException;
import com.demo.config.jwt.JwtTokenProvider;
import com.demo.config.jwt.dto.TokenInfo;
import com.demo.reserve.member.dto.MemberAddRequestDto;
import com.demo.reserve.member.dto.MemberResponseDto;
import com.demo.reserve.member.dto.MemberSearchDto;
import com.demo.reserve.member.dto.MemberUpdateDto;
import com.demo.reserve.member.domain.Member;
import com.demo.reserve.member.repository.MemberRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

	private final MemberRepository memberRepository;
	private final AuthenticationManagerBuilder authenticationManagerBuilder;
	private final JwtTokenProvider jwtTokenProvider;

	/**
	 * 저장(회원가입)
	 */
	@Transactional
	public MemberResponseDto save(MemberAddRequestDto requestDto) {
		// member id validation check
		validateDuplicateMember(requestDto);

		BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
		Member savedMember = memberRepository.save(Member.builder()
			.memberId(requestDto.getMemberId())
			.memberPwd(bCryptPasswordEncoder.encode(requestDto.getMemberPwd()))
			.memberName(requestDto.getMemberName())
			.memberTel(requestDto.getMemberTel())
			.build());

		return MemberResponseDto.builder()
			.memberId(savedMember.getMemberId())
			.memberName(savedMember.getMemberName())
			.memberTel(savedMember.getMemberTel())
			//.memberState(savedMember.getMemberState())
//			.refreshToken(savedMember.getRefreshToken())
			.build();
	}

	private void validateDuplicateMember(MemberAddRequestDto requestDto) {
		if (memberRepository.existsByMemberId(requestDto.getMemberId())) {
			throw new DuplicatedUserException();
		}
	}

	/**
	 * 로그인(=jwt 인증)
	 */
	@Transactional
	public TokenInfo login(String memberId, String password) {

		TokenInfo tokenInfo = null;
		try {
			log.debug("Authentication 객체 생성");
			// 1. Login ID/PW 를 기반으로 Authentication 객체 생성
			UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(memberId, password);

			log.debug("CustomUserDetailsService.loadUserByUsername 실행");

			// 2. 실제 검증. CustomUserDetailsService.loadUserByUsername 실행
			Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

			// 3. 인증 정보를 기반으로 JWT 토큰 생성
			tokenInfo = jwtTokenProvider.generateToken(authentication);

			// 4. refreshToken 저장
			Member member = memberRepository.findByMemberId(memberId)
				.orElseThrow();
			member.updateRefreshToken(tokenInfo.getRefreshToken());
		} catch (InternalAuthenticationServiceException authenticationServiceException) {
			throw new UserNotFoundException();
		} catch (Exception e) {
			throw e;
		}
		return tokenInfo;
	}

	public MemberResponseDto findByMemberId(String memberId) {
		Member findMember = memberRepository.findByMemberId(memberId)
			.orElseThrow(UserNotFoundException::new);

		return MemberResponseDto.builder()
			.memberId(findMember.getMemberId())
			.memberName(findMember.getMemberName())
			.memberTel(findMember.getMemberTel())
			//.memberState(findMember.getMemberState())
//			.refreshToken(findMember.getRefreshToken())
			.build();
	}

	public List<MemberResponseDto> findAll() {
		return memberRepository.findAll()
			.stream()
			.map(MemberResponseDto::new)
			.collect(Collectors.toList());
	}

	public List<MemberResponseDto> findMembers(MemberSearchDto memberSearchDto) {
		return memberRepository.findMembers(memberSearchDto);
	}

	@Transactional
	public void update(MemberUpdateDto memberUpdateDto, String memberId) {
		Member member = memberRepository.findByMemberId(memberId)
			.orElseThrow(UserNotFoundException::new);
		BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
		memberUpdateDto.setMemberPwd(bCryptPasswordEncoder.encode(memberUpdateDto.getMemberPwd()));
		member.updateMember(memberUpdateDto);
	}
}
