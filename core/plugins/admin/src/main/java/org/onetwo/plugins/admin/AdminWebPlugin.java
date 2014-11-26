package org.onetwo.plugins.admin;

import java.util.List;

import org.onetwo.common.fish.plugin.AbstractJFishPlugin;
public class AdminWebPlugin extends AbstractJFishPlugin<AdminWebPlugin> {

	private static AdminWebPlugin instance;
	
	
	public static AdminWebPlugin getInstance() {
		return instance;
	}
	

	public static String getTemplatePath(String template) {
		return getInstance().getPluginMeta().getPluginConfig().getTemplatePath(template);
	}

	@Override
	public void onMvcContextClasses(List<Class<?>> annoClasses) {
		if(!AdminPlugin.getInstance().isConfigExists()){
			return ;
		}
		boolean enable = AdminPlugin.getInstance().getConfig().isAdminIndexEnable();
		logger.info("admin index enable: {}", enable);
		if(enable)
			annoClasses.add(AdminWebContext.class);
		
		if(AdminPlugin.getInstance().getConfig().isAdminModuleEnable()){
			annoClasses.add(AdminAppWebContext.class);
		}
	}

	@Override
	public void setPluginInstance(AdminWebPlugin plugin){
		instance = plugin;
	}

	@Override
	public boolean registerMvcResources() {
		return true;
	}

}
