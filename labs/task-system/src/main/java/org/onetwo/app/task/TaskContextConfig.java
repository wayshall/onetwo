package org.onetwo.app.task;

import org.onetwo.common.spring.config.JFishProfile;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@JFishProfile
public class TaskContextConfig {
	
	@Bean
	public TaskConfig taskConfig(){
		return TaskConfig.getInstance();
	}
}
