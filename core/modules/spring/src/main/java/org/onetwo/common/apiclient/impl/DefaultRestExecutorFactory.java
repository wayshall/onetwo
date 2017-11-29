package org.onetwo.common.apiclient.impl;

import org.onetwo.common.apiclient.RestExecutor;
import org.onetwo.common.apiclient.RestExecutorFactory;
import org.onetwo.common.spring.rest.ExtRestTemplate;
import org.onetwo.common.spring.rest.RestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.client.OkHttp3ClientHttpRequestFactory;

/**
 * @author wayshall
 * <br/>
 */
public class DefaultRestExecutorFactory extends RestExecutorFactory {

	@Autowired(required=false)
	private RestExecutorConfig restExecutorConfig;
	
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
	
	

}
