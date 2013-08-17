package org.onetwo.common.spring.web.mvc;

import javax.annotation.Resource;

import org.onetwo.common.spring.web.mvc.config.JFishMvcConfig.MvcBeanNames;
import org.onetwo.common.spring.web.utils.JFishWebUtils;
import org.springframework.context.MessageSource;

public class DefaultCodeMessager implements CodeMessager {

	@Resource(name=MvcBeanNames.EXCEPTION_MESSAGE)
	private MessageSource exceptionMessages;

	@Override
	public String getMessage(String code, Object... args) {
		return JFishWebUtils.getMessage(exceptionMessages, code, args);
	}

}
