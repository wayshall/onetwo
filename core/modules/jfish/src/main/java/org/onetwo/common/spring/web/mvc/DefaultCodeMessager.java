package org.onetwo.common.spring.web.mvc;

import javax.annotation.Resource;

import org.onetwo.common.fish.JFishUtils;
import org.onetwo.common.fish.spring.config.JFishContextConfig.ContextBeanNames;
import org.springframework.context.MessageSource;

public class DefaultCodeMessager implements CodeMessager {

	@Resource(name=ContextBeanNames.EXCEPTION_MESSAGE)
	private MessageSource exceptionMessages;

	@Override
	public String getMessage(String code, Object... args) {
		return JFishUtils.getMessage(exceptionMessages, code, args);
	}

}
