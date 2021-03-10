package org.onetwo.boot.module.oauth2ssoclient.accesstoken;

import java.util.Optional;

import org.onetwo.boot.module.oauth2ssoclient.response.AccessTokenInfo;
import org.onetwo.common.log.JFishLoggerFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.redis.util.RedisLockRegistry;
import org.springframework.util.Assert;

import lombok.Getter;
import lombok.Setter;

/**
 * @author wayshall
 * <br/>
 */
abstract public class AbstractAccessTokenService implements InitializingBean {

	public static final String CACHE_PREFIX = "SSOCLIENT:ACESSTOKEN:";
	
	protected final Logger logger = JFishLoggerFactory.getLogger(getClass());

	@Getter
	private SsoClientAccessTokenProvider accessTokenProvider;
	
	@Autowired
	private RedisLockRegistry redisLockRegistry;
//	private RedisLockRunner redisLockRunner;
	@Setter
	private long lockWaitInSeconds = 1;
	
	@Override
	public void afterPropertiesSet() throws Exception {
		Assert.notNull(redisLockRegistry, "redisLockRegistry not found");
	}

	public AccessTokenInfo getOrRefreshAccessToken(String userId) {
		Optional<AccessTokenInfo> atOpt = getAccessTokenFromCache(userId);
		if (atOpt.isPresent()) {
			AccessTokenInfo at = atOpt.get();
			if (at.isExpired()) {
				// refresh
				at = accessTokenProvider.refreshAccessToken();
				return at;
			} else {
				return at;
			}
		} else {
			// get
			AccessTokenInfo at = accessTokenProvider.getAccessToken();
			return at;
		}
	}

	abstract protected Optional<AccessTokenInfo> getAccessTokenFromCache(String userId);
	
}
