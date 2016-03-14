package org.onetwo.plugins.zkclient;


import org.onetwo.common.spring.plugin.ConfigurableContextPlugin;
import org.onetwo.common.spring.plugin.event.ContextConfigRegisterEvent;
import org.onetwo.common.spring.plugin.event.JFishContextPluginListener;
import org.onetwo.common.spring.plugin.event.JFishContextPluginListenerAdapter;



public class ZkclientPlugin extends ConfigurableContextPlugin<ZkclientPlugin, ZkclientPluginConfig> {

	
	private static ZkclientPlugin instance;
	
	
	public static ZkclientPlugin getInstance() {
		return instance;
	}
	

	public ZkclientPlugin() {
		super("zkclient-config", false);
	}

	public void setPluginInstance(ZkclientPlugin plugin){
		instance = plugin;
	}
	

	@Override
	public JFishContextPluginListener getJFishContextPluginListener() {
		return new JFishContextPluginListenerAdapter(this) {

			@Override
			public void listening(ContextConfigRegisterEvent event) {
//				event.registerConfigClasses(ZkclientContext.class);
			}
			
		};
	}

}
