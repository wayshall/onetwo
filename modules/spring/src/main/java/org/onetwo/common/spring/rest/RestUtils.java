package org.onetwo.common.spring.rest;

import java.beans.PropertyDescriptor;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.onetwo.common.utils.Assert;
import org.onetwo.common.utils.CharsetUtils;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.utils.ReflectUtils;
import org.onetwo.common.utils.ReflectUtils.PropertyDescriptorCallback;
import org.onetwo.common.utils.StringUtils;
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
		
		final MultiValueMap<String, String> params = toMultiValueMap(obj);
		return new HttpEntity<MultiValueMap<String, ?>>(params, formHeader);
	}
	
	public static MultiValueMap<String, String> toMultiValueMap(final Object obj){
		final MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
		appendMultiValueMap(params, "", obj);
		return params;
	}
	
	@SuppressWarnings("unchecked")
	public static void appendMultiValueMap(final MultiValueMap<String, String> params, final String prefixName, final Object obj){
		if(obj==null)
			return ;
		if(String.class.isInstance(obj) || LangUtils.isBaseTypeObject(obj)){
			params.set(prefixName, obj==null?"":obj.toString());
		}else if(MultiValueMap.class.isInstance(obj)){
			params.putAll((MultiValueMap<String, String>)obj);
		}else if(Map.class.isInstance(obj)){
			String mapPrefixName = prefixName;
			if(StringUtils.isNotBlank(prefixName)){
				mapPrefixName = prefixName+DOT_ACCESOR;
			}
			for(Entry<String, Object> entry : ((Map<String, Object>)obj).entrySet()){
				if(entry.getValue()==null){
					params.set(mapPrefixName+entry.getKey(), "");
				}else if(String.class.isInstance(entry.getValue()) || LangUtils.isBaseTypeObject(entry.getValue())){
					params.set(mapPrefixName+entry.getKey(), entry.getValue()==null?"":entry.getValue().toString());
				}else{
					appendMultiValueMap(params, mapPrefixName+entry.getKey(), entry.getValue());
				}
			}
		}else if(LangUtils.isMultiple(obj)){
			List<Object> list = LangUtils.asList(obj);
			int index = 0;
			for(Object o : list){
				String listPrefixName = prefixName + LIST_LEFT+index+LIST_RIGHT;
				if(String.class.isInstance(o) || LangUtils.isBaseTypeObject(o)){
					params.set(listPrefixName, o==null?"":o.toString());
				}else{
					appendMultiValueMap(params, listPrefixName, o);
				}
				index++;
			}
		}else{
			ReflectUtils.listProperties(obj.getClass(), new PropertyDescriptorCallback() {
				
				@Override
				public void doWithProperty(PropertyDescriptor prop) {
					Object val = ReflectUtils.getProperty(obj, prop);
					if(val!=null){
						if(StringUtils.isBlank(prefixName)){
							appendMultiValueMap(params, prop.getName(), val);
						}else{
							appendMultiValueMap(params, prefixName+DOT_ACCESOR+prop.getName(), val);
						}
					}
				}
			});
		}
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
