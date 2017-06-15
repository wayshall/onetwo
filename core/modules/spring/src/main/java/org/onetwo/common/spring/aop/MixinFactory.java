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

	public <T> T of(Object obj, Class<?>... mixinInterfaces){
		return Mixins.of(obj, advisorStrategy, mixinInterfaces);
	}

	public void setAdvisorStrategy(MixinAdvisorStrategy advisorFactory) {
		this.advisorStrategy = advisorFactory;
	}
	
}
