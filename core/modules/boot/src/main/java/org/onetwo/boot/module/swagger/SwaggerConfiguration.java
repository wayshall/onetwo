package org.onetwo.boot.module.swagger;

import org.onetwo.boot.module.swagger.controller.ExtSwagger2Controller;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.web.servlet.HandlerMapping;

import springfox.documentation.service.Documentation;
import springfox.documentation.spring.web.DocumentationCache;
import springfox.documentation.spring.web.PropertySourcedRequestMappingHandlerMapping;
import springfox.documentation.spring.web.json.JsonSerializer;
import springfox.documentation.swagger2.mappers.ServiceModelToSwagger2Mapper;

/**
 * @author wayshall <br/>
 */
@Configuration
@ConditionalOnProperty(name="jfish.swagger.dbstore", havingValue="true")
@ConditionalOnClass(Documentation.class)
public class SwaggerConfiguration {
	@Bean
	public HandlerMapping swagger2ControllerMapping(Environment environment,
			DocumentationCache documentationCache,
			ServiceModelToSwagger2Mapper mapper, JsonSerializer jsonSerializer) {
		return new PropertySourcedRequestMappingHandlerMapping(environment,
				new ExtSwagger2Controller(environment, documentationCache, mapper,
						jsonSerializer));
	}
}
