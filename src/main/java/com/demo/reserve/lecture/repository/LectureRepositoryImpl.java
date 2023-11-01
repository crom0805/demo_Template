package com.demo.reserve.lecture.repository;

import static com.demo.reserve.lecture.domain.QLecture.lecture;

import com.demo.reserve.lecture.domain.Lecture;
import com.demo.reserve.lecture.domain.QApplicant;
import com.demo.reserve.lecture.dto.LectureResponseDto;
import com.demo.reserve.lecture.dto.QLectureResponseDto;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import java.time.LocalDateTime;
import java.util.List;

public class LectureRepositoryImpl implements LectureRepositoryCustom {

	private final JPAQueryFactory queryFactory;

	public LectureRepositoryImpl(EntityManager entityManager) {
		this.queryFactory = new JPAQueryFactory(entityManager);
	}

	/**
	 * 강연목록(검색조건)
	 */
	@Override
	public List<LectureResponseDto> findLecturesByFront() {
		LocalDateTime today = LocalDateTime.now();
		return queryFactory
			.select(new QLectureResponseDto(
				lecture.id
				, lecture.lecturer
				, lecture.lectureRoom
				, lecture.lectureContent
				, lecture.lecturePeople
				, lecture.applicants.size()
				, lecture.startDt
				, lecture.endDt
			))
			.from(lecture)
			.where(lecture.startDt.between(today.minusDays(1), today.plusDays(7)))
			.orderBy(lecture.startDt.asc())
			.fetch();
	}

	/**
	 * 실시간 인기강연 메뉴 조회
	 */
	@Override
	public List<LectureResponseDto> findPopularityLectures() {
		LocalDateTime today = LocalDateTime.now();
		return queryFactory
			.select(new QLectureResponseDto(
				lecture.id
				, lecture.lecturer
				, lecture.lectureRoom
				, lecture.lectureContent
				, lecture.lecturePeople
				, QApplicant.applicant.member.count().intValue()
				, lecture.startDt
				, lecture.endDt
			))
			.from(lecture)
			.join(lecture.applicants, QApplicant.applicant)
			.where(QApplicant.applicant.applyDt.between(today.minusDays(3), today))
			.groupBy(lecture.id, lecture.lecturer, lecture.lectureRoom, lecture.lecturePeople, lecture.startDt, lecture.endDt)
			.orderBy(QApplicant.applicant.member.count().desc(), lecture.startDt.asc())
			.fetch();
	}

	/**
	 * 강연조회(ForUpdate)
	 */
	public Lecture findByIdForUpdate(Integer id) {
		return queryFactory
			.selectFrom(lecture)
			.where(lecture.id.eq(id))
			.setLockMode(LockModeType.PESSIMISTIC_WRITE)
			.fetchOne();
	}

}
