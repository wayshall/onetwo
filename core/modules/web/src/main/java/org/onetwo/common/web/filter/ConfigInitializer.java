package org.onetwo.common.web.filter;

import java.util.List;

import javax.servlet.ServletContext;

import org.onetwo.common.exception.BaseException;
import org.onetwo.common.log.JFishLoggerFactory;
import org.onetwo.common.spring.Springs;
import org.onetwo.common.spring.SpringUtils;
import org.slf4j.Logger;
import org.springframework.util.Assert;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

public class ConfigInitializer  {
	public static final String SITE_CONFIG_NAME = "siteConfig";
	public static final String SITE_CONFIG_NAME_ALIAS = "site";
	public static final String JNA_LIBRARY_PATH = "jna.library.path";
	
	protected final Logger logger = JFishLoggerFactory.getLogger(this.getClass());

	private SiteConfig siteConfig;
	final private ServletContext servletContext;
	

	public ConfigInitializer(ServletContext servletContext) {
		super();
		this.servletContext = servletContext;
	}

	public void initialize() {
		logger.info("正在初始化配置...");
		String libraryPath = System.getProperty(JNA_LIBRARY_PATH);
		logger.info("jna.library.path: {}", libraryPath);
		
		ServletContext context = servletContext;
		
		WebApplicationContext app = WebApplicationContextUtils.getRequiredWebApplicationContext(context);
		if(app==null){
			throw new BaseException("the ConfigInitializer must be invoke afater WebApplicationContext init!");
		}
//		SpringApplication.initApplication(app);
		Springs.initApplicationIfNotInitialized(app);
		
		SiteConfigProvider<?> webConfigProvider = SpringUtils.getBean(app, SiteConfigProvider.class);
		if(webConfigProvider!=null){
			siteConfig = webConfigProvider.initWebConfig(servletContext);
			Assert.notNull(siteConfig);
			context.setAttribute(SITE_CONFIG_NAME, siteConfig);
			context.setAttribute(SITE_CONFIG_NAME_ALIAS, siteConfig);
//			context.setAttribute(WEB_CONFIG_NAME, webConfigProvider.createWebConfig(config));
			logger.info("find webConfigProvider : {}", webConfigProvider);
			
			this.initOnAppConfig(siteConfig);
		}else{
//			siteConfig = AppConfig.create(true);
			logger.info("no webConfigProvider found.");
		}
		
		List<WebContextConfigProvider> configs = SpringUtils.getBeans(app, WebContextConfigProvider.class);
		configs.stream().forEach(cnf->{
			context.setAttribute(cnf.getConfigName(), cnf.getWebConfig(servletContext));
			logger.info("find WebContextConfigProvider : {} -> {}", cnf.getConfigName(), cnf);
		});
		
	}

	protected void initOnAppConfig(SiteConfig appConfig){
		//xss
	}

}
