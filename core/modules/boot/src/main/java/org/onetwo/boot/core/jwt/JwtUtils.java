package org.onetwo.boot.core.jwt;

import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
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
	
	public static Optional<JwtUserDetail> getOrSetJwtUserDetail(HttpServletRequest request, JwtTokenService jwtTokenService, String authHeaderName) {
		Object data = request.getAttribute(JwtUtils.AUTH_ATTR_KEY);
		if(data instanceof JwtUserDetail){
			return Optional.of((JwtUserDetail)data);
		}
		String token = request.getHeader(authHeaderName);

		if(StringUtils.isBlank(token)){
//			throw new ServiceException(JwtErrors.CM_NOT_LOGIN);
			return Optional.empty();
		}
		
		JwtUserDetail userDetail = jwtTokenService.createUserDetail(token);
		request.setAttribute(JwtUtils.AUTH_ATTR_KEY, userDetail);
		return Optional.ofNullable(userDetail);
	}
	
	private JwtUtils(){
	}

}
