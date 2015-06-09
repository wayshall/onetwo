package org.onetwo.plugins.jsonrpc.server;


import javax.annotation.Resource;

import org.onetwo.common.spring.plugin.ConfigurableContextPlugin;
import org.onetwo.common.spring.plugin.ContextPluginMeta;
import org.onetwo.common.spring.plugin.event.ContextConfigRegisterEvent;
import org.onetwo.common.spring.plugin.event.JFishContextPluginListener;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;



public class RpcServerPlugin extends ConfigurableContextPlugin<RpcServerPlugin, RpcServerPluginConfig> {

	
	private static RpcServerPlugin instance;
	
	
	public static RpcServerPlugin getInstance() {
		return instance;
	}
	

	public RpcServerPlugin() {
		super("/plugins/jsonrpc/", "server-config", true);
	}

	public void setPluginInstance(RpcServerPlugin plugin){
		instance = plugin;
	}
	

	protected void initWithEnv(ContextPluginMeta pluginMeta, String appEnv) {
		super.initWithEnv(pluginMeta, appEnv);
	}
	@Override
	public JFishContextPluginListener getJFishContextPluginListener() {
		return new JFishContextPluginListener() {

			@Override
			public void listening(ContextConfigRegisterEvent event) {
				event.registerConfigClasses(JsonRpcServerContext.class);
			}
			
		};
	}

	@Configuration
	public static class JsonRpcServerContext {
		@Resource
		private ApplicationContext applicationContext;
		
		@Bean
		public RpcServerPluginConfig rpcServerPluginConfig(){
			return RpcServerPlugin.getInstance().getConfig();
		}
		
	
	}
}
