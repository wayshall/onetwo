package org.onetwo.boot.ueditor;

import org.onetwo.boot.ueditor.controller.UeditorController;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author weishao zeng
 * <br/>
 */
@Configuration
@EnableConfigurationProperties(UeditorProperties.class)
public class UeditorConfiguration {
	
	@Bean
	public UeditorController ueditorController() {
		return new UeditorController();
	}

}
