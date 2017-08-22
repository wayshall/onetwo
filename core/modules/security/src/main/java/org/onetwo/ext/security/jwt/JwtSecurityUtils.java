package org.onetwo.ext.security.jwt;
/**
 * @author wayshall
 * <br/>
 */
public abstract class JwtSecurityUtils {
	public static final String DEFAULT_HEADER_KEY = "auth";
	
	public static final String CLAIM_USER_ID = "userId";
	public static final String EXT_KEY = "key_";
	public static final String CLAIM_AUTHORITIES = "authorities";
	
	private JwtSecurityUtils(){
	}

}
