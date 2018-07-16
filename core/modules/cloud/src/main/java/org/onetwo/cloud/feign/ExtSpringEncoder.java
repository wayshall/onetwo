package org.onetwo.cloud.feign;

import java.lang.reflect.Type;
import java.util.Collection;

import org.onetwo.common.apiclient.ApiClientMethod;
import org.onetwo.common.reflect.BeanToMapConvertor;
import org.onetwo.common.spring.rest.RestUtils;
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

	final private BeanToMapConvertor beanToMapConvertor = ApiClientMethod.getBeanToMapConvertor();
	
	public ExtSpringEncoder(ObjectFactory<HttpMessageConverters> messageConverters) {
		super(messageConverters);
	}

	@Override
	public void encode(Object requestBody, Type bodyType, RequestTemplate request) throws EncodeException {
		if(GET_METHOD.equalsIgnoreCase(request.method()) && requestBody!=null){
//			Map<String, Object> map = beanToMapConvertor.toFlatMap(requestBody);
			MultiValueMap<String, String> map = RestUtils.toMultiValueStringMap(requestBody);
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
			beanToMapConvertor.flatObject("", requestBody, (k, v, ctx)->{
				if(ctx!=null){
					values.add(ctx.getName(), v);
				}else{
					values.add(k, v);
				}
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
