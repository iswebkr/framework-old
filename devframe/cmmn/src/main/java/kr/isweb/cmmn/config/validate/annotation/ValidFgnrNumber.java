package kr.isweb.cmmn.config.validate.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import kr.isweb.cmmn.config.validate.constraint.CmmnValidFgnrNumberConstraint;

/**
 * 외국인등록번호 Validation 용 Annotation
 * <pre>
 * @ValidFgnrNumber
 * private String fgnrNo;
 * </pre>
 */
@Documented
@Constraint(validatedBy = CmmnValidFgnrNumberConstraint.class)
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD, ElementType.FIELD })
public @interface ValidFgnrNumber {
	String message() default "{validate.fgnr.number}";
	Class<?> [] groups() default {};
	Class<? extends Payload>[] payload() default {};
}
