package org.onetwo.ext.ons.producer;

import lombok.extern.slf4j.Slf4j;

import org.onetwo.common.spring.SpringUtils;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.ext.ons.annotation.EnableONSClient;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionReaderUtils;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;

/**
 * @author wayshall
 * <br/>
 */
@Configuration
@Slf4j
public class ProducerRegistar implements ImportBeanDefinitionRegistrar {

	@Override
	public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
		AnnotationAttributes attributes = SpringUtils.getAnnotationAttributes(importingClassMetadata, EnableONSClient.class);
		if (attributes == null) {
			throw new IllegalArgumentException(String.format("@%s is not present on importing class '%s' as expected", EnableONSClient.class.getSimpleName(), importingClassMetadata.getClassName()));
		}
		AnnotationAttributes[] producers = attributes.getAnnotationArray("producers");
		if(LangUtils.isEmpty(producers)){
			return ;
		}
		for(AnnotationAttributes producer : producers){
			String producerId = producer.getString("producerId");
			Class<?> producerClass = producer.getBoolean("transactional")?ONSTransactionProducerServiceImpl.class:ONSProducerServiceImpl.class;
			
			BeanDefinitionBuilder definition = BeanDefinitionBuilder.genericBeanDefinition(producerClass);
			definition.addPropertyValue("producerId", producerId);
//			definition.setAutowireMode(AbstractBeanDefinition.AUTOWIRE_BY_TYPE);
			String beanName = "ONSProduers:"+producerId;
			BeanDefinitionHolder holder = new BeanDefinitionHolder(definition.getBeanDefinition(), beanName);
			BeanDefinitionReaderUtils.registerBeanDefinition(holder, registry);
			if(log.isInfoEnabled()){
				log.info("register producer {}: {}", producerClass.getSimpleName(), beanName);
			}
		}
	}
	
}
