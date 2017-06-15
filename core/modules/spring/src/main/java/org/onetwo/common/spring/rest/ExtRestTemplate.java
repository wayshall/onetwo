package org.onetwo.common.spring.rest;

import java.io.IOException;
import java.util.Map;
import java.util.function.Consumer;

import org.onetwo.common.apiclient.RequestContextData;
import org.onetwo.common.apiclient.RestExecutor;
import org.onetwo.common.jackson.JsonMapper;
import org.onetwo.common.reflect.BeanToMapConvertor;
import org.onetwo.common.reflect.BeanToMapConvertor.BeanToMapBuilder;
import org.onetwo.common.utils.ParamUtils;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpRequest;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.OkHttp3ClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RequestCallback;
import org.springframework.web.client.ResponseExtractor;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

public class ExtRestTemplate extends RestTemplate implements RestExecutor {
	
	private BeanToMapConvertor beanToMapConvertor = BeanToMapBuilder.newBuilder().enableUnderLineStyle().build();

	public ExtRestTemplate(){
		this(RestUtils.isOkHttp3Present()?new OkHttp3ClientHttpRequestFactory():null);
	}
	
	public ExtRestTemplate(ClientHttpRequestFactory requestFactory){
		super();
		for(HttpMessageConverter<?> converter : this.getMessageConverters()){
			if(MappingJackson2HttpMessageConverter.class.isInstance(converter)){
				((MappingJackson2HttpMessageConverter)converter).setObjectMapper(JsonMapper.IGNORE_NULL.getObjectMapper());
				break;
			}
			
		}
		if(requestFactory!=null){
			this.setRequestFactory(requestFactory);
//			this.setRequestFactory(new HttpComponentsClientHttpRequestFactory());
		}
	}

	public void setBeanToMapConvertor(BeanToMapConvertor beanToMapConvertor) {
		this.beanToMapConvertor = beanToMapConvertor;
	}
	
	@Override
	public <T> ResponseEntity<T> execute(RequestContextData context) {
		RequestCallback rc = null;
		HttpMethod method = context.getHttpMethod();
		ResponseExtractor<ResponseEntity<T>> responseExtractor = null;
		
		switch (method) {
			case GET:
				rc = super.acceptHeaderRequestCallback(context.getResponseType());
				responseExtractor = responseEntityExtractor(context.getResponseType());
				break;
			case POST:
			case PATCH:
				rc = super.httpEntityCallback(context.getRequestBody(), context.getResponseType());
				responseExtractor = responseEntityExtractor(context.getResponseType());
				break;
			default:
				throw new RestClientException("unsupported method: " + method);
		}
		if(context.getRequestCallback()!=null){
			rc = wrapRequestCallback(rc, context.getRequestCallback());
		}
		return execute(context.getRequestUrl(), method, rc, responseExtractor, context.getUriVariables());
	}
	
	public <T> RequestCallback wrapRequestCallback(RequestCallback acceptHeaderRequestCallback, Consumer<ClientHttpRequest> callback) {
		return new ProcessHeaderRequestCallback(acceptHeaderRequestCallback, callback);
	}
	

	public <T> ResponseEntity<T> getForEntity(String url, Class<T> responseType, RequestCallback requestCallback, Map<String, ?> uriVariables) throws RestClientException {
		ResponseExtractor<ResponseEntity<T>> responseExtractor = responseEntityExtractor(responseType);
		return execute(url, HttpMethod.GET, requestCallback, responseExtractor, uriVariables);
	}
	

	public <T> T post(String url, Object request, Class<T> responseType){
		ResponseEntity<T> response = postForEntity(url, RestUtils.createFormEntity(request, beanToMapConvertor), responseType);
		if(HttpStatus.OK.equals(response.getStatusCode())){
			return response.getBody();
		}
		throw new RestClientException("invoke rest interface["+url+"] error: " + response);
	}

	public <T> T get(String url, Object request, Class<T> responseType){
		String paramString = RestUtils.propertiesToParamString(request);
		MultiValueMap<String, Object> urlVariables = RestUtils.toMultiValueMap(request, beanToMapConvertor);
		String atualUrl = ParamUtils.appendParamString(url, paramString);
		return getForObject(atualUrl, responseType, urlVariables);
	}

	public <T> T get(String url, Class<T> responseType, Object...urlVariables){
		ResponseEntity<T> response = getForEntity(url, responseType, urlVariables);
		if(HttpStatus.OK.equals(response.getStatusCode())){
			return response.getBody();
		}
		throw new RestClientException("invoke rest interface["+url+"] error: " + response);
	}

	protected static class ProcessHeaderRequestCallback implements RequestCallback {
		private final RequestCallback acceptHeaderRequestCallback;
		private final Consumer<ClientHttpRequest> callback;

		public ProcessHeaderRequestCallback(RequestCallback acceptHeaderRequestCallback, Consumer<ClientHttpRequest> callback) {
			super();
			this.acceptHeaderRequestCallback = acceptHeaderRequestCallback;
			this.callback = callback;
		}

		@Override
		public void doWithRequest(ClientHttpRequest request) throws IOException {
			this.acceptHeaderRequestCallback.doWithRequest(request);
			this.callback.accept(request);
		}
		
	}
}
