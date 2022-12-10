package kr.isweb.sample.config.spring.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.stereotype.Controller;

import kr.isweb.cmmn.config.egovframe.annotation.EnableEgovFramework;
import kr.isweb.cmmn.config.spring.config.CmmnRootWebApplicationContext;

@Configuration
@EnableEgovFramework
@ComponentScan(basePackages = {"kr.isweb.sample"}, excludeFilters = {
        @ComponentScan.Filter(type = FilterType.ANNOTATION, value = Controller.class),
})
public class SampleRootApplicationContext extends CmmnRootWebApplicationContext {

}
