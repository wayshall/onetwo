package org.onetwo.plugins.task.client;

import org.onetwo.common.hibernate.event.EntityPackageRegisterEvent;
import org.onetwo.common.hibernate.event.HibernatePluginEventListenerAdapter;
import org.onetwo.common.spring.plugin.ConfigurableContextPlugin;
import org.onetwo.common.spring.plugin.event.ContextConfigRegisterEvent;
import org.onetwo.common.spring.plugin.event.JFishContextPluginListener;
import org.onetwo.plugins.email.JavaMailService;
import org.onetwo.plugins.task.client.service.impl.TaskEmailServiceImpl;
import org.onetwo.plugins.task.entity.TaskBase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


public class TaskClientPlugin extends ConfigurableContextPlugin<TaskClientPlugin, TaskClientConfig> {


	public TaskClientPlugin() {
		super("/plugins/task/", "task-client-config", true);
	}

	private static TaskClientPlugin instance;
	
	
	public static TaskClientPlugin getInstance() {
		return instance;
	}
	
	@Override
	public void setPluginInstance(TaskClientPlugin plugin){
		instance = plugin;
	}
	

	/*@Override
	public void onJFishContextClasses(List<Class<?>> annoClasses) {
		annoClasses.add(TaskClientPluginContext.class);
		if(getConfig().isTaskClientMailService()){
			annoClasses.add(TaskClientMailServiceContext.class);
		}
	}*/
	

	@Override
	public JFishContextPluginListener getJFishContextPluginListener() {
		return new HibernatePluginEventListenerAdapter(this){
			
			@Override
			public void listening(ContextConfigRegisterEvent event) {
				event.registerConfigClasses(TaskClientPluginContext.class);
				if(getConfig().isTaskClientMailService()){
					event.registerConfigClasses(TaskClientMailServiceContext.class);
				}
			}
			
			@Override
			public void listening(EntityPackageRegisterEvent event){
				event.registerEntityPackages(TaskBase.class.getPackage().getName());
			}
			
		};
	}

//	@Override
	/*public void registerEntityPackage(List<String> packages) {
		packages.add(TaskBase.class.getPackage().getName());
	}*/
	
	@Configuration
	public static class TaskClientMailServiceContext {
		
		@Bean
		public JavaMailService taskMailService(){
			return new TaskEmailServiceImpl();
		}
	}
	
}
