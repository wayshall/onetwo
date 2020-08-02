package org.onetwo.common.spring.validator;

import java.util.List;
import java.util.Map;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.internal.engine.constraintvalidation.ConstraintValidatorContextImpl;
import org.onetwo.common.exception.BaseException;
import org.onetwo.common.spring.SpringUtils;
import org.onetwo.common.spring.Springs;
import org.onetwo.common.spring.validator.annotation.ContentCheck;
import org.onetwo.common.utils.LangUtils;
import org.springframework.beans.ConfigurablePropertyAccessor;

import com.google.common.collect.Maps;

/***
 * hibernate validator只把有限的一些变量放到了i18n错误信息的上下文里，参考文档：
 * 
 * https://docs.jboss.org/hibernate/validator/5.1/reference/en-US/html/chapter-message-interpolation.html#section-interpolation-with-message-expressions
 * 4.1.2. Interpolation with message expressions
 * As of Hibernate Validator 5 (Bean Validation 1.1) it is possible to use the Unified Expression Language (as defined by JSR 341) in constraint violation messages. This allows to define error messages based on conditional logic and also enables advanced formatting options. The validation engine makes the following objects available in the EL context:

> the attribute values of the constraint mapped to the attribute names

> the currently validated value (property, bean, method parameter etc.) under the name validatedValue

> a bean mapped to the name formatter exposing the var-arg method format(String format, Object... args) which behaves like java.util.Formatter.format(String format, Object... args).
也参考了：
https://jinnianshilongnian.iteye.com/blog/1990081

所以这里暴力修改一下相关属性～

 * @author way
 *
 */
public class ContentCheckConstraintValidator implements ConstraintValidator<ContentCheck, String> {
	
	private ContentChecker contentChecker;

	@Override
	public void initialize(ContentCheck constraintAnnotation) {
		if (Springs.getInstance().isActive()) {
			this.contentChecker = Springs.getInstance().getBean(ContentChecker.class);
		}
	}

	/***
	 * @param value 需要检测到字段内容
	 */
	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		if (StringUtils.isBlank(value)) {
			return true;
		}
		List<String> sentitiveWords = getContentChecker().check(value);
		if (LangUtils.isEmpty(sentitiveWords)) {
			return true;
		}
		ConstraintValidatorContextImpl ctx = (ConstraintValidatorContextImpl)context;
		
		// 这种方法虽然可以动态显示错误信息，但是无法把错误信息放到i18n国际化资源文件里
//		ctx.addExpressionVariable("invalidWords", StringUtils.join(sentitiveWords, ", "));
//		ctx.buildConstraintViolationWithTemplate("${invalidWords}").addConstraintViolation();
		
		Map<String, Object> newAttributes = Maps.newHashMap(ctx.getConstraintDescriptor().getAttributes());
		newAttributes.put("invalidWords", StringUtils.join(sentitiveWords, ", "));
		ConfigurablePropertyAccessor constraintDescriptor = SpringUtils.newPropertyAccessor(ctx.getConstraintDescriptor(), true);
		constraintDescriptor.setPropertyValue("attributes", newAttributes);
		return false;
	}

	public ContentChecker getContentChecker() {
		if (contentChecker==null) {
			throw new BaseException("ContentChecker not found!");
		}
		return contentChecker;
	}
	
}
