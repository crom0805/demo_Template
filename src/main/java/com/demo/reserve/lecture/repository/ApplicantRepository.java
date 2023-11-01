package com.demo.reserve.lecture.repository;

import com.demo.reserve.lecture.domain.Applicant;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 신청자관련 Repository
 */
public interface ApplicantRepository extends JpaRepository<Applicant, Integer> {

	/**
	 * 특정 강연의 신청자목록 조회
	 */
	List<Applicant> findByLectureId(Integer lectureId);

	/**
	 * 신청취소를 위한 정보조회
	 */
	Optional<Applicant> findByMemberIdAndLectureId(Integer memberId, Integer lectureId);

}
