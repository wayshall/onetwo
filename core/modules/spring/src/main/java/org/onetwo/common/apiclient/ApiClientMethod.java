package org.onetwo.common.apiclient;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.onetwo.common.apiclient.ApiClientMethod.ApiClientMethodParameter;
import org.onetwo.common.apiclient.annotation.InjectProperties;
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
import org.onetwo.common.spring.converter.ValueEnum;
import org.onetwo.common.spring.rest.RestUtils;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.utils.StringUtils;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ValueConstants;
import org.springframework.web.client.RestClientException;

/**
 * 支持  @PathVariable @RequestBody @RequestParam 注解
 * @PathVariable：解释路径参数
 * @RequestBody：body，一般会转为json，一次请求 只允许一个requestbody
 * @RequestParam：转化为queryString参数
 * 没有注解的方法参数：如果为get请求，则所有参数都转为queryString参数，效果和使用了@RequestParam一样；
 * 					 如果为post请求，则自动包装为类型为Map的requestBody
 * 
 * 
 * get请求忽略requestBody
 * post请求会把非url参数转化为requestBody
 * 
 * @author wayshall
 * <br/>
 */
public class ApiClientMethod extends AbstractMethodResolver<ApiClientMethodParameter> {
	private BeanToMapConvertor beanToMapConvertor = BeanToMapBuilder.newBuilder()
																	.enableFieldNameAnnotation()
																	.build();
	
//	final private AnnotationAttributes requestMappingAttributes;
	private RequestMethod requestMethod;
	private String path;
	private Class<?> componentType;
	
	private Optional<String> acceptHeader;
	private Optional<String> contentType;
	private String[] headers;
	
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
		String[] paths = reqestMappingAttrs.getStringArray("value");
		path = LangUtils.isEmpty(paths)?"":paths[0];
		//SpringMvcContract#parseAndValidateMetadata
		this.acceptHeader = LangUtils.getFirstOptional(reqestMappingAttrs.getStringArray("produces"));
		this.contentType = LangUtils.getFirstOptional(reqestMappingAttrs.getStringArray("consumes"));
		this.headers = reqestMappingAttrs.getStringArray("headers");
		
		RequestMethod[] methods = (RequestMethod[])reqestMappingAttrs.get("method");
		requestMethod = LangUtils.isEmpty(methods)?RequestMethod.GET:methods[0];

	}

	public String[] getHeaders() {
		return headers;
	}

	public Optional<String> getAcceptHeader() {
		return acceptHeader;
	}

	public Optional<String> getContentType() {
		return contentType;
	}

	public Map<String, ?> getQueryStringParameters(Object[] args){
		if(LangUtils.isEmpty(args)){
			return Collections.emptyMap();
		}
		
		List<ApiClientMethodParameter> urlVariableParameters = parameters.stream()
												.filter(p->isQueryStringParameters(p))
												.collect(Collectors.toList());
		
		Map<String, ?> values = toMap(urlVariableParameters, args).toSingleValueMap();
		
		return values;
	}
	
	protected boolean isQueryStringParameters(ApiClientMethodParameter p){
		if(requestMethod==RequestMethod.POST || requestMethod==RequestMethod.PATCH){
			//post方法，使用了RequestParam才转化为queryString
			return p.hasParameterAnnotation(RequestParam.class);
		}
		return true;
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
			if(LangUtils.isEmpty(args)){
				return null;
			}else if(args.length==1){
				return args[0];
			}else{
				return toMap(parameters, args);
			}
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
	protected MultiValueMap<String, Object> toMap(List<ApiClientMethodParameter> methodParameters, Object[] args, boolean flatable){
		MultiValueMap<String, Object> values = new LinkedMultiValueMap<>(methodParameters.size());
		for(ApiClientMethodParameter parameter : methodParameters){
			Object pvalue = args[parameter.getParameterIndex()];
			handleArg(values, parameter, pvalue, flatable);
		}
		return values;
	}
	
	protected void handleArg(MultiValueMap<String, Object> values, ApiClientMethodParameter mp, final Object pvalue, boolean flatable){
		Object paramValue = pvalue;
		if(mp.hasParameterAnnotation(RequestParam.class)){
			RequestParam params = mp.getParameterAnnotation(RequestParam.class);
			if(pvalue==null && params.required() && (paramValue=params.defaultValue())==ValueConstants.DEFAULT_NONE){
				throw new BaseException("parameter["+params.name()+"] must be required : " + mp.getParameterName());
			}
		}
		
		if(flatable){
			beanToMapConvertor.flatObject(mp.getParameterName(), paramValue, (k, v, ctx)->{
				if(v instanceof Enum){
					Enum<?> e = (Enum<?>)v;
					if(e instanceof ValueEnum){
						v = ((ValueEnum<?>)e).getValue();
					}else{//默认使用name
						v = e.name();
					}
				}
				if(ctx!=null){
//					System.out.println("ctx.getName():"+ctx.getName());
					values.add(ctx.getName(), v.toString());
				}else{
					values.add(k, v.toString());
				}
	//			values.put(k, v);
			});
		}else{
			values.add(mp.getParameterName(), pvalue);
		}
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
		public String getParameterName() {
			String pname = super.getParameterName();
			if(StringUtils.isNotBlank(pname)){
				return pname;
			}
			pname = getOptionalParameterAnnotation(RequestParam.class).map(rp->rp.value()).orElse(null);
			if(StringUtils.isBlank(pname)){
				pname = getOptionalParameterAnnotation(PathVariable.class).map(pv->pv.value()).orElse(null);
			}
			if(StringUtils.isBlank(pname) && parameter!=null && parameter.isNamePresent()){
				pname = parameter.getName();
			}else{
				pname = String.valueOf(getParameterIndex());
			}
			this.setParameterName(pname);
			return pname;
		}

		public boolean isInjectProperties() {
			return injectProperties;
		}
	}

}
