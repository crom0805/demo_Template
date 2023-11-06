package com.demo.reserve.member.service;

import com.demo.common.exception.DuplicatedUserException;
import com.demo.common.exception.UserNotFoundException;
import com.demo.config.jwt.JwtTokenProvider;
import com.demo.config.jwt.dto.TokenInfo;
import com.demo.config.security.Role;
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
import org.springframework.security.core.GrantedAuthority;
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
				.loginId(requestDto.getLoginId())
				.loginPw(bCryptPasswordEncoder.encode(requestDto.getLoginPw()))
				.memberName(requestDto.getMemberName())
				.memberTel(requestDto.getMemberTel())
				.regId(requestDto.getLoginId())
				.modId(requestDto.getLoginId())
				.role(Role.USER.getValue())
			.build());

		return MemberResponseDto.builder()
			.loginId(savedMember.getLoginId())
			.memberName(savedMember.getMemberName())
			.memberTel(savedMember.getMemberTel())
			.build();
	}

	/**
	 * 어드민 등록
	 */
	@Transactional
	public MemberResponseDto saveAdmin(MemberAddRequestDto requestDto) {
		// member id validation check
		validateDuplicateMember(requestDto);

		BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
		Member savedMember = memberRepository.save(Member.builder()
				.loginId(requestDto.getLoginId())
				.loginPw(bCryptPasswordEncoder.encode(requestDto.getLoginPw()))
				.memberName(requestDto.getMemberName())
				.memberTel(requestDto.getMemberTel())
				.regId(requestDto.getLoginId())
				.modId(requestDto.getLoginId())
				.role(Role.ADMIN.getValue())
			.build());

		return MemberResponseDto.builder()
			.loginId(savedMember.getLoginId())
			.memberName(savedMember.getMemberName())
			.memberTel(savedMember.getMemberTel())
			.build();
	}

	private void validateDuplicateMember(MemberAddRequestDto requestDto) {
		if (memberRepository.existsByLoginId(requestDto.getLoginId())) {
			throw new DuplicatedUserException();
		}
	}

	/**
	 * 로그인(=jwt 인증)
	 */
	@Transactional
	public TokenInfo login(String loginId, String password, String role) {

		TokenInfo tokenInfo = null;
		try {
			log.debug("Authentication 객체 생성");
			// 1. Login ID/PW 를 기반으로 Authentication 객체 생성
			UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginId, password);

			log.debug("CustomUserDetailsService.loadUserByUsername 실행");

			// 2. 실제 검증. CustomUserDetailsService.loadUserByUsername 실행
			Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

			// 권한 가져오기
			String authorities = authentication.getAuthorities().stream()
				.map(GrantedAuthority::getAuthority)
				.collect(Collectors.joining(","));

			if (!role.equals(authorities)) {
				throw new UserNotFoundException();
			}

			// 3. 인증 정보를 기반으로 JWT 토큰 생성
			tokenInfo = jwtTokenProvider.generateToken(authentication, authorities);

			// 4. refreshToken 저장
			Member member = memberRepository.findByLoginId(loginId)
				.orElseThrow();
			member.updateRefreshToken(tokenInfo.getRefreshToken());
		} catch (InternalAuthenticationServiceException authenticationServiceException) {
			throw new UserNotFoundException();
		} catch (Exception e) {
			throw e;
		}
		return tokenInfo;
	}

	public MemberResponseDto findByMemberId(String loginId) {
		Member findMember = memberRepository.findByLoginId(loginId)
			.orElseThrow(UserNotFoundException::new);

		return MemberResponseDto.builder()
			.memberId(findMember.getId())
			.loginId(findMember.getLoginId())
			.memberName(findMember.getMemberName())
			.memberTel(findMember.getMemberTel())
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
	public void update(MemberUpdateDto memberUpdateDto, String loginId) {
		Member member = memberRepository.findByLoginId(loginId)
			.orElseThrow(UserNotFoundException::new);
		BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
		memberUpdateDto.setLoginPw(bCryptPasswordEncoder.encode(memberUpdateDto.getLoginPw()));
		member.updateMember(memberUpdateDto);
	}
}
