package com.example.demo.member.service;

import com.example.demo.config.jwt.JwtTokenProvider;
import com.example.demo.config.jwt.dto.TokenInfo;
import com.example.demo.member.dto.MemberAddRequestDto;
import com.example.demo.member.dto.MemberResponseDto;
import com.example.demo.member.dto.MemberUpdateDto;
import com.example.demo.member.entity.Member;
import com.example.demo.member.repository.MemberRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
			.refreshToken(savedMember.getRefreshToken())
			.build();
	}

	private void validateDuplicateMember(MemberAddRequestDto requestDto) {
		if (memberRepository.existsByMemberId(requestDto.getMemberId())) {
			throw new IllegalStateException("이미 존재하는 회원ID입니다.");
		}
	}

	/**
	 * 로그인(=jwt 인증)
	 */
	@Transactional
    public TokenInfo login(String memberId, String password) {
		TokenInfo tokenInfo = null;
		try {
			// 1. Login ID/PW 를 기반으로 Authentication 객체 생성
			// 이때 authentication 는 인증 여부를 확인하는 authenticated 값이 false
			UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(memberId, password);

			// 2. 실제 검증 (사용자 비밀번호 체크)이 이루어지는 부분
			// authenticate 매서드가 실행될 때 CustomUserDetailsService 에서 만든 loadUserByUsername 메서드가 실행
			Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        	// 3. 인증 정보를 기반으로 JWT 토큰 생성
        	tokenInfo = jwtTokenProvider.generateToken(authentication);

			// 4. refreshToken 저장
			Member member = memberRepository.findByMemberId(memberId)
				.orElseThrow();
			member.updateRefreshToken(tokenInfo.getRefreshToken());

		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}

        return tokenInfo;
    }

	public MemberResponseDto findByMemberId(String memberId) {
		Member findMember = memberRepository.findByMemberId(memberId)
			.orElseThrow(() -> new IllegalArgumentException("해당 사용자를 찾을수 없습니다."));

		return MemberResponseDto.builder()
			.memberId(findMember.getMemberId())
			.memberName(findMember.getMemberName())
			.memberTel(findMember.getMemberTel())
			//.memberState(findMember.getMemberState())
			.refreshToken(findMember.getRefreshToken())
			.build();
	}

	public List<MemberResponseDto> findAll() {
		return memberRepository.findAll()
			.stream()
			.map(MemberResponseDto::new)
			.collect(Collectors.toList());
	}

	@Transactional
	public void update(MemberUpdateDto memberUpdateDto, String memberId) {
		Member member = memberRepository.findByMemberId(memberId)
			.orElseThrow();
		BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
		memberUpdateDto.setMemberPwd(bCryptPasswordEncoder.encode(memberUpdateDto.getMemberPwd()));
		member.updateMember(memberUpdateDto);
	}
}
