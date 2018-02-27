package org.onetwo.cloud.feign;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.onetwo.boot.core.config.BootSpringConfig;
import org.onetwo.boot.core.web.service.impl.SimpleLoggerManager;
import org.onetwo.common.spring.Springs;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.web.HttpMessageConverters;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.netflix.feign.AnnotatedParameterProcessor;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.cloud.netflix.feign.annotation.PathVariableParameterProcessor;
import org.springframework.cloud.netflix.feign.annotation.RequestHeaderParameterProcessor;
import org.springframework.cloud.netflix.feign.annotation.RequestParamParameterProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.ConversionService;

import feign.Contract;
import feign.Feign;
import feign.Logger;
import feign.codec.Decoder;

/**
 * @author wayshall
 * <br/>
 */
@Configuration
@ConditionalOnClass(Feign.class)
//@Import(FixHystrixFeignTargeterConfiguration.class)
@EnableConfigurationProperties({FeignProperties.class, BootSpringConfig.class})
@ConditionalOnProperty(value=FeignProperties.ENABLE_KEY, matchIfMissing=true)
public class ExtFeignConfiguration implements InitializingBean {

	@Autowired(required = false)
	private List<AnnotatedParameterProcessor> parameterProcessors = new ArrayList<>();
	@Autowired
	private HttpMessageConverters httpMessageConverters;
	@Autowired
	private ObjectFactory<HttpMessageConverters> messageConverters;
	@Autowired
	private FeignProperties feignProperties;
	@Autowired
	private BootSpringConfig bootSpringConfig;
	@Autowired(required=false)
	private SimpleLoggerManager simpleLoggerManager;
	@Autowired
	private ApplicationContext applicationContext;


	@Override
	public void afterPropertiesSet() throws Exception {
		if(simpleLoggerManager==null){
			this.simpleLoggerManager = new SimpleLoggerManager();
		}
	}

	@Bean
	@ConditionalOnMissingBean
	public Decoder feignDecoder() {
		return new ExtResponseEntityDecoder(messageConverters);
	}

	@Bean
	@ConditionalOnMissingBean
	public Contract feignContract(ConversionService feignConversionService) {
		List<AnnotatedParameterProcessor> parameterProcessors = getDefaultAnnotatedArgumentsProcessors();
		if(!this.parameterProcessors.isEmpty()){
			parameterProcessors.addAll(this.parameterProcessors);
		}
		EnhanceSpringMvcContract contract = new EnhanceSpringMvcContract(parameterProcessors, feignConversionService);
		return contract;
	}


	private List<AnnotatedParameterProcessor> getDefaultAnnotatedArgumentsProcessors() {
		List<AnnotatedParameterProcessor> annotatedArgumentResolvers = new ArrayList<>();

		annotatedArgumentResolvers.add(new PathVariableParameterProcessor());
		annotatedArgumentResolvers.add(new RequestParamParameterProcessor());
		annotatedArgumentResolvers.add(new RequestHeaderParameterProcessor());

		return annotatedArgumentResolvers;
	}

	@Bean
	@ConditionalOnMissingBean
	public ResultErrorDecoder errorDecoder(){
		return new ResultErrorDecoder(httpMessageConverters);
	}
	
	@Bean
	@ConditionalOnMissingBean(Logger.Level.class)
	public Logger.Level feignLoggerLevel(){
		Logger.Level level = feignProperties.getLogger().getLevel();
		if(level==null){
			if(bootSpringConfig.isDev()){
				level = Logger.Level.FULL;
			}else if(bootSpringConfig.isTest()){
				level = Logger.Level.BASIC;
			}else{
				level = Logger.Level.NONE;
			}
		}
		if(level!=Logger.Level.NONE && feignProperties.getLogger().isAutoChangeLevel()){
			Set<String> apiNames = Stream.of(Springs.getInstance().getAppContext().getBeanDefinitionNames())
									.filter(beanName->applicationContext.findAnnotationOnBean(beanName, FeignClient.class)!=null)
									.collect(Collectors.toSet());
			simpleLoggerManager.changeLevels("DEBUG", apiNames.toArray(new String[0]));
		}
		return level;
	}

	/*@Configuration
	@ConditionalOnClass({ HystrixCommand.class, HystrixFeign.class })
	protected static class HystrixFeignConfiguration {
		@Bean
		@Scope("prototype")
		@ConditionalOnMissingBean
		@ConditionalOnProperty(name = "feign.hystrix.enabled", matchIfMissing = true)
		public Feign.Builder feignHystrixBuilder() {
			return new HystrixFeignBuilder();
		}
	}*/
}
