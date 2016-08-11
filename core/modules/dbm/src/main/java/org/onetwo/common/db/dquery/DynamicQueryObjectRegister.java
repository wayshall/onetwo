package org.onetwo.common.db.dquery;

import java.lang.reflect.Method;
import java.util.Map;

import org.onetwo.common.db.filequery.NamespacePropertiesManager;
import org.onetwo.common.db.filequery.SpringBasedSqlFileScanner;
import org.onetwo.common.db.filequery.SqlFileScanner;
import org.onetwo.common.log.JFishLoggerFactory;
import org.onetwo.common.propconf.ResourceAdapter;
import org.onetwo.common.reflect.ReflectUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.util.ClassUtils;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

public class DynamicQueryObjectRegister {
	protected final Logger logger = JFishLoggerFactory.getLogger(this.getClass());

	private SqlFileScanner sqlFileScanner = new SpringBasedSqlFileScanner(ClassUtils.getDefaultClassLoader());

	private LoadingCache<Method, DynamicMethod> methodCache = CacheBuilder.newBuilder()
																.build(new CacheLoader<Method, DynamicMethod>() {
																	@Override
																	public DynamicMethod load(Method method) throws Exception {
																		return DynamicMethod.newDynamicMethod(method);
																	}
																});

	private String databaseName;
	private BeanDefinitionRegistry registry;
	
	public DynamicQueryObjectRegister(BeanDefinitionRegistry registry) {
		super();
		this.registry = registry;
	}
	

	public void setDatabaseName(String databaseName) {
		this.databaseName = databaseName;
	}

	public void registerQueryBeans() {
		Map<String, ResourceAdapter<?>> sqlfiles = sqlFileScanner.scanMatchSqlFiles(databaseName);
		sqlfiles.entrySet().forEach(f->{
			/*final String fileName = f.getName();
			String className = StringUtils.substring(fileName, 0, fileName.length()-SqlFileScanner.JFISH_SQL_POSTFIX.length());*/
			String className = f.getKey();
			if(NamespacePropertiesManager.GLOBAL_NS_KEY.equalsIgnoreCase(className)){
				return ;
			}
			final Class<?> interfaceClass = ReflectUtils.loadClass(className);
			BeanDefinition beandef = BeanDefinitionBuilder.rootBeanDefinition(JDKDynamicProxyCreator.class)
								.addConstructorArgValue(interfaceClass)
								.addConstructorArgValue(methodCache)
								.addPropertyValue("sqlFile", f.getValue())
								.setScope(BeanDefinition.SCOPE_SINGLETON)
//								.setRole(BeanDefinition.ROLE_APPLICATION)
								.getBeanDefinition();
			registry.registerBeanDefinition(className, beandef);
			logger.info("register dao bean: {} -> {}", className, f.getValue().getFile());
		});
		
	}
}
