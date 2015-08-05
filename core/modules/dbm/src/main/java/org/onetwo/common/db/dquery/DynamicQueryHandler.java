package org.onetwo.common.db.dquery;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.onetwo.common.db.DataQuery;
import org.onetwo.common.db.ParsedSqlContext;
import org.onetwo.common.db.dquery.annotation.BatchObject;
import org.onetwo.common.db.filequery.FileNamedSqlGenerator;
import org.onetwo.common.db.filequery.NamespaceProperty;
import org.onetwo.common.db.filequery.QueryProvideManager;
import org.onetwo.common.exception.BaseException;
import org.onetwo.common.jdbc.JdbcDao;
import org.onetwo.common.log.JFishLoggerFactory;
import org.onetwo.common.profiling.TimeCounter;
import org.onetwo.common.spring.SpringApplication;
import org.onetwo.common.spring.SpringUtils;
import org.onetwo.common.spring.sql.ParsedSqlUtils;
import org.onetwo.common.spring.sql.ParsedSqlUtils.ParsedSqlWrapper;
import org.onetwo.common.spring.sql.ParsedSqlUtils.ParsedSqlWrapper.SqlParamterMeta;
import org.onetwo.common.utils.ClassUtils;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.utils.Page;
import org.onetwo.common.utils.ReflectUtils;
import org.onetwo.common.utils.convert.Types;
import org.slf4j.Logger;
import org.springframework.beans.BeanWrapper;
import org.springframework.cache.Cache;
import org.springframework.cache.Cache.ValueWrapper;

public class DynamicQueryHandler implements InvocationHandler {
	
	protected Logger logger = JFishLoggerFactory.getLogger(this.getClass());

	private Cache methodCache;
	private QueryProvideManager em;
	private Object proxyObject;
	private List<Method> excludeMethods = new ArrayList<Method>();
	private boolean debug = true;
//	private ParameterNameDiscoverer pnd = new LocalVariableTableParameterNameDiscoverer();
//	private Map<String, Method> methodCache = new HashMap<String, Method>();
	private JdbcDao jdao;
	

	/*public DynamicQueryHandler(CreateQueryable em, Cache methodCache, Class<?>... proxiedInterfaces){
		this(em, methodCache, SpringApplication.getInstance().getBean(JdbcDao.class, false), proxiedInterfaces);
	}*/
	
	public DynamicQueryHandler(QueryProvideManager em, Cache methodCache, JdbcDao jdao, Class<?>... proxiedInterfaces){
//		Class[] proxiedInterfaces = srcObject.getClass().getInterfaces();
//		Assert.notNull(em);
		this.em = em;
		this.methodCache = methodCache;
		Method[] methods = Object.class.getDeclaredMethods();
		for (int j = 0; j < methods.length; j++) {
			Method method = methods[j];
			excludeMethods.add(method);
		}
		this.jdao = jdao;
		this.proxyObject = Proxy.newProxyInstance(ClassUtils.getDefaultClassLoader(), proxiedInterfaces, this);
		
	}
	

	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		if(excludeMethods.contains(method)){
			logger.info("ignore method {} ...", method.toString());
			return ReflectUtils.invokeMethod(method, this, args);
		}

