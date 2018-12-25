package org.onetwo.common.apiclient.impl;

import java.beans.PropertyDescriptor;
import java.util.List;
import java.util.Map;

import org.onetwo.common.apiclient.ApiClientMethod;
import org.onetwo.common.apiclient.ApiClientResponseHandler;
import org.onetwo.common.apiclient.utils.ApiClientUtils;
import org.onetwo.common.reflect.ReflectUtils;
import org.onetwo.common.spring.SpringUtils;
import org.onetwo.common.utils.JFishProperty;
import org.onetwo.common.utils.JFishPropertyInfoImpl;
import org.onetwo.common.utils.LangUtils;
import org.slf4j.Logger;
import org.springframework.beans.BeanWrapper;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;
import org.springframework.web.client.RestClientException;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.Lists;

/**
 * @author wayshall
 * <br/>
 */
public class DefaultApiClientResponseHandler<M extends ApiClientMethod> implements ApiClientResponseHandler<M> {
	protected final Logger logger = ApiClientUtils.getApiclientlogger();
	
	@Override
	public Class<?> getActualResponseType(M invokeMethod) {
		return invokeMethod.getMethodReturnType();
	}

	@Override
	public Object handleResponse(M invokeMethod, ResponseEntity<?> responseEntity, Class<?> actualResponseType){
		Object resposne = responseEntity.getBody();
		if(responseEntity.getStatusCode().is2xxSuccessful()){
			if (List.class.isAssignableFrom(actualResponseType)) {
				return convert2List(invokeMethod, responseEntity);
			}
			return resposne;
		}
		throw new RestClientException("error response: " + responseEntity.getStatusCodeValue());
	}
	
	@SuppressWarnings("unchecked")
	protected Object convert2List(M invokeMethod, ResponseEntity<?> responseEntity) {
		List<?> dataList = (List<?>) responseEntity.getBody();
		if (LangUtils.isEmpty(dataList) || !Map.class.isAssignableFrom(dataList.get(0).getClass())) {
			return responseEntity.getBody();
		}
		
		List<Map<String, ?>> mapList = (List<Map<String,?>>) Lists.newArrayList((List<Map<String,?>>) responseEntity.getBody());
		List<Object> datas = (List<Object>) responseEntity.getBody();
		datas.clear();
		for(Map<String, ?> data : mapList) {
			Object bean = this.map2Bean(data, invokeMethod.getComponentType());
			datas.add(bean);
		}
		return responseEntity.getBody();
	}
	
	protected <T> T map2Bean(Map<String, ?> props, Class<T> beanClass){
		Assert.notNull(beanClass, "beanClass can not be null");
		Assert.notNull(props, "props can not be null");
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
