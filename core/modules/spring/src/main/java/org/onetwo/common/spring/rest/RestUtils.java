package org.onetwo.common.spring.rest;

import java.util.Arrays;

import org.onetwo.common.reflect.BeanToMapConvertor;
import org.onetwo.common.utils.Assert;
import org.onetwo.common.utils.CharsetUtils;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

public final class RestUtils {
	
	private RestUtils(){}

	public static final String LIST_LEFT = "[";
	public static final String LIST_RIGHT = "]";
	public static final String DOT_ACCESOR = ".";
	
	public static final String ACCEPT_CHARSET = "Accept-Charset";

	public static final HttpHeaders FORM_HEADER;
	public static final HttpHeaders XML_HEADER;
	public static final HttpHeaders JSON_HEADER;
	public static final HttpHeaders TEXT_HEADER;

	private static final BeanToMapConvertor beanToMapConvertor = new BeanToMapConvertor();
	
	static{
		FORM_HEADER = createHeader(MediaType.APPLICATION_FORM_URLENCODED);
		
		XML_HEADER = new HttpHeaders();
		XML_HEADER.setContentType(MediaType.APPLICATION_ATOM_XML);
		XML_HEADER.set("Accept-Charset", CharsetUtils.UTF_8);

		JSON_HEADER = new HttpHeaders();
		JSON_HEADER.setContentType(MediaType.APPLICATION_JSON);
		JSON_HEADER.set(ACCEPT_CHARSET, CharsetUtils.UTF_8);

		TEXT_HEADER = new HttpHeaders();
		TEXT_HEADER.setContentType(MediaType.TEXT_PLAIN);
		TEXT_HEADER.set(ACCEPT_CHARSET, CharsetUtils.UTF_8);
		TEXT_HEADER.setAccept(Arrays.asList(MediaType.TEXT_PLAIN));
		
	}
	

//	@SuppressWarnings("unchecked")
	public static HttpEntity<?> createFormEntity(final Object obj){
		Assert.notNull(obj);
		if(HttpEntity.class.isInstance(obj)){
			return (HttpEntity<?>)obj;
		}
		
		HttpHeaders formHeader = FORM_HEADER;//createHeader(MediaType.APPLICATION_FORM_URLENCODED);
		
		final MultiValueMap<String, Object> params = toMultiValueMap(obj);
		return new HttpEntity<MultiValueMap<String, ?>>(params, formHeader);
	}
	
	public static MultiValueMap<String, Object> toMultiValueMap(final Object obj){
		final MultiValueMap<String, Object> params = new LinkedMultiValueMap<>();
//		appendMultiValueMap(params, "", obj);
		beanToMapConvertor.flatObject("", obj, (key, value)->params.set(key, value));
		return params;
	}
	
	public static HttpHeaders createHeader(MediaType mediaType){
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(mediaType);
		headers.set(ACCEPT_CHARSET, CharsetUtils.UTF_8);
		return headers;
	}
	
	public static <T> HttpEntity<T> createHttpEntity(T obj, MediaType mediaType){
		HttpHeaders headers = createHeader(mediaType);
		
		HttpEntity<T> entity = new HttpEntity<T>(obj, headers);
		return entity;
	}

}
