package org.onetwo.common.apiclient.impl;

import org.onetwo.common.apiclient.RestExecutor;
import org.onetwo.common.apiclient.RestExecutorFactory;
import org.onetwo.common.apiclient.utils.ApiClientUtils;
import org.onetwo.common.spring.rest.ExtRestTemplate;
import org.onetwo.common.spring.rest.RestUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.client.OkHttp3ClientHttpRequestFactory;

/**
 * @author wayshall
 * <br/>
 */
public class DefaultRestExecutorFactory extends RestExecutorFactory implements InitializingBean {
	
	static private final Logger logger = ApiClientUtils.getApiclientlogger();

	@Autowired(required=false)
	private RestExecutorConfig restExecutorConfig;
	@Autowired
	private ApplicationContext applicationContext;
	
	public DefaultRestExecutorFactory() {
	}


	@Override
	public RestExecutor createRestExecutor() {
		RestExecutorConfig config = this.restExecutorConfig;
		if(config==null){
			config = new RestExecutorConfig();
		}
		ExtRestTemplate restTemplate = null;
		if(RestUtils.isOkHttp3Present()){
			OkHttp3ClientHttpRequestFactory requestFactory = new OkHttp3ClientHttpRequestFactory();
			requestFactory.setConnectTimeout(config.getConnectTimeout());
			requestFactory.setReadTimeout(config.getReadTimeout());
			requestFactory.setWriteTimeout(config.getWriteTimeout());
			restTemplate = new ExtRestTemplate(requestFactory);
		}else{
			restTemplate = new ExtRestTemplate();
		}
		return restTemplate;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		/*ExtRestTemplate restTemplate = (ExtRestTemplate) getObject();
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
		}*/
	}
	
	

}
