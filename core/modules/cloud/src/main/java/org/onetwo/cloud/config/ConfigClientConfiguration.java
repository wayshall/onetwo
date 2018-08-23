package org.onetwo.cloud.config;

import java.util.Arrays;

import org.onetwo.boot.core.web.view.ExtJackson2HttpMessageConverter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.config.client.ConfigClientProperties;
import org.springframework.cloud.config.client.ConfigServicePropertySourceLocator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * @author wayshall
 * <br/>
 */
@Configuration
public class ConfigClientConfiguration {

	@Bean
//	@ConditionalOnMissingBean(ConfigServicePropertySourceLocator.class)
	@ConditionalOnProperty(value = "spring.cloud.config.enabled", matchIfMissing = true)
	public ConfigServicePropertySourceLocator configServicePropertySource(ConfigClientProperties properties) {
		ConfigServicePropertySourceLocator locator = new ConfigServicePropertySourceLocator(properties);
		RestTemplate restTemplate = new RestTemplate();
		restTemplate.setMessageConverters(Arrays.asList(new ExtJackson2HttpMessageConverter()));
		locator.setRestTemplate(restTemplate);
		return locator;
	}
}
