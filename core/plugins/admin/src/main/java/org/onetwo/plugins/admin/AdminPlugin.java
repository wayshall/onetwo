package org.onetwo.plugins.admin;

import java.util.List;

import org.onetwo.common.spring.plugin.ConfigurableContextPlugin;
import org.onetwo.plugins.admin.model.AdminModelContext;
import org.onetwo.plugins.admin.model.entity.AdminAppEntity;
import org.onetwo.plugins.admin.utils.AdminPluginConfig;


public class AdminPlugin extends ConfigurableContextPlugin<AdminPlugin, AdminPluginConfig> {

	public AdminPlugin() {
		super("/plugins/admin", "adminconfig");
	}

	private static AdminPlugin instance;
	
	
	public static AdminPlugin getInstance() {
		return instance;
	}
	
	@Override
	public void setPluginInstance(AdminPlugin plugin){
		instance = plugin;
	}

	@Override
	public void onJFishContextClasses(List<Class<?>> annoClasses) {
		if(isConfigExists()){
			annoClasses.add(AdminModelContext.class);
		}
	}

	@Override
	public void registerEntityPackage(List<String> packages) {
		packages.add(AdminAppEntity.class.getPackage().getName());
	}


}
