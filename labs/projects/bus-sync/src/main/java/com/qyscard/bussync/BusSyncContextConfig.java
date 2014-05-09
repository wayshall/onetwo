package com.qyscard.bussync;

import org.onetwo.common.spring.config.JFishProfile;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@JFishProfile
public class BusSyncContextConfig {
	
	@Bean
	public TimerConfig timerConfig(){
		return TimerConfig.getInstance();
	}
}
