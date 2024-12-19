package org.onetwo.ext.security.utils;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.onetwo.common.utils.StringUtils;
import org.onetwo.common.web.userdetails.UserDetail;
import org.springframework.security.core.GrantedAuthority;

import lombok.Setter;

/**
 * 移除admin_organ，用户表增加tenant_id和保留organId用于业务扩展
 * 
 * @author weishao zeng
 * <br/>
 */
@SuppressWarnings("serial")
public class AdminLoginUserInfo extends GenericLoginUserDetails<Long> implements UserDetail {
	public static final String ROLE_ADMIN = "ADMIN";

	@Setter
	private Long bindingUserId;
	
	@Setter
	private String organId;
	@Setter
	private Long tenantId;
	
	private List<String> roles;
	
	public AdminLoginUserInfo(long userId, String username, String password,
			Collection<? extends GrantedAuthority> authorities) {
		super(userId, username, password, authorities==null?Collections.emptyList():authorities);
	}
	
	public boolean isAdminRole() {
		return this.roles!=null && this.roles.contains(ROLE_ADMIN);
	}
	
	public boolean isRole(String roleCode) {
		return this.roles!=null && this.roles.contains(roleCode);
	}

	public String getOrganId() { 
		return organId; 
	}

	public Long getOrganIdAsLong() {
		if (StringUtils.isBlank(organId)) {
			return null;
		}
		return Long.valueOf(organId); 
	}

	public Long getBindingUserId() {
		return bindingUserId;
	}

	public Long getTenantId() {
		return tenantId;
	}

	public List<String> getRoles() {
		return roles;
	}

	public void setRoles(List<String> roles) {
		this.roles = roles;
	}

}

