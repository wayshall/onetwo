package org.onetwo.cloud.feign.local;

import org.onetwo.common.reflect.ReflectUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

/**
 * @author wayshall
 * <br/>
 */
public class LocalFeignBeanPostProcessor implements BeanPostProcessor {

	@Override
	public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
		if(ReflectUtils.loadClass("com.icloudsoft.neo.client.uaa.api.TenementClientApi").isInstance(bean) ||
				ReflectUtils.loadClass("org.springframework.cloud.netflix.feign.FeignClientFactoryBean").isInstance(bean)){
			System.out.println("test");
		}
		System.out.println("beanname: " + beanName+", bean: " +bean);
		return bean;
	}

	@Override
	public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
		return bean;
	}
	
	

}
