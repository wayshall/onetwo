package org.onetwo.common.apiclient;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;
import org.onetwo.common.annotation.FieldName;
import org.onetwo.common.apiclient.ApiClientMethod.ApiClientMethodParameter;
import org.onetwo.common.apiclient.ApiErrorHandler.DefaultErrorHandler;
import org.onetwo.common.apiclient.CustomResponseHandler.NullHandler;
import org.onetwo.common.apiclient.annotation.InjectProperties;
import org.onetwo.common.apiclient.annotation.ResponseHandler;
import org.onetwo.common.apiclient.utils.ApiClientConstants.ApiClientErrors;
import org.onetwo.common.apiclient.utils.ApiClientUtils;
import org.onetwo.common.exception.ApiClientException;
import org.onetwo.common.exception.BaseException;
import org.onetwo.common.proxy.AbstractMethodResolver;
import org.onetwo.common.proxy.BaseMethodParameter;
import org.onetwo.common.reflect.BeanToMapConvertor;
import org.onetwo.common.reflect.BeanToMapConvertor.BeanToMapBuilder;
import org.onetwo.common.reflect.ReflectUtils;
import org.onetwo.common.spring.SpringUtils;
import org.onetwo.common.spring.Springs;
import org.onetwo.common.spring.rest.RestUtils;
import org.onetwo.common.utils.LangUtils;
import org.slf4j.Logger;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ValueConstants;
import org.springframework.web.client.RestClientException;
import org.springframework.web.multipart.MultipartFile;

/**
 * 支持  @PathVariable @RequestBody @RequestParam 注解
 * @PathVariable：解释路径参数
 * @RequestBody：body，一般会转为json，一次请求 只允许一个requestbody
 * @RequestParam：转化为queryString参数
 * 没有注解的方法参数：如果为get请求，则所有参数都转为queryString参数，效果和使用了@RequestParam一样；
 * 					 如果为post请求，则自动包装为类型为Map的requestBody
 * 
 * 如果没有指定requestBody，则根据规则查找可以作为requestBody的参数
 * 方法多于一个参数时，使用参数名称作为参数前缀；
 * 只有一个参数的时候，除非用@RequestParam等注解指定了参数名称前缀，否则前缀为空，直接把对象转化为map作为键值对参数
 * 
 * 
 * get请求忽略requestBody
 * post请求会把非url参数转化为requestBody
 * 
 * consumes -> contentType，指定提交请求的convertor，详见：HttpEntityRequestCallback
 * produces -> acceptHeader，指定accept header，从而通过response的contentType头指定读取响应数据的convertor，详见：ResponseEntityResponseExtractor
 * 
 * 值转换器：ValueConvertor
 * HttpEntityRequestCallback
 * 
 * @author wayshall
 * <br/>
 */
public class ApiClientMethod extends AbstractMethodResolver<ApiClientMethodParameter> {
	
	public static BeanToMapConvertor getBeanToMapConvertor() {
		return beanToMapConvertor;
	}

	final static private BeanToMapConvertor beanToMapConvertor = BeanToMapBuilder.newBuilder()
																	.enableFieldNameAnnotation()
																	.valueConvertor(new ValueConvertor())
																	.flatableObject(obj->{
																		boolean flatable = BeanToMapConvertor.DEFAULT_FLATABLE.apply(obj);
																		return  flatable &&
																				!Resource.class.isInstance(obj) &&
																				!byte[].class.isInstance(obj) &&
																				!MultipartFile.class.isInstance(obj);
																	})
																	.build();
	
//	final private AnnotationAttributes requestMappingAttributes;
	private RequestMethod requestMethod;
	private String path;
	private Class<?> componentType;

	private List<String> acceptHeaders;
	private List<MediaType> consumeMediaTypes;
	/***
	 * resttemplate会根据contentType（consumes）决定采用什么样的httpMessageConvertor
	 * 详见HttpEntityRequestCallback#doWithRequest -> requestContentType
	 */
	private Optional<String> contentType;
	private String[] headers;
	private int apiHeaderCallbackIndex = -1;
	private int headerParameterIndex = -1;
	
	private CustomResponseHandler<?> customResponseHandler;
	private ApiErrorHandler apiErrorHandler;
	
	public ApiClientMethod(Method method) {
		super(method);
		componentType = ReflectUtils.getGenricType(method.getGenericReturnType(), 0, null);
	}
	
