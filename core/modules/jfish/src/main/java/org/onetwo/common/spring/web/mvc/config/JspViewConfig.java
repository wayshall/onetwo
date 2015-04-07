package org.onetwo.common.spring.web.mvc.config;

import org.onetwo.common.excel.view.jsp.DatagridExcelModelBuilder;
import org.onetwo.common.spring.web.mvc.JFishInternalResourceViewResolver;
import org.onetwo.common.spring.web.tag.CookiesTagThemeSettting;
import org.onetwo.common.spring.web.tag.SessionTagThemeSettting;
import org.onetwo.common.spring.web.tag.ThemeSettingWebFilter;
import org.onetwo.common.web.config.BaseSiteConfig;
import org.onetwo.common.web.view.DefaultTagThemeSetting;
import org.onetwo.common.web.view.ThemeSetting;
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
	public DatagridExcelModelBuilder datagridExcelModelBuilder(){
		return new DatagridExcelModelBuilder();
	}
	
	@Bean
	public ThemeSettingWebFilter themeSettingWebFilter(){
		return new ThemeSettingWebFilter();
	}
}
