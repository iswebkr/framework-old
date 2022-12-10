package kr.isweb.cmmn.config.spring.servlet;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import javax.servlet.Filter;
import javax.servlet.MultipartConfigElement;
import javax.servlet.ServletContext;
import javax.servlet.ServletRegistration.Dynamic;

import org.apache.cxf.transport.servlet.CXFServlet;
import org.h2.server.web.WebServlet;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.core.annotation.Order;
import org.springframework.lang.Nullable;
import org.springframework.security.web.session.HttpSessionEventPublisher;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.request.RequestContextListener;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.filter.FormContentFilter;
import org.springframework.web.filter.HiddenHttpMethodFilter;
import org.springframework.web.filter.ShallowEtagHeaderFilter;
import org.springframework.web.multipart.support.MultipartFilter;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.FrameworkServlet;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

import kr.isweb.cmmn.config.spring.initializer.CmmnApplicationContextInitializer;
import kr.isweb.cmmn.config.xss.CmmnXSSFilter;

@Order(1)
public abstract class CmmnAbstractDispatcherServletInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {

	@Override
	protected String getServletName() {
		return "devframe";
	}

	@Override
	protected boolean isAsyncSupported() {
		return Boolean.TRUE;
	}

	@Override
	protected void registerDispatcherServlet(ServletContext servletContext) {
		// cxf webservice dispatcher
		Dynamic cxfDispatcher = servletContext.addServlet("cxfDispatcher",  new CXFServlet());
		cxfDispatcher.setLoadOnStartup(1);
		cxfDispatcher.addMapping("/cxf-webservices/*");

		// h2 database
		Dynamic h2Servhet = servletContext.addServlet("h2Servlet",  new WebServlet());
		h2Servhet.setLoadOnStartup(2);
		h2Servhet.addMapping("/h2-console/*");

		super.registerDispatcherServlet(servletContext);
	}

	@Override
	protected FrameworkServlet createDispatcherServlet(WebApplicationContext servletAppContext) {
		DispatcherServlet dispatcherServlet = (DispatcherServlet) super.createDispatcherServlet(servletAppContext);
		dispatcherServlet.setThrowExceptionIfNoHandlerFound(Boolean.TRUE);
		return dispatcherServlet;
	}

	@Override
	protected ApplicationContextInitializer<?>[] getRootApplicationContextInitializers() {
		return new ApplicationContextInitializer<?> [] {
			new CmmnApplicationContextInitializer()
		};
	}

	@Override
	protected void registerContextLoaderListener(ServletContext servletContext) {
		servletContext.addListener(RequestContextListener.class);
		// servletContext.addListener(FileCleanerCleanup.class);
		servletContext.addListener(HttpSessionEventPublisher.class);
		super.registerContextLoaderListener(servletContext);
	}

	@Override
	protected void customizeRegistration(Dynamic registration) {
		registration.setAsyncSupported(Boolean.TRUE);
		registration.setInitParameter("javax.servlet.jsp.jstl.fmt.locale", "ko_KR");
		registration.setInitParameter("defaultHtmlEscape", Boolean.TRUE.toString());
		registration.setInitParameter("enableLoggingRequestDetails", Boolean.TRUE.toString());

		registration.setLoadOnStartup(1);

		// MultipartConfig for Servlet 3.0
		registration.setMultipartConfig(getMultipartConfigElement());
	}

	@Override
	protected Filter[] getServletFilters() {
		return new Filter[] {
				characterEncodingFilter(),
				corsFilter(),
				new HiddenHttpMethodFilter(),
				new FormContentFilter(),
				new ShallowEtagHeaderFilter(),
				new MultipartFilter(),
				new CmmnXSSFilter(),
		};
	}

	@Override
	protected String[] getServletMappings() {
		return new String[] {
				"/"
		};
	}

	private CharacterEncodingFilter characterEncodingFilter() {
		CharacterEncodingFilter characterEncodingFilter = new CharacterEncodingFilter();
		characterEncodingFilter.setBeanName("characterEncodingFilter");
		characterEncodingFilter.setEncoding(StandardCharsets.UTF_8.name());
		characterEncodingFilter.setForceEncoding(Boolean.TRUE);
		return characterEncodingFilter;
	}

	private CorsFilter corsFilter() {
		CorsConfiguration configSource = new CorsConfiguration();
		configSource.setAllowCredentials(Boolean.TRUE);
		configSource.setAllowedHeaders(Arrays.asList(CorsConfiguration.ALL));
		configSource.setAllowedMethods(Arrays.asList(CorsConfiguration.ALL));
		configSource.setAllowedOriginPatterns(Arrays.asList(CorsConfiguration.ALL));
		configSource.setAllowCredentials(Boolean.TRUE);
		configSource.setMaxAge(5000L);
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configSource);
		CorsFilter corsFilter = new CorsFilter(source);
		corsFilter.setBeanName("corsFilter");
		return corsFilter;
	}

	private MultipartConfigElement getMultipartConfigElement() {
		String location = System.getProperty("java.io.tmpdir");
		int maxFileSize = 5 * 1024 * 1024;
		int maxRequestSize = maxFileSize * 2;
		int fileSizeThreshold = maxFileSize / 2;
		return new MultipartConfigElement(location, maxFileSize, maxRequestSize, fileSizeThreshold);
	}

	@Nullable
	protected abstract Class<?>[] getRootConfigClasses();

	@Nullable
	protected abstract Class<?>[] getServletConfigClasses();
}


