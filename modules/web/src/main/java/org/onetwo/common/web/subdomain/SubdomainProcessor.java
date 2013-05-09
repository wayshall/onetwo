package org.onetwo.common.web.subdomain;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/****
 * 实际处理二级域名的业务bean
 * @author weishao
 *
 */
public interface SubdomainProcessor {
	
	public String BEAN_NAME = "subdomainProcessor";
	SubdomainMapping getSubdomainMapping();
	public void readConfig();
	public void process(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)throws IOException, ServletException;

}
