package org.onetwo.common.spring.context;

import org.onetwo.common.spring.plugin.ContextPluginManagerInitializer;
import org.onetwo.common.spring.plugin.PluginManagerInitializer;
import org.onetwo.common.utils.list.JFishList;
import org.springframework.beans.factory.support.BeanNameGenerator;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.annotation.AnnotatedBeanDefinitionReader;
import org.springframework.context.annotation.AnnotationConfigUtils;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.context.annotation.ScopeMetadataResolver;
import org.springframework.context.support.AbstractRefreshableConfigApplicationContext;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

/****
 * 非web应用程序上下文
 * @author weishao
 *
 */
public class SpringConfigApplicationContext extends AbstractRefreshableConfigApplicationContext {
	

	private PluginManagerInitializer pluginManagerInitializer = new ContextPluginManagerInitializer();
	private String appEnvironment;
	
	private Class<?>[] annotatedClasses;
	private String[] basePackages;
	private BeanNameGenerator beanNameGenerator;
	private ScopeMetadataResolver scopeMetadataResolver;
//	private ContextPluginManager contextPluginManager;
	
	
	@Override
	protected void prepareRefresh() {
		Assert.hasText(appEnvironment);
		JFishList<Class<?>> configClasseList = JFishList.create();
		this.getPluginManagerInitializer().initPluginContext(getAppEnvironment(), configClasseList);
		configClasseList.addArray(annotatedClasses);
		if(configClasseList.isNotEmpty())
			register(configClasseList.toArray(new Class<?>[0]));
		super.prepareRefresh();
	}

	public void register(Class<?>... annotatedClasses) {
		Assert.notEmpty(annotatedClasses, "At least one annotated class must be specified");
		this.annotatedClasses = annotatedClasses;
	}
	
	
	@Override
	protected void loadBeanDefinitions(DefaultListableBeanFactory beanFactory) {
		AnnotatedBeanDefinitionReader reader = new AnnotatedBeanDefinitionReader(beanFactory);
		reader.setEnvironment(this.getEnvironment());

		ClassPathBeanDefinitionScanner scanner = new ClassPathBeanDefinitionScanner(beanFactory);
		scanner.setEnvironment(this.getEnvironment());

		BeanNameGenerator beanNameGenerator = getBeanNameGenerator();
		ScopeMetadataResolver scopeMetadataResolver = getScopeMetadataResolver();
		if (beanNameGenerator != null) {
			reader.setBeanNameGenerator(beanNameGenerator);
			scanner.setBeanNameGenerator(beanNameGenerator);
			beanFactory.registerSingleton(
					AnnotationConfigUtils.CONFIGURATION_BEAN_NAME_GENERATOR, beanNameGenerator);
		}
		if (scopeMetadataResolver != null) {
			reader.setScopeMetadataResolver(scopeMetadataResolver);
			scanner.setScopeMetadataResolver(scopeMetadataResolver);
		}

		if (!ObjectUtils.isEmpty(this.annotatedClasses)) {
			if (logger.isInfoEnabled()) {
				logger.info("Registering annotated classes: [" +
						StringUtils.arrayToCommaDelimitedString(this.annotatedClasses) + "]");
			}
			reader.register(this.annotatedClasses);
		}

		if (!ObjectUtils.isEmpty(this.basePackages)) {
			if (logger.isInfoEnabled()) {
				logger.info("Scanning base packages: [" +
						StringUtils.arrayToCommaDelimitedString(this.basePackages) + "]");
			}
			scanner.scan(this.basePackages);
		}

		String[] configLocations = getConfigLocations();
		if (configLocations != null) {
			for (String configLocation : configLocations) {
				try {
					Class<?> clazz = getClassLoader().loadClass(configLocation);
					if (logger.isInfoEnabled()) {
						logger.info("Successfully resolved class for [" + configLocation + "]");
					}
					reader.register(clazz);
				}
				catch (ClassNotFoundException ex) {
					if (logger.isDebugEnabled()) {
						logger.debug("Could not load class for config location [" + configLocation +
								"] - trying package scan. " + ex);
					}
					int count = scanner.scan(configLocation);
					if (logger.isInfoEnabled()) {
						if (count == 0) {
							logger.info("No annotated classes found for specified class/package [" + configLocation + "]");
						}
						else {
							logger.info("Found " + count + " annotated classes in package [" + configLocation + "]");
						}
					}
				}
			}
		}
	}

	public PluginManagerInitializer getPluginManagerInitializer() {
		return pluginManagerInitializer;
	}

	public void setPluginManagerInitializer(
			PluginManagerInitializer pluginManagerInitializer) {
		this.pluginManagerInitializer = pluginManagerInitializer;
	}

	public Class<?>[] getAnnotatedClasses() {
		return annotatedClasses;
	}

	public String[] getBasePackages() {
		return basePackages;
	}

	public void setBasePackages(String[] basePackages) {
		this.basePackages = basePackages;
	}

	public BeanNameGenerator getBeanNameGenerator() {
		return beanNameGenerator;
	}

	public void setBeanNameGenerator(BeanNameGenerator beanNameGenerator) {
		this.beanNameGenerator = beanNameGenerator;
	}

	public ScopeMetadataResolver getScopeMetadataResolver() {
		return scopeMetadataResolver;
	}

	public void setScopeMetadataResolver(ScopeMetadataResolver scopeMetadataResolver) {
		this.scopeMetadataResolver = scopeMetadataResolver;
	}

	public String getAppEnvironment() {
		return appEnvironment;
	}

	public void setAppEnvironment(String appEnvironment) {
		this.appEnvironment = appEnvironment;
	}
	
	

}
