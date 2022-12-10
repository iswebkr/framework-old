package kr.isweb.cmmn.config.validate.constraint;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.apache.commons.lang3.StringUtils;

import kr.isweb.cmmn.config.validate.annotation.ValidCorpNumber;
import kr.isweb.cmmn.util.egov.EgovNumberCheckUtil;

public class CmmnValidCorpNumberConstraint implements ConstraintValidator<ValidCorpNumber, String> {
	@Override
	public void initialize(ValidCorpNumber constraintAnnotation) {
	}

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		if(StringUtils.isNotBlank(value)) {
			return EgovNumberCheckUtil.checkBubinNumber(value);
		}
		return true;
	}
}
