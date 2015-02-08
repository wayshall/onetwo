package org.onetwo.common.hibernate.event;

import java.util.List;

import org.onetwo.common.spring.plugin.ContextPluginManager;
import org.onetwo.common.spring.plugin.ContextPluginMeta;

public class EntityPackageRegisterEvent {

	final private ContextPluginManager<? extends ContextPluginMeta> contextPluginManager;
	final private List<String> packages;
	public EntityPackageRegisterEvent(
			ContextPluginManager<? extends ContextPluginMeta> contextPluginManager,
			List<String> packages) {
		super();
		this.contextPluginManager = contextPluginManager;
		this.packages = packages;
	}

	public EntityPackageRegisterEvent registerEntityPackages(String...packageNames){
		for(String pack : packageNames){
			packages.add(pack);
		}
		return this;
	}
	
	public ContextPluginManager<? extends ContextPluginMeta> getContextPluginManager() {
		return contextPluginManager;
	}

	public List<String> getPackages() {
		return packages;
	}
	
	

}
