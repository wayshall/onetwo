package org.onetwo.common.spring.aop;

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.function.Supplier;

import org.aopalliance.aop.Advice;
import org.aopalliance.intercept.MethodInterceptor;
import org.onetwo.common.exception.BaseException;
import org.springframework.aop.Pointcut;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.aop.support.annotation.AnnotationMatchingPointcut;

/**
 * @author wayshall
 * <br/>
 */
public abstract class Proxys {
	
	public static <T> T intercept(Object target, Class<? extends Annotation> annoClass, MethodInterceptor methodInterceptor){
		return intercept(target, ()->AnnotationMatchingPointcut.forMethodAnnotation(annoClass), methodInterceptor);
	}
	
	public static <T> T intercept(Object target, Supplier<Pointcut> pointcutCreator, MethodInterceptor methodInterceptor){
		return intercept(target, pointcutCreator.get(), methodInterceptor);
	}

	public static <T> T intercept(Object target, MethodInterceptor methodInterceptor){
		return intercept(target, Pointcut.TRUE, methodInterceptor);
	}

	public static <T> T intercept(Object target, Pointcut pointcut, MethodInterceptor methodInterceptor){
		return advice(target, pointcut, methodInterceptor);
	}

	@SuppressWarnings("unchecked")
	public static <T> T advice(Object target, Pointcut pointcut, Advice advice){
		ProxyFactory pf = new ProxyFactory(target);
		pf.setOptimize(true);
		pf.setProxyTargetClass(true);
		pf.addAdvisor(new DefaultPointcutAdvisor(pointcut, advice));
		return (T)pf.getProxy();
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T interceptInterfaces(List<Class<?>> proxiedInterfaces, MethodInterceptor methodInterceptor){
		ProxyFactory pf = new ProxyFactory();
		/*pf.setOptimize(true);
		pf.setProxyTargetClass(true);*/
		pf.addAdvisor(new DefaultPointcutAdvisor(Pointcut.TRUE, methodInterceptor));
		proxiedInterfaces.forEach(interf->{
			if(!interf.isInterface()){
				throw new BaseException(interf + " is not a interface");
			}
			pf.addInterface(interf);
		});
		return (T) pf.getProxy();
	}
	
}
