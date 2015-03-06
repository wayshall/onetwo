package org.onetwo.plugins.task.client;

import org.onetwo.common.spring.plugin.AbstractPluginContext;
import org.onetwo.plugins.task.client.service.impl.TaskClientServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class TaskClientPluginContext extends AbstractPluginContext {

	
	public TaskClientPluginContext() {
	}
	
	
	@Bean
	public TaskClientServiceImpl taskClientService(){
		return new TaskClientServiceImpl();
	}
	
	@Bean
	public TaskClientConfig taskClientConfig(){
		return TaskClientPlugin.getInstance().getConfig();
	}
}
