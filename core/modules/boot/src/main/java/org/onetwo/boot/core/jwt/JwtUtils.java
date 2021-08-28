package org.onetwo.boot.core.jwt;

import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.onetwo.common.reflect.ReflectUtils;
import org.onetwo.common.spring.SpringUtils;
import org.onetwo.common.web.userdetails.GenericUserDetail;
import org.onetwo.ext.security.utils.GenericLoginUserDetails;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.util.ClassUtils;

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
	
	private static final String SECURITY_USER_CLASS = "org.springframework.security.core.userdetails.User";
	
	public static boolean isSecurityUserPresent(){
		return ClassUtils.isPresent(SECURITY_USER_CLASS, ClassUtils.getDefaultClassLoader());
	}

	@SuppressWarnings("unchecked")
	public static <T extends GenericUserDetail<?>> T createUserDetail(JwtUserDetail jwtUserDetail, Class<T> parameterType) {
		if (parameterType==jwtUserDetail.getClass()) {
			return (T)jwtUserDetail;
		}
		Map<String, Object> userMap = Maps.newHashMap(jwtUserDetail.getProperties());
//		T userDetail = SpringUtils.map2Bean(userMap, parameterType);
		T userDetail = null;
		
		if (isSecurityUserPresent()) {
			Collection<GrantedAuthority> authorities = Collections.emptyList();
			if (jwtUserDetail instanceof User) {
				User user = (User) jwtUserDetail;
				authorities = user.getAuthorities();
			}
			if (GenericLoginUserDetails.class.isAssignableFrom(parameterType)) {
				Constructor<?> constructor = ReflectUtils.getConstructor(parameterType, Serializable.class, String.class, String.class, Collection.class);
				userDetail = (T)ReflectUtils.newInstance(constructor, jwtUserDetail.getUserId(), jwtUserDetail.getUserName(), "N/A", authorities);
				SpringUtils.setMap2Bean(userMap, userDetail);
			} else {
				userMap.put(JwtUtils.CLAIM_USER_ID, jwtUserDetail.getUserId());
				userMap.put(JwtUtils.CLAIM_USER_NAME, jwtUserDetail.getUserName());
				userMap.put(JwtUtils.CLAIM_AUTHORITIES, authorities);
				userDetail = SpringUtils.map2Bean(userMap, parameterType);
			}
		} else {
			userMap.put(JwtUtils.CLAIM_USER_ID, jwtUserDetail.getUserId());
			userMap.put(JwtUtils.CLAIM_USER_NAME, jwtUserDetail.getUserName());
			userDetail = SpringUtils.map2Bean(userMap, parameterType);
		}
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
