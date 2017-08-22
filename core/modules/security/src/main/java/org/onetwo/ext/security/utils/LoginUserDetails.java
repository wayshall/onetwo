package org.onetwo.ext.security.utils;

import java.util.Collection;

import org.onetwo.common.web.userdetails.SsoTokenable;
import org.onetwo.common.web.userdetails.UserDetail;
import org.onetwo.common.web.userdetails.UserRoot;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

@SuppressWarnings("serial")
public class LoginUserDetails extends User implements UserDetail, SsoTokenable, UserRoot {

	final private long userId;
	private String token;
	
	public LoginUserDetails(long userId, String username, String password,
			Collection<? extends GrantedAuthority> authorities) {
		super(username, password, authorities);
		this.userId = userId;
	}

	public long getUserId() {
		return userId;
	}

	@Override
	public String getUserName() {
		return getUsername();
	}

	@Override
	public boolean isSystemRootUser() {
		return userId==ROOT_USER_ID;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

}
