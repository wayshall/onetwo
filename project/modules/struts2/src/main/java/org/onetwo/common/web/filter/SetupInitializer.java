package org.onetwo.common.web.filter;

import javax.servlet.FilterConfig;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.onetwo.common.web.s2.tag.webtag.TemplateTagManagerFactory;
import org.onetwo.common.web.utils.SessionUtils;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

@Component
public class SetupInitializer extends  FilterInitializerAdapter implements Ordered {
	
	public void onInit(FilterConfig config){
		TemplateTagManagerFactory.setup();
	}
	
	public void onFilter(HttpServletRequest request, HttpServletResponse response){
		SessionUtils.setTestSiteInfo(request);
	}
	
	public int getOrder(){
		return 1;
	}
	
}
