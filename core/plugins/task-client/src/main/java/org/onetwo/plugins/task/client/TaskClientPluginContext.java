package org.onetwo.plugins.task.client;

import org.onetwo.common.fish.plugin.AbstractPluginContext;
import org.onetwo.plugins.task.client.service.impl.TaskClientServiceImpl;
import org.onetwo.plugins.task.service.impl.TaskQueueServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class TaskClientPluginContext extends AbstractPluginContext {

	
	public TaskClientPluginContext() {
	}
	
	@Bean
	public TaskQueueServiceImpl taskQueueService(){
		return new TaskQueueServiceImpl();
	}
	
	@Bean
	public TaskClientServiceImpl taskClientService(){
		return new TaskClientServiceImpl();
	}
}
