package org.onetwo.common.spring.web.authentic;

import org.onetwo.common.spring.SpringUtils;
import org.onetwo.common.utils.Assert;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.web.servlet.handler.MappedInterceptor;

public class SecurityInterceptorFacotryBean implements FactoryBean<MappedInterceptor>, InitializingBean, ApplicationContextAware {

	private ApplicationContext context;
	private MappedInterceptor mappedSecurityInterceptor;
	
	@Override
	public void afterPropertiesSet() throws Exception {
		SpringSecurityInterceptor springSecurityInterceptor = SpringUtils.getHighestOrder(context, SpringSecurityInterceptor.class);
		Assert.notNull(springSecurityInterceptor, "you must config the springSecurityInterceptor");
		this.mappedSecurityInterceptor = new MappedInterceptor(null, springSecurityInterceptor);
	}

	@Override
	public MappedInterceptor getObject() throws Exception {
		return mappedSecurityInterceptor;
	}

	@Override
	public Class<?> getObjectType() {
		return mappedSecurityInterceptor==null?MappedInterceptor.class:mappedSecurityInterceptor.getClass();
	}

	@Override
	public boolean isSingleton() {
		return true;
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.context = applicationContext;
	}
}
