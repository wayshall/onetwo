package org.onetwo.common.jsonrpc;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.List;
import java.util.Map;

import org.onetwo.common.proxy.AbstractMethodResolver;
import org.onetwo.common.proxy.BaseMethodParameter;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.utils.ReflectUtils;

public class RpcMethodResolver extends AbstractMethodResolver<BaseMethodParameter> {

	final private Class<?> responseType;
	private Class<?> componentClass;
	private boolean namedParam;
	
	public RpcMethodResolver(Method method) {
		super(method);
		this.responseType = method.getReturnType();
		this.componentClass = ReflectUtils.getGenricType(method.getGenericReturnType(), 0, null);
		if(!this.parameters.isEmpty()){
			this.namedParam = this.parameters.get(0).isNamePresent();
		}
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
	
	public boolean isNamedParam() {
		return namedParam;
	}


	public Map<String, Object> toMapByArgs(final Object[] args){
		Map<String, Object> values = LangUtils.newHashMap(parameters.size());
		
		Object pvalue = null;
		for(BaseMethodParameter mp : parameters){
			pvalue = args[mp.getParameterIndex()];
			values.put(mp.getParameterName(), pvalue);
		}
		
		return values;
	}

	public List<Object> toListByArgs(final Object[] args){
		List<Object> values = LangUtils.newArrayList(parameters.size());
		
		Object pvalue = null;
		for(BaseMethodParameter mp : parameters){
			pvalue = args[mp.getParameterIndex()];
			values.add(pvalue);
		}
		
		return values;
	}
	
}
