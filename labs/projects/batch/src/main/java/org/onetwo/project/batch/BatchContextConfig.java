package org.onetwo.project.batch;

import org.onetwo.common.spring.config.JFishProfile;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@JFishProfile
public class BatchContextConfig {
	
	public BatchContextConfig(){
	}
	
	@Bean
	public BatchConfig timerConfig(){
		return BatchConfig.getInstance();
	}
	
}
