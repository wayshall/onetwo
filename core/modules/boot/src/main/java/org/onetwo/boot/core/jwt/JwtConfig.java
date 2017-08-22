package org.onetwo.boot.core.jwt;

import java.util.concurrent.TimeUnit;

import lombok.Data;

import org.apache.commons.lang3.StringUtils;

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

	String authHeader = JwtUtils.DEFAULT_HEADER_KEY;
	String signingKey;
	Long expirationInSeconds = TimeUnit.HOURS.toSeconds(1);
	String issuer = "jfish";
	String audience = "webclient";
	
	public boolean isEnabled(){
		return StringUtils.isNotBlank(signingKey);
	}
	
}
