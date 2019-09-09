package org.onetwo.boot.core.jwt;

import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.onetwo.common.utils.LangOps;

import lombok.Data;

/**
 * iss: jwt签发者
sub: jwt所面向的用户
aud: 接收jwt的一方
exp: jwt的过期时间，这个过期时间必须要大于签发时间
nbf: 定义在什么时间之前，该jwt都是不可用的.
iat: jwt的签发时间
jti: jwt的唯一身份标识，主要用来作为一次性token,从而回避重放攻击。

 * @author wayshall
 * <br/>
 */
@Data
public class JwtConfig {
	
	public static final String PREFIX = org.onetwo.boot.core.config.BootJFishConfig.ZIFISH_CONFIG_PREFIX+ ".jwt";
	public static final String ENABLE_KEY = PREFIX + ".enable";

	String authHeader = JwtUtils.DEFAULT_HEADER_KEY;
	String signingKey;
	Long expirationInSeconds = TimeUnit.HOURS.toSeconds(1);
	String expiration;
	String issuer = "jfish";
	String audience = "webclient";
	String refreshTokenIfRemainingTime = "30s";
	
	public String getSigningKey(){
		String key = this.signingKey;
		if(StringUtils.isBlank(key)){
			//单实例部署时可随机生成
			key = RandomStringUtils.randomAscii(128);
			this.signingKey = key;
		}
		return key;
	}
	
	public String getAuthHeader(){
		return authHeader;
	}

	public Long getExpirationInSeconds() {
		if(StringUtils.isNotBlank(expiration)){
			long inSeconds = LangOps.timeToSeconds(expiration, TimeUnit.HOURS.toSeconds(1));
			return inSeconds;
		}
		return expirationInSeconds;
	}
	
	public long getRefreshTokenIfRemainingSeconds() {
		return LangOps.timeToSeconds(getRefreshTokenIfRemainingTime(), 30);
	}
	
}
