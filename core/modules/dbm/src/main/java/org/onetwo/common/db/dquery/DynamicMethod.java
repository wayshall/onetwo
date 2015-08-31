package org.onetwo.common.db.dquery;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.tuple.Pair;
import org.onetwo.common.annotation.AnnotationUtils;
import org.onetwo.common.db.DataQuery;
import org.onetwo.common.db.QueryConfigData;
import org.onetwo.common.db.QueryContextVariable;
import org.onetwo.common.db.dquery.DynamicMethod.DynamicMethodParameter;
import org.onetwo.common.db.dquery.annotation.AsCountQuery;
import org.onetwo.common.db.dquery.annotation.BatchObject;
import org.onetwo.common.db.dquery.annotation.ExecuteUpdate;
import org.onetwo.common.db.dquery.annotation.Matcher;
import org.onetwo.common.db.dquery.annotation.Name;
import org.onetwo.common.db.dquery.annotation.QueryConfig;
import org.onetwo.common.db.filequery.FileNamedQueryException;
import org.onetwo.common.db.filequery.JNamedQueryKey;
import org.onetwo.common.db.filequery.ParsedSqlUtils;
import org.onetwo.common.db.filequery.ParserContext;
import org.onetwo.common.db.filequery.ParserContextFunctionSet;
import org.onetwo.common.db.sqlext.ExtQueryUtils;
import org.onetwo.common.proxy.AbstractMethodResolver;
import org.onetwo.common.proxy.BaseMethodParameter;
import org.onetwo.common.reflect.ReflectUtils;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.utils.Page;
import org.onetwo.common.utils.StringUtils;
import org.springframework.core.MethodParameter;


public class DynamicMethod extends AbstractMethodResolver<DynamicMethodParameter>{

	public static DynamicMethod newDynamicMethod(Method method){
		return new DynamicMethod(method);
	}

	public static final List<String> EXECUTE_UPDATE_PREFIX = LangUtils.newArrayList("save", "update", "remove", "delete", "insert", "create");
	public static final List<String> BATCH_PREFIX = LangUtils.newArrayList("batch");
//	public static final String FIELD_NAME_SPERATOR = "By";
	
//	private final Method method;
//	private final List<DynamicMethodParameter> parameters;
	private final Class<?> resultClass;
	private final Class<?> componentClass;
	private String queryName;
//	private final ExecuteUpdate executeUpdate;
	private boolean update;
	private boolean batchUpdate;
	private AsCountQuery asCountQuery;
//	private List<String> parameterNames;
	
	private DynamicMethodParameter pageParamter;
	private DynamicMethodParameter matcherParamter;
	
	public DynamicMethod(Method method){
		super(method);
		
		this.checkAndSetExecuteType();
		
		
		Class<?> rClass = method.getReturnType();
		Class<?> compClass = ReflectUtils.getGenricType(method.getGenericReturnType(), 0);
		if(rClass==void.class){
//			DynamicMethodParameter firstParamter = parameters.get(0);
			pageParamter = matcherParamter!=null?parameters.get(1):parameters.get(0);
			//如果返回类型为空，看第一个参数是否是page对象
			rClass = pageParamter.getParameterType();
			if(Page.class != rClass){
				throw new FileNamedQueryException("["+method.toGenericString()+"] return void type, pelease define the Page object at parameter position : " + pageParamter.getParameterIndex());
			}
			/*if(pageParamter.getParameterAnnotation(Name.class)==null)//如果page对象没有name注解，移除它
				this.parameters.remove(0)*/;
			Type ptype = pageParamter.getGenericParameterType();
			if(ptype instanceof ParameterizedType){
				compClass = ReflectUtils.getGenricType(ptype, 0);
			}
		}else{
			parameters.stream().filter(p->p.getParameterType()==Page.class)
							.findAny()
							.ifPresent(p->{
								throw new FileNamedQueryException("define Page Type at the first parameter and return void if you want to pagination: " + method.toGenericString());
							});
			if(Page.class==rClass){
				throw new FileNamedQueryException("define Page Type at the first parameter and return void if you want to pagination: " + method.toGenericString());
			}else if(DataQuery.class==rClass){
				compClass = null;
			}
		}
		
		
		resultClass = rClass;
		if(compClass==Object.class)
			compClass = resultClass;
		this.componentClass = compClass;

		//check query swither
		checkAndFindQuerySwitch(parameters);
		checkAndFindAsCountQuery(componentClass);
		findAndSetQueryName(this.asCountQuery);
		
		LangUtils.println("resultClass: ${0}, componentClass:${1}", resultClass, compClass);
	}
	
