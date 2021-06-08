package org.springframework.cloud.openfeign;

import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Stream;

import org.onetwo.cloud.feign.EnhanceFeignClient;
import org.onetwo.cloud.feign.FeignProperties;
import org.onetwo.common.spring.aop.Proxys;
import org.onetwo.common.spring.aop.Proxys.SpringBeanMethodInterceptor;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.cloud.openfeign.FeignContext;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.util.ClassUtils;

import feign.Feign;
import feign.Target;
import lombok.extern.slf4j.Slf4j;


/**
 * 参考 HystrixFeignTargeterConfiguration 
 * 
 * @author wayshall
 * <br/>
 */
@Slf4j
public class LocalTargeter implements Targeter, ApplicationContextAware, InitializingBean {

	public final static String CLASS_HYSTRIX_FEIGN = "feign.hystrix.HystrixFeign";
	
	private ApplicationContext applicationContext;
	private Targeter defaultTargeter;
	@Autowired
	private FeignProperties feignProperties;
	
	@Override
	public void afterPropertiesSet() throws Exception {
		if (ClassUtils.isPresent(CLASS_HYSTRIX_FEIGN, ClassUtils.getDefaultClassLoader())) {
			defaultTargeter = new HystrixTargeter();
		} else {
			defaultTargeter = new DefaultTargeter();
		}
	}
	
	private <T> T defaultTargeter(FeignClientFactoryBean factory, Feign.Builder feign, FeignContext context, Target.HardCodedTarget<T> target) {
		/*String name = factory.getName();
		if (RequestUtils.isHttpPath(name)) {
			factory.setUrl(name);
			factory.setName("");
		}*/
		return defaultTargeter.target(factory, feign, context, target);
	}

	@Override
	public <T> T target(FeignClientFactoryBean factory, Feign.Builder feign, FeignContext context, Target.HardCodedTarget<T> target) {
		if (!feignProperties.getLocal().isEnabled()) {
			return defaultTargeter(factory, feign, context, target);
		}
		
		return getTarget(applicationContext, factory, ()->{
			return (T)getTarget(applicationContext.getParent(), factory, ()->{
				return defaultTargeter(factory, feign, context, target);
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

		T localProxy = (T)Proxys.interceptInterface(clientInterface, new SpringBeanMethodInterceptor(appContext, localBeanNameOpt.get()));
		if(log.isInfoEnabled()){
			log.info("local implement has been found for feign interface: {}, use local bean: {}", apiInterface, localBeanNameOpt.get());
		}
		return localProxy;
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

}
