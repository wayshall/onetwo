package org.onetwo.plugins.jsonrpc.client;


import org.onetwo.common.spring.SpringUtils;
import org.onetwo.common.spring.plugin.ConfigurableContextPlugin;
import org.onetwo.common.spring.plugin.event.ContextConfigRegisterEvent;
import org.onetwo.common.spring.plugin.event.JFishContextPluginListener;
import org.onetwo.common.spring.plugin.event.JFishContextPluginListenerAdapter;
import org.onetwo.plugins.jsonrpc.client.core.JsonRpcClientListener;
import org.onetwo.plugins.jsonrpc.client.core.JsonRpcClientRepository;
import org.onetwo.plugins.jsonrpc.client.core.JsonRpcClientScanner;
import org.onetwo.plugins.jsonrpc.client.core.impl.DirectServerEndpointClientRegister;
import org.onetwo.plugins.jsonrpc.client.core.impl.ZkServerEndpointRpcClientRegister;
import org.onetwo.plugins.zkclient.ZkclientContext;
import org.onetwo.plugins.zkclient.curator.CuratorClient;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
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
					event.registerConfigClasses(ZkclientContext.class, ZkRegisterContext.class);
					logger.info("build rpc client with zk mode!");
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
	public static class RpcClientScannableContext implements ApplicationContextAware {
		
		private RpcClientPluginConfig rpcClientPluginConfig = RpcClientPlugin.getInstance().getConfig();
		
		private ApplicationContext applicationContext;
		
		@Bean
		public JsonRpcClientScanner jsonRpcClientScanner(){
			JsonRpcClientScanner scaner = new JsonRpcClientScanner(applicationContext, rpcClientPluginConfig.getRpcClientPackages());
			scaner.setJsonRpcClientRepository(jsonRpcClientRepository());
			return scaner;
		}
		
		@Bean
		public JsonRpcClientRepository jsonRpcClientRepository(){
			JsonRpcClientRepository repository = new JsonRpcClientRepository();
			return repository;
		}

		@Override
		public void setApplicationContext(ApplicationContext applicationContext)
				throws BeansException {
			this.applicationContext = applicationContext;
		}
		
		
	}


	@Configuration
	public static class DirectRegisterContext {

		@Bean
		public JsonRpcClientListener directServerEndpointRegister(){
			DirectServerEndpointClientRegister directServerEndpointRegister = new DirectServerEndpointClientRegister();
			return directServerEndpointRegister;
		}
	}

	@Configuration
	public static class ZkRegisterContext implements ApplicationContextAware, InitializingBean {
		private CuratorClient curatorClient;
		private ApplicationContext applicationContext;
		
		private RpcClientPluginConfig rpcClientPluginConfig = RpcClientPlugin.getInstance().getConfig();

		
		@Override
		public void afterPropertiesSet() throws Exception {
			this.curatorClient = SpringUtils.getBean(applicationContext, CuratorClient.class);
		}

		@Bean
		public JsonRpcClientListener zkServerEndpointRegister(){
			ZkServerEndpointRpcClientRegister zkServerEndpointRegister = new ZkServerEndpointRpcClientRegister();
			zkServerEndpointRegister.setCuratorClient(curatorClient);
			zkServerEndpointRegister.setRpcClientPluginConfig(rpcClientPluginConfig);
			return zkServerEndpointRegister;
		}

		@Override
		public void setApplicationContext(ApplicationContext applicationContext)
				throws BeansException {
			this.applicationContext = applicationContext;
		}
		
		
	}
}
