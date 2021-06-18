package org.onetwo.boot.module.swagger.json;

import java.util.List;

import org.onetwo.boot.module.swagger.SwaggerProperties;
import org.onetwo.common.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import io.swagger.models.Swagger;
import springfox.documentation.spring.web.json.JacksonModuleRegistrar;
import springfox.documentation.spring.web.json.Json;
import springfox.documentation.spring.web.json.JsonSerializer;

/**
 * @author weishao zeng
 * <br/>
 */
public class CustomBasePathSerializer extends JsonSerializer {

//	@Autowired
//	private Environment env;
	@Autowired
	private SwaggerProperties swaggerProperties;
	
	public CustomBasePathSerializer(List<JacksonModuleRegistrar> modules) {
		super(modules);
	}

		
	@Override
    public Json toJson(Object toSerialize) {
		if (StringUtils.isBlank(swaggerProperties.getBasePath())) {
	        return super.toJson(toSerialize);
		}
        if (toSerialize instanceof Swagger) {
            Swagger swagger = (Swagger) toSerialize;
        	String basePath = swaggerProperties.getBasePath();
            swagger.basePath(basePath);
        }
        return super.toJson(toSerialize);
    }


	
}
