package org.onetwo.common.spring.aop;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.aopalliance.intercept.MethodInterceptor;

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
	final protected List<Class<?>> mixinInterfaces = new ArrayList<Class<?>>();
	final protected List<Class<?>> proxyInterfaces = new ArrayList<Class<?>>();
	
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
		for(Class<?> inter : this.proxyInterfaces){
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
