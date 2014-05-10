package org.onetwo.plugins.admin;

import java.util.List;

import org.onetwo.common.fish.plugin.AbstractJFishPlugin;
import org.onetwo.plugins.admin.utils.AdminPluginConfig;

public class AdminWebPlugin extends AbstractJFishPlugin<AdminWebPlugin> {

	private static AdminWebPlugin instance;
	private boolean enable = AdminPluginConfig.getInstance().isAdminIndexEnable();
	
	
	public static AdminWebPlugin getInstance() {
		return instance;
	}
	

	public static String getTemplatePath(String template) {
		return getInstance().getPluginMeta().getPluginConfig().getTemplatePath(template);
	}

	@Override
	public void onMvcContextClasses(List<Class<?>> annoClasses) {
		logger.info("admin index enable: {}", enable);
		if(enable)
			annoClasses.add(AdminWebContext.class);
	}

	@Override
	public void setPluginInstance(AdminWebPlugin plugin){
		instance = plugin;
	}

	@Override
	public boolean registerMvcResources() {
		return enable;
	}

}
