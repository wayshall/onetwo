package org.onetwo.common.apiclient.impl;

import java.beans.PropertyDescriptor;
import java.util.Map;

import org.onetwo.common.apiclient.ApiClientMethod;
import org.onetwo.common.apiclient.ApiClientResponseHandler;
import org.onetwo.common.reflect.ReflectUtils;
import org.onetwo.common.spring.SpringUtils;
import org.onetwo.common.utils.JFishProperty;
import org.onetwo.common.utils.JFishPropertyInfoImpl;
import org.springframework.beans.BeanWrapper;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;
import org.springframework.web.client.RestClientException;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author wayshall
 * <br/>
 */
public class DefaultApiClientResponseHandler<M extends ApiClientMethod> implements ApiClientResponseHandler<M> {

	@Override
	public Class<?> getActualResponseType(M invokeMethod) {
		return invokeMethod.getMethodReturnType();
	}

	@Override
	public Object handleResponse(M invokeMethod, ResponseEntity<?> responseEntity, Class<?> actualResponseType){
		Object resposne = responseEntity.getBody();
		if(responseEntity.getStatusCode().is2xxSuccessful()){
			return resposne;
		}
		throw new RestClientException("error response: " + responseEntity.getStatusCodeValue());
	}
	
	protected <T> T map2Bean(Map<String, ?> props, Class<T> beanClass){
		Assert.notNull(beanClass);
		Assert.notNull(props);
		T bean = ReflectUtils.newInstance(beanClass);
		BeanWrapper bw = SpringUtils.newBeanWrapper(bean);
		for(PropertyDescriptor pd : bw.getPropertyDescriptors()){
			if(props.containsKey(pd.getName())){
				Object value = props.get(pd.getName());
				bw.setPropertyValue(pd.getName(), value);
			}else{
				JFishProperty jproperty = new JFishPropertyInfoImpl(pd);
				JsonProperty jsonProperty = jproperty.getAnnotation(JsonProperty.class);
				if(jsonProperty==null){
					jsonProperty = jproperty.getCorrespondingJFishProperty()
											.map(jp->jp.getAnnotation(JsonProperty.class))
											.orElse(null);
				}
				if(jsonProperty!=null && props.containsKey(jsonProperty.value())){
					Object value = props.get(jsonProperty.value());
					bw.setPropertyValue(pd.getName(), value);
				}
			}
		}
		return bean;
	}

}
