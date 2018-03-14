package org.springframework.cloud.netflix.feign;

import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Stream;

import org.onetwo.cloud.feign.EnhanceFeignClient;
import org.onetwo.common.spring.aop.Proxys;
import org.onetwo.common.spring.aop.Proxys.SpringBeanMethodInterceptor;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import feign.Feign;
import feign.Target;


/**
 * @author wayshall
 * <br/>
 */
public class LocalTargeter implements Targeter, ApplicationContextAware {

	private ApplicationContext applicationContext;
	private HystrixTargeter hystrixTargeter = new HystrixTargeter();
	
	@Override
	public <T> T target(FeignClientFactoryBean factory, Feign.Builder feign, FeignContext context, Target.HardCodedTarget<T> target) {
		return getTarget(applicationContext, factory, ()->{
			return (T)getTarget(applicationContext.getParent(), factory, ()->{
				return hystrixTargeter.target(factory, feign, context, target);
			});
		});
		/*return getTarget(applicationContext.getParent(), factory, ()->{
			return hystrixTargeter.target(factory, feign, context, target);
		});*/
	}
	
	@SuppressWarnings("unchecked")
	private <T> T getTarget(ApplicationContext appContext, FeignClientFactoryBean factory, Supplier<T> defaultTarget) {
		BeanDefinitionRegistry bdr = (BeanDefinitionRegistry) appContext;
		
		Class<?> fallbackType = factory.getFallback();
		Class<?> clientInterface = factory.getType();
		EnhanceFeignClient enhanceAnno = clientInterface.getAnnotation(EnhanceFeignClient.class);
		if(enhanceAnno==null || enhanceAnno.local()==void.class){
			return defaultTarget.get();
		}
//		Class<?> apiInterface = enhanceAnno.local()==void.class?clientInterface.getInterfaces()[0]:enhanceAnno.local();//parent interface
		Class<?> apiInterface = enhanceAnno.local();
		String[] typeBeanNames = appContext.getBeanNamesForType(apiInterface);//maybe: feignclient, fallback, controller
//		ApplicationContext app = Springs.getInstance().getAppContext();
		if(typeBeanNames.length<=1){
			return defaultTarget.get();
		}
		//排除fallback和自己后，取第一个bean
		Optional<String> localBeanNameOpt = Stream.of(typeBeanNames).filter(lbn->{
			BeanDefinition bd = bdr.getBeanDefinition(lbn);
			return !bd.getBeanClassName().equals(fallbackType.getName()) && !bd.getBeanClassName().equals(FeignClientFactoryBean.class.getName());
		})
		.findFirst();
		
		if(!localBeanNameOpt.isPresent()){
			return defaultTarget.get();
		}

		T localProxy = (T)Proxys.interceptInterface(clientInterface, new SpringBeanMethodInterceptor(appContext, localBeanNameOpt.get()));
		return localProxy;
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

}
