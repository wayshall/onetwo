package org.onetwo.common.spring.rest;

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.function.BiFunction;

import org.onetwo.common.reflect.BeanToMapConvertor;
import org.onetwo.common.spring.utils.EnhanceBeanToMapConvertor.EnhanceBeanToMapBuilder;
import org.onetwo.common.utils.CharsetUtils;
import org.onetwo.common.utils.ParamUtils;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.util.ClassUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestMethod;

import com.google.common.collect.Sets;

public final class RestUtils {
	private static Collection<RequestMethod> BODY_SUPPORT_METHODS = Sets.newHashSet(RequestMethod.POST, RequestMethod.PATCH, RequestMethod.PUT);

	public static final String CLASS_OK_HTTP_CLIENT = "okhttp3.OkHttpClient";
	
	public static final String LIST_LEFT = "[";
	public static final String LIST_RIGHT = "]";
	public static final String DOT_ACCESOR = ".";
	
	public static final String ACCEPT_CHARSET = "Accept-Charset";

	public static final HttpHeaders FORM_HEADER;
	public static final HttpHeaders XML_HEADER;
	public static final HttpHeaders JSON_HEADER;
	public static final HttpHeaders TEXT_HEADER;

//	private static final BeanToMapConvertor BEAN_TO_MAP_CONVERTOR = BeanToMapBuilder.newBuilder().build();
	/*private static final BeanToMapConvertor STRING_VALUE_CONVERTOR = BeanToMapBuilder.newBuilder()
																						.propertyAcceptor((p, v)->v!=null)
																						.valueConvertor((p, v)->v.toString())
																						.build();*/
	private static final BeanToMapConvertor BEAN_TO_MAP_CONVERTOR = EnhanceBeanToMapBuilder.enhanceBuilder()
																							.enableJsonPropertyAnnotation()
																							.enableFieldNameAnnotation()
//																							.enableUnderLineStyle()
																							.propertyAcceptor((p, v)->v!=null)
//																							.valueConvertor((p, v)->v.toString())
																							.build();
	
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
	
	public static BeanToMapConvertor getBeanToMapConvertor() {
		return BEAN_TO_MAP_CONVERTOR;
	}
	public static boolean isRequestBodySupportedMethod(HttpMethod method){
		return isRequestBodySupportedMethod(RequestMethod.valueOf(method.name()));
	}
	public static boolean isRequestBodySupportedMethod(RequestMethod method){
		return BODY_SUPPORT_METHODS.contains(method);
	}
	
	public static boolean isOkHttp3Present(){
		return ClassUtils.isPresent(CLASS_OK_HTTP_CLIENT, null);
	}
	

//	@SuppressWarnings("unchecked")
	public static HttpEntity<?> createFormEntity(final Object obj){
		return createFormEntity(obj, BEAN_TO_MAP_CONVERTOR);
	}
	public static HttpEntity<?> createFormEntity(final Object obj, BeanToMapConvertor convertor){
//		Assert.notNull(obj);
		if (obj==null) {
			return new HttpEntity<MultiValueMap<String, ?>>(FORM_HEADER);
		}
		if(HttpEntity.class.isInstance(obj)){
			return (HttpEntity<?>)obj;
		}
		
		HttpHeaders formHeader = FORM_HEADER;//createHeader(MediaType.APPLICATION_FORM_URLENCODED);
		
		final MultiValueMap<String, Object> params = toMultiValueMap(obj, convertor);
		return new HttpEntity<MultiValueMap<String, ?>>(params, formHeader);
	}
	
	public static MultiValueMap<String, Object> toMultiValueMap(final Object obj){
		return toMultiValueMap(obj, BEAN_TO_MAP_CONVERTOR);
	}
	
	public static MultiValueMap<String, String> toMultiValueStringMap(final Object obj){
		final MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		BEAN_TO_MAP_CONVERTOR.flatObject("", obj, (key, value, ctx)->params.set(key, value.toString()));
		return params;
	}
	/*public static MultiValueMap<String, String> toMultiValueStringMap(final Object obj, BeanToMapConvertor convertor){
		final MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		convertor.flatObject("", obj, (key, value, ctx)->{
			if(value==null){
				return ;
			}
			params.add(key, value.toString());
		});
		return params;
	}*/
	@SuppressWarnings("unchecked")
	public static <V> MultiValueMap<String, V> toMultiValueMap(final Object obj, BeanToMapConvertor convertor){
		final MultiValueMap<String, V> params = new LinkedMultiValueMap<>();
//		appendMultiValueMap(params, "", obj);
		convertor.flatObject("", obj, (key, value, ctx)->params.set(key, (V)value));
		return params;
	}
	
	public static String propertiesToParamString(Object request){
		return keysToParamString(toMultiValueMap(request));
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static String keysToParamString(Map params){
		return ParamUtils.toParamString(params, (BiFunction<String, Object, String>)(k, v)->{
			return k+"={"+k+"}";
		});
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

	private RestUtils(){}
}
