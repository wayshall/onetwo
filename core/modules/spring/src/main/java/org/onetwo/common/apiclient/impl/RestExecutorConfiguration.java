package org.onetwo.common.apiclient.impl;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.onetwo.common.apiclient.RestExecutor;
import org.onetwo.common.apiclient.RestExecutorFactory;
import org.onetwo.common.apiclient.annotation.RestExecutorInterceptor;
import org.onetwo.common.apiclient.utils.ApiClientUtils;
import org.onetwo.common.spring.rest.ExtRestTemplate;
import org.onetwo.common.utils.LangUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.AnnotationAwareOrderComparator;
import org.springframework.http.client.ClientHttpRequestInterceptor;

import com.google.common.collect.Lists;

/**
 * @author wayshall
 * <br/>
 */
@Configuration
public class RestExecutorConfiguration {
	

	@Bean(RestExecutorFactory.REST_EXECUTOR_FACTORY_BEAN_NAME)
	public RestExecutorFactory apiClientRestExecutor(){
		return new DefaultRestExecutorFactory();
	}
	
	@Bean
	public RestExecutorConfigurer restExecutorConfigurer(){
		return new RestExecutorConfigurer();
	}
	
	public static final class RestExecutorConfigurer implements InitializingBean {
		private final Logger logger = ApiClientUtils.getApiclientlogger();

		@Autowired
		private ApplicationContext applicationContext;
		@Autowired
		private RestExecutor restExecutor;
		
		@Override
		public void afterPropertiesSet() throws Exception {
			ExtRestTemplate restTemplate = (ExtRestTemplate) restExecutor;
			Map<String, Object> restExecutorInterceptors = applicationContext.getBeansWithAnnotation(RestExecutorInterceptor.class);
			if(!LangUtils.isEmpty(restExecutorInterceptors)){
				List<ClientHttpRequestInterceptor> interList = restTemplate.getInterceptors();
				if(interList==null){
					interList = Lists.newArrayList();
					restTemplate.setInterceptors(interList);
				}
				for(Entry<String, Object> entry : restExecutorInterceptors.entrySet()){
					if(logger.isDebugEnabled()){
						logger.debug("register ClientHttpRequestInterceptor for RestExecutor: {}", entry.getKey());
					}
					interList.add((ClientHttpRequestInterceptor)entry.getValue());
				}
				AnnotationAwareOrderComparator.sort(interList);
			}
		}
	}

}
