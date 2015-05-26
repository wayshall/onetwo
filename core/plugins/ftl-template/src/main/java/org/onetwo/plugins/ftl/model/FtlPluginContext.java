package org.onetwo.plugins.ftl.model;

import org.onetwo.common.spring.ftl.DefaultTemplateParser;
import org.onetwo.common.spring.ftl.DirsFreemarkerTemplateConfigurer;
import org.onetwo.common.spring.ftl.TemplateParser;
import org.onetwo.plugins.ftl.FtlPlugin;
import org.onetwo.plugins.ftl.FtlPluginConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class FtlPluginContext {
	
	@Bean
	public FtlPluginConfig ftlPluginConfig(){
		return FtlPlugin.getInstance().getConfig();
	}
	
	@Bean
	public DirsFreemarkerTemplateConfigurer dirFreemarkerTemplateConfigurer(){
		DirsFreemarkerTemplateConfigurer dirconfig = new DirsFreemarkerTemplateConfigurer();
		dirconfig.setTemplatePaths(ftlPluginConfig().getTemplateDir());
		dirconfig.initialize();
		return dirconfig;
	}
	
	@Bean
	public TemplateParser dirTemplateParser(){
		DefaultTemplateParser parser = new DefaultTemplateParser(dirFreemarkerTemplateConfigurer());
		return parser;
	}
	
}
