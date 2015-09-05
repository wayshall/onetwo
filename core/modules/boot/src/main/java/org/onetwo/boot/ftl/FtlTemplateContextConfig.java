package org.onetwo.boot.ftl;

import javax.annotation.Resource;

import org.onetwo.boot.core.config.BootJFishConfig;
import org.onetwo.boot.core.web.WebRender;
import org.onetwo.boot.core.web.ftl.FtlWebRender;
import org.onetwo.common.spring.ftl.DefaultTemplateParser;
import org.onetwo.common.spring.ftl.DirsFreemarkerTemplateConfigurer;
import org.onetwo.common.spring.ftl.TemplateParser;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class FtlTemplateContextConfig {
	
	@Resource
	private BootJFishConfig jfishBootConfig;
	
	@Bean
	public DirsFreemarkerTemplateConfigurer dirFreemarkerTemplateConfigurer(){
		DirsFreemarkerTemplateConfigurer dirconfig = new DirsFreemarkerTemplateConfigurer();
		dirconfig.setTemplatePaths(jfishBootConfig.getFtlDir());
		dirconfig.initialize();
		return dirconfig;
	}
	
	@Bean
	public TemplateParser dirTemplateParser(){
		DefaultTemplateParser parser = new DefaultTemplateParser(dirFreemarkerTemplateConfigurer());
		return parser;
	}
	
	@Bean
	public WebRender ftlWebRender(){
		WebRender webrender = new FtlWebRender(dirFreemarkerTemplateConfigurer());
		return webrender;
	}
	
}
