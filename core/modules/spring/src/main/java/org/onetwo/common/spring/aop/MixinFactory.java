package org.onetwo.common.spring.aop;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 使用不同的mixin策略创建mixin对象
 * @author wayshall
 * <br/>
 */
public class MixinFactory {
	
	
	private MixinAdvisorStrategy advisorStrategy = new AnnotationMixinAdvisorStrategy();
	
	public boolean isMixinInterface(Class<?> interfaceClass){
		return advisorStrategy.isMixinInterface(interfaceClass);
	}
	
	public List<Class<?>> extractMixinInterfaces(Collection<Class<?>> interfaceClass){
		return interfaceClass.stream()
							.filter(this::isMixinInterface)
							.collect(Collectors.toList());
	}
	
	public List<Class<?>> extractNotMixinInterfaces(Collection<Class<?>> interfaceClass){
		return interfaceClass.stream()
							.filter(i->!isMixinInterface(i))
							.collect(Collectors.toList());
	}
	
	/***
	 * 根据指定的策略（MixinAdvisorStrategy），把mixin接口(mixinInterfaces)混入到target对象
	 * @author weishao zeng
	 * @param <T>
	 * @param target
	 * @param mixinInterfaces
	 * @return
	 */
	public <T> T of(Object target, Class<?>... mixinInterfaces){
		return Mixins.of(target, advisorStrategy, mixinInterfaces);
	}

	public void setAdvisorStrategy(MixinAdvisorStrategy advisorFactory) {
		this.advisorStrategy = advisorFactory;
	}
	
}
