package org.onetwo.common.spring.validator;

import java.util.Map;

import javax.validation.ConstraintValidatorContext;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.internal.engine.constraintvalidation.ConstraintValidatorContextImpl;
import org.onetwo.common.exception.BaseException;
import org.onetwo.common.spring.SpringUtils;
import org.springframework.beans.ConfigurablePropertyAccessor;

import com.google.common.collect.Maps;

/***
 * @author way
 *
 */
abstract public class BaseConstraintValidator {
	
	/***
	 * 添加错误消息变量，以便国际化
	 * @author weishao zeng
	 * @param context
	 * @param varName
	 * @param varValue
	 */
	protected void addMessageFormatVariable(ConstraintValidatorContext context, String varName, Object varValue) {
		if (StringUtils.isBlank(varName)) {
			throw new BaseException("varName can not be blank!");
		}
		ConstraintValidatorContextImpl ctx = (ConstraintValidatorContextImpl)context;
		Map<String, Object> newAttributes = Maps.newHashMap(ctx.getConstraintDescriptor().getAttributes());
		newAttributes.put(varName, varValue);
		ConfigurablePropertyAccessor constraintDescriptor = SpringUtils.newPropertyAccessor(ctx.getConstraintDescriptor(), true);
		constraintDescriptor.setPropertyValue("attributes", newAttributes);
	}

	
}
