package org.onetwo.plugins.zkclient;

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

}
