package org.onetwo.plugins.zkclient;

import org.onetwo.plugins.zkclient.core.RootNodeRegister;
import org.onetwo.plugins.zkclient.curator.CuratorClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ZkclientContext {
	
	@Bean
	public ZkclientPluginConfig zkclientPluginConfig(){
		return ZkclientPlugin.getInstance().getConfig();
	}
	
	@Bean
	public CuratorClient curatorClient(){
		CuratorClient curator = new CuratorClient(zkclientPluginConfig());
		return curator;
	}

	/*@Bean
	public Zkclienter zkclienter(){
		Zkclienter zk = new Zkclienter();
		zk.setZkclientPluginConfig(zkclientPluginConfig());
		return zk;
	}*/
	
	/*@Bean
	public ZkEventListenerRegister zkEventListenerRegister(){
		return new ZkEventListenerRegister(zkclienter());
	}*/
	
	@Bean
	public RootNodeRegister baseNodeRegister(){
		RootNodeRegister module = new RootNodeRegister();
		module.setZkclientPluginConfig(zkclientPluginConfig());
		return module;
	}
}
