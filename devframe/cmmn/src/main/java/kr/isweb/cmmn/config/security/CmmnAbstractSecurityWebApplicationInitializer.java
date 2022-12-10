package kr.isweb.cmmn.config.security;

import java.nio.charset.StandardCharsets;

import javax.servlet.FilterRegistration;
import javax.servlet.ServletContext;

import org.springframework.core.annotation.Order;
import org.springframework.security.web.context.AbstractSecurityWebApplicationInitializer;
import org.springframework.web.filter.CharacterEncodingFilter;

@Order(2)
public class CmmnAbstractSecurityWebApplicationInitializer extends AbstractSecurityWebApplicationInitializer {
	@Override
	protected void beforeSpringSecurityFilterChain(ServletContext servletContext) {
		FilterRegistration.Dynamic encodingFilter = servletContext.addFilter("encodingFilter",  new CharacterEncodingFilter());
		encodingFilter.setInitParameter("encoding", StandardCharsets.UTF_8.name());
		encodingFilter.setInitParameter("forceEncoding", "true");
		encodingFilter.addMappingForUrlPatterns(null, false, "/*");
	}
}
