package com.demo.reserve.lecture.repository;

import com.demo.reserve.lecture.domain.ApplicantHis;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 신청이력관련 Repository
 */
public interface ApplicantHisRepository extends JpaRepository<ApplicantHis, Integer> {

	List<ApplicantHis> findByEmpNo(String empNo);
}
