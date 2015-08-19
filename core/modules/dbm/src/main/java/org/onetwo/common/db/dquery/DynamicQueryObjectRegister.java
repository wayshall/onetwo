package org.onetwo.common.db.dquery;

import java.lang.reflect.Method;
import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;
import org.onetwo.common.db.filequery.NamespacePropertiesManager;
import org.onetwo.common.db.filequery.SpringBasedSqlFileScanner;
import org.onetwo.common.db.filequery.SqlFileScanner;
import org.onetwo.common.log.JFishLoggerFactory;
import org.onetwo.common.utils.ReflectUtils;
import org.onetwo.common.utils.propconf.ResourceAdapter;
import org.slf4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.util.ClassUtils;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;


public class DynamicQueryObjectRegister implements /*FileNamedQueryFactoryListener, */ BeanDefinitionRegistryPostProcessor {
	protected final Logger logger = JFishLoggerFactory.getLogger(this.getClass());

	private SqlFileScanner sqlFileScanner = new SpringBasedSqlFileScanner(ClassUtils.getDefaultClassLoader());

	private LoadingCache<Method, DynamicMethod> methodCache = CacheBuilder.newBuilder()
																.build(new CacheLoader<Method, DynamicMethod>() {
																	@Override
																	public DynamicMethod load(Method method) throws Exception {
																		return DynamicMethod.newDynamicMethod(method);
																	}
																});

	@Override
	public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
	}

	
	@Override
	public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
		ResourceAdapter<?>[] sqlfiles = sqlFileScanner.scanMatchSqlFiles();
		Stream.of(sqlfiles).forEach(f->{
			final String fileName = f.getName();
			String className = StringUtils.substring(fileName, 0, fileName.length()-SqlFileScanner.JFISH_SQL_POSTFIX.length());
			if(NamespacePropertiesManager.GLOBAL_NS_KEY.equalsIgnoreCase(className)){
				return ;
			}
			final Class<?> interfaceClass = ReflectUtils.loadClass(className);
			BeanDefinition beandef = BeanDefinitionBuilder.rootBeanDefinition(JDKDynamicProxyCreator.class)
								.addConstructorArgValue(interfaceClass)
								.addConstructorArgValue(methodCache)
								.addPropertyValue("sqlFile", f)
								.setScope(BeanDefinition.SCOPE_SINGLETON)
//								.setRole(BeanDefinition.ROLE_APPLICATION)
								.getBeanDefinition();
			registry.registerBeanDefinition(className, beandef);
			logger.info("register dao bean: {} ", className);
		});
		
	}
	
}
