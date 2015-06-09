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
				if(getConfig().isRpcClientScanable()){
					event.registerConfigClasses(RpcClientScannableContext.class);
				}
			}
			
		};
	}

	@Configuration
	public static class RpcClientContext {
		
		@Bean
		public RpcClientPluginConfig rpcClientPluginConfig(){
			return RpcClientPlugin.getInstance().getConfig();
		}
		
	}

	@Configuration
	public static class RpcClientScannableContext {
		
		
		@Bean
		public JsonRpcClientScanner jsonRpcClientScanner(){
			RpcClientPluginConfig config = RpcClientPlugin.getInstance().getConfig();
			return new JsonRpcClientScanner(config.getRpcServerEndpoint(), config.getRpcClientPackages());
		}
		
	}
}
