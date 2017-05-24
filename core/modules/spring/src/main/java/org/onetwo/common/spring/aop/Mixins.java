package org.onetwo.common.spring.aop;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.onetwo.common.reflect.ReflectUtils;
import org.onetwo.common.spring.Springs;
import org.onetwo.common.spring.aop.Mixin.MixinInitor;
import org.springframework.aop.Advisor;
import org.springframework.aop.DynamicIntroductionAdvice;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.aop.support.DefaultIntroductionAdvisor;
import org.springframework.aop.support.DelegatingIntroductionInterceptor;
import org.springframework.core.annotation.AnnotationUtils;

/**
 * @author wayshall
 * <br/>
 */
public class Mixins {

	@SuppressWarnings("unchecked")
	public static <T> T mixins(Object obj, Class<?>... mixinInterfaces){
		List<Advisor> advisors = Stream.of(mixinInterfaces)
										.map(Mixins::createMixinAdvisor)
										.collect(Collectors.toList());
		ProxyFactory proxy = new ProxyFactory(obj);
		proxy.addAdvisors(advisors);
		proxy.setOptimize(true);
		obj = proxy.getProxy();
		return (T)obj;
	}
	
	private static Advisor createMixinAdvisor(Class<?> mixinInterface){
		if(!mixinInterface.isInterface()){
			throw new IllegalArgumentException("mixinInterface must be a interface");
		}
		Mixin mixin = AnnotationUtils.findAnnotation(mixinInterface, Mixin.class);
		if(mixin==null){
			throw new IllegalArgumentException("@Mixin not found on interface: " + mixinInterface);
		}
		Class<?> implementorClass = mixin.value();
		MixinInitor initor = mixin.initor();
		Object implementor = null;
		if(initor == MixinInitor.SPRING){
			implementor = Springs.getInstance().getBean(implementorClass);
		}else{
			implementor = ReflectUtils.newInstance(implementorClass);
		}

		DynamicIntroductionAdvice interceptor = null;
		if(DynamicIntroductionAdvice.class.isInstance(implementor)){
			interceptor = (DynamicIntroductionAdvice)implementor;
		}else{
			interceptor = new DelegatingIntroductionInterceptor(implementor);
		}
		DefaultIntroductionAdvisor advisor = new DefaultIntroductionAdvisor(interceptor, mixinInterface);
		return advisor;
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T mixin(Object obj, Class<?> mixinInterface){
		ProxyFactory proxy = new ProxyFactory(obj);
		proxy.addAdvisor(createMixinAdvisor(mixinInterface));
		proxy.setOptimize(true);
		obj = proxy.getProxy();
		return (T)obj;
	}
}
