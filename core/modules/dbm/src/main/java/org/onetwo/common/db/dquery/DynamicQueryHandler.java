package org.onetwo.common.db.dquery;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;

import javax.sql.DataSource;

import org.onetwo.common.convert.Types;
import org.onetwo.common.db.DataQuery;
import org.onetwo.common.db.ParsedSqlContext;
import org.onetwo.common.db.filequery.FileNamedSqlGenerator;
import org.onetwo.common.db.filequery.ParsedSqlUtils;
import org.onetwo.common.db.filequery.ParsedSqlUtils.ParsedSqlWrapper;
import org.onetwo.common.db.filequery.ParsedSqlUtils.ParsedSqlWrapper.SqlParamterMeta;
import org.onetwo.common.db.filequery.QueryProvideManager;
import org.onetwo.common.exception.BaseException;
import org.onetwo.common.log.JFishLoggerFactory;
import org.onetwo.common.profiling.TimeCounter;
import org.onetwo.common.spring.SpringApplication;
import org.onetwo.common.spring.SpringUtils;
import org.onetwo.common.utils.ClassUtils;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.utils.Page;
import org.onetwo.dbm.exception.FileNamedQueryException;
import org.onetwo.dbm.jdbc.JFishNamedJdbcTemplate;
import org.onetwo.dbm.jdbc.NamedJdbcTemplate;
import org.onetwo.dbm.utils.DbmUtils;
import org.slf4j.Logger;
import org.springframework.beans.BeanWrapper;

import com.google.common.cache.LoadingCache;
import com.google.common.collect.Sets;

public class DynamicQueryHandler implements InvocationHandler {
	
	protected Logger logger = JFishLoggerFactory.getLogger(this.getClass());

	private LoadingCache<Method, DynamicMethod> methodCache;
	private QueryProvideManager em;
	private Object proxyObject;
//	private List<Method> excludeMethods = new ArrayList<Method>();
//	private List<Method> includeMethods = new ArrayList<Method>();
	private boolean debug = true;
//	private ParameterNameDiscoverer pnd = new LocalVariableTableParameterNameDiscoverer();
//	private Map<String, Method> methodCache = new HashMap<String, Method>();
	private NamedJdbcTemplate namedJdbcTemplate;
	
	public DynamicQueryHandler(QueryProvideManager em, LoadingCache<Method, DynamicMethod> methodCache, NamedJdbcTemplate namedJdbcTemplate, Class<?>... proxiedInterfaces){
		this.em = em;
		this.methodCache = methodCache;
		
		/*includeMethods = Stream.of(proxiedInterfaces)
								.map(i->i.getDeclaredMethods())
								.flatMap(array->Stream.of(array))
								.collect(Collectors.toList());*/
		this.namedJdbcTemplate = namedJdbcTemplate;
		this.proxyObject = Proxy.newProxyInstance(ClassUtils.getDefaultClassLoader(), proxiedInterfaces, this);
		
	}
	

	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		/*if(!includeMethods.contains(method)){
			logger.info("ignore method {} ...", method.toString());
			return ReflectUtils.invokeMethod(method, this, args);
		}*/
		
		if(Object.class  == method.getDeclaringClass()) {
			String name = method.getName();
			if("equals".equals(name)) {
				return proxy == args[0];
			} else if("hashCode".equals(name)) {
				return System.identityHashCode(proxy);
			} else if("toString".equals(name)) {
				return proxy.getClass().getName() + "@" +
	               Integer.toHexString(System.identityHashCode(proxy)) + ", InvocationHandler " + this;
			} else {
				throw new IllegalStateException(String.valueOf(method));
			}
		}


