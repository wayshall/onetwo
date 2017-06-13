package org.onetwo.common.apiclient;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.onetwo.common.log.JFishLoggerFactory;
import org.onetwo.common.proxy.AbstractMethodInterceptor;
import org.onetwo.common.reflect.ReflectUtils;
import org.onetwo.common.spring.SpringUtils;
import org.onetwo.common.spring.aop.Proxys;
import org.onetwo.common.spring.rest.ExtRestTemplate;
import org.onetwo.common.spring.rest.RestUtils;
import org.onetwo.common.spring.validator.ValidatorWrapper;
import org.onetwo.common.utils.JFishProperty;
import org.onetwo.common.utils.JFishPropertyInfoImpl;
import org.onetwo.common.utils.ParamUtils;
import org.onetwo.common.utils.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.cache.LoadingCache;

/**
 * @author wayshall
 * <br/>
 */
abstract public class AbstractApiClientFactoryBean<M extends ApiClientMethod> implements FactoryBean<Object>, InitializingBean {

	
	final protected Logger logger = JFishLoggerFactory.getLogger(this.getClass());
	
	protected Class<?> interfaceClass;
	
	protected String url;

	protected String path;

//	private boolean decode404;

//	private Class<?> fallback = void.class;
	
	protected Object apiObject;

	@Autowired(required=false)
	protected RestTemplate restTemplate;
	@Autowired(required=false)
	protected ValidatorWrapper validatorWrapper;
	
	@Override
	public void afterPropertiesSet() throws Exception {
		if(restTemplate==null){
			restTemplate = new ExtRestTemplate();
		}
		MethodInterceptor apiClient = createApiMethodInterceptor();
		this.apiObject = Proxys.interceptInterfaces(Arrays.asList(interfaceClass), apiClient);
	}
	

	abstract protected MethodInterceptor createApiMethodInterceptor();

	@Override
	public Object getObject() throws Exception {
		return this.apiObject;
	}

	@Override
	public Class<?> getObjectType() {
		return interfaceClass;
	}

	public void setRestTemplate(ExtRestTemplate extRestTemplate){
		this.restTemplate = extRestTemplate;
	}
	
	protected String getFullPath(String requestPath){
		StringBuilder fullPath = new StringBuilder();
		if(StringUtils.isNotBlank(url)){
			fullPath.append(url);
		}
		if(StringUtils.isNotBlank(path)){
			if(!path.startsWith("/")){
				fullPath.append("/");
			}
			fullPath.append(path);
		}
		if(StringUtils.isNotBlank(requestPath)){
			if(!requestPath.startsWith("/")){
				fullPath.append("/");
			}
			fullPath.append(requestPath);
		}
		return fullPath.toString();
	}
	@Override
	public boolean isSingleton() {
		return true;
	}

	public void setInterfaceClass(Class<?> interfaceClass) {
		this.interfaceClass = interfaceClass;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public void setRestTemplate(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}

	public void setValidatorWrapper(ValidatorWrapper validatorWrapper) {
		this.validatorWrapper = validatorWrapper;
	}
	
	public class DefaultApiMethodInterceptor extends AbstractMethodInterceptor<M> {

		public DefaultApiMethodInterceptor(LoadingCache<Method, M> methodCache) {
			super(methodCache);
		}
		
		protected Class<?> getActualResponseType(M invokeMethod){
			return invokeMethod.getMethodReturnType();
		}
		
		protected Object doInvoke(MethodInvocation invocation, M invokeMethod) {
			invokeMethod.validateArgements(validatorWrapper, invocation.getArguments());
			
			Class<?> responseType = getActualResponseType(invokeMethod);
			
			ResponseEntity<?> responseEntity = null;
			String path = invokeMethod.parsePath();
			path = getFullPath(path);
			RequestMethod requestMethod = invokeMethod.getRequestMethod();
			if(requestMethod==RequestMethod.GET){
				Map<String, Object> urlVariables = invokeMethod.toMap(invocation.getArguments());
				String paramString = RestUtils.keysToParamString(urlVariables);
				String actualUrl = ParamUtils.appendParamString(path, paramString);
				
				if(logger.isInfoEnabled()){
					logger.info("rest url : {}, urlVariables: {}", actualUrl, urlVariables);
				}
				responseEntity = restTemplate.getForEntity(actualUrl, responseType, urlVariables);
			}
			
			Object response = handleResponse(invokeMethod, responseEntity, responseType);
			return response;
		}
		
		protected Object handleResponse(ApiClientMethod invokeMethod, ResponseEntity<?> responseEntity, Class<?> actualResponseType){
			if(responseEntity.getStatusCode().is2xxSuccessful()){
				return responseEntity.getBody();
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

}
