package com.demo.reserve.lecture.domain;

import com.demo.common.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Lecture extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "lecture_id", nullable = false)
	private Integer id;

	@NotNull
	@Column(nullable = false, length = 20)
	private String lecturer;

	@NotNull
	@Column(nullable = false, length = 50)
	private String lectureRoom;

	@NotNull
	@Column(nullable = false)
	private String lectureContent;

	@NotNull
	@Column(nullable = false)
	private Integer lecturePeople;

	@NotNull
	@Column(nullable = false)
	private LocalDateTime startDt;

	@NotNull
	@Column(nullable = false)
	private LocalDateTime endDt;

	@OneToMany(mappedBy = "lecture")
	private List<Applicant> applicants = new ArrayList<>();

	public Lecture(Integer id) {
		this.id = id;
	}
}
