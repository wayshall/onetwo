package org.onetwo.ext.security.jwt;

import java.io.Serializable;
import java.util.Collection;
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
