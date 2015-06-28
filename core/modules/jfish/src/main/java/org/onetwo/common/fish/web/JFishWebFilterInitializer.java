package org.onetwo.common.fish.web;

import javax.servlet.Filter;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import org.onetwo.common.fish.JFishUtils;
import org.onetwo.common.web.config.BaseSiteConfig;
import org.onetwo.common.web.init.CommonWebFilterInitializer;
import org.springframework.core.annotation.Order;
import org.springframework.web.WebApplicationInitializer;

@Order(JFishUtils.WEBAPP_INITIALIZER_ORDER+5)
public class JFishWebFilterInitializer extends CommonWebFilterInitializer implements WebApplicationInitializer {

	@Override
	public void onStartup(ServletContext servletContext) throws ServletException {
		if(!BaseSiteConfig.getInstance().isStartupByInitializer()){
			return ;
		}
		
		onServletContextStartup(servletContext);
		
	}


	protected void registeredInitFilter(ServletContext servletContext, Class<? extends Filter> initFilterClass){
		super.registeredInitFilter(servletContext, JFishInitFilter.class);
	}
	
	/*@Override
    public Class<? extends Filter> getInitFilterClass() {
	    return JFishInitFilter.class;
    }*/

}
