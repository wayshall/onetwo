package org.onetwo.boot.core.jwt;

import java.util.Map;

import org.onetwo.common.spring.SpringUtils;
import org.onetwo.common.web.userdetails.UserDetail;

import com.google.common.collect.Maps;

/**
 * @author wayshall
 * <br/>
 */
public abstract class JwtUtils {
	public static final String AUTH_ATTR_KEY = "__Authorization__";
	public static final String DEFAULT_HEADER_KEY = "auth";
	
	public static final String PROPERTY_KEY = "p_";
	public static final String CLAIM_USER_ID = "userId";
	public static final String CLAIM_USER_NAME = "userName";
	public static final String CLAIM_AUTHORITIES = "authorities";
	
	

	public static <T extends UserDetail> T createUserDetail(JwtUserDetail jwtUserDetail, Class<T> parameterType) {
		Map<String, Object> userMap = Maps.newHashMap(jwtUserDetail.getProperties());
		userMap.put(JwtUtils.CLAIM_USER_ID, jwtUserDetail.getUserId());
		userMap.put(JwtUtils.CLAIM_USER_NAME, jwtUserDetail.getUserName());
		T userDetail = SpringUtils.map2Bean(userMap, parameterType);
		return userDetail;
	}
	
	private JwtUtils(){
	}

}
