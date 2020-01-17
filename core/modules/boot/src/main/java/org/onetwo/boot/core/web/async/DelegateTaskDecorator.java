package org.onetwo.boot.core.web.async;

import org.onetwo.common.spring.SpringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.task.TaskDecorator;

/**
 * @author weishao zeng
 * <br/>
 */
public class DelegateTaskDecorator implements TaskDecorator {
	
	@Autowired
	private ApplicationContext applicationContext;
	@Autowired(required=false)
	private AsyncTaskDecorator asyncTaskDecorator;
	

	@Override
	public Runnable decorate(Runnable runnable) {
		AsyncTaskDecorator asyncTaskDecorator = this.getAsyncTaskDecorator();
		if (asyncTaskDecorator==null) {
			return runnable;
		}
		return asyncTaskDecorator.decorate(runnable);
	}
	
	
	
	public AsyncTaskDecorator getAsyncTaskDecorator() {
		AsyncTaskDecorator asyncTaskDecorator = this.asyncTaskDecorator;
		if (asyncTaskDecorator==null) {
			asyncTaskDecorator = SpringUtils.getBean(applicationContext, AsyncTaskDecorator.class);
			this.asyncTaskDecorator = asyncTaskDecorator;
		}
		return asyncTaskDecorator;
	}

	public static interface AsyncTaskDecorator {
		Runnable decorate(Runnable runnable);
	}
	
}
