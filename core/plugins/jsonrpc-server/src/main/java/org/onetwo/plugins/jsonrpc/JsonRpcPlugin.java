package org.onetwo.plugins.jsonrpc;


import org.onetwo.common.spring.plugin.ConfigurableContextPlugin;
import org.onetwo.common.spring.plugin.event.ContextConfigRegisterEvent;
import org.onetwo.common.spring.plugin.event.JFishContextPluginListener;
import org.onetwo.common.spring.plugin.event.JFishContextPluginListenerAdapter;



public class JsonRpcPlugin extends ConfigurableContextPlugin<JsonRpcPlugin, JsonRpcPluginConfig> {

	
	private static JsonRpcPlugin instance;
	
	
	public static JsonRpcPlugin getInstance() {
		return instance;
	}
	

	public JsonRpcPlugin() {
		super("jsonrpc-config", false);
	}

	public void setPluginInstance(JsonRpcPlugin plugin){
		instance = plugin;
	}
	

	@Override
	public JFishContextPluginListener getJFishContextPluginListener() {
		return new JFishContextPluginListenerAdapter(this) {

			@Override
			public void listening(ContextConfigRegisterEvent event) {
			}
			
		};
	}

}
