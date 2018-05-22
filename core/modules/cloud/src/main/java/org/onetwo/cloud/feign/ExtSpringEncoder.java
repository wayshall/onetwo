package org.onetwo.cloud.feign;

import java.lang.reflect.Type;
import java.util.Map;

import org.onetwo.common.spring.utils.EnhanceBeanToMapConvertor;
import org.onetwo.common.spring.utils.EnhanceBeanToMapConvertor.EnhanceBeanToMapBuilder;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.boot.autoconfigure.web.HttpMessageConverters;
import org.springframework.cloud.netflix.feign.support.SpringEncoder;

import feign.RequestTemplate;
import feign.codec.EncodeException;

/**
 * @author wayshall
 * <br/>
 */
public class ExtSpringEncoder extends SpringEncoder {
	public static final String GET_METHOD = "get";
	
	EnhanceBeanToMapConvertor beanToMapConvertor = EnhanceBeanToMapBuilder.enhanceBuilder()
										 				.enableFieldNameAnnotation()
										 				.enableJsonPropertyAnnotation()
//										 				.enableUnderLineStyle()
										 				.build();

	public ExtSpringEncoder(ObjectFactory<HttpMessageConverters> messageConverters) {
		super(messageConverters);
	}

	@Override
	public void encode(Object requestBody, Type bodyType, RequestTemplate request) throws EncodeException {
		if(GET_METHOD.equalsIgnoreCase(request.method()) && requestBody!=null){
			Map<String, Object> map = beanToMapConvertor.toFlatMap(requestBody);
			map.forEach((name, value)->{
				if(value!=null){
					request.query(name, value.toString());
				}
			});
			return ;
		}
		super.encode(requestBody, bodyType, request);
	}
	
	

}
