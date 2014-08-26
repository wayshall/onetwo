package org.onetwo.plugins.task.client;

import javax.annotation.Resource;

import org.springframework.context.annotation.Configuration;


@Configuration
public class TaskClientWebPluginContext {

	@Resource
	private TaskClientConfig taskClientConfig;
	
}
