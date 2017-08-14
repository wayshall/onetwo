package org.onetwo.common.spring.aop;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.aop.Advisor;
import org.springframework.aop.framework.ProxyFactory;

/**
 * @author wayshall
 * <br/>
 */
public class Mixins {
	
	final private static MixinFactory DEFAULT_MIXIN_FACTORY = new MixinFactory();
	
	public static boolean isMixinInterface(Class<?> interfaceClass){
		return DEFAULT_MIXIN_FACTORY.isMixinInterface(interfaceClass);
	}
	
	public static List<Class<?>> extractMixinInterfaces(Collection<Class<?>> interfaceClass){
		return DEFAULT_MIXIN_FACTORY.extractMixinInterfaces(interfaceClass);
	}
	
	public static List<Class<?>> extractNotMixinInterfaces(Collection<Class<?>> interfaceClass){
		return DEFAULT_MIXIN_FACTORY.extractNotMixinInterfaces(interfaceClass);
	}

	/****
	 * mixin mixinInterfaces ot target object
	 * 混入接口的实现类寻找策略可由MixinAdvisorStrategy指定
	 * @author wayshall
	 * @param obj
	 * @param mixinInterfaces
	 * @return
	 */
	public static <T> T of(Object obj, Class<?>... mixinInterfaces){
		return DEFAULT_MIXIN_FACTORY.of(obj, mixinInterfaces);
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T of(Object obj, MixinAdvisorStrategy factory, Class<?>... mixinInterfaces){
		List<Advisor> advisors = Stream.of(mixinInterfaces)
										.filter(factory::isMixinInterface)
										.map(factory::createAdvisor)
										.collect(Collectors.toList());
		ProxyFactory proxy = new ProxyFactory(obj);
		proxy.addAdvisors(advisors);
		proxy.setOptimize(true);
		obj = proxy.getProxy();
		return (T)obj;
	}
	
}
