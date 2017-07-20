package org.onetwo.ext.security.jwt;
/**
 * @author wayshall
 * <br/>
 */
public abstract class JwtUtils {
	public static final String DEFAULT_HEADER_KEY = "Authorization";
	
	public static final String CLAIM_USER_ID = "userId";
	public static final String CLAIM_AUTHORITIES = "authorities";
	
	private JwtUtils(){
	}

}
