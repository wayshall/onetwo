package org.onetwo.boot.module.swagger;

import org.onetwo.boot.module.swagger.controller.ExtSwagger2Controller;
import org.onetwo.boot.module.swagger.service.impl.DatabaseSwaggerResourceService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.web.servlet.HandlerMapping;

import springfox.documentation.service.Documentation;
import springfox.documentation.spring.web.DocumentationCache;
import springfox.documentation.spring.web.PropertySourcedRequestMappingHandlerMapping;
import springfox.documentation.spring.web.json.JsonSerializer;
import springfox.documentation.swagger2.mappers.ServiceModelToSwagger2Mapper;
import springfox.documentation.swagger2.web.Swagger2Controller;

/**
 * @author wayshall <br/>
 */
@Configuration
@ConditionalOnProperty(name="jfish.swagger.dbstore", havingValue="true")
@ConditionalOnClass(Documentation.class)
@ComponentScan(basePackageClasses=DatabaseSwaggerResourceService.class)
public class SwaggerConfiguration {
	/*@Autowired
	DatabaseSwaggerResourceService databaseSwaggerResourceService;*/
	@Bean
	public HandlerMapping swagger2ControllerMapping(Environment environment, Swagger2Controller controller) {
		return new PropertySourcedRequestMappingHandlerMapping(environment, controller);
	}
	
	@Bean
	public Swagger2Controller swagger2Controller(Environment environment,
			DocumentationCache documentationCache,
			ServiceModelToSwagger2Mapper mapper, JsonSerializer jsonSerializer) {
		return new ExtSwagger2Controller(environment, documentationCache, mapper,
				jsonSerializer);
	}
	
	@Bean
    public DocumentationCache resourceGroupCache(){
    	return new DatabaseDocumentationCache();
    }
}
