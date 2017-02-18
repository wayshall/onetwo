package org.onetwo.common.spring.validator;

import java.util.stream.Stream;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.onetwo.common.spring.SpringUtils;
import org.onetwo.common.spring.validator.annotation.AnyOneNotBlank;
import org.onetwo.common.utils.StringUtils;
import org.springframework.beans.BeanWrapper;

public class AnyOneNotBlankConstraintValidator implements ConstraintValidator<AnyOneNotBlank, Object> {
	
	private String[] fields;

	@Override
	public void initialize(AnyOneNotBlank constraintAnnotation) {
		this.fields = constraintAnnotation.value();
	}

	@Override
	public boolean isValid(Object value, ConstraintValidatorContext context) {
		BeanWrapper bw = SpringUtils.newBeanWrapper(value);
		return Stream.of(fields).anyMatch(field->{
			Object propValue = bw.getPropertyValue(field);
			if(propValue instanceof String){
				return StringUtils.isNotBlank(propValue.toString());
			}
			return propValue!=null;
		});
		
		/*String fieldString = StringUtils.join(this.fields, ", ");
		context.disableDefaultConstraintViolation();
		String messageTemplate = context.getDefaultConstraintMessageTemplate();
		ConstraintViolationBuilder builder = context.buildConstraintViolationWithTemplate(messageTemplate);*/
	}

}
