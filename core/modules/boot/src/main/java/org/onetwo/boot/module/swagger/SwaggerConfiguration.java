package org.onetwo.boot.module.swagger;

import java.util.List;

import org.onetwo.boot.module.swagger.json.CustomBasePathSerializer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
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
@EnableConfigurationProperties(SwaggerProperties.class)
public class SwaggerConfiguration {
//	@Bean
//	@ConditionalOnBean(Swagger2DocumentationConfiguration.class)
//	public ModelFileParameterBuilderPlugin modelFileParameterBuilderPlugin() {
//		return new ModelFileParameterBuilderPlugin();
//	}
	
	@Bean
	@Primary
	public CustomBasePathSerializer customBasePathSerializer(List<JacksonModuleRegistrar> modules) {
		return new CustomBasePathSerializer(modules);
	}
}
