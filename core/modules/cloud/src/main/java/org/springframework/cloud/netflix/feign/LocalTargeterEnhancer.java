package org.springframework.cloud.netflix.feign;

import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Stream;

import org.onetwo.cloud.feign.EnhanceFeignClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.cloud.netflix.feign.ExtTargeter.FeignTargetContext;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.transaction.PlatformTransactionManager;

import lombok.extern.slf4j.Slf4j;

/**
 * @author weishao zeng
 * <br/>
 */
@Slf4j
public class LocalTargeterEnhancer implements TargeterEnhancer {
	@Autowired
	private ApplicationContext appContext;
	@Autowired
	private PlatformTransactionManager transactionManager;

	public <T> T enhanceTargeter(FeignTargetContext<T> ctx) {
		return enhanceTargeter0(appContext, ctx.getFeignClientfactory(), ()->{
			return (T)enhanceTargeter0(appContext.getParent(), ctx.getFeignClientfactory(), ()->{
				return ctx.createTargeter();
			});
		});
	}
	
	@SuppressWarnings("unchecked")
	public <T> T enhanceTargeter0(ApplicationContext appContext, FeignClientFactoryBean factory, Supplier<T> defaultTarget) {
		BeanDefinitionRegistry bdr = (BeanDefinitionRegistry) appContext;
		
		Class<?> fallbackType = factory.getFallback();
		Class<T> clientInterface = (Class<T>)factory.getType();
//		EnhanceFeignClient enhanceAnno = clientInterface.getAnnotation(EnhanceFeignClient.class);
		EnhanceFeignClient enhanceAnno = AnnotatedElementUtils.findMergedAnnotation(clientInterface, EnhanceFeignClient.class);
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
		//排除fallback和FeignClientFactoryBean后，取第一个bean
		Optional<String> localBeanNameOpt = Stream.of(typeBeanNames).filter(lbn->{
			BeanDefinition bd = bdr.getBeanDefinition(lbn);
			return !bd.getBeanClassName().equals(fallbackType.getName()) && !bd.getBeanClassName().equals(FeignClientFactoryBean.class.getName());
		})
		.findFirst();
		
		if(!localBeanNameOpt.isPresent()){
			if(log.isInfoEnabled()){
				log.info("local implement not found for feign interface: {}, use default target.", apiInterface);
			}
			return defaultTarget.get();
		}

//		T localProxy = (T)Proxys.interceptInterface(clientInterface, new SpringBeanMethodInterceptor(appContext, localBeanNameOpt.get()));
		LocalFeignDelegateBean<T> localProxy = new LocalFeignDelegateBean<T>(appContext, clientInterface, localBeanNameOpt.get());
		if(log.isInfoEnabled()){
			log.info("local implement has been found for feign interface: {}, use local bean: {}", apiInterface, localBeanNameOpt.get());
		}
		return localProxy.createProxy();
	}
}

