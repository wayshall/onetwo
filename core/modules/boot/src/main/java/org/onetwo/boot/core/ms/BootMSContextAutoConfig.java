package org.onetwo.boot.core.ms;

import org.onetwo.boot.core.BootContextConfig;
import org.onetwo.boot.core.BootWebCommonAutoConfig;
import org.onetwo.boot.core.config.BootBusinessConfig;
import org.onetwo.boot.core.config.BootJFishConfig;
import org.onetwo.boot.core.config.BootSiteConfig;
import org.onetwo.boot.core.config.BootSpringConfig;
import org.onetwo.boot.core.embedded.TomcatProperties;
import org.onetwo.boot.plugin.PluginContextConfig;
import org.springframework.boot.autoconfigure.http.HttpEncodingProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@EnableConfigurationProperties({HttpEncodingProperties.class, BootJFishConfig.class, BootSpringConfig.class, BootBusinessConfig.class, BootSiteConfig.class, TomcatProperties.class})
@Import({BootContextConfig.class, PluginContextConfig.class})
//@Import({BootContextConfig.class})
//@ConditionalOnProperty(name=BootJFishConfig.ENABLE_JFISH_AUTO_CONFIG, havingValue=BootJFishConfig.VALUE_AUTO_CONFIG_WEB_MS)
public class BootMSContextAutoConfig extends BootWebCommonAutoConfig {
	
	public BootMSContextAutoConfig(){
	}

}