		try {
			return this.doInvoke(proxy, method, args);
		}/* catch (HibernateException e) {
			throw (HibernateException) e;
		}*/catch (Throwable e) {
			throw new BaseException("invoke query error : " + e.getMessage(), e);
		}
		
	}
	
	protected DynamicMethod getDynamicMethod(Method method){
		if(methodCache!=null){
			ValueWrapper value = methodCache.get(method);
			if(value!=null)
				return (DynamicMethod) value.get();
			DynamicMethod dm = newDynamicMethod(method);
			methodCache.put(method, dm);
			return dm;
		}else{
			return newDynamicMethod(method);
		}
	}
	
	private DynamicMethod newDynamicMethod(Method method){
		return new DynamicMethod(method);
//		return new DynamicMethodJ8(method);
	}
	
	public Object doInvoke(Object proxy, Method method, Object[] args) throws Throwable {
//		LangUtils.println("proxy: "+proxy+", method: ${0}", method);
//		TimeCounter t = new TimeCounter("doproxy", true);
//		t.start();
		DynamicMethod dmethod = getDynamicMethod(method);
		Class<?> resultClass = dmethod.getResultClass();
//		Class<?> componentClass = dmethod.getComponentClass();
		String queryName = dmethod.getQueryName();

		if(debug)
			logger.info("{}: {}", method.getDeclaringClass().getSimpleName()+"."+method.getName(), LangUtils.toString(args));
		
		Object result = null;
		Object[] methodArgs = null;
		if(Page.class.isAssignableFrom(resultClass)){
			Page<?> page = (Page<?>)args[0];
			
//			Object[] trimPageArgs = ArrayUtils.remove(args, 0);
			methodArgs = dmethod.toArrayByArgs(args);
			result = em.getFileNamedQueryFactory().findPage(queryName, page, methodArgs);
			
		}else if(List.class.isAssignableFrom(resultClass)){
			methodArgs = dmethod.toArrayByArgs(args);
//			logger.info("dq args: {}", LangUtils.toString(methodArgs));
			result = em.getFileNamedQueryFactory().findList(queryName, methodArgs);
			
		}else if(DataQuery.class.isAssignableFrom(resultClass)){
//			methodArgs = dmethod.toArrayByArgs(args, null);
			methodArgs = dmethod.toArrayByArgs(args);
//			logger.info("dq args: {}", LangUtils.toString(methodArgs));
			DataQuery dq = em.getFileNamedQueryFactory().createQuery(queryName, methodArgs);
			return dq;
			
		}else if(dmethod.isExecuteUpdate()){
			methodArgs = dmethod.toArrayByArgs(args);
			DataQuery dq = em.getFileNamedQueryFactory().createQuery(queryName, methodArgs);
			result = dq.executeUpdate();
			
		}else if(dmethod.isBatch()){
			//TODO 先特殊处理，待修改
			result = handleBatch(dmethod, args);
			
		}else{
			methodArgs = dmethod.toArrayByArgs(args);
			result = em.getFileNamedQueryFactory().findOne(queryName, methodArgs);
		}
		
		return result;
	}
	
	protected Object handleBatch(DynamicMethod dmethod, Object[] args){

		Class<?> componentClass = dmethod.getComponentClass();
//		methodArgs = dmethod.toArrayByArgs(args);
		Map<Object, Object> params = dmethod.toMapByArgs(args);
		Collection<?> batchParameter = (Collection<?>)params.get(BatchObject.class);;
		if(batchParameter==null){
			if(LangUtils.size(args)!=1 || !Collection.class.isInstance(args[0])){
				throw new BaseException("BatchObject not found, the batch method parameter only supported one parameter and must a Collection : " + dmethod.getMethod().toGenericString());
			}
			
			//default is first arg
			batchParameter = (Collection<?>)args[0];
		}
		
		FileNamedSqlGenerator<NamespaceProperty> sqlGen = (FileNamedSqlGenerator<NamespaceProperty>)em.getFileNamedQueryFactory().createFileNamedSqlGenerator(dmethod.getQueryName(), params);
		ParsedSqlContext sv = sqlGen.generatSql();
		JdbcDao jdao = this.jdao;
		if(jdao==null){
			jdao = SpringApplication.getInstance().getBean(JdbcDao.class, false);
			if(jdao==null)
				throw new BaseException("no supported jdbc batch execute!");
		}
		
		
		logger.info("===>>> batch insert start ...");
		TimeCounter t = new TimeCounter("prepare insert");
		t.start();
		
		BeanWrapper paramsContextBean = SpringUtils.newBeanWrapper(params);
		List<Map<String, Object>> batchValues = LangUtils.newArrayList(batchParameter.size());
		ParsedSqlWrapper sqlWrapper = ParsedSqlUtils.parseSql(sv.getParsedSql(), em.getSqlParamterPostfixFunctionRegistry());
		for(Object val : batchParameter){
			Map<String, Object> paramValueMap = new HashMap<String, Object>();
			BeanWrapper paramBean = SpringUtils.newBeanWrapper(val);
			
			for(SqlParamterMeta parameter : sqlWrapper.getParameters()){
				Object value = null;
				if(paramBean.isReadableProperty(parameter.getProperty())){
					value = parameter.getParamterValue(paramBean);
				}else{
					if(!paramsContextBean.isReadableProperty(parameter.getProperty()))
						throw new BaseException("batch execute parameter["+parameter.getProperty()+"] not found in bean["+val+"]'s properties or params");
				}
				
				if(value==null && paramsContextBean.isReadableProperty(parameter.getProperty()))
					value = parameter.getParamterValue(paramsContextBean);
				
				paramValueMap.put(parameter.getName(), value);
			}
			batchValues.add(paramValueMap);
		}

		logger.info("prepare insert finish!");
		t.stop();
		t.restart("insert to db");

		logger.info("batch sql : {}", sv.getParsedSql() );
		@SuppressWarnings("unchecked")
		int[] counts = jdao.getNamedParameterJdbcTemplate().batchUpdate(sv.getParsedSql(), batchValues.toArray(new HashMap[0]));
		
		logger.info("===>>> batch insert stop ...");
		t.stop();
		
		if(dmethod.getResultClass()==int[].class || dmethod.getResultClass()==Integer[].class){
			return counts;
		}else{
			int count = LangUtils.sum(counts);
			return Types.convertValue(count, dmethod.getResultClass());
		}
	}

	public Object getQueryObject(){
		return this.proxyObject;
	}
	
}
