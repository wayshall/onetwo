package org.onetwo.project.batch.test;

import org.onetwo.common.spring.config.JFishProfile;
import org.onetwo.project.batch.BatchConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

@Configuration
@JFishProfile
@ImportResource("classpath:test/applicationContext.xml")
public class TestBatchContextConfig {
	
	public TestBatchContextConfig(){
	}
	
	@Bean
	public BatchConfig timerConfig(){
		return BatchConfig.getInstance();
	}
	
}
