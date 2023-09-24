package com.example.demo.member.repository;

import com.example.demo.member.dto.MemberResponseDto;
import com.example.demo.member.dto.MemberSearchDto;
import java.util.List;

public interface MemberRepositoryCustom {

	List<MemberResponseDto> findMembers(MemberSearchDto memberSearchDto);
}
