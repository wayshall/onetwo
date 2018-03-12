package org.onetwo.cloud.feign.local;

import java.util.Iterator;
import java.util.Optional;
import java.util.stream.Stream;

import org.apache.commons.lang3.ArrayUtils;
import org.onetwo.cloud.feign.EnhanceFeignClient;
import org.onetwo.common.reflect.ReflectUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.PropertyValue;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;

/**
 * @author wayshall
 * <br/>
 */
public class LocalFeignBeanDefinitionRegistryPostProcessor implements BeanDefinitionRegistryPostProcessor {

	@Override
	public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
//		modifyFeignBeanFacotries(beanFactory);
	}

	@Override
	public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
		ConfigurableListableBeanFactory beanFactory = (ConfigurableListableBeanFactory) registry;
		modifyFeignBeanFacotries(beanFactory);
	}
	
	protected void modifyFeignBeanFacotries(ConfigurableListableBeanFactory beanFactory){
		ListableBeanFactory bf = (ListableBeanFactory)beanFactory.getParentBeanFactory();
		String[] feignBeanNames = bf.getBeanNamesForAnnotation(EnhanceFeignClient.class);
		
		BeanDefinitionRegistry bdr = (BeanDefinitionRegistry)bf;
		Stream.of(feignBeanNames).forEach(beanName->{
			BeanDefinition feignBeanDefinition = bdr.getBeanDefinition(feignBeanNames[0]);
			String typeName = (String)feignBeanDefinition.getPropertyValues().getPropertyValue("type").getValue();
			Class<?> fallbackType = (Class<?>)feignBeanDefinition.getPropertyValues().getPropertyValue("fallback").getValue();
			Class<?> clientInterface = ReflectUtils.loadClass(typeName);
			Class<?> apiInterface = clientInterface.getInterfaces()[0];
			String[] typeBeanNames = bf.getBeanNamesForType(apiInterface);//maybe: feignclient, fallback, controller
			if(typeBeanNames.length<=1){
				return ;
			}
			String[] localBeanNames = ArrayUtils.removeElement(typeBeanNames, beanName);//remove
			Optional<String> localBeanNameOpt = Stream.of(localBeanNames).filter(lbn->{
				BeanDefinition bd = bdr.getBeanDefinition(lbn);
				return !bd.getBeanClassName().equals(fallbackType.getName());
			})
			.findFirst();
			
			if(!localBeanNameOpt.isPresent()){
				return ;
			}

			MutablePropertyValues mpvs = feignBeanDefinition.getPropertyValues();
			this.clearMutablePropertyValues(mpvs);
			feignBeanDefinition.setBeanClassName(LocalFeignInvokerFactoryBean.class.getName());
			mpvs.addPropertyValue("remoteInterface", clientInterface);
			mpvs.addPropertyValue("localBeanName", localBeanNameOpt.get());
			System.out.println("typeBeanNames: " + typeBeanNames);
		});
	}
	
	final protected void clearMutablePropertyValues(MutablePropertyValues mpvs){
		Iterator<PropertyValue> it = mpvs.getPropertyValueList().iterator();
		while(it.hasNext()){
			it.next();
			it.remove();
		}
	}
	

}
