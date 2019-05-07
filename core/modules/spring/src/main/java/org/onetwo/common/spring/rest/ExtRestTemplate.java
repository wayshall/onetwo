package org.onetwo.common.spring.rest;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URI;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Consumer;

import org.onetwo.common.apiclient.RequestContextData;
import org.onetwo.common.apiclient.RestExecutor;
import org.onetwo.common.apiclient.convertor.ApiclientJackson2HttpMessageConverter;
import org.onetwo.common.apiclient.convertor.ApiclientJackson2XmlMessageConverter;
import org.onetwo.common.jackson.JsonMapper;
import org.onetwo.common.log.JFishLoggerFactory;
import org.onetwo.common.reflect.BeanToMapConvertor;
import org.onetwo.common.reflect.BeanToMapConvertor.BeanToMapBuilder;
import org.onetwo.common.utils.CUtils;
import org.onetwo.common.utils.ParamUtils;
import org.slf4j.Logger;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpRequest;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.OkHttp3ClientHttpRequestFactory;
import org.springframework.http.converter.AbstractHttpMessageConverter;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.converter.xml.MappingJackson2XmlHttpMessageConverter;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RequestCallback;
import org.springframework.web.client.ResponseExtractor;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import net.jodah.typetools.TypeResolver;

public class ExtRestTemplate extends RestTemplate implements RestExecutor {
	
	static private final Logger logger = JFishLoggerFactory.getLogger(ExtRestTemplate.class);
	
	private BeanToMapConvertor beanToMapConvertor = BeanToMapBuilder.newBuilder().enableUnderLineStyle().build();
	
	@SuppressWarnings("rawtypes")
	private ExtRestErrorHandler extErrorHandler;
	private Type extErrorResultType;
	
	private AtomicLong requestIdGenerator = new AtomicLong(0);
	
	private Charset charset = FormHttpMessageConverter.DEFAULT_CHARSET;

	public ExtRestTemplate(){
		this(RestUtils.isOkHttp3Present()?new OkHttp3ClientHttpRequestFactory():null);
	}
	

	public void setExtErrorHandler(ExtRestErrorHandler<?> extErrorHandler) {
//		this.extErrorResultType = GenericTypeResolver.resolveTypeArgument(ExtRestErrorHandler.class, extErrorHandler.getClass());
		this.extErrorResultType = TypeResolver.resolveRawArgument(ExtRestErrorHandler.class, extErrorHandler.getClass());
		this.extErrorHandler = extErrorHandler;
	}
	
	public ExtRestTemplate(ClientHttpRequestFactory requestFactory){
		super();
		/*CUtils.findByClass(this.getMessageConverters(), MappingJackson2HttpMessageConverter.class)
				.ifPresent(p->{
					MappingJackson2HttpMessageConverter convertor = p.getValue();
					convertor.setObjectMapper(JsonMapper.IGNORE_NULL.getObjectMapper());
					convertor.setSupportedMediaTypes(Arrays.asList(MediaType.APPLICATION_JSON, 
																	MediaType.APPLICATION_JSON_UTF8,
																	MediaType.TEXT_PLAIN));
				});*/
		CUtils.replaceOrAdd(getMessageConverters(), MappingJackson2HttpMessageConverter.class, new ApiclientJackson2HttpMessageConverter());
		CUtils.replaceOrAdd(getMessageConverters(), MappingJackson2XmlHttpMessageConverter.class, new ApiclientJackson2XmlMessageConverter());
		
		applyDefaultCharset();
		
		if(requestFactory!=null){
			this.setRequestFactory(requestFactory);
//			this.setRequestFactory(new HttpComponentsClientHttpRequestFactory());
		}
		this.setErrorHandler(new OnExtRestErrorHandler());
	}
	

	private void applyDefaultCharset() {
		for (HttpMessageConverter<?> candidate : this.getMessageConverters()) {
			if (candidate instanceof AbstractHttpMessageConverter) {
				AbstractHttpMessageConverter<?> converter = (AbstractHttpMessageConverter<?>) candidate;
				if (converter.getDefaultCharset() != null) {
					converter.setDefaultCharset(this.charset);
				}
			}
		}
	}
	
	@Override
	public String requestId() {
		return String.valueOf(requestIdGenerator.getAndIncrement());
	}

	public final ExtRestTemplate addMessageConverters(HttpMessageConverter<?>... elements){
		getMessageConverters().addAll(Arrays.asList(elements));
		return this;
	}
	
	public final ExtRestTemplate replaceOrAddMessageConverter(Class<? extends HttpMessageConverter<?>> targetClass, HttpMessageConverter<?> element){
		CUtils.replaceOrAdd(getMessageConverters(), targetClass, element);
		return this;
	}

	public void setBeanToMapConvertor(BeanToMapConvertor beanToMapConvertor) {
		this.beanToMapConvertor = beanToMapConvertor;
	}
	
	@Override
	public <T> ResponseEntity<T> execute(RequestContextData context) {
		try {
			RestExecuteThreadLocal.set(context);
			return doExecute(context);
		} finally {
			RestExecuteThreadLocal.remove();
		}
	}
	
