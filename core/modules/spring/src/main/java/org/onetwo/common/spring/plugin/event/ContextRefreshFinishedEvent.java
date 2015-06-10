package org.onetwo.common.spring.plugin.event;

import org.onetwo.common.spring.plugin.ContextPluginManager;
import org.onetwo.common.spring.plugin.ContextPluginMeta;

/***
 * on SpringProfilesWebApplicationContext.finishRefresh
 * @author way
 *
 */
public class ContextRefreshFinishedEvent {
	
	final private ContextPluginManager<? extends ContextPluginMeta> contextPluginManager;
	public ContextRefreshFinishedEvent(
			ContextPluginManager<? extends ContextPluginMeta> contextPluginManager) {
		super();
		this.contextPluginManager = contextPluginManager;
	}
	public ContextPluginManager<? extends ContextPluginMeta> getContextPluginManager() {
		return contextPluginManager;
	}
	
	

}
