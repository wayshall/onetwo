package org.onetwo.common.spring.rest;

import org.onetwo.common.jackson.JsonMapper;
import org.onetwo.common.reflect.BeanToMapConvertor;
import org.onetwo.common.reflect.BeanToMapConvertor.BeanToMapBuilder;
import org.onetwo.common.utils.ParamUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.OkHttp3ClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

public class ExtRestTemplate extends RestTemplate {
	
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

}