		DynamicMethod dmethod = getDynamicMethod(method);
		try {
			return this.doInvoke(proxy, dmethod, args);
		}/* catch (HibernateException e) {
			throw (HibernateException) e;
		}*/catch (Throwable e) {
			if(e instanceof FileNamedQueryException){
				throw (FileNamedQueryException)e;
			}else{
				throw new FileNamedQueryException("invoke query["+dmethod.getQueryName()+"] error : " + e.getMessage(), e);
			}
		}
		
	}
	
	protected DynamicMethod getDynamicMethod(Method method){
		try {
			return methodCache.get(method);
		} catch (ExecutionException e) {
			throw new FileNamedQueryException("get dynamic method error", e);
//			return newDynamicMethod(method);
		}
	}
	
	public Object doInvoke(Object proxy, DynamicMethod dmethod, Object[] args) throws Throwable {
		MethodDynamicQueryInvokeContext invokeContext = new MethodDynamicQueryInvokeContext(dmethod, args);
		
		Class<?> resultClass = dmethod.getResultClass();
//		JFishNamedFileQueryInfo parsedQueryName = (JFishNamedFileQueryInfo) em.getFileNamedQueryManager().getNamedQueryInfo(invokeContext);

		if(debug)
			logger.info("{}: {}", dmethod.getQueryName(), LangUtils.toString(args));
		
		Object result = null;
//		Object[] methodArgs = null;
		
		if(dmethod.isBatch()){
			//先特殊处理，待修改
//			result = handleBatch(dmethod, args, parsedQueryName, parsedParams);
			result = handleBatch(invokeContext);
		}else{
//			methodArgs = Langs.toArray(invokeContext.getParsedParams());
			
			if(Page.class.isAssignableFrom(resultClass)){
				Page<?> page = (Page<?>)args[dmethod.getPageParamter().getParameterIndex()];
				
				result = em.getFileNamedQueryManager().findPage(page, invokeContext);
				
			}else if(Collection.class.isAssignableFrom(resultClass)){
				result = em.getFileNamedQueryManager().findList(invokeContext);
				
			}else if(Set.class.isAssignableFrom(resultClass)){
				List<?> datalist = em.getFileNamedQueryManager().findList(invokeContext);
				result = Sets.newHashSet(datalist);
				
			}else if(DataQuery.class.isAssignableFrom(resultClass)){
				DataQuery dq = em.getFileNamedQueryManager().createQuery(invokeContext);
				return dq;
				
			}else if(dmethod.isExecuteUpdate()){
				DataQuery dq = em.getFileNamedQueryManager().createQuery(invokeContext);
				result = dq.executeUpdate();
				
			}else if(dmethod.isAsCountQuery()){
//				parsedQueryName.setMappedEntity(dmethod.getResultClass());
				DataQuery dq = em.getFileNamedQueryManager().createCountQuery(invokeContext);
				result = dq.getSingleResult();
				result = Types.convertValue(result, resultClass);
			}else{
				result = em.getFileNamedQueryManager().findOne(invokeContext);
			}
		}
		
		return result;
	}
	
	protected Object handleBatch(MethodDynamicQueryInvokeContext invokeContext){
		DynamicMethod dmethod = invokeContext.getDynamicMethod();
		Collection<?> batchParameter = invokeContext.getBatchParameter();
		/*if(batchParameter==null){
			if(LangUtils.size(invokeContext.getParameterValues())!=1 || !Collection.class.isInstance(invokeContext.getParameterValues()[0])){
				throw new BaseException("BatchObject not found, the batch method parameter only supported one parameter and must a Collection : " + invokeContext.getDynamicMethod().getMethod().toGenericString());
			}
			
			//default is first arg
			batchParameter = (Collection<?>)args[0];
		}*/
		
		FileNamedSqlGenerator sqlGen = em.getFileNamedQueryManager().createFileNamedSqlGenerator(invokeContext);
		ParsedSqlContext sv = sqlGen.generatSql();
//		JdbcDao jdao = this.jdao;
		NamedJdbcTemplate namedJdbcTemplate = this.namedJdbcTemplate;
		if(namedJdbcTemplate==null){
			DataSource ds = SpringApplication.getInstance().getBean(DataSource.class, false);
			namedJdbcTemplate = new JFishNamedJdbcTemplate(ds);
		}
		
		
		logger.info("===>>> batch insert start ...");
		TimeCounter t = new TimeCounter("prepare insert");
		t.start();
		
		BeanWrapper paramsContextBean = SpringUtils.newBeanMapWrapper(invokeContext.getParsedParams());
		List<Map<String, Object>> batchValues = LangUtils.newArrayList(batchParameter.size());
		ParsedSqlWrapper sqlWrapper = ParsedSqlUtils.parseSql(sv.getParsedSql(), em.getSqlParamterPostfixFunctionRegistry());
		for(Object val : batchParameter){
			Map<String, Object> paramValueMap = new HashMap<String, Object>();
			BeanWrapper paramBean = SpringUtils.newBeanWrapper(val);
			
			for(SqlParamterMeta parameter : sqlWrapper.getParameters()){
				Object value = null;
				if(paramBean.isReadableProperty(parameter.getProperty())){
					value = parameter.getParamterValue(paramBean);
//					value = DbmUtils.convertSqlParameterValue(paramBean.getPropertyDescriptor(parameter.getProperty()), value, em.getSqlTypeMapping());
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
//		int[] counts = jdao.getNamedParameterJdbcTemplate().batchUpdate(sv.getParsedSql(), batchValues.toArray(new HashMap[0]));
		int[] counts = namedJdbcTemplate.batchUpdate(sv.getParsedSql(), batchValues.toArray(new HashMap[0]));
		
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
