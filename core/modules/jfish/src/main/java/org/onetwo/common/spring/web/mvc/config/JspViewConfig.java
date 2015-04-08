package org.onetwo.common.spring.web.mvc.config;

import org.onetwo.common.excel.view.jsp.DatagridExcelModelBuilder;
import org.onetwo.common.spring.web.mvc.JFishInternalResourceViewResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

@Configuration
public class JspViewConfig {
	
//	@Resource
//	private AppConfig appConfig;
	
	@Bean
	public InternalResourceViewResolver jspResolver(){
//		BaseSiteConfig siteconfig = (BaseSiteConfig)appConfig;
		JFishInternalResourceViewResolver jspResoler = new JFishInternalResourceViewResolver();
		jspResoler.setSuffix(".jsp");
//		jspResoler.setPrefix("/WEB-INF/views/");
		jspResoler.setPrefix("/WEB-INF");
//		jspResoler.setThemeSetting(themeSetting());
		return jspResoler;
	}

	@Bean
	public DatagridExcelModelBuilder datagridExcelModelBuilder(){
		return new DatagridExcelModelBuilder();
	}
	
}
