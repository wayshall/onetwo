package org.onetwo.plugins.melody;

import org.onetwo.common.spring.plugin.ConfigurableContextPlugin;
import org.onetwo.common.spring.plugin.event.ContextConfigRegisterEvent;
import org.onetwo.common.spring.plugin.event.JFishContextPluginListener;
import org.onetwo.common.spring.plugin.event.JFishContextPluginListenerAdapter;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.plugins.melody.model.MelodyPluginModelContext;
import org.onetwo.plugins.melody.model.MonitoringPatternsContext;



public class MelodyPlugin extends ConfigurableContextPlugin<MelodyPlugin, MelodyConfig> {

	public MelodyPlugin() {
		super("melody", false);
	}


	private static MelodyPlugin instance;
	
	
	public static MelodyPlugin getInstance() {
		return instance;
	}

	@Override
	public JFishContextPluginListener getJFishContextPluginListener() {
		return new JFishContextPluginListenerAdapter(this){

			@Override
			public void listening(ContextConfigRegisterEvent event) {
				/*if(!getConfig().isMonitoring()){
					return ;
				}*/
				if(!isConfigExists() || getConfig().isDisabled()){
					return ;
				}
				
				event.registerConfigClasses(MelodyPluginModelContext.class);
				if(!LangUtils.isEmpty(getConfig().getMonitoringPatterns())){
					event.registerConfigClasses(MonitoringPatternsContext.class);
				}
			}
		};
	}


	public void setPluginInstance(MelodyPlugin plugin){
		instance = plugin;
	}

}
