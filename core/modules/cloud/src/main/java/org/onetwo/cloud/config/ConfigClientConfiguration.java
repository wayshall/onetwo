package org.onetwo.cloud.config;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.onetwo.boot.core.web.view.ExtJackson2HttpMessageConverter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.config.client.ConfigClientProperties;
import org.springframework.cloud.config.client.ConfigServicePropertySourceLocator;
import org.springframework.cloud.config.client.ConfigServicePropertySourceLocator.GenericRequestHeaderInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.util.Base64Utils;
import org.springframework.web.client.RestTemplate;

/**
 * @author wayshall
 * <br/>
 */
@Configuration
@ConditionalOnClass(ConfigClientProperties.class)
public class ConfigClientConfiguration {

//	@ConditionalOnMissingBean(ConfigServicePropertySourceLocator.class)
	@Bean
	@ConditionalOnProperty(value = "spring.cloud.config.enabled", matchIfMissing = true)
	public ConfigServicePropertySourceLocator configServicePropertySource(ConfigClientProperties properties) {
		ConfigServicePropertySourceLocator locator = new ConfigServicePropertySourceLocator(properties);
		RestTemplate restTemplate = getSecureRestTemplate(properties);
		restTemplate.setMessageConverters(Arrays.asList(new ExtJackson2HttpMessageConverter()));
		locator.setRestTemplate(restTemplate);
		return locator;
	}

	/****
	 * copy form ConfigServicePropertySourceLocator#getSecureRestTemplate
	 * @author weishao zeng
	 * @param client
	 * @return
	 */
	private RestTemplate getSecureRestTemplate(ConfigClientProperties client) {
		SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
		requestFactory.setReadTimeout((60 * 1000 * 3) + 5000); //TODO 3m5s, make configurable?
		RestTemplate template = new RestTemplate(requestFactory);
		String username = client.getUsername();
		String password = client.getPassword();
		String authorization = client.getAuthorization();
		Map<String, String> headers = new HashMap<>(client.getHeaders());

		if (password != null && authorization != null) {
			throw new IllegalStateException(
					"You must set either 'password' or 'authorization'");
		}

		if (password != null) {
			byte[] token = Base64Utils.encode((username + ":" + password).getBytes());
			headers.put("Authorization", "Basic " + new String(token));
		}
		else if (authorization != null) {
			headers.put("Authorization", authorization);
		}

		if (!headers.isEmpty()) {
			template.setInterceptors(Arrays.<ClientHttpRequestInterceptor> asList(
					new GenericRequestHeaderInterceptor(headers)));
		}

		return template;
	}
}
