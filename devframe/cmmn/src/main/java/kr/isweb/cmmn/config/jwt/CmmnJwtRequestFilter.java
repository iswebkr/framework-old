package kr.isweb.cmmn.config.jwt;

import java.io.IOException;

import javax.annotation.CheckReturnValue;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.HttpHeaders;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import kr.isweb.cmmn.config.jwt.CmmnJwtTokenProvider.GRANT_TYPE;

public abstract class CmmnJwtRequestFilter extends OncePerRequestFilter {

	protected final Log logger = LogFactory.getLog(getClass());

	@Autowired
	CmmnJwtTokenProvider jwtTokenProvider;

	@Value("${jwt.refresh.token.url}")
	String jwtRefreshTokenUrl;

	@Value("${jwt.grant.type}")
	String jwtGrantType;

	String userName = null;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
		String requestTokenHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

		// 기본 grantType 을 AccessToken 으로 설정
		GRANT_TYPE grantType = GRANT_TYPE.ACCESS_TOKEN;

		// requestTokenHeader 가 존재하고 인증타입이 Bearer(jwt or OAuth) 인 경우
		if(StringUtils.isNotBlank(requestTokenHeader) && requestTokenHeader.startsWith(jwtGrantType)) {
			String jwtToken = requestTokenHeader.substring(7);

			// Request URI 가 refresh 요청인 경우 TOKEN_TYPE 을 REFRESH_TOKEN 으로 변경
			if(request.getRequestURI().equalsIgnoreCase(jwtRefreshTokenUrl)) {
				grantType = GRANT_TYPE.REFRESH_TOKEN;
			}

			userName = jwtTokenProvider.getUsernameFromJwtToken(jwtToken, grantType);

			// userName 이 존재하고 로그인 하지 않은 경우
			if(StringUtils.isNotBlank(userName) && SecurityContextHolder.getContext().getAuthentication() == null) {
				UserDetails userDetails = getUserDetails();
				// 토근의 유효성을 확인
				if(jwtTokenProvider.validateJwtToken(jwtToken, userDetails, grantType)) {
					UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(userDetails,  null, userDetails.getAuthorities());
					token.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
					SecurityContextHolder.getContext().setAuthentication(token);
				}
			}
		}
		filterChain.doFilter(request, response);
	}

	@CheckReturnValue
	public abstract UserDetails getUserDetails();

	public String getUserName() {
		return userName;
	}
}
