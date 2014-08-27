package org.onetwo.plugins.task;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class TaskPluginContext {

	@Bean
	public TaskPluginConfig taskPluginConfig(){
		return TaskCorePlugin.getInstance().getConfig();
	}
}
