package org.onetwo.plugins.zkclient;

import org.onetwo.plugins.zkclient.provider.ModuleRegister;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class ZkclientWebContext {
	
//	@Resource
	private ZkclientPluginConfig zkclientPluginConfig = ZkclientPlugin.getInstance().getConfig();
	
	@Bean
	public Zkclienter zkclienter(){
		Zkclienter zk = new Zkclienter();
		zk.setZkclientPluginConfig(zkclientPluginConfig);
		return zk;
	}
	
	@Bean
	public ZkEventListenerRegister zkEventListenerRegister(){
		return new ZkEventListenerRegister(zkclienter());
	}
	
	@Bean
	public ModuleRegister moduleRegister(){
		ModuleRegister module = new ModuleRegister();
		module.setZkclientPluginConfig(zkclientPluginConfig);
		return module;
	}
}
