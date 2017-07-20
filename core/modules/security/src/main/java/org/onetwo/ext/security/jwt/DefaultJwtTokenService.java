package org.onetwo.ext.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.DefaultClaims;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.joda.time.DateTime;
import org.onetwo.common.utils.GuavaUtils;
import org.onetwo.ext.security.utils.LoginUserDetails;
import org.onetwo.ext.security.utils.SecurityConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * @author wayshall
 * <br/>
 */
public class DefaultJwtTokenService implements JwtTokenService {
	
	@Autowired
	private SecurityConfig securityConfig;
	
	protected Long getExpirationInSeconds(){
		return securityConfig.getJwt().getExpirationInSeconds();
	}
	
	@Override
	public JwtTokenInfo generateToken(Authentication authentication){
		if(authentication==null){
			return null;
		}
		LoginUserDetails userDetails = (LoginUserDetails)authentication.getPrincipal();
		Collection<String> authorities = userDetails.getAuthorities()
										.stream()
										.map(auth->auth.getAuthority())
										.collect(Collectors.toSet());
		String authoritiesString = GuavaUtils.join(authorities, ",");
		DateTime issuteAt = DateTime.now();
		Date expirationDate = issuteAt.plusSeconds(getExpirationInSeconds().intValue()).toDate();
		String token = Jwts.builder()
							.setSubject(userDetails.getUsername())
							.claim(JwtUtils.CLAIM_USER_ID, userDetails.getUserId())
							.claim(JwtUtils.CLAIM_AUTHORITIES, authoritiesString)
							.setIssuedAt(issuteAt.toDate())
							.setExpiration(expirationDate)
							.signWith(SignatureAlgorithm.HS512, securityConfig.getJwt().getSigningKey())
							.compact();
		
		return JwtTokenInfo.builder()
							.token(token)
							.build();
	}
	
	@Override
	public Authentication createAuthentication(String token) throws BadCredentialsException {
		Claims claims = createClaimsFromToken(token);
		DateTime expireation = new DateTime(claims.getExpiration());
		if(expireation.isBeforeNow()){
			return null;
		}
		String authorityString = claims.get(JwtUtils.CLAIM_AUTHORITIES).toString();
		List<GrantedAuthority> authorities = GuavaUtils.splitAsStream(authorityString, ",").map(auth->{
			return new SimpleGrantedAuthority(auth);
		})
		.collect(Collectors.toList());
		
		Authentication authentication = buildAuthentication(claims, authorities);
		return authentication;
	}

	protected Authentication buildAuthentication(Claims claims, List<GrantedAuthority> authes){
		UserDetails userDetail = new LoginUserDetails(Long.parseLong(claims.get(JwtUtils.CLAIM_USER_ID).toString()), 
													claims.getSubject(), "", authes);
		Authentication token = new UsernamePasswordAuthenticationToken(userDetail, "", userDetail.getAuthorities());
		return token;
	}
	
	final protected Claims createClaimsFromToken(String token) throws BadCredentialsException {
		try {
			DefaultClaims claims = (DefaultClaims)Jwts.parser()
										.setSigningKey(securityConfig.getJwt().getSigningKey())
										.parse(token)
										.getBody();
			return claims;
		} catch (Exception e) {
			throw new BadCredentialsException("error token");
		}
	}

}