	/***
	 * dependency AsCountQuery
	 * @param asCountQuery
	 */
	private void findAndSetQueryName(AsCountQuery asCountQuery){
		if(asCountQuery!=null){
			queryName = method.getDeclaringClass().getName()+"."+asCountQuery.value();
		}else{
			queryName = method.getDeclaringClass().getName()+"."+method.getName();
		}
	}
	
	/***
	 * dependency componentClass
	 * @param componentClass
	 */
	private void checkAndFindAsCountQuery(Class<?> componentClass){
		this.asCountQuery = method.getAnnotation(AsCountQuery.class);
		if(asCountQuery!=null){
			if(update || batchUpdate){
				update = batchUpdate = false;
			}
			if(!LangUtils.isNumberType(componentClass)){
				throw new FileNamedQueryException("countquery's return type must be a number, but " + componentClass);
			}
		}
	}
	
	private void checkAndFindQuerySwitch(List<DynamicMethodParameter> parameters){
		this.matcherParamter = parameters.stream().filter(p->{
								if(p.hasParameterAnnotation(Matcher.class)){
									if(p.getParameterIndex()!=0){
										throw new FileNamedQueryException("QuerySwitch must be first parameter but actual index is " + (p.getParameterIndex()+1));
									}else if(p.getParameterType()!=String.class){
										throw new FileNamedQueryException("QuerySwitch must be must be a String!");
									}
									return true;
								}
								return false;
							})
						.findFirst()
						.orElse(null);
	}
	
	public boolean isAsCountQuery(){
		return asCountQuery!=null;
	}
	
	@Override
	protected DynamicMethodParameter createMethodParameter(Method method, int parameterIndex, Parameter parameter) {
		return new DynamicMethodParameterJ8(method, parameterIndex, parameter);
	}
	
	public DynamicMethodParameter getPageParamter() {
		return pageParamter;
	}

	protected boolean judgeBatchUpdateFromParameterObjects(List<DynamicMethodParameter> mparameters){
		for(DynamicMethodParameter mp : mparameters){
			if(mp.hasParameterAnnotation(BatchObject.class)){
				return true;
			}
		}
		return false;
	}
	
	final private void checkAndSetExecuteType(){
		ExecuteUpdate executeUpdate = method.getAnnotation(ExecuteUpdate.class);
		if(executeUpdate!=null){
			this.update = true;
			this.batchUpdate = executeUpdate.isBatch();
		}else{
			this.update = EXECUTE_UPDATE_PREFIX.contains(StringUtils.getFirstWord(this.method.getName()));
			this.batchUpdate = BATCH_PREFIX.contains(StringUtils.getFirstWord(this.method.getName()));
		}
		if(!batchUpdate){
			this.batchUpdate = judgeBatchUpdateFromParameterObjects(parameters);
		}
	}
	
	public MethodParameter remove(int index){
		return this.parameters.remove(index);
	}
	
	/*private boolean addAndCheckParamValue(Name name, List<Object> values, String pname, Object pvalue){
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
		if(String.class.isInstance(pvalue) && name.isLikeQuery()){
			values.add(ExtQueryUtils.getLikeString(pvalue.toString()));
		}else{
			values.add(pvalue);
		}
		return true;
	}*/
	

	/*public Object[] toArrayByArgs(Object[] args){
		Map<Object, Object> map = toMapByArgs(args);
		return Langs.toArray(map);
//		return toArrayByArgs2(args, componentClass);
	}*/

