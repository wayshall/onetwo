package org.onetwo.boot.module.security.oauth2;

import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

import org.onetwo.common.convert.Types;
import org.onetwo.common.reflect.ReflectUtils;
import org.onetwo.common.spring.SpringUtils;
import org.onetwo.ext.security.utils.LoginUserDetails;
import org.springframework.boot.autoconfigure.security.oauth2.resource.PrincipalExtractor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

/***
 * convert authenticate server Principal object to local Principal
 * @author way
 *
 */
public class UserDetailPrincipalExtractor implements PrincipalExtractor {
	
	private Class<? extends UserDetails> userDetailClass;

	public UserDetailPrincipalExtractor() {
		super();
	}

	public UserDetailPrincipalExtractor(Class<? extends UserDetails> userDetailClass) {
		super();
		this.userDetailClass = userDetailClass;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Object extractPrincipal(Map<String, Object> map) {
		Long userId = Types.convertValue(map.get("userId"), Long.class);
		String username = SpringUtils.convertValue(map.get("username"), String.class);
		Collection<Map<String, Object>> authorities = (Collection<Map<String, Object>>)map.get("authorities");
		Collection<? extends GrantedAuthority> authorityCollection = authorities.stream().map(g->{
			return new SimpleGrantedAuthority(g.get("authority").toString());
		}).collect(Collectors.toSet());
		LoginUserDetails userDetail = new LoginUserDetails(userId, username, "N/A", authorityCollection);
		return userDetailClass==null?userDetail:ReflectUtils.newByConstructor(userDetailClass, 0, userDetail);
	}

	public void setUserDetailClass(Class<? extends UserDetails> userDetailClass) {
		this.userDetailClass = userDetailClass;
	}
	
	

}
