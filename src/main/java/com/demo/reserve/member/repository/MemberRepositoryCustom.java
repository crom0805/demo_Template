package com.demo.reserve.member.repository;

import com.demo.reserve.member.dto.MemberResponseDto;
import com.demo.reserve.member.dto.MemberSearchDto;
import java.util.List;

public interface MemberRepositoryCustom {

	List<MemberResponseDto> findMembers(MemberSearchDto memberSearchDto);
}
