package org.onetwo.common.spring.aop;

import java.lang.annotation.Annotation;
import java.util.function.Supplier;

import org.aopalliance.intercept.MethodInterceptor;
import org.springframework.aop.Pointcut;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.aop.support.annotation.AnnotationMatchingPointcut;

/**
 * @author wayshall
 * <br/>
 */
public abstract class Proxys {
	
	public static <T> T proxy(Object target, Class<? extends Annotation> annoClass, MethodInterceptor methodInterceptor){
		return proxy(target, ()->AnnotationMatchingPointcut.forMethodAnnotation(annoClass), methodInterceptor);
	}
	
	public static <T> T proxy(Object target, Supplier<Pointcut> pointcutCreator, MethodInterceptor methodInterceptor){
		return proxy(target, pointcutCreator.get(), methodInterceptor);
	}

	@SuppressWarnings("unchecked")
	public static <T> T proxy(Object target, Pointcut pointcut, MethodInterceptor methodInterceptor){
		ProxyFactory pf = new ProxyFactory(target);
		pf.addAdvisor(new DefaultPointcutAdvisor(pointcut, methodInterceptor));
		return (T)pf.getProxy();
	}
	
}
