package com.demo.config.jpa;

import java.util.Optional;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;

@Configuration
public class AuditConfig implements AuditorAware<String> {

	@Override
	public Optional<String> getCurrentAuditor() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		if (authentication == null || !authentication.isAuthenticated()
			|| authentication.getPrincipal().equals("anonymousUser")) {
			return Optional.empty();
		}

		return Optional.of(((User) authentication.getPrincipal()).getUsername());
	}
}
