package kr.isweb.cmmn.config.validate.constraint;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.apache.commons.lang3.StringUtils;

import kr.isweb.cmmn.config.validate.annotation.ValidBizNumber;
import kr.isweb.cmmn.util.egov.EgovNumberCheckUtil;

public class CmmnValidBizNumberConstraint implements ConstraintValidator<ValidBizNumber, String> {
	@Override
	public void initialize(ValidBizNumber constraintAnnotation) {
	}

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		if(StringUtils.isNotBlank(value)) {
			return EgovNumberCheckUtil.checkCompNumber(value);
		}
		return true;
	}
}
