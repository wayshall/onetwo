package org.onetwo.plugins.session;

import java.util.Collections;
import java.util.Map;

import org.onetwo.common.spring.SpringUtils;
import org.onetwo.common.spring.plugin.ConfigurableContextPlugin.LoadableConfig;
import org.onetwo.common.utils.propconf.JFishProperties;

import redis.clients.jedis.Protocol;

public class SessionPluginConfig implements LoadableConfig {
	

	private JFishProperties config;
//	private SessionRepository sessionRepository;
	private boolean embeddedRedis;
	private EmbeddedRedisServerConfig embeddedRedisServerConfig;
	private boolean silentJdkSerializer;
	private Map<String, ?> redisConfig = Collections.EMPTY_MAP;

	public SessionPluginConfig() {
	}
	
	@Override
	public void load(JFishProperties properties) {
		this.config = properties;
		/*String sr = properties.getProperty("session.repository", SessionRepository.CONTAINER.name());
		this.sessionRepository = SessionRepository.valueOf(sr.toUpperCase());*/
		
		this.embeddedRedis = properties.getBoolean("redis.embedded", true);
		if(embeddedRedis){
			Map<String, String> props = properties.getPropertiesStartWith("embedded.redis.");
			this.embeddedRedisServerConfig = SpringUtils.map2Bean(props, EmbeddedRedisServerConfig.class);
		}
		
		this.redisConfig = properties.getPropertiesStartWith("redis.");
		
		this.silentJdkSerializer = properties.getBoolean("jdkserializer.silent", false);
		
	}

	public boolean isSilentJdkSerializer() {
		return silentJdkSerializer;
	}

	@Override
	public JFishProperties getSourceConfig() {
		return config;
	}
	
	
	
	public boolean isEmbeddedRedis() {
		return embeddedRedis;
	}

	public EmbeddedRedisServerConfig getEmbeddedRedisServerConfig() {
		return embeddedRedisServerConfig;
	}

	public Map<String, ?> getRedisConfig() {
		return redisConfig;
	}

	public static class EmbeddedRedisServerConfig {
		private String executable;
		private String configFile;
		private int port = Protocol.DEFAULT_PORT;
		public int getPort() {
			return port;
		}
		public void setPort(int port) {
			this.port = port;
		}
		public String getExecutable() {
			return executable;
		}
		public void setExecutable(String executable) {
			this.executable = executable;
		}
		public String getConfigFile() {
			return configFile;
		}
		public void setConfigFile(String configFile) {
			this.configFile = configFile;
		}
		
	}

}
