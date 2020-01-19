package org.onetwo.boot.core.jwt;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.onetwo.common.web.userdetails.SimpleUserDetail;

/**
 * @author wayshall
 * <br/>
 */
public class SimpleJwtTokenServiceTest {
	
	@Test
	public void test(){
		SimpleJwtTokenService tokenService = new SimpleJwtTokenService();
		JwtConfig jwtConfig = new JwtConfig();
		jwtConfig.setSigningKey("test");
		tokenService.setJwtConfig(jwtConfig);
		
		SimpleUserDetail userDetail = new SimpleUserDetail();
		userDetail.setUserId(111L);
		userDetail.setUserName("test");
//		userDetail.setToken("testTOken");
		JwtTokenInfo tokenInfo = tokenService.generateToken(userDetail);
		System.out.println("tokenInfo:"+tokenInfo);
		
		SimpleUserDetail ud = tokenService.createUserDetail(tokenInfo.getToken(), SimpleUserDetail.class);
		assertThat(ud).isNotNull();
		assertThat(ud.getUserId()).isEqualTo(userDetail.getUserId());
		assertThat(ud.getUserName()).isEqualTo(userDetail.getUserName());
//		assertThat(ud.getToken()).isEqualTo(userDetail.getToken());
	}

}
