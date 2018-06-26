package org.onetwo.common.spring.aop;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.aopalliance.intercept.MethodInterceptor;

import com.google.common.collect.Lists;

/**
 * @author wayshall
 * <br/>
 */
public class SpringMixinableInterfaceCreator implements MixinableInterfaceCreator {
	
	public static MixinableInterfaceCreator classNamePostfixMixin(Class<?>... proxiedInterfaces){
		return new SpringMixinableInterfaceCreator(new ClassNamePostfixMixinAdvisorStrategy(), proxiedInterfaces);
	}
	
	public static MixinableInterfaceCreator annotationMixin(Class<?>... proxiedInterfaces){
		return new SpringMixinableInterfaceCreator(new AnnotationMixinAdvisorStrategy(), proxiedInterfaces);
	}

	final private MixinFactory mixinFactory;
	/***
	 * mixin接口有自己的实现类，非动态代理
	 */
	final protected List<Class<?>> mixinInterfaces = new ArrayList<Class<?>>();
	/***
	 * 代理接口
	 */
	final protected List<Class<?>> proxyInterfaces = new ArrayList<>();
	
	public SpringMixinableInterfaceCreator(MixinAdvisorStrategy mixinAdvisorStrategy, Class<?>... proxiedInterfaces){
		this.proxyInterfaces.addAll(Arrays.asList(proxiedInterfaces));
		mixinFactory = new MixinFactory();
//		mixinFactory.setAdvisorFactory(new ClassNamePostfixMixinAdvisorFactory());
		mixinFactory.setAdvisorStrategy(mixinAdvisorStrategy);
		analyseProxyInterfaces();
	}
	
	@Override
	public Object createMixinObject(MethodInterceptor interceptor){
		Object mixinObject = null;
		mixinObject = Proxys.interceptInterfaces(proxyInterfaces, interceptor);
		
		if(!mixinInterfaces.isEmpty()){
			mixinObject = mixinFactory.of(mixinObject, mixinInterfaces.toArray(new Class<?>[0]));
		}
		return mixinObject;
	}
	
	private void analyseProxyInterfaces(){
		List<Class<?>> proxyInterfaces = Lists.newArrayList(this.proxyInterfaces);
		for(Class<?> inter : proxyInterfaces){
			analyseInterface(inter);
		}
	}
	private void analyseInterface(Class<?> interfaceClass){
		Class<?>[] interfaces = interfaceClass.getInterfaces();
		for(Class<?> inter : interfaces){
			if(mixinFactory.isMixinInterface(inter)){
				this.mixinInterfaces.add(inter);
			}else{
				this.proxyInterfaces.add(inter);
			}
			analyseInterface(inter);
		}
	}
	
	@Override
	final public void addMixinInterfaces(Class<?>...mixinInterfaces){
		for(Class<?> inter : mixinInterfaces){
			this.mixinInterfaces.add(inter);
		}
	}
}
