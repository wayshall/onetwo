package org.onetwo.boot.module.oauth2.token;

import java.util.concurrent.TimeUnit;

import org.onetwo.boot.core.jwt.JwtErrors;
import org.onetwo.boot.module.redis.JsonValueRedisTemplate;
import org.onetwo.common.exception.ServiceException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.BoundValueOperations;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.util.Assert;

/**
 * 基于jwt，把tokenid和jwt token映射保存到redis
 * @author wayshall
 * <br/>
 */
public class JwtTokenRedisStore extends JwtTokenStore implements InitializingBean {

	public static final String OAUTH_JWT_TOKEN_PREFIX = "oauth2:jwt:";
	public static final String OAUTH_TOKEN_ID_PREFIX = "jf.";
	
	@Autowired
	private RedisConnectionFactory connectionFactory;
	private JsonValueRedisTemplate<JwtStoredTokenValue> redisTemplate;

	public JwtTokenRedisStore(JwtAccessTokenConverter jwtTokenEnhancer) {
		super(jwtTokenEnhancer);
	}
	

	@Override
	public void afterPropertiesSet() throws Exception {
		this.redisTemplate = new JsonValueRedisTemplate<JwtStoredTokenValue>(connectionFactory, JwtStoredTokenValue.class);
//		Assert.notNull(redisTemplate, "redisTemplate can not be null");
	}


	@Override
	public OAuth2Authentication readAuthentication(String token) {
//		JwtStoredTokenValue storedTokenValue = getJwtStoredTokenValue(tokenId);
		return super.readAuthentication(token);
	}
	
	/***
	 * 
	 */
	/*@Override
	public OAuth2Authentication readAuthentication(OAuth2AccessToken token) {
		String key = getStoreKey(token.getValue());
		BoundValueOperations<String, JwtStoredTokenValue> ops = redisTemplate.boundValueOps(key);
		JwtStoredTokenValue storedTokenValue = ops.get();
		return readAuthentication(storedTokenValue.getToken());
	}*/
	
	/***
	 * resouce server auth token
	 */
	@Override
	public OAuth2AccessToken readAccessToken(String tokenId) {
		JwtStoredTokenValue storedTokenValue = getJwtStoredTokenValue(tokenId);
		OAuth2AccessToken accessToken = super.readAccessToken(storedTokenValue.getToken());
		return accessToken;
	}

	/***
	 * auth server store accessToken
	 * tokenEndpoint store acessToken
	 */
	@Override
	public void storeAccessToken(OAuth2AccessToken token, OAuth2Authentication authentication) {
		DefaultOAuth2AccessToken at = (DefaultOAuth2AccessToken) token;
		String tokenId = getTokenId(at);
		Assert.hasLength(tokenId, "tokenId can not be null");
		String key = getStoreKey(tokenId);
		JwtStoredTokenValue value = JwtStoredTokenValue.builder()
											.token(at.getValue())
											.build();
		BoundValueOperations<String, JwtStoredTokenValue> ops = redisTemplate.boundValueOps(key);
		//保存到redis并设置过期时间
		ops.set(value, at.getExpiresIn(), TimeUnit.MILLISECONDS);
		//把tokenvalue置换为tokenId
		at.setValue(tokenId);
	}
	
	protected String getTokenId(DefaultOAuth2AccessToken at){
		Object tokenIdValue = at.getAdditionalInformation().get(JwtAccessTokenConverter.TOKEN_ID);
		if(tokenIdValue==null){
			throw new ServiceException(JwtErrors.CM_ERROR_TOKEN).put("token", at.getValue());
		}
		String tokenId = OAUTH_TOKEN_ID_PREFIX+tokenIdValue.toString();
		return tokenId;
	}
	
	protected final JwtStoredTokenValue getJwtStoredTokenValue(String tokenId){
		String key = getStoreKey(tokenId);
		BoundValueOperations<String, JwtStoredTokenValue> ops = redisTemplate.boundValueOps(key);
		JwtStoredTokenValue storedTokenValue = ops.get();
		if(storedTokenValue==null){
			throw new ServiceException(JwtErrors.CM_ERROR_TOKEN);
		}
		return storedTokenValue;
	}
	

	@Override
	public void removeAccessToken(OAuth2AccessToken token) {
		super.removeAccessToken(token);
		String tokenId = token.getValue();
		String key = getStoreKey(tokenId);
		redisTemplate.delete(key);
	}
	
	private String getStoreKey(String key){
		return OAUTH_JWT_TOKEN_PREFIX + key;
	}
	
}
