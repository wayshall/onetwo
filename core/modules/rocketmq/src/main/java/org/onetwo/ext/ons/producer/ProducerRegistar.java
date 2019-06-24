package org.onetwo.ext.ons.producer;

import org.onetwo.common.spring.context.BaseImportRegistrar;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.utils.StringUtils;
import org.onetwo.ext.ons.annotation.EnableONSClient;
import org.onetwo.ext.ons.producer.ONSTransactionProducerServiceImpl.FakeProducerService;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionReaderUtils;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.AnnotationAttributes;

/**
 * @author wayshall
 * <br/>
 */
@Configuration
public class ProducerRegistar extends BaseImportRegistrar<EnableONSClient> {

	@Override
	public void doRegisterBeanDefinitions(AnnotationAttributes attributes, BeanDefinitionRegistry registry) {
		AnnotationAttributes[] producers = attributes.getAnnotationArray("producers");
		if(LangUtils.isEmpty(producers)){
			return ;
		}
		for(AnnotationAttributes producer : producers){
			String producerId = resolveAttribute(producer, "producerId", null);
//			String beanName = "ONS-"+producerId;
			String beanName = StringUtils.appendStartWith(producerId, "ons-");
			
			if(registry.containsBeanDefinition(beanName)){
				logger.info("produer[{}] has been registered, ignored...", beanName);
				continue;
			}
			
			boolean transactional = producer.getBoolean("transactional");
			Class<?> producerClass = transactional?ONSTransactionProducerServiceImpl.class:ONSProducerServiceImpl.class;
			
			BeanDefinitionBuilder definition = BeanDefinitionBuilder.genericBeanDefinition(producerClass);
			definition.addPropertyValue("producerId", producerId);
//			definition.setAutowireMode(AbstractBeanDefinition.AUTOWIRE_BY_TYPE);
			BeanDefinitionHolder holder = new BeanDefinitionHolder(definition.getBeanDefinition(), beanName);
			BeanDefinitionReaderUtils.registerBeanDefinition(holder, registry);
			if(logger.isInfoEnabled()){
				logger.info("register producer {}: {}", producerClass.getSimpleName(), beanName);
			}
			
			if(transactional){
				BeanDefinitionBuilder noTransactionalBean = BeanDefinitionBuilder.genericBeanDefinition(FakeProducerService.class);
//				definition.setAutowireMode(AbstractBeanDefinition.AUTOWIRE_BY_TYPE);
				String fakeProducerService = beanName + "-fake";
				BeanDefinitionHolder notTransactionalHolder = new BeanDefinitionHolder(noTransactionalBean.getBeanDefinition(), fakeProducerService);
				BeanDefinitionReaderUtils.registerBeanDefinition(notTransactionalHolder, registry);
				if(logger.isInfoEnabled()){
					logger.info("register producer {}: {}", FakeProducerService.class.getSimpleName(), fakeProducerService);
				}
			}
		}
	}

}
