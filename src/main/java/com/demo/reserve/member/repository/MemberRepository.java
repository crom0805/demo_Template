package com.demo.reserve.member.repository;

import com.demo.reserve.member.domain.Member;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Integer>, MemberRepositoryCustom {

	Optional<Member> findByLoginId(String loginId);

	boolean existsByLoginId(String loginId);
}
