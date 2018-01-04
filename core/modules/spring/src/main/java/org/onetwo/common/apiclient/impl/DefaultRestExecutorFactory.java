package org.onetwo.common.apiclient.impl;

import java.util.List;

import org.onetwo.common.apiclient.RestExecutor;
import org.onetwo.common.apiclient.RestExecutorFactory;
import org.onetwo.common.spring.rest.ExtRestTemplate;
import org.onetwo.common.spring.rest.RestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.OkHttp3ClientHttpRequestFactory;

import com.google.common.collect.Lists;

/**
 * @author wayshall
 * <br/>
 */
public class DefaultRestExecutorFactory extends RestExecutorFactory {

	@Autowired(required=false)
	private RestExecutorConfig restExecutorConfig;
	@Autowired(required=false)
	private List<ClientHttpRequestInterceptor> restExecutorRequestInterceptors;
	
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
		if(restExecutorRequestInterceptors!=null){
			List<ClientHttpRequestInterceptor> interList = restTemplate.getInterceptors();
			if(interList==null){
				interList = Lists.newArrayList();
				restTemplate.setInterceptors(interList);
			}
			interList.addAll(restExecutorRequestInterceptors);
		}
		return restTemplate;
	}
	
	

}
