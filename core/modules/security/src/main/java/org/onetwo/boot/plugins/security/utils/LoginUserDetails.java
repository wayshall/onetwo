package org.onetwo.boot.plugins.security.utils;

import java.util.Collection;

import org.onetwo.common.web.userdetails.UserDetail;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

@SuppressWarnings("serial")
public class LoginUserDetails extends User implements UserDetail {
	public static final long ROOT_USER_ID = 1;

	final private long userId;
	
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

}