	public void initialize(){
		if(!ApiClientUtils.isRequestMappingPresent()){
			throw new ApiClientException(ApiClientErrors.REQUEST_MAPPING_NOT_FOUND);
		}
		Optional<AnnotationAttributes> requestMapping = SpringUtils.getAnnotationAttributes(method, RequestMapping.class);
		if(!requestMapping.isPresent()){
			throw new ApiClientException(ApiClientErrors.REQUEST_MAPPING_NOT_FOUND, method, null);
		}
		
		AnnotationAttributes reqestMappingAttrs = requestMapping.get();
		//SpringMvcContract#parseAndValidateMetadata
		this.acceptHeaders = Arrays.asList(reqestMappingAttrs.getStringArray("produces"));
		String[] consumes = reqestMappingAttrs.getStringArray("consumes");
		this.contentType = LangUtils.getFirstOptional(consumes);
		this.consumeMediaTypes = Stream.of(consumes).map(MediaType::parseMediaType).collect(Collectors.toList());
		this.headers = reqestMappingAttrs.getStringArray("headers");
		
		String[] paths = reqestMappingAttrs.getStringArray("value");
		path = LangUtils.isEmpty(paths)?"":paths[0];
		
		RequestMethod[] methods = (RequestMethod[])reqestMappingAttrs.get("method");
		requestMethod = LangUtils.isEmpty(methods)?RequestMethod.GET:methods[0];

		findParameterByType(ApiHeaderCallback.class).ifPresent(p->{
			this.apiHeaderCallbackIndex = p.getParameterIndex();
		});
		findParameterByType(HttpHeaders.class).ifPresent(p->{
			this.headerParameterIndex = p.getParameterIndex();
		});
		

		ResponseHandler resHandler = AnnotatedElementUtils.getMergedAnnotation(getMethod(), ResponseHandler.class);
		if (resHandler==null){
			resHandler = AnnotatedElementUtils.getMergedAnnotation(getDeclaringClass(), ResponseHandler.class);
		}
		if (resHandler!=null){
			Class<? extends CustomResponseHandler<?>> handlerClass = resHandler.value();
			if (handlerClass!=NullHandler.class) {
				CustomResponseHandler<?> customHandler = createAndInitComponent(resHandler.value());
				this.customResponseHandler = customHandler;
			}
			
			Class<? extends ApiErrorHandler> errorHandlerClass = resHandler.errorHandler();
			if (errorHandlerClass==DefaultErrorHandler.class) {
				this.apiErrorHandler = obtainDefaultApiErrorHandler();
			} else {
				this.apiErrorHandler = createAndInitComponent(errorHandlerClass);
			}
		} else {
			this.apiErrorHandler = obtainDefaultApiErrorHandler();
		}
	}
	
	protected ApiErrorHandler obtainDefaultApiErrorHandler() {
		return null;
	}
	
	private <T> T createAndInitComponent(Class<T> clazz) {
		T component = null;
		if (Springs.getInstance().isInitialized()){
			component = Springs.getInstance().getBean(clazz);
			if (component==null) {
				component = ReflectUtils.newInstance(clazz);
				SpringUtils.injectAndInitialize(Springs.getInstance().getAppContext(), component);
			}
		} else {
			Logger logger = ApiClientUtils.getApiclientlogger();
			if (logger.isWarnEnabled()) {
				logger.warn("spring application not initialized, use reflection to create component: {}", clazz);
			}
			component = ReflectUtils.newInstance(clazz);
		}
		return component;
	}
	
	public CustomResponseHandler<?> getCustomResponseHandler() {
		return customResponseHandler;
	}
	public Optional<ApiErrorHandler> getApiErrorHandler() {
		return Optional.ofNullable(apiErrorHandler);
	}

	public Optional<ApiHeaderCallback> getApiHeaderCallback(Object[] args){
		return apiHeaderCallbackIndex<0?Optional.empty():Optional.ofNullable((ApiHeaderCallback)args[apiHeaderCallbackIndex]);
	}
	
	public Optional<HttpHeaders> getHttpHeaders(Object[] args){
		return headerParameterIndex<0?Optional.empty():Optional.ofNullable((HttpHeaders)args[headerParameterIndex]);
	}

	public String[] getHeaders() {
		return headers;
	}

	public List<String> getAcceptHeaders() {
		return acceptHeaders;
	}
	public List<MediaType> getProduceMediaTypes() {
		return acceptHeaders.stream().map(accept -> {
			return MediaType.parseMediaType(accept);
		}).collect(Collectors.toList());
	}

	public List<MediaType> getConsumeMediaTypes() {
		return consumeMediaTypes;
	}

	public Optional<String> getContentType() {
		return contentType;
	}

	public Map<String, Object> getUriVariables(Object[] args){
		if(LangUtils.isEmpty(args)){
			return Collections.emptyMap();
		}
		
		List<ApiClientMethodParameter> urlVariableParameters = parameters.stream()
												.filter(p->{
													return isUriVariables(p);
												})
												.collect(Collectors.toList());

//		boolean parameterNameAsPrefix = urlVariableParameters.size()>1;
		Map<String, Object> values = toMap(urlVariableParameters, args).toSingleValueMap();
		
		return values;
	}

