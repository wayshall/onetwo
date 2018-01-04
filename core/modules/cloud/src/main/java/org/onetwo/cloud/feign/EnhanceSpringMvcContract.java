package org.onetwo.cloud.feign;

import static org.springframework.core.annotation.AnnotatedElementUtils.findMergedAnnotation;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Optional;

import lombok.extern.slf4j.Slf4j;

import org.onetwo.common.expr.ExpressionFacotry;
import org.onetwo.common.spring.SpringUtils;
import org.onetwo.common.utils.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.bind.RelaxedPropertyResolver;
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
public class EnhanceSpringMvcContract extends SpringMvcContract implements ApplicationContextAware, InitializingBean {
	private static final String FEIGN_BASE_PATH_TAG = ":";
	private static final String FEIGN_BASE_PATH_KEY = "jfish.cloud.feign.basePath.";

	private ApplicationContext applicationContext;
	private RelaxedPropertyResolver relaxedPropertyResolver;
	
	public EnhanceSpringMvcContract(List<AnnotatedParameterProcessor> annotatedParameterProcessors) {
		super(annotatedParameterProcessors);
	}
	
	public EnhanceSpringMvcContract(
			List<AnnotatedParameterProcessor> annotatedParameterProcessors,
			ConversionService conversionService) {
		super(annotatedParameterProcessors, conversionService);
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		relaxedPropertyResolver = new RelaxedPropertyResolver(applicationContext.getEnvironment());
	}

	@Override
	protected void processAnnotationOnClass(MethodMetadata data, Class<?> clz) {
		super.processAnnotationOnClass(data, clz);
		if (clz.isAnnotationPresent(FeignClient.class)) {
			EnhanceFeignClient classAnnotation = findMergedAnnotation(clz, EnhanceFeignClient.class);
			Optional<String> basePathOpt = getFeignBasePath(clz, classAnnotation);
			if(basePathOpt.isPresent()){
				data.template().insert(0, basePathOpt.get());
			}
		}
	}
	
	@SuppressWarnings("deprecation")
	private Optional<String> getFeignBasePath(Class<?> clz, EnhanceFeignClient classAnnotation){
		if (classAnnotation == null){
			return Optional.empty();
		}
		String pathValue = classAnnotation.basePath();
		if(StringUtils.isBlank(pathValue)){
			FeignClient feignClient = findMergedAnnotation(clz, FeignClient.class);
			String serviceName = StringUtils.isNotBlank(feignClient.name())?feignClient.name():feignClient.serviceId();
			serviceName = SpringUtils.resolvePlaceholders(applicationContext, serviceName);
			//不填，默认查找对应的配置 -> jfish.cloud.feign.basePath.serviceName
			pathValue = FEIGN_BASE_PATH_KEY + serviceName;
			pathValue = this.relaxedPropertyResolver.getProperty(pathValue);
		}else if(pathValue.startsWith(FEIGN_BASE_PATH_TAG)){
			//:serviceName -> jfish.cloud.feign.basePath.serviceName
			pathValue = FEIGN_BASE_PATH_KEY + pathValue.substring(1);
			pathValue = this.relaxedPropertyResolver.getProperty(pathValue);
		}else if(ExpressionFacotry.DOLOR.isExpresstion(pathValue)){
			//${basePath}
			pathValue = SpringUtils.resolvePlaceholders(applicationContext, pathValue);
		}
		if(StringUtils.isBlank(pathValue)){
			return Optional.empty();
		}
		if (!pathValue.startsWith("/")) {
			pathValue = "/" + pathValue;
		}
//			data.template().insert(0, pathValue);
		return Optional.ofNullable(pathValue);
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
