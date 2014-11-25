package org.onetwo.plugins.task;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class TaskPluginContext {

	@Bean
	public TaskCoreConfig taskPluginConfig(){
		return TaskCorePlugin.getInstance().getConfig();
	}
}
