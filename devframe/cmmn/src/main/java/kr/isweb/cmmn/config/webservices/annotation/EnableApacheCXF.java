package kr.isweb.cmmn.config.webservices.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.context.annotation.Import;

import kr.isweb.cmmn.config.webservices.CmmnCxfWebServiceConfigurer;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Import(CmmnCxfWebServiceConfigurer.class)
public @interface EnableApacheCXF {

}
