package org.onetwo.plugins.dq;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.onetwo.common.db.CreateQueryable;
import org.onetwo.common.db.DataQuery;
import org.onetwo.common.db.FileNamedSqlGenerator;
import org.onetwo.common.db.SqlAndValues;
import org.onetwo.common.exception.BaseException;
import org.onetwo.common.jdbc.JdbcDao;
import org.onetwo.common.log.MyLoggerFactory;
import org.onetwo.common.spring.SpringApplication;
import org.onetwo.common.spring.SpringUtils;
import org.onetwo.common.spring.sql.SqlUtils;
import org.onetwo.common.spring.sql.SqlUtils.ParsedSqlWrapper;
import org.onetwo.common.spring.sql.SqlUtils.ParsedSqlWrapper.SqlParamterMeta;
import org.onetwo.common.utils.ClassUtils;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.utils.Page;
import org.onetwo.common.utils.ReflectUtils;
import org.onetwo.common.utils.convert.Types;
import org.onetwo.common.utils.propconf.AbstractPropertiesManager.NamespaceProperty;
import org.slf4j.Logger;
import org.springframework.beans.BeanWrapper;
import org.springframework.cache.Cache;
import org.springframework.cache.Cache.ValueWrapper;

public class DynamicQueryHandler implements InvocationHandler {
	
	protected Logger logger = MyLoggerFactory.getLogger(this.getClass());

	private Cache methodCache;
	private CreateQueryable em;
	private Object proxyObject;
	private List<Method> excludeMethods = new ArrayList<Method>();
	private boolean debug = true;
//	private ParameterNameDiscoverer pnd = new LocalVariableTableParameterNameDiscoverer();
//	private Map<String, Method> methodCache = new HashMap<String, Method>();
	private JdbcDao jdao;
	

	public DynamicQueryHandler(CreateQueryable em, Cache methodCache, Class<?>... proxiedInterfaces){
		this(em, methodCache, SpringApplication.getInstance().getBean(JdbcDao.class, false), proxiedInterfaces);
	}
	
	public DynamicQueryHandler(CreateQueryable em, Cache methodCache, JdbcDao jdao, Class<?>... proxiedInterfaces){
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
		} catch (Throwable e) {
			throw new BaseException("invoke query["+method.getDeclaringClass().getSimpleName()+"."+method.getName()+"] error : " + e.getMessage(), e);
		}
		
	}
	
	protected DynamicMethod getDynamicMethod(Method method){
		if(methodCache!=null){
			ValueWrapper value = methodCache.get(method);
			if(value!=null)
				return (DynamicMethod) value.get();
			DynamicMethod dm = new DynamicMethod(method);
			methodCache.put(method, dm);
			return dm;
		}else{
			return new DynamicMethod(method);
		}
	}
	
	public Object doInvoke(Object proxy, Method method, Object[] args) throws Throwable {
//		LangUtils.println("proxy: "+proxy+", method: ${0}", method);
//		TimeCounter t = new TimeCounter("doproxy", true);
//		t.start();
		DynamicMethod dmethod = getDynamicMethod(method);
		Class<?> resultClass = dmethod.getResultClass();
		Class<?> componentClass = dmethod.getComponentClass();
		String queryName = dmethod.getQueryName();

		if(debug)
			logger.info("{}: {}", method.getDeclaringClass().getSimpleName()+"."+method.getName(), LangUtils.toString(args));
		
		Object result = null;
		Object[] methodArgs = null;
		if(Page.class.isAssignableFrom(resultClass)){
			Page<?> page = (Page<?>)args[0];
			
//			Object[] trimPageArgs = ArrayUtils.remove(args, 0);
			methodArgs = dmethod.toArrayByArgs(args, componentClass);
			result = em.getFileNamedQueryFactory().findPage(queryName, page, methodArgs);
			
		}else if(List.class.isAssignableFrom(resultClass)){
			methodArgs = dmethod.toArrayByArgs(args, componentClass);
//			logger.info("dq args: {}", LangUtils.toString(methodArgs));
			result = em.getFileNamedQueryFactory().findList(queryName, methodArgs);
			
		}else if(DataQuery.class.isAssignableFrom(resultClass)){
			methodArgs = dmethod.toArrayByArgs(args, null);
//			logger.info("dq args: {}", LangUtils.toString(methodArgs));
			DataQuery dq = em.getFileNamedQueryFactory().createQuery(queryName, methodArgs);
			return dq;
			
		}else{
//			logger.info("dq args: {}", LangUtils.toString(methodArgs));
			if(dmethod.isExecuteUpdate()){
				methodArgs = dmethod.toArrayByArgs(args, componentClass);
				DataQuery dq = em.getFileNamedQueryFactory().createQuery(queryName, methodArgs);
				result = dq.executeUpdate();
				
			}else if(dmethod.isBatch()){
				//TODO 先特殊处理，待修改
				if(LangUtils.size(args)!=1 || !List.class.isInstance(args[0])){
					throw new BaseException("batch execute only has a List Type paramter : " + args);
				}
				FileNamedSqlGenerator<NamespaceProperty> sqlGen = (FileNamedSqlGenerator<NamespaceProperty>)em.getFileNamedQueryFactory().createFileNamedSqlGenerator(queryName);
				SqlAndValues sv = sqlGen.generatSql();
				JdbcDao jdao = this.jdao;
				if(jdao==null){
					jdao = SpringApplication.getInstance().getBean(JdbcDao.class, false);
					if(jdao==null)
						throw new BaseException("no supported jdbc batch execute!");
				}
				
				List<?> batchParameter = (List<?>)args[0];
				List<Map<String, Object>> batchValues = LangUtils.newArrayList(batchParameter.size());
				ParsedSqlWrapper sqlWrapper = SqlUtils.parseSql(sv.getParsedSql());
				for(Object val : batchParameter){
					Map<String, Object> paramValueMap = new HashMap<String, Object>();
					BeanWrapper paramBean = SpringUtils.newBeanWrapper(val);
					for(SqlParamterMeta parameter : sqlWrapper.getParameters()){
						if(!paramBean.isReadableProperty(parameter.getProperty()))
							throw new BaseException("batch execute parameter["+parameter.getProperty()+"] is not a bean["+val+"] property");
						Object value = parameter.getParamterValue(paramBean);
						paramValueMap.put(parameter.getName(), value);
					}
					batchValues.add(paramValueMap);
				}
				int[] counts = jdao.getNamedParameterJdbcTemplate().batchUpdate(sv.getParsedSql(), batchValues.toArray(new HashMap[0]));
				if(dmethod.getResultClass()==int[].class || dmethod.getResultClass()==Integer[].class){
					return counts;
				}else{
					int count = LangUtils.sum(counts);
					return Types.convertValue(count, dmethod.getResultClass());
				}
				
			}else{
				methodArgs = dmethod.toArrayByArgs(args, componentClass);
				result = em.getFileNamedQueryFactory().findUnique(queryName, methodArgs);
			}
		}
		return result;
	}

//	public Object[] appendEntityClass(Class<?> entityClass, Object[] args){
//		if(entityClass==null || entityClass==Object.class)
//			return args;
//		Object[] result = ArrayUtils.addAll(args, new Object[]{JNamedQueryKey.ResultClass, entityClass});
//		return result;
//	}
	
	public Object getQueryObject(){
		return this.proxyObject;
	}
	
}
