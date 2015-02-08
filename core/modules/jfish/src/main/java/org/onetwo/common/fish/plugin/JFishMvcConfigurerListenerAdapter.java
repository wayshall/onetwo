package org.onetwo.common.fish.plugin;

import org.onetwo.common.spring.web.mvc.config.JFishMvcConfigurerListener;
import org.onetwo.common.spring.web.mvc.config.event.ArgumentResolverEvent;
import org.onetwo.common.spring.web.mvc.config.event.FreeMarkerConfigurerBuildEvent;
import org.onetwo.common.spring.web.mvc.config.event.MvcContextConfigRegisterEvent;
import org.onetwo.common.spring.web.mvc.config.event.MvcContextInitEvent;
import org.onetwo.common.spring.web.mvc.config.event.PropertyEditorRegisterEvent;

/****
 * 改用eventbus实现后的适配
 * @author way
 *
 */
public class JFishMvcConfigurerListenerAdapter implements JFishMvcConfigurerListener {

	final private AbstractJFishPlugin<?> jfishPlugin;
	
	public JFishMvcConfigurerListenerAdapter(AbstractJFishPlugin<?> jfishPlugin) {
		super();
		this.jfishPlugin = jfishPlugin;
	}

	@Override
	public void listening(FreeMarkerConfigurerBuildEvent event) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void listening(PropertyEditorRegisterEvent event) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void listening(MvcContextInitEvent event) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void listening(ArgumentResolverEvent event) {
		
	}

	@Override
	public void listening(final MvcContextConfigRegisterEvent event){
		this.jfishPlugin.onMvcContextClasses(event.getConfigClasses());
	}
	
	
}
