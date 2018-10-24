package org.onetwo.boot.support;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.AnnotationBeanNameGenerator;

/**
 * 修复不同的bff项目部署在一起时，自动生成的beanName冲突的问题 
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
