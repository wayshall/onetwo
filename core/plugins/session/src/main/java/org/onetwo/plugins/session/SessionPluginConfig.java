package org.onetwo.plugins.session;

import java.util.Map;

import org.onetwo.common.spring.SpringUtils;
import org.onetwo.common.spring.plugin.ConfigurableContextPlugin.LoadableConfig;
import org.onetwo.common.utils.propconf.JFishProperties;

import redis.clients.jedis.Protocol;

public class SessionPluginConfig implements LoadableConfig {
	
	public static enum SessionRepository {
		CONTAINER,
		REDIS
	}

	private JFishProperties config;
	private SessionRepository sessionRepository;
	private boolean embeddedRedis;
	private EmbeddedRedisConfig embeddedRedisConfig;
	private boolean silentJdkSerializer;
	private Map<String, ?> externalRedisConfig;

	public SessionPluginConfig() {
	}
	
	@Override
	public void load(JFishProperties properties) {
		this.config = properties;
		String sr = properties.getProperty("session.repository", SessionRepository.CONTAINER.name());
		this.sessionRepository = SessionRepository.valueOf(sr.toUpperCase());
		
		this.embeddedRedis = properties.getBoolean("redis.embedded", true);
		if(embeddedRedis){
			Map<String, String> props = properties.getPropertiesStartWith("embedded.redis.");
			this.embeddedRedisConfig = SpringUtils.map2Bean(props, EmbeddedRedisConfig.class);
		}else{
			this.externalRedisConfig = properties.getPropertiesStartWith("redis.");
		}
		
		this.silentJdkSerializer = properties.getBoolean("jdkserializer.silent", false);
		
	}
	
	public boolean isContainerSession(){
		return getSessionRepository()==SessionRepository.CONTAINER;
	}

	public boolean isSilentJdkSerializer() {
		return silentJdkSerializer;
	}

	public SessionRepository getSessionRepository() {
		return sessionRepository;
	}

	@Override
	public JFishProperties getSourceConfig() {
		return config;
	}
	
	
	
	public boolean isEmbeddedRedis() {
		return embeddedRedis;
	}

	public EmbeddedRedisConfig getEmbeddedRedisConfig() {
		return embeddedRedisConfig;
	}

	public Map<String, ?> getExternalRedisConfig() {
		return externalRedisConfig;
	}

	public static class EmbeddedRedisConfig {
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
