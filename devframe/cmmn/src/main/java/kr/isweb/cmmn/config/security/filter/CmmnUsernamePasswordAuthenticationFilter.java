package kr.isweb.cmmn.config.security.filter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

public class CmmnUsernamePasswordAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
		return super.attemptAuthentication(request, response);
	}
}
