package kr.isweb.cmmn.config.egovframe.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.context.annotation.Import;

import kr.isweb.cmmn.config.egovframe.CmmnEgovFrameConfigurer;

/**
 * EgovFramework 활성화를 위한 인터페이스
 * @Configuration Annotation 이 먼저 선언되어져야 함
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Import(CmmnEgovFrameConfigurer.class)
public @interface EnableEgovFramework {

}
