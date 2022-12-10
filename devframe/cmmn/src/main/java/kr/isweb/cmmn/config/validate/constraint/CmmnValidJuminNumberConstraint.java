package kr.isweb.cmmn.config.validate.constraint;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.apache.commons.lang3.StringUtils;

import kr.isweb.cmmn.config.validate.annotation.ValidJuminNumber;
import kr.isweb.cmmn.util.egov.EgovNumberCheckUtil;

public class CmmnValidJuminNumberConstraint implements ConstraintValidator<ValidJuminNumber, String> {
	@Override
	public void initialize(ValidJuminNumber constraintAnnotation) {
	}

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		if(StringUtils.isNotBlank(value)) {
			return EgovNumberCheckUtil.checkJuminNumber(value);
		}
		return true;
	}
}
