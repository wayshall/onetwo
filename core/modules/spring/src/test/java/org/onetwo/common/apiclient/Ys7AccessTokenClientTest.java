package org.onetwo.common.apiclient;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.onetwo.common.apiclient.api.simple2.Ys7AccessTokenClient;
import org.onetwo.common.apiclient.api.simple2.Ys7AccessTokenClient.DeleteDeviceRequest;
import org.onetwo.common.apiclient.api.simple2.Ys7AccessTokenClient.GetAccessTokenRequst;
import org.onetwo.common.apiclient.api.simple2.Ys7AccessTokenClient.GetAccessTokenResponse;
import org.onetwo.common.apiclient.api.simple2.Ys7AccessTokenClient.SimpleResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

/**
 * @author wayshall
 * <br/>
 */
public class Ys7AccessTokenClientTest extends ApiClientBaseTests {
	
	@Autowired
	Ys7AccessTokenClient ys7AccessTokenClient;
	@Value("${ys7.appKey}")
	String appKey;
	@Value("${ys7.appSecret}")
	String appSecret;
	
	@Test
	public void testGEetAccessToken(){
		assertThat(appKey).isNotNull();
		assertThat(appSecret).isNotNull();
		GetAccessTokenResponse res = ys7AccessTokenClient.getAccessToken(appKey, appSecret);
		System.out.println("res:"+res);
		assertThat(res.getCode()).isEqualTo("200");
	}
	
	@Test
	public void testGetAccessTokenWithBody(){
		assertThat(appKey).isNotNull();
		assertThat(appSecret).isNotNull();
		GetAccessTokenResponse res = ys7AccessTokenClient.getAccessTokenWithBody(new GetAccessTokenRequst(appKey, appSecret));
		System.out.println("res:"+res);
		assertThat(res.getCode()).isEqualTo("200");
	}
	

	
	@Test
	public void testDeleteDevice(){

		GetAccessTokenResponse res = ys7AccessTokenClient.getAccessToken(appKey, appSecret);
		System.out.println("res:"+res);
		
		String at = res.getData().getAccessToken();
		
		DeleteDeviceRequest request = new DeleteDeviceRequest();
		request.setAccessToken(at);
		request.setDeviceSerial("");
		SimpleResponse delRes = ys7AccessTokenClient.deleteDevice(request);
		System.out.println("delRes:"+delRes);
		assertThat(res.getCode()).isEqualTo("200");
	}

}
