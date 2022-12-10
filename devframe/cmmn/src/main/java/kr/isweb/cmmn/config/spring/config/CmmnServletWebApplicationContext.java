package kr.isweb.cmmn.config.spring.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ViewResolverRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.view.BeanNameViewResolver;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;
import org.springframework.web.servlet.view.xml.MappingJackson2XmlView;

@EnableWebMvc
@ComponentScan(basePackages = {"kr.isweb.cmmn"}, useDefaultFilters = false, excludeFilters = {
		@ComponentScan.Filter(type = FilterType.ANNOTATION, value = Configuration.class),
}, includeFilters = {
		@ComponentScan.Filter(type = FilterType.ANNOTATION, value = Controller.class),
})
public class CmmnServletWebApplicationContext implements WebMvcConfigurer {
	@Override
	public void configureViewResolvers(ViewResolverRegistry registry) {
		BeanNameViewResolver beanNameView = new BeanNameViewResolver();
		beanNameView.setOrder(1);

		InternalResourceViewResolver internalResourceView = new InternalResourceViewResolver();
		internalResourceView.setViewClass(JstlView.class);
		internalResourceView.setPrefix("/WEB-INF/jsp/");
		internalResourceView.setSuffix(".jsp");
		internalResourceView.setOrder(2);

		MappingJackson2JsonView jsonView = new MappingJackson2JsonView();
		jsonView.setExtractValueFromSingleKeyModel(Boolean.TRUE);
		jsonView.setPrettyPrint(Boolean.TRUE);

		MappingJackson2XmlView xmlView = new MappingJackson2XmlView();
		xmlView.setPrettyPrint(Boolean.TRUE);

		registry.viewResolver(beanNameView);
		registry.viewResolver(internalResourceView);
		registry.enableContentNegotiation(jsonView, xmlView);
	}

	@Override
	public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
		configurer.favorParameter(Boolean.TRUE)
		.parameterName("mediaType")
		.ignoreAcceptHeader(Boolean.TRUE)
		.mediaType("xml", MediaType.APPLICATION_XML)
		.mediaType("json", MediaType.APPLICATION_JSON);
	}
}
