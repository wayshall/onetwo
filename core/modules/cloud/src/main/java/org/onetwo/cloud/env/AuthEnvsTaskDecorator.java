package org.onetwo.cloud.env;

import org.onetwo.boot.core.web.async.DelegateTaskDecorator.AsyncTaskDecorator;
import org.onetwo.common.web.utils.WebHolder;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author weishao zeng
 * <br/>
 */
public class AuthEnvsTaskDecorator implements AsyncTaskDecorator {
	
	@Autowired
	private AuthEnvs authEnvs;

	@Override
	public Runnable decorate(Runnable runnable) {
		if (!WebHolder.getRequest().isPresent()) {
			return runnable;
		}
		return authEnvs.decorate(runnable);
	}

}
