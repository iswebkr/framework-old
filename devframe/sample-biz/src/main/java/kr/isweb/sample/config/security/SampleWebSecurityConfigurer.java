package kr.isweb.sample.config.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

import kr.isweb.cmmn.config.jwt.CmmnJwtAccessDeniedHandler;
import kr.isweb.cmmn.config.jwt.CmmnJwtAuthenticationEntryPoint;
import kr.isweb.cmmn.config.security.CmmnWebSecurityConfigurer;
import kr.isweb.sample.config.security.jwt.SampleJwtRequestFilter;

@EnableWebSecurity
public class SampleWebSecurityConfigurer extends CmmnWebSecurityConfigurer {

	@Autowired
	CmmnJwtAuthenticationEntryPoint cmmnJwtAuthenticationEntryPoint;

	@Autowired
	CmmnJwtAccessDeniedHandler cmmnJwtAccessDeniedHandler;

	@Autowired
	SampleJwtRequestFilter sampleJwtRequestFilter;

	/**
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.cors().and().csrf().disable();
		http.authorizeRequests()
			.antMatchers("/", "/websocket/**", "/user/**").permitAll()
			.anyRequest().authenticated();
		http.sessionManagement()
			.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
		http.exceptionHandling()
			.accessDeniedHandler(cmmnJwtAccessDeniedHandler)
			.authenticationEntryPoint(cmmnJwtAuthenticationEntryPoint);
		http.addFilterBefore(sampleJwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
	}
	**/
}
