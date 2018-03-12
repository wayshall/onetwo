package org.onetwo.cloud.feign.local;

import org.onetwo.common.spring.aop.Proxys;
import org.onetwo.common.spring.aop.Proxys.SpringBeanMethodInterceptor;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

/**
 * @author wayshall
 * <br/>
 */
public class LocalFeignInvokerFactoryBean implements FactoryBean<Object>{
	@Autowired
	private ApplicationContext applicationContext;
	private Class<?> remoteInterface;
	private String localBeanName;
	

	@Override
	public Object getObject() throws Exception {
		Object localProxy = Proxys.delegateInterface(remoteInterface, new SpringBeanMethodInterceptor(applicationContext, localBeanName));
		return localProxy;
	}

	@Override
	public Class<?> getObjectType() {
		return remoteInterface;
	}

	@Override
	public boolean isSingleton() {
		return true;
	}

	public void setRemoteInterface(Class<?> remoteInterface) {
		this.remoteInterface = remoteInterface;
	}

	public void setLocalBeanName(String localBeanName) {
		this.localBeanName = localBeanName;
	}

}
