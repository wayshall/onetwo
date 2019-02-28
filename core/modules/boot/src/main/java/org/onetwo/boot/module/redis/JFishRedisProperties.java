package org.onetwo.boot.module.redis;

import lombok.Data;
import lombok.EqualsAndHashCode;

import org.onetwo.common.utils.LangOps;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author wayshall
 * <br/>
 */
@ConfigurationProperties(org.onetwo.boot.core.config.BootJFishConfig.ZIFISH_CONFIG_PREFIX+ ".redis")
@EqualsAndHashCode(callSuper=false)
@Data
public class JFishRedisProperties {

	public static final String ENABLED_KEY = org.onetwo.boot.core.config.BootJFishConfig.ZIFISH_CONFIG_PREFIX+ ".redis.enabled";
	public static final String SERIALIZER_KEY = org.onetwo.boot.core.config.BootJFishConfig.ZIFISH_CONFIG_PREFIX+ ".redis.serializer";
	public static final String ENABLED_LOCK_REGISTRY = org.onetwo.boot.core.config.BootJFishConfig.ZIFISH_CONFIG_PREFIX+ ".redis.lockRegistry.key";

	/*String hostName = "localhost";
	int port = Protocol.DEFAULT_PORT;
	int timeout = Protocol.DEFAULT_TIMEOUT;
	String password;
	boolean usePool = true;
	int dbIndex = 0;
	boolean convertPipelineAndTxResults = true;
	JedisPoolConfig pool;*/
	
	OnceTokenProperties onceToken = new OnceTokenProperties();
	LockRegistryProperties lockRegistry = new LockRegistryProperties();
	
	/*public String getHostName() {
		return hostName;
	}*/
	
	@Data
	public static class LockRegistryProperties {
		String key;
		long expireAfter = 60000;
		
		public String getKey(){
			return key;
		}
	}
	
	@Data
	public static class OnceTokenProperties {
	    private String expiredTime = "2m";
	    private String prefix = "once-token:";
	    
	    public int getExpiredInSeconds(){
	    	return (int)LangOps.timeToSeconds(expiredTime, 120);
	    }
		
	}
}
