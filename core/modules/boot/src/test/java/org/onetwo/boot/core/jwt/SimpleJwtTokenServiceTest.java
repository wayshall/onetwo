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
		jwtConfig.setSigningKey("59}SF^,.7ImHaTJAgzpG.[J+\"Bg_Ie$KK3C+rth,.v{8$UXGG|gBMy%>K$M5V3&JKS?nO%QzZk75%Zj1)JFqB8J^l_w{[v5*'iD~Ye7hXx\"Wo|,A[AR+K~uHEQ)7;");
		tokenService.setJwtConfig(jwtConfig);
		
		SimpleUserDetail userDetail = new SimpleUserDetail();
		userDetail.setUserId(111L);
		userDetail.setUserName("test");
		userDetail.setToken("testTOken");
		JwtTokenInfo tokenInfo = tokenService.generateToken(userDetail);
		System.out.println("tokenInfo:"+tokenInfo);
		
		SimpleUserDetail ud = tokenService.createUserDetail(tokenInfo.getToken(), SimpleUserDetail.class);
		assertThat(ud).isNotNull();
		assertThat(ud.getUserId()).isEqualTo(userDetail.getUserId());
		assertThat(ud.getUserName()).isEqualTo(userDetail.getUserName());
		assertThat(ud.getToken()).isEqualTo(userDetail.getToken());
	}

}
