package org.onetwo.boot.module.swagger;

import java.util.List;

import org.onetwo.boot.module.swagger.json.CustomSwaggerBasePathSerializer;
import org.onetwo.boot.module.swagger.plugin.ModelFileParameterBuilderPlugin;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import springfox.documentation.spring.web.json.JacksonModuleRegistrar;
import springfox.documentation.swagger2.configuration.Swagger2DocumentationConfiguration;

/**
 * @author wayshall <br/>
 */
@Configuration
@ConditionalOnClass(Swagger2DocumentationConfiguration.class)
public class SwaggerConfiguration {
	@Bean
	@ConditionalOnBean(Swagger2DocumentationConfiguration.class)
	public ModelFileParameterBuilderPlugin modelFileParameterBuilderPlugin() {
		return new ModelFileParameterBuilderPlugin();
	}

	@Bean
	@Primary
	public CustomSwaggerBasePathSerializer customBasePathSerializer(List<JacksonModuleRegistrar> modules) {
		return new CustomSwaggerBasePathSerializer(modules);
	}
}
