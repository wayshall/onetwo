package org.onetwo.plugins.email;

import java.util.List;

import org.onetwo.common.spring.plugin.ConfigurableContextPlugin;


public class EmailPlugin extends ConfigurableContextPlugin<EmailPlugin, EmailConfig> {

	public EmailPlugin() {
		super("email", "mailconfig");
	}


	private static EmailPlugin instance;
	
	
	public static EmailPlugin getInstance() {
		return instance;
	}
	
	@Override
	public void onJFishContextClasses(List<Class<?>> annoClasses) {
		annoClasses.add(EmailPluginContext.class);
	}


	public void setPluginInstance(EmailPlugin plugin){
		instance = (EmailPlugin)plugin;
	}

}
