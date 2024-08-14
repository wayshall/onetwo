package org.onetwo.ext.security.jwt;

/**
 * @author wayshall
 * <br/>
 */
public abstract class JwtSecurityUtils {
	public static final String DEFAULT_HEADER_KEY = "auth";
	
	public static final String EXT_KEY = "key_";
	

	public static final String PROPERTY_KEY = "p_";
	public static final String CLAIM_USER_ID = "userId";
	public static final String CLAIM_USER_NAME = "userName";
	public static final String CLAIM_NICK_NAME = "nickName";
	public static final String CLAIM_AUTHORITIES = "authorities";
	public static final String CLAIM_ROLES = "roles";
	public static final String CLAIM_TENANT_ID = "tenantId";
	
	
	private JwtSecurityUtils(){
	}

}
