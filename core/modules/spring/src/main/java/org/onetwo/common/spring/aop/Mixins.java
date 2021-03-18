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
	 * mixin mixinInterfaces to target object
	 * 混入接口的实现类寻找策略可由MixinAdvisorStrategy指定
	 * target和mixinInterfaces实际上没有关系，但mixin后，target可强转为mixinInterfaces，
	 * 调用mixinInterfaces的接口方法时，将会调用mixinInterfaces的实际实现类对应方法:
	 * SimpleObject obj = new SimpleObject();
	 * obj = Mixins.of(obj, Human.class);
		String talkWord = ((Human)obj).talk();//invoke HumanImpl.talk
	 * @author wayshall
	 * @param target
	 * @param mixinInterfaces
	 * @return
	 */
	public static <T> T of(Object target, Class<?>... mixinInterfaces){
		return DEFAULT_MIXIN_FACTORY.of(target, mixinInterfaces);
	}
	
	/***
	 * 根据指定策略（MixinAdvisorStrategy）查找mixin接口（mixinInterfaces）的实现，并创建Advisor，
	 * 为target对象创建一个代理工厂（ProxyFactory），并把mixin接口的Advisor，添加到ProxyFactory，即把mixin接口混入到target对象。
	 * 
	 * target和mixinInterfaces实际上没有关系，但mixin后，target可强转为mixinInterfaces，
	 * 
	 *  mixin mixinInterfaces to target object
	 * 混入接口的实现类寻找策略可由MixinAdvisorStrategy指定
	 * target和mixinInterfaces实际上没有关系，但mixin后，target可强转为mixinInterfaces，
	 * 调用mixinInterfaces的接口方法时，将会调用mixinInterfaces的实际实现类对应方法:
	 * SimpleObject obj = new SimpleObject();
	 * obj = Mixins.of(obj, Human.class);
		String talkWord = ((Human)obj).talk();//invoke HumanImpl.talk
	 * @author wayshall
	 * @param target
	 * @param factory
	 * @param mixinInterfaces
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> T of(Object target, MixinAdvisorStrategy factory, Class<?>... mixinInterfaces){
		List<Advisor> advisors = Stream.of(mixinInterfaces)
										.filter(factory::isMixinInterface)
										.map(factory::createAdvisor)
										.collect(Collectors.toList());
		ProxyFactory proxy = new ProxyFactory(target);
		proxy.addAdvisors(advisors);
		proxy.setOptimize(true);
		T proxyObject = (T)proxy.getProxy();
		return proxyObject;
	}
	
}
