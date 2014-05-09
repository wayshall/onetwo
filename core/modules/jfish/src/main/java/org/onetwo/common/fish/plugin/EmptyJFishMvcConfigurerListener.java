package org.onetwo.common.fish.plugin;

import java.util.List;

import org.onetwo.common.spring.ftl.JFishFreeMarkerConfigurer;
import org.onetwo.common.spring.web.mvc.config.JFishMvcApplicationContext;
import org.onetwo.common.spring.web.mvc.config.JFishMvcConfig;
import org.onetwo.common.spring.web.mvc.config.JFishMvcConfigurerListener;
import org.springframework.beans.PropertyEditorRegistrar;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;

public class EmptyJFishMvcConfigurerListener implements JFishMvcConfigurerListener {

	@Override
	public void onMvcBuildFreeMarkerConfigurer(final JFishFreeMarkerConfigurer config, final boolean hasBuilt){
	}

	@Override
	public void onMvcPropertyEditorRegistrars(List<PropertyEditorRegistrar> propertyEditorRegistrars) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onMvcInitContext(JFishMvcApplicationContext applicationContext, JFishMvcConfig mvcConfig) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onRegisterArgumentResolvers(
			List<HandlerMethodArgumentResolver> argumentResolvers) {
		// TODO Auto-generated method stub
		
	}
	
}
