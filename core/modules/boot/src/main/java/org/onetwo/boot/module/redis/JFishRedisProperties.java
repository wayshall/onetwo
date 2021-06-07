package org.onetwo.boot.module.redis;

import org.onetwo.boot.core.config.BootJFishConfig;
import org.onetwo.common.utils.LangOps;
import org.springframework.boot.context.properties.ConfigurationProperties;

import org.onetwo.common.utils.LangOps;
import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;
import lombok.EqualsAndHashCode;

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
	private static final String LOCK_PREFX_KEY = BootJFishConfig.ZIFISH_CONFIG_PREFIX + ":RedisLock:";
//	private static final String LOCK_PREFX_KEY = "Zifish:RedisLock:";

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
	
	String cacheKeyPrefix = RedisOperationService.DEFAUTL_CACHE_PREFIX;
	
	/*public String getHostName() {
		return hostName;
	}*/
	
	@Data
	public static class LockRegistryProperties {
		public static final String DEFAULT_LOCK_KEY = "${"+BootJFishConfig.PREFIX+".redis.lockRegistry.key:${spring.application.name}}";
		/***
		 * default is 2 min
		 */
		long expireAfter = 120000;
		
		public String getLockKey(String key){
			return LOCK_PREFX_KEY + key;
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
