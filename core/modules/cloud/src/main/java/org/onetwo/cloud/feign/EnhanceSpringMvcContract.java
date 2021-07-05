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

import org.onetwo.cloud.feign.FeignProperties.ServiceProps;
import org.onetwo.common.expr.ExpressionFacotry;
import org.onetwo.common.spring.SpringUtils;
import org.onetwo.common.utils.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.openfeign.AnnotatedParameterProcessor;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.support.SpringMvcContract;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.convert.ConversionService;
import org.springframework.web.bind.annotation.RequestMapping;

import feign.MethodMetadata;
import feign.Util;
import lombok.extern.slf4j.Slf4j;

/**
 * @EnhanceFeignClient注解basePath查找逻辑：
 * 
 * @EnhanceFeignClient:
 * 查找：jfish.cloud.feign.basePath.serviceName
 * 如果找不到，则查找server.contextPath
 * 
 * @EnhanceFeignClient(basePath="${feign.serivceName}"):
 * 查找：feign.serviceName
 * 
 * @EnhanceFeignClient(basePath=":serivceName"):
 * 查找：jfish.cloud.feign.basePath.serviceName
 * 
 * @EnhanceFeignClient(basePath="/servicePath"):
 * 返回/servicePath
 * 
 * @author wayshall
 * <br/>
 */
@Slf4j
public class EnhanceSpringMvcContract extends SpringMvcContract implements ApplicationContextAware, InitializingBean {
//	private static final String FEIGN_BASE_PATH_TAG = ":";
//	private static final String FEIGN_BASE_PATH_KEY = FeignProperties.FEIGN_BASE_PATH_KEY;
//	private static final String FEIGN_CONTEXT_PATH_KEY = FeignProperties.FEIGN_CONTEXT_PATH_KEY;
	@Deprecated
	private static final String FEIGN_CONTEXT_PATH_KEY2 = "feignClient.basePaths.contextPath";

	private ApplicationContext applicationContext;
//	private RelaxedPropertyResolver relaxedPropertyResolver;
	@Autowired
	private FeignProperties feignProperties;
	
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
//		relaxedPropertyResolver = new RelaxedPropertyResolver(applicationContext.getEnvironment());
	}


	@Override
    public List<MethodMetadata> parseAndValidateMetadata(Class<?> targetType) {
		try {
	    	return super.parseAndValidateMetadata(targetType);
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
		        /*try {
			        metadata = parseAndValidateMetadata(targetType, method);
				} catch (Exception e2) {
					throw new BaseException("parse and validate meta error", e);
				}*/
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
		/*if(clz.getName().contains("ProductApiClient") || clz.getName().contains("ProductApi")){
			System.out.println("test");
		}*/
		if (clz.getInterfaces().length == 0 || 
				//避免feignClient同时存在EnhanceFeignClient和FeignClient注解时，@RequestMapping路径多解释了一次的问题
				(clz.isAnnotationPresent(EnhanceFeignClient.class) && !clz.isAnnotationPresent(FeignClient.class)) ) {
			RequestMapping classAnnotation = findMergedAnnotation(clz, RequestMapping.class);
			if (classAnnotation != null) {
				// Prepend path from class annotation if specified
				if (classAnnotation.value().length > 0) {
					String pathValue = emptyToNull(classAnnotation.value()[0]);
					pathValue = SpringUtils.resolvePlaceholders(applicationContext, pathValue);
					if (!pathValue.startsWith("/")) {
						pathValue = "/" + pathValue;
					}
					// insert is deprecated
//					data.template().insert(0, pathValue);
					data.template().uri(pathValue);
				}
			}
		}
		if (clz.isAnnotationPresent(FeignClient.class)) {
			EnhanceFeignClient classAnnotation = findMergedAnnotation(clz, EnhanceFeignClient.class);
			Optional<String> basePathOpt = getFeignBasePath(clz, classAnnotation);
			if(basePathOpt.isPresent()){
				// insert is deprecated
//				data.template().insert(0, basePathOpt.get());
				data.template().uri(basePathOpt.get());
			}
		}
	}
	
	@SuppressWarnings("deprecation")
	private Optional<String> getFeignBasePath(Class<?> clz, EnhanceFeignClient classAnnotation){
		if (classAnnotation == null){
			return Optional.empty();
		}
		/*if(clz.getName().contains("ProductApiClient") || clz.getName().contains("ProductApi")){
			System.out.println("test");
		}*/
		String pathValue = classAnnotation.basePath();
		if(StringUtils.isBlank(pathValue)){
			FeignClient feignClient = findMergedAnnotation(clz, FeignClient.class);
			String serviceName = StringUtils.isNotBlank(feignClient.name())?feignClient.name():feignClient.serviceId();
			serviceName = SpringUtils.resolvePlaceholders(applicationContext, serviceName);
			//不填，默认查找对应的配置 -> jfish.cloud.feign.basePath.serviceName
			/*pathValue = FEIGN_BASE_PATH_KEY + serviceName;
			pathValue = this.relaxedPropertyResolver.getProperty(pathValue);*/
			ServiceProps serviceProps = this.feignProperties.getServices().get(serviceName);
			if (serviceProps!=null) {
				pathValue = serviceProps.getBasePath();
			}
			if(StringUtils.isBlank(pathValue)){
				// jfish.cloud.feign.base.contextPath
				pathValue = this.resolveRelatedProperty(FeignProperties.FEIGN_CONTEXT_PATH_KEY);
				//兼容旧配置
				if(StringUtils.isBlank(pathValue)){
//					pathValue = this.relaxedPropertyResolver.getProperty(FEIGN_CONTEXT_PATH_KEY2);
					pathValue = resolveRelatedProperty(FEIGN_CONTEXT_PATH_KEY2);
				}
			}
		}/*else if(pathValue.startsWith(FEIGN_BASE_PATH_TAG)){
			//:serviceName -> jfish.cloud.feign.basePath.serviceName
			pathValue = FEIGN_BASE_PATH_KEY + pathValue.substring(1);
			pathValue = this.relaxedPropertyResolver.getProperty(pathValue);
		}*/else if(ExpressionFacotry.DOLOR.isExpresstion(pathValue)){
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
	
	private String resolveRelatedProperty(String name) {
		String value = SpringUtils.getPropertyResolver(applicationContext).getProperty(name, "");
		return value;
//		return SpringUtils.resolvePlaceholders(applicationContext, name);
	}

	@Override
	public MethodMetadata parseAndValidateMetadata(Class<?> targetType, Method method) {
		MethodMetadata data = super.parseAndValidateMetadata(targetType, method);
		/*if(targetType.getName().contains("ProductApiClient") || targetType.getName().contains("ProductApi")){
			System.out.println("test");
		}*/
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
