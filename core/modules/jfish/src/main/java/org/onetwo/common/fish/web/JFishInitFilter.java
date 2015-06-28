package org.onetwo.common.fish.web;

import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;

import org.onetwo.common.web.config.BaseSiteConfig;
import org.onetwo.common.web.filter.BaseInitFilter;
import org.springframework.web.context.WebApplicationContext;

/****
 * 自定义的过滤器
 * @author weishao
 *
 */
public class JFishInitFilter extends BaseInitFilter {

//	private AppConfigProvider webConfigProvider;
	

	protected void initWithWebApplicationContext(FilterConfig config, WebApplicationContext app) {
		ServletContext context = config.getServletContext();
		/*webConfigProvider = SpringApplication.getInstance().getBean(AppConfigProvider.class);
		if(webConfigProvider!=null){
			logger.info("find webConfigProvider : {}", webConfigProvider);
		}else{
			logger.info("no webConfigProvider found.");
		}*/


		BaseSiteConfig siteConfig = getBaseSiteConfig().initWeb(config);
//		Object webconfig = webConfigProvider==null?null:webConfigProvider.createWebConfig(config);
		//webconfig
//		siteConfig.setWebAppConfigurator(webconfig);
		siteConfig.getFreezer().freezing();
		context.setAttribute(BaseSiteConfig.CONFIG_NAME, siteConfig);
//		context.setAttribute(BaseSiteConfig.WEB_CONFIG_NAME, webconfig);
		
		//xss
		this.setPreventXssRequest(BaseSiteConfig.getInstance().isPreventXssRequest());
		
	}
	
	protected BaseSiteConfig getBaseSiteConfig(){
		return BaseSiteConfig.getInstance();
	}
	
	/*protected Object getWebConfig(BaseSiteConfig siteConfig){
		return webConfigProvider==null?null:webConfigProvider.createWebConfig(siteConfig);
	}*/
	
}
