package kr.isweb.cmmn.config.tiles.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.context.annotation.Import;

import kr.isweb.cmmn.config.tiles.CmmnTilesWebMvcConfigurer;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Import(CmmnTilesWebMvcConfigurer.class)
public @interface EnableTiles {

}
