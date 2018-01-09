package org.onetwo.boot.core.jwt;

import java.util.Collections;
import java.util.Map;

import org.onetwo.common.web.userdetails.UserDetail;

/**
 * @author wayshall
 * <br/>
 */
public class JwtUserDetail implements UserDetail {
	private long userId;
	private String userName;
	
	private Map<String, Object> properties;
	
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

}
