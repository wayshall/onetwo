package org.onetwo.boot.ds;

import java.util.Map;

import javax.sql.DataSource;

import org.onetwo.boot.core.config.BootJFishConfig;
import org.onetwo.common.ds.DatasourceFactoryBean;
import org.onetwo.common.exception.BaseException;
import org.onetwo.common.log.JFishLoggerFactory;
import org.onetwo.common.spring.SpringUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionReaderUtils;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.boot.bind.RelaxedPropertyResolver;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotationMetadata;

/**
 * @author weishao zeng
 * <br/>
 */
public class DatasourceRegistrar implements EnvironmentAware, ImportBeanDefinitionRegistrar {
	
	private final Logger logger = JFishLoggerFactory.getLogger(getClass());
	
	/***
	 * jfish: 
	 * 		datasources: 
	 * 			{name}: 
	 * 				driverClassName=com.mysql.jdbc.Driver
					password=root
					url=jdbc\:mysql\://localhost\:3306/jormtest?useUnicode\=true&amp;characterEncoding\=UTF-8
					username=root
	 */
	private String datasourceConfigPrefix = BootJFishConfig.PREFIX + ".datasources.";
	
	private Environment environment;
	private AnnotationAttributes attributes;
//	private BeanDefinitionRegistry registry;

	@Override
	public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
		Class<?> annotationType = EnableConfiguratableDatasource.class;
		AnnotationAttributes attributes = SpringUtils.getAnnotationAttributes(importingClassMetadata, annotationType);
		if (attributes == null) {
			throw new IllegalArgumentException(String.format("@%s is not present on importing class '%s' as expected", 
												annotationType.getSimpleName(), 
												importingClassMetadata.getClassName()));
		}
		this.attributes = attributes;
		
		String[] dsnames = attributes.getStringArray("value");
		if (dsnames.length==0) {
			logger.info("datasource not found");
			return ;
		}
		RelaxedPropertyResolver resolver = new RelaxedPropertyResolver(environment);
		for (String dsname : dsnames) {
			BeanDefinitionHolder holder = createDatasourceBeanBuilder(registry, dsname, resolver);
			if (holder!=null) {
				BeanDefinitionReaderUtils.registerBeanDefinition(holder, registry);
				logger.info("register datasource: {}", dsname);
			} else {
				logger.info("ignore register datasource: {}", dsname);
			}
		}
		
	}
	
	protected BeanDefinitionHolder createDatasourceBeanBuilder(BeanDefinitionRegistry registry, String dsname, RelaxedPropertyResolver resolver) {
		String configPrefix = datasourceConfigPrefix + dsname + ".";
		Map<String, Object> configs = resolver.getSubProperties(configPrefix);
		if (configs.isEmpty()) {
			throw new BaseException("datasource config not fouond: " + dsname);
		}
		
		boolean primary = false;
		if (configs.containsKey("primary")) {
			primary = "true".equals(configs.remove("primary"));
		}
		

		boolean enabled = true;
		if (configs.containsKey("enabled")) {
			enabled = "true".equals(configs.remove("enabled"));
			logger.info("datasource[{}] enabled is : {}", dsname, enabled);
		}
		if (!enabled) {
			return null;
		}
		
//		String className = annotationMetadata.getClassName();
		BeanDefinitionBuilder definition = BeanDefinitionBuilder.genericBeanDefinition(DatasourceFactoryBean.class);

		Class<? extends DataSource> implementClass = this.attributes.getClass("implementClass");
		if (implementClass==DataSource.class) {
			implementClass = null;
		}
		
//		definition.addPropertyValue("prefix", configPrefix);
		definition.addPropertyValue("prefix", "");
		definition.addPropertyValue("configMap", configs);
		definition.addPropertyValue("implementClass", implementClass);
		

		AbstractBeanDefinition beanDefinition = definition.getBeanDefinition();
		beanDefinition.setPrimary(primary);
		BeanDefinitionHolder holder = new BeanDefinitionHolder(beanDefinition, dsname);
		
		return holder;
	}
	
	@Override
	public void setEnvironment(Environment environment) {
		this.environment = environment;
	}
	
	
}
