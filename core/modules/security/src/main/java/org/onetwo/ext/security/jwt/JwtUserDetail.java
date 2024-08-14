package org.onetwo.ext.security.jwt;

import java.io.Serializable;
import java.util.Map;

import org.onetwo.common.web.userdetails.GenericUserDetail;

import io.jsonwebtoken.Claims;

/**
 * @author weishao zeng
 * <br/>
 */
public interface JwtUserDetail extends GenericUserDetail<Serializable> {

	Map<String, Object> getProperties();
	
	boolean isAnonymousLogin();
	
	String getRoles();
	
	void setRoles(String roles);
	
	default void setClaims(Claims claims) {
	}

}