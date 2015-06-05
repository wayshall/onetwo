package org.onetwo.plugins.jsonrpc.client;


import org.onetwo.common.spring.plugin.ConfigurableContextPlugin;
import org.onetwo.common.spring.plugin.event.ContextConfigRegisterEvent;
import org.onetwo.common.spring.plugin.event.JFishContextPluginListener;
import org.onetwo.common.spring.plugin.event.JFishContextPluginListenerAdapter;



public class JsonRpcClientPlugin extends ConfigurableContextPlugin<JsonRpcClientPlugin, JsonRpcClientPluginConfig> {

	
	private static JsonRpcClientPlugin instance;
	
	
	public static JsonRpcClientPlugin getInstance() {
		return instance;
	}
	

	public JsonRpcClientPlugin() {
		super("/plugins/jsonrpc/", "client-config", false);
	}

	public void setPluginInstance(JsonRpcClientPlugin plugin){
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
