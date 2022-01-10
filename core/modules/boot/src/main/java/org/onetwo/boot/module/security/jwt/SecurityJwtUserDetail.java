package org.onetwo.boot.module.security.jwt;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;

import org.onetwo.boot.core.jwt.JwtUserDetail;
import org.onetwo.ext.security.utils.GenericLoginUserDetails;
import org.springframework.security.core.GrantedAuthority;

import com.google.common.collect.Maps;

/**
 * @author weishao zeng
 * <br/>
 */
@SuppressWarnings("serial")
public class SecurityJwtUserDetail extends GenericLoginUserDetails<Serializable> implements JwtUserDetail {

	/***
	 * 把userdetail对象解释为token时，扩展属性properties会存储到claim，然后解释为token
	 */
	private Map<String, Object> properties = Maps.newHashMap();
	private boolean anonymousLogin;

	public SecurityJwtUserDetail(Serializable userId, String username, String password,
			Collection<? extends GrantedAuthority> authorities) {
		super(userId, username, password, authorities);
	}

	@Override
	public Map<String, Object> getProperties() {
		return properties;
	}

	public void setProperties(Map<String, Object> properties) {
		this.properties = properties;
	}

	@Override
	public boolean isAnonymousLogin() {
		return anonymousLogin;
	}

	public void setAnonymousLogin(boolean anonymousLogin) {
		this.anonymousLogin = anonymousLogin;
	}

}
