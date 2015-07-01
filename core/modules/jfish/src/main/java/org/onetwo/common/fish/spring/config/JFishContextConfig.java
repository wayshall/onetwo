package org.onetwo.common.fish.spring.config;

import java.util.List;
import java.util.Properties;

import org.onetwo.common.fish.utils.ContextHolder;
import org.onetwo.common.outer.CodeMessager;
import org.onetwo.common.spring.SpringUtils;
import org.onetwo.common.spring.config.JFishProfiles;
import org.onetwo.common.spring.context.BaseApplicationContextSupport;
import org.onetwo.common.spring.context.SpringProfilesWebApplicationContext;
import org.onetwo.common.spring.rest.JFishRestTemplate;
import org.onetwo.common.spring.web.WebRequestHolder;
import org.onetwo.common.spring.web.mvc.DefaultCodeMessager;
import org.onetwo.common.spring.web.mvc.MvcSetting;
import org.onetwo.common.spring.web.reqvalidator.JFishRequestValidator;
import org.onetwo.common.spring.web.reqvalidator.JFishUploadFileTypesChecker;
import org.onetwo.common.spring.web.tag.CookiesTagThemeSettting;
import org.onetwo.common.spring.web.tag.SessionTagThemeSettting;
import org.onetwo.common.spring.web.tag.ThemeSettingWebFilter;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.utils.propconf.AppConfig;
import org.onetwo.common.utils.propconf.Environment;
import org.onetwo.common.web.config.BaseSiteConfig;
import org.onetwo.common.web.utils.WebHolderManager;
import org.onetwo.common.web.view.DefaultTagThemeSetting;
import org.onetwo.common.web.view.ThemeSetting;
import org.springframework.cache.CacheManager;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.cache.ehcache.EhCacheManagerFactoryBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.Profile;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.core.io.Resource;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.multipart.support.MultipartFilter;
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
//@Import({JFishProfiles.class, DirFreemarkerConfig.class})
@Import({JFishProfiles.class})
public class JFishContextConfig extends BaseApplicationContextSupport {
	public static class ContextBeanNames {
		public static final String EXCEPTION_MESSAGE = "exceptionMessages";
	}
	
	public static final String MVC_CONFIG = "mvcConfig";
	
	@javax.annotation.Resource
	private ApplicationContext applicationContext;

//	@Value("${jfish.base.packages}")
//	private String jfishBasePackages;

//	private ContextPluginManager contextPluginManager;


	public JFishContextConfig() {
		// this.jfAppConfigurator =
		// BaseSiteConfig.getInstance().getWebAppConfigurator(JFishAppConfigurator.class);
	}

	@Bean
	public AppConfig appConfig(){
//		AppConfig appConfig = SpringUtils.getBean(applicationContex, AppConfig.class);
		return BaseSiteConfig.getInstance();
	}
	/*
	@Bean
	public ThemeSetting themeSetting(){
		String tagSetting = BaseSiteConfig.getInstance().getThemeSetting();
		if(SessionTagThemeSettting.CONFIG_KEY.equals(tagSetting)){
			return new SessionTagThemeSettting();
		}else if(CookiesTagThemeSettting.CONFIG_KEY.equals(tagSetting)){
			return new CookiesTagThemeSettting();
		}else{
			return new DefaultTagThemeSetting();
		}
	}
	
	@Bean
	public ThemeSettingWebFilter themeSettingWebFilter(){
		return new ThemeSettingWebFilter();
	}*/
	
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
	public RestTemplate restTemplate() {
		RestTemplate rest = new JFishRestTemplate();
		return rest;
	}

	@Bean(name = MVC_CONFIG)
	public Properties mvcConfig() {
		Properties prop = SpringUtils.createProperties("/mvc/mvc.properties", true);
		return prop;
	}

	@Bean
	public MvcSetting mvcSetting(){
		return new MvcSetting(mvcConfig());
	}
	
	@Bean(name=MultipartFilter.DEFAULT_MULTIPART_RESOLVER_BEAN_NAME)
	public MultipartResolver filterMultipartResolver(){
		CommonsMultipartResolver multipart = new CommonsMultipartResolver();
		multipart.setMaxUploadSize(mvcSetting().getMaxUploadSize());
		return multipart;
	}
	protected CacheManager ehcacheCacheManager(){
		Resource configLocation = SpringUtils.newClassPathResource("cache/ehcache.xml");
		if(!configLocation.exists()){
			return null;
		}
		net.sf.ehcache.CacheManager cm = null;
		if(SpringProfilesWebApplicationContext.class.isInstance(applicationContex)){
			SpringProfilesWebApplicationContext jfishWebapp = (SpringProfilesWebApplicationContext) applicationContex;
			cm = jfishWebapp.registerAndGetBean(EhCacheManagerFactoryBean.class, "configLocation", configLocation);
		}else{
			cm = SpringUtils.registerBean(applicationContex, EhCacheManagerFactoryBean.class, "configLocation", configLocation);
		}
		EhCacheCacheManager cacheManager = new EhCacheCacheManager();
		cacheManager.setCacheManager(cm);
		return cacheManager;
	}

	@Bean
	public WebHolderManager webHolderManager() {
		WebHolderManager webHolderManager = new WebHolderManager();
		return webHolderManager;
	}
	
	@Bean
	public JFishRequestValidator fileTypesRequestValidator(){
		JFishUploadFileTypesChecker validator = new JFishUploadFileTypesChecker();
		List<String> allowed = this.mvcSetting().getAllowedFileTypes();
		if(LangUtils.isNotEmpty(allowed)){
			validator.setAllowedFileTypes(allowed);
		}
		return validator;
	}
/*	@Bean
	public ThreadLocalCleaner ThreadLocalCleaner() {
		ThreadLocalCleaner cleaner = new ThreadLocalCleaner();
		return cleaner;
	}
*/
	@Bean
	public ContextHolder contextHolder(){
		return new WebRequestHolder();
	}

	@Bean
	public ThemeSetting themeSetting(){
		String tagSetting = BaseSiteConfig.getInstance().getThemeSetting();
		if(SessionTagThemeSettting.CONFIG_KEY.equals(tagSetting)){
			return new SessionTagThemeSettting();
		}else if(CookiesTagThemeSettting.CONFIG_KEY.equals(tagSetting)){
			return new CookiesTagThemeSettting();
		}else{
			return new DefaultTagThemeSetting();
		}
	}
	@Bean
	public ThemeSettingWebFilter themeSettingWebFilter(){
		return new ThemeSettingWebFilter();
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
	

	@Bean
	public CodeMessager codeMessager(){
		CodeMessager messager = SpringUtils.getBean(applicationContext, CodeMessager.class);;
		if(messager==null){
			messager = new DefaultCodeMessager();
		}
		return messager;
	}
	

	@Bean(name=ContextBeanNames.EXCEPTION_MESSAGE)
	public MessageSource exceptionMessageSource(){
		ReloadableResourceBundleMessageSource ms = new ReloadableResourceBundleMessageSource();
		ms.setCacheSeconds(BaseSiteConfig.getInstance().getMessageCacheSecond());
		ms.setBasenames("classpath:messages/ExceptionMessages", "classpath:messages/DefaultExceptionMessages");
		return ms;
	}

}