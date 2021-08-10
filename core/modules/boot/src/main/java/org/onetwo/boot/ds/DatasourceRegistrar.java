package org.onetwo.boot.ds;

import java.util.Map;

import org.onetwo.common.ds.DatasourceFactoryBean;
import org.onetwo.common.ds.TransactionManagerFactoryBean;
import org.onetwo.common.exception.BaseException;
import org.onetwo.common.log.JFishLoggerFactory;
import org.onetwo.common.utils.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionReaderUtils;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotationMetadata;

import com.google.common.collect.Maps;

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
	 * 				driverClassName: com.mysql.jdbc.Driver
					password: root
					url: jdbc\:mysql\://localhost\:3306/jormtest?useUnicode\=true&amp;characterEncoding\=UTF-8
					username: root
					type: org.apache.tomcat.jdbc.pool.DataSource
	 */
	private String datasourceConfigPrefix = ConfiguratableDatasourceConfiguration.DATASOURCE_CONFIG_PREFIX;
	
	private Environment environment;
//	private AnnotationAttributes attributes;
//	private BeanDefinitionRegistry registry;
	private String transactionManagerKey = "transactionManager";
	
	@SuppressWarnings("unchecked")
	@Override
	public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
//		RelaxedPropertyResolver resolver = new RelaxedPropertyResolver(environment);
//		String enabledDsnames = resolver.getProperty(ConfiguratableDatasourceConfiguration.ENABLED_DSNAMES, "");
		
		String enabledDsnames = environment.getProperty(ConfiguratableDatasourceConfiguration.ENABLED_DSNAMES, "");
		String[] dsnames = StringUtils.split(enabledDsnames, ",");
		if (dsnames.length==0) {
			logger.info("datasource not found");
			return ;
		}
		for (String dsname : dsnames) {
			String configPrefix = datasourceConfigPrefix + "." + dsname + ".";
			
//			Map<String, Object> configs = Maps.newHashMap(resolver.getSubProperties(configPrefix));
			Map<String, Object> configs = Binder.get(environment)
												.bind(configPrefix, Map.class)
												.orElse(Maps.newHashMap());
			
			String tmName = dsname + StringUtils.capitalize(transactionManagerKey);
			if (configs.containsKey(transactionManagerKey)) {
				tmName = (String)configs.remove(transactionManagerKey);
			}
			
			BeanDefinitionHolder holder = createDatasourceBeanBuilder(dsname, configs);
			if (holder!=null) {
				BeanDefinitionReaderUtils.registerBeanDefinition(holder, registry);
				
				BeanDefinitionHolder tmHolder = createDataSourceTransactionManagerBeanBuilder(tmName, dsname);
				BeanDefinitionReaderUtils.registerBeanDefinition(tmHolder, registry);
				
				logger.info("register datasource: {}, dataSourceTransactionManager: {}", dsname, tmName);
				
			} else {
				logger.info("ignore register datasource: {}", dsname);
			}
		}
		
	}
	
	protected BeanDefinitionHolder createDataSourceTransactionManagerBeanBuilder(String tmName, String dsname) {
		
		BeanDefinitionBuilder definition = BeanDefinitionBuilder.genericBeanDefinition(TransactionManagerFactoryBean.class);
//		definition.addPropertyReference("dataSource", dsname);
		definition.addConstructorArgReference(dsname);

		AbstractBeanDefinition beanDefinition = definition.getBeanDefinition();
		beanDefinition.setPrimary(false);
		BeanDefinitionHolder holder = new BeanDefinitionHolder(beanDefinition, tmName);
		
		return holder;
	}
	
	protected BeanDefinitionHolder createDatasourceBeanBuilder(String dsname, Map<String, Object> configs) {
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

//		Class<? extends DataSource> implementClass = this.attributes.getClass("implementClass");
		String implementClassName = (String)configs.remove("type");
		
//		definition.addPropertyValue("prefix", configPrefix);
		definition.addPropertyValue("prefix", "");
		definition.addPropertyValue("configMap", configs);
		
		if (StringUtils.isNotBlank(implementClassName)) {
			definition.addPropertyValue("implementClassName", implementClassName);
		}
		

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
