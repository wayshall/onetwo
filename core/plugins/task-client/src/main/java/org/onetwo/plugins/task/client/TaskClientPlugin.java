package org.onetwo.plugins.task.client;

import java.util.List;

import org.onetwo.common.hibernate.event.HibernatePluginEventListenerAdapter;
import org.onetwo.common.spring.plugin.ConfigurableContextPlugin;
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
	

	@Override
	public void onJFishContextClasses(List<Class<?>> annoClasses) {
		annoClasses.add(TaskClientPluginContext.class);
		if(getConfig().isTaskClientMailService()){
			annoClasses.add(TaskClientMailServiceContext.class);
		}
	}
	

	@Override
	public JFishContextPluginListener getJFishContextPluginListener() {
		return new HibernatePluginEventListenerAdapter(this){

			@Override
			protected void registerEntityPackage(List<String> packages) {
				TaskClientPlugin.this.registerEntityPackage(packages);
			}
			
		};
	}

//	@Override
	public void registerEntityPackage(List<String> packages) {
		packages.add(TaskBase.class.getPackage().getName());
	}
	
	@Configuration
	public static class TaskClientMailServiceContext {
		
		@Bean
		public JavaMailService javaMailService(){
			return new TaskEmailServiceImpl();
		}
	}
	
}
