package org.onetwo.common.spring.web.authentic;

import org.onetwo.common.spring.SpringApplication;
import org.onetwo.common.web.s2.security.Authenticator;
import org.onetwo.common.web.s2.security.config.AbstractConfigBuilder;

public class SpringConfigBuilder extends AbstractConfigBuilder {

	/*public SpringConfigBuilder(Class<?> clazz, Method method) {
		super(clazz, method);
	}*/

	@Override
	protected Authenticator getAuthenticator(String name) {
		return (Authenticator)SpringApplication.getInstance().getBean(name);
	}

	@Override
	protected <T extends Authenticator> T getAuthenticator(Class<T> cls) {
		T bean = SpringApplication.getInstance().getBean(cls);
		if(bean==null){
			bean = SpringApplication.getInstance().getOrRegisteredBean(cls);
		}
		return bean;
	}

}
