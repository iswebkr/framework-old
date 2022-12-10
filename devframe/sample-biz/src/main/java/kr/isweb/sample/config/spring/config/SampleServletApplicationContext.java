package kr.isweb.sample.config.spring.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.stereotype.Controller;

import kr.isweb.cmmn.config.spring.config.CmmnServletWebApplicationContext;
import kr.isweb.cmmn.config.tiles.annotation.EnableTiles;

@Configuration
@EnableTiles
@ComponentScan(basePackages = {"kr.isweb.sample"}, useDefaultFilters = false, includeFilters = {
		@ComponentScan.Filter(type = FilterType.ANNOTATION, value = Controller.class),
})
public class SampleServletApplicationContext extends CmmnServletWebApplicationContext {

}
