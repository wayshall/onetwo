package org.onetwo.common.spring.aop;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

import org.aopalliance.aop.Advice;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.onetwo.common.exception.BaseException;
import org.springframework.aop.Pointcut;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.aop.support.AopUtils;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.aop.support.annotation.AnnotationMatchingPointcut;
import org.springframework.context.ApplicationContext;

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
	

	public static <T> T interceptInterface(Class<T> proxiedInterface, MethodInterceptor methodInterceptor){
		return interceptInterfaces(Arrays.asList(proxiedInterface), methodInterceptor);
	}
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static <T> T interceptInterfaces(List<Class> proxiedInterfaces, MethodInterceptor methodInterceptor){
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
	
	/***
	 * 此方法通常用于被代理的接口是delegate实现接口的子接口
	 * interface Parent {}
	 * interface Child extends Parent {}
	 * class ParentImpl implements Parent {}
	 * Child child = interceptInterface(Child.class, new ParentImpl());
	 * @author wayshall
	 * @param proxiedInterface
	 * @param delegate
	 * @return
	 */
	public static <T> T delegateInterface(Class<T> proxiedInterface, Object delegate){
		return interceptInterfaces(Arrays.asList(proxiedInterface), new DelegateMethodInterceptor(delegate));
	}

	public static class DelegateMethodInterceptor implements MethodInterceptor {
		final private Object delegate;
		public DelegateMethodInterceptor(Object delegate) {
			this.delegate = delegate;
		}
		@Override
		public Object invoke(MethodInvocation invocation) throws Throwable {
			return AopUtils.invokeJoinpointUsingReflection(delegate, invocation.getMethod(), invocation.getArguments());
		}
	}

	public static class SpringBeanMethodInterceptor implements MethodInterceptor {
		final ApplicationContext applicationContext;
		final private String beanName;
		private Object bean;
		public SpringBeanMethodInterceptor(ApplicationContext applicationContext, String beanName) {
			this.beanName = beanName;
			this.applicationContext = applicationContext;
		}
		@Override
		public Object invoke(MethodInvocation invocation) throws Throwable {
			Object target = this.bean;
			if(target==null){
				target = applicationContext.getBean(beanName);
				this.bean = target;
			}
			return AopUtils.invokeJoinpointUsingReflection(target, invocation.getMethod(), invocation.getArguments());
		}
	}
	/*public static <T> T interceptInterfaces(List<Class<?>> proxiedInterfaces, MethodInvocationDelegateFunc deleteFunc){
		return interceptInterfaces(proxiedInterfaces, new MethodInterceptor(){
			@Override
			public Object invoke(MethodInvocation invocation) throws Throwable {
				return deleteFunc.invoke(invocation);
			}
		});
	}
	
	@FunctionalInterface
	public static interface MethodInvocationDelegateFunc {
		Object invoke(MethodInvocation invocation) throws Throwable;
	}
	*/
	
}
