package kr.isweb.cmmn.config.tiles;

import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.ViewResolverRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.view.tiles3.TilesConfigurer;
import org.springframework.web.servlet.view.tiles3.TilesViewResolver;

public class CmmnTilesWebMvcConfigurer implements WebMvcConfigurer {
	@Bean
	public TilesConfigurer tilesConfigurer() {
		TilesConfigurer tilesConfigurer = new TilesConfigurer();
		tilesConfigurer.setDefinitions(new String[] {
		        "/WEB-INF/tiles/**/tiles*.xml"
		});
		tilesConfigurer.setCheckRefresh(Boolean.TRUE);
		return tilesConfigurer;
	}

	@Override
	public void configureViewResolvers(ViewResolverRegistry registry) {
		TilesViewResolver viewResolver = new TilesViewResolver();
		viewResolver.setOrder(Integer.MIN_VALUE);
		registry.viewResolver(viewResolver);
	}
}