	/*public Object[] toArrayByArgs2(Object[] args, Class<?> componentClass){
		List<Object> values = LangUtils.newArrayList(parameters.size()*2);
		
		Object pvalue = null;
		ParserContext parserContext = ParserContext.create();
		for(DynamicMethodParameter mp : parameters){
			pvalue = args[mp.getParameterIndex()];
			if(pvalue instanceof ParserContext){
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
					
			}else{
				values.add(mp.getParameterName());
				values.add(pvalue);
			}
		}
		
		QueryConfig queryConfig = method.getAnnotation(QueryConfig.class);
		if(queryConfig!=null){
			QueryConfigData config = new QueryConfigData(queryConfig.stateful());
			config.setLikeQueryFields(Arrays.asList(queryConfig.likeQueryFields()));
			parserContext.setQueryConfig(config);
		}else{
			parserContext.setQueryConfig(QueryConfigData.EMPTY_CONFIG);
		}

		values.add(JNamedQueryKey.ParserContext);
		values.add(parserContext);
		if(componentClass!=null){
			values.add(JNamedQueryKey.ResultClass);
			values.add(componentClass);
		}
		return values.toArray();
	}*/
	
	private Pair<String, Object> addAndCheckParamValue(Name name, String pname, Object pvalue){
		//TODO 参数ifParamNull已过时，将来注释下面这段代码
		/*IfNull ifnull = name.ifParamNull();
		if(pvalue==null){
			switch (ifnull) {
				case Ignore:
					return null;
				case Throw:
					throw new BaseException("param["+pname+"]' value must not be null");
				default:
					break;
			}
		}*/
		//end
//		values.add(pname);
		Object val = null;
		if(String.class.isInstance(pvalue) && name.isLikeQuery()){
//			values.add(ExtQueryUtils.getLikeString(pvalue.toString()));
			val = ExtQueryUtils.getLikeString(pvalue.toString());
		}else{
//			values.add(pvalue);
			/*if(pvalue!=null && pvalue.getClass().isArray()){
				val = LangUtils.asList(pvalue);
			}else{
				val = pvalue;
			}*/
			val = pvalue;
		}
		return Pair.of(pname, val);
	}
	
	protected void handleArg(Map<Object, Object> values, ParserContext parserContext, DynamicMethodParameter mp, Object pvalue){
		if(pvalue instanceof ParserContext){
			parserContext.putAll((ParserContext) pvalue);
		}else if(mp.hasParameterAnnotation(Name.class)){
			Name name = mp.getParameterAnnotation(Name.class);
			if(name.renamedUseIndex()){
				List<?> listValue = LangUtils.asList(pvalue);
				int index = 0;
				//parem0, value0, param1, value1, ...
				for(Object obj : listValue){
					Pair<String, Object> pair = addAndCheckParamValue(name, mp.getParameterName()+index, obj);
					if(pair!=null){
						putArg2Map(values, pair.getLeft(), pair.getRight());
//						if(addAndCheckParamValue(name, values, mp.getParameterName()+index, obj)){
						index++;
					}
				}
				/*values.add(mp.getParameterName());
				values.add(listValue);*/
				putArg2Map(values, mp.getParameterName(), listValue);
				
			}else{
//				addAndCheckParamValue(name, values, mp.getParameterName(), pvalue);
				/*if(mp.hasParameterAnnotation(BatchObject.class)){
					values.put(BatchObject.class, pvalue);
				}else{
					Pair<String, Object> pair = addAndCheckParamValue(name, mp.getParameterName(), pvalue);
					if(pair!=null){
						values.put(pair.getValue0(), pair.getValue1());
					}
				}*/
				Pair<String, Object> pair = addAndCheckParamValue(name, mp.getParameterName(), pvalue);
				if(pair!=null){
					putArg2Map(values, pair.getLeft(), pair.getRight());
				}
			}
				
		}else if(mp.hasParameterAnnotation(BatchObject.class)){
			putArg2Map(values, BatchObject.class, pvalue);
			
		}/*else if(mp.hasParameterAnnotation(QuerySwitch.class)){
			if(mp.getParameterIndex()!=0){
				throw new FileNamedQueryException("QuerySwitch must be first parameter but actual index is " + (mp.getParameterIndex()+1));
			}
			putArg2Map(values, QuerySwitch.class, pvalue);
			
		}*/else{
			/*values.add(mp.getParameterName());
			values.add(pvalue);*/
			putArg2Map(values, mp.getParameterName(), pvalue);
		}
		
		/*if(mp.hasParameterAnnotation(QuerySwitch.class)){
			if(values.containsKey(QuerySwitch.class)){
				throw new FileNamedQueryException("allows only one QuerySwitch parameter!");
			}
			if(mp.getParameterIndex()!=0){
				throw new FileNamedQueryException("QuerySwitch must be first parameter but actual index is " + (mp.getParameterIndex()+1));
			}
			putArg2Map(values, QuerySwitch.class, pvalue);
			
		}*/
	}
	
