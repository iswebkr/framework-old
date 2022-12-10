package kr.isweb.cmmn.config.datasource.router.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import kr.isweb.cmmn.config.datasource.router.RoutingKey;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RoutingMethod {
	RoutingKey value();
}
