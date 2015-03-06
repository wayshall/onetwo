package org.onetwo.common.spring.web.mvc.config.event;

import org.onetwo.common.fish.plugin.JFishPluginManager;
import org.onetwo.common.ftl.directive.AbstractDirective;
import org.onetwo.common.spring.ftl.JFishFreeMarkerConfigurer;

public class FreeMarkerConfigurerBuildEvent {
	private final JFishPluginManager jfishPluginManager;
	private final JFishFreeMarkerConfigurer freemarkerConfigurer;
	private final boolean hasBuilt;
	public FreeMarkerConfigurerBuildEvent(JFishPluginManager jfishPluginManager, JFishFreeMarkerConfigurer config,
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
	public JFishPluginManager getJfishPluginManager() {
		return jfishPluginManager;
	}
	public JFishFreeMarkerConfigurer getFreemarkerConfigurer() {
		return freemarkerConfigurer;
	}
	
	
}
