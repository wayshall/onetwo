package org.onetwo.plugins.dq;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

import org.onetwo.common.db.ExtQuery.K.IfNull;
import org.onetwo.common.exception.BaseException;
import org.onetwo.common.spring.sql.JNamedQueryKey;
import org.onetwo.common.spring.sql.ParserContext;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.utils.Page;
import org.onetwo.common.utils.ReflectUtils;
import org.onetwo.common.utils.StringUtils;
import org.onetwo.plugins.dq.annotations.ExecuteBatch;
import org.onetwo.plugins.dq.annotations.Name;
import org.springframework.core.MethodParameter;

public class DynamicMethod {

	private static final List<String> EXECUTE_UPDATE_PREFIX = LangUtils.newArrayList("save", "update", "remove", "delete", "insert", "create");
	private static final List<String> BATCH_PREFIX = LangUtils.newArrayList("batch");
	private static final String FIELD_NAME_SPERATOR = "By";
	
	private final Method method;
	private final List<DynamicMethodParameter> parameters;
	private final Class<?> resultClass;
	private final Class<?> componentClass;
	private final String queryName;
	private final ExecuteBatch executeBatch;
//	private List<String> parameterNames;
	
	public DynamicMethod(Method method){
		this.method = method;
		this.executeBatch = method.getAnnotation(ExecuteBatch.class);
		int psize = method.getParameterTypes().length;
		parameters = LangUtils.newArrayList(psize+2);
//		this.parameterNames = LangUtils.newArrayList(psize);
		DynamicMethodParameter mp = null;
		
		String methodName = method.getName();
		int byIndex = methodName.indexOf(FIELD_NAME_SPERATOR);
		String[] pnames = LangUtils.EMPTY_STRING_ARRAY;
		//是否通过byUserNameByAge的命名方式
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
			//如果返回类型为空，看第一个参数是否是page对象
			if(Page.class != rClass){
				throw new BaseException("method no return type, the first arg of method must be a Page object: " + method.toGenericString());
			}
			DynamicMethodParameter pageParamter = this.parameters.get(0);
			if(pageParamter.getParameterAnnotation(Name.class)==null)//如果page对象没有name注解，移除它
				this.parameters.remove(0);
			Type ptype = pageParamter.getGenericParameterType();
			if(ptype instanceof ParameterizedType){
				compClass = ReflectUtils.getGenricType(ptype, 0);
			}
		}else if(Page.class==rClass){
			rClass = parameters.get(0).getParameterType();
//			rClass = parameters.remove(0).getParameterType();
			if(Page.class == rClass){
				throw new BaseException("method has return Page object, the first arg can not return the Page object: " + method.toGenericString());
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
	
	private boolean addAndCheckParamValue(Name name, List<Object> values, String pname, Object pvalue){
		//TODO 参数ifParamNull已过时，将来注释下面这段代码
		IfNull ifnull = name.ifParamNull();
		if(pvalue==null){
			switch (ifnull) {
				case Ignore:
					return false;
				case Throw:
					throw new BaseException("param["+pname+"]' value must not be null");
				default:
					break;
			}
		}
		//end
		values.add(pname);
		values.add(pvalue);
		return true;
	}

	public Object[] toArrayByArgs(Object[] args, Class<?> componentClass){
		List<Object> values = LangUtils.newArrayList(parameters.size()*2);
		
		Object pvalue = null;
		ParserContext parserContext = null;
		for(DynamicMethodParameter mp : parameters){
			pvalue = args[mp.getParameterIndex()];
			if(pvalue instanceof ParserContext){
				//parserContext
				if(parserContext==null)
					parserContext = ParserContext.create();
				parserContext.putAll((ParserContext) pvalue);
			}else if(mp.hasParameterAnnotation(Name.class)){
				Name name = mp.getParameterAnnotation(Name.class);
				if(name.renamedUseIndex()){
					List<?> listValue = LangUtils.asList(pvalue);
					int index = 0;
					//parem0, value0, param1, value1, ...
					for(Object obj : listValue){
						if(addAndCheckParamValue(name, values, mp.getParameterName()+index, obj)){
							index++;
						}
					}
					values.add(mp.getParameterName());
					values.add(listValue);
				}else{
					addAndCheckParamValue(name, values, mp.getParameterName(), pvalue);
				}
				/*if(name.queryParam()){
					if(name.renamedUseIndex()){
						List<?> listValue = LangUtils.asList(pvalue);
						int index = 0;
						//parem0, value0, param1, value1, ...
						for(Object obj : listValue){
							if(addAndCheckParamValue(name, values, mp.getParameterName()+index, obj)){
								index++;
							}
							values.add(mp.getParameterName()+index);
							values.add(obj);
							index++;
						}
						//parserContext
						if(parserContext==null)
							parserContext = ParserContext.create();
						parserContext.put(mp.getParameterName(), listValue);
					}else{
						addAndCheckParamValue(name, values, mp.getParameterName(), pvalue);
//						values.add(mp.getParameterName());
//						values.add(pvalue);
					}
				}else{
					//parserContext
					if(parserContext==null)
						parserContext = ParserContext.create();
					parserContext.put(mp.getParameterName(), pvalue);
				}*/
					
			}else{
				values.add(mp.getParameterName());
				values.add(pvalue);
			}
		}

		if(parserContext!=null){
			values.add(JNamedQueryKey.ParserContext);
			values.add(parserContext);
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
	
	public boolean isBatch(){
		return executeBatch!=null || BATCH_PREFIX.contains(StringUtils.getFirstWord(this.method.getName()));
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
