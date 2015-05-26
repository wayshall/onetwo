package org.onetwo.plugins.admin;

import org.onetwo.common.hibernate.event.EntityPackageRegisterEvent;
import org.onetwo.common.hibernate.event.HibernatePluginEventListenerAdapter;
import org.onetwo.common.spring.plugin.ConfigurableContextPlugin;
import org.onetwo.common.spring.plugin.event.ContextConfigRegisterEvent;
import org.onetwo.common.spring.plugin.event.JFishContextPluginListener;
import org.onetwo.plugins.admin.model.AdminModelContext;
import org.onetwo.plugins.admin.model.app.entity.AdminAppEntity;
import org.onetwo.plugins.admin.model.app.service.impl.AdminAppServiceImpl;
import org.onetwo.plugins.admin.model.data.entity.DictionaryEntity;
import org.onetwo.plugins.admin.model.data.service.impl.DictionaryServiceImpl;
import org.onetwo.plugins.admin.utils.AdminPluginConfig;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;


public class AdminPlugin extends ConfigurableContextPlugin<AdminPlugin, AdminPluginConfig> {

	public AdminPlugin() {
		super("/plugins/admin", "admin-config");
	}

	private static AdminPlugin instance;
	
	
	public static AdminPlugin getInstance() {
		return instance;
	}
	
	@Override
	public void setPluginInstance(AdminPlugin plugin){
		instance = plugin;
	}

	/*@Override
	public void onJFishContextClasses(List<Class<?>> annoClasses) {
		if(!isConfigExists()){
			return ;
		}

		annoClasses.add(AdminModelContext.class);
		
		if(getConfig().isAdminModuleEnable()){
			annoClasses.add(AdminAppModelContext.class);
		}
		
		if(getConfig().isDataModuleEnable()){
			annoClasses.add(DataModelContext.class);
		}
	}*/
	

	@Override
	public JFishContextPluginListener getJFishContextPluginListener() {
		return new HibernatePluginEventListenerAdapter(this){

			@Override
			public void listening(ContextConfigRegisterEvent event) {
				if(!isConfigExists()){
					return ;
				}

				event.registerConfigClasses(AdminModelContext.class);
				
				if(getConfig().isAdminModuleEnable()){
					event.registerConfigClasses(AdminAppModelContext.class);
				}
				
				if(getConfig().isDataModuleEnable()){
					event.registerConfigClasses(DataModelContext.class);
				}
			}
			
			@Override
			public void listening(EntityPackageRegisterEvent event){
				if(getConfig().isAdminModuleEnable()){
					event.registerEntityPackages(AdminAppEntity.class.getPackage().getName());
				}
				if(getConfig().isDataModuleEnable()){
					event.registerEntityPackages(DictionaryEntity.class.getPackage().getName());
				}
			}
			
		};
	}

	/*public void registerEntityPackage(List<String> packages) {
		if(getConfig().isAdminModuleEnable()){
			packages.add(AdminAppEntity.class.getPackage().getName());
		}
		if(getConfig().isDataModuleEnable()){
			packages.add(DictionaryEntity.class.getPackage().getName());
		}
	}*/


	@Configuration
	@ComponentScan(basePackageClasses=AdminAppServiceImpl.class)
	public static class AdminAppModelContext{
	}


	@Configuration
	@ComponentScan(basePackageClasses=DictionaryServiceImpl.class)
	public static class DataModelContext {
		
		/*@Bean
		public DictionaryService dictionaryService(){
			return new DictionaryServiceImpl();
		}*/
	}

}
