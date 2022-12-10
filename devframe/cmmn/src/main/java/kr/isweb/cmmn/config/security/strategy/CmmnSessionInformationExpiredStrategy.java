package kr.isweb.cmmn.config.security.strategy;

import java.io.IOException;

import javax.servlet.ServletException;

import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.session.SessionInformationExpiredEvent;
import org.springframework.security.web.session.SessionInformationExpiredStrategy;

public class CmmnSessionInformationExpiredStrategy implements SessionInformationExpiredStrategy {
	@Override
	public void onExpiredSessionDetected(SessionInformationExpiredEvent event) throws IOException, ServletException {
		DefaultRedirectStrategy redirectStrategy = new DefaultRedirectStrategy();
		redirectStrategy.sendRedirect(event.getRequest(), event.getResponse(), "/login");
	}
}
