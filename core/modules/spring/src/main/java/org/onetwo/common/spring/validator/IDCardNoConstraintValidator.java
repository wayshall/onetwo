package org.onetwo.common.spring.validator;

import java.util.regex.Pattern;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.apache.commons.lang3.StringUtils;
import org.onetwo.common.spring.validator.annotation.IDCardNo;

/***
 * @author way
 *
 */
public class IDCardNoConstraintValidator extends BaseConstraintValidator implements ConstraintValidator<IDCardNo, String> {
	
	private Pattern idcardNoPattern;
	
	@Override
	public void initialize(IDCardNo constraintAnnotation) {
		String mobilePattern = constraintAnnotation.value();
		this.idcardNoPattern = Pattern.compile(mobilePattern);
	}

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		if (StringUtils.isBlank(value)) {
			return true;
		}
		
		boolean isValidMobile = this.idcardNoPattern.matcher(value).matches();
		addMessageFormatVariable(context, "idcardNo", value);
		return isValidMobile;
	}

}
