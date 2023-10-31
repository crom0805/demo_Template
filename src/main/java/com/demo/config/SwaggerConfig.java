package com.demo.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

	@Bean
	public GroupedOpenApi front() {
		return GroupedOpenApi.builder()
			.group("Front")
			.pathsToMatch("/front/**")
			.pathsToMatch("/members/**")
			.build();
	}

	@Bean
	public GroupedOpenApi backOffice() {
		return GroupedOpenApi.builder()
			.group("BackOffice")
			.pathsToMatch("/back/**")
			.build();
	}

	@Bean
	public OpenAPI openAPI() {

		Info info = new Info()
			.version("v1.0.0")
			.title("Demo Reservation System")
			.description("[Demo] Spring Boot를 이용한 강연 예약시스템 API입니다.");

		// SecurityScheme
		String jwtSchemeName = "jwtAuth";
		// API 요청헤더에 인증정보 포함
		SecurityRequirement securityRequirement = new SecurityRequirement().addList(jwtSchemeName);
		// SecuritySchemes 등록
		Components components = new Components()
			.addSecuritySchemes(jwtSchemeName, new SecurityScheme()
				.name(jwtSchemeName)
				.type(SecurityScheme.Type.HTTP) // HTTP 방식
				.scheme("bearer")
				.bearerFormat("JWT")); // 토큰 형식을 지정하는 임의의 문자(Optional)

		return new OpenAPI()
			.info(info)
			.addSecurityItem(securityRequirement)
			.components(components);
	}
}
