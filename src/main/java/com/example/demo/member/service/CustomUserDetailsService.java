package com.example.demo.member.service;

import com.example.demo.common.exception.UserNotFoundException;
import com.example.demo.member.entity.Member;
import com.example.demo.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        return memberRepository.findByMemberId(username)
            .map(this::createUserDetails)
            .orElseThrow(UserNotFoundException::new);
    }

    // 해당하는 User 의 데이터가 존재한다면 UserDetails 객체로 만들어서 리턴
    private UserDetails createUserDetails(Member member) {
        try {

            return User.builder()
                .username(member.getUsername())
                .password(member.getPassword())
                //.roles(List.of(new SimpleGrantedAuthority("user")).toArray(new String[0]))
                .build();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
