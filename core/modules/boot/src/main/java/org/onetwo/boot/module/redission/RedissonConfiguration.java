package org.onetwo.boot.module.redission;

import java.io.IOException;
import java.util.Map;

import org.onetwo.boot.module.cache.RedisCacheProperties;
import org.onetwo.boot.module.cache.SpringCacheConfiguration;
import org.onetwo.boot.module.redission.RedissonProperties.SpringCache;
import org.onetwo.common.exception.BaseException;
import org.onetwo.common.propconf.JFishProperties;
import org.onetwo.common.reflect.ReflectUtils;
import org.onetwo.common.spring.utils.BeanPropertiesMapper;
import org.onetwo.common.utils.StringUtils;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.client.RedisClient;
import org.redisson.config.Config;
import org.redisson.config.SingleServerConfig;
import org.redisson.spring.cache.CacheConfig;
import org.redisson.spring.cache.RedissonSpringCacheManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.ClassPathResource;

import com.google.common.collect.Maps;

/**
 * @author wayshall
 * <br/>
 */
@Configuration
@ConditionalOnProperty(name=RedissonProperties.ENABLED_KEY)
@ConditionalOnClass(RedisClient.class)
@EnableConfigurationProperties(RedissonProperties.class)
public class RedissonConfiguration {
	@Autowired
	private RedissonProperties redissonProperties;
	
	@Bean(destroyMethod="shutdown")
	@ConditionalOnMissingBean(RedisClient.class)
	public RedissonClient redissonClient(){
		Config config = null;
		String configPath = null;
		try {
			if(StringUtils.isNotBlank(redissonProperties.getYamlConfig())){
				configPath = redissonProperties.getYamlConfig();
				config = Config.fromYAML(new ClassPathResource(configPath).getInputStream());
			}else if(StringUtils.isNotBlank(redissonProperties.getJsonConfig())){
				configPath = redissonProperties.getJsonConfig();
				config = Config.fromJSON(new ClassPathResource(configPath).getInputStream());
			}else{
				config = new Config();
				SingleServerConfig singleConfig = config.useSingleServer()
														.setAddress(redissonProperties.getAddress())
														.setPassword(redissonProperties.getPassword());
				JFishProperties singleServerProperties = redissonProperties.getSingleServer();
				if(!singleServerProperties.isEmpty()){
					BeanPropertiesMapper.ignoreNotFoundProperty(singleServerProperties)
										.fieldAccessors()
										.ignoreBlankString()
										.mapToObject(singleConfig);
				}
//				singleConfig.setConnectionPoolSize(connectionPoolSize);
			}
		} catch (Exception e) {
			throw new BaseException("read redisson config error: " + configPath, e);
		}
		if(redissonProperties.getCodec()!=null){
			config.setCodec(ReflectUtils.newInstance(redissonProperties.getCodec()));
		}else{
			config.setCodec(new FixJsonJacksonCodec());
		}
		RedissonClient client = Redisson.create(config);
		return client;
	}

	@ConditionalOnProperty(name=SpringCache.SPRING_CACHE_ENABLED_KEY)
	@EnableConfigurationProperties(RedisCacheProperties.class)
	protected static class RedissonSpringCacheConfiguration {
		@Autowired
		RedisCacheProperties redisCacheProperties;
		@Autowired
		RedissonProperties redissonProperties;
		
		@Bean
		@Primary
		@ConditionalOnMissingBean(RedissonSpringCacheManager.class)
		@ConditionalOnProperty(name=SpringCacheConfiguration.SPRING_CACHE_ENABLED_KEY, havingValue="redis")
		public CacheManager cacheManager(RedissonClient redissonClient) throws IOException {
			SpringCache springCache = redissonProperties.getSpringCache();
			RedissonSpringCacheManager cacheManager = null;
			if(StringUtils.isNotBlank(springCache.getConfigPath())){
				//"classpath:/org/redisson/spring/cache/cache-config.json" {"testMap":{"ttl":1440000,"maxIdleTime":720000}}
				cacheManager = new RedissonSpringCacheManager(redissonClient, springCache.getConfigPath());
			}else if(!springCache.getConfig().isEmpty()){
				cacheManager = new RedissonSpringCacheManager(redissonClient, springCache.getConfig());
			}else if(!redisCacheProperties.getExpires().isEmpty()){
				Map<String, CacheConfig> cacheConfig = Maps.newHashMap();
				redisCacheProperties.expiresInSeconds()
									.entrySet()
									.stream()
									.forEach(entry->{
										CacheConfig cc = new CacheConfig();
										cc.setTTL(entry.getValue()*1000);
										cacheConfig.put(entry.getKey(), cc);
									});
				cacheManager = new RedissonSpringCacheManager(redissonClient, cacheConfig);
			}else{
				cacheManager = new RedissonSpringCacheManager(redissonClient);
			}
			return cacheManager;
        }
		
	}

}
