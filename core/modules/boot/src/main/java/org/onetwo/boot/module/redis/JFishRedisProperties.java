package org.onetwo.boot.module.redis;

import lombok.Data;
import lombok.EqualsAndHashCode;

import org.onetwo.common.utils.LangOps;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author wayshall
 * <br/>
 */
@ConfigurationProperties("jfish.redis")
@EqualsAndHashCode(callSuper=false)
@Data
public class JFishRedisProperties {

	public static final String ENABLED_KEY = "jfish.redis.enabled";
	public static final String ENABLED_LOCK_REGISTRY = "jfish.redis.lockRegistry.key";

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
