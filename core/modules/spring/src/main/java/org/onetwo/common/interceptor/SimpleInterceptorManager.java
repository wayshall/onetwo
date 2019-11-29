package org.onetwo.common.interceptor;

import java.util.List;

import org.onetwo.common.spring.SpringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.AnnotationAwareOrderComparator;

import com.google.common.collect.ImmutableList;

public class SimpleInterceptorManager<T extends Interceptor> implements InitializingBean {
	
	private List<T> interceptors;
	@Autowired
	private ApplicationContext applicationContext;
	private Class<T> interceptorClass;
	
	public SimpleInterceptorManager(Class<T> interceptorClass) {
		super();
		this.interceptorClass = interceptorClass;
	}

	@Override
	public void afterPropertiesSet() {
		this.interceptors = SpringUtils.getBeans(applicationContext, interceptorClass);
		/*List<Interceptor> interceptors = SpringUtils.getBeansWithAnnotation(applicationContext, annotationClass)
													.stream().map(bd -> (Interceptor)bd.getBean())
													.collect(Collectors.toList());*/
		
		AnnotationAwareOrderComparator.sort(interceptors);
		this.interceptors = ImmutableList.copyOf(interceptors);
		
	}

	public List<T> getInterceptors() {
		return interceptors;
	}

	public void setInterceptors(List<T> interceptors) {
		this.interceptors = interceptors;
	}
	
}
