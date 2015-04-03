package org.onetwo.plugins.dq;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.List;

import org.onetwo.common.utils.LangUtils;
import org.onetwo.plugins.dq.annotations.Name;

public class DynamicMethodJ8 extends DynamicMethod {
	public DynamicMethodJ8(Method method) {
		super(method);
	}

	@Override
	protected List<DynamicMethodParameter> createDynamicMethodParameters(Method method){
		int psize = method.getParameterTypes().length;
		List<DynamicMethodParameter> parameters = LangUtils.newArrayList(psize+2);
//		this.parameterNames = LangUtils.newArrayList(psize);
		DynamicMethodParameter mp = null;
		
		Parameter[] paramters = method.getParameters();
		for(int index=0; index<psize; index++){
			mp = new DynamicMethodParameterJ8(method, index, paramters[index]);
			parameters.add(mp);
			/*if(!batchUpdate){
				this.batchUpdate = mp.hasParameterAnnotation(BatchObject.class);
			}*/
		}
		return parameters;
	}
	
	protected static class DynamicMethodParameterJ8 extends DynamicMethodParameter {

		private Parameter parameter;
		
		public DynamicMethodParameterJ8(Method method, int parameterIndex, Parameter parameter) {
			super(method, parameterIndex, LangUtils.EMPTY_STRING_ARRAY);
			this.parameter = parameter;
		}

		@Override
		public String getParameterName() {
			Name name = getParameterAnnotation(Name.class);
			if(name!=null){
				return name.value();
			}else if(parameter!=null && parameter.isNamePresent()){
				return parameter.getName();
			}else{
				return String.valueOf(getParameterIndex());
			}
		}
	}
}
