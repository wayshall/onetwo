package org.onetwo.plugins.task;

import org.onetwo.plugins.task.service.impl.DefaultTaskListenerManager;
import org.onetwo.plugins.task.service.impl.DefaultTaskProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class TaskPluginContext {
	
	@Bean
	public DefaultTaskProcessor taskProcessor(){
		return new DefaultTaskProcessor();
	}
	
	@Bean
	public DefaultTaskListenerManager taskListenerManager(){
		return new DefaultTaskListenerManager();
	}
}
