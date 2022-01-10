package org.onetwo.ext.security.jwt;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.onetwo.common.utils.StringUtils;
import org.onetwo.common.web.utils.RequestUtils;
import org.onetwo.ext.security.utils.CookieStorer;

import lombok.Builder;
import lombok.Data;

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
			/*String value = JwtAuthStores.HEADER.getToken(request, authName);
			if (value!=null) {
				return value;
			}*/
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
	},
	COOKIES_HEADER {
		@Override
		public String getToken(HttpServletRequest request, String authName) {
			String token = JwtAuthStores.COOKIES.getToken(request, authName);
			if (StringUtils.isBlank(token)) {
				token = JwtAuthStores.HEADER.getToken(request, authName);
			}
			return token;
		}
		@Override
		public void saveToken(StoreContext ctx) {
			JwtAuthStores.COOKIES.saveToken(ctx);
			JwtAuthStores.HEADER.saveToken(ctx);
		}
	},
	;

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
