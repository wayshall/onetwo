package org.onetwo.plugins.email;

import java.util.List;

import org.onetwo.common.spring.plugin.ConfigurableContextPlugin;


public class EmailPlugin extends ConfigurableContextPlugin<EmailPlugin, EmailConfig> {

	public EmailPlugin() {
		super("/plugins/email", "email-config");
	}


	private static EmailPlugin instance;
	
	
	public static EmailPlugin getInstance() {
		return instance;
	}
	
	@Override
	public void onJFishContextClasses(List<Class<?>> annoClasses) {
		/*if(getConfig().isMailSendActive()){
			annoClasses.add(EmailPluginContext.class);
			annoClasses.add(JavaMailServiceContext.class);
		}else{
			annoClasses.add(JavaMailServiceContext.class);
		}*/

		annoClasses.add(EmailPluginContext.class);
		annoClasses.add(JavaMailServiceContext.class);
	}


	public void setPluginInstance(EmailPlugin plugin){
		instance = (EmailPlugin)plugin;
	}

}