	public Map<String, ?> getQueryStringParameters(Object[] args){
		if(LangUtils.isEmpty(args)){
			return Collections.emptyMap();
		}
		
		List<ApiClientMethodParameter> queryParameters = parameters.stream()
												.filter(p->isQueryStringParameters(p))
												.collect(Collectors.toList());

		boolean parameterNameAsPrefix = queryParameters.size()>1;
		Map<String, ?> values = toMap(queryParameters, args, parameterNameAsPrefix).toSingleValueMap();
		
		return values;
	}
	
	protected boolean isQueryStringParameters(ApiClientMethodParameter p){
		if(requestMethod==RequestMethod.POST || requestMethod==RequestMethod.PATCH){
			//post方法，使用了RequestParam才转化为queryString
			return p.hasParameterAnnotation(RequestParam.class);
		}
		return !isUriVariables(p) && !isSpecalPemerater(p);
	}
	
	protected boolean isUriVariables(ApiClientMethodParameter p){
		return p.hasParameterAnnotation(PathVariable.class);
	}

	public Map<String, ?> getPathVariables(Object[] args){
		if(LangUtils.isEmpty(args)){
			return Collections.emptyMap();
		}
		
		List<ApiClientMethodParameter> pathVariables = parameters.stream()
												.filter(p->{
													return p.hasParameterAnnotation(PathVariable.class);
												})
												.collect(Collectors.toList());
		/*Object pvalue = null;
		for(ApiClientMethodParameter parameter : pathVariables){
			pvalue = args[parameter.getParameterIndex()];
			handleArg(values, parameter, pvalue);
		}*/
		Map<String, ?> values = toMap(pathVariables, args).toSingleValueMap();
		
		return values;
	}
	
	public Object getRequestBody(Object[] args){
		if(!RestUtils.isRequestBodySupportedMethod(requestMethod)){
			throw new RestClientException("unsupported request body method: " + method);
		}
		List<ApiClientMethodParameter> requestBodyParameters = parameters.stream()
												.filter(p->{
													return p.hasParameterAnnotation(RequestBody.class);
												})
												.collect(Collectors.toList());
		if(requestBodyParameters.isEmpty()){
			//如果没有使用RequestBody注解的参数
//			Object values = toMap(parameters, args);
			Object values = null;
			if(LangUtils.isEmpty(args)){
				return null;
			}
			
			//如果没有指定requestBody，则根据规则查找可以作为requestBody的参数
			List<ApiClientMethodParameter> bodyableParameters = parameters.stream()
					.filter(p->!isSpecalPemerater(p) && !isQueryStringParameters(p))
					.collect(Collectors.toList());
//			List<ApiClientMethodParameter> bodyableParameters = parameters;
			
			if(LangUtils.isEmpty(bodyableParameters)){
				return null;
			}
			
//			boolean parameterNameAsPrefix = bodyableParameters.size()>1; //大于一个参数时，使用参数名称作为参数前缀
			boolean parameterNameAsPrefix = false; //修改为默认不使用前缀 
			//没有requestBody注解时，根据contentType做简单的转换策略
			if(getContentType().isPresent()){
				String contentType = getContentType().get();
				MediaType consumerMediaType = MediaType.parseMediaType(contentType);
				if(MediaType.APPLICATION_FORM_URLENCODED.equals(consumerMediaType) ||
						MediaType.MULTIPART_FORM_DATA.equals(consumerMediaType)){
					//form的话，需要转成multipleMap
					values = toMap(bodyableParameters, args, parameterNameAsPrefix);
				}else{
					//如contentType为json之类，则转成单值的map
//					values = args.length==1?args[0]:toMap(parameters, args).toSingleValueMap();
					values = args.length==1?args[0]:toMap(bodyableParameters, args, parameterNameAsPrefix).toSingleValueMap();
//					values = args.length==1?args[0]:toMap(parameters, args);
//					values = toMap(parameters, args);
				}
			}else{
				//默认为form
				values = toMap(bodyableParameters, args, parameterNameAsPrefix);
			}
			return values;
		}else if(requestBodyParameters.size()==1){
			return args[requestBodyParameters.get(0).getParameterIndex()];
		}else{
			throw new ApiClientException(ApiClientErrors.REQUEST_BODY_ONLY_ONCE, method, null);
		}
		/*if(requestBodyParameter.isPresent()){
			return args[requestBodyParameter.get().getParameterIndex()];
		}
		return null;*/
	}
	

	protected MultiValueMap<String, Object> toMap(List<ApiClientMethodParameter> methodParameters, Object[] args){
		return toMap(methodParameters, args, true);
	}
	protected MultiValueMap<String, Object> toMap(List<ApiClientMethodParameter> methodParameters, Object[] args, boolean parameterNameAsPrefix){
		MultiValueMap<String, Object> values = new LinkedMultiValueMap<>(methodParameters.size());
		for(ApiClientMethodParameter parameter : methodParameters){
			//忽略特殊参数
			if(isSpecalPemerater(parameter)){
				continue;
			}
			Object pvalue = args[parameter.getParameterIndex()];
			handleArg(values, parameter, pvalue, parameterNameAsPrefix);
		}
		return values;
	}
	
