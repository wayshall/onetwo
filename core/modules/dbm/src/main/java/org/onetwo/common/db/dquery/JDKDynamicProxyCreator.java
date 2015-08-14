package org.onetwo.common.db.dquery;

import org.onetwo.common.db.filequery.PropertiesNamespaceInfo;
import org.onetwo.common.db.filequery.QueryProvideManager;
import org.onetwo.common.exception.BaseException;
import org.onetwo.common.jdbc.JdbcDao;
import org.onetwo.common.jdbc.NamedJdbcTemplate;
import org.onetwo.common.spring.SpringUtils;
import org.onetwo.common.spring.sql.JFishNamedFileQueryInfo;
import org.onetwo.common.spring.sql.JFishNamedSqlFileManager;
import org.onetwo.common.utils.propconf.ResourceAdapter;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.cache.Cache;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.util.Assert;

public class JDKDynamicProxyCreator implements InitializingBean, ApplicationContextAware, FactoryBean<Object> {
	
	private ApplicationContext applicationContext;
	private Class<?> interfaceClass;
	private Object targetObject;
	private ResourceAdapter<?> sqlFile;

	private Cache methodCache;
	private NamedJdbcTemplate namedJdbcTemplate;
//	private JFishNamedSqlFileManager namedSqlFileManager;
	private QueryProvideManager queryProvideManager;
	
	public JDKDynamicProxyCreator(Class<?> interfaceClass) {
		super();
		this.interfaceClass = interfaceClass;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		/*if(namedSqlFileManager==null){
			namedSqlFileManager = SpringUtils.getBean(applicationContext, JFishNamedSqlFileManager.class);
		}*/
		if(namedJdbcTemplate==null){
			namedJdbcTemplate = SpringUtils.getBean(applicationContext, NamedJdbcTemplate.class);
		}
		if(methodCache==null){
			methodCache = SpringUtils.getBean(applicationContext, Cache.class);
		}
		if(queryProvideManager==null){
			queryProvideManager = SpringUtils.getBean(applicationContext, QueryProvideManager.class);
		}
		
		JFishNamedSqlFileManager namedSqlFileManager = (JFishNamedSqlFileManager)queryProvideManager.getFileNamedQueryFactory().getNamespacePropertiesManager();
		Assert.notNull(namedSqlFileManager);
		Assert.notNull(namedJdbcTemplate);
		Assert.notNull(sqlFile);
		
		PropertiesNamespaceInfo<JFishNamedFileQueryInfo> info = namedSqlFileManager.buildSqlFile(sqlFile);
//		interfaceClass = ReflectUtils.loadClass(info.getNamespace());
		if(!interfaceClass.getName().equals(info.getNamespace())){
			throw new BaseException("namespace error:  interface->" + interfaceClass+", namespace->"+info.getNamespace());
		}
		targetObject = new DynamicQueryHandler(queryProvideManager, methodCache, namedJdbcTemplate, interfaceClass).getQueryObject();
	}

	@Override
	public Object getObject() throws Exception {
		return targetObject;
	}

	@Override
	public Class<?> getObjectType() {
		return interfaceClass;
	}

	@Override
	public boolean isSingleton() {
		return true;
	}

	public void setMethodCache(Cache methodCache) {
		this.methodCache = methodCache;
	}

	public void setApplicationContext(ApplicationContext applicationContext) {
		this.applicationContext = applicationContext;
	}

	public void setSqlFile(ResourceAdapter<?> sqlFile) {
		this.sqlFile = sqlFile;
	}

	public void setNamedJdbcTemplate(NamedJdbcTemplate namedJdbcTemplate) {
		this.namedJdbcTemplate = namedJdbcTemplate;
	}

	public void setQueryProvideManager(QueryProvideManager queryProvideManager) {
		this.queryProvideManager = queryProvideManager;
	}
	
}
