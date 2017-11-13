package org.onetwo.common.spring.context;

import java.lang.annotation.Annotation;

import org.onetwo.common.exception.BaseException;
import org.onetwo.common.log.JFishLoggerFactory;
import org.onetwo.common.reflect.TypeResolver;
import org.onetwo.common.spring.SpringUtils;
import org.onetwo.common.utils.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.type.AnnotationMetadata;

/**
 * @IMPORTING_ANNOTATION
 * class Test {
 * }
 * @author wayshall
 * <br/>
 */
abstract public class BaseImportRegistrar<IMPORTING_ANNOTATION> implements ImportBeanDefinitionRegistrar, EnvironmentAware, ResourceLoaderAware {
	protected Logger logger = JFishLoggerFactory.getLogger(this.getClass());

	protected Environment environment; 
	protected ResourceLoader resourceLoader;
	protected Class<? extends Annotation> importingAnnotationClass;
	protected AnnotationMetadata importingClassMetadata;

	@SuppressWarnings("unchecked")
	public BaseImportRegistrar() {
		super();
		Class<? extends Annotation>[] paramClasses = (Class<?  extends Annotation>[])TypeResolver.resolveRawArguments(BaseImportRegistrar.class, getClass());
		this.importingAnnotationClass = paramClasses[0];
	}

	@Override
	public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
		this.importingClassMetadata = importingClassMetadata;
		
		AnnotationAttributes importingClassAttribute = getImportingClassAttribute(importingClassMetadata);
		doRegisterBeanDefinitions(importingClassAttribute, registry);
	}

	abstract public void doRegisterBeanDefinitions(AnnotationAttributes importingClassAttribute, BeanDefinitionRegistry registry);
	
	protected AnnotationAttributes getImportingClassAttribute(AnnotationMetadata importingClassMetadata){
		AnnotationAttributes attributes = SpringUtils.getAnnotationAttributes(importingClassMetadata, importingAnnotationClass);
		if (attributes == null) {
			throw new IllegalArgumentException(String.format("@%s is not present on importing class '%s' as expected", importingAnnotationClass.getSimpleName(), importingClassMetadata.getClassName()));
		}
		return attributes;
	}


	final protected String resolveAttribute(AnnotationAttributes attributes, String attribute, String def) {
		String value = attributes.getString(attribute);
		if (StringUtils.isBlank(value)) {
			if(def==null){
				throw new BaseException("attribute value not found and no default value is specify. attribute: " + attribute);
			}else{
				value = def;
			}
		}
		value = resolve(value);
		return value;
	}
	

	final protected String resolve(String value) {
		return SpringUtils.resolvePlaceholders(environment, value);
	}

	@Override
	public void setEnvironment(Environment environment) {
		this.environment = environment;
	}


	@Override
	public void setResourceLoader(ResourceLoader resourceLoader) {
		this.resourceLoader = resourceLoader;
	}
	
}
