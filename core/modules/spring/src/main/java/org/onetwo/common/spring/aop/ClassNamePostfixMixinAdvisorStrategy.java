package org.onetwo.common.spring.aop;

import org.onetwo.common.exception.BaseException;
import org.onetwo.common.reflect.ReflectUtils;
import org.onetwo.common.spring.Springs;
import org.onetwo.common.spring.aop.Mixin.MixinFrom;
import org.springframework.aop.Advisor;
import org.springframework.aop.DynamicIntroductionAdvice;
import org.springframework.aop.support.DefaultIntroductionAdvisor;
import org.springframework.aop.support.DelegatingIntroductionInterceptor;
import org.springframework.util.ClassUtils;

/**
 * @author wayshall
 * <br/>
 */
public class ClassNamePostfixMixinAdvisorStrategy implements MixinAdvisorStrategy {
	
	private static final String IMPLEMENTOR_POSTFIX = "Impl";
	
	@Override
	public boolean isMixinInterface(Class<?> interfaceClass) {
		return interfaceClass.isInterface() && ClassUtils.isPresent(getImplementorClassName(interfaceClass), null);
	}

	private String getImplementorClassName(Class<?> interfaceClass){
		return interfaceClass.getName()+IMPLEMENTOR_POSTFIX;
	}

	@Override
	public Advisor createAdvisor(Class<?> mixinInterface) {
		String implementorClassName = getImplementorClassName(mixinInterface);
		Class<?> implementor;
		try {
			implementor = ClassUtils.forName(implementorClassName, null);
		}catch (Throwable e) {
			throw new BaseException("load mixin implementor class error: " + implementorClassName);
		}
		MixinAttrs attrs = new MixinAttrs(implementor, MixinFrom.DEFAULLT);
		return createMixinAdvisor(mixinInterface, attrs);
	}
	

	protected Advisor createMixinAdvisor(Class<?> mixinInterface, MixinAttrs mixin){
		if(!mixinInterface.isInterface()){
			throw new IllegalArgumentException("mixinInterface must be a interface");
		}
		
		Class<?> implementorClass = mixin.getImplementor();
		MixinFrom initor = mixin.getFrom();
		Object implementor = null;
		
		if(initor==MixinFrom.DEFAULLT){
			initor = Springs.getInstance().isInitialized()?MixinFrom.SPRING:MixinFrom.REFLECTION;
		}
		
		if(initor == MixinFrom.SPRING){
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
	
	
}
