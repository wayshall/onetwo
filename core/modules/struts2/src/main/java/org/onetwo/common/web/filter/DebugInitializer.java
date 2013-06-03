package org.onetwo.common.web.filter;

import javax.servlet.FilterConfig;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.onetwo.common.web.config.SiteConfig;
import org.springframework.stereotype.Component;

@Component
public class DebugInitializer extends FilterInitializerAdapter {
	
	public void onInit(FilterConfig config){
	}
	
	public void onFilter(HttpServletRequest request, HttpServletResponse response){
		if("reload".equals(request.getParameter("siteConfig"))){
			SiteConfig.getInstance().reload();
		}
	}
	
	public int getOrder(){
		return 2;
	}
	
}
