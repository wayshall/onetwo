package org.onetwo.common.spring.aop;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author wayshall
 * <br/>
 */
public class MixinFactory {
	
	
	private MixinAdvisorFactory advisorFactory = new AnnotationMixinAdvisorFactory();
	
	public boolean isMixinInterface(Class<?> interfaceClass){
		return advisorFactory.isMixinInterface(interfaceClass);
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
		return Mixins.of(obj, advisorFactory, mixinInterfaces);
	}

	public void setAdvisorFactory(MixinAdvisorFactory advisorFactory) {
		this.advisorFactory = advisorFactory;
	}
	
}
