package com.example.demo.member.repository;

import static com.example.demo.member.entity.QMember.member;

import com.example.demo.member.dto.MemberResponseDto;
import com.example.demo.member.dto.MemberSearchDto;
import com.example.demo.member.dto.QMemberResponseDto;
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
				member.memberId
				, member.memberName
				, member.memberTel
			))
			.from(member)
			.where(
				memberIdEq(memberSearchDto.getMemberId())
				, memberNameLike(memberSearchDto.getMemberName())
				, memberTelLike(memberSearchDto.getMemberTel())
			)
			.fetch();
	}

	private BooleanExpression memberIdEq(String memberId) {
		return StringUtils.hasText(memberId) ? member.memberId.eq(memberId) : null;
	}

	private BooleanExpression memberNameLike(String memberName) {
		return StringUtils.hasText(memberName) ? member.memberName.contains(memberName) : null;
	}

	private BooleanExpression memberTelLike(String memberTel) {
		return StringUtils.hasText(memberTel) ? member.memberTel.contains(memberTel) : null;
	}
}
