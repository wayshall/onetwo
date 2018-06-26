package org.onetwo.cloud.feign;

import java.lang.reflect.Type;

import org.onetwo.common.spring.rest.RestUtils;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.boot.autoconfigure.web.HttpMessageConverters;
import org.springframework.cloud.netflix.feign.support.SpringEncoder;
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
		super.encode(requestBody, bodyType, request);
	}
	
	

}
