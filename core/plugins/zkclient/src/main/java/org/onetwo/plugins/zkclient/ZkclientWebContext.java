package org.onetwo.plugins.zkclient;

import org.onetwo.plugins.zkclient.provider.BaseNodeRegister;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class ZkclientWebContext {
	
//	@Resource
	private ZkclientPluginConfig zkclientPluginConfig = ZkclientPlugin.getInstance().getConfig();
	
}
