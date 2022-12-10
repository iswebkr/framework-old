package kr.isweb.cmmn.config.security;

import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.session.ConcurrentSessionFilter;

import kr.isweb.cmmn.config.security.adapter.CmmnWebSecurityConfigurerAdpater;

public class CmmnWebSecurityConfigurer extends CmmnWebSecurityConfigurerAdpater {
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.authenticationProvider(cmmnMemberAuthenticationProvider());
	}

	@Override
	public void configure(WebSecurity web) {
		web.ignoring().antMatchers(
				"/resources/**",
				"/static/**",
				"/template/**",
				"/h2-console/**",
				"/cxf-webservices/**");
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.cors().and().csrf().disable();
		http.addFilterBefore(concurrentSessionFilter(), ConcurrentSessionFilter.class);
		http.sessionManagement()
			.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
			.maximumSessions(1)
			.expiredSessionStrategy(sessionInformationExpiredStrategy())
			.maxSessionsPreventsLogin(Boolean.TRUE);
		http.authorizeRequests()
			.antMatchers("/**").permitAll();
	}
}
