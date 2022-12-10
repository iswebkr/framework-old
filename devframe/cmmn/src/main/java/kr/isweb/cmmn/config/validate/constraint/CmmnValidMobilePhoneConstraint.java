package kr.isweb.cmmn.config.validate.constraint;

import java.util.regex.Pattern;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.apache.commons.lang3.StringUtils;

import kr.isweb.cmmn.config.validate.annotation.ValidMobilePhone;

public class CmmnValidMobilePhoneConstraint implements ConstraintValidator<ValidMobilePhone, String> {
	@Override
	public void initialize(ValidMobilePhone constraintAnnotation) {
	}

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		Pattern pattern = Pattern.compile("^01(?:0|1)[?:.-](\\d{3}|\\d{4})[?:.-]([0-9]{4})$");
		if(StringUtils.isNotBlank(value)) {
			return pattern.matcher(value).matches();
		}
		return true;
	}
}
