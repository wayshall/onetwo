package org.onetwo.boot.core.web.filter;

import java.io.IOException;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

import com.google.common.collect.Maps;

public class SimpleCorsFilter implements Filter {
	public static final String CORS_FILTER_NAME = "corsFilter";
	private Map<String, String> headers = Maps.newHashMap();

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
		if(headers.isEmpty()){
			httpResponse.setHeader("Access-Control-Allow-Origin", "*");
			httpResponse.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
			httpResponse.setHeader("Access-Control-Max-Age", "3600");
			httpResponse.setHeader("Access-Control-Allow-Headers", "x-requested-with, Content-Type, authorization");
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
