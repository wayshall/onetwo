package org.onetwo.common.spring.web.mvc.config;

import java.util.List;

import org.onetwo.common.spring.ftl.JFishFreeMarkerConfigurer;
import org.springframework.beans.PropertyEditorRegistrar;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;

public interface JFishMvcConfigurerListener {

	public void onMvcBuildFreeMarkerConfigurer(JFishFreeMarkerConfigurer config, boolean hasBuilt);
	
	void onMvcPropertyEditorRegistrars(final List<PropertyEditorRegistrar> propertyEditorRegistrars);
	
	void onMvcInitContext(JFishMvcApplicationContext applicationContext, JFishMvcConfig mvcConfig);
	
	void onRegisterArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers);
}
