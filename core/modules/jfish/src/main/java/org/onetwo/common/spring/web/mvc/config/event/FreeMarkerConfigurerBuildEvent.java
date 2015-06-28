package org.onetwo.common.spring.web.mvc.config.event;

import org.onetwo.common.fish.plugin.JFishWebMvcPluginManager;
import org.onetwo.common.spring.ftl.JFishFreeMarkerConfigurer;
import org.onetwo.common.web.view.ftl.AbstractDirective;

public class FreeMarkerConfigurerBuildEvent {
	private final JFishWebMvcPluginManager jfishPluginManager;
	private final JFishFreeMarkerConfigurer freemarkerConfigurer;
	private final boolean hasBuilt;
	public FreeMarkerConfigurerBuildEvent(JFishWebMvcPluginManager jfishPluginManager, JFishFreeMarkerConfigurer config,
			boolean hasBuilt) {
		super();
		this.jfishPluginManager = jfishPluginManager;
		this.freemarkerConfigurer = config;
		this.hasBuilt = hasBuilt;
	}
	public FreeMarkerConfigurerBuildEvent registerDirective(AbstractDirective directive){
		this.freemarkerConfigurer.addDirective(directive);
		return this;
	}
	public FreeMarkerConfigurerBuildEvent registerDirective(AbstractDirective directive, boolean override){
		this.freemarkerConfigurer.addDirective(directive, override);
		return this;
	}
	public boolean isHasBuilt() {
		return hasBuilt;
	}
	public JFishWebMvcPluginManager getJfishPluginManager() {
		return jfishPluginManager;
	}
	public JFishFreeMarkerConfigurer getFreemarkerConfigurer() {
		return freemarkerConfigurer;
	}
	
	
}
