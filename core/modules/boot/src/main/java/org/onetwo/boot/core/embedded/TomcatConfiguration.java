package org.onetwo.boot.core.embedded;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author weishao zeng
 * <br/>
 */

@EnableConfigurationProperties({TomcatProperties.class})
@Configuration
@ConditionalOnProperty(value=TomcatProperties.ENABLED_CUSTOMIZER_TOMCAT, matchIfMissing=true, havingValue="true")
public class TomcatConfiguration {

	@Bean
	public BootServletContainerCustomizer bootServletContainerCustomizer(){
		return new BootServletContainerCustomizer();
	}
	
}
