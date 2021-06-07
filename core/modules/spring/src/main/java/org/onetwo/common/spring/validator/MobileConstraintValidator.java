package org.onetwo.common.spring.validator;

import java.util.regex.Pattern;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.apache.commons.lang3.StringUtils;
import org.onetwo.common.spring.validator.annotation.Mobile;

/***
 * @author way
 *
 */
public class MobileConstraintValidator extends BaseConstraintValidator implements ConstraintValidator<Mobile, String> {
	
	private Pattern mobilePattern;
	
	@Override
	public void initialize(Mobile constraintAnnotation) {
		String mobilePattern = constraintAnnotation.value();
		this.mobilePattern = Pattern.compile(mobilePattern);
	}

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		if (StringUtils.isBlank(value)) {
			return true;
		}
		
		boolean isValidMobile = this.mobilePattern.matcher(value).matches();
		addMessageFormatVariable(context, "mobile", value);
		return isValidMobile;
	}

}
