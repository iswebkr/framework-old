package kr.isweb.cmmn.config.validate.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import kr.isweb.cmmn.config.validate.constraint.CmmnValidCorpNumberConstraint;

/**
 * 법인번호 Validation 용 Annotation
 * <pre>
 * @ValidCorpNumber
 * private String corpNo;
 * </pre>
 */
@Documented
@Constraint(validatedBy = CmmnValidCorpNumberConstraint.class)
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD, ElementType.FIELD })
public @interface ValidCorpNumber {
	String message() default "{validate.corp.number}";
	Class<?> [] groups() default {};
	Class<? extends Payload>[] payload() default {};
}
