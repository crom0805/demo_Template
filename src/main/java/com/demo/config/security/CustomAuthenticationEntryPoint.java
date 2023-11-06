package com.demo.config.security;

import com.demo.common.api.ApiResult;
import com.demo.common.exception.ExceptionEnum;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {

		if (authException instanceof BadCredentialsException) {
			setErrorResponse(response, ExceptionEnum.BAD_CREDENTIALS);
		} else {
			setErrorResponse(response, ExceptionEnum.FORBIDDEN);
		}
	}

	private void setErrorResponse(HttpServletResponse response, ExceptionEnum exceptionEnum) {
		ObjectMapper objectMapper = new ObjectMapper();
		response.setCharacterEncoding("UTF-8");
		response.setStatus(exceptionEnum.getStatus().value());
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		ApiResult<?> error = ApiResult.createError(exceptionEnum.getMessage());
		try {
			response.getWriter().write(objectMapper.writeValueAsString(error));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
