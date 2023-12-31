package com.demo.config.security;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Role {
	ADMIN("ADMIN"),
	USER("USER");

	private final String value;
}
