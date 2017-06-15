package org.onetwo.common.apiclient;

import java.lang.reflect.Method;
import java.util.Map;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.onetwo.common.log.JFishLoggerFactory;
import org.onetwo.common.proxy.AbstractMethodInterceptor;
import org.onetwo.common.spring.aop.MixinableInterfaceCreator;
import org.onetwo.common.spring.aop.SpringMixinableInterfaceCreator;
import org.onetwo.common.spring.rest.ExtRestTemplate;
import org.onetwo.common.spring.rest.RestUtils;
import org.onetwo.common.spring.validator.ValidatorWrapper;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.utils.ParamUtils;
import org.onetwo.common.utils.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMethod;

import com.google.common.cache.LoadingCache;

/**
 * @author wayshall
 * <br/>
 */
abstract public class AbstractApiClientFactoryBean<M extends ApiClientMethod> implements FactoryBean<Object>, InitializingBean {


	private static final String ACCEPT = "Accept";
	private static final String CONTENT_TYPE = "Content-Type";
	
	final private static ExtRestTemplate API_REST_TEMPLATE = new ExtRestTemplate();
	
	final protected Logger logger = JFishLoggerFactory.getLogger(this.getClass());
	
	protected Class<?> interfaceClass;
	
	protected String url;

	protected String path;

//	private boolean decode404;

//	private Class<?> fallback = void.class;
	//ClientHttpRequestInterceptor
	
	protected Object apiObject;

	@Autowired(required=false)
	protected RestExecutor restExecutor;
	@Autowired(required=false)
	protected ValidatorWrapper validatorWrapper;
	protected ApiClientResponseHandler<M> responseHandler = new DefaultApiClientResponseHandler<M>();
	@Autowired
	private ApplicationContext applicationContext;
	
	final public void setResponseHandler(ApiClientResponseHandler<M> responseHandler) {
		this.responseHandler = responseHandler;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		if(restExecutor==null){
			restExecutor = API_REST_TEMPLATE;
		}
		MethodInterceptor apiClient = createApiMethodInterceptor();
//		this.apiObject = Proxys.interceptInterfaces(Arrays.asList(interfaceClass), apiClient);
		
		MixinableInterfaceCreator mixinableCreator = SpringMixinableInterfaceCreator.classNamePostfixMixin(interfaceClass);
		this.apiObject = mixinableCreator.createMixinObject(apiClient);
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

	public void setRestExecutor(RestExecutor restExecutor) {
		this.restExecutor = restExecutor;
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

	public void setValidatorWrapper(ValidatorWrapper validatorWrapper) {
		this.validatorWrapper = validatorWrapper;
	}
	
	public class DefaultApiMethodInterceptor extends AbstractMethodInterceptor<M> {

		public DefaultApiMethodInterceptor(LoadingCache<Method, M> methodCache) {
			super(methodCache);
		}
		
		protected String processUrlBeforeRequest(String actualUrl, M invokeMethod, Map<String, Object> uriVariables){
			if(!uriVariables.isEmpty()){
				String paramString = RestUtils.keysToParamString(uriVariables);
				actualUrl = ParamUtils.appendParamString(actualUrl, paramString);
			}
			return actualUrl;
		}
		
		protected Object doInvoke(MethodInvocation invocation, M invokeMethod) {
			Object[] args = processArgumentsBeforeRequest(invocation, invokeMethod);
			invokeMethod.validateArgements(validatorWrapper, args);

			RequestContextData context = createRequestContextData(args, invokeMethod);
			if(logger.isInfoEnabled()){
				logger.info("rest url : {} - {}, urlVariables: {}", context.getHttpMethod(), context.getRequestUrl(), context.getUriVariables());
			}
			
			ResponseEntity<Object> responseEntity = restExecutor.execute(context);
			Object response = responseHandler.handleResponse(invokeMethod, responseEntity, context.getResponseType());
			return response;
		}
		
		protected Object[] processArgumentsBeforeRequest(MethodInvocation invocation, M invokeMethod){
			return invocation.getArguments();
		}
		
		protected RequestContextData createRequestContextData(Object[] args, M invokeMethod){
			Map<String, Object> uriVariables = invokeMethod.toMap(args);
			String actualUrl = getFullPath(invokeMethod.getPath());
			actualUrl = processUrlBeforeRequest(actualUrl, invokeMethod, uriVariables);

			RequestMethod requestMethod = invokeMethod.getRequestMethod();
			Class<?> responseType = responseHandler.getActualResponseType(invokeMethod);
			
			RequestContextData context = new RequestContextData(requestMethod, actualUrl, uriVariables, responseType);
			if(requestMethod==RequestMethod.POST){
				Object requestBody = invokeMethod.getRequestBody(args);
				context.setRequestBody(requestBody);
			}
			
			context.doWithRequestCallback(request->{
				invokeMethod.getAcceptHeader().ifPresent(accept->{
					request.getHeaders().set(ACCEPT, accept);
				});
				invokeMethod.getContentType().ifPresent(contentType->{
					request.getHeaders().set(CONTENT_TYPE, contentType);
				});
				if(!LangUtils.isEmpty(invokeMethod.getHeaders())){
					for (String header : invokeMethod.getHeaders()) {
						int index = header.indexOf('=');
						request.getHeaders().set(header.substring(0, index), header.substring(index + 1).trim());
					}
				}
			});

			return context;
		}
		
	}
	

}
