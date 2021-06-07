package org.onetwo.common.spring.resource;

import java.lang.annotation.Annotation;
import java.util.Set;

import org.onetwo.common.spring.SpringUtils;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.util.ClassUtils;

import com.google.common.collect.Sets;

abstract public class ImportingAnnotationComponentRegistar<T extends Annotation> implements ImportBeanDefinitionRegistrar {
	
	private Class<T> enableAnnotationClass;
	
	public ImportingAnnotationComponentRegistar(Class<T> enableAnnotationClass) {
		super();
		this.enableAnnotationClass = enableAnnotationClass;
	}


	@Override
	public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
		AnnotationAttributes attributes = SpringUtils.getAnnotationAttributes(importingClassMetadata, enableAnnotationClass);
		if (attributes == null) {
			throw new IllegalArgumentException(String.format("@%s is not present on importing class '%s' as expected", enableAnnotationClass.getSimpleName(), importingClassMetadata.getClassName()));
		}
		
		Set<String> packages = getComponentPackages(importingClassMetadata, attributes);
		this.doRegisterBeanDefinitions(importingClassMetadata, registry, packages);
		
	}
	
	abstract protected void doRegisterBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry, Set<String> packages);

	
	protected Set<String> getComponentPackages(AnnotationMetadata importingClassMetadata, AnnotationAttributes attributes) {
		boolean isSpecifyPackages = false;
		Set<String> packs = Sets.newHashSet();
		for(String p : attributes.getStringArray("value")){
			packs.add(p);
			isSpecifyPackages = true;
		}
		for(Class<?> p : attributes.getClassArray("basePackageClasses")){
			packs.add(p.getPackage().getName());
			isSpecifyPackages = true;
		}
		if(!isSpecifyPackages && importingClassMetadata.getClassName()!=null){
			packs.add(ClassUtils.getPackageName(importingClassMetadata.getClassName()));
		}
		return packs;
	}
}
