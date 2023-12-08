package org.onetwo.ext.security.jwt;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;

import org.onetwo.ext.security.utils.GenericLoginUserDetails;
import org.springframework.security.core.GrantedAuthority;

import com.google.common.collect.Maps;

/**
 * @author weishao zeng
 * <br/>
 */
@SuppressWarnings("serial")
public class SecurityJwtUserDetail extends GenericLoginUserDetails<Serializable> implements JwtUserDetail {
	public static final String ANONYMOUS_LOGIN_KEY = "anonymousLogin";

	/***
	 * 把userdetail对象解释为token时，扩展属性properties会存储到claim，然后解释为token
	 */
	private Map<String, Object> properties = Maps.newHashMap();
	private boolean anonymousLogin;
	private String roles;
	private Long tenantId;

	public SecurityJwtUserDetail(Serializable userId, String username, String password,
			Collection<? extends GrantedAuthority> authorities) {
		super(userId, username, password, authorities);
	}

	public SecurityJwtUserDetail(Serializable userId, String userName, Boolean anonymousLogin) {
		super(userId, userName, "N/A", Collections.emptySet());
		if (anonymousLogin!=null) {
			this.anonymousLogin = anonymousLogin; 
			this.properties.put(ANONYMOUS_LOGIN_KEY, anonymousLogin);
		}
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
	public String getRoles() {
		return roles;
	}
	public void setRoles(String roles) {
		this.roles = roles;
	}
	public Long getTenantId() {
		return tenantId;
	}
	public void setTenantId(Long tenantId) {
		this.tenantId = tenantId;
	}
}
