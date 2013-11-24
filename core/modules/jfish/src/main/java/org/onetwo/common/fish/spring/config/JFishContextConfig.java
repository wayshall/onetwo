package org.onetwo.common.fish.spring.config;

import org.onetwo.common.fish.plugin.JFishPluginManager;
import org.onetwo.common.fish.plugin.JFishPluginManagerFactory;
import org.onetwo.common.fish.utils.ContextHolder;
import org.onetwo.common.fish.utils.ThreadLocalCleaner;
import org.onetwo.common.spring.SpringUtils;
import org.onetwo.common.spring.config.JFishProfiles;
import org.onetwo.common.spring.context.AbstractJFishAnnotationConfig;
import org.onetwo.common.spring.dozer.DozerBeanFactoryBean;
import org.onetwo.common.spring.rest.JFishRestTemplate;
import org.onetwo.common.spring.web.WebRequestHolder;
import org.onetwo.common.utils.propconf.Environment;
import org.onetwo.common.web.config.BaseApplicationContextSupport;
import org.onetwo.common.web.config.BaseSiteConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.cache.ehcache.EhCacheManagerFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.Resource;
import org.springframework.web.client.RestTemplate;
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
public class JFishContextConfig extends BaseApplicationContextSupport {

	@Value("${jfish.base.packages}")
	private String jfishBasePackages;


	public JFishContextConfig() {
		// this.jfAppConfigurator =
		// BaseSiteConfig.getInstance().getWebAppConfigurator(JFishAppConfigurator.class);
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

	
	protected CacheManager ehcacheCacheManager(){
		Resource configLocation = SpringUtils.newClassPathResource("cache/ehcache.xml");
		if(!configLocation.exists()){
			return null;
		}
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
	public ContextHolder contextHolder(){
		return new WebRequestHolder();
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