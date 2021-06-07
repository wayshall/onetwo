package org.onetwo.boot.core.web.async;

import org.onetwo.boot.core.web.async.DelegateTaskDecorator.AsyncTaskDecorator;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

/**
 * @author weishao zeng
 * <br/>
 */

public class WebContextAsyncTaskDecorator implements AsyncTaskDecorator {

	@Override
	public Runnable decorate(Runnable runnable) {
		RequestAttributes attrs = RequestContextHolder.getRequestAttributes();
		if (attrs==null) {
			return runnable;
		}
		return () -> {
			RequestContextHolder.setRequestAttributes(attrs);
			try {
				runnable.run();
			} finally {
				RequestContextHolder.resetRequestAttributes();
			}
		};
	}

}
