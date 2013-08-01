package org.onetwo.plugins.fmtagext;

import org.onetwo.common.spring.rest.RestPather;
import org.onetwo.common.spring.web.mvc.WebInterceptorAdapter;
import org.onetwo.plugins.fmtagext.spring.ControllerRestPatherBeanPostProcessor;
import org.onetwo.plugins.fmtagext.spring.PageBuilder;
import org.onetwo.plugins.fmtagext.ui.DefaultViewEntryManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.handler.MappedInterceptor;

@Configuration
public class FmtagextContext{
	
	@Bean
	public RestPather restPather(){
		return new RestPather();
	}
	
	@Bean
	public MappedInterceptor fmUIModelAndViewHandler(){
		FmUIModelAndViewHandler fmuiHander = new FmUIModelAndViewHandler();
		return WebInterceptorAdapter.createMappedInterceptor(fmuiHander);
	}

	@Bean
	public ControllerRestPatherBeanPostProcessor ControllerRestPatherBeanPostProcessor(){
		return new ControllerRestPatherBeanPostProcessor();
	}

	@Bean
	public PageBuilder pageBuilder(){
		return new PageBuilder();
	}
	
	@Bean
	public DefaultViewEntryManager viewEntryManager(){
		DefaultViewEntryManager vem = new DefaultViewEntryManager();
		return vem;
	}
	
}
