package org.onetwo.common.web.subdomain;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.onetwo.common.log.JFishLoggerFactory;
import org.onetwo.common.spring.SpringApplication;
import org.onetwo.common.web.filter.IgnoreFiler;
import org.slf4j.Logger;


/***
 * 处理二级域名的过滤器
 * @author weishao
 *
 */
public class SubDomainFilter extends IgnoreFiler{
	
	protected Logger logger = JFishLoggerFactory.getLogger(SubDomainFilter.class);
	public static final String CURRENT_SUB_DOMAIN = "currentSubDomain";
	
	private SubdomainProcessor processor;
	
	public void initApplication(FilterConfig config){
		super.initApplication(config);
		this.processor = SpringApplication.getInstance().getBean(SubdomainProcessor.class, SubdomainProcessor.BEAN_NAME);
		this.processor.readConfig();
	}

	@Override
	public void doFilterInternal(ServletRequest req, ServletResponse res, FilterChain filterChain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest)req;
		String value = request.getParameter("subdomain");
		if("reload-config".equals(value)){
			processor.readConfig();
		}
		else if("reload-subdomain".equals(value)){
			processor.getSubdomainMapping().readSubdomainInfos();
		}
		processor.process(request, (HttpServletResponse)res, filterChain);
	}

}
