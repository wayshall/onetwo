package org.onetwo.plugins.groovy.model;

import javax.annotation.Resource;

import org.onetwo.common.spring.SpringUtils;
import org.onetwo.common.utils.propconf.AppConfig;
import org.onetwo.plugins.groovy.GroovyBeanFactory;
import org.onetwo.plugins.groovy.GroovyPluginConfig;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scripting.support.ScriptFactoryPostProcessor;


@Configuration
//@ComponentScan(basePackageClasses=PluginModelContext.class)
public class PluginModelContext {

	private static final String GROOVY_CONFIG_BASE = "/groovy/groovy-config";
	public static final String GROOVY_CONFIG_PATH = GROOVY_CONFIG_BASE + ".properties";
	
	@Resource
	private AppConfig appConfig;
	
	@Bean
	public GroovyPluginConfig groovyPluginConfig(){
		GroovyPluginConfig config = new GroovyPluginConfig();
		return config;
	}
	
	@Bean
	public PropertiesFactoryBean groovyPluginProperties() {
		String envLocation = GROOVY_CONFIG_BASE + "-" + appConfig.getAppEnvironment() + ".properties";
		return SpringUtils.createPropertiesBySptring(GROOVY_CONFIG_PATH, envLocation);
	}

	@Bean
	public ScriptFactoryPostProcessor scriptFactoryPostProcessor(){
		return new ScriptFactoryPostProcessor();
	}
	
	@Bean
	public GroovyBeanFactory groovyBeanFactory(){
		GroovyBeanFactory gbf =  new GroovyBeanFactory();
		GroovyPluginConfig config = groovyPluginConfig();
		gbf.setPackagesToScan(config.getModelPackage());
		return gbf;
	}
}
