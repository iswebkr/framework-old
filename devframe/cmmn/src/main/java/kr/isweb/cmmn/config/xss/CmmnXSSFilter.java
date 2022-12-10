package kr.isweb.cmmn.config.xss;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

public class CmmnXSSFilter implements Filter {
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		CmmnXSSRequestWrapper wrapper = new CmmnXSSRequestWrapper((HttpServletRequest) request);
		String body = IOUtils.toString(wrapper.getReader());
		if(StringUtils.isNotBlank(body)) {
			body = CmmnXSSUtils.stripXSS(body);
			wrapper.resetInputStrea(body.getBytes());
		}
		chain.doFilter(wrapper, response);
	}

	@Override
	public void destroy() {
	}
}
