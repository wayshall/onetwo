package org.onetwo.plugins.zkclient;

import org.springframework.context.annotation.Configuration;


@Configuration
public class ZkclientWebContext {
	
//	@Resource
	private ZkclientPluginConfig zkclientPluginConfig = ZkclientPlugin.getInstance().getConfig();
	
}
