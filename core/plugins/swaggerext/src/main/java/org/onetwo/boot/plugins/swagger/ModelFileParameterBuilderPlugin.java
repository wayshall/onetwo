package org.onetwo.boot.plugins.swagger;

import static springfox.documentation.swagger.schema.ApiModelProperties.findApiModePropertyAnnotation;
import io.swagger.annotations.ApiModelProperty;

import org.springframework.core.annotation.Order;
import org.springframework.web.multipart.MultipartFile;

import com.google.common.base.Optional;

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
		Optional<ApiModelProperty> apiModelPropertyOptional = findApiModePropertyAnnotation(context.getField().getRawMember());
		if (apiModelPropertyOptional.isPresent()) {
			if(MultipartFile.class.isAssignableFrom(context.getField().getType().getErasedType())){
				context.getParameterBuilder().parameterType("form");
			}
		}
	}

}
