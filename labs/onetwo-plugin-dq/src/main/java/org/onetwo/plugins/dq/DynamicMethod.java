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

	private static final List<String> EXECUTE_UPDATE_PREFIX = LangUtils.newArrayList("save", "update", "delete", "insert");
	private static final String FIELD_NAME_SPERATOR = "By";
	
	private final Method method;
	private final List<MethodParameter> parameters;
	private final Class<?> resultClass;
	private final Class<?> componentClass;
	private final String queryName;
	private List<String> parameterNames;
	
	public DynamicMethod(Method method){
		this.method = method;
		int psize = method.getParameterTypes().length;
		parameters = LangUtils.newArrayList(psize+2);
		this.parameterNames = LangUtils.newArrayList(psize);
		MethodParameter mp = null;
		
		String methodName = method.getName();
		int byIndex = methodName.indexOf(FIELD_NAME_SPERATOR);
		String[] pnames = LangUtils.EMPTY_STRING_ARRAY;
		if(byIndex!=-1){
			pnames = StringUtils.split(methodName.substring(byIndex), FIELD_NAME_SPERATOR);
		}
		for(int index=0; index<psize; index++){
			mp = new MethodParameter(method, index);
			parameters.add(mp);
			
			Name name = mp.getParameterAnnotation(Name.class);
			if(name!=null){
				parameterNames.add(name.value());
			}else if(pnames.length>index){
				parameterNames.add(StringUtils.uncapitalize(pnames[index]));
			}else{
				parameterNames.add(String.valueOf(mp.getParameterIndex()));
			}
		}
		
		queryName = method.getDeclaringClass().getName()+"."+methodName;
		Class<?> rClass = method.getReturnType();
		Class<?> compClass = ReflectUtils.getGenricType(method.getGenericReturnType(), 0);
		if(rClass==void.class){
			rClass = parameters.get(0).getParameterType();
			if(!Page.class.isInstance(rClass)){
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
		for(MethodParameter mp : parameters){
			pvalue = args[mp.getParameterIndex()];
			if(!LangUtils.isSimpleTypeObject(pvalue)){
				Map<?, ?> map = ReflectUtils.toMap(pvalue);
				for(Entry<?, ?> entry : map.entrySet()){
					values.add(entry.getKey());
					values.add(entry.getValue());
				}
			}else{
				values.add(parameterNames.get(mp.getParameterIndex()));
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

	public List<MethodParameter> getParameters() {
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
	
}
