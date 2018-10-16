package org.onetwo.boot.support;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.AnnotationBeanNameGenerator;

/**
 * @author wayshall
 * <br/>
 */
public class BootBeanNameGenerator extends AnnotationBeanNameGenerator {

	protected String buildDefaultBeanName(BeanDefinition definition, BeanDefinitionRegistry registry) {
		String beanName = buildDefaultBeanName(definition);
		if(registry.containsBeanDefinition(beanName)){
			BeanDefinition bd = registry.getBeanDefinition(beanName);
			if(!bd.getBeanClassName().equals(definition.getBeanClassName())){
				beanName = definition.getBeanClassName();
			}
		}
		return beanName;
	}
}
