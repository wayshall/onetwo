package org.onetwo.boot.core.jwt;

import java.util.Collections;
import java.util.Map;

import org.onetwo.common.web.userdetails.UserDetail;

import io.jsonwebtoken.Claims;

/**
 * @author wayshall
 * <br/>
 */
public class JwtUserDetail implements UserDetail {
	private long userId;
	private String userName;
	/***
	 * 把userdetail对象解释为token时，扩展属性properties会存储到claim，然后解释为token
	 */
	private Map<String, Object> properties;
	
	/***
	 * 从token解释为Claims对象
	 */
	private Claims claims;
	private JwtTokenInfo newToken;
	
	public JwtUserDetail(long userId, String userName) {
		super();
		this.userId = userId;
		this.userName = userName;
	}
	
	@SuppressWarnings("unchecked")
	public <T> T getProperty(String key, T def){
		T val = (T)getProperties().get(key);
		if(val==null){
			return def;
		}
		return val;
	}
	
	public Map<String, Object> getProperties() {
		if(properties==null){
			return Collections.emptyMap();
		}
		return properties;
	}

	public void setProperties(Map<String, Object> properties) {
		this.properties = properties;
	}


	@Override
	public String getUserName() {
		return userName;
	}

	@Override
	public long getUserId() {
		return userId;
	}

	public Claims getClaims() {
		return claims;
	}

	public void setClaims(Claims claims) {
		this.claims = claims;
	}

	public JwtTokenInfo getNewToken() {
		return newToken;
	}

	public void setNewToken(JwtTokenInfo newToken) {
		this.newToken = newToken;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

}
