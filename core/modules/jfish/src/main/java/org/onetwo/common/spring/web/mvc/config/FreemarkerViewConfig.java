package org.onetwo.common.spring.web.mvc.config;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.onetwo.common.fish.plugin.JFishPluginManager;
import org.onetwo.common.fish.plugin.JFishPluginManagerFactory;
import org.onetwo.common.spring.SpringUtils;
import org.onetwo.common.spring.ftl.FtlUtils;
import org.onetwo.common.spring.ftl.JFishFreeMarkerConfigurer;
import org.onetwo.common.spring.ftl.JFishFreeMarkerView;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.view.freemarker.FreeMarkerViewResolver;

@Configuration
public class FreemarkerViewConfig {

	protected JFishPluginManager jfishPluginManager = JFishPluginManagerFactory.getPluginManager();
	
	@Bean
	public JFishFreeMarkerConfigurer freeMarkerConfigurer() {
		final JFishFreeMarkerConfigurer freeMarker = new JFishFreeMarkerConfigurer(this.jfishPluginManager.getMvcEventBus());
		final List<String> templatePaths = new ArrayList<String>(3);
		templatePaths.add("/WEB-INF/views/");
		templatePaths.add("/WEB-INF/ftl/");
		final Properties setting = freemarkerSetting();
		freeMarker.setTemplateLoaderPaths(templatePaths.toArray(new String[templatePaths.size()]));
		freeMarker.setDefaultEncoding("UTF-8");
		freeMarker.setFreemarkerSettings(setting);
		freeMarker.setJfishPluginManager(jfishPluginManager);
		
		return freeMarker;
	}

	@Bean(name = "freemarkerSetting")
	public Properties freemarkerSetting() {
		Properties prop = SpringUtils.createProperties("/mvc/freemarker.properties", true);
		if(!prop.containsKey(FtlUtils.CONFIG_CLASSIC_COMPATIBLE)){
			prop.setProperty(FtlUtils.CONFIG_CLASSIC_COMPATIBLE, "true");
		}
		return prop;
	}
	

	@Bean
	public FreeMarkerViewResolver freeMarkerViewResolver() {
		FreeMarkerViewResolver fmResolver = new FreeMarkerViewResolver();
		fmResolver.setViewClass(JFishFreeMarkerView.class);
		fmResolver.setSuffix(".ftl");
		fmResolver.setContentType("text/html;charset=UTF-8");
		fmResolver.setExposeRequestAttributes(true);
		fmResolver.setExposeSessionAttributes(true);
		fmResolver.setExposeSpringMacroHelpers(true);
		fmResolver.setRequestContextAttribute("request");
		fmResolver.setOrder(1);
		return fmResolver;
	}
}
