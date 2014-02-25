package org.onetwo.plugins.groovy.model;

import javax.annotation.Resource;

import org.onetwo.common.spring.SpringUtils;
import org.onetwo.common.utils.propconf.AppConfig;
import org.onetwo.plugins.groovy.GroovyPluginConfig;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scripting.support.ScriptFactoryPostProcessor;


@Configuration
//@ComponentScan(basePackageClasses=PluginModelContext.class)
public class PluginModelContext {

	@Resource
	private AppConfig appConfig;
	
	@Bean
	public GroovyPluginConfig groovyPluginConfig(){
		GroovyPluginConfig config = new GroovyPluginConfig();
		return config;
	}
	
	@Bean
	public PropertiesFactoryBean groovyPluginProperties() {
		String envLocation = "/groovy/groovyconfig-" + appConfig.getAppEnvironment() + ".properties";
		return SpringUtils.createPropertiesBySptring("/groovy/groovyconfig.properties", envLocation);
	}

	@Bean
	public ScriptFactoryPostProcessor scriptFactoryPostProcessor(){
		return new ScriptFactoryPostProcessor();
	}
}
