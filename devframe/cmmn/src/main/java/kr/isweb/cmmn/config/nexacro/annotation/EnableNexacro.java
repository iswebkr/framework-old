package kr.isweb.cmmn.config.nexacro.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.context.annotation.Import;

import kr.isweb.cmmn.config.nexacro.CmmnNexacroConfigurer;

/**
 * 넥사크로 사용을 위한 Annotation
 * @Configuration Annotation이 먼저 선언되어져야 함
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Import(CmmnNexacroConfigurer.class)
public @interface EnableNexacro {
}
