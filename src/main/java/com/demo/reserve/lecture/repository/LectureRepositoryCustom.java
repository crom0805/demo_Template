package com.demo.reserve.lecture.repository;

import com.demo.reserve.lecture.domain.Lecture;
import com.demo.reserve.lecture.dto.LectureResponseDto;
import java.util.List;

public interface LectureRepositoryCustom {

	/**
	 * 강연목록(검색조건)
	 */
	List<LectureResponseDto> findLecturesByFront();
	/**
	 * 실시간 인기강연 목록
	 */
	List<LectureResponseDto> findPopularityLectures();

	Lecture findByIdForUpdate(Integer id);
}
