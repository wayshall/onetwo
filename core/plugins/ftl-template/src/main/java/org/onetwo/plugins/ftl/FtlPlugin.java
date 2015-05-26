package org.onetwo.plugins.ftl;

import org.onetwo.common.spring.plugin.ConfigurableContextPlugin;
import org.onetwo.common.spring.plugin.event.ContextConfigRegisterEvent;
import org.onetwo.common.spring.plugin.event.JFishContextPluginListener;
import org.onetwo.common.spring.plugin.event.JFishContextPluginListenerAdapter;
import org.onetwo.plugins.ftl.model.FtlPluginContext;



public class FtlPlugin extends ConfigurableContextPlugin<FtlPlugin, FtlPluginConfig> {

	
	private static FtlPlugin instance;
	
	
	public static FtlPlugin getInstance() {
		return instance;
	}
	

	public FtlPlugin() {
		super("ftl-template-config", false);
	}


	@Override
	public JFishContextPluginListener getJFishContextPluginListener() {
//		final FtlPluginConfig config = getConfig();
		return new JFishContextPluginListenerAdapter(this){

			@Override
			public void listening(ContextConfigRegisterEvent event) {
				event.registerConfigClasses(FtlPluginContext.class);
			}
			
		};
	}

	public void setPluginInstance(FtlPlugin plugin){
		instance = plugin;
	}

}
