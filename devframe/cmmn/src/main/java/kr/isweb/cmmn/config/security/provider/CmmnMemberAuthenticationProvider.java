package kr.isweb.cmmn.config.security.provider;

import java.util.ArrayList;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

public class CmmnMemberAuthenticationProvider implements AuthenticationProvider {
	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		String name = authentication.getPrincipal().toString();
		String password = authentication.getCredentials().toString();

		/*
		 * 회원정보조회 로직
		 * UserDetailsVO userDetailsVO = (UserDetailsVO) userDetailsService.loadUserByUsername(userEmail);
		 *
		 * 비밀번호 일치여부 확인
		 * if (!passwordEncoder.matches(userPw, userDetailsVO.getPassword())) {
		 * 		throw new BadCredentialsException(userDetailsVO.getUsername() * "Invalid password");
		 * }
		 */

		UsernamePasswordAuthenticationToken result = new UsernamePasswordAuthenticationToken(name, password, new ArrayList<>());
		// result.setDetails(result);
		return result;
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
	}
}
