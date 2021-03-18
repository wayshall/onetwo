package org.onetwo.cloud.feign;

import java.lang.reflect.Type;
import java.util.Collection;

import org.onetwo.common.apiclient.ApiClientMethod;
import org.onetwo.common.reflect.BeanToMapConvertor;
import org.onetwo.common.spring.SpringUtils.ConsumableValuePutter;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.boot.autoconfigure.web.HttpMessageConverters;
import org.springframework.cloud.netflix.feign.support.SpringEncoder;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import feign.RequestTemplate;
import feign.codec.EncodeException;

/**
 * @author wayshall
 * <br/>
 */
public class ExtSpringEncoder extends SpringEncoder {
	public static final String GET_METHOD = "get";
	
	/*EnhanceBeanToMapConvertor beanToMapConvertor = EnhanceBeanToMapBuilder.enhanceBuilder()
										 				.enableFieldNameAnnotation()
										 				.enableJsonPropertyAnnotation()
//										 				.enableUnderLineStyle()
										 				.build();*/

	final private BeanToMapConvertor postBodyConvertor = ApiClientMethod.getBeanToMapConvertor();
	final private BeanToMapConvertor getParamsConvertor = ApiClientMethod.getBeanToMapConvertor();
	
	public ExtSpringEncoder(ObjectFactory<HttpMessageConverters> messageConverters) {
		super(messageConverters);
		/*getParamsConvertor.setPropertyAcceptor((prop, value)->{
			return true;
		});*/
	}

	@Override
	public void encode(Object requestBody, Type bodyType, RequestTemplate request) throws EncodeException {
		if(requestBody==null){
			super.encode(requestBody, bodyType, request);
			return ;
		}
		if(GET_METHOD.equalsIgnoreCase(request.method()) && requestBody!=null){
//			Map<String, Object> map = beanToMapConvertor.toFlatMap(requestBody);
//			MultiValueMap<String, String> map = RestUtils.toMultiValueStringMap(requestBody);
			MultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
			
			// 使用ConsumableValuePutter增强对注解处理
			/*getParamsConvertor.flatObject("", requestBody, (k, v, ctx)->{
				map.add(k, v);
			});*/
			getParamsConvertor.flatObject("", requestBody, new ConsumableValuePutter((k, v) -> {
				// urlencode?
				map.add(k, v);
			}));
			map.forEach((name, value)->{
				if(value!=null){
					request.query(name, value.toArray(new String[0]));
				}
			});
			return ;
		}
		Object convertedRequestBody = convertRequestBodyIfNecessary(requestBody, request);
		super.encode(convertedRequestBody, bodyType, request);
	}
	
	protected Object convertRequestBodyIfNecessary(Object requestBody, RequestTemplate request){
		MediaType contentType = getRequestContentType(request);
		if(MediaType.APPLICATION_FORM_URLENCODED.equals(contentType) ||
				MediaType.MULTIPART_FORM_DATA.equals(contentType)){
			//form的话，需要转成multipleMap
			MultiValueMap<String, Object> values = new LinkedMultiValueMap<>();
			postBodyConvertor.flatObject("", requestBody, (k, v, ctx)->{
				/*if(ctx!=null){
					values.add(ctx.getName(), v);
				}else{
					values.add(k, v);
				}*/
				values.add(k, v);
			});
			return values;
		}
		return requestBody;
	}
	
	protected MediaType getRequestContentType(RequestTemplate request){
		Collection<String> contentTypes = request.headers().get("Content-Type");

		MediaType requestContentType = null;
		if (contentTypes != null && !contentTypes.isEmpty()) {
			String type = contentTypes.iterator().next();
			requestContentType = MediaType.valueOf(type);
		}
		return requestContentType;
	}

}
