package org.onetwo.boot.module.cache;


import java.io.Serializable;
import java.util.Collection;
import java.util.concurrent.TimeUnit;

import lombok.Builder;
import lombok.Data;

/**
 * @author wayshall
 * <br/>
 */
public interface CacheConfigProvider {

	Collection<CacheConfig> cacheConfigs();
	
	@SuppressWarnings("serial")
	@Data
	public class CacheConfig implements Serializable {
		String name;
		Long expire;
		TimeUnit timeUnit = TimeUnit.SECONDS;
		@Builder
		public CacheConfig(String name, Long expire, TimeUnit timeUnit) {
			super();
			this.name = name;
			this.expire = expire;
			if(timeUnit!=null){
				this.timeUnit = timeUnit;
			}
		}
	}
}
