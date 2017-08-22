package org.onetwo.boot.core.jwt;
/**
 * @author wayshall
 * <br/>
 */
public abstract class JwtUtils {
	public static final String AUTH_ATTR_KEY = "__Authorization__";
	public static final String DEFAULT_HEADER_KEY = "auth";
	
	public static final String PROPERTY_KEY = "p_";
	public static final String CLAIM_USER_ID = "userId";
	public static final String CLAIM_AUTHORITIES = "authorities";
	
	private JwtUtils(){
	}

}
