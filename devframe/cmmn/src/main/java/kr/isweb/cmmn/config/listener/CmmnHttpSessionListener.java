package kr.isweb.cmmn.config.listener;

import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
@WebListener
public class CmmnHttpSessionListener implements HttpSessionListener {

	private Logger logger = LoggerFactory.getLogger(getClass());

	@Override
	public void sessionCreated(HttpSessionEvent event) {
		event.getSession().setMaxInactiveInterval(60);
		logger.info("************* Created Session ID : {}", event.getSession().getId());
	}

	@Override
	public void sessionDestroyed(HttpSessionEvent event) {
		logger.info("************* Destroyed Session ID {}", event.getSession().getId());
	}
}
