package org.onetwo.boot.module.redis;

import lombok.Data;
import lombok.EqualsAndHashCode;

import org.onetwo.common.propconf.JFishProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;

import redis.clients.jedis.JedisPoolConfig;

/**
 * @author wayshall
 * <br/>
 */
@ConfigurationProperties("jfish.redis")
@SuppressWarnings("serial")
@EqualsAndHashCode(callSuper=false)
@Data
public class JFishRedisProperties extends JFishProperties {

	public static final String ENABLED_KEY = "jfish.redis.enabled";
	public static final String ENABLED_LOCK_REGISTRY = "jfish.redis.lockRegistry.key";
	
	public JFishRedisProperties() {
		super();
		this.setProperty("hostName", "localhost");
		this.setProperty("port", "6379");
	}
	private JedisPoolConfig pool;
	
	LockRegistryProperties lockRegistry = new LockRegistryProperties();

	public String getHostName() {
		return getProperty("hostName");
	}
	public int getPort() {
		return getInt("port");
	}
	
	@Data
	public static class LockRegistryProperties {
		String key;
		long expireAfter = 60000;
	}
}
