package org.onetwo.common.utils;

public interface SsoTokenable {

	public final static String TOKEN_KEY = "token"; 
	
	public String getToken();
	
	public void setToken(String token);
}
