package org.onetwo.boot.module.oauth2ssoclient;

import javax.servlet.http.HttpServletRequest;

import org.onetwo.common.exception.ServiceException;

import lombok.Setter;

/**
 * @author wayshall
 * <br/>
 */
public interface OAuth2SsoContext {
	
	String getClientId();
	

	String getCode();
	
	String getState();
	
	HttpServletRequest getRequest();
	Oauth2SsoClientConfig getOauth2SsoClientConfig();
	
	String getRedirectUrl();
	

//	default boolean isSsnUserInfoScope(){
//		Oauth2SsoClientConfig wechatConfig = getOauth2SsoClientConfig();
//		return Oauth2SsoClients.SCOPE_SNSAPI_USERINFO.equalsIgnoreCase(wechatConfig.getOauth2Scope());
//	}
	

	public class RequestOAuth2SsoContext implements OAuth2SsoContext {
		private HttpServletRequest request;
		@Setter
		private Oauth2SsoClientConfig ssoClientConfig;
		
		public RequestOAuth2SsoContext(HttpServletRequest request) {
			super();
			if (request==null) {
				throw new ServiceException("required a web request!");
			}
			this.request = request;
		}
		

		public String getClientId() {
			String appid = request.getParameter("clientId");
			return appid;
		}
		
		public String getCode() {
			return request.getParameter(Oauth2SsoClients.PARAMS_CODE);
		}
		
		public String getState() {
			return request.getParameter(Oauth2SsoClients.PARAMS_STATE);
		}

		@Override
		public HttpServletRequest getRequest() {
			return request;
		}


		@Override
		public Oauth2SsoClientConfig getOauth2SsoClientConfig() {
			return ssoClientConfig;
		}

		public String getRedirectUrl() {
			return request.getParameter("redirectUrl");
		}
		
	}
	

	public class DataWechatOAuth2Context implements OAuth2SsoContext {
		private HttpServletRequest request;
		private OAuth2SsoRequest oauth2Request;
		@Setter
		private Oauth2SsoClientConfig oauthSsoClientConfig;
		
		public DataWechatOAuth2Context(OAuth2SsoRequest oauth2Request, HttpServletRequest request) {
			super();
			this.request = request;
			this.oauth2Request = oauth2Request;
		}
		

		public String getClientId() {
			String appid = oauth2Request.getClientId();
			return appid;
		}
		
		public String getCode() {
			return oauth2Request.getCode();
		}
		
		public String getState() {
			return oauth2Request.getState();
		}

		public String getRedirectUrl() {
			return oauth2Request.getRedirectUrl();
		}

		@Override
		public HttpServletRequest getRequest() {
			return request;
		}
		
		@Override
		public Oauth2SsoClientConfig getOauth2SsoClientConfig() {
			return oauthSsoClientConfig;
		}
		
	}

}
