package org.onetwo.plugins.zkclient;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ZkclientContext {
	
	@Bean
	public ZkclientPluginConfig zkclientPluginConfig(){
		return ZkclientPlugin.getInstance().getConfig();
	}
	
}
