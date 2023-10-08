package org.onetwo.boot.core;

import org.onetwo.boot.core.config.BootJFishConfig;
import org.onetwo.boot.core.json.BootJackson2ObjectMapperBuilder;
import org.onetwo.boot.core.json.ObjectMapperProvider;
import org.onetwo.boot.core.json.ObjectMapperProvider.DefaultObjectMapperProvider;
import org.onetwo.boot.core.web.view.BootJsonView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration
public class JsonConfiguration {
	@Autowired
	protected BootJFishConfig bootJfishConfig;
	@Bean
	public BootJsonView bootJsonView(){
		BootJsonView jv = new BootJsonView();
		jv.setPrettyPrint(bootJfishConfig.getMvc().getJson().isPrettyPrint());
		return jv;
	}
	
	@Bean
	@ConditionalOnMissingBean(ObjectMapperProvider.class)
	public ObjectMapperProvider objectMapperProvider(){
		return new DefaultObjectMapperProvider();
	}

	@Primary
	@Bean
	@ConditionalOnMissingBean(ObjectMapper.class)
	public ObjectMapper objectMapper(){
		return objectMapperProvider().createObjectMapper();
	}
	
	@Bean
	public BootJackson2ObjectMapperBuilder bootJackson2ObjectMapperBuilder(){
		return new BootJackson2ObjectMapperBuilder();
	}
}
