package org.onetwo.common.spring.validator;

import java.util.List;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.apache.commons.lang3.StringUtils;
import org.onetwo.common.exception.BaseException;
import org.onetwo.common.spring.Springs;
import org.onetwo.common.spring.validator.annotation.ContentCheck;

public class ContentCheckConstraintValidator implements ConstraintValidator<ContentCheck, String> {
	
	private ContentChecker contentChecker;

	@Override
	public void initialize(ContentCheck constraintAnnotation) {
		if (Springs.getInstance().isActive()) {
			this.contentChecker = Springs.getInstance().getBean(ContentChecker.class);
		}
	}

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		if (StringUtils.isBlank(value)) {
			return true;
		}
		List<String> sentitiveWords = getContentChecker().check(value);
		if (sentitiveWords.isEmpty()) {
			return true;
		}
		context.disableDefaultConstraintViolation();
		context.buildConstraintViolationWithTemplate("发现敏感词：" + StringUtils.join(sentitiveWords, ", "))
				.addConstraintViolation();
		return false;
	}

	public ContentChecker getContentChecker() {
		if (contentChecker==null) {
			throw new BaseException("ContentChecker not found!");
		}
		return contentChecker;
	}
	
}
