package kr.isweb.cmmn.config.validate.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import kr.isweb.cmmn.config.validate.constraint.CmmnValidJuminNumberConstraint;

/**
 * 주민등록번호 Validation 용 Annotation
 * <pre>
 * @ValidJuminNumber
 * private String juminNo;
 * </pre>
 */
@Documented
@Constraint(validatedBy = CmmnValidJuminNumberConstraint.class)
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD, ElementType.FIELD })
public @interface ValidJuminNumber {
	String message() default "{validate.jumin.number}";
	Class<?> [] groups() default {};
	Class<? extends Payload>[] payload() default {};
}
