package org.onetwo.boot.module.oauth2.token;

import javax.sql.DataSource;

import org.onetwo.boot.module.oauth2.JFishOauth2Properties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;

/**
 * @author wayshall
 * <br/>
 */
@Configuration
@ConditionalOnClass(TokenStore.class)
@ConditionalOnProperty(name=JFishOauth2Properties.TOKEN_STORE_ENABLED_KEY)
@EnableConfigurationProperties(JFishOauth2Properties.class)
public class Oauth2TokenStoreConfiguration {
//	static private final Logger logger = JFishLoggerFactory.getLogger(Oauth2TokenStoreConfiguration.class);

	@Configuration
	@ConditionalOnProperty(name=JFishOauth2Properties.TOKEN_STORE_ENABLED_KEY, havingValue=JFishOauth2Properties.KEYS_REDIS)
	protected static class  RedisTokenStoreConfiguration {
		@Bean
		public RedisTokenStore redisTokenStore(@Autowired RedisConnectionFactory redisConnectionFactory){
			RedisTokenStore store = new RedisTokenStore(redisConnectionFactory);
			return store;
		}
	}

	@Configuration
	@ConditionalOnProperty(name=JFishOauth2Properties.TOKEN_STORE_ENABLED_KEY, havingValue=JFishOauth2Properties.KEYS_JDBC)
	protected static class JdbcTokenStoreConfiguration {
		@Bean
		public JdbcTokenStore jdbcTokenStore(@Autowired DataSource dataSource){
			JdbcTokenStore store = new JdbcTokenStore(dataSource);
			return store;
		}
	}

	/***
	 * @author wayshall
	 *
	 */
	@Configuration
	@ConditionalOnProperty(name=JFishOauth2Properties.TOKEN_STORE_ENABLED_KEY, havingValue=JFishOauth2Properties.KEYS_JWT)
	protected static class  JwtTokenStoreConfiguration {
		@Autowired
		private JFishOauth2Properties jfishOauth2Properties;
		
		@Bean
		public JwtTokenStore jwtTokenStore(){
			JwtTokenStore store = new JwtTokenStore(jwtAccessTokenConverter());
			return store;
		}

		@Bean
		public JwtAccessTokenConverter jwtAccessTokenConverter() {
			JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
			converter.setSigningKey(jfishOauth2Properties.getJwt().getSigningKey());
			/*if (keyValue != null) {
				converter.setVerifierKey(keyValue);
			}*/
			return converter;
		}

	}
	

	@Configuration
	@ConditionalOnProperty(name=JFishOauth2Properties.TOKEN_STORE_ENABLED_KEY, havingValue=JFishOauth2Properties.KEYS_JWT_REDIS)
	protected static class JwtRedisTokenStoreConfiguration {
		@Autowired
		private JFishOauth2Properties jfishOauth2Properties;
		
		@Bean
		public JwtTokenStore jwtTokenStore(){
			JwtTokenStore store = new JwtTokenRedisStore(jwtAccessTokenConverter());
			return store;
		}

		@Bean
		public JwtAccessTokenConverter jwtAccessTokenConverter() {
			JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
			converter.setSigningKey(jfishOauth2Properties.getJwt().getSigningKey());
			/*if (keyValue != null) {
				converter.setVerifierKey(keyValue);
			}*/
			return converter;
		}

	}
}
