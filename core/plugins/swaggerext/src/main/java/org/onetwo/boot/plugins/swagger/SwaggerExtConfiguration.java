package org.onetwo.boot.plugins.swagger;

import java.util.Map;

import org.onetwo.boot.plugin.core.JFishWebPlugin;
import org.onetwo.boot.plugins.swagger.controller.ExtApiResourceController;
import org.onetwo.boot.plugins.swagger.controller.ExtSwagger2Controller;
import org.onetwo.boot.plugins.swagger.mapper.SwaggerModelMapper;
import org.onetwo.boot.plugins.swagger.service.impl.DatabaseSwaggerResourceService;
import org.onetwo.boot.plugins.swagger.util.DatabaseDocumentationCache;
import org.onetwo.common.reflect.ReflectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

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
@JFishWebPlugin(SwaggerExtPlugin.class)
public class SwaggerExtConfiguration {
	/*@Autowired
	DatabaseSwaggerResourceService databaseSwaggerResourceService;*/
	@Autowired(required=false)
	private WebMvcConfigurationSupport webMvcConfigurationSupport;
	
	@SuppressWarnings("unchecked")
	@Bean
	public HandlerMapping swagger2ControllerMapping(Environment environment, Swagger2Controller controller) {
		PropertySourcedRequestMappingHandlerMapping mapping = new PropertySourcedRequestMappingHandlerMapping(environment, controller);
		
		if (webMvcConfigurationSupport!=null) {
			Map<String, CorsConfiguration> corsConfigurations = (Map<String, CorsConfiguration>)ReflectUtils.invokeMethod("getCorsConfigurations", webMvcConfigurationSupport);
			mapping.setCorsConfigurations(corsConfigurations);
		}
		return mapping;
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
	
	@Bean
	public SwaggerModelMapper swaggerModelMapper(){
		return new SwaggerModelMapper();
	}
	
	@Bean
	public ExtApiResourceController ExtApiResourceController() {
		return new ExtApiResourceController();
	}
}
