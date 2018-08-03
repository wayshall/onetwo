package org.onetwo.boot.module.swagger;

import org.onetwo.boot.module.swagger.plugin.ModelFileParameterBuilderPlugin;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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
}
