package org.onetwo.ext.security.jwt;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.joda.time.DateTime;
import org.onetwo.common.date.Dates;
import org.onetwo.common.exception.BaseException;
import org.onetwo.common.reflect.BeanToMapConvertor;
import org.onetwo.common.reflect.BeanToMapConvertor.BeanToMapBuilder;
import org.onetwo.common.utils.GuavaUtils;
import org.onetwo.common.utils.StringUtils;
import org.onetwo.common.web.userdetails.GenericUserDetail;
import org.onetwo.ext.security.utils.GenericLoginUserDetails;
import org.onetwo.ext.security.utils.SecurityConfig;
import org.onetwo.ext.security.utils.SecurityConfig.JwtConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import com.google.common.collect.Maps;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.DefaultClaims;

/**
 * @author wayshall
 * <br/>
 */
public class DefaultJwtSecurityTokenService implements JwtSecurityTokenService {

	private final BeanToMapConvertor beanToMap = BeanToMapBuilder.newBuilder()
																.enableFieldNameAnnotation()
																.build();
	
	@Autowired
	private SecurityConfig securityConfig;
	private String propertyKey = JwtSecurityUtils.PROPERTY_KEY;
	
	protected Long getExpirationInSeconds(){
		return securityConfig.getJwt().getExpirationInSeconds();
	}
	
	protected GenericUserDetail<?> createUserDetailForAuthentication(Serializable userId, String username, 
			Collection<? extends GrantedAuthority> authorities, Claims claims) {
		return new GenericLoginUserDetails<>(userId, username, "N/A", authorities);
	}
	
	protected GenericUserDetail<?> createUserDetailForToken(User user) {
		return new GenericLoginUserDetails<>(0L, user.getUsername(), "N/A", user.getAuthorities());
	}
	
	@Override
	public JwtSecurityTokenInfo generateToken(Authentication authentication){
		if(authentication==null){
			return null;
		}
		
		GenericUserDetail<?> userDetails = null;
//		Collection<String> authorities = Collections.emptyList();
		String authoritiesString = "";
		if(authentication.getPrincipal() instanceof GenericUserDetail){
			userDetails = (GenericUserDetail<?>)authentication.getPrincipal();
		}else{
			User user = (User)authentication.getPrincipal();
			userDetails = createUserDetailForToken(user);
			Collection<String> authorities = user.getAuthorities()
					.stream()
					.map(auth->auth.getAuthority())
					.collect(Collectors.toSet());
			authoritiesString = GuavaUtils.join(authorities, ",");
		}
		
		if (StringUtils.isBlank(userDetails.getUserName())) {
			throw new BaseException("username can not be blank");
		}
		

		JwtConfig jwtConfig = securityConfig.getJwt();
		Map<String, Object> props = beanToMap.toFlatMap(userDetails);
		Long userId = (Long)props.remove(JwtSecurityUtils.CLAIM_USER_ID);
		String userName = (String)props.remove(JwtSecurityUtils.CLAIM_USER_NAME);
		LocalDateTime issuteAt = LocalDateTime.now();

		Date expirationDate = Dates.toDate(issuteAt.plusSeconds(getExpirationInSeconds().intValue()));
		JwtBuilder builder = Jwts.builder()
						.setSubject(userName)
						.setIssuer(jwtConfig.getIssuer())
						.setAudience(jwtConfig.getAudience())
		//				.setId(jti)
						.claim(JwtSecurityUtils.CLAIM_USER_ID, userId)
						.claim(JwtSecurityUtils.CLAIM_AUTHORITIES, authoritiesString)
						.setIssuedAt(Dates.toDate(issuteAt))
						.setExpiration(expirationDate)
						.signWith(SignatureAlgorithm.HS512, jwtConfig.getSigningKey());
		
		if(props!=null && !props.isEmpty()){
			props.forEach((k,v)->builder.claim(getPropertyKey(k), v));
		}
		
		String token = builder.compact();
		
		return JwtSecurityTokenInfo.builder()
							.token(token)
							.build();
	}
	

	protected Map<String, Object> toMap(Claims claims) {
		if (claims==null) {
			return Maps.newHashMap();
		}
		Map<String, Object> properties = claims.entrySet()
												.stream()
												.filter(entry->isPropertyKey(entry.getKey()))
												.collect(Collectors.toMap(entry->getProperty(entry.getKey()), entry->entry.getValue()));
		return properties;
	}

	private boolean isPropertyKey(Object key){
		return key.toString().startsWith(propertyKey);
	}
	private String getProperty(String key){
		return key.substring(propertyKey.length());
	}

	private String getPropertyKey(String prop){
		return propertyKey+prop;
	}
	
	@Override
	public Authentication createAuthentication(String token) throws BadCredentialsException {
		Claims claims = createClaimsFromToken(token);
		DateTime expireation = new DateTime(claims.getExpiration());
		if(expireation.isBeforeNow()){
			return null;
		}
		
		List<GrantedAuthority> authorities = Collections.emptyList();
		if (claims.containsKey(JwtSecurityUtils.CLAIM_AUTHORITIES)) {
			String authorityString = claims.get(JwtSecurityUtils.CLAIM_AUTHORITIES).toString();
			authorities = GuavaUtils.splitAsStream(authorityString, ",").map(auth->{
				return new SimpleGrantedAuthority(auth);
			})
			.collect(Collectors.toList());
		}
		
		Authentication authentication = buildAuthentication(claims, authorities);
		return authentication;
	}

	protected Authentication buildAuthentication(Claims claims, List<GrantedAuthority> authes){
		GenericUserDetail<?> userDetail = createUserDetailForAuthentication((Serializable)claims.get(JwtSecurityUtils.CLAIM_USER_ID), 
													claims.getSubject(), authes, claims);
		Authentication token = new UsernamePasswordAuthenticationToken(userDetail, "", authes);
		return token;
	}
	
	final protected Claims createClaimsFromToken(String token) throws BadCredentialsException {
		try {
			DefaultClaims claims = (DefaultClaims)Jwts.parser()
										.setSigningKey(securityConfig.getJwt().getSigningKey())
										.parse(token)
										.getBody();
			return claims;
		} catch(ExpiredJwtException e){
			throw new CredentialsExpiredException("session expired", e);
//			throw new ServiceException(JwtErrors.CM_ERROR_TOKEN, e);
		}catch (Exception e) {
			throw new CredentialsExpiredException("error token", e);
//			throw new ServiceException(JwtErrors.CM_ERROR_TOKEN, e);
		}
	}

	public void setSecurityConfig(SecurityConfig securityConfig) {
		this.securityConfig = securityConfig;
	}

	public void setPropertyKey(String propertyKey) {
		this.propertyKey = propertyKey;
	}

}
