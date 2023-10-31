package com.demo.reserve.lecture.repository;

import com.demo.reserve.lecture.domain.Lecture;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 강연정보관련 Repository
 */
public interface LectureRepository extends JpaRepository<Lecture, Integer>, LectureRepositoryCustom {

	/**
	 * 강연정보조회
	 */
	Lecture findLectureById(Integer id);
}
