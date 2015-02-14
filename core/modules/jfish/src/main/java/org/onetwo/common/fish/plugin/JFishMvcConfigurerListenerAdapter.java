package org.onetwo.common.fish.plugin;

import org.onetwo.common.spring.web.mvc.config.JFishMvcPluginListener;
import org.onetwo.common.spring.web.mvc.config.event.ArgumentResolverEvent;
import org.onetwo.common.spring.web.mvc.config.event.FreeMarkerConfigurerBuildEvent;
import org.onetwo.common.spring.web.mvc.config.event.MvcContextConfigRegisterEvent;
import org.onetwo.common.spring.web.mvc.config.event.MvcContextInitEvent;
import org.onetwo.common.spring.web.mvc.config.event.PropertyEditorRegisterEvent;
import org.onetwo.common.spring.web.mvc.config.event.WebApplicationStartupEvent;
import org.onetwo.common.spring.web.mvc.config.event.WebApplicationStopEvent;

/****
 * 改用eventbus实现后的适配
 * @author way
 *
 */
public class JFishMvcConfigurerListenerAdapter implements JFishMvcPluginListener {

	final private AbstractJFishPlugin<?> jfishPlugin;
	
	public JFishMvcConfigurerListenerAdapter(AbstractJFishPlugin<?> jfishPlugin) {
		super();
		this.jfishPlugin = jfishPlugin;
	}

	
	@Override
	public void listening(WebApplicationStartupEvent event) {
	}


	@Override
	public void listening(WebApplicationStopEvent event) {
	}


	@Override
	public void listening(FreeMarkerConfigurerBuildEvent event) {
	}

	@Override
	public void listening(PropertyEditorRegisterEvent event) {
	}

	@Override
	public void listening(MvcContextInitEvent event) {
	}

	@Override
	public void listening(ArgumentResolverEvent event) {
	}

	@Override
	public void listening(final MvcContextConfigRegisterEvent event){
		this.jfishPlugin.onMvcContextClasses(event.getConfigClasses());
	}
	
	
}
