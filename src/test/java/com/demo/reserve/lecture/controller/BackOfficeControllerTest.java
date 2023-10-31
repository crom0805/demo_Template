package com.demo.reserve.lecture.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.HashMap;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * BackOffice API Test
 */
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("local")
class BackOfficeControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Test
	@DisplayName("강연목록")
	void findLectures() throws Exception {
		mockMvc.perform(get("/back/lectures"))
			.andExpect(status().isOk())
			.andDo(print());
	}

	@Test
	@DisplayName("강연등록")
	void saveLecture() throws Exception {

		HashMap<String, Object> param = new HashMap<>();
		param.put("lecturer", "강연자");
		param.put("lectureRoom", "글로벌룸");
		param.put("lectureContent", "테스트 강연");
		param.put("lecturePeople", 7);
		param.put("startDt", "20231020100000");
		param.put("endDt", "20231025180000");

		mockMvc.perform(post("/back/lecture")
				.content(objectMapper.writeValueAsString(param))
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isCreated())
			.andDo(print());
	}

	@Test
	@DisplayName("전체강연 신청자목록")
	void findLectureApplicant() throws Exception {
		mockMvc.perform(get("/back/lecture/applicant"))
			.andExpect(status().isOk())
			.andDo(print());
	}

	@Test
	@DisplayName("강연별 신청자목록")
	void findApplicantByLecture() throws Exception {
		mockMvc.perform(get("/back/applicant/9"))
			.andExpect(status().isOk())
			.andDo(print());
	}
}
