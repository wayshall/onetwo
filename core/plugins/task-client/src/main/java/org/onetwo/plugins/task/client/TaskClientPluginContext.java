package org.onetwo.plugins.task.client;

import java.util.Properties;

import javax.annotation.Resource;

import org.onetwo.common.fish.plugin.AbstractPluginContext;
import org.onetwo.common.spring.SpringUtils;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class TaskClientPluginContext extends AbstractPluginContext {

	@Resource
	private Properties taskClientPropertiesConfig;
	
	protected TaskClientPluginContext() {
		super("/task/", "client");
	}
	
	@Bean
	public PropertiesFactoryBean taskClientPropertiesConfig() {
		return SpringUtils.createPropertiesBySptring(getConfigPath(), getEnvConfigPath());
	}
	
	@Bean
	public TaskClientConfig taskClientConfig(){
		return new TaskClientConfig(taskClientPropertiesConfig);
	}
}
