package org.onetwo.boot.core.web.filter;

import java.io.IOException;
import java.util.Map;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletResponse;

/***
 * 
CorsFilter 须在所有filter之前，包括security的filter
否则会抛 No 'Access-Control-Allow-Origin' header is present on the requested resource

jfish:
    corsfilter: 
        enabled: true
 * @author way
 * @deprecated 被 CorsFilterConfiguration 取代
 *
 */
public class CorsFilter implements Filter {
	public static final String CORS_FILTER_NAME = "corsFilter";
	private Map<String, String> headers;

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		HttpServletResponse httpResponse = (HttpServletResponse) response;
		configHeader(httpResponse);
        chain.doFilter(request, httpResponse);
	}
	
	protected void configHeader(HttpServletResponse httpResponse){
		if(headers==null){
			httpResponse.setHeader("Access-Control-Allow-Methods", "POST, PUT, GET, OPTIONS, DELETE, HEAD");
			//设置预检的有效时间的，单位是秒
			httpResponse.setHeader("Access-Control-Max-Age", "3600");
			httpResponse.setHeader("Access-Control-Allow-Headers", "x-requested-with, Content-Type, authorization");
			
			//1. 允许接收从任何来源发来的请求
			httpResponse.setHeader("Access-Control-Allow-Origin", "*");
			
			//2. 跨越cookies，同时客户端需要设置: RestangularProvider.setDefaultHttpFields({withCredentials: true});
			/*httpResponse.setHeader("Access-Control-Allow-Origin", "http://localhost:8100");
			httpResponse.setHeader("Access-Control-Allow-Credentials", "true");*/
			httpResponse.setHeader("Access-Control-Allow-Credentials", "true");
		}else{
			headers.entrySet().forEach(e->{
				httpResponse.setHeader(e.getKey(), e.getValue());
			});
		}
	}
	
	

	public Map<String, String> getHeaders() {
		return headers;
	}

	public void setHeaders(Map<String, String> headers) {
		this.headers = headers;
	}

	@Override
	public void destroy() {
	}
	
	

}
