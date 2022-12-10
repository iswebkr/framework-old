package kr.isweb.cmmn.config.validate.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import kr.isweb.cmmn.config.validate.constraint.CmmnValidMobilePhoneConstraint;

/**
 * 핸드폰(스마트폰)번호 Validation 용 Annotation
 * <pre>
 * @ValidMobilePhone
 * private String mobilePhone;
 * </pre>
 */
@Documented
@Constraint(validatedBy = CmmnValidMobilePhoneConstraint.class)
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD, ElementType.FIELD })
public @interface ValidMobilePhone {
	String message() default "{validate.mobile.phone}";
	Class<?> [] groups() default {};
	Class<? extends Payload>[] payload() default {};
}
