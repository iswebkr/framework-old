package kr.isweb.cmmn.config.security.adapter;

import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.session.ConcurrentSessionFilter;
import org.springframework.security.web.session.HttpSessionEventPublisher;
import org.springframework.security.web.session.SessionInformationExpiredStrategy;

import kr.isweb.cmmn.config.security.handler.CmmnAuthenticationFailureHandler;
import kr.isweb.cmmn.config.security.handler.CmmnLogoutHandler;
import kr.isweb.cmmn.config.security.provider.CmmnMemberAuthenticationProvider;
import kr.isweb.cmmn.config.security.strategy.CmmnSessionInformationExpiredStrategy;

public class CmmnWebSecurityConfigurerAdpater extends WebSecurityConfigurerAdapter {
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public SessionRegistry sessionRegistry() {
	    return new SessionRegistryImpl();
	}

	@Bean
	public HttpSessionEventPublisher httpSessionEventPublisher() {
		return new HttpSessionEventPublisher();
	}

	@Bean
	public AuthenticationFailureHandler cmmnnAuthenticationFailureHandler() {
		return new CmmnAuthenticationFailureHandler();
	}

	@Bean
	public AuthenticationProvider cmmnMemberAuthenticationProvider() {
		return new CmmnMemberAuthenticationProvider();
	}

	@Bean
	public SessionInformationExpiredStrategy sessionInformationExpiredStrategy() {
		return new CmmnSessionInformationExpiredStrategy();
	}

	@Bean
	public ConcurrentSessionFilter concurrentSessionFilter() {
		ConcurrentSessionFilter filter = new ConcurrentSessionFilter(sessionRegistry(), sessionInformationExpiredStrategy());
		filter.setLogoutHandlers(new LogoutHandler[] {
				new CmmnLogoutHandler()
		});
		return filter;
	}
}
