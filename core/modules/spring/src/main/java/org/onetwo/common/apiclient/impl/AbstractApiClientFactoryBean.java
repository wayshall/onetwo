package org.onetwo.common.apiclient.impl;

import java.lang.reflect.Method;
import java.util.Map;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.onetwo.common.apiclient.ApiClientMethod;
import org.onetwo.common.apiclient.ApiClientMethod.ApiClientMethodParameter;
import org.onetwo.common.apiclient.ApiClientResponseHandler;
import org.onetwo.common.apiclient.RequestContextData;
import org.onetwo.common.apiclient.RestExecutor;
import org.onetwo.common.apiclient.utils.ApiClientConstants.ApiClientErrors;
import org.onetwo.common.exception.ApiClientException;
import org.onetwo.common.expr.ExpressionFacotry;
import org.onetwo.common.log.JFishLoggerFactory;
import org.onetwo.common.proxy.AbstractMethodInterceptor;
import org.onetwo.common.spring.SpringUtils;
import org.onetwo.common.spring.Springs;
import org.onetwo.common.spring.aop.MixinableInterfaceCreator;
import org.onetwo.common.spring.aop.SpringMixinableInterfaceCreator;
import org.onetwo.common.spring.rest.RestUtils;
import org.onetwo.common.spring.validator.ValidatorWrapper;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.utils.ParamUtils;
import org.onetwo.common.utils.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMethod;

import com.google.common.cache.LoadingCache;

/**
 * @author wayshall
 * <br/>
 */
abstract public class AbstractApiClientFactoryBean<M extends ApiClientMethod> implements FactoryBean<Object>, InitializingBean, ApplicationContextAware {


	private static final String ACCEPT = "Accept";
	private static final String CONTENT_TYPE = "Content-Type";
	
	
	final protected Logger logger = JFishLoggerFactory.getLogger(this.getClass());
	
	protected Class<?> interfaceClass;
	
	protected String url;

	protected String path;

//	private boolean decode404;

//	private Class<?> fallback = void.class;
	//ClientHttpRequestInterceptor
	
	protected Object apiObject;

	protected RestExecutor restExecutor;
	@Autowired(required=false)
	protected ValidatorWrapper validatorWrapper;
	protected ApiClientResponseHandler<M> responseHandler = new DefaultApiClientResponseHandler<M>();
	protected ApplicationContext applicationContext;
	
	final public void setResponseHandler(ApiClientResponseHandler<M> responseHandler) {
		this.responseHandler = responseHandler;
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		Assert.notNull(restExecutor);
		
//		this.apiObject = Proxys.interceptInterfaces(Arrays.asList(interfaceClass), apiClient);
		
		//在getObject里初始化可避免启动时发生下面的错误
		//Bean creation exception on non-lazy FactoryBean type check: org.springframework.beans.factory.BeanCreationException: Error creating bean with name
//		Springs.initApplicationIfNotInitialized(applicationContext);
		
		/*if(interfaceClass.getSimpleName().startsWith("WechatOauth2")){
			System.out.println("test");
		}*/
		/*MethodInterceptor apiClient = createApiMethodInterceptor();
		MixinableInterfaceCreator mixinableCreator = SpringMixinableInterfaceCreator.classNamePostfixMixin(interfaceClass);
		this.apiObject = mixinableCreator.createMixinObject(apiClient);*/
	}
	

	abstract protected MethodInterceptor createApiMethodInterceptor();

	@Override
	public Object getObject() throws Exception {
		Object apiObject = this.apiObject;
		if(apiObject==null){
			Springs.initApplicationIfNotInitialized(applicationContext);
			MethodInterceptor apiClient = createApiMethodInterceptor();
			MixinableInterfaceCreator mixinableCreator = SpringMixinableInterfaceCreator.classNamePostfixMixin(interfaceClass);
			apiObject = mixinableCreator.createMixinObject(apiClient);
			this.apiObject = apiObject;
		}
		return apiObject;
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
		
		protected Object doInvoke(MethodInvocation invocation, M invokeMethod) {
			Object[] args = processArgumentsBeforeRequest(invocation, invokeMethod);
			invokeMethod.validateArgements(validatorWrapper, args);

			RequestContextData context = createRequestContextData(args, invokeMethod);
			if(logger.isInfoEnabled()){
				logger.info("rest url : {} - {}, urlVariables: {}", context.getHttpMethod(), context.getRequestUrl(), context.getUriVariables());
			}
			
			try {
				ResponseEntity<Object> responseEntity = restExecutor.execute(context);
				Object response = responseHandler.handleResponse(invokeMethod, responseEntity, context.getResponseType());
				return response;
			} catch (ApiClientException e) {
				throw e;
			}catch (Exception e) {
				throw new ApiClientException(ApiClientErrors.EXECUTE_REST_ERROR, invokeMethod.getMethod(), e);
			}
		}
		
		protected Object[] processArgumentsBeforeRequest(MethodInvocation invocation, M invokeMethod){
			Object[] args = invocation.getArguments();
			for(ApiClientMethodParameter param : invokeMethod.getParameters()){
				if(param.isInjectProperties()){
					SpringUtils.injectAndInitialize(applicationContext, args[param.getParameterIndex()]);
				}
			}
			return args;
		}
		
		protected RequestContextData createRequestContextData(Object[] args, M invokeMethod){
			Map<String, ?> uriVariables = invokeMethod.getQueryStringParameters(args);
			RequestMethod requestMethod = invokeMethod.getRequestMethod();
			Class<?> responseType = responseHandler.getActualResponseType(invokeMethod);
			
			RequestContextData context = RequestContextData.builder()
															.requestMethod(requestMethod)
															.uriVariables(uriVariables)
															.responseType(responseType)
															.build();
			
			String actualUrl = getFullPath(invokeMethod.getPath());
			actualUrl = processUrlBeforeRequest(actualUrl, invokeMethod, context);
			context.setRequestUrl(actualUrl);

			context.doWithHeaderCallback(headers->{
				invokeMethod.getAcceptHeader().ifPresent(accept->{
					headers.set(ACCEPT, accept);
				});
				invokeMethod.getContentType().ifPresent(contentType->{
					headers.set(CONTENT_TYPE, contentType);
				});
				if(!LangUtils.isEmpty(invokeMethod.getHeaders())){
					for (String header : invokeMethod.getHeaders()) {
						int index = header.indexOf('=');
						headers.set(header.substring(0, index), header.substring(index + 1).trim());
					}
				}
			})
			.requestBodySupplier(()->{
				return invokeMethod.getRequestBody(args);
			});
			
			return context;
		}
		
		/****
		 * 解释pathvariable参数，并且把所有UriVariables转化为queryString参数
		 * @author wayshall
		 * @param url
		 * @param invokeMethod
		 * @param context
		 * @return
		 */
		protected String processUrlBeforeRequest(final String url, M invokeMethod, RequestContextData context){
			String actualUrl = url;
			if(LangUtils.isNotEmpty(context.getPathVariables())){
				actualUrl = ExpressionFacotry.newStrSubstitutor("{", "}", context.getPathVariables()).replace(actualUrl);
			}
			Map<String, ?> uriVariables = context.getUriVariables();
			if(!uriVariables.isEmpty()){
				String paramString = RestUtils.keysToParamString(uriVariables);
				actualUrl = ParamUtils.appendParamString(actualUrl, paramString);
			}
			return actualUrl;
		}
		
	}
	

}
