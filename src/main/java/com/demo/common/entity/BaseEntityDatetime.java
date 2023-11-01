package com.demo.common.entity;

import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import java.time.LocalDateTime;
import lombok.Getter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@EntityListeners(value = AuditingEntityListener.class)
@MappedSuperclass
@Getter
public class BaseEntityDatetime {

	@CreationTimestamp
	private LocalDateTime regDt;

	@UpdateTimestamp
	private LocalDateTime modDt;
}
