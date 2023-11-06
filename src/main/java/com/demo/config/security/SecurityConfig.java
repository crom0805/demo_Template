package com.demo.config.security;

import com.demo.config.jwt.JwtTokenProvider;
import com.demo.config.jwt.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

	private final JwtTokenProvider jwtTokenProvider;

	@Bean
	public WebSecurityCustomizer configure() {
		return (web) -> web.ignoring()
			.requestMatchers("/swagger-ui/**", "/v3/api-docs/**");
	}

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.csrf(AbstractHttpConfigurer::disable)
			.httpBasic(AbstractHttpConfigurer::disable);

		http.sessionManagement(sessionManagement -> sessionManagement
			.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

		http.authorizeHttpRequests(request -> request
			.requestMatchers("/front/members/login").permitAll()
			.requestMatchers("/front/members/signup").permitAll()
			.requestMatchers("/back/admin/login").permitAll()
			.requestMatchers("/back/admin/reg").permitAll()
			.requestMatchers("/swagger-ui/index.html").permitAll()
			.requestMatchers("/v3/api-docs/**").permitAll()
			.requestMatchers("/front/**").hasAuthority(Role.USER.getValue())
			.requestMatchers("/back/**").hasAuthority(Role.ADMIN.getValue())
			.anyRequest().authenticated());

		http.exceptionHandling(exception -> exception
			.defaultAuthenticationEntryPointFor(new CustomAuthenticationEntryPoint(), new AntPathRequestMatcher("/")));

		http.addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class);
		http.addFilterBefore(new ExceptionHandlerFilter(), JwtAuthenticationFilter.class);

		return http.build();
	}

	@Bean
	public BCryptPasswordEncoder bCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
