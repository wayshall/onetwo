package org.onetwo.common.fish.spring.config;

import javax.validation.Validator;

import org.onetwo.common.fish.plugin.JFishPluginManager;
import org.onetwo.common.fish.plugin.JFishPluginManagerFactory;
import org.onetwo.common.fish.utils.ThreadLocalCleaner;
import org.onetwo.common.spring.SpringUtils;
import org.onetwo.common.spring.cache.JFishSimpleCacheManagerImpl;
import org.onetwo.common.spring.config.JFishProfiles;
import org.onetwo.common.spring.context.AbstractJFishAnnotationConfig;
import org.onetwo.common.spring.dozer.DozerBeanFactoryBean;
import org.onetwo.common.spring.rest.JFishRestTemplate;
import org.onetwo.common.spring.validator.JFishTraversableResolver;
import org.onetwo.common.spring.validator.ValidatorWrapper;
import org.onetwo.common.utils.propconf.AppConfig;
import org.onetwo.common.utils.propconf.Environment;
import org.onetwo.common.web.config.BaseSiteConfig;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.cache.ehcache.EhCacheManagerFactoryBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.Profile;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.core.io.Resource;
import org.springframework.util.ClassUtils;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;

/*******
 * 负责applicationContext.xml上下文初始化
 * 
 * initialize in web app start
 * in JFishWebApplicationContext
 * @author wayshall
 *
 */
@Configuration
@ImportResource({ "classpath*:jfish-spring.xml", "classpath:applicationContext.xml" })
@Import(JFishProfiles.class)
public class JFishContextConfig implements ApplicationContextAware {

	private ApplicationContext applicationContex;

	@Value("${jfish.base.packages}")
	private String jfishBasePackages;


	public JFishContextConfig() {
		// this.jfAppConfigurator =
		// BaseSiteConfig.getInstance().getWebAppConfigurator(JFishAppConfigurator.class);
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContex = applicationContext;
		// listenerManager.addListener(pluginManager());
	}

	public ApplicationContext getApplicationContex() {
		return applicationContex;
	}
	
	@Bean
	public AppConfig appConfig(){
		return BaseSiteConfig.getInstance();
	}

	@Bean
	public JFishAppConfigrator jfishAppConfigurator() {
		JFishAppConfigrator jfAppConfigurator = SpringUtils.getBean(applicationContex, JFishAppConfigrator.class);
		if (jfAppConfigurator == null) {
			jfAppConfigurator = BaseSiteConfig.getInstance().getWebAppConfigurator(JFishAppConfigrator.class);
		}
		if(jfAppConfigurator==null){
			jfAppConfigurator = new JFishAppConfigrator() {
				
				@Override
				public String[] getXmlBasePackages() {
					return new String[]{jfishBasePackages};
				}
				
				@Override
				public String getJFishBasePackage() {
					return jfishBasePackages;
				}
			};
		}
		return jfAppConfigurator;
	}

	@Bean
	public JFishPluginManager jfishPluginManager() {
		return JFishPluginManagerFactory.getPluginManager();
	}

	@Bean
	public RestTemplate restTemplate() {
		RestTemplate rest = new JFishRestTemplate();
		return rest;
	}

	@Bean
	public Validator beanValidator() {
		Validator validator = null;
		if (ClassUtils.isPresent("javax.validation.Validator", getClass().getClassLoader())) {
			Class<?> clazz;
			try {
				String className = "org.springframework.validation.beanvalidation.LocalValidatorFactoryBean";
				clazz = ClassUtils.forName(className, WebMvcConfigurationSupport.class.getClassLoader());
			} catch (ClassNotFoundException e) {
				throw new BeanInitializationException("Could not find default validator", e);
			} catch (LinkageError e) {
				throw new BeanInitializationException("Could not find default validator", e);
			}
			validator = (Validator) BeanUtils.instantiate(clazz);
			LocalValidatorFactoryBean vfb = (LocalValidatorFactoryBean) validator;
			vfb.setValidationMessageSource(validateMessageSource());
			vfb.setTraversableResolver(new JFishTraversableResolver());
		}
		return validator;
	}
	
	@Bean
	public ValidatorWrapper validatorWrapper(){
		return ValidatorWrapper.wrap(beanValidator());
	}

	@Bean
	public ReloadableResourceBundleMessageSource validateMessageSource() {
		ReloadableResourceBundleMessageSource ms = null;
		if(this.applicationContex.containsBean("validationMessages")){
			ms = this.applicationContex.getBean("validationMessages", ReloadableResourceBundleMessageSource.class);
		}else{
			ms = new ReloadableResourceBundleMessageSource();
			ms.setBasename("classpath:messages/ValidationMessages");
		}
//		ms.setCacheSeconds(60);
		return ms;
	}
	
	@Bean(name = "cacheManager")
	public CacheManager cacheManager() {
		CacheManager cache = null;
		Resource res = SpringUtils.newClassPathResource("cache/ehcache.xml");
		if(res.exists()){
			cache = ehcacheCacheManager(res);
		}else{
			cache = jfishSimpleCacheManager();
		}
		
		return cache;
	}

	@Bean(name = "jfishSimpleCacheManager")
	public CacheManager jfishSimpleCacheManager() {
		JFishSimpleCacheManagerImpl cache = new JFishSimpleCacheManagerImpl();
		return cache;
	}
	
	protected CacheManager ehcacheCacheManager(Resource configLocation){
		net.sf.ehcache.CacheManager cm = null;
		if(AbstractJFishAnnotationConfig.class.isInstance(applicationContex)){
			AbstractJFishAnnotationConfig jfishWebapp = (AbstractJFishAnnotationConfig) applicationContex;
			cm = jfishWebapp.registerAndGetBean(EhCacheManagerFactoryBean.class, "configLocation", configLocation);
		}else{
			cm = SpringUtils.registerBean(applicationContex, EhCacheManagerFactoryBean.class, "configLocation", configLocation);
		}
		EhCacheCacheManager cacheManager = new EhCacheCacheManager();
		cacheManager.setCacheManager(cm);
		return cacheManager;
	}

	@Bean
	public ThreadLocalCleaner ThreadLocalCleaner() {
		ThreadLocalCleaner cleaner = new ThreadLocalCleaner();
		return cleaner;
	}

	@Bean
	public DozerBeanFactoryBean dozerBeanFactoryBean(){
		DozerBeanFactoryBean f = new DozerBeanFactoryBean();
		f.setBasePackage(jfishBasePackages);
		return f;
	}

	@Configuration
	@Profile(Environment.TEST)
	static class TestConcfig {

		@Bean
		public RequestMappingHandlerAdapter handlerAdapter() {
			RequestMappingHandlerAdapter ha = new RequestMappingHandlerAdapter();
			return ha;
		}
	}

}