package org.onetwo.cloud.feign;

import static org.springframework.core.annotation.AnnotatedElementUtils.findMergedAnnotation;

import java.lang.reflect.Method;
import java.util.List;

import lombok.extern.slf4j.Slf4j;

import org.onetwo.common.spring.SpringUtils;
import org.onetwo.common.utils.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.cloud.netflix.feign.AnnotatedParameterProcessor;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.cloud.netflix.feign.support.SpringMvcContract;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.convert.ConversionService;

import feign.MethodMetadata;

/**
 * @author wayshall
 * <br/>
 */
@Slf4j
public class EnhanceSpringMvcContract extends SpringMvcContract implements ApplicationContextAware {

	private ApplicationContext applicationContext;
	
	public EnhanceSpringMvcContract(List<AnnotatedParameterProcessor> annotatedParameterProcessors) {
		super(annotatedParameterProcessors);
	}
	
	public EnhanceSpringMvcContract(
			List<AnnotatedParameterProcessor> annotatedParameterProcessors,
			ConversionService conversionService) {
		super(annotatedParameterProcessors, conversionService);
	}



	@Override
	protected void processAnnotationOnClass(MethodMetadata data, Class<?> clz) {
		super.processAnnotationOnClass(data, clz);
		if (clz.isAnnotationPresent(FeignClient.class)) {
			EnhanceFeignClient classAnnotation = findMergedAnnotation(clz, EnhanceFeignClient.class);
			if (classAnnotation != null && StringUtils.isNotBlank(classAnnotation.basePath())) {
				String pathValue = classAnnotation.basePath();
				pathValue = SpringUtils.resolvePlaceholders(applicationContext, pathValue);
				if (!pathValue.startsWith("/")) {
					pathValue = "/" + pathValue;
				}
				data.template().insert(0, pathValue);
			}
		}
	}

	@Override
	public MethodMetadata parseAndValidateMetadata(Class<?> targetType, Method method) {
		MethodMetadata data = super.parseAndValidateMetadata(targetType, method);
		if(log.isInfoEnabled()){
			log.info("feign client[{}] path: {}", targetType, data.template().url());
		}
		return data;
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}
	
}
