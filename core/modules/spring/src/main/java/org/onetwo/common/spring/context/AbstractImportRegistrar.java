package org.onetwo.common.spring.context;

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.onetwo.common.log.JFishLoggerFactory;
import org.onetwo.common.spring.SpringUtils;
import org.onetwo.common.utils.LangUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionReaderUtils;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.util.StringUtils;

import net.jodah.typetools.TypeResolver;

/**
 * @author wayshall
 * <br/>
 */
abstract public class AbstractImportRegistrar<IMPORT, COMPONENT> implements ImportBeanDefinitionRegistrar, BeanClassLoaderAware, ResourceLoaderAware, EnvironmentAware {

	public static final String ATTRS_NAME = "name";
	
	protected Logger logger = JFishLoggerFactory.getLogger(this.getClass());
	
	protected ResourceLoader resourceLoader;
	protected ClassLoader classLoader;
	protected Environment environment;
	protected AnnotationMetadataHelper annotationMetadataHelper;

	private Class<? extends Annotation> importingAnnotationClass;
	private Class<? extends Annotation> componentAnnotationClass;
	
	
	@SuppressWarnings("unchecked")
	public AbstractImportRegistrar() {
		super();
		Class<? extends Annotation>[] paramClasses = (Class<?  extends Annotation>[])TypeResolver.resolveRawArguments(AbstractImportRegistrar.class, getClass());
		this.importingAnnotationClass = paramClasses[0];
		this.componentAnnotationClass = paramClasses[1];
	}

	public void setResourceLoader(ResourceLoader resourceLoader) {
		this.resourceLoader = resourceLoader;
	}

	@Override
	public void setBeanClassLoader(ClassLoader classLoader) {
		this.classLoader = classLoader;
	}
	
	/****
	 * EnableWechatClient
	 * 
	 * @author wayshall
	 * @return
	 */
	protected Class<? extends Annotation> getImportingAnnotationClass(){
		return importingAnnotationClass;
	}
	
	/****
	 * 
	 * @author wayshall
	 * @return
	 */
	protected Class<? extends Annotation> getComponentAnnotationClass(){
		return componentAnnotationClass;
	}
	
	public AnnotationMetadataHelper getAnnotationMetadataHelper(AnnotationMetadata importingClassMetadata){
		AnnotationMetadataHelper annotationMetadataHelper = this.annotationMetadataHelper;
		if(annotationMetadataHelper==null){
			annotationMetadataHelper = createAnnotationMetadataHelper(importingClassMetadata);
			this.afterCreateAnnotationMetadataHelper(annotationMetadataHelper);
			this.annotationMetadataHelper = annotationMetadataHelper;
		}
		return annotationMetadataHelper;
	}
	
	protected AnnotationMetadataHelper createAnnotationMetadataHelper(AnnotationMetadata importingClassMetadata) {
		AnnotationMetadataHelper annotationMetadataHelper = new AnnotationMetadataHelper(importingClassMetadata, getImportingAnnotationClass());
		annotationMetadataHelper.setResourceLoader(resourceLoader);
		annotationMetadataHelper.setClassLoader(classLoader);
		return annotationMetadataHelper;
	}
	protected void afterCreateAnnotationMetadataHelper(AnnotationMetadataHelper annotationMetadataHelper){
	}
	
	protected List<BeanDefinition> scanBeanDefinitions(AnnotationMetadata importingClassMetadata){
		return getAnnotationMetadataHelper(importingClassMetadata).scanBeanDefinitions(getComponentAnnotationClass());
	}

	@Override
	public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
		Class<? extends Annotation> componentAnnoClass = getComponentAnnotationClass();
		List<BeanDefinition> beandefList = scanBeanDefinitions(importingClassMetadata);
		beandefList.stream().filter(AnnotatedBeanDefinition.class::isInstance)
						.forEach(bd->{
							AnnotatedBeanDefinition beanDefinition = (AnnotatedBeanDefinition) bd;
							AnnotationMetadata annotationMetadata = beanDefinition.getMetadata();
							checkComponent(componentAnnoClass, annotationMetadata);

							AnnotationAttributes tagAttributes = SpringUtils.getAnnotationAttributes(annotationMetadata, componentAnnoClass);
							registerComponent(registry, annotationMetadata, tagAttributes);
						});
	}
	
	protected void checkComponent(Class<? extends Annotation> componentAnnoClass, AnnotationMetadata annotationMetadata) {
//		Assert.isTrue(annotationMetadata.isInterface(),
//				"@"+componentAnnoClass.getSimpleName()+" can only be specified on an interface");
	}
	

	/***
	 * 
	 * @author wayshall
	 * @return
	 */
	abstract protected BeanDefinitionBuilder createComponentFactoryBeanBuilder(AnnotationMetadata annotationMetadata, AnnotationAttributes attributes);

	protected void registerComponent(BeanDefinitionRegistry registry, AnnotationMetadata annotationMetadata, AnnotationAttributes tagAttributes) {
		String className = annotationMetadata.getClassName();
		String beanName = resolveName(tagAttributes, className);
		
		if (registry.containsBeanDefinition(beanName)) {
			if(logger.isInfoEnabled()){
				logger.info("api client has registed, ingored. beanName: {}, class: {}", beanName, className);
			}
			return ;
		} else {
			if(logger.isInfoEnabled()){
				logger.info("register api client beanName: {}, class: {}", beanName, className);
			}
		}
		
		BeanDefinitionBuilder definition = createComponentFactoryBeanBuilder(annotationMetadata, tagAttributes);
		
		String alias = beanName + "-" + getComponentAnnotationClass().getSimpleName();
		AbstractBeanDefinition beanDefinition = definition.getBeanDefinition();
		beanDefinition.setPrimary(true);
		BeanDefinitionHolder holder = new BeanDefinitionHolder(beanDefinition, beanName, new String[] { alias });
		BeanDefinitionReaderUtils.registerBeanDefinition(holder, registry);
	}
	

	final protected String resolveName(AnnotationAttributes attributes, String defName) {
		if (!attributes.containsKey(ATTRS_NAME)) {
			return defName;
		}
		String name = attributes.getString(ATTRS_NAME);
		if (!StringUtils.hasText(name)) {
			name = defName;
		}
		name = resolve(name);
		return name;
	}
	

	final protected String resolve(String value) {
		return SpringUtils.resolvePlaceholders(resourceLoader, value);
	}

	final protected String resolveAttributeAsString(AnnotationAttributes attributes, String attrName) {
		String value = attributes.getString(attrName);
		if (!StringUtils.hasText(value)) {
			return value;
		}
		return resolve(value);
	}

	final protected String[] resolveAttributeAsStringArray(AnnotationAttributes attributes, String attrName) {
		String[] values = attributes.getStringArray(attrName);
		if (LangUtils.isEmpty(values)) {
			return LangUtils.EMPTY_STRING_ARRAY;
		}
		return Stream.of(values).map(v -> resolve(v)).collect(Collectors.toList()).toArray(new String[0]);
	}

	@Override
	public void setEnvironment(Environment environment) {
		this.environment = environment;
	}
}
