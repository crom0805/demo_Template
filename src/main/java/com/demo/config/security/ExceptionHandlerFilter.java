package com.demo.config.security;

import com.demo.common.api.ApiResult;
import com.demo.common.exception.EmptyTokenException;
import com.demo.common.exception.ExceptionEnum;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.filter.OncePerRequestFilter;

public class ExceptionHandlerFilter extends OncePerRequestFilter {

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

		try {
			filterChain.doFilter(request, response);
		} catch (Exception e) {
			if (e instanceof BadCredentialsException) {
				setErrorResponse(response, ExceptionEnum.FORBIDDEN);
			} else if (e instanceof io.jsonwebtoken.security.SecurityException) {
				setErrorResponse(response, ExceptionEnum.INVALID_JWT_TOKEN);
			} else if (e instanceof MalformedJwtException) {
				setErrorResponse(response, ExceptionEnum.INVALID_JWT_TOKEN);
			} else if (e instanceof ExpiredJwtException) {
				setErrorResponse(response, ExceptionEnum.EXPIRED_JWT_EXCEPTION);
			} else if (e instanceof UnsupportedJwtException) {
				setErrorResponse(response, ExceptionEnum.UNSUPPORTED_JWT_EXCEPTION);
			} else if (e instanceof EmptyTokenException) {
				setErrorResponse(response, ExceptionEnum.EMPTY_JWT_EXCEPTION);
			}
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
