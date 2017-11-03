package org.onetwo.cloud.feign;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.web.HttpMessageConverters;
import org.springframework.cloud.netflix.feign.AnnotatedParameterProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.ConversionService;

import feign.Contract;
import feign.Feign;

/**
 * @author wayshall
 * <br/>
 */
@Configuration
@ConditionalOnClass(Feign.class)
//@Import(FixHystrixFeignTargeterConfiguration.class)
public class ExtFeignConfiguration {

	@Autowired(required = false)
	private List<AnnotatedParameterProcessor> parameterProcessors = new ArrayList<>();
	@Autowired
	private HttpMessageConverters httpMessageConverters;


	@Bean
	@ConditionalOnMissingBean
	public Contract feignContract(ConversionService feignConversionService) {
		EnhanceSpringMvcContract contract = new EnhanceSpringMvcContract(this.parameterProcessors, feignConversionService);
		return contract;
	}

	@Bean
	@ConditionalOnMissingBean
	public ResultErrorDecoder errorDecoder(){
		return new ResultErrorDecoder(httpMessageConverters);
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
