package org.onetwo.cloud.env;

import org.onetwo.boot.core.web.async.SimpleTaskDecorator.AsyncTaskDecorator;
import org.onetwo.cloud.env.AuthEnvs.AuthEnv;
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
		AuthEnv webAuthEnv = authEnvs.createWebAuthEnv(true);
		return () -> {
			authEnvs.runInCurrentEnvs(webAuthEnv, () -> {
				runnable.run();
				return null;
			});
		};
	}
	
	

}
