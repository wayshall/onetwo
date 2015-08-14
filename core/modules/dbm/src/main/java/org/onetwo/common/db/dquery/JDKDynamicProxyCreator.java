package org.onetwo.common.db.dquery;

import java.lang.reflect.Method;

import org.apache.commons.lang3.StringUtils;
import org.onetwo.common.db.dquery.annotation.QueryProvider;
import org.onetwo.common.db.filequery.FileNamedQueryException;
import org.onetwo.common.db.filequery.PropertiesNamespaceInfo;
import org.onetwo.common.db.filequery.QueryProvideManager;
import org.onetwo.common.jdbc.NamedJdbcTemplate;
import org.onetwo.common.spring.SpringUtils;
import org.onetwo.common.spring.sql.JFishNamedFileQueryInfo;
import org.onetwo.common.spring.sql.JFishNamedSqlFileManager;
import org.onetwo.common.utils.propconf.ResourceAdapter;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.util.Assert;

import com.google.common.cache.LoadingCache;

public class JDKDynamicProxyCreator implements InitializingBean, ApplicationContextAware, FactoryBean<Object> {

	private LoadingCache<Method, DynamicMethod> methodCache;
	private ApplicationContext applicationContext;
	private Class<?> interfaceClass;
	private Object targetObject;
	private ResourceAdapter<?> sqlFile;

	private NamedJdbcTemplate namedJdbcTemplate;
//	private JFishNamedSqlFileManager namedSqlFileManager;
//	private QueryProvideManager queryProvideManager;
	
	public JDKDynamicProxyCreator(Class<?> interfaceClass, LoadingCache<Method, DynamicMethod> methodCache) {
		super();
		this.interfaceClass = interfaceClass;
		this.methodCache = methodCache;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		/*if(namedSqlFileManager==null){
			namedSqlFileManager = SpringUtils.getBean(applicationContext, JFishNamedSqlFileManager.class);
		}*/
		if(namedJdbcTemplate==null){
			namedJdbcTemplate = SpringUtils.getBean(applicationContext, NamedJdbcTemplate.class);
		}
		
		QueryProvideManager queryProvideManager;
		QueryProvider queryProvider = this.interfaceClass.getAnnotation(QueryProvider.class);
		if(queryProvider==null){
			queryProvideManager = SpringUtils.getBean(applicationContext, QueryProvideManager.class);
		}else{
			if(StringUtils.isNotBlank(queryProvider.value())){
				queryProvideManager = SpringUtils.getBean(applicationContext, queryProvider.value());
			}else{
				queryProvideManager = SpringUtils.getBean(applicationContext, queryProvider.beanClass());
			}
		}
		if(queryProvideManager==null){
			throw new FileNamedQueryException("no QueryProvideManager found!");
		}
		
		JFishNamedSqlFileManager namedSqlFileManager = (JFishNamedSqlFileManager)queryProvideManager.getFileNamedQueryFactory().getNamespacePropertiesManager();
		Assert.notNull(namedSqlFileManager);
		Assert.notNull(namedJdbcTemplate);
		Assert.notNull(sqlFile);
		
		PropertiesNamespaceInfo<JFishNamedFileQueryInfo> info = namedSqlFileManager.buildSqlFile(sqlFile);
//		interfaceClass = ReflectUtils.loadClass(info.getNamespace());
		if(!interfaceClass.getName().equals(info.getNamespace())){
			throw new FileNamedQueryException("namespace error:  interface->" + interfaceClass+", namespace->"+info.getNamespace());
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

	public void setApplicationContext(ApplicationContext applicationContext) {
		this.applicationContext = applicationContext;
	}

	public void setSqlFile(ResourceAdapter<?> sqlFile) {
		this.sqlFile = sqlFile;
	}

	public void setNamedJdbcTemplate(NamedJdbcTemplate namedJdbcTemplate) {
		this.namedJdbcTemplate = namedJdbcTemplate;
	}

	/*public void setQueryProvideManager(QueryProvideManager queryProvideManager) {
		this.queryProvideManager = queryProvideManager;
	}*/
	
}
