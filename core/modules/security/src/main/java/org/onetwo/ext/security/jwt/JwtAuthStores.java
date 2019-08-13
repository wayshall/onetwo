package org.onetwo.ext.security.jwt;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lombok.Builder;
import lombok.Data;

import org.onetwo.common.web.utils.RequestUtils;
import org.onetwo.ext.security.utils.CookieStorer;

/**
 * @author wayshall
 * <br/>
 */
public enum JwtAuthStores {
	HEADER{
		@Override
		public String getToken(HttpServletRequest request, String authName) {
			return request.getHeader(authName);
		}
		@Override
		public void saveToken(StoreContext ctx) {
			ctx.getResponse().addHeader(ctx.getAuthKey(), ctx.getToken().getToken());
		}
	},
	COOKIES{
		@Override
		public String getToken(HttpServletRequest request, String authName) {
			String value = JwtAuthStores.HEADER.getToken(request, authName);
			if (value!=null) {
				return value;
			}
			return RequestUtils.getCookieValue(request, authName);
		}
		@Override
		public void saveToken(StoreContext ctx) {
			ctx.getCookieStorer().clear(ctx.getRequest(), ctx.getResponse(), ctx.getAuthKey());
			ctx.getCookieStorer().save(ctx.getRequest(), ctx.getResponse(), ctx.getAuthKey(), ctx.getToken().getToken());
		}
	},
	PARAMETER{
		@Override
		public String getToken(HttpServletRequest request, String authName) {
			return request.getParameter(authName);
		}
		@Override
		public void saveToken(StoreContext ctx) {
			ctx.getResponse().addHeader(ctx.getAuthKey(), ctx.getToken().getToken());
		}
	};

	abstract public String getToken(HttpServletRequest request, String authName);
	abstract public void saveToken(StoreContext ctx);
	
	@Data
	@Builder
	public static class StoreContext {
		private HttpServletRequest request;
		private HttpServletResponse response;
		private String authKey;
		private JwtSecurityTokenInfo token;
		private CookieStorer cookieStorer;
	}

}
