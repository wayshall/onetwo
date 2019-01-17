package org.onetwo.cloud.eureka;
/**
 * @author weishao zeng
 * <br/>
 */

import java.util.Map;

import org.onetwo.boot.plugin.core.PluginManager;
import org.onetwo.boot.plugin.core.PluginMeta;
import org.onetwo.boot.plugin.core.WebPlugin;
import org.onetwo.cloud.feign.CornerFeignConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.cloud.client.serviceregistry.Registration;
import org.springframework.context.ApplicationContext;

import lombok.extern.slf4j.Slf4j;

/****
 * 
 * @author way
 *
 */
@Deprecated
@Slf4j
public class PluginContextPathMetaAppender implements ApplicationRunner {
	private static final String KEY_PREFIX = "plugins";

	@Value("${"+CornerFeignConfiguration.KEY_REJECT_PLUGIN_CONTEXT_PATH+":true}")
	private boolean rejectPluginContextPath;
	@Autowired
	private ApplicationContext applicationContext;
	/*@Autowired
	private DiscoveryClient discoveryClient;*/
	@Autowired
	private Registration registration;
	@Autowired(required=false)
	private PluginManager pluginManager;

	@Override
	public void run(ApplicationArguments args) throws Exception {
		if (rejectPluginContextPath) {
			return ;
		}
		Map<String, String> meta = registration.getMetadata();//discoveryClient.getLocalServiceInstance().getMetadata();

		for(WebPlugin plugin : pluginManager.getPlugins()){
			String key = pluginMetaKey(plugin.getPluginMeta().getName());
			String path = PluginMeta.resolvePluginContextPath(applicationContext, plugin.getContextPath());
			meta.put(key, path);
			if (log.isDebugEnabled()) {
				log.debug("add plugin context path to discover client, key: {}, path: {}", key, path);
			}
		}
	}
	
	public static String pluginMetaKey(String plugName) {
		String key = KEY_PREFIX + "." + plugName + ".contextPath";
		return key;
	}
	
}

