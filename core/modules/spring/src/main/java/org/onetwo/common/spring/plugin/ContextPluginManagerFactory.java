package org.onetwo.common.spring.plugin;

public class ContextPluginManagerFactory {
	
	private static ContextPluginManager contextPluginManager;

	public static ContextPluginManager getContextPluginManager() {
		return contextPluginManager;
	}

	public static void initContextPluginManager(ContextPluginManager contextPluginManager) {
		ContextPluginManagerFactory.contextPluginManager = contextPluginManager;
	}
	
	

}
