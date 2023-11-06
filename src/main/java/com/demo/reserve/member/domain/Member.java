package com.demo.reserve.member.domain;

import com.demo.common.entity.BaseEntityDatetime;
import com.demo.config.security.Role;
import com.demo.reserve.lecture.domain.Applicant;
import com.demo.reserve.lecture.domain.ApplicantHis;
import com.demo.reserve.member.dto.MemberUpdateDto;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Member extends BaseEntityDatetime implements UserDetails {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "member_id", nullable = false)
	private Integer id;
	private String loginId;
	private String loginPw;
	private String memberName;
	private String memberTel;
	private String refreshToken;
	private String regId;
	private String modId;
	private String role;

	@OneToMany(mappedBy = "member")
	private List<Applicant> applicants = new ArrayList<>();

	@OneToMany(mappedBy = "member")
	private List<ApplicantHis> applicantHisList = new ArrayList<>();

	public Member(String loginId) {
		this.loginId = loginId;
	}

	public Member(Integer memberId) {
		this.id = memberId;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return List.of(new SimpleGrantedAuthority(role));
	}

	@Override
	public String getUsername() {
		return loginId;
	}

	@Override
	public String getPassword() {
		return loginPw;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	// 도메인 로직
	public void updateRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}

	public void updateMember(MemberUpdateDto updateDto) {
		this.loginPw = updateDto.getLoginPw();
		this.memberName = updateDto.getMemberName();
		this.memberTel = updateDto.getMemberTel();
	}
}
