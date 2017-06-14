package org.onetwo.common.apiclient;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.onetwo.common.log.JFishLoggerFactory;
import org.onetwo.common.proxy.AbstractMethodInterceptor;
import org.onetwo.common.spring.aop.Proxys;
import org.onetwo.common.spring.rest.ExtRestTemplate;
import org.onetwo.common.spring.rest.RestUtils;
import org.onetwo.common.spring.validator.ValidatorWrapper;
import org.onetwo.common.utils.ParamUtils;
import org.onetwo.common.utils.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

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
	protected ApiClientResponseHandler<M> responseHandler = new DefaultApiClientResponseHandler<M>();
	
	final public void setResponseHandler(ApiClientResponseHandler<M> responseHandler) {
		this.responseHandler = responseHandler;
	}

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
		
		protected String processUrlBeforeRequest(M invokeMethod, String path){
			return path;
		}
		
		protected Object doInvoke(MethodInvocation invocation, M invokeMethod) {
			invokeMethod.validateArgements(validatorWrapper, invocation.getArguments());
			
			ResponseEntity<?> responseEntity = null;
			String path = invokeMethod.parsePath();
			path = getFullPath(path);
			path = processUrlBeforeRequest(invokeMethod, path);
			RequestMethod requestMethod = invokeMethod.getRequestMethod();
			
			Object[] args = invocation.getArguments();
			Class<?> responseType = responseHandler.getActualResponseType(invokeMethod);
			Map<String, Object> uriVariables = invokeMethod.toMap(invocation.getArguments());
			String paramString = RestUtils.keysToParamString(uriVariables);
			String actualUrl = ParamUtils.appendParamString(path, paramString);

			if(logger.isInfoEnabled()){
				logger.info("rest url : {}, urlVariables: {}", actualUrl, uriVariables);
			}
			if(requestMethod==RequestMethod.GET){
				responseEntity = restTemplate.getForEntity(actualUrl, responseType, uriVariables);
			}else if(requestMethod==RequestMethod.POST){
				Object requestBody = invokeMethod.getRequestBody(args);
				responseEntity = restTemplate.postForEntity(actualUrl, requestBody, responseType, uriVariables);
			}else{
				throw new RestClientException("unsupported method: " + requestMethod);
			}
			
			Object response = responseHandler.handleResponse(invokeMethod, responseEntity, responseType);
			return response;
		}
		
	}

}
