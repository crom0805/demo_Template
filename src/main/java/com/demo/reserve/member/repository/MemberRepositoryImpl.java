package com.demo.reserve.member.repository;

import static com.demo.reserve.member.domain.QMember.member;

import com.demo.reserve.member.dto.MemberResponseDto;
import com.demo.reserve.member.dto.MemberSearchDto;
import com.demo.reserve.member.dto.QMemberResponseDto;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import java.util.List;
import org.springframework.util.StringUtils;

public class MemberRepositoryImpl implements MemberRepositoryCustom {

	private final JPAQueryFactory queryFactory;

	public MemberRepositoryImpl(EntityManager em) {
		this.queryFactory = new JPAQueryFactory(em);
	}

	@Override
	public List<MemberResponseDto> findMembers(MemberSearchDto memberSearchDto) {
		return queryFactory
			.select(new QMemberResponseDto(
				member.id
				, member.loginId
				, member.memberName
				, member.memberTel
			))
			.from(member)
			.where(
				loginIdEq(memberSearchDto.getLoginId())
				, memberNameLike(memberSearchDto.getMemberName())
				, memberTelLike(memberSearchDto.getMemberTel())
			)
			.fetch();
	}

	private BooleanExpression loginIdEq(String loginId) {
		return StringUtils.hasText(loginId) ? member.loginId.eq(loginId) : null;
	}

	private BooleanExpression memberNameLike(String memberName) {
		return StringUtils.hasText(memberName) ? member.memberName.contains(memberName) : null;
	}

	private BooleanExpression memberTelLike(String memberTel) {
		return StringUtils.hasText(memberTel) ? member.memberTel.contains(memberTel) : null;
	}
}
