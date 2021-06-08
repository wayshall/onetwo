package org.onetwo.boot.module.redission;

import java.util.Map;

import lombok.Data;

import org.onetwo.common.propconf.JFishProperties;
import org.redisson.client.codec.Codec;
import org.redisson.spring.cache.CacheConfig;
import org.springframework.boot.context.properties.ConfigurationProperties;

import com.google.common.collect.Maps;

/**
 * @author wayshall
 * <br/>
 */
@Data
@ConfigurationProperties(RedissonProperties.PREFIX)
public class RedissonProperties {
	public static final String PREFIX = org.onetwo.boot.core.config.BootJFishConfig.ZIFISH_CONFIG_PREFIX+ ".redisson";
	public static final String ENABLED_KEY = PREFIX + ".enabled";
	
	String yamlConfig;
	String jsonConfig;
	Class<Codec> codec;
	
//	SingleServerConfig singleServerConfig = new SingleServerConfig();
	JFishProperties singleServer = new JFishProperties();
	
	//非文件配置的方式，只提供两个配置，方便测试开发
	String address = "redis://127.0.0.1:6379";
	String password;
	
	SpringCache springCache = new SpringCache();
	
	@Data
	public static class SpringCache {
		public static final String SPRING_CACHE_ENABLED_KEY = PREFIX + ".springCache.enabled";
		String configPath;
		Map<String, ? extends CacheConfig> config = Maps.newHashMap();
	}

}
