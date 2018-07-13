package org.onetwo.cloud.bugfix;

import org.onetwo.boot.bugfix.AllEncompassingFormHttpMessageConverter;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.cloud.netflix.zuul.filters.pre.FormBodyWrapperFilter;

/**
 * bug相关连接：
 * https://github.com/spring-cloud/spring-cloud-netflix/issues/1385
 * 
 * https://jira.spring.io/browse/SPR-15205
 * 
 * @author wayshall
 * <br/>
 */
public class FixFormBodyWrapperFilterPostProcessor implements BeanPostProcessor {

	@Override
	public Object postProcessBeforeInitialization(Object bean, String beanName)
			throws BeansException {
		if(bean instanceof FormBodyWrapperFilter){
			//因为关键代码都是private，所以直接复制了spring 5.0 的两个类过来
			AllEncompassingFormHttpMessageConverter converter = new AllEncompassingFormHttpMessageConverter();
//			converter.setMultipartCharset(Charset.forName("utf-8"));
			return new FormBodyWrapperFilter(converter);
		}
		return bean;
	}

	@Override
	public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
		return bean;
	}

}
