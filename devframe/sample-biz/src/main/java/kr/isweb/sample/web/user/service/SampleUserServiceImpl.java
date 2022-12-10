package kr.isweb.sample.web.user.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import kr.isweb.cmmn.config.security.CmmnRoles;
import kr.isweb.cmmn.config.security.user.CmmnUserDetailsServiceImpl;

@Service
public class SampleUserServiceImpl extends CmmnUserDetailsServiceImpl {
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		// 사용자 정보를 조회 (from DBMS)
		List<GrantedAuthority> list = new ArrayList<>();
		list.add(new SimpleGrantedAuthority(CmmnRoles.USER.getValue()));
		list.add(new SimpleGrantedAuthority(CmmnRoles.ADMIN.getValue()));
		return new User("iswebkr@gmail.com", "test", list);
	}
}
