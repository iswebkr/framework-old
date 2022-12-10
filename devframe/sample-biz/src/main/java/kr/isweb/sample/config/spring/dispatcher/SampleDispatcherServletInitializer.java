package kr.isweb.sample.config.spring.dispatcher;

import kr.isweb.cmmn.config.spring.servlet.CmmnAbstractDispatcherServletInitializer;
import kr.isweb.sample.config.spring.config.SampleServletApplicationContext;
import kr.isweb.sample.config.spring.config.SampleRootApplicationContext;

public class SampleDispatcherServletInitializer extends CmmnAbstractDispatcherServletInitializer {
	@Override
	protected Class<?>[] getRootConfigClasses() {
		return new Class[] {
			SampleRootApplicationContext.class
		};
	}

	@Override
	protected Class<?>[] getServletConfigClasses() {
		return new Class[] {
			SampleServletApplicationContext.class
		};
	}
}
