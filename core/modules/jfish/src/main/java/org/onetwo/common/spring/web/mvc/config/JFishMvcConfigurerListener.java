package org.onetwo.common.spring.web.mvc.config;

import org.onetwo.common.spring.web.mvc.config.event.ArgumentResolverEvent;
import org.onetwo.common.spring.web.mvc.config.event.FreeMarkerConfigurerBuildEvent;
import org.onetwo.common.spring.web.mvc.config.event.MvcContextConfigRegisterEvent;
import org.onetwo.common.spring.web.mvc.config.event.MvcContextInitEvent;
import org.onetwo.common.spring.web.mvc.config.event.PropertyEditorRegisterEvent;

import com.google.common.eventbus.Subscribe;

public interface JFishMvcConfigurerListener {

	@Subscribe
	void listening(FreeMarkerConfigurerBuildEvent event);

	@Subscribe
	void listening(PropertyEditorRegisterEvent event);

	@Subscribe
	void listening(MvcContextInitEvent event);
	
	@Subscribe
	void listening(final ArgumentResolverEvent event);
	
	@Subscribe
	void listening(final MvcContextConfigRegisterEvent event);
}
