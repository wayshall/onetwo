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
 * 参考下面问题实现：
 * https://stackoverflow.com/questions/36198827/how-to-change-basepath-for-springfox-swagger-2-0
 * @author weishao zeng
 * <br/>
 */
public class CustomSwaggerBasePathSerializer extends JsonSerializer {

//	@Autowired
//	private Environment env;
	@Autowired
	private SwaggerProperties swaggerProperties;
	
	public CustomSwaggerBasePathSerializer(List<JacksonModuleRegistrar> modules) {
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
