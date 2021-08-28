package org.onetwo.ext.security.utils;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;

import org.onetwo.common.web.userdetails.GenericUserDetail;
import org.onetwo.common.web.userdetails.UserRoot;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

@SuppressWarnings("serial")
public class GenericLoginUserDetails<ID extends Serializable> extends User implements GenericUserDetail<ID>, /*SsoTokenable,*/ UserRoot {

	final private ID userId;
//	private String token;
	private String nickname;
	private String avatar;
//	private boolean systemRootUser;
	
	public GenericLoginUserDetails(ID userId, String username, String password,
			Collection<? extends GrantedAuthority> authorities) {
		super(username, password, authorities);
		this.userId = userId;
	}
	
	public GenericLoginUserDetails(ID userId, String username) {
		this(userId, username, "N/A", Collections.emptyList());
	}

	public ID getUserId() {
		return userId;
	}

	@Override
	public String getUserName() {
		return getUsername();
	}

	@Override
	public boolean isSystemRootUser() {
		return getUserId().equals(ROOT_USER_ID);
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}
	
	/*public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}*/

}
