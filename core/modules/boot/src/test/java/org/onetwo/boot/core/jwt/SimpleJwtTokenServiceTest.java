package org.onetwo.boot.core.jwt;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.onetwo.common.date.NiceDate;
import org.onetwo.common.jackson.JsonMapper;
import org.onetwo.common.md.Hashs;
import org.onetwo.common.spring.SpringUtils;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.web.userdetails.SimpleUserDetail;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.Data;

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
		jwtConfig.setExpiration("1d");
		tokenService.setJwtConfig(jwtConfig);
		
		SimpleUserDetail userDetail = new SimpleUserDetail();
		userDetail.setUserId(111L);
		userDetail.setUserName("test");
//		userDetail.setToken("testTOken");
		JwtTokenInfo tokenInfo = tokenService.generateToken(userDetail);
		System.out.println("tokenInfo:"+tokenInfo);
		
		String token = tokenInfo.getToken();
		SimpleUserDetail ud = tokenService.createUserDetail(token, SimpleUserDetail.class);
		assertThat(ud).isNotNull();
		assertThat(ud.getUserId()).isEqualTo(userDetail.getUserId());
		assertThat(ud.getUserName()).isEqualTo(userDetail.getUserName());
//		assertThat(ud.getToken()).isEqualTo(userDetail.getToken());
	}
	
	@Test
	public void testZhipuToken() {
		// bigmodel token
		ApiAuthParams params = new ApiAuthParams();
		params.setApi_key("id");
		Date ts = NiceDate.Now().getTime();
		Date exp = NiceDate.Now().nextMonth(1).getTime();
		
		params.setExp(exp.getTime());
		params.setTimestamp(ts.getTime());
		
		String json = JsonMapper.toJsonString(params);
		System.out.println("json: " + json);
		
		Map<String, Object> map = SpringUtils.toMap(params);
		
		
		JwtBuilder builder = Jwts.builder()
				.setClaims(map)
				.setIssuedAt(ts)
				.setExpiration(exp)
				.setHeaderParam("alg", "HS256")
				.setHeaderParam("sign_type", "SIGN")
				.signWith(SignatureAlgorithm.HS256, LangUtils.getBytes("secrect"));

		
		String token = builder.compact();
		System.out.println("token: " + token);
	}
	

	@Test
	public void testBcSign() {
		// bigmodel token
		Date ts = NiceDate.Now().getTime();

		List<PropmtData> list = Lists.newArrayList();
		PropmtData p1 = new PropmtData();
		p1.setRole("user");
		p1.setContent("你是一个把 中文 翻译为 英文 的得力助手。请翻译我说出的中文，只返回英文即可。");
		list.add(p1);
		
		p1 = new PropmtData();
		p1.setRole("user");
		p1.setContent("足球");
		
		Map<String, Object> body = Maps.newHashMap();
		body.put("prompt", list);
		
		String utcTs = String.valueOf(ts.getTime()/1000);
		String bodyStr = JsonMapper.toJsonString(body);
		String apiSecrect = "";
		String source = apiSecrect + bodyStr + utcTs;
		String sign = Hashs.MD5.hash(source);
		
		System.out.println("utcTs: " + utcTs);
		System.out.println("bodyStr: " + bodyStr);
		System.out.println("sign: " + sign);
	}
	
	@Data
	private static class ApiAuthParams {
		String api_key;
		Long exp;
		Long timestamp;
	}

	
	@Data
	private static class PropmtData {
		String role;
		String content;
	}
	
}
