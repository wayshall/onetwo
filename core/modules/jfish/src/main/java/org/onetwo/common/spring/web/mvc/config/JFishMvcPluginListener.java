package org.onetwo.common.spring.web.mvc.config;

import org.onetwo.common.spring.web.mvc.config.event.ArgumentResolverEvent;
import org.onetwo.common.spring.web.mvc.config.event.FreeMarkerConfigurerBuildEvent;
import org.onetwo.common.spring.web.mvc.config.event.MvcContextConfigRegisterEvent;
import org.onetwo.common.spring.web.mvc.config.event.MvcContextInitEvent;
import org.onetwo.common.spring.web.mvc.config.event.PropertyEditorRegisterEvent;
import org.onetwo.common.spring.web.mvc.config.event.WebApplicationStartupCompletedEvent;
import org.onetwo.common.spring.web.mvc.config.event.WebApplicationStartupEvent;
import org.onetwo.common.spring.web.mvc.config.event.WebApplicationStopEvent;

import com.google.common.eventbus.Subscribe;

/****
 * 监听器接口
 * 显式定义监听器接口，以使代码逻辑更清晰
 * @author way
 *
 */
public interface JFishMvcPluginListener {

	@Subscribe
	void listening(WebApplicationStartupEvent event);
	@Subscribe
	void listening(WebApplicationStopEvent event);

	@Subscribe
	void listening(FreeMarkerConfigurerBuildEvent event);

	@Subscribe
	void listening(PropertyEditorRegisterEvent event);

	@Subscribe
	void listening(MvcContextInitEvent event);
	
	@Subscribe
	void listening(ArgumentResolverEvent event);
	
	@Subscribe
	void listening(MvcContextConfigRegisterEvent event);
	

	@Subscribe
	void listening(WebApplicationStartupCompletedEvent event);
}
