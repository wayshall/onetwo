package org.onetwo.common.apiclient.api;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import org.onetwo.common.apiclient.annotation.RestApiClient;
import org.onetwo.common.apiclient.api.Ys7AccessTokenClient.GetAccessTokenResponse.AccessTokenInfo;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


@RestApiClient(url = "https://open.ys7.com/api/lapp")
public interface Ys7AccessTokenClient {

    @RequestMapping(value = "/token/get", method = RequestMethod.POST)
    GetAccessTokenResponse getAccessToken(String appKey, String appSecret);
    
    @RequestMapping(value = "/token/get", method = RequestMethod.POST)
    GetAccessTokenResponse getAccessTokenWithBody(GetAccessTokenRequst request);
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public class GetAccessTokenRequst {
    	String appKey;
    	String appSecret;
    }
    

	@Data
	@ToString(callSuper = true)
	@EqualsAndHashCode(callSuper=true)
	public class GetAccessTokenResponse extends BaseResponse<AccessTokenInfo>{
	
	    @Data
	    public static class AccessTokenInfo {
	        String accessToken;
	        Long expireTime;
	    }
	}
	
	@Data
	public class BaseResponse<T> {
	    String code;
	    String msg;
	    T data;
	}

}
