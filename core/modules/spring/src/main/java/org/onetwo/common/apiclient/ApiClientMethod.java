package org.onetwo.common.apiclient;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.onetwo.common.apiclient.ApiClientConstants.ApiClientError;
import org.onetwo.common.apiclient.ApiClientMethod.ApiClientMethodParameter;
import org.onetwo.common.apiclient.annotation.InjectProperties;
import org.onetwo.common.exception.ApiClientException;
import org.onetwo.common.exception.BaseException;
import org.onetwo.common.proxy.AbstractMethodResolver;
import org.onetwo.common.proxy.BaseMethodParameter;
import org.onetwo.common.reflect.BeanToMapConvertor;
import org.onetwo.common.reflect.BeanToMapConvertor.BeanToMapBuilder;
import org.onetwo.common.reflect.ReflectUtils;
import org.onetwo.common.spring.SpringUtils;
import org.onetwo.common.utils.LangUtils;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ValueConstants;

/**
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
			throw new ApiClientException(ApiClientError.REQUEST_MAPPING_NOT_FOUND);
		}
		Optional<AnnotationAttributes> requestMapping = SpringUtils.getAnnotationAttributes(method, RequestMapping.class);
		if(!requestMapping.isPresent()){
			throw new ApiClientException(ApiClientError.REQUEST_MAPPING_NOT_FOUND, method);
		}
		
		AnnotationAttributes reqestMappingAttrs = requestMapping.get();
		String[] paths = reqestMappingAttrs.getStringArray("value");
		path = LangUtils.isEmpty(paths)?"":paths[0];
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

	public Map<String, Object> toMap(Object[] args){
		if(LangUtils.isEmpty(args)){
			return Collections.emptyMap();
		}
		Map<String, Object> values = LangUtils.newHashMap(parameters.size());
		
		List<ApiClientMethodParameter> urlVariableParameters = parameters.stream()
												.filter(p->{
													return !p.hasParameterAnnotation(RequestBody.class);
												})
												.collect(Collectors.toList());
		Object pvalue = null;
		for(ApiClientMethodParameter parameter : urlVariableParameters){
			pvalue = args[parameter.getParameterIndex()];
			handleArg(values, parameter, pvalue);
		}
		
		return values;
	}
	
	public Object getRequestBody(Object[] args){
		Optional<ApiClientMethodParameter> requestBodyParameter = parameters.stream()
												.filter(p->{
													return p.hasParameterAnnotation(RequestBody.class);
												})
												.findFirst();
		if(requestBodyParameter.isPresent()){
			return args[requestBodyParameter.get().getParameterIndex()];
		}
		return null;
	}
	

	protected void handleArg(Map<String, Object> values, ApiClientMethodParameter mp, final Object pvalue){
		Object paramValue = pvalue;
		if(mp.hasParameterAnnotation(RequestParam.class)){
			RequestParam params = mp.getParameterAnnotation(RequestParam.class);
			if(pvalue==null && params.required() && (paramValue=params.defaultValue())==ValueConstants.DEFAULT_NONE){
				throw new BaseException("parameter must be required : " + mp.getParameterName());
			}
		}
		beanToMapConvertor.flatObject(mp.getParameterName(), paramValue, (k, v, ctx)->{
			if(v instanceof Enum){
				Enum<?> e = (Enum<?>)v;
				v = e.name();
			}
			if(ctx!=null){
				values.put(ctx.getName(), v);
			}else{
				values.put(k, v);
			}
		});
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
			RequestParam name = getParameterAnnotation(RequestParam.class);
			if(name!=null){
				return name.value();
			}else if(parameter!=null && parameter.isNamePresent()){
				return parameter.getName();
			}else{
				return String.valueOf(getParameterIndex());
			}
		}

		public boolean isInjectProperties() {
			return injectProperties;
		}
	}

}
