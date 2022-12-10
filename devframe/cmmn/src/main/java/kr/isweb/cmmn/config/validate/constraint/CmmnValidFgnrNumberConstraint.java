package kr.isweb.cmmn.config.validate.constraint;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.apache.commons.lang3.StringUtils;

import kr.isweb.cmmn.config.validate.annotation.ValidFgnrNumber;
import kr.isweb.cmmn.util.egov.EgovNumberCheckUtil;

public class CmmnValidFgnrNumberConstraint implements ConstraintValidator<ValidFgnrNumber, String> {
	@Override
	public void initialize(ValidFgnrNumber constraintAnnotation) {
	}

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		if(StringUtils.isNotBlank(value)) {
			return EgovNumberCheckUtil.checkforeignNumber(value);
		}
		return true;
	}
}