	protected <T> ResponseEntity<T> doExecute(RequestContextData context) {
		RequestCallback rc = null;
		HttpMethod method = context.getHttpMethod();
		ResponseExtractor<ResponseEntity<T>> responseExtractor = null;
		HttpHeaders headers = null;
		HttpEntity<?> requestEntity = null;
		
		if(method==HttpMethod.GET) {
//				rc = super.acceptHeaderRequestCallback(context.getResponseType());
//				responseExtractor = responseEntityExtractor(context.getResponseType());
			//根据consumers 设置header，以指定messageConvertor
			headers = new HttpHeaders();
			context.getHeaderCallback().accept(headers);
			requestEntity = new HttpEntity<>(headers);
			
			rc = super.httpEntityCallback(requestEntity, context.getResponseType());
			responseExtractor = responseEntityExtractor(context.getResponseType());
		}else if(RestUtils.isRequestBodySupportedMethod(method)){
			headers = new HttpHeaders();
			context.getHeaderCallback().accept(headers);
			//根据consumers 设置header，以指定messageConvertor
//			Object requestBody = context.getRequestBodySupplier().get();
			Object requestBody = context.getRequestBodySupplier().getRequestBody(context);
			logData(requestBody);
			requestEntity = new HttpEntity<>(requestBody, headers);
			
			rc = super.httpEntityCallback(requestEntity, context.getResponseType());
			responseExtractor = responseEntityExtractor(context.getResponseType());
		}else{
			throw new RestClientException("unsupported method: " + method);
		}
		if(context.getHeaderCallback()!=null){
			rc = wrapRequestCallback(context, rc);
		}
		return execute(context.getRequestUrl(), method, rc, responseExtractor, context.getUriVariables());
	}
	
	private void logData(Object requestBody) {
		if(requestBody!=null && logger.isDebugEnabled()){
			//打印时不能使用toJson，会破坏某些特殊对象，比如resource
			try {
				logger.debug("requestBody for json: {}", JsonMapper.IGNORE_NULL.toJson(requestBody));
			} catch (Exception e) {
				// ignore
				logger.debug("requestBody {} : {}", requestBody.getClass(), requestBody);
			}
		}
	}
	
	@Override
	protected <T> T doExecute(URI url, HttpMethod method, RequestCallback requestCallback, ResponseExtractor<T> responseExtractor) throws RestClientException {
		RequestContextData ctx = RestExecuteThreadLocal.get();
		if(logger.isDebugEnabled()){
			logger.debug("rest requestId[{}] : {} - {}", ctx.getRequestId(), method, url);
		}
		return super.doExecute(url, method, requestCallback, responseExtractor);
	}
	
	public <T> RequestCallback wrapRequestCallback(RequestContextData context, RequestCallback acceptHeaderRequestCallback) {
		return new ProcessHeaderRequestCallback(context, acceptHeaderRequestCallback);
	}
	

	public <T> ResponseEntity<T> getForEntity(String url, Class<T> responseType, RequestCallback requestCallback, Map<String, ?> uriVariables) throws RestClientException {
		ResponseExtractor<ResponseEntity<T>> responseExtractor = responseEntityExtractor(responseType);
		return execute(url, HttpMethod.GET, requestCallback, responseExtractor, uriVariables);
	}
	

	public <T> T post(String url, Object request, Class<T> responseType){
		ResponseEntity<T> response = postForEntity(url, RestUtils.createFormEntity(request, beanToMapConvertor), responseType);
		if(HttpStatus.OK.equals(response.getStatusCode())){
			return response.getBody();
		}
		throw new RestClientException("invoke rest interface["+url+"] error: " + response);
	}

	public <T> T get(String url, Object request, Class<T> responseType){
		String paramString = RestUtils.propertiesToParamString(request);
		MultiValueMap<String, Object> urlVariables = RestUtils.toMultiValueMap(request, beanToMapConvertor);
		String atualUrl = ParamUtils.appendParamString(url, paramString);
		return getForObject(atualUrl, responseType, urlVariables);
	}

	public <T> T get(String url, Class<T> responseType, Object...urlVariables){
		ResponseEntity<T> response = getForEntity(url, responseType, urlVariables);
		if(HttpStatus.OK.equals(response.getStatusCode())){
			return response.getBody();
		}
		throw new RestClientException("invoke rest interface["+url+"] error: " + response);
	}

	protected static class ProcessHeaderRequestCallback implements RequestCallback {
		private final RequestCallback acceptHeaderRequestCallback;
		private final Consumer<HttpHeaders> callback;
		private RequestContextData context;

		public ProcessHeaderRequestCallback(RequestContextData context, RequestCallback acceptHeaderRequestCallback) {
			super();
			this.acceptHeaderRequestCallback = acceptHeaderRequestCallback;
			this.context = context;
			this.callback = context.getHeaderCallback();
		}

		@Override
		public void doWithRequest(ClientHttpRequest request) throws IOException {
			//再次回调header callback，此时为实际请求的header，前一次调用为匹配messageConverter
			this.callback.accept(request.getHeaders());
			/*if(ReflectUtils.getFieldsAsMap(acceptHeaderRequestCallback.getClass()).containsKey("requestEntity")){
				HttpEntity<?> requestEntity = (HttpEntity<?>) ReflectUtils.getFieldValue(acceptHeaderRequestCallback, "requestEntity");
				if(requestEntity!=null){
					this.callback.accept(requestEntity.getHeaders());
				}
			}*/
			if(logger.isDebugEnabled()){
				logger.debug("requestId[{}] header: {}", context.getRequestId(), request.getHeaders());
			}
			this.acceptHeaderRequestCallback.doWithRequest(request);
		}
	}
	

	@SuppressWarnings("unchecked")
	//AccessTokenErrorHandler
	public class OnExtRestErrorHandler extends DefaultResponseErrorHandler {
		@Override
		public void handleError(ClientHttpResponse response) throws IOException {
			ResponseExtractor<ResponseEntity<Object>> responseExtractor = null;
			if(extErrorResultType!=null){
				responseExtractor = responseEntityExtractor(extErrorResultType);
			}
			if (responseExtractor != null) {
				ResponseEntity<?> errorData = responseExtractor.extractData(response);
				extErrorHandler.onError(errorData.getBody());
			}else{
				super.handleError(response);
			}
		}
	}

	public void setCharset(Charset charset) {
		this.charset = charset;
	}
	
	
}
