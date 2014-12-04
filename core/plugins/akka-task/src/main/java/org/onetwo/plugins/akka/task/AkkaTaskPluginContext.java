package org.onetwo.plugins.akka.task;

import org.onetwo.plugins.akka.task.core.DefaultTaskListenerManager;
import org.onetwo.plugins.akka.task.core.DefaultTaskProcessor;
import org.onetwo.plugins.akka.task.core.TaskListenerManager;
import org.onetwo.plugins.akka.task.core.TaskProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class AkkaTaskPluginContext {
	
	@Bean
	public TaskProcessor taskProcessor(){
		return new DefaultTaskProcessor();
	}
	
	@Bean
	public TaskListenerManager taskListenerManager(){
		return new DefaultTaskListenerManager();
	}
}
