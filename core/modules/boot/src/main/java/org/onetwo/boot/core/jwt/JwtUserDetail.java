package org.onetwo.boot.core.jwt;

import java.util.Map;

import org.onetwo.common.convert.Types;
import org.onetwo.common.web.userdetails.UserDetail;

import com.google.common.collect.Maps;

import io.jsonwebtoken.Claims;

/**
 * @author wayshall
 * <br/>
 */
public class JwtUserDetail implements UserDetail {
	public static final String ANONYMOUS_LOGIN_KEY = "anonymousLogin";
	
	private long userId;
	private String userName;
	/***
	 * 把userdetail对象解释为token时，扩展属性properties会存储到claim，然后解释为token
	 */
	private Map<String, Object> properties = Maps.newHashMap();
	
	/***
	 * 从token解释为Claims对象
	 */
	private Claims claims;
	private JwtTokenInfo newToken;
//	private boolean anonymousLogin;
	
	public JwtUserDetail(long userId, String userName) {
		this(userId, userName, null);
	}
	public JwtUserDetail(long userId, String userName, Boolean anonymousLogin) {
		super();
		this.userId = userId;
		this.userName = userName;
//		this.anonymousLogin = anonymousLogin;
		if (anonymousLogin!=null) {
			this.properties.put(ANONYMOUS_LOGIN_KEY, anonymousLogin);
		}
	}

	public boolean isAnonymousLogin() {
		if (!properties.containsKey(ANONYMOUS_LOGIN_KEY)) {
			return false;
		}
		Boolean anonymousLogin = Types.convertValue(properties.get(ANONYMOUS_LOGIN_KEY), Boolean.class);
		if (anonymousLogin==null) {
			return false;
		}
		return anonymousLogin;
	}
	
	@SuppressWarnings("unchecked")
	public <T> T getProperty(String key, T def){
		T val = (T)getProperties().get(key);
		if(val==null){
			return def;
		}
		return val;
	}
	
	final public Map<String, Object> getProperties() {
		if(properties==null){
			return Maps.newHashMap();
		}
		return properties;
	}

	public void setProperties(Map<String, Object> properties) {
		this.getProperties().putAll(properties);
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
