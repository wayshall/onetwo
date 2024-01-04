package org.onetwo.ext.security.ajax;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.onetwo.common.data.DataResult;
import org.onetwo.common.jackson.JsonMapper;
import org.onetwo.common.spring.mvc.utils.DataResults;
import org.onetwo.common.web.utils.Browsers.BrowserMeta;
import org.onetwo.common.web.utils.RequestUtils;
import org.onetwo.common.web.utils.ResponseUtils;
import org.onetwo.ext.security.jwt.JwtAuthStores.StoreContext;
import org.onetwo.ext.security.utils.CookieStorer;
import org.onetwo.ext.security.utils.SecurityConfig;
import org.onetwo.ext.security.utils.SecurityConfig.JwtConfig;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;

import com.google.common.collect.ImmutableMap;

/**
 * @author weishao zeng
 * <br/>
 */
public class AjaxLogoutSuccessHandler extends SimpleUrlLogoutSuccessHandler implements LogoutSuccessHandler, InitializingBean {
	@Autowired
	private SecurityConfig securityConfig;
	private JsonMapper mapper = JsonMapper.IGNORE_NULL;
	private CookieStorer cookieStorer;

	@Override
	public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
			throws IOException, ServletException {
		
		JwtConfig jwtConfig = securityConfig.getJwt();
		if(jwtConfig.isEnabled()){
			StoreContext ctx = StoreContext.builder()
											.authKey(jwtConfig.getAuthKey())
											.request(request)
											.response(response)
											.cookieStorer(cookieStorer)
											.build();
			jwtConfig.getAuthStore().removeToken(ctx);
		}
		
		if(RequestUtils.isAjaxRequest(request)){
	        response.setStatus(HttpServletResponse.SC_OK);
	        
	        String redirectUrl = securityConfig.getAfterLoginUrl();
	        Object data = ImmutableMap.of("redirectUrl", redirectUrl);;
			DataResult<?> rs = DataResults.success("登出成功！")
//											.data(authentication.getPrincipal())
											.data(data)
											.build();
			String text = mapper.toJson(rs);
			
			BrowserMeta meta = RequestUtils.getBrowerMetaByAgent(request);
			//如果是ie某些低版本，必须设置为html，否则会导致json下载
			if(meta.isFuckingBrowser()){
				ResponseUtils.render(response, text, ResponseUtils.HTML_TYPE, true);
			} else {
				ResponseUtils.render(response, text, ResponseUtils.JSON_TYPE, true);
			}
		} else {
			super.handle(request, response, authentication);
		}
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		this.setDefaultTargetUrl(this.securityConfig.getLogoutSuccessUrl());
		this.setAlwaysUseDefaultTargetUrl(this.securityConfig.isAlwaysUseDefaultTargetUrl());
	}
	
	public void setCookieStorer(CookieStorer cookieStorer) {
		this.cookieStorer = cookieStorer;
	}
}

