package kr.isweb.cmmn.config.validate.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import kr.isweb.cmmn.config.validate.constraint.CmmnValidBizNumberConstraint;

/**
 * 사업자등록번호 Validation 용 Annotation
 * <pre>
 * @ValidBizNumber
 * private String bizNo;
 * </pre>
 */
@Documented
@Constraint(validatedBy = CmmnValidBizNumberConstraint.class)
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD, ElementType.FIELD })
public @interface ValidBizNumber {
	String message() default "{validate.biz.number}";
	Class<?> [] groups() default {};
	Class<? extends Payload>[] payload() default {};
}
