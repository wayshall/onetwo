package org.onetwo.plugins.dq;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.onetwo.common.exception.BaseException;
import org.onetwo.common.fish.spring.JNamedQueryKey;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.utils.Page;
import org.onetwo.common.utils.ReflectUtils;
import org.onetwo.common.utils.StringUtils;
import org.onetwo.plugins.dq.annotations.Name;
import org.springframework.core.MethodParameter;

public class DynamicMethod {

	private static final List<String> EXECUTE_UPDATE_PREFIX = LangUtils.newArrayList("save", "update", "delete", "insert", "create");
	private static final String FIELD_NAME_SPERATOR = "By";
	
	private final Method method;
	private final List<DynamicMethodParameter> parameters;
	private final Class<?> resultClass;
	private final Class<?> componentClass;
	private final String queryName;
//	private List<String> parameterNames;
	
	public DynamicMethod(Method method){
		this.method = method;
		int psize = method.getParameterTypes().length;
		parameters = LangUtils.newArrayList(psize+2);
//		this.parameterNames = LangUtils.newArrayList(psize);
		DynamicMethodParameter mp = null;
		
		String methodName = method.getName();
		int byIndex = methodName.indexOf(FIELD_NAME_SPERATOR);
		String[] pnames = LangUtils.EMPTY_STRING_ARRAY;
		if(byIndex!=-1){
			pnames = StringUtils.split(methodName.substring(byIndex), FIELD_NAME_SPERATOR);
		}
		for(int index=0; index<psize; index++){
			mp = new DynamicMethodParameter(method, index, pnames);
			parameters.add(mp);
		}
		
		queryName = method.getDeclaringClass().getName()+"."+methodName;
		Class<?> rClass = method.getReturnType();
		Class<?> compClass = ReflectUtils.getGenricType(method.getGenericReturnType(), 0);
		if(rClass==void.class){
			rClass = parameters.get(0).getParameterType();
//			rClass = parameters.remove(0).getParameterType();
			if(Page.class != rClass){
				throw new BaseException("method no return type, the first arg of method must be a Page object: " + method.toGenericString());
			}
			Type ptype = this.parameters.remove(0).getGenericParameterType();
			if(ptype instanceof ParameterizedType){
				compClass = ReflectUtils.getGenricType(ptype, 0);
			}
		}
		
		resultClass = rClass;
		if(compClass==Object.class)
			compClass = resultClass;
		this.componentClass = compClass;
		
		LangUtils.println("resultClass: ${0}, componentClass:${1}", resultClass, compClass);
	}
	
	public MethodParameter remove(int index){
		return this.parameters.remove(index);
	}

	public Object[] toArrayByArgs(Object[] args, Class<?> componentClass){
		List<Object> values = LangUtils.newArrayList(parameters.size()*2);
		
		Object pvalue = null;
		for(DynamicMethodParameter mp : parameters){
			pvalue = args[mp.getParameterIndex()];
			if(!LangUtils.isSimpleTypeObject(pvalue)){
				Map<?, ?> map = ReflectUtils.toMap(pvalue);
				String prefix = "";
				if(mp.hasParameterAnnotation(Name.class)){
					prefix = mp.getParameterName();
				}
				for(Entry<?, ?> entry : map.entrySet()){
					values.add(prefix+entry.getKey());
					values.add(entry.getValue());
				}
			}else{
				values.add(mp.getParameterName());
				values.add(pvalue);
			}
		}
		if(componentClass!=null){
			values.add(JNamedQueryKey.ResultClass);
			values.add(componentClass);
		}
		return values.toArray();
	}
	
	public Method getMethod() {
		return method;
	}

	public List<DynamicMethodParameter> getParameters() {
		return parameters;
	}

	public Class<?> getResultClass() {
		return resultClass;
	}

	public Class<?> getComponentClass() {
		return componentClass;
	}

	public String getQueryName() {
		return queryName;
	}
	
	public boolean isExecuteUpdate(){
		String name = StringUtils.getFirstWord(this.method.getName());
		return EXECUTE_UPDATE_PREFIX.contains(name);
	}
	
	private static class DynamicMethodParameter extends MethodParameter {

		private String[] condidateParameterNames;
		private String parameterName;
		
		public DynamicMethodParameter(Method method, int parameterIndex, String[] parameterNamesByMethodName) {
			super(method, parameterIndex);
			this.condidateParameterNames = parameterNamesByMethodName;
		}

		/****
		 * 查询参数策略
		 * 如果有注解优先
		 * 其次是by分割符
		 * 以上皆否，则通过参数位置作为名称
		 */
		public String getParameterName() {
			if(StringUtils.isNotBlank(parameterName))
				return parameterName;
			
			Name name = getParameterAnnotation(Name.class);
			if(name!=null){
				parameterName = name.value();
			}else if(condidateParameterNames.length>getParameterIndex()){
				parameterName = StringUtils.uncapitalize(condidateParameterNames[getParameterIndex()]);
			}else{
				parameterName = String.valueOf(getParameterIndex());
			}
			return parameterName;
		}
		
	}
}
