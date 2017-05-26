package org.onetwo.common.spring.aop;

import java.lang.annotation.Annotation;
import java.util.List;
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
	
	public static <T> T of(Object target, Class<? extends Annotation> annoClass, MethodInterceptor methodInterceptor){
		return of(target, ()->AnnotationMatchingPointcut.forMethodAnnotation(annoClass), methodInterceptor);
	}
	
	public static <T> T of(Object target, Supplier<Pointcut> pointcutCreator, MethodInterceptor methodInterceptor){
		return of(target, pointcutCreator.get(), methodInterceptor);
	}

	public static <T> T of(Object target, MethodInterceptor methodInterceptor){
		return of(target, Pointcut.TRUE, methodInterceptor);
	}

	@SuppressWarnings("unchecked")
	public static <T> T of(Object target, Pointcut pointcut, MethodInterceptor methodInterceptor){
		ProxyFactory pf = new ProxyFactory(target);
		pf.setOptimize(true);
		pf.setProxyTargetClass(true);
		pf.addAdvisor(new DefaultPointcutAdvisor(pointcut, methodInterceptor));
		return (T)pf.getProxy();
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T ofInterfaces(List<Class<?>> proxiedInterfaces, MethodInterceptor methodInterceptor){
		ProxyFactory pf = new ProxyFactory();
		/*pf.setOptimize(true);
		pf.setProxyTargetClass(true);*/
		pf.addAdvisor(new DefaultPointcutAdvisor(Pointcut.TRUE, methodInterceptor));
		proxiedInterfaces.forEach(pf::addInterface);
		return (T) pf.getProxy();
	}
	
}
