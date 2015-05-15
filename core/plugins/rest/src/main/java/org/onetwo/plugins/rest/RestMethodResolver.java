package org.onetwo.plugins.rest;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Map;

import org.onetwo.common.exception.BaseException;
import org.onetwo.common.proxy.AbstractMethodResolver;
import org.onetwo.common.proxy.BaseMethodParameter;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.utils.ReflectUtils;
import org.onetwo.common.utils.StringUtils;
import org.onetwo.common.utils.map.CasualMap;
import org.onetwo.common.web.view.jsp.TagUtils;
import org.onetwo.plugins.rest.annotation.QueryName;
import org.onetwo.plugins.rest.annotation.RestClient;
import org.springframework.web.bind.annotation.RequestMethod;

public class RestMethodResolver extends AbstractMethodResolver<BaseMethodParameter> {

	final private RequestMethod requestMethod;
//	final private RestClient restClient;
	private String url;
	private RestUrlParser urlParser;
	final private Class<?> responseType;
	private Class<?> componentClass;
	
	public RestMethodResolver(Method method) {
		super(method);
		RestClient classRestClient = method.getDeclaringClass().getAnnotation(RestClient.class);
		RestClient methodRestClient = method.getAnnotation(RestClient.class);
		if(classRestClient==null && methodRestClient==null){
			throw new BaseException("no @RestClient found class and method: " + method.toGenericString());
		}
		String tempUrl = getRestClientUrl(methodRestClient, method.getName());
		if(StringUtils.isNotBlank(tempUrl)){
			tempUrl = StringUtils.appendStartWith(tempUrl, "/");
		}
		tempUrl = getRestClientUrl(classRestClient, LangUtils.EMPTY_STRING) + tempUrl;
		this.url = tempUrl;
		this.requestMethod = getRestClientRequestMethod(methodRestClient, getRestClientRequestMethod(classRestClient, RequestMethod.GET));
		this.responseType = method.getReturnType();
		
		this.componentClass = ReflectUtils.getGenricType(method.getGenericReturnType(), 0, null);
	}
	
	final private String getRestClientUrl(RestClient restClient, String defValueIfNull){
		if(restClient==null)
			return defValueIfNull;
		return restClient.value();
	}
	
	final private RequestMethod getRestClientRequestMethod(RestClient restClient, RequestMethod defValueIfNull){
		if(restClient==null)
			return defValueIfNull;
		return restClient.method();
	}

	public String getUrl() {
		if(urlParser!=null){
			url = this.urlParser.parseUrl(url);
		}
		return url;
	}

	public String getUrlWithQueryParams(Map<Object, Object> paramMap) {
		String url = getUrl();
		CasualMap params = new CasualMap();
		params.addMapWithFilter(paramMap);
		String paramstr = params.toParamString();
		url = TagUtils.appendParamString(url, paramstr);
		return url;
	}

	public RequestMethod getRequestMethod() {
		return requestMethod;
	}

	@Override
	protected BaseMethodParameter createMethodParameter(Method method, int parameterIndex, Parameter parameter) {
		return new BaseMethodParameter(method, parameter, parameterIndex);
	}
	
	public Class<?> getResponseType() {
		return responseType;
	}

	public Class<?> getComponentClass() {
		return componentClass;
	}

	public Map<Object, Object> toMapByArgs(final Object[] args){
		Map<Object, Object> values = LangUtils.newHashMap(parameters.size());
		
		Object pvalue = null;
		for(BaseMethodParameter mp : parameters){
			pvalue = args[mp.getParameterIndex()];
			handleArg(values, mp, pvalue);
		}
		
		return values;
	}
	
	protected void handleArg(Map<Object, Object> values, BaseMethodParameter mp, Object pvalue){
		QueryName qn = mp.getParameterAnnotation(QueryName.class);
		String pname = null;
		if(qn!=null){
			pname = qn.value();
		}
		if(StringUtils.isBlank(pname)){
			pname = mp.getParameterName();
		}
		values.put(pname, pvalue);
	}

	/*public static class RestMethodParameter extends BaseMethodParameter {

		public RestMethodParameter(Method method, int parameterIndex) {
			super(method, parameterIndex);
		}
		
	}*/
}
