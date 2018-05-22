package org.onetwo.cloud.feign;

import static feign.Util.checkState;
import static feign.Util.emptyToNull;
import static org.springframework.core.annotation.AnnotatedElementUtils.findMergedAnnotation;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
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
import org.springframework.web.bind.annotation.RequestMapping;

import feign.MethodMetadata;
import feign.Util;

/**
 * @author wayshall
 * <br/>
 */
@Slf4j
public class EnhanceSpringMvcContract extends SpringMvcContract implements ApplicationContextAware, InitializingBean {
	private static final String FEIGN_BASE_PATH_TAG = ":";
	private static final String FEIGN_BASE_PATH_KEY = FeignProperties.PROPERTIES_PREFIX+".basePath.";

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
    public List<MethodMetadata> parseAndValidatateMetadata(Class<?> targetType) {
		try {
	    	return super.parseAndValidatateMetadata(targetType);
		} catch (IllegalStateException e) {
			
			if(e.getMessage().startsWith("Only single-level inheritance supported")){
				//去掉feign对接口继承层次的限制……
				checkState(targetType.getTypeParameters().length == 0, "Parameterized types unsupported: %s",
		                 targetType.getSimpleName());
		      checkState(targetType.getInterfaces().length <= 1, "Only single inheritance supported: %s",
		                 targetType.getSimpleName());
		      /*if (targetType.getInterfaces().length == 1) {
		        checkState(targetType.getInterfaces()[0].getInterfaces().length == 0,
		                   "Only single-level inheritance supported: %s",
		                   targetType.getSimpleName());
		      }*/
		      Map<String, MethodMetadata> result = new LinkedHashMap<String, MethodMetadata>();
		      for (Method method : targetType.getMethods()) {
		        if (method.getDeclaringClass() == Object.class ||
		            (method.getModifiers() & Modifier.STATIC) != 0 ||
		            Util.isDefault(method)) {
		          continue;
		        }
		        //fix bug:当使用泛型方法的时候，java编译器会在字节码里生成名字和参数类型相同的两个方法，
		        //其中一个非泛型的方法会被标记为bridge,而bridge方法是没有注解的元数据的导致下面的parseAndValidateMetadata解释时
		        //调用method.getAnnotations()返回空数组，从而导致出错
		        if(method.isBridge()){
		        	continue;
		        }
		        MethodMetadata metadata = parseAndValidateMetadata(targetType, method);
		        checkState(!result.containsKey(metadata.configKey()), "Overrides unsupported: %s",
		                   metadata.configKey());
		        result.put(metadata.configKey(), metadata);
		      }
		      return new ArrayList<MethodMetadata>(result.values());
			}
			throw e;
		}
    }
	
	@Override
	protected void processAnnotationOnClass(MethodMetadata data, Class<?> clz) {
		//父类限制了接口的继承层次，父类只检测了client接口和client父接口，且接口不能再继承任何接口，
		//导致三层继承的时候，相关的元数据注解解释不到，导致feign请求路径错误404
//		super.processAnnotationOnClass(data, clz);
		if (clz.getInterfaces().length == 0 || clz.isAnnotationPresent(EnhanceFeignClient.class)) {
			RequestMapping classAnnotation = findMergedAnnotation(clz, RequestMapping.class);
			if (classAnnotation != null) {
				// Prepend path from class annotation if specified
				if (classAnnotation.value().length > 0) {
					String pathValue = emptyToNull(classAnnotation.value()[0]);
					pathValue = SpringUtils.resolvePlaceholders(applicationContext, pathValue);
					if (!pathValue.startsWith("/")) {
						pathValue = "/" + pathValue;
					}
					data.template().insert(0, pathValue);
				}
			}
		}
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
