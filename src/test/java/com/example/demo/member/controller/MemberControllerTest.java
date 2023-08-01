package com.example.demo.member.controller;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.demo.common.api.ApiResult;
import com.example.demo.config.jwt.dto.TokenInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.context.TestConstructor.AutowireMode;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Transactional
@SpringBootTest
@TestConstructor(autowireMode = AutowireMode.ANNOTATED)
public class MemberControllerTest {

	private String DOMAIN = "http://localhost:8080";

	private static String token;

	@Autowired
    private MockMvc mockMvc;

	private final ObjectMapper objectMapper = new ObjectMapper();;

	@BeforeEach
	public void setUp() {
	}

	@Order(1)
	@Test
	public void 회원가입() throws Exception {

		//given
		Map<String, String> dto = new HashMap<>();
		dto.put("memberId", "junit@test.com");
		dto.put("memberPwd", "1q2w3e4r5t");
		dto.put("memberName", "유닛테스트");
		dto.put("memberTel", "01099994444");

		RequestBuilder requestBuilder = MockMvcRequestBuilders.post(DOMAIN + "/members/signup")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
			.content(objectMapper.writeValueAsString(dto));

		//when
		ResultActions actions  = mockMvc.perform(requestBuilder);

		//then
		actions.andExpect(status().isCreated())
			.andExpect(jsonPath("status").value("success"))
			.andExpect(jsonPath("data.memberId").value("junit@test.com"))
			.andExpect(jsonPath("data.memberName").value("유닛테스트"))
			.andExpect(jsonPath("data.memberTel").value("01099994444"))
		;
	}

	@Order(2)
	@Test
	public void 로그인() throws Exception {
		//given
		Map<String, String> dto = new HashMap<>();
		dto.put("memberId", "test@test.com");
		dto.put("memberPwd", "1q2w3e4r");

		RequestBuilder requestBuilder = MockMvcRequestBuilders.post(DOMAIN + "/members/login")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
			.content(objectMapper.writeValueAsString(dto));

		//when
		ResultActions actions  = mockMvc.perform(requestBuilder);

		//then
		actions.andExpect(status().isOk())
			.andExpect(jsonPath("status").value("success"))
			.andExpect(jsonPath("data.grantType").value("Bearer"))
			.andExpect(jsonPath("data.accessToken").isString())
			.andExpect(jsonPath("data.refreshToken").isString())
			;

		String result = actions.andReturn().getResponse().getContentAsString();
		ApiResult<?> apiResult = (objectMapper.readValue(result, ApiResult.class));
		token = ((LinkedHashMap) apiResult.getData()).get("accessToken").toString();
	}

	@Order(3)
	@Test
	public void 회원목록조회() throws Exception {
		//given
		RequestBuilder requestBuilder = MockMvcRequestBuilders.get(DOMAIN + "/members")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
			.header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
			;

		//when
		ResultActions actions  = mockMvc.perform(requestBuilder);

		//then
		actions.andExpect(status().isOk())
			.andExpect(jsonPath("status").value("success"))
			.andExpect(jsonPath("data").isArray())
			;
	}

	@Order(4)
	@Test
	public void 회원조회() throws Exception {
		//given
		RequestBuilder requestBuilder = MockMvcRequestBuilders.get(DOMAIN + "/members/test@test.com")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
			.header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
			;

		//when
		ResultActions actions  = mockMvc.perform(requestBuilder);

		//then
		actions.andExpect(status().isOk())
			.andExpect(jsonPath("status").value("success"))
			.andExpect(jsonPath("data.memberId").value("test@test.com"))
			.andExpect(jsonPath("data.memberName").value("팜하니"))
			;
	}

	@Order(5)
	@Test
	public void 회원수정() throws Exception {

		//given
		Map<String, String> dto = new HashMap<>();
		dto.put("memberId", "test@test.com");
		dto.put("memberPwd", "1q2w3e4r");
		dto.put("memberName", "팜하니2");
		dto.put("memberTel", "01099994444");

		RequestBuilder requestBuilder = MockMvcRequestBuilders.put(DOMAIN + "/members/test@test.com")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
			.header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
			.content(objectMapper.writeValueAsString(dto));

		//when
		ResultActions actions  = mockMvc.perform(requestBuilder);

		//then
		actions.andExpect(status().isOk())
			.andExpect(jsonPath("status").value("success"))
		;
	}
}
