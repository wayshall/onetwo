package org.onetwo.common.apiclient.impl;

import java.lang.reflect.Method;
import java.util.Map;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.onetwo.common.apiclient.ApiClientMethod;
import org.onetwo.common.apiclient.ApiClientMethod.ApiClientMethodParameter;
import org.onetwo.common.apiclient.ApiClientResponseHandler;
import org.onetwo.common.apiclient.ApiErrorHandler;
import org.onetwo.common.apiclient.ApiErrorHandler.ErrorInvokeContext;
import org.onetwo.common.apiclient.CustomResponseHandler;
import org.onetwo.common.apiclient.RequestContextData;
import org.onetwo.common.apiclient.RestExecutor;
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
	protected ApiClientResponseHandler<M> responseHandler;
	protected ApiErrorHandler apiErrorHandler;
	protected ApplicationContext applicationContext;
	/***
	 * 默认不重试
	 */
	protected int maxRetryCount = 0;
	protected int retryWaitInMillis = 500;
	
	final public void setResponseHandler(ApiClientResponseHandler<M> responseHandler) {
		this.responseHandler = responseHandler;
	}

	public void setApiErrorHandler(ApiErrorHandler apiErrorHandler) {
		this.apiErrorHandler = apiErrorHandler;
	}

	public void setMaxRetryCount(int maxRetryCount) {
		this.maxRetryCount = maxRetryCount;
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		Assert.notNull(restExecutor, "restExecutor can not be null");
		if (responseHandler == null) {
			responseHandler = new DefaultApiClientResponseHandler<M>();
		}
		if (apiErrorHandler == null) {
			apiErrorHandler = ApiErrorHandler.DEFAULT;
		}
		
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

			RequestContextData context = createRequestContextData(invocation, args, invokeMethod);
			return this.actualInvoke0(invokeMethod, context);
		}

		@SuppressWarnings({ "rawtypes", "unchecked" })
		protected Object actualInvoke0(M invokeMethod, RequestContextData context) {
			try {
				context.increaseInvokeCount(1);
				Object response;
				CustomResponseHandler<?> customHandler = invokeMethod.getCustomResponseHandler();
				if(customHandler!=null){
//					errorHanlder = customHandler;
					context.setResponseType(customHandler.getResponseType());
					ResponseEntity responseEntity = invokeRestExector(context);
					response = customHandler.handleResponse(invokeMethod, responseEntity);
				}else{
					ResponseEntity responseEntity = invokeRestExector(context);
					response = responseHandler.handleResponse(invokeMethod, responseEntity, context.getResponseType());
				}
				return response;
			}
			catch (Throwable e) {
				// /ocketException, UnknownHostException等网络异常
				ApiErrorHandler handler = invokeMethod.getApiErrorHandler().orElse(apiErrorHandler);
				ErrorInvokeContext ctx = new ErrorInvokeContext(context, e);
				ctx.setRetryInvoker(ioe -> {
					logger.warn("{} times invoke throws io error: {}, retry after {} ms ...", context.getInvokeCount(), ioe.getMessage(), retryWaitInMillis);
					if (retryWaitInMillis>0) {
						LangUtils.awaitInMillis(retryWaitInMillis); //休眠500毫秒
					}
					Object res = actualInvoke0(invokeMethod, context);
					return res;
				});
				return handler.handleError(ctx);
			}
		}

		@SuppressWarnings({ "rawtypes" })
		private ResponseEntity invokeRestExector(RequestContextData context){
			//WechatApiClientFactoryBean logger
			if(logger.isDebugEnabled()){
				logger.debug("rest url : {} - {}, urlVariables: {}", context.getHttpMethod(), context.getRequestUrl(), context.getUriVariables());
			}
			ResponseEntity responseEntity = restExecutor.execute(context);
			return responseEntity;
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
		
		protected RequestContextData createRequestContextData(MethodInvocation invocation, Object[] args, M invokeMethod){
			Map<String, ?> queryParameters = invokeMethod.getQueryStringParameters(args);
			Map<String, Object> uriVariables = invokeMethod.getUriVariables(args);
			uriVariables.putAll(queryParameters);
			RequestMethod requestMethod = invokeMethod.getRequestMethod();
			Class<?> responseType = responseHandler.getActualResponseType(invokeMethod);
			
			RequestContextData context = RequestContextData.builder()
															.requestId(restExecutor.requestId())
															.requestMethod(requestMethod)
															.uriVariables(uriVariables)
															.queryParameters(queryParameters)
															.responseType(responseType)
															.methodArgs(args)
															.invokeMethod(invokeMethod)
															.invocation(invocation)
															.maxRetryCount(maxRetryCount)
															.build();
			
			String actualUrl = getFullPath(invokeMethod.getPath());
			actualUrl = processUrlBeforeRequest(actualUrl, invokeMethod, context);
			context.setRequestUrl(actualUrl);

			context.doWithHeaderCallback(headers->{
				if (!invokeMethod.getAcceptHeaders().isEmpty()) {
					headers.set(ACCEPT, StringUtils.join(invokeMethod.getAcceptHeaders(), ","));
				}
				//HttpEntityRequestCallback#doWithRequest -> requestContentType
				invokeMethod.getContentType().ifPresent(contentType->{
					headers.set(CONTENT_TYPE, contentType);
				});
				if(!LangUtils.isEmpty(invokeMethod.getHeaders())){
					for (String header : invokeMethod.getHeaders()) {
						int index = header.indexOf('=');
						headers.set(header.substring(0, index), header.substring(index + 1).trim());
					}
				}
				invokeMethod.getHttpHeaders(args)
							.ifPresent(h->{
								headers.putAll(h);
							});
				//回调
				invokeMethod.getApiHeaderCallback(args)
							.ifPresent(c->c.onHeader(headers));
			})
			.requestBodySupplier(ctx->{
				return invokeMethod.getRequestBody(args);
			});
			
			return context;
		}
		
		/****
		 * 解释pathvariable参数，并且把所有queryParameters转化为queryString参数
		 * @author wayshall
		 * @param url
		 * @param invokeMethod
		 * @param context
		 * @return
		 */
		protected String processUrlBeforeRequest(final String url, M invokeMethod, RequestContextData context){
			String actualUrl = url;
			/*if(LangUtils.isNotEmpty(context.getPathVariables())){
				actualUrl = ExpressionFacotry.newStrSubstitutor("{", "}", context.getPathVariables()).replace(actualUrl);
			}*/
			Map<String, ?> queryParameters = context.getQueryParameters();
			if(!queryParameters.isEmpty()){
				String paramString = RestUtils.keysToParamString(queryParameters);
				actualUrl = ParamUtils.appendParamString(actualUrl, paramString);
			}
			return actualUrl;
		}
		
	}
	

}