	private void putArg2Map(Map<Object, Object> values, Object key, Object value){
		if(values.containsKey(key)){
			throw new IllegalArgumentException("parameter has exist: " + key);
		}
		if(value instanceof Enum){
			values.put(key, ((Enum<?>)value).name());
		}else{
			values.put(key, value);
		}
	}
	
	protected void buildQueryConfig(ParserContext parserContext){
		QueryConfig queryConfig = AnnotationUtils.findAnnotation(method, QueryConfig.class, true);//method.getAnnotation(QueryConfig.class);
		if(queryConfig!=null){
			QueryConfigData config = new QueryConfigData(queryConfig.stateful());
			config.setLikeQueryFields(Arrays.asList(queryConfig.likeQueryFields()));
			if(queryConfig.funcClass()==ParserContextFunctionSet.class){
				config.setVariables(ParserContextFunctionSet.getInstance());
			}else{
				QueryContextVariable func = (QueryContextVariable)ReflectUtils.newInstance(queryConfig.funcClass());
				config.setVariables(ParserContextFunctionSet.getInstance(), func);
			}
			parserContext.setQueryConfig(config);
			
		}else{
			parserContext.setQueryConfig(ParsedSqlUtils.EMPTY_CONFIG);
		}
	}
	
	public Object getMatcherValue(Object[] args){
		if(!hasMatcher())
			return null;
		return args[matcherParamter.getParameterIndex()];
	}
	
	public boolean hasMatcher(){
		return this.matcherParamter!=null;
	}

	public Map<Object, Object> toMapByArgs(Object[] args){
		Map<Object, Object> values = LangUtils.newHashMap(parameters.size());
		
		Object pvalue = null;
		ParserContext parserContext = ParserContext.create();
		for(DynamicMethodParameter mp : parameters){
			pvalue = args[mp.getParameterIndex()];
			handleArg(values, parserContext, mp, pvalue);
		}
		
		buildQueryConfig(parserContext);

		values.put(JNamedQueryKey.ParserContext, parserContext);
		if(componentClass!=null){
			values.put(JNamedQueryKey.ResultClass, componentClass);
		}
		return values;
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
		/*String name = StringUtils.getFirstWord(this.method.getName());
		return EXECUTE_UPDATE_PREFIX.contains(name);*/
		return update && !batchUpdate; //(executeUpdate!=null && !executeUpdate.isBatch()) || );
	}
	
	public boolean isBatch(){
		return batchUpdate;//(executeUpdate!=null && executeUpdate.isBatch()) || );
	}
	
	protected static class DynamicMethodParameter extends BaseMethodParameter {

		final protected String[] condidateParameterNames;
		final protected Name nameAnnotation;
		

		public DynamicMethodParameter(Method method, int parameterIndex) {
			this(method, parameterIndex, LangUtils.EMPTY_STRING_ARRAY);
		}
		
		public DynamicMethodParameter(Method method, int parameterIndex, String[] parameterNamesByMethodName) {
			super(method, parameterIndex);
			this.condidateParameterNames = parameterNamesByMethodName;
			nameAnnotation = getParameterAnnotation(Name.class);
		}

		/****
		 * 查询参数策略
		 * 如果有注解优先
		 * 其次是by分割符
		 * 以上皆否，则通过参数位置作为名称
		 */
		public String getParameterName() {
			Name name = getParameterAnnotation(Name.class);
			if(name!=null){
				return name.value();
			}else if(condidateParameterNames.length>getParameterIndex()){
				return StringUtils.uncapitalize(condidateParameterNames[getParameterIndex()]);
			}else{
				return String.valueOf(getParameterIndex());
			}
		}
		
	}
	
	/***
	 * for java8
	 * @author wayshall
	 *
	 */
	protected static class DynamicMethodParameterJ8 extends DynamicMethodParameter {

		public DynamicMethodParameterJ8(Method method, int parameterIndex, Parameter parameter) {
			super(method, parameterIndex, LangUtils.EMPTY_STRING_ARRAY);
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