	protected boolean isSpecalPemerater(ApiClientMethodParameter parameter){
		return parameter.getParameterIndex()==apiHeaderCallbackIndex || parameter.getParameterIndex()==headerParameterIndex;
	}
	
	protected void handleArg(MultiValueMap<String, Object> values, ApiClientMethodParameter mp, final Object pvalue, boolean parameterNameAsPrefix){
		if(pvalue instanceof ApiArgumentTransformer){
			Object val = ((ApiArgumentTransformer)pvalue).asApiValue();
			values.add(mp.getParameterName(), val);
			return ;
		}
		
		String prefix = "";
		Object paramValue = pvalue;
		//下列情况，强制使用名称作为前缀
		if(mp.hasParameterAnnotation(RequestParam.class)){
			RequestParam params = mp.getParameterAnnotation(RequestParam.class);
			if(pvalue==null && params.required() && (paramValue=params.defaultValue())==ValueConstants.DEFAULT_NONE){
				throw new BaseException("parameter["+params.name()+"] must be required : " + mp.getParameterName());
			}
			parameterNameAsPrefix = true;
		}else if(isUriVariables(mp) || mp.hasParameterAnnotation(FieldName.class)){
			parameterNameAsPrefix = true;
		}else if(beanToMapConvertor.isMappableValue(pvalue)){//可直接映射为值的参数
			parameterNameAsPrefix = true;
		}
		
		if(parameterNameAsPrefix){
			prefix = mp.getParameterName();
		}
		beanToMapConvertor.flatObject(prefix, paramValue, (k, v, ctx)->{
			values.add(k, v);
		});
		
		/*if(falatable){
//			beanToMapConvertor.flatObject(mp.getParameterName(), paramValue, (k, v, ctx)->{
			beanToMapConvertor.flatObject(mp.getParameterName(), paramValue, (k, v, ctx)->{
				if(v instanceof Enum){
					Enum<?> e = (Enum<?>)v;
					if(e instanceof ValueEnum){
						v = ((ValueEnum<?>)e).getValue();
					}else{//默认使用name
						v = e.name();
					}
				}else if(v instanceof Resource){
					//ignore，忽略，不转为string
				}else{
					v = v.toString();
				}
				if(ctx!=null){
//					System.out.println("ctx.getName():"+ctx.getName());
					values.add(ctx.getName(), v);
				}else{
					values.add(k, v);
				}
//				values.add(k, v);
			});
		}else{
			values.add(mp.getParameterName(), pvalue);
		}*/
	}

	public String getPath() {
		return path;
	}

	public RequestMethod getRequestMethod() {
		return requestMethod;
	}

	public Class<?> getComponentType() {
		return componentType;
	}


	@Override
	protected ApiClientMethodParameter createMethodParameter(Method method, int parameterIndex, Parameter parameter) {
		return new ApiClientMethodParameter(method, parameter, parameterIndex);
	}

	public static class ApiClientMethodParameter extends BaseMethodParameter {

		private boolean injectProperties;
		
		public ApiClientMethodParameter(Method method, Parameter parameter, int parameterIndex) {
			super(method, parameter, parameterIndex);
			this.injectProperties = parameter.isAnnotationPresent(InjectProperties.class);
		}

		@Override
		public String obtainParameterName() {
			/*String pname = super.getParameterName();
			if(StringUtils.isNotBlank(pname)){
				return pname;
			}*/
			String pname = getOptionalParameterAnnotation(RequestParam.class).map(rp->rp.value()).orElseGet(()->{
//				return getOptionalParameterAnnotation(PathVariable.class).map(pv->pv.value()).orElse(null);
				return getOptionalParameterAnnotation(PathVariable.class).map(pv->pv.value()).orElseGet(()->{
					return getOptionalParameterAnnotation(FieldName.class).map(fn->fn.value()).orElse(null);
				});
			});
			// 如果没有找到命名的注解	
			if(StringUtils.isBlank(pname)){
				/*pname = getOptionalParameterAnnotation(PathVariable.class).map(pv->pv.value()).orElse(null);
				
				if(StringUtils.isNotBlank(pname)){
					return pname;
				}*/

				if(parameter!=null && parameter.isNamePresent()){
					pname = parameter.getName();
				}else{
					pname = String.valueOf(getParameterIndex());
				}
			}
//			this.setParameterName(pname);
			return pname;
		}

		public boolean isInjectProperties() {
			return injectProperties;
		}
	}

}
