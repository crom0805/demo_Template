package com.demo.reserve.lecture.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
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
 * Front API Test
 */
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("local")
class FrontControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;


	@Test
	@DisplayName("강연목록")
	void findLectures() throws Exception {
		mockMvc.perform(get("/front/lectures"))
			.andExpect(status().isOk())
			.andDo(print());
	}

	@Test
	@DisplayName("강연신청")
	void applyLecture() throws Exception {

		HashMap<String, Object> param = new HashMap<>();
		param.put("lectureId", "1");
		param.put("empNo", "T1234");

		mockMvc.perform(post("/front/applicant")
				.content(objectMapper.writeValueAsString(param))
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isCreated())
			.andDo(print());
	}

	@Test
	@DisplayName("신청내역조회")
	void findApplicantHis() throws Exception {
		mockMvc.perform(get("/front/applicant/A2345"))
			.andExpect(status().isOk())
			.andDo(print());
	}

	@Test
	@DisplayName("신청취소")
	void cancelLecture() throws Exception {
		// 신청취소할 데이터 생성
		HashMap<String, Object> param = new HashMap<>();
		param.put("lectureId", "1");
		param.put("empNo", "T1234");
		mockMvc.perform(post("/front/applicant")
				.content(objectMapper.writeValueAsString(param))
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isCreated())
			.andDo(print());

		// 신청취소
		mockMvc.perform(delete("/front/lecture/applicant")
				.content(objectMapper.writeValueAsString(param))
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andDo(print());
	}

	@Test
	@DisplayName("인기강연")
	void findPopularityLectures() throws Exception {
		mockMvc.perform(get("/front/lecture/popularity"))
			.andExpect(status().isOk())
			.andDo(print());
	}
}
