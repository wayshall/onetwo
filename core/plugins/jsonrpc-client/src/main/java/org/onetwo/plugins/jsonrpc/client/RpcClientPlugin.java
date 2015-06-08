package org.onetwo.plugins.jsonrpc.client;


import org.onetwo.common.spring.plugin.ConfigurableContextPlugin;
import org.onetwo.common.spring.plugin.event.ContextConfigRegisterEvent;
import org.onetwo.common.spring.plugin.event.JFishContextPluginListener;
import org.onetwo.common.spring.plugin.event.JFishContextPluginListenerAdapter;
import org.onetwo.plugins.jsonrpc.client.core.JsonRpcClientScanner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;



public class RpcClientPlugin extends ConfigurableContextPlugin<RpcClientPlugin, RpcClientPluginConfig> {

	
	private static RpcClientPlugin instance;
	
	
	public static RpcClientPlugin getInstance() {
		return instance;
	}
	

	public RpcClientPlugin() {
		super("/plugins/jsonrpc/", "client-config", false);
	}

	public void setPluginInstance(RpcClientPlugin plugin){
		instance = plugin;
	}
	

	@Override
	public JFishContextPluginListener getJFishContextPluginListener() {
		return new JFishContextPluginListenerAdapter(this) {

			@Override
			public void listening(ContextConfigRegisterEvent event) {
				event.registerConfigClasses(RpcClientContext.class);
			}
			
		};
	}

	@Configuration
	public static class RpcClientContext {
		
		@Bean
		public RpcClientPluginConfig rpcClientPluginConfig(){
			return RpcClientPlugin.getInstance().getConfig();
		}
		
		@Bean
		public JsonRpcClientScanner jsonRpcClientScanner(){
			RpcClientPluginConfig config = rpcClientPluginConfig();
			return new JsonRpcClientScanner(config.getRpcServerEndpoint(), config.getRpcClientPackages());
		}
		
	}
}
