package org.onetwo.common.spring.validator;

import java.util.Map;

import javax.validation.ConstraintValidatorContext;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.internal.engine.constraintvalidation.ConstraintValidatorContextImpl;
import org.hibernate.validator.internal.metadata.descriptor.ConstraintDescriptorImpl;
import org.onetwo.common.exception.BaseException;
import org.onetwo.common.log.JFishLoggerFactory;
import org.onetwo.common.spring.SpringUtils;
import org.springframework.beans.ConfigurablePropertyAccessor;
import org.springframework.beans.NotWritablePropertyException;

import com.google.common.collect.Maps;

/***
 * 自定义i18n消息里，可以使用${validatedValue}内置变量
 * 但有时需要使用一些额外的，则需要此扩展，额外的扩展使用{}符号访问
 * @author way
 *
 */
abstract public class BaseConstraintValidator {

	protected void addMessageFormatVariable(ConstraintValidatorContext context, String varName, Object varValue) {
		if (StringUtils.isBlank(varName)) {
			throw new BaseException("varName can not be blank!");
		}
		if (!addMessageFormatVariableForValidator5(context, varName, varValue)) {
			addMessageFormatVariableForValidator6(context, varName, varValue);
		}
	}

	protected void addMessageFormatVariableForValidator6(ConstraintValidatorContext context, String varName, Object varValue) {
		ConstraintValidatorContextImpl ctx = (ConstraintValidatorContextImpl)context;
		ConstraintDescriptorImpl<?> descriptor = (ConstraintDescriptorImpl<?>)ctx.getConstraintDescriptor();
		
//		descriptor.getAnnotationDescriptor().getAttributes().put(varName, varValue);
		Map<String, Object> newAttributes = Maps.newHashMap(descriptor.getAnnotationDescriptor().getAttributes());
		newAttributes.put(varName, varValue);
		ConfigurablePropertyAccessor constraintDescriptor = SpringUtils.newPropertyAccessor(descriptor.getAnnotationDescriptor(), true);
		constraintDescriptor.setPropertyValue("attributes", newAttributes);
	}
	
	/***
	 * 添加错误消息变量，以便国际化
	 * @deprecated 升级到hiberante-validator 6.x 后出错，废弃
	 * @author weishao zeng
	 * @param context
	 * @param varName
	 * @param varValue
	 */
	protected boolean addMessageFormatVariableForValidator5(ConstraintValidatorContext context, String varName, Object varValue) {
		ConstraintValidatorContextImpl ctx = (ConstraintValidatorContextImpl)context;
		ConfigurablePropertyAccessor constraintDescriptor = SpringUtils.newPropertyAccessor(ctx.getConstraintDescriptor(), true);
		
		if (!constraintDescriptor.isWritableProperty("attributes")) {
			return false;
		}

		// ConstraintDescriptor的attributes是个不可修改的hashmap
		Map<String, Object> newAttributes = Maps.newHashMap(ctx.getConstraintDescriptor().getAttributes());
		newAttributes.put(varName, varValue);
		try {
			//升级到spring boot 2.x 后出错，废弃
			constraintDescriptor.setPropertyValue("attributes", newAttributes);
		} catch (NotWritablePropertyException e) {
			JFishLoggerFactory.getCommonLogger().error("ConstraintDescriptor set attributes error: " + e.getMessage());
			return false;
		}
		return true;
	}

	
}
