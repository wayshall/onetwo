package org.onetwo.plugins.melody.model;

import org.onetwo.plugins.melody.MelodyConfig;
import org.onetwo.plugins.melody.MelodyPlugin;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
//@ComponentScan(basePackageClasses=PluginModelContext.class)
public class MelodyPluginModelContext {
	
	@Bean
	public MelodyConfig melodyConfig(){
		return MelodyPlugin.getInstance().getConfig();
	}
}
