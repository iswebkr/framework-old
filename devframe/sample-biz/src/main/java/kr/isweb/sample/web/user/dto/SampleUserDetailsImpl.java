package kr.isweb.sample.web.user.dto;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;

import kr.isweb.cmmn.config.security.user.CmmnUserDetailsImpl;

public class SampleUserDetailsImpl extends CmmnUserDetailsImpl {

	private static final long serialVersionUID = 1L;

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return null;
	}

	@Override
	public String getPassword() {
		return null;
	}

	@Override
	public String getUsername() {
		return null;
	}

	@Override
	public boolean isAccountNonExpired() {
		return false;
	}

	@Override
	public boolean isAccountNonLocked() {
		return false;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return false;
	}

	@Override
	public boolean isEnabled() {
		return false;
	}

}
