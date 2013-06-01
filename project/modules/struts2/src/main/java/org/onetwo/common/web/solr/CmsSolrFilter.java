package org.onetwo.common.web.solr;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.solr.servlet.SolrDispatchFilter;
import org.onetwo.common.utils.UserDetail;
import org.onetwo.common.web.config.SiteConfig;
import org.onetwo.common.web.filter.IgnoreFiler;
import org.onetwo.common.web.utils.SessionUtils;

public class CmsSolrFilter extends SolrDispatchFilter {
	protected Logger logger = Logger.getLogger(IgnoreFiler.class);
	
	private boolean setupSolr = false;

	@Override
	public void init(FilterConfig config) throws ServletException {
		String solrHome = SiteConfig.getInstance().getSolrHome();
		this.setupSolr = StringUtils.isBlank(solrHome)?false:true;
		if(!setupSolr)
			return ;
		System.setProperty(SiteConfig.SOLR_HOME, solrHome);
		if(logger.isInfoEnabled())
			logger.info("set solr.home: " + solrHome);
		
		super.init(config);
		SolrContext.init(cores, parsers);
	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		if(!setupSolr){
			chain.doFilter(request, response);
			return ;
		}
		
		HttpServletRequest req = (HttpServletRequest) request;
        String path = req.getServletPath();
        if( req.getPathInfo() != null ) {
          path += req.getPathInfo();
        }
        
        if(path.startsWith("/solr/") || path.startsWith("/admin/")){
        	boolean isPass = checkUser(req);
        	if(!isPass)
        		throw new RuntimeException("没有权限");
        }
        
		if(!SolrContext.isInit()){
			SolrContext.init(cores, parsers);
		}
		super.doFilter(request, response, chain);
	}
	
	protected boolean checkUser(HttpServletRequest req){
    	UserDetail user = getUserDetail(req);
    	if(user!=null)
    		return true;
    	
		String clientToken = req.getHeader("solr-token");
		String configToken = SiteConfig.getInstance().getProperty("solr.authentic.token");
		if(StringUtils.isNotBlank(configToken) && configToken.equals(clientToken))
			return true;
		
    	return false;
	}
	
	protected UserDetail getUserDetail(HttpServletRequest req){
    	UserDetail user = SessionUtils.getUserDetail(req.getSession(), UserDetail.USER_DETAIL_KEY);
    	return user;
	}
	
}
