package org.onetwo.plugins.jsonrpc.client;


import org.onetwo.common.spring.plugin.ConfigurableContextPlugin;
import org.onetwo.common.spring.plugin.event.ContextConfigRegisterEvent;
import org.onetwo.common.spring.plugin.event.JFishContextPluginListener;
import org.onetwo.common.spring.plugin.event.JFishContextPluginListenerAdapter;
import org.onetwo.plugins.jsonrpc.client.core.JsonRpcClientListener;
import org.onetwo.plugins.jsonrpc.client.core.JsonRpcClientRepository;
import org.onetwo.plugins.jsonrpc.client.core.JsonRpcClientScanner;
import org.onetwo.plugins.jsonrpc.client.core.impl.DirectServerEndpointRegister;
import org.onetwo.plugins.jsonrpc.client.core.impl.ZkServerEndpointRegister;
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
					logger.info("auto scan and build rpc client!");
				}else{
					logger.info("manual build rpc client!");
				}
				
				if(getConfig().isFindServerEndpointFromZk()){
					event.registerConfigClasses(ZkServerEndpointRegister.class);
					
				}else{
					event.registerConfigClasses(DirectRegisterContext.class);
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
		
		private RpcClientPluginConfig rpcClientPluginConfig = RpcClientPlugin.getInstance().getConfig();
		
		
		@Bean
		public JsonRpcClientScanner jsonRpcClientScanner(){
			JsonRpcClientScanner scaner = new JsonRpcClientScanner(rpcClientPluginConfig.getRpcClientPackages());
			return scaner;
		}
		
		@Bean
		public JsonRpcClientRepository jsonRpcClientRepository(){
			JsonRpcClientRepository repository = new JsonRpcClientRepository();
			return repository;
		}
	}


	@Configuration
	public static class DirectRegisterContext {

		@Bean
		public JsonRpcClientListener directServerEndpointRegister(){
			DirectServerEndpointRegister directServerEndpointRegister = new DirectServerEndpointRegister();
			return directServerEndpointRegister;
		}
	}

	@Configuration
	public static class ZkRegisterContext {

		@Bean
		public JsonRpcClientListener zkServerEndpointRegister(){
			ZkServerEndpointRegister zkServerEndpointRegister = new ZkServerEndpointRegister();
			return zkServerEndpointRegister;
		}
	}
}
