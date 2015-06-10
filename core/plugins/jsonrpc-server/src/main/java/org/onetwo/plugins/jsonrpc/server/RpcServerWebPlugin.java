package org.onetwo.plugins.jsonrpc.server;

import javax.annotation.Resource;

import org.onetwo.common.fish.plugin.AbstractJFishPlugin;
import org.onetwo.common.fish.plugin.JFishMvcConfigurerListenerAdapter;
import org.onetwo.common.spring.web.mvc.config.JFishMvcPluginListener;
import org.onetwo.common.spring.web.mvc.config.event.MvcContextConfigRegisterEvent;
import org.onetwo.plugins.jsonrpc.server.core.DispatcherController;
import org.onetwo.plugins.jsonrpc.server.core.JsonRpcSerivceRepository;
import org.onetwo.plugins.jsonrpc.server.core.JsonRpcServiceScanner;
import org.onetwo.plugins.jsonrpc.server.zk.ZkServiceNodeRegisterListener;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

public class RpcServerWebPlugin extends AbstractJFishPlugin<RpcServerWebPlugin> {

	private static RpcServerWebPlugin instance;
	
	
	public static RpcServerWebPlugin getInstance() {
		return instance;
	}

	@Override
	public void setPluginInstance(RpcServerWebPlugin plugin){
		instance = plugin;
	}

	@Override
	public JFishMvcPluginListener getJFishMvcConfigurerListener() {
		return new JFishMvcConfigurerListenerAdapter(this){

			@Override
			public void listening(final MvcContextConfigRegisterEvent event){
				event.registerConfigClasses(JsonRpcWebContext.class);
			}
		};
	}
	
	@Configuration
	public static class JsonRpcWebContext {
		@Resource
		private ApplicationContext applicationContext;
		@Resource
		private RpcServerPluginConfig rpcServerPluginConfig;
		
		@Bean
		public DispatcherController dispatcherController(){
			return new DispatcherController();
		}
		
		@Bean
		public JsonRpcSerivceRepository jsonRpcSerivceRepository(){
			JsonRpcSerivceRepository jsonRpcSerivceRepository = new JsonRpcSerivceRepository();
			jsonRpcSerivceRepository.registerListener(zkRegisterListener());
			return jsonRpcSerivceRepository;
		}
		
		@Bean
		public JsonRpcServiceScanner jsonRpcServiceScanner(){
			JsonRpcServiceScanner scaner = new JsonRpcServiceScanner(applicationContext);
			scaner.setJsonRpcSerivceRepository(jsonRpcSerivceRepository());
			scaner.setPackagesToScan(rpcServerPluginConfig.getRpcSerivcePackages());
			return scaner;
		}
		
		@Bean
		public ZkServiceNodeRegisterListener zkRegisterListener(){
			return new ZkServiceNodeRegisterListener();
		}
		

		/*@Bean
		@Order(0)
		public View jsonView() {
			JsonView jview = new JsonRpcView();
			return jview;
		}*/
	}

}
