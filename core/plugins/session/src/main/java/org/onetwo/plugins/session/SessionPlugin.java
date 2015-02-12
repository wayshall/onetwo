package org.onetwo.plugins.session;

import org.onetwo.common.spring.plugin.ConfigurableContextPlugin;
import org.onetwo.common.spring.plugin.event.ContextConfigRegisterEvent;
import org.onetwo.common.spring.plugin.event.JFishContextPluginListener;
import org.onetwo.common.spring.plugin.event.JFishContextPluginListenerAdapter;
import org.onetwo.plugins.session.model.SessionPluginContext;



public class SessionPlugin extends ConfigurableContextPlugin<SessionPlugin, SessionPluginConfig> {

	public static final String CONFIG_PATH = "/plugins/session/session-config.properties";
	
	private static SessionPlugin instance;
	
	
	public static SessionPlugin getInstance() {
		return instance;
	}
	

	public SessionPlugin() {
		super("/plugins/session/", "session-config");
	}


	@Override
	public JFishContextPluginListener getJFishContextPluginListener() {
		return new JFishContextPluginListenerAdapter(this){

			@Override
			public void listening(ContextConfigRegisterEvent event) {
				if(!getConfig().isContainerSession()){
					event.registerConfigClasses(SessionPluginContext.class);
				}
			}
			
		};
	}

	public void setPluginInstance(SessionPlugin plugin){
		instance = plugin;
	}

}
