package kr.isweb.sample.config.security.jwt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import kr.isweb.cmmn.config.jwt.CmmnJwtRequestFilter;
import kr.isweb.sample.web.user.service.SampleUserServiceImpl;

@Component
public class SampleJwtRequestFilter extends CmmnJwtRequestFilter {

	@Autowired
	SampleUserServiceImpl sampleUserServiceImpl;

	@Override
	public UserDetails getUserDetails() {
		UserDetails userDetails = sampleUserServiceImpl.loadUserByUsername(getUserName());
		return userDetails;
	}
}
