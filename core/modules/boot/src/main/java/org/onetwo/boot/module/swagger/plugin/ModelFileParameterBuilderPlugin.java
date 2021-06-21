package org.onetwo.boot.module.swagger.plugin;

import java.util.Optional;

import org.springframework.core.annotation.Order;
import org.springframework.web.multipart.MultipartFile;

import io.swagger.annotations.ApiModelProperty;
import springfox.documentation.service.ParameterType;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.ExpandedParameterBuilderPlugin;
import springfox.documentation.spi.service.contexts.ParameterExpansionContext;
import springfox.documentation.swagger.common.SwaggerPluginSupport;

/**
 * @author wayshall <br/>
 */
@Order(SwaggerPluginSupport.SWAGGER_PLUGIN_ORDER)
public class ModelFileParameterBuilderPlugin implements
		ExpandedParameterBuilderPlugin {

	@Override
	public boolean supports(DocumentationType delimiter) {
		return SwaggerPluginSupport.pluginDoesApply(delimiter);
	}

	@Override
	public void apply(ParameterExpansionContext context) {
//		Optional<ApiModelProperty> apiModelPropertyOptional = findApiModePropertyAnnotation(context.getField().getRawMember());
		Optional<ApiModelProperty> apiModelPropertyOptional = context.findAnnotation(ApiModelProperty.class);
		if (apiModelPropertyOptional.isPresent()) {
			if(MultipartFile.class.isAssignableFrom(context.getFieldType().getErasedType())){
				context.getRequestParameterBuilder().in(ParameterType.FORM);
			}
		}
	}

}
