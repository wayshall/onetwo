package org.onetwo.common.spring.utils;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.List;

import org.onetwo.common.utils.LangUtils;

public abstract class AbstractMethodResolver<T extends AbstractMethodParameter> {
	protected final Method method;
	protected final List<T> parameters;
	
	
	public AbstractMethodResolver(Method method) {
		super();
		this.method = method;
		this.parameters = createMethodParameters(method);
	}

	public Method getMethod() {
		return method;
	}

	final protected List<T> createMethodParameters(Method method){
		int psize = method.getParameterTypes().length;
		List<T> parameters = LangUtils.newArrayList(psize+2);
//		this.parameterNames = LangUtils.newArrayList(psize);
		T mp = null;
		
		Parameter[] paramters = method.getParameters();
		for(int index=0; index<psize; index++){
			mp = createMethodParameter(method, index, paramters[index]);
			parameters.add(mp);
		}
		return parameters;
	}
	
	public List<T> getParameters() {
		return parameters;
	}

	abstract protected T createMethodParameter(Method method, int parameterIndex, Parameter parameter);
}
