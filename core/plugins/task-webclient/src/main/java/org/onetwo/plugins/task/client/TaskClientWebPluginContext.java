package org.onetwo.plugins.task.client;

import org.onetwo.plugins.task.client.web.TaskArchivedController;
import org.onetwo.plugins.task.client.web.TaskQueueController;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class TaskClientWebPluginContext {

	@Bean
	public TaskQueueController taskQueueController(){
		return new TaskQueueController();
	}
	
	@Bean
	public TaskArchivedController taskArchivedController(){
		return new TaskArchivedController();
	}
	
}
