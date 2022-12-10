package kr.isweb.cmmn.config.spring.config;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import org.springframework.aop.interceptor.AsyncExecutionAspectSupport;
import org.springframework.cache.CacheManager;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.cache.ehcache.EhCacheManagerFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.AsyncSupportConfigurer;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.handler.SimpleUrlHandlerMapping;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;
import org.springframework.web.servlet.resource.ResourceHttpRequestHandler;

import kr.isweb.cmmn.config.interceptor.CmmnHandlerInterceptor;
import kr.isweb.cmmn.config.message.CmmnRelodableResourceBundleMessageSource;

@EnableWebMvc
@EnableAsync
@PropertySource("classpath:application-${spring.profiles.active}.properties")
@ComponentScan(basePackages = { "kr.isweb.cmmn" }, excludeFilters = {
		@ComponentScan.Filter(type = FilterType.ANNOTATION, value = Configuration.class),
		@ComponentScan.Filter(type = FilterType.ANNOTATION, value = Controller.class), })
public class CmmnRootWebApplicationContext implements WebMvcConfigurer {
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/resources/**").addResourceLocations("/resources/", "classpath:/resources/",
				"classpath:/META-INF/resources/", "classpath:/META-INF/resources/webjars/");
		registry.addResourceHandler("/static/**").addResourceLocations("/static/", "classpath:/static/");
		registry.addResourceHandler("/templates/**").addResourceLocations("/templates/", "classpath:/templates/");
	}

	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**");
	}

	@Override
	public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
		converters.add(new StringHttpMessageConverter(Charset.forName(StandardCharsets.UTF_8.name())));
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		LocaleChangeInterceptor localeChangeInterceptor = new LocaleChangeInterceptor();
		localeChangeInterceptor.setParamName("lang");
		registry.addInterceptor(localeChangeInterceptor);
		registry.addInterceptor(new CmmnHandlerInterceptor());
	}

	@Override
	public void configureAsyncSupport(AsyncSupportConfigurer configurer) {
		configurer.setTaskExecutor(taskExecutor());
	}

	@Bean(name = AsyncExecutionAspectSupport.DEFAULT_TASK_EXECUTOR_BEAN_NAME)
	public ThreadPoolTaskExecutor taskExecutor() {
		ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
		threadPoolTaskExecutor.setThreadNamePrefix("THREAD-");
		threadPoolTaskExecutor.setCorePoolSize(10);
		threadPoolTaskExecutor.setMaxPoolSize(10);
		threadPoolTaskExecutor.setQueueCapacity(0);
		threadPoolTaskExecutor.setKeepAliveSeconds(60);
		threadPoolTaskExecutor.setWaitForTasksToCompleteOnShutdown(Boolean.TRUE);
		threadPoolTaskExecutor.setAwaitTerminationSeconds(15);
		return threadPoolTaskExecutor;
	}

	@Bean
	public ReloadableResourceBundleMessageSource messageSource() {
		ReloadableResourceBundleMessageSource rrbms = new CmmnRelodableResourceBundleMessageSource();
		rrbms.setBasenames(new String[] { "classpath:locale/message" });
		rrbms.setUseCodeAsDefaultMessage(Boolean.TRUE);
		rrbms.setDefaultEncoding(StandardCharsets.UTF_8.name());
		rrbms.setDefaultLocale(Locale.KOREA);
		rrbms.setCacheSeconds(60);
		rrbms.setCacheMillis(60);
		return rrbms;
	}

	@Bean
	public LocaleResolver localeResolver() {
		SessionLocaleResolver resolver = new SessionLocaleResolver();
		resolver.setDefaultLocale(Locale.KOREAN);
		resolver.setDefaultTimeZone(TimeZone.getTimeZone("UTC"));
		resolver.setLocaleAttributeName("lang");
		return resolver;
	}

	@Bean
	public LocalValidatorFactoryBean getValidator() {
		LocalValidatorFactoryBean bean = new LocalValidatorFactoryBean();
		bean.setValidationMessageSource(messageSource());
		return bean;
	}

	@Bean
	public CacheManager cacheManager() {
		return new EhCacheCacheManager(ehCacheCacheManager().getObject());
	}

	@Bean
	public EhCacheManagerFactoryBean ehCacheCacheManager() {
		EhCacheManagerFactoryBean ecmfb = new EhCacheManagerFactoryBean();
		ecmfb.setConfigLocation(new ClassPathResource("ehcache.xml"));
		ecmfb.setShared(Boolean.TRUE);
		return ecmfb;
	}

	/**
	 * @Bean public StandardServletMultipartResolver multipartResolver() {
	 *       StandardServletMultipartResolver multipartResolver = new
	 *       StandardServletMultipartResolver();
	 *       multipartResolver.setResolveLazily(Boolean.TRUE); return
	 *       multipartResolver; }
	 **/

	@Bean
	public CommonsMultipartResolver multipartResolver() {
		CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver();
		multipartResolver.setDefaultEncoding(StandardCharsets.UTF_8.name());
		multipartResolver.setMaxUploadSize(100000);
		multipartResolver.setResolveLazily(Boolean.TRUE);
		return multipartResolver;
	}

	@Bean
	public SimpleUrlHandlerMapping customFaviconHandlerMapping() {
		SimpleUrlHandlerMapping mapping = new SimpleUrlHandlerMapping();
		mapping.setOrder(Integer.MIN_VALUE);
		mapping.setUrlMap(Collections.singletonMap("favicon.ico", faviconRequestHandler()));
		return mapping;
	}

	@Bean
	protected ResourceHttpRequestHandler faviconRequestHandler() {
		ResourceHttpRequestHandler requestHandler = new ResourceHttpRequestHandler();
		ClassPathResource resources = new ClassPathResource("/");
		List<Resource> locations = Arrays.asList(resources);
		requestHandler.setLocations(locations);
		return requestHandler;
	}
}
