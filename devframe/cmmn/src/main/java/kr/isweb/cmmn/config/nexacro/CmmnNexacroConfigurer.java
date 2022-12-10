package kr.isweb.cmmn.config.nexacro;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;

import com.nexacro.uiadapter17.spring.core.resolve.NexacroHandlerMethodReturnValueHandler;
import com.nexacro.uiadapter17.spring.core.resolve.NexacroMappingExceptionResolver;
import com.nexacro.uiadapter17.spring.core.resolve.NexacroMethodArgumentResolver;
import com.nexacro.uiadapter17.spring.core.resolve.NexacroRequestMappingHandlerAdapter;
import com.nexacro.uiadapter17.spring.core.view.NexacroFileView;
import com.nexacro.uiadapter17.spring.core.view.NexacroView;

public class CmmnNexacroConfigurer {

	@Autowired
	MessageSource messageSource;

	@Bean
	public NexacroFileView nexacroFileView() {
		NexacroFileView nexacroFileView = new NexacroFileView();
		nexacroFileView.setDefaultCharset(StandardCharsets.UTF_8.name());
		return nexacroFileView;
	}

	@Bean
	public NexacroView nexacroView() {
		NexacroView nexacroView = new NexacroView();
		nexacroView.setDefaultContentType("PlatformXml");
		nexacroView.setDefaultCharset(StandardCharsets.UTF_8.name());
		return nexacroView;
	}

	@Bean
	public NexacroRequestMappingHandlerAdapter nexacroRequestMappingHandlerAdapter() {
		NexacroRequestMappingHandlerAdapter nexacroRequestMappingHandlerAdapter = new NexacroRequestMappingHandlerAdapter();

		List<HandlerMethodArgumentResolver> list = new ArrayList<>();
		List<HandlerMethodReturnValueHandler> rtnList = new ArrayList<>();

		list.add(nexacroMethodArgumentResolver());
		rtnList.add(nexacroHandlerMethodReturnValueHandler());

		nexacroRequestMappingHandlerAdapter.setCustomArgumentResolvers(list);
		nexacroRequestMappingHandlerAdapter.setCustomReturnValueHandlers(rtnList);

		return nexacroRequestMappingHandlerAdapter;
	}

	@Bean
	protected NexacroMethodArgumentResolver nexacroMethodArgumentResolver() {
		NexacroMethodArgumentResolver nexacroMethodArgumentResolver = new NexacroMethodArgumentResolver();
		return nexacroMethodArgumentResolver;
	}

	@Bean
	protected NexacroHandlerMethodReturnValueHandler nexacroHandlerMethodReturnValueHandler() {
		NexacroHandlerMethodReturnValueHandler nexacroHandlerMethodReturnValueHandler = new NexacroHandlerMethodReturnValueHandler();
		nexacroHandlerMethodReturnValueHandler.setView(nexacroView());
		nexacroHandlerMethodReturnValueHandler.setFileView(nexacroFileView());
		return nexacroHandlerMethodReturnValueHandler;
	}

	@Bean
	public NexacroMappingExceptionResolver nexacroExceptionResolver() {
		NexacroMappingExceptionResolver exceptionResolver = new NexacroMappingExceptionResolver();
		exceptionResolver.setView(nexacroView());
		exceptionResolver.setShouldLogStackTrace(Boolean.TRUE);
		exceptionResolver.setShouldSendStackTrace(Boolean.TRUE);
		exceptionResolver.setDefaultErrorMsg("fail.common.msg");
		exceptionResolver.setMessageSource(messageSource);
		return exceptionResolver;
	}
}
