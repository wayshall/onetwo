package org.onetwo.common.web.utils;

import java.io.IOException;
import java.util.Properties;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.mitre.dsmiley.httpproxy.ProxyServlet;
import org.onetwo.common.exception.BaseException;

/**
 * @author wayshall
 * <br/>
 */
@SuppressWarnings("serial")
public class DsmileyWebProxy extends ProxyServlet implements WebProxy {
	
	private Properties config;

	@Override
	protected String getConfigParam(String key) {
		if(config==null){
			return null;
		}
		return config.getProperty(key);
	}
	
	@PostConstruct
	public void afterPropertiesSet() throws Exception {
		this.init(null);
	}

	@Override
	public void doProxy(HttpServletRequest servletRequest, HttpServletResponse servletResponse) {
		try {
			this.service(servletRequest, servletResponse);
		} catch (ServletException | IOException e) {
			throw new BaseException("proxy error: " + e.getMessage(), e);
		}
	}
	
	@PreDestroy
	@Override
	public void destroy(){
		super.destroy();
	}

	public void setDoLog(boolean doLog) {
		this.doLog = doLog;
	}

	public void setDoForwardIP(boolean doForwardIP) {
		this.doForwardIP = doForwardIP;
	}

	public void setDoSendUrlFragment(boolean doSendUrlFragment) {
		this.doSendUrlFragment = doSendUrlFragment;
	}

	public void setDoPreserveHost(boolean doPreserveHost) {
		this.doPreserveHost = doPreserveHost;
	}

	public void setDoPreserveCookies(boolean doPreserveCookies) {
		this.doPreserveCookies = doPreserveCookies;
	}

	public void setDoHandleRedirects(boolean doHandleRedirects) {
		this.doHandleRedirects = doHandleRedirects;
	}

	public void setConnectTimeout(int connectTimeout) {
		this.connectTimeout = connectTimeout;
	}

	public void setTargetUri(String targetUri) {
		this.targetUri = targetUri;
	}

}
