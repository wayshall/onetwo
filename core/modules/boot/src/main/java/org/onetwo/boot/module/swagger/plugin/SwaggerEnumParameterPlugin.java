package org.onetwo.boot.module.swagger.plugin;


import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;
import org.onetwo.boot.module.swagger.annotation.SwaggerEnumConfig;
import org.onetwo.common.spring.SpringUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.annotation.Order;

import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.schema.ModelPropertyBuilderPlugin;
import springfox.documentation.spi.schema.contexts.ModelPropertyContext;
import springfox.documentation.swagger.common.SwaggerPluginSupport;

/**
 * @author weishao zeng
 * <br/>
 */
@Order(SwaggerPluginSupport.SWAGGER_PLUGIN_ORDER)
public class SwaggerEnumParameterPlugin implements ModelPropertyBuilderPlugin {

	@Override
	public boolean supports(DocumentationType delimiter) {
		return SwaggerPluginSupport.pluginDoesApply(delimiter);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void apply(ModelPropertyContext context) {
//        AnnotatedElement field = context.getAnnotatedElement().get();
//        Optional<ApiModelProperty> apiModelPropertyOpt = ApiModelProperties.findApiModePropertyAnnotation(field);
//        if (!apiModelPropertyOpt.isPresent()) {
//        	return ;
//        }
        //获取当前字段的类型
        final Class<?> fieldType = context.getBeanPropertyDefinition().get().getField().getRawType();
        if (!Enum.class.isAssignableFrom(fieldType)) {
        	return ;
        }
        Class<? extends Enum<?>> enumType = (Class<? extends Enum<?>>)fieldType;
        SwaggerEnumConfig annoConfig = AnnotationUtils.findAnnotation(enumType, SwaggerEnumConfig.class);
        final String valueField;
        String labelField;
        if (annoConfig!=null) {
        	valueField = annoConfig.valueProperty();
        	labelField = annoConfig.labelProperty();
        } else {
        	valueField = "name";
        	labelField = "label";
        }
        Enum<?>[] enumList = enumType.getEnumConstants();
        List<String> fieldDescList = Stream.of(enumList).map(e -> {
        	BeanWrapper bw = SpringUtils.newBeanWrapper(e);
        	String desc = resovleFieldDesc(bw, e, valueField);
        	desc = desc + ": " + resovleFieldDesc(bw, e, labelField);
        	return desc;
        }).collect(Collectors.toList());
        
//        final ResolvedType resolvedType = context.getResolver().resolve(fieldType);
        String desc = StringUtils.join(fieldDescList, ";");
        context.getSpecificationBuilder().description(desc);
	}
	
	private String resovleFieldDesc(BeanWrapper bw, Enum<?> enumValue, String name) {
		if (name.equals("name")) {
			return enumValue.name();
		} else if (name.equals("ordinal")) {
			return enumValue.ordinal() + "";
		} else {
			return (String)bw.getPropertyValue(name);
		}
	}

}
