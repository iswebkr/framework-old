package kr.isweb.cmmn.config.spring.initializer;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.io.support.ResourcePropertySource;

@Order(Ordered.HIGHEST_PRECEDENCE)
public class CmmnApplicationContextInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

	private Logger logger = LoggerFactory.getLogger(getClass());

	/**
	 * Property Source 를 데이터베이스 기반으로 설정하고자 하는 경우 아래 URL 참조
	 * https://www.egovframe.go.kr/wiki/doku.php?id=egovframework:rte3:fdl:property_source
	 *
	 * @param applicationContext the application context
	 */
	@Override
	public void initialize(ConfigurableApplicationContext applicationContext) {
		ConfigurableEnvironment env = applicationContext.getEnvironment();
		try {
			env.getPropertySources().addFirst(new ResourcePropertySource("classpath:application.properties"));
		} catch (IOException e) {
			if(logger.isErrorEnabled()) {
				logger.error(e.getLocalizedMessage());
			}
		}
	}
}
