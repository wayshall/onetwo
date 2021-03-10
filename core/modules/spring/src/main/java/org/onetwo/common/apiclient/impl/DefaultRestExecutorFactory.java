package org.onetwo.common.apiclient.impl;

import org.onetwo.common.apiclient.RestExecutor;
import org.onetwo.common.apiclient.RestExecutorFactory;
import org.onetwo.common.spring.rest.ExtRestTemplate;
import org.onetwo.common.spring.rest.RestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.client.OkHttp3ClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;

/**
 * @author wayshall
 * <br/>
 */
public class DefaultRestExecutorFactory extends RestExecutorFactory {
	
//	static private final Logger logger = ApiClientUtils.getApiclientlogger();

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
		ClientHttpRequestFactory requestFactory = createClientHttpRequestFactory(config);
		ExtRestTemplate restTemplate = new ExtRestTemplate(requestFactory);
//		if(RestUtils.isOkHttp3Present()){
//			OkHttp3ClientHttpRequestFactory requestFactory = new OkHttp3ClientHttpRequestFactory();
//			requestFactory.setConnectTimeout(config.getConnectTimeout());
//			requestFactory.setReadTimeout(config.getReadTimeout());
//			requestFactory.setWriteTimeout(config.getWriteTimeout());
//			restTemplate = new ExtRestTemplate(requestFactory);
//		}else{
//			restTemplate = new ExtRestTemplate();
//		}
		return restTemplate;
	}
	
	public static ClientHttpRequestFactory createClientHttpRequestFactory(RestExecutorConfig config) {
		if (RestUtils.isOkHttp3Present()) {
			OkHttp3ClientHttpRequestFactory requestFactory = new OkHttp3ClientHttpRequestFactory();
			requestFactory.setConnectTimeout(config.getConnectTimeout());
			requestFactory.setReadTimeout(config.getReadTimeout());
			requestFactory.setWriteTimeout(config.getWriteTimeout());
			return requestFactory;
		} else if (RestUtils.isHttpComponentPresent()){
			HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
			requestFactory.setConnectTimeout(config.getConnectTimeout());
			requestFactory.setReadTimeout(config.getReadTimeout());
//			requestFactory.setWriteTimeout(config.getWriteTimeout());
			return requestFactory;
		} else {
			SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
			requestFactory.setConnectTimeout(config.getConnectTimeout());
			requestFactory.setReadTimeout(config.getReadTimeout());
//			requestFactory.setWriteTimeout(config.getWriteTimeout());
			return requestFactory;
		}
	}

}
